package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdicionarIngredientesScreen extends JPanel {
    private JTextField txtIngredientes;
    private JButton btnAdicionar;
    private JButton btnVoltar;
    private MainScreen mainScreen;

    public AdicionarIngredientesScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        txtIngredientes = new JTextField(30);
        btnAdicionar = new JButton("Adicionar");
        btnVoltar = new JButton("Voltar");

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Adicionar Ingredientes", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.add(new JLabel("Nome do Ingrediente:"));
        inputPanel.add(txtIngredientes);
        inputPanel.add(btnAdicionar);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(btnVoltar);

        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // lógica para adicionar o ingrediente
                String nomeIngrediente = txtIngredientes.getText();
                // lógica para adicionar o ingrediente à lista de ingredientes
                JOptionPane.showMessageDialog(AdicionarIngredientesScreen.this, "Ingrediente adicionado: " + nomeIngrediente);
            }
        });

        btnVoltar.addActionListener(e -> mainScreen.showDespensa());
    }
}
