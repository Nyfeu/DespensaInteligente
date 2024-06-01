package view;

import view.utils.viewUtils;

import javax.swing.*;
import java.awt.*;

public class ReceitasScreen extends JPanel {
    private JButton btnVoltar;
    private MainScreen mainScreen;

    public ReceitasScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        btnVoltar = new JButton("Voltar");

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel lblTitle = viewUtils.createTitleLabel("Despensa Inteligente");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        JLabel lblSubtitle = new JLabel("Receitas", JLabel.CENTER);
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 18));
        titlePanel.add(lblTitle, BorderLayout.NORTH);
        titlePanel.add(lblSubtitle, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnVoltar, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(btnVoltar);

        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnVoltar.addActionListener(e -> mainScreen.showMain());
    }
}




