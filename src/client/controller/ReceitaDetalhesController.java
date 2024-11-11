package client.controller;

import shared.entities.Receita;
import client.view.ReceitaDetalhesView;
import client.view.ReceitaView;
import client.view.MainView;

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
            
        detalhesView.addExportarMenuItemActionListener(e -> exportarReceita());

        detalhesView.addExportarPDFMenuItemButtonActionListener(e -> exportarReceitaPDF());

        detalhesView.addVoltarButtonActionListener(e -> detalhesView.dispose());
    }

    private void abrirEdicaoReceita() {
        ReceitaView receitaView = new ReceitaView((MainView) detalhesView.getParent(), receita, bn, false);
        receitaView.setVisible(true);

        detalhesView.atualizarDadosReceita(receita);
    }

    private void exportarReceita() {
        detalhesView.exportarReceita(receita);
    }

    private void exportarReceitaPDF() { detalhesView.exportarReceitaParaPDF(receita); }
}
