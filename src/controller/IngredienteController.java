package controller;

import model.builder.IngredienteBuilder;
import model.dao.DAOFactory;
import model.dao.interfaces.UsuarioDao;
import model.entities.Ingrediente;
import model.entities.Usuario;
import model.utils.Authenticator;
import view.IngredienteView;
import view.utils.Validator;

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

                boolean continuar = Validator.verifyIngrediente(nome, ingredienteView);

                if (!continuar) return;

                int quantidade = ingredienteView.getQuantidade();
                Date dataValidade;
                try {
                    dataValidade = ingredienteView.getData();
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

                UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();
                Usuario usuario = Authenticator.getAuthenticatedUser();
                ArrayList<Ingrediente> novaDespensa = usuario.getDespensa();

                IngredienteBuilder ingredienteBuilder = new IngredienteBuilder();

                ingredienteBuilder.nome(nome)
                        .validade(dataValidade)
                        .quantidade(quantidade);

                Ingrediente ingrediente = ingredienteBuilder.build();

                ArrayList<String> ingredientesNome = new ArrayList<>();
                for (Ingrediente ingred : novaDespensa) ingredientesNome.add(ingred.getNome());

                int index = ingredientesNome.indexOf(nome);

                if (index == -1) {

                        Authenticator.getAuthenticatedUser().addIngredienteDespensa(ingrediente);
                        usuarioDao.update(usuario);

                } else {

                        Ingrediente ingredienteEncontrado = novaDespensa.get(index);

                        int novaQuantidade = ingredienteEncontrado.getQuantidade() + ingrediente.getQuantidade();
                        ingredienteEncontrado.setQuantidade(novaQuantidade);

                        Date validade = dataValidade.before(ingredienteEncontrado.getValidade()) ? dataValidade : ingredienteEncontrado.getValidade();
                        ingredienteEncontrado.setValidade(validade);

                        usuario.setDespensa(novaDespensa);
                        usuarioDao.update(usuario);

                }

                ingredienteView.getMainView().setListaDespensaData(novaDespensa);
                ingredienteView.dispose();

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
