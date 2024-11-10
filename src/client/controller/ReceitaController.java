package client.controller;

import client.model.builder.IngredienteBuilder;
import client.model.builder.ReceitaBuilder;
import server.dao.DAOFactory;
import server.dao.interfaces.ReceitaDao;
import shared.entities.Ingrediente;
import shared.entities.Receita;
import client.model.utils.Authenticator;
import client.view.ReceitaView;
import client.view.utils.LanguageManager;
import client.view.utils.Validator;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ReceitaController {

    private ReceitaView receitaView;
    private ArrayList<Ingrediente> ingredientes;
    private static ResourceBundle bn;

    public ReceitaController(ReceitaView receitaView) {
        this.receitaView = receitaView;
        bn = LanguageManager.getInstance().getResourceBundle();
        this.ingredientes = new ArrayList<>();
        initButtonListeners();
    }

    private void initButtonListeners() {

        receitaView.addCancelarButtonActionListener(e -> receitaView.dispose());

        receitaView.addPublicarButtonActionListener(e -> {

            ReceitaBuilder receitaBuilder = new ReceitaBuilder();

            receitaBuilder.titulo(receitaView.getTxtTitulo())
                    .descricao(receitaView.getTxtDescricao())
                    .instrucoes(receitaView.getTxtModoPreparo())
                    .tempoPreparo(0.0)
                    .ingredientes(ingredientes)
                    .emailAutor(Authenticator.getAuthenticatedUser().getEmail());

            Receita receita = receitaBuilder.build();


            ReceitaDao receitaDao = DAOFactory.createReceitaDao();
            receitaDao.create(receita);

            receitaView.getMainView().getMainViewController().updateReceitasList(0, new HashMap<>());
            receitaView.dispose();
        });

        receitaView.addAdicionarButtonActionListener(e -> {

            IngredienteBuilder ingredienteBuilder = new IngredienteBuilder();

            String nome = receitaView.getTxtNome();

            boolean continuar = Validator.verifyIngrediente(nome, receitaView);

            if (!continuar) return;

            ingredienteBuilder.nome(nome);
            ingredienteBuilder.quantidade(Integer.parseInt(receitaView.getTxtQuantidade()));

            ingredientes.add(ingredienteBuilder.build());

            receitaView.setListaIngredientesData(ingredientes);

            receitaView.setTxtNome(bn.getString("main.receita.botao.publicar.nomeingrediente"));
            receitaView.setTxtQuantidade(bn.getString("main.receita.botao.publicar.quantidadeingrediente"));

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