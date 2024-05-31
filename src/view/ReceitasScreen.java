package view;

import javax.swing.*;
import java.awt.*;

public class ReceitasScreen extends JPanel {
    private JButton btnVoltar;
    private MainScreen mainScreen;

    public ReceitasScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
        btnVoltar = new JButton("Voltar");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(btnVoltar, gbc);

        btnVoltar.addActionListener(e -> mainScreen.showMain());
    }
}


