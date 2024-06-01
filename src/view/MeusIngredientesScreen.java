package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MeusIngredientesScreen extends JPanel {
    private MainScreen mainScreen;

    public MeusIngredientesScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Meus Ingredientes", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        JTextArea ingredientesTextArea = new JTextArea(10, 30);
        ingredientesTextArea.setEditable(false); 

        // lógica para obter a lista de ingredientes do usuário

        JScrollPane scrollPane = new JScrollPane(ingredientesTextArea);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnVoltar = new JButton("Voltar");
        bottomPanel.add(btnVoltar);

        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnVoltar.addActionListener(e -> mainScreen.showDespensa());
    }
}

