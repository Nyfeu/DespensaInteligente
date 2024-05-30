package model.dao.implementations;

import db.DB;
import db.DBException;
import model.dao.interfaces.ReceitaDao;
import model.entities.Receita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReceitaDaoJDBC implements ReceitaDao {

    private Connection conn;

    public ReceitaDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void create(Receita receita) {

        String sqlInsert = "INSERT INTO RECEITA(id, titulo, descricao, modo_preparo) VALUES(?,?,?,?)";

        PreparedStatement stm = null;
        try{

            stm = conn.prepareStatement(sqlInsert);
            stm.setInt(1, receita.getId());
            stm.setString(2, receita.getTitulo());
            stm.setString(3, receita.getDescricao());
            stm.setString(4, receita.getModoPreparo());
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }

    }

    @Override
    public Receita read(Integer recipe_id) {

        String sqlCarregar = "SELECT titulo, descricao, modo_preparo FROM RECEITA WHERE id = ?";
        ResultSet rs = null;
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlCarregar);
            stm.setInt(1, recipe_id);
            rs = stm.executeQuery();

            if(rs.next()) return instantiateReceita(rs);
            return null;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally{

            DB.closeStatement(stm);
            DB.closeResultSet(rs);

        }
    }

    @Override
    public void update(Receita receita) {

        String sqlUpdate = "UPDATE RECEITA SET Titulo = ?, Descricao = ?, modo_preparo = ? WHERE id = ?";
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlUpdate);
            stm.setString(1, receita.getTitulo());
            stm.setString(2, receita.getDescricao());
            stm.setString(3, receita.getModoPreparo());
            stm.setInt(4, receita.getId());
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }

    }

    @Override
    public void delete(Integer recipe_id) {

        String sqlExcluir = "DELETE FROM RECEITA WHERE id = ?";
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlExcluir);
            stm.setInt(1, recipe_id);
            stm.execute();

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }

    }

    @Override
    public List<Receita> readAll() {

        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            st = conn.prepareStatement("SELECT * FROM RECEITA ORDER BY Nome");

            rs = st.executeQuery();
            List<Receita> departmentList = new ArrayList<>();

            while(rs.next()) {
                departmentList.add(instantiateReceita(rs));
            }

            return departmentList;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(st);
            DB.closeResultSet(rs);

        }

    }

    private Receita instantiateReceita(ResultSet rs) throws SQLException {

        Receita receita = new Receita();
        receita.setId(rs.getInt(1));
        receita.setTitulo(rs.getString(2));
        receita.setDescricao(rs.getString(3));
        receita.setModoPreparo(rs.getString(4));
        return receita;

    }

}
