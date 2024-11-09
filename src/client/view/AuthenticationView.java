package client.view;

import client.controller.AuthenticationController;
import client.view.utils.LanguageManager;
import client.view.utils.ViewUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;
import java.util.Locale;

public class AuthenticationView extends JFrame {

    // Layout:
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // TextFields:
    private JTextField nome, email1, email2;
    private JPasswordField password1, password2;

    // Labels and Buttons
    private JLabel email_login, password_login,titulo_login, email_registro, password_registro,nome_registro, titulo_registro;
    private JButton botao_login, botao_registro, submit;

    // Authentication
    private boolean isLogin = false;

    // Language Management
    private JMenu emojiMenu;
    private JLabel idiomaLabel;
    private LanguageManager languageManager;

    public AuthenticationView() {
        configurarJanela();
        inicializarComponentes();
        configurarMenuIdioma();
        configurarPainelPrincipal();
        configurarBotaoPainel();
        configurarControlador();
        atualizarIdioma(languageManager.getCurrentLocale());
    }

    private void configurarJanela() {
        setTitle("Autenticação");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        languageManager = LanguageManager.getInstance();
        idiomaLabel = new JLabel(languageManager.getLanguageDisplayName(languageManager.getCurrentLocale()));

        nome = new JTextField(30);
        email1 = new JTextField(30);
        password1 = new JPasswordField(30);
        email2 = new JTextField(30);
        password2 = new JPasswordField(30);

        botao_registro = new JButton("Registrar-se");
        botao_login = new JButton("Login");
        submit = new JButton("Submeter!");
    }

    private void configurarMenuIdioma() {
        setupLanguageMenu(); // Configura as opções de idioma no menu de seleção

        JMenuBar barra = new JMenuBar();
        barra.add(Box.createHorizontalGlue());
        barra.add(idiomaLabel);
        barra.add(emojiMenu);
        setJMenuBar(barra);
    }

    private void configurarPainelPrincipal() {
        JPanel registerPanel = createRegistrationPanel();
        JPanel loginPanel = createLoginPanel();

        cardPanel.add(registerPanel, "Registrar-se");
        cardPanel.add(loginPanel, "Login");
        add(cardPanel, BorderLayout.CENTER);
    }

    private void configurarBotaoPainel() {
        JPanel buttonPanel = new JPanel();
        ViewUtils.configureButton(botao_registro);
        ViewUtils.configureButton(botao_login);
        ViewUtils.configureButton(submit);

        buttonPanel.setBackground(Color.GRAY);
        buttonPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        botao_registro.addActionListener(e -> switchPanel("Registrar-se"));
        botao_login.addActionListener(e -> switchPanel("Login"));

        buttonPanel.add(botao_registro);
        buttonPanel.add(botao_login);
        buttonPanel.add(submit);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void configurarControlador() {
        new AuthenticationController(this);
    }

    private void setupLanguageMenu() {

        // Menu com o emoji 🔽
        emojiMenu = new JMenu("🔽");
        emojiMenu.add(createLanguageMenuItem("Português", "/client/resources/images/pt_BR.png", new Locale("pt", "BR")));
        emojiMenu.add(createLanguageMenuItem("English", "/client/resources/images/en_US.png", Locale.US));
        emojiMenu.add(createLanguageMenuItem("Italiano", "/client/resources/images/it_IT.png", new Locale("it", "IT")));
        emojiMenu.add(createLanguageMenuItem("Español", "/client/resources/images/es_ES.png", new Locale("es", "ES")));
        emojiMenu.add(createLanguageMenuItem("Français", "/client/resources/images/fr_FR.png", new Locale("fr", "FR")));

    }
    
    private JPanel createRegistrationPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        titulo_registro = ViewUtils.createTitleLabel("Registro");

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Adiciona os componentes ao painel com o GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        nome_registro = new JLabel("Nome:");
        formPanel.add(nome_registro, gbc);
        gbc.gridy++;
        email_registro = new JLabel("Email:");
        formPanel.add(email_registro, gbc);
        gbc.gridy++;
        password_registro = new JLabel("Senha:");
        formPanel.add(password_registro, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(nome, gbc);
        gbc.gridy++;
        formPanel.add(email1, gbc);
        gbc.gridy++;
        formPanel.add(password1, gbc);

        panel.add(titulo_registro, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        return panel;

    }

    private JPanel createLoginPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        titulo_login = ViewUtils.createTitleLabel("Login");

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Adiciona os componentes ao painel com o GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        email_login = new JLabel("Email:");
        password_login = new JLabel("Senha:");
        formPanel.add(email_login, gbc);
        gbc.gridy++;
        formPanel.add(password_login, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(email2, gbc);
        gbc.gridy++;
        formPanel.add(password2, gbc);

        panel.add(titulo_login, BorderLayout.NORTH);
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

    public String getPassword_login() {
        if (isLogin) return String.valueOf(password2.getPassword());
        return String.valueOf(password1.getPassword());
    }

    public String getEmail_login() {
        if (isLogin) return email2.getText();
        return email1.getText();
    }

    public JButton getSubmitButton() {
        return submit;
    }

    public boolean isLogin() {
        return isLogin;
    }

    private JMenuItem createLanguageMenuItem(String languageName, String iconPath, Locale locale) {

        JMenuItem menuItem = new JMenuItem(languageName);

        // Carregando ícone
        URL iconUrl = getClass().getClassLoader().getResource(iconPath.substring(1));
        if (iconUrl != null) menuItem.setIcon(new ImageIcon(iconUrl));
        else System.out.println("Icon not found for " + languageName);

        menuItem.addActionListener(e -> atualizarIdioma(locale));
        return menuItem;

    }

    private void atualizarIdioma(Locale locale) {

        languageManager.changeLanguage(locale, idiomaLabel);

        setTitle(languageManager.getResourceBundle().getString("autenticacao.titulo"));
        if (submit != null) submit.setText(languageManager.getResourceBundle().getString("autenticacao.botao.submeter"));
        if (email_login != null) email_login.setText(languageManager.getResourceBundle().getString("autenticacao.login.email"));
        if (password_login != null) password_login.setText(languageManager.getResourceBundle().getString("autenticacao.login.senha"));
        if (titulo_login != null) titulo_login.setText(languageManager.getResourceBundle().getString("autenticacao.login.label.titulo"));
        if (password_registro != null) password_registro.setText(languageManager.getResourceBundle().getString("autenticacao.registro.senha"));
        if (nome_registro != null) nome_registro.setText(languageManager.getResourceBundle().getString("autenticacao.registro.nome"));
        if (email_registro != null) email_registro.setText(languageManager.getResourceBundle().getString("autenticacao.registro.email"));
        if (titulo_registro != null) titulo_registro.setText(languageManager.getResourceBundle().getString("autenticacao.registro.label.titulo"));
        if (botao_registro != null) botao_registro.setText(languageManager.getResourceBundle().getString("autenticacao.botao.registro"));
        if (botao_login != null) botao_login.setText(languageManager.getResourceBundle().getString("autenticacao.botao.login"));

        revalidate();
        repaint();
        pack();

    }

}