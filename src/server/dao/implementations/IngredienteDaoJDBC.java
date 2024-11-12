package server.dao.implementations;

import client.model.builder.IngredienteBuilder;
import server.db.DB;
import server.db.DBException;
import server.dao.interfaces.IngredienteDao;
import shared.entities.Ingrediente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredienteDaoJDBC implements IngredienteDao {

    @Override
    public void create(Ingrediente ingrediente) {

        Connection conn = null;
        String sqlInsert = "INSERT INTO ingrediente(nome, categoria) VALUES(?,?)";
        PreparedStatement stm = null;

        try {

            conn = DB.getConnection();
            stm = conn.prepareStatement(sqlInsert);
            stm.setString(1, ingrediente.getNome());
            stm.setInt(2, ingrediente.getCategoria());
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.releaseConnection(conn);

        }
    }

    @Override
    public Ingrediente read(String name) {

        Connection conn = null;
        String sqlCarregar = "SELECT nome, categoria FROM ingrediente WHERE nome = ?";
        ResultSet rs = null;
        PreparedStatement stm = null;

        try {

            conn = DB.getConnection();
            stm = conn.prepareStatement(sqlCarregar);
            stm.setString(1, name);
            rs = stm.executeQuery();

            if(rs.next()) return instantiateIngrediente(rs);
            return null;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.closeResultSet(rs);
            DB.releaseConnection(conn);

        }

    }

    @Override
    public void update(Ingrediente ingrediente) {

        Connection conn = null;
        String sqlUpdate = "UPDATE ingrediente SET categoria = ? WHERE nome = ?";
        PreparedStatement stm = null;

        try {

            conn = DB.getConnection();

            stm = conn.prepareStatement(sqlUpdate);
            stm.setInt(1, ingrediente.getCategoria());
            stm.setString(2, ingrediente.getNome());
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.releaseConnection(conn);

        }

    }

    @Override
    public void delete(String name) {

        Connection conn = null;
        String sqlExcluir = "DELETE FROM ingrediente WHERE nome = ?";
        PreparedStatement stm = null;

        try{

            conn = DB.getConnection();
            stm = conn.prepareStatement(sqlExcluir);
            stm.setString(1, name);
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.releaseConnection(conn);

        }

    }

    @Override
    public List<Ingrediente> readAll() {

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            conn = DB.getConnection();

            st = conn.prepareStatement("SELECT * FROM ingrediente ORDER BY nome");

            rs = st.executeQuery();
            List<Ingrediente> ingredienteList = new ArrayList<>();

            while(rs.next()) {
                ingredienteList.add(instantiateIngrediente(rs));
            }

            return ingredienteList;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(st);
            DB.closeResultSet(rs);
            DB.releaseConnection(conn);

        }
    }

    private Ingrediente instantiateIngrediente(ResultSet rs) throws SQLException {

        IngredienteBuilder ingredienteBuilder = new IngredienteBuilder();

        String nome = rs.getString(1);
        int categoria = rs.getInt(2);

        return ingredienteBuilder.nome(nome).categoria(categoria).build();

    }

}
