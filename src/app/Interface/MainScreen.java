package app.Interface;

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
        panelMain = new JPanel(new GridBagLayout());
        despensaScreen = new DespensaScreen(this);
        receitasScreen = new ReceitasScreen(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelMain.add(btnDespensa, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelMain.add(btnReceitas, gbc);

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

    public void init() {
        throw new UnsupportedOperationException("Unimplemented method 'init'");
    }
}
