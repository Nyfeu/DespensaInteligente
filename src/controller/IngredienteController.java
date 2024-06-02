package controller;

import model.dao.DAOFactory;
import model.dao.interfaces.UsuarioDao;
import model.entities.Ingrediente;
import model.entities.Usuario;
import model.utils.Authenticator;
import view.IngredienteView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class IngredienteController {

    private IngredienteView ingredienteView;
    private boolean isUpdate;

    public IngredienteController(IngredienteView ingredienteView, boolean isUpdate) {
        this.ingredienteView = ingredienteView;
        this.isUpdate = isUpdate;
        initButtonListeners();
    }

    private void initButtonListeners() {

        ingredienteView.addCancelarButtonActionListener(e -> ingredienteView.dispose());

        if (!isUpdate) {

            ingredienteView.addAdicionarButtonActionListener(e -> {

                String nome = ingredienteView.getTxtNome();
                int quantidade = ingredienteView.getQuantidade();
                try {
                    Date dataValidade = ingredienteView.getData();
                    Ingrediente ingrediente = new Ingrediente(nome, 0, dataValidade, quantidade);
                    Authenticator.getAuthenticatedUser().addIngredienteDespensa(ingrediente);
                    UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();
                    Usuario usuario = Authenticator.getAuthenticatedUser();
                    usuarioDao.update(usuario);
                    ArrayList<Ingrediente> novaDespensa = Authenticator.getAuthenticatedUser().getDespensa();
                    ingredienteView.getMainView().setListaDespensaData(novaDespensa);
                    ingredienteView.dispose();
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

            });

        } else {

            ingredienteView.addAdicionarButtonActionListener(e -> {

                Ingrediente ingrediente = ingredienteView.getIngrediente();

                UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();
                Usuario usuario = Authenticator.getAuthenticatedUser();
                ArrayList<Ingrediente> novaDespensa = usuario.getDespensa();
                int index = novaDespensa.indexOf(ingrediente);
                novaDespensa.remove(ingrediente);

                try {

                    Date dataValidade = ingredienteView.getData();
                    ingrediente.setValidade(dataValidade);
                    ingrediente.setQuantidade(ingredienteView.getQuantidade());
                    novaDespensa.add(index, ingrediente);
                    usuario.setDespensa(novaDespensa);
                    usuarioDao.update(usuario);
                    ingredienteView.getMainView().setListaDespensaData(novaDespensa);
                    ingredienteView.dispose();

                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

            });

        }

    }

}
