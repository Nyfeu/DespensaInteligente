package server.dao;

import server.dao.implementations.IngredienteDaoJDBC;
import server.dao.implementations.ReceitaDaoJDBC;
import server.dao.implementations.UsuarioDaoJDBC;
import server.dao.interfaces.IngredienteDao;
import server.dao.interfaces.ReceitaDao;
import server.dao.interfaces.UsuarioDao;

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
