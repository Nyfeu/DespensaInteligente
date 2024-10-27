package view;

import controller.AuthenticationController;
import view.utils.ViewUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

public class AuthenticationView extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JTextField nome, email1, email2;
    private JPasswordField password1, password2;
    private JButton submit;
    private boolean isLogin = false;
    private JLabel email_login, password_login,titulo_login;
    private JLabel email_registro, password_registro,nome_registro, titulo_registro;
    private JButton botao_login, botao_registro;
    private JMenu alteralinguagem;
    private static ResourceBundle bn;
    private Locale idiomaSelecionado;

    public AuthenticationView() {
        // Configurações da janela
        setTitle("Autenticação");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializa o CardLayout e o painel de cartões
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        alteralinguagem = new JMenu("Linguagem");
        JMenuItem port = new JMenuItem("Português");
        JMenuItem eng = new JMenuItem("English");
        JMenuItem ita = new JMenuItem("Italiano");
        JMenuItem esp = new JMenuItem("Español");
        JMenuItem fra = new JMenuItem("Français");
        alteralinguagem.add(port);
        alteralinguagem.add(eng);
        alteralinguagem.add(ita);
        alteralinguagem.add(esp);
        alteralinguagem.add(fra);

        JMenuBar barra = new JMenuBar();
        setJMenuBar(barra);
        barra.add(alteralinguagem);

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
        botao_registro = new JButton("Registrar-se");
        botao_login = new JButton("Login");
        submit = new JButton("Submeter!");

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
        AuthenticationController controller = new AuthenticationController(this);

        atualizarIdioma(new Locale("pt", "BR"));
        port.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarIdioma(new Locale("pt", "BR"));
            }
        });

        eng.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarIdioma(Locale.US);
            }
        });

        ita.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarIdioma(new Locale("it", "IT"));
            }
        });

        fra.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarIdioma(new Locale("fr", "FR"));
            }
        });

        esp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarIdioma(new Locale("es", "ES"));
            }
        });


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

    public static ResourceBundle getResourceBundle() {
        return bn;
    }

    private void atualizarIdioma(Locale locale) {
        bn = ResourceBundle.getBundle("view.DespensaInteligente", locale);
        idiomaSelecionado = locale;

        setTitle(bn.getString("autenticacao.titulo"));
        submit.setText(bn.getString("autenticacao.botao.submeter"));
        email_login.setText(bn.getString("autenticacao.login.email"));
        password_login.setText(bn.getString("autenticacao.login.senha"));
        titulo_login.setText(bn.getString("autenticacao.login.label.titulo"));
        password_registro.setText(bn.getString("autenticacao.registro.senha"));
        nome_registro.setText(bn.getString("autenticacao.registro.nome"));
        email_registro.setText(bn.getString("autenticacao.registro.email"));
        titulo_registro.setText(bn.getString("autenticacao.registro.label.titulo"));
        botao_registro.setText(bn.getString("autenticacao.botao.registro"));
        botao_login.setText(bn.getString("autenticacao.botao.login"));
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthenticationView().setVisible(true));
    }
}
