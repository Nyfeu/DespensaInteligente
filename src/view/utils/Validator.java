package view.utils;

import model.builder.IngredienteBuilder;
import model.dao.DAOFactory;
import model.dao.interfaces.IngredienteDao;
import model.entities.Ingrediente;
import model.utils.CategoriaIngrediente;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static boolean verifyIngrediente(String nome, Component component) {

        IngredienteDao ingredienteDao = DAOFactory.createIngredienteDao();
        Ingrediente ingredienteVerify = ingredienteDao.read(nome);

        System.out.println(ingredienteVerify);

        if (ingredienteVerify == null) {

            int option = JOptionPane.showConfirmDialog(component, "Esse ingrediente não está cadastrado. Deseja cadastrá-lo?", "Confirmar Cadastro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {

                JPanel panel = new JPanel(new GridLayout(1,2));
                JComboBox<String> comboBox = new JComboBox<>();
                for (String categoria : getCategoriaNomes()) {
                    comboBox.addItem(categoria);
                }
                JLabel label = new JLabel("Selecione a categoria: ");
                panel.add(label);
                panel.add(comboBox);

                int result = JOptionPane.showConfirmDialog(component, panel, "Seleção de categoria", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    int categoria = comboBox.getSelectedIndex();
                    IngredienteBuilder ingredienteBuilder = new IngredienteBuilder();
                    ingredienteBuilder.nome(nome).categoria(categoria);
                    ingredienteDao.create(ingredienteBuilder.build());
                }

                return true;

            } else return false;

        }

        return true;

    }

    public static ArrayList<String> getCategoriaNomes() {
        ArrayList<String> categoriaNomes = new ArrayList<>();
        for (CategoriaIngrediente categoria : CategoriaIngrediente.values()) {
            categoriaNomes.add(categoria.getNome());
        }
        return categoriaNomes;
    }

    public static boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

        if (email == null) {
            return false;
        }

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

}
