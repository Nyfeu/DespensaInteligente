package app;

import model.dao.DAOFactory;
import model.dao.interfaces.IngredienteDao;
import model.entities.Ingrediente;
import utils.DateParser;

import java.text.ParseException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {

            IngredienteDao ingredienteDAO = DAOFactory.createIngredienteDao();

            Ingrediente ingrediente1 = new Ingrediente("Mussarela", 1, DateParser.parseString("30/05/2024"), 3);
            ingredienteDAO.create(ingrediente1);

            Ingrediente ingrediente2 = new Ingrediente("Pepperoni", 2, DateParser.parseString("01/06/2024"), 6);
            ingredienteDAO.create(ingrediente2);

            Ingrediente ingrediente3 = new Ingrediente("Atum", 3, DateParser.parseString("01/06/2024"), 6);
            ingredienteDAO.create(ingrediente3);

            List<Ingrediente> ingredientes = ingredienteDAO.readAll();
            for (Ingrediente ingrediente : ingredientes) System.out.println(ingrediente);

        } catch (RuntimeException | ParseException e) {

            System.out.println(e.getMessage());

        }

    }
}