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

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(btnMeusIngredientes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(btnAdicionarIngredientes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(btnVoltar, gbc);

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

