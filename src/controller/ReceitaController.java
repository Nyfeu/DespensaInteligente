package controller;

import view.ReceitaView;

public class ReceitaController {

    private ReceitaView receitaView;

    public ReceitaController(ReceitaView receitaView) {
        this.receitaView = receitaView;
        initButtonListeners();
    }

    private void initButtonListeners() {

        receitaView.addCancelarButtonActionListener(e -> receitaView.dispose());

        receitaView.addAdicionarButtonActionListener(e -> receitaView.dispose());

    }

}
