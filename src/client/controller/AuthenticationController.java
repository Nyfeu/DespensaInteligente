package client.controller;

import client.model.utils.Authenticator;
import client.view.AuthenticationView;
import client.view.MainView;
import client.view.utils.LanguageManager;
import client.view.utils.Validator;
import client.view.utils.ViewUtils;

import javax.swing.*;
import java.util.concurrent.CompletableFuture;

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

        CompletableFuture<Void> authenticated = Authenticator.login(email, senha, view).thenAccept(this::verifyAuthentication);

    }

    private void register() {

        String nome = view.getNome();
        String email = view.getEmail_login();
        String senha = view.getPassword_login();

        if (view.getEmail_login().isEmpty() ||
            view.getPassword_login().isEmpty() ||
            view.getNome().isEmpty()) return;

        if (!Validator.isValidEmail(view.getEmail_login())) {
            JOptionPane.showMessageDialog(view,LanguageManager.getInstance().getResourceBundle().getString("client.controller.authentication.email.invalido"));
            return;
        }

        Authenticator.registrar(nome, email, senha).thenAccept(this::verifyAuthentication);

    }

    private void verifyAuthentication(boolean authenticated) {
        if (authenticated) {
            System.out.println("USER::SUCCESSFULLY::AUTHENTICATED");
            ViewUtils.closeView(view);
            new MainView().setVisible(true);
        } else {
            System.out.println("USER::ERROR::AUTHENTICATION");
        }
    }

}
 