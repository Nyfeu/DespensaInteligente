package model.dao;

import model.dao.implementations.IngredienteDaoJDBC;
import model.dao.implementations.ReceitaDaoJDBC;
import model.dao.implementations.UsuarioDaoJDBC;
import model.dao.interfaces.IngredienteDao;
import model.dao.interfaces.ReceitaDao;
import model.dao.interfaces.UsuarioDao;

public class DAOFactory {

    public static IngredienteDao createIngredienteDao() {
        return new IngredienteDaoJDBC();
    }

    public static UsuarioDao createUsuarioDao() {
        return new UsuarioDaoJDBC();
    }

    public static ReceitaDao createReceitaDao() {
        return new ReceitaDaoJDBC();
    }

}
