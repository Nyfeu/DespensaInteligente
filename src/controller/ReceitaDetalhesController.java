package controller;

import model.entities.Receita;
import model.utils.Authenticator;
import view.ReceitaDetalhesView;
import view.ReceitaView;
import view.MainView;

import java.util.ResourceBundle;

public class ReceitaDetalhesController {
    private ReceitaDetalhesView detalhesView;
    private Receita receita;
    private ResourceBundle bn;

    public ReceitaDetalhesController(ReceitaDetalhesView detalhesView, Receita receita, ResourceBundle bn) {
        this.detalhesView = detalhesView;
        this.receita = receita;
        this.bn = bn;
        initButtonListeners();
    }

    private void initButtonListeners() {
        detalhesView.addEditarButtonActionListener(e -> abrirEdicaoReceita());
            
        detalhesView.addExportarButtonActionListener(e -> exportarReceita());

        detalhesView.addVoltarButtonActionListener(e -> detalhesView.dispose());
    }

    private void abrirEdicaoReceita() {
        ReceitaView receitaView = new ReceitaView((MainView) detalhesView.getParent(), receita, bn);
        receitaView.setVisible(true);

        detalhesView.atualizarDadosReceita(receita);
    }

    private void exportarReceita() {
        detalhesView.exportarReceita(receita);
    }
}
