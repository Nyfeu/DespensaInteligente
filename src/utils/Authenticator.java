package utils;

import model.dao.DAOFactory;
import model.dao.interfaces.UsuarioDao;
import model.entities.Usuario;

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

    public boolean login(String email, String password, Component component) {

        UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();
        Usuario usuario = usuarioDao.read(email);

        if (usuario == null) {
            JOptionPane.showMessageDialog(component, "Usuário não encontrado!", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String encodedPassword = encodePassword(password);

        if (encodedPassword != null) {
            if (encodedPassword.equals(usuario.getSenha())) {
                usuario.setSenha(encodedPassword);
                authenticatedUser = usuario;
                return true;
            }
        }

        return false;

    }

    public void logout() {
        authenticatedUser = null;
    }

    public void registrar(String nome, String email, String password) {

        String encodedPassword = encodePassword(password);

        Usuario usuario = new Usuario(nome, email, encodedPassword);

        UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();
        usuarioDao.create(usuario);

        authenticatedUser = usuario;

    }

}
