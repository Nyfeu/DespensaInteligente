package client.controller;

import client.facade.ClientFacade;
import client.model.builder.IngredienteBuilder;
import client.view.utils.LanguageManager;
import shared.entities.Ingrediente;
import shared.entities.Usuario;
import client.model.utils.Authenticator;
import client.view.IngredienteView;
import client.view.utils.Validator;
import shared.enums.Attributes;
import shared.enums.Command;
import shared.enums.Entity;
import shared.enums.Status;

import javax.swing.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

                Validator.verifyIngrediente(nome, ingredienteView).thenAccept(continuar -> {

                    if (continuar) {

                        int quantidade = ingredienteView.getQuantidade();
                        Date dataValidade;
                        try {
                            dataValidade = ingredienteView.getData();
                        } catch (ParseException ex) {
                            throw new RuntimeException(ex);
                        }

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

                        if (index == -1) usuario.addIngredienteDespensa(ingrediente);
                        else {

                            Ingrediente ingredienteEncontrado = novaDespensa.get(index);

                            int novaQuantidade = ingredienteEncontrado.getQuantidade() + ingrediente.getQuantidade();
                            ingredienteEncontrado.setQuantidade(novaQuantidade);

                            Date validade = dataValidade.before(ingredienteEncontrado.getValidade()) ? dataValidade : ingredienteEncontrado.getValidade();
                            ingredienteEncontrado.setValidade(validade);

                            usuario.setDespensa(novaDespensa);

                        }

                        // Envia a solicitação de atualização da despensa via ClientFacade
                        Map<String, Object> args = new HashMap<>();
                        args.put(Attributes.USER.getDescription(), usuario);

                        ClientFacade.sendRequest(Entity.USUARIO, Command.UPDATE, args, responsePacket -> {

                            if (responsePacket.getStatus().equals(Status.SUCCESS)) {

                                ingredienteView.getMainView().setListaDespensaData(novaDespensa);
                                JOptionPane.showMessageDialog(ingredienteView, LanguageManager.getInstance().getResourceBundle().getString("client.controller.ingrediente.sucesso"), LanguageManager.getInstance().getResourceBundle().getString("client.controller.ingrediente.sucesso.titulo"), JOptionPane.INFORMATION_MESSAGE);

                            } else JOptionPane.showMessageDialog(ingredienteView, LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.removeingrediente.erro"), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);

                            ingredienteView.dispose();

                        });

                    }

                });

            });

        } else {

            ingredienteView.addAdicionarButtonActionListener(e -> {

                Ingrediente ingrediente = ingredienteView.getIngrediente();
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

                    // Envia a solicitação de atualização via ClientFacade
                    Map<String, Object> args = new HashMap<>();
                    args.put(Attributes.USER.getDescription(), usuario);

                    ClientFacade.sendRequest(Entity.USUARIO, Command.UPDATE, args, responsePacket -> {
                        if (responsePacket.getStatus().equals(Status.SUCCESS)) {
                            ingredienteView.getMainView().setListaDespensaData(novaDespensa);
                            JOptionPane.showMessageDialog(ingredienteView, LanguageManager.getInstance().getResourceBundle().getString("client.controller.ingrediente.sucesso2"), LanguageManager.getInstance().getResourceBundle().getString("client.controller.ingrediente.sucesso.titulo2"), JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(ingredienteView, LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.removeingrediente.erro"), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                        }
                        ingredienteView.dispose();
                    });

                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

            });

        }

    }

}
