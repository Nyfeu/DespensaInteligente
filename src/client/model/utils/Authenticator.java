package client.model.utils;

import client.facade.ClientFacade;
import shared.enums.Command;
import shared.enums.Entity;
import shared.entities.Usuario;
import client.view.utils.LanguageManager;
import shared.enums.Attributes;
import shared.enums.Status;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Authenticator {

    private static Usuario authenticatedUser;

    private Authenticator() {}

    private static String encodePassword(String password)  {

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-512");

            byte[] messageDigest = md.digest(password.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));

            while (hashText.length() < 32) hashText.insert(0, "0");

            return hashText.toString();

        } catch (NoSuchAlgorithmException e) {

            System.out.println(e.getMessage());

        }

        return null;

    }

    public static CompletableFuture<Boolean> login(String email, String password, Component component) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        Map<String, Object> args = new HashMap<>();
        args.put(Attributes.EMAIL.getDescription(), email);

        ClientFacade.sendRequest(Entity.USUARIO, Command.READ, args, responsePacket -> {

            Usuario usuario = (Usuario) responsePacket.getData().get(Attributes.RESULT.getDescription());

            if (usuario == null) {
                String error_string = LanguageManager.getInstance().getResourceBundle().getString("utils.authenticator.user.desconhecido");
                JOptionPane.showMessageDialog(component, error_string, "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                future.complete(false);
                return;
            }

            String encodedPassword = encodePassword(password);

            if (encodedPassword != null) {
                if (encodedPassword.equals(usuario.getSenha())) {
                    authenticatedUser = usuario;
                    future.complete(true);
                    return;
                } else {
                    String error_string = LanguageManager.getInstance().getResourceBundle().getString("utils.authenticator.senha.invalida");
                    JOptionPane.showMessageDialog(component, error_string, "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                }
            }

            future.complete(false);

        });

        return future;

    }

    public static void logout() {
        authenticatedUser = null;
    }

    public static CompletableFuture<Boolean> registrar(String nome, String email, String password) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        String encodedPassword = encodePassword(password);

        Usuario usuario = new Usuario(nome, email, encodedPassword);

        Map<String, Object> args = new HashMap<>();
        args.put(Attributes.USER.getDescription(), usuario);

        ClientFacade.sendRequest(Entity.USUARIO, Command.CREATE, args, responsePacket -> {

            if (responsePacket.getStatus().equals(Status.SUCCESS)) {

                authenticatedUser = usuario;
                future.complete(true);

            } else future.complete(false);

        });

        return future;

    }

    public static Usuario getAuthenticatedUser() {
        return authenticatedUser;
    }

    public static void changePassword(String newPassword) {

        String encodedPassword = encodePassword(newPassword);
        authenticatedUser.setSenha(encodedPassword);

        Map<String, Object> args = new HashMap<>();
        args.put(Attributes.USER.getDescription(), authenticatedUser);

        ClientFacade.sendRequest(Entity.USUARIO, Command.UPDATE, args, e -> {});

    }

    public static void changeNome(String newNome) {

        try {

            authenticatedUser.setNome(newNome);

            Map<String, Object> args = new HashMap<>();
            args.put(Attributes.USER.getDescription(), authenticatedUser);

            ClientFacade.sendRequest(Entity.USUARIO, Command.UPDATE, args, e -> {});

        } catch (RuntimeException e) {

            System.out.println(e.getMessage());

        }

    }

}
