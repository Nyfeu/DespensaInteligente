package app;

import model.utils.Authenticator;

public class Main {
    public static void main(String[] args) {

        try {

            boolean logado = Authenticator.login("joao.santos@example.com", "hash_senha_123", null);

            System.out.println(logado ? "Logado" : "Falhou");

            System.out.println(Authenticator.getAuthenticatedUser());

            Authenticator.logout();

        } catch (RuntimeException e) {

            System.out.println(e.getMessage());

        }

    }
}