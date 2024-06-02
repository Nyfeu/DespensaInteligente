package controller;

import model.entities.Ingrediente;
import model.utils.DateParser;
import view.ReceitaView;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.util.ArrayList;

public class ReceitaController {

    private ReceitaView receitaView;

    public ReceitaController(ReceitaView receitaView) {
        this.receitaView = receitaView;
        initButtonListeners();
    }

    private void initButtonListeners() {

        receitaView.addCancelarButtonActionListener(e -> receitaView.dispose());

        receitaView.addPublicarButtonActionListener(e -> {

            /* ----------------------------------------------

                Modificar lógica para publicação de receita!

            ----------------------------------------------- */

            receitaView.dispose();
        });

        receitaView.addAdicionarButtonActionListener(e -> {

            /* ----------------------------------------------

                Modificar lógica de adição de ingredientes!

            ----------------------------------------------- */

            ArrayList<Ingrediente> ingredientes = new ArrayList<>();

            try {
                Ingrediente ingrediente = new Ingrediente("Teste1", 1, DateParser.parseString("02/06/2024"), 2);
                ingredientes.add(ingrediente);
                ingrediente = new Ingrediente("Teste2", 2, DateParser.parseString("02/06/2024"), 3);
                ingredientes.add(ingrediente);
                receitaView.setListaIngredientesData(ingredientes);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

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
