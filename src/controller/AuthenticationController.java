package controller;

import model.utils.Authenticator;
import view.AuthenticationView;
import view.MainView;
import view.utils.ViewUtils;

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

        String email = view.getEmail();
        String senha = view.getPassword();

        if (view.getEmail().isEmpty() || view.getPassword().isEmpty()) return;

        boolean authenticated = Authenticator.login(email, senha, view);
        verifyAuthentication(authenticated);

    }

    private void register() {

        String nome = view.getNome();
        String email = view.getEmail();
        String senha = view.getPassword();

        System.out.println(view.getEmail());
        System.out.println(view.getNome());
        System.out.println(view.getPassword());

        if (view.getEmail().isEmpty() ||
            view.getPassword().isEmpty() ||
            view.getNome().isEmpty()) return;

        boolean authenticated = Authenticator.registrar(nome, email, senha);
        verifyAuthentication(authenticated);

    }

    private void verifyAuthentication(boolean authenticated) {
        if (authenticated) {
            System.out.println("Usuário Autenticado!");
            ViewUtils.closeView(view);
            new MainView().setVisible(true);
        } else {
            System.out.println("Falha na autenticação...");
        }
    }

}
