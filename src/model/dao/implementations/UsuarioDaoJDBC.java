package model.dao.implementations;

import model.dao.interfaces.UsuarioDao;
import model.entities.Usuario;

import java.sql.Connection;

public class UsuarioDaoJDBC implements UsuarioDao {

    private Connection conn;

    public UsuarioDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void create(Usuario usuario) {

    }

    @Override
    public Usuario read(Integer user_id) {
        return null;
    }

    @Override
    public void update(Usuario usuario) {

    }

    @Override
    public void delete(Integer user_id) {

    }

}
