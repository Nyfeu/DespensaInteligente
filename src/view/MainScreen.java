package view;

import view.utils.viewUtils;

import javax.swing.*;
import java.awt.*;

public class MainScreen extends JFrame {
    private JButton btnDespensa;
    private JButton btnReceitas;
    private JPanel panelMain;
    private DespensaScreen despensaScreen;
    private ReceitasScreen receitasScreen;

    public MainScreen() {
        btnDespensa = new JButton("Minha Despensa");
        btnReceitas = new JButton("Receitas");
        panelMain = new JPanel(new BorderLayout());
        despensaScreen = new DespensaScreen(this);
        receitasScreen = new ReceitasScreen(this);

        JLabel lblTitle = viewUtils.createTitleLabel("Despensa Inteligente");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        panelMain.add(lblTitle, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnDespensa, gbc);

        gbc.gridx = 1;
        buttonPanel.add(btnReceitas, gbc);

        panelMain.add(buttonPanel, BorderLayout.CENTER);

        btnDespensa.addActionListener(e -> showDespensa());
        btnReceitas.addActionListener(e -> showReceitas());

        setContentPane(panelMain);
        setTitle("Despensa Inteligente");
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the frame on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void showDespensa() {
        setContentPane(despensaScreen);
        invalidate();
        validate();
    }

    private void showReceitas() {
        setContentPane(receitasScreen);
        invalidate();
        validate();
    }

    public void showMain() {
        setContentPane(panelMain);
        invalidate();
        validate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainScreen::new);
    }
}



