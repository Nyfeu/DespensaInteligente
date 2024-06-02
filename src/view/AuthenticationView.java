package view;

import controller.AuthenticationController;
import view.utils.ViewUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AuthenticationView extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JTextField nome, email1, email2;
    private JPasswordField password1, password2;
    private JButton submit;
    private boolean isLogin = false;

    public AuthenticationView() {
        // Configurações da janela
        setTitle("Autenticação");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializa o CardLayout e o painel de cartões
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Cria textFields e passwordField
        nome = new JTextField(30);
        email1 = new JTextField(30);
        password1 = new JPasswordField(30);
        email2 = new JTextField(30);
        password2 = new JPasswordField(30);

        // Cria os diferentes painéis
        JPanel registerPanel = createRegistrationPanel();
        JPanel loginPanel = createLoginPanel();

        // Adiciona os painéis ao CardLayout
        cardPanel.add(registerPanel, "Registrar-se");
        cardPanel.add(loginPanel, "Login");

        // Adiciona o painel de cartões à janela
        add(cardPanel, BorderLayout.CENTER);

        // Cria e adiciona os botões de navegação
        JPanel buttonPanel = new JPanel();
        JButton button1 = new JButton("Registrar-se");
        JButton button2 = new JButton("Login");
        submit = new JButton("Submeter!");

        ViewUtils.configureButton(button1);
        ViewUtils.configureButton(button2);
        ViewUtils.configureButton(submit);

        buttonPanel.setBackground(Color.GRAY);
        buttonPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        button1.addActionListener(e -> switchPanel("Registrar-se"));
        button2.addActionListener(e -> switchPanel("Login"));

        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(submit);

        add(buttonPanel, BorderLayout.SOUTH);
        AuthenticationController controller = new AuthenticationController(this);
    }

    private JPanel createRegistrationPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = ViewUtils.createTitleLabel("Registro");

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Adiciona os componentes ao painel com o GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(nome, gbc);
        gbc.gridy++;
        formPanel.add(email1, gbc);
        gbc.gridy++;
        formPanel.add(password1, gbc);

        panel.add(label, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        return panel;

    }

    private JPanel createLoginPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = ViewUtils.createTitleLabel("Login");

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Adiciona os componentes ao painel com o GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridy++;
        formPanel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(email2, gbc);
        gbc.gridy++;
        formPanel.add(password2, gbc);

        panel.add(label, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }

    private void switchPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
        isLogin = panelName.equals("Login");
    }

    public String getNome() {
        return nome.getText();
    }

    public String getPassword() {
        if (isLogin) return String.valueOf(password2.getPassword());
        return String.valueOf(password1.getPassword());
    }

    public String getEmail() {
        if (isLogin) return email2.getText();
        return email1.getText();
    }

    public JButton getSubmitButton() {
        return submit;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthenticationView().setVisible(true));
    }
}
