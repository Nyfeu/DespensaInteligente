package controller;

import model.builder.IngredienteBuilder;
import model.builder.ReceitaBuilder;
import model.dao.DAOFactory;
import model.dao.interfaces.ReceitaDao;
import model.entities.Ingrediente;
import model.entities.Receita;
import model.utils.Authenticator;
import view.ReceitaView;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class ReceitaController {

    private ReceitaView receitaView;
    private ArrayList<Ingrediente> ingredientes;

    public ReceitaController(ReceitaView receitaView) {
        this.receitaView = receitaView;
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

            receitaView.dispose();
        });

        receitaView.addAdicionarButtonActionListener(e -> {

            IngredienteBuilder ingredienteBuilder = new IngredienteBuilder();

            ingredienteBuilder.nome(receitaView.getTxtNome());
            ingredienteBuilder.quantidade(Integer.parseInt(receitaView.getTxtQuantidade()));

            ingredientes.add(ingredienteBuilder.build());

            receitaView.setListaIngredientesData(ingredientes);

            receitaView.setTxtNome("Nome do ingrediente");
            receitaView.setTxtQuantidade("Quantidade");

        });

        receitaView.addNomeFocusListener(new FocusListener() {

            private static String hold_string = "Nome do ingrediente";

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

            private static String hold_string = "Quantidade";

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
