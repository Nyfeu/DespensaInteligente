package model.dao.implementations;

import model.builder.IngredienteBuilder;
import model.db.DB;
import model.db.DBException;
import model.dao.interfaces.IngredienteDao;
import model.entities.Ingrediente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IngredienteDaoJDBC implements IngredienteDao {

    private Connection conn;

    public IngredienteDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void create(Ingrediente ingrediente) {

        String sqlInsert = "INSERT INTO ingrediente(nome, categoria) VALUES(?,?)";
        PreparedStatement stm = null;

        try {

            stm = conn.prepareStatement(sqlInsert);
            stm.setString(1, ingrediente.getNome());
            stm.setInt(2, ingrediente.getCategoria());
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }
    }

    @Override
    public Ingrediente read(String name) {

        String sqlCarregar = "SELECT Nome, Categoria FROM Ingrediente WHERE Nome = ?";
        ResultSet rs = null;
        PreparedStatement stm = null;

        try {

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

        }

    }

    @Override
    public void update(Ingrediente ingrediente) {

        String sqlUpdate = "UPDATE INGREDIENTE SET Categoria = ? WHERE Nome = ?";
        PreparedStatement stm = null;

        try {

            stm = conn.prepareStatement(sqlUpdate);
            stm.setInt(1, ingrediente.getCategoria());
            stm.setString(2, ingrediente.getNome());
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }

    }

    @Override
    public void delete(String name) {

        String sqlExcluir = "DELETE FROM INGREDIENTE WHERE Nome = ?";
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlExcluir);
            stm.setString(1, name);
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }

    }

    @Override
    public List<Ingrediente> readAll() {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            st = conn.prepareStatement("SELECT * FROM INGREDIENTE ORDER BY Nome");

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

        }
    }

    private Ingrediente instantiateIngrediente(ResultSet rs) throws SQLException {

        IngredienteBuilder ingredienteBuilder = new IngredienteBuilder();

        String nome = rs.getString(1);
        int categoria = rs.getInt(2);

        return ingredienteBuilder.nome(nome).categoria(categoria).build();

    }

}
