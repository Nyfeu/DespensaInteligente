package model.utils;

import model.dao.DAOFactory;
import model.dao.interfaces.UsuarioDao;
import model.entities.Usuario;
import view.AuthenticationView;

import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public static boolean login(String email, String password, Component component) {

        UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();
        Usuario usuario = usuarioDao.read(email);

        if (usuario == null) {
            JOptionPane.showMessageDialog(component, AuthenticationView.getResourceBundle().getString("utils.authenticator.user.desconhecido"), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String encodedPassword = encodePassword(password);

        if (encodedPassword != null) {
            if (encodedPassword.equals(usuario.getSenha())) {
                authenticatedUser = usuario;
                return true;
            } else {
                JOptionPane.showMessageDialog(component, AuthenticationView.getResourceBundle().getString("utils.authenticator.senha.invalida"), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            }
        }

        return false;

    }

    public static void logout() {
        authenticatedUser = null;
    }

    public static boolean registrar(String nome, String email, String password) {

        try {

            String encodedPassword = encodePassword(password);

            Usuario usuario = new Usuario(nome, email, encodedPassword);

            UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();

            usuarioDao.create(usuario);

            authenticatedUser = usuario;

            return true;

        } catch (RuntimeException e) {

            System.out.println(e.getMessage());
            return false;

        }

    }

    public static Usuario getAuthenticatedUser() {
        return authenticatedUser;
    }

    public static void changePassword(String newPassword) {

        try {

            String encodedPassword = encodePassword(newPassword);
            authenticatedUser.setSenha(encodedPassword);
            UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();
            usuarioDao.update(authenticatedUser);

        } catch (RuntimeException e) {

            System.out.println(e.getMessage());

        }

    }

    public static void changeNome(String newNome) {

        try {

            authenticatedUser.setNome(newNome);
            UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();
            usuarioDao.update(authenticatedUser);

        } catch (RuntimeException e) {

            System.out.println(e.getMessage());

        }

    }

}
