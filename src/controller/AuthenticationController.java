package controller;

import model.utils.Authenticator;
import view.AuthenticationView;
import view.MainView;
import view.utils.Validator;
import view.utils.ViewUtils;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class AuthenticationController {

    private AuthenticationView view;

    public AuthenticationController(AuthenticationView view) {

        this.view = view;

        view.getSubmitButton().addActionListener(e -> {

            if (view.isLogin()) {
                login();
            } else {
                register();
            }

        });

    }

    private void login() {

        String email = view.getEmail_login();
        String senha = view.getPassword_login();

        if (view.getEmail_login().isEmpty() || view.getPassword_login().isEmpty()) return;

        boolean authenticated = Authenticator.login(email, senha, view);
        verifyAuthentication(authenticated);

    }

    private void register() {

        String nome = view.getNome();
        String email = view.getEmail_login();
        String senha = view.getPassword_login();

        System.out.println(view.getEmail_login());
        System.out.println(view.getNome());
        System.out.println(view.getPassword_login());

        if (view.getEmail_login().isEmpty() ||
            view.getPassword_login().isEmpty() ||
            view.getNome().isEmpty()) return;

        if (!Validator.isValidEmail(view.getEmail_login())) {
            JOptionPane.showMessageDialog(view,AuthenticationView.getResourceBundle().getString("controller.authentication.email.invalido"));
            return;
        }

        boolean authenticated = Authenticator.registrar(nome, email, senha);
        verifyAuthentication(authenticated);

    }

    private void verifyAuthentication(boolean authenticated) {
        if (authenticated) {
            System.out.println("Usuário Autenticado!");
            ViewUtils.closeView(view);
            new MainView(AuthenticationView.getResourceBundle()).setVisible(true);
        } else {
            System.out.println("Falha na autenticação...");
        }
    }

}
 