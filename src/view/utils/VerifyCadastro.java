package view.utils;

import model.builder.IngredienteBuilder;
import model.dao.DAOFactory;
import model.dao.interfaces.IngredienteDao;
import model.entities.Ingrediente;

import javax.swing.*;
import java.awt.*;

public class VerifyCadastro {

    public static boolean ingrediente(String nome, Component component) {

        IngredienteDao ingredienteDao = DAOFactory.createIngredienteDao();
        Ingrediente ingredienteVerify = ingredienteDao.read(nome);

        System.out.println(ingredienteVerify);

        if (ingredienteVerify == null) {

            int option = JOptionPane.showConfirmDialog(component, "Esse ingrediente não está cadastrado. Deseja cadastrá-lo?", "Confirmar Cadastro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                int categoria = Integer.parseInt(JOptionPane.showInputDialog("Digite o número da categoria:"));
                IngredienteBuilder ingredienteBuilder = new IngredienteBuilder();
                ingredienteBuilder.nome(nome).categoria(categoria);
                ingredienteDao.create(ingredienteBuilder.build());
                return true;
            } else return false;

        }

        return true;

    }

}
