package controller;

import model.dao.DAOFactory;
import model.dao.interfaces.UsuarioDao;
import model.entities.Ingrediente;
import model.utils.Authenticator;
import view.IngredienteView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class IngredienteController {

    private IngredienteView ingredienteView;

    public IngredienteController(IngredienteView ingredienteView) {
        this.ingredienteView = ingredienteView;
        initButtonListeners();
    }

    private void initButtonListeners() {

        ingredienteView.addCancelarButtonActionListener(e -> ingredienteView.dispose());

        ingredienteView.addAdicionarButtonActionListener(e -> {

            String nome = ingredienteView.getTxtNome();
            int quantidade = ingredienteView.getQuantidade();
            try {
                Date dataValidade = ingredienteView.getData();
                Ingrediente ingrediente = new Ingrediente(nome,0,dataValidade,quantidade);
                Authenticator.getAuthenticatedUser().addIngredienteDespensa(ingrediente);
                UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();
                usuarioDao.update(Authenticator.getAuthenticatedUser());
                ArrayList<Ingrediente> novaDespensa = Authenticator.getAuthenticatedUser().getDespensa();
                ingredienteView.getMainView().setListaDespensaData(novaDespensa);
                ingredienteView.dispose();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

        });

    }

}
