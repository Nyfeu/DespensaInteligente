package model.dao;

import db.DB;
import model.dao.implementations.IngredienteDAOImpl;
import model.dao.implementations.ReceitaDAOImpl;
import model.dao.implementations.UsuarioDAOImpl;
import model.dao.interfaces.IngredienteDAO;
import model.dao.interfaces.ReceitaDAO;
import model.dao.interfaces.UsuarioDAO;

public class DAOFactory {

    public static IngredienteDAO createIngredienteDAO() {
        return new IngredienteDAOImpl(DB.getConnection());
    }

    public static UsuarioDAO createUsuarioDAO() {
        return new UsuarioDAOImpl(DB.getConnection());
    }

    public static ReceitaDAO createReceitaDAO() {
        return new ReceitaDAOImpl(DB.getConnection());
    }

}
