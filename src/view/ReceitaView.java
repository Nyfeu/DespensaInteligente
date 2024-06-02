package view;

import controller.ReceitaController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ReceitaView extends JDialog {

    private JTextField txtTitulo, txtDescricao, txtIngredientes;
    private JButton btnAdicionar, btnCancelar;
    private MainView mainView;
    private ReceitaController receitaController;

    public ReceitaView(MainView mainView) {
        super(mainView, "Adicionar Receita", true);
        this.mainView = mainView;
        initComponents();
        setLocationRelativeTo(mainView);

    }

    private void initComponents() {

        // Painéis
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JPanel btnPanel = new JPanel();

        // Labels
        JLabel lblTitulo = new JLabel("Título:");
        JLabel lblDescricao = new JLabel("Descrição:");

        // TextFields
        txtTitulo = new JTextField(20);
        txtDescricao = new JTextField(10);

        // Buttons
        btnAdicionar = new JButton("Adicionar");
        btnCancelar = new JButton("Cancelar");

        // Adicionando aos paineis
        panel.add(lblTitulo);
        panel.add(txtTitulo);
        panel.add(lblDescricao);
        panel.add(txtDescricao);
        btnPanel.add(btnAdicionar);
        btnPanel.add(btnCancelar);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.PAGE_END);

        receitaController = new ReceitaController(this);
        pack();
        setResizable(false);
    }

    public void addAdicionarButtonActionListener(ActionListener listener) {
        btnAdicionar.addActionListener(listener);
    }

    public void addCancelarButtonActionListener(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }

    public String getTxtTitulo() {
        return txtTitulo.getText();
    }
    public String getTxtDescricao() {
        return txtDescricao.getText();
    }
    public MainView getMainView() {
        return mainView;
    }

}
