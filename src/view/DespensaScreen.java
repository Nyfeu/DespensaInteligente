package view;

import javax.swing.*;
import java.awt.*;

public class DespensaScreen extends JPanel {
    private JButton btnMeusIngredientes;
    private JButton btnAdicionarIngredientes;
    private JButton btnVoltar;
    private MainScreen mainScreen;

    public DespensaScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        btnMeusIngredientes = new JButton("Meus Ingredientes");
        btnAdicionarIngredientes = new JButton("Adicionar Ingredientes");
        btnVoltar = new JButton("Voltar");

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Despensa Inteligente", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel lblSubtitle = new JLabel("Minha Despensa", JLabel.CENTER);
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        titlePanel.add(lblTitle, BorderLayout.NORTH);
        titlePanel.add(lblSubtitle, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnMeusIngredientes, gbc);

        gbc.gridy = 1;
        buttonPanel.add(btnAdicionarIngredientes, gbc);

        gbc.gridy = 2;
        buttonPanel.add(btnVoltar, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(btnVoltar);

        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnMeusIngredientes.addActionListener(e -> showIngredientes());
        btnAdicionarIngredientes.addActionListener(e -> showAdicionarIngredientes());
        btnVoltar.addActionListener(e -> mainScreen.showMain());
    }

    private void showIngredientes() {
        // Placeholder para mostrar ingredientes
        JOptionPane.showMessageDialog(this, "Mostrar Ingredientes");
    }

    private void showAdicionarIngredientes() {
        // Placeholder para adicionar ingredientes
        JOptionPane.showMessageDialog(this, "Adicionar Ingredientes");
    }
}



