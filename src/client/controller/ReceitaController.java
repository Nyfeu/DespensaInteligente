package client.controller;

import client.facade.ClientFacade;
import client.model.builder.IngredienteBuilder;
import client.model.builder.ReceitaBuilder;
import shared.entities.Ingrediente;
import shared.entities.Receita;
import client.model.utils.Authenticator;
import client.view.ReceitaView;
import client.view.utils.LanguageManager;
import client.view.utils.Validator;
import shared.enums.Attributes;
import shared.enums.Command;
import shared.enums.Entity;
import shared.enums.Strategy;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.*;

public class ReceitaController {

    private ReceitaView receitaView;
    private List<Ingrediente> ingredientes;
    private static ResourceBundle bn;

    public ReceitaController(ReceitaView receitaView) {
        this.receitaView = receitaView;
        bn = LanguageManager.getInstance().getResourceBundle();

        if (receitaView.isNewRecipe()) this.ingredientes = new ArrayList<>();
        else this.ingredientes = receitaView.getReceita().getIngredientes();

        initButtonListeners();
    }

    private void initButtonListeners() {

        receitaView.addCancelarButtonActionListener(e -> receitaView.dispose());

        receitaView.addPublicarButtonActionListener(e -> {

            if (!receitaView.isNewRecipe()) {

                Receita receita = receitaView.getReceita();

                receita.setTitulo(receitaView.getTxtTitulo());
                receita.setDescricao(receitaView.getTxtDescricao());
                receita.setModoPreparo(receitaView.getTxtModoPreparo());
                receita.setTempoPreparo(0.0);
                receita.setIngredientes(receitaView.getListaIngredientes());

                Map<String, Object> args = new HashMap<>();
                args.put(Attributes.RECEITA.getDescription(), receita);

                ClientFacade.sendRequest(Entity.RECEITA, Command.UPDATE, args, responsePacket -> {

                    receitaView.getMainView().getMainViewController().setFilterStrategy(Strategy.PAGE);
                    receitaView.getMainView().getMainViewController().updateReceitasList(0, new HashMap<>());
                    receitaView.dispose();
                    receitaView.getReceitaDetalhesView().dispose();

                });

            } else {

                ReceitaBuilder receitaBuilder = new ReceitaBuilder();

                receitaBuilder.titulo(receitaView.getTxtTitulo())
                        .descricao(receitaView.getTxtDescricao())
                        .instrucoes(receitaView.getTxtModoPreparo())
                        .tempoPreparo(0.0)
                        .ingredientes(ingredientes)
                        .emailAutor(Authenticator.getAuthenticatedUser().getEmail());

                Receita receita = receitaBuilder.build();

                Map<String, Object> args = new HashMap<>();
                args.put(Attributes.RECEITA.getDescription(), receita);

                ClientFacade.sendRequest(Entity.RECEITA, Command.CREATE, args, responsePacket -> {

                    receitaView.getMainView().getMainViewController().setFilterStrategy(Strategy.PAGE);
                    receitaView.getMainView().getMainViewController().updateReceitasList(0, new HashMap<>());
                    receitaView.dispose();

                });

            }

        });

        receitaView.addAdicionarButtonActionListener(e -> {

            IngredienteBuilder ingredienteBuilder = new IngredienteBuilder();

            String nome = receitaView.getTxtNome();

            Validator.verifyIngrediente(nome, receitaView).thenAccept(continuar -> {

                if (!continuar) return;

                ingredienteBuilder.nome(nome);
                ingredienteBuilder.quantidade(Integer.parseInt(receitaView.getTxtQuantidade()));

                ingredientes.add(ingredienteBuilder.build());

                receitaView.setListaIngredientesData(ingredientes);

                receitaView.setTxtNome(bn.getString("main.receita.botao.publicar.nomeingrediente"));
                receitaView.setTxtQuantidade(bn.getString("main.receita.botao.publicar.quantidadeingrediente"));

            });

        });

        receitaView.addNomeFocusListener(new FocusListener() {

            private static String hold_string = bn.getString("main.receita.botao.publicar.nomeingrediente");

            @Override
            public void focusGained(FocusEvent e) {
                if (receitaView.getTxtNome().equals(hold_string)) {
                    receitaView.setTxtNome("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (receitaView.getTxtNome().isEmpty()) {
                    receitaView.setTxtNome(hold_string);
                }
            }
        });

        receitaView.addQuantidadeFocusListener(new FocusListener() {

            private static String hold_string = bn.getString("main.receita.botao.publicar.quantidadeingrediente");

            @Override
            public void focusGained(FocusEvent e) {
                if (receitaView.getTxtQuantidade().equals(hold_string)) {
                    receitaView.setTxtQuantidade("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (receitaView.getTxtQuantidade().isEmpty()) {
                    receitaView.setTxtQuantidade(hold_string);
                }
            }
        });
    }

}