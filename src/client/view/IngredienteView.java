package client.view;

import client.controller.IngredienteController;
import shared.entities.Ingrediente;
import shared.util.DateParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;

public class IngredienteView extends JDialog {

    private JTextField txtNome, txtQuantidade, txtDataValidade;
    private JButton btnAdicionar, btnCancelar;
    private MainView mainView;
    private IngredienteController ingredienteController;
    private Ingrediente ingrediente;
    private ResourceBundle bn;

    public IngredienteView(MainView mainView, Ingrediente ingrediente, ResourceBundle bn) {
        super(mainView, bn.getString("main.despensa.botao.adicionar.titulo"), true);
        this.mainView = mainView;
        this.ingrediente = ingrediente;
        initComponents(!(ingrediente == null), bn);
        setLocationRelativeTo(mainView);

    }

    private void initComponents(boolean isUpdate, ResourceBundle bn) {

        // Strings
        String nome = isUpdate ? ingrediente.getNome() : "";
        String quantidade = isUpdate ? String.format("%d", ingrediente.getQuantidade()) : "";
        String validade = isUpdate ? DateParser.parseDate(ingrediente.getValidade()) : "";

        // Painéis
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JPanel btnPanel = new JPanel();

        // Labels
        JLabel lblNome = new JLabel(bn.getString("main.despensa.botao.adicionar.nome"));
        JLabel lblQuantidade = new JLabel(bn.getString("main.despensa.botao.adicionar.quantidade"));
        JLabel lblDataValidade = new JLabel(bn.getString("main.despensa.botao.adicionar.datavalidade"));

        // TextFields
        txtNome = new JTextField(nome,20);
        if (isUpdate) txtNome.setEnabled(false);
        txtQuantidade = new JTextField(quantidade,10);
        txtDataValidade = new JTextField(validade,10);

        // Buttons
        btnAdicionar = new JButton(isUpdate ? bn.getString("main.despensa.botao.atualizar") : bn.getString("main.despensa.botao.adicionar.botao.adicionar"));
        btnCancelar = new JButton(bn.getString("main.despensa.botao.adicionar.botao.cancelar"));

        // Adicionando aos paineis
        panel.add(lblNome);
        panel.add(txtNome);
        panel.add(lblQuantidade);
        panel.add(txtQuantidade);
        panel.add(lblDataValidade);
        panel.add(txtDataValidade);
        btnPanel.add(btnAdicionar);
        btnPanel.add(btnCancelar);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.PAGE_END);

        ingredienteController = new IngredienteController(this, isUpdate);
        pack();
        setResizable(false);
    }

    public void addAdicionarButtonActionListener(ActionListener listener) {
        btnAdicionar.addActionListener(listener);
    }

    public void addCancelarButtonActionListener(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }

    public String getTxtNome() {
        return txtNome.getText();
    }

    public int getQuantidade() {
        return Integer.parseInt(txtQuantidade.getText());
    }

    public Date getData() throws ParseException {
        return DateParser.parseString(txtDataValidade.getText());
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public MainView getMainView() {
        return mainView;
    }

}