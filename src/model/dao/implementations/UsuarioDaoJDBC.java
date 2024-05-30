package model.dao.implementations;

import db.DB;
import db.DBException;
import model.dao.interfaces.UsuarioDao;
import model.entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDaoJDBC implements UsuarioDao {

    private Connection conn;

    public UsuarioDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void create(Usuario usuario) {
        String sqlInsert = "INSERT INTO USUARIO(Nome, Email, Senha_Hash) VALUES(?,?,?)";

        PreparedStatement stm = null;
        try{

            stm = conn.prepareStatement(sqlInsert);
            stm.setString(1, usuario.getNome());
            stm.setString(2, usuario.getEmail());
            stm.setString(3, usuario.getSenha());
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }
    }

    @Override
    public Usuario read(String email) {
        String sqlCarregar = "SELECT Nome, Email, Senha_Hash FROM USUARIO WHERE Email = ?";
        ResultSet rs = null;
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlCarregar);
            stm.setString(1, email);
            rs = stm.executeQuery();

            if(rs.next()) return instantiateUsuario(rs);
            return null;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally{

            DB.closeStatement(stm);
            DB.closeResultSet(rs);

        }
    }

    @Override
    public void update(Usuario usuario) {
        String sqlUpdate = "UPDATE USUARIO SET Nome = ?, Senha_Hash = ? WHERE Email = ?";
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlUpdate);
            stm.setString(1, usuario.getNome());
            stm.setString(2, usuario.getSenha());
            stm.setString(3, usuario.getEmail());
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }

    }

    @Override
    public void delete(String email) {

        String sqlExcluir = "DELETE FROM USUARIO WHERE email = ?";
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlExcluir);
            stm.setString(1, email);
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }

    }
    public List<Usuario> readAll() {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            st = conn.prepareStatement("SELECT * FROM USUARIO ORDER BY Nome");

            rs = st.executeQuery();
            List<Usuario> departmentList = new ArrayList<>();

            while(rs.next()) {
                departmentList.add(instantiateUsuario(rs));
            }

            return departmentList;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(st);
            DB.closeResultSet(rs);

        }

    }

    private Usuario instantiateUsuario(ResultSet rs) throws SQLException {

        Usuario usuario = new Usuario();
        usuario.setNome(rs.getString(1));
        usuario.setEmail(rs.getString(2));
        usuario.setSenha(rs.getString(3));
        return usuario;

    }

}
