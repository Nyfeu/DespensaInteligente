package view;

import controller.IngredienteController;
import model.utils.DateParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;

public class IngredienteView extends JDialog {

    private JTextField txtNome, txtQuantidade, txtDataValidade;
    private JButton btnAdicionar, btnCancelar;
    private MainView mainView;
    private IngredienteController ingredienteController;

    public IngredienteView(MainView mainView) {
        super(mainView, "Adicionar Ingrediente", true);
        this.mainView = mainView;
        initComponents();
        setLocationRelativeTo(mainView);

    }

    private void initComponents() {

        // Painéis
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JPanel btnPanel = new JPanel();

        // Labels
        JLabel lblNome = new JLabel("Nome:");
        JLabel lblQuantidade = new JLabel("Quantidade:");
        JLabel lblDataValidade = new JLabel("Data de Validade (opcional):");

        // TextFields
        txtNome = new JTextField(20);
        txtQuantidade = new JTextField(10);
        txtDataValidade = new JTextField(10);

        // Buttons
        btnAdicionar = new JButton("Adicionar");
        btnCancelar = new JButton("Cancelar");

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

        ingredienteController = new IngredienteController(this);
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

    public MainView getMainView() {
        return mainView;
    }

}