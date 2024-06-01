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
    private AdicionarIngredientesScreen adicionarIngredientesScreen;
    private MeusIngredientesScreen meusIngredientesScreen;

    public MainScreen() {
        btnDespensa = new JButton("Minha Despensa");
        btnReceitas = new JButton("Receitas");
        panelMain = new JPanel(new BorderLayout());
        despensaScreen = new DespensaScreen(this);
        receitasScreen = new ReceitasScreen(this);
        adicionarIngredientesScreen = new AdicionarIngredientesScreen(this);
        meusIngredientesScreen = new MeusIngredientesScreen(this);

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("Despensa Inteligente", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(lblTitle, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(btnDespensa, gbc);

        gbc.gridx = 1;
        buttonPanel.add(btnReceitas, gbc);

        panelMain.add(titlePanel, BorderLayout.NORTH);
        panelMain.add(buttonPanel, BorderLayout.CENTER);

        btnDespensa.addActionListener(e -> showDespensa());
        btnReceitas.addActionListener(e -> showReceitas());

        setContentPane(panelMain);
        setTitle("Despensa Inteligente");
        setSize(400, 300);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void showDespensa() {
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

    public void showAdicionarIngredientes() {
        setContentPane(adicionarIngredientesScreen);
        invalidate();
        validate();
    }

    public void showMeusIngredientes() {
        setContentPane(meusIngredientesScreen);
        invalidate();
        validate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainScreen::new);
    }
}




