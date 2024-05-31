package model.dao.implementations;

import model.db.DB;
import model.db.DBException;
import model.dao.DAOFactory;
import model.dao.interfaces.IngredienteDao;
import model.dao.interfaces.ReceitaDao;
import model.entities.Ingrediente;
import model.entities.Receita;
import model.strategies.FilterStrategy;
import model.strategies.Filterable;

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

        String sqlInsert = "INSERT INTO RECEITA(id, titulo, descricao, modo_preparo, email_usuario) VALUES(?,?,?,?,?)";

        PreparedStatement stm = null;
        try{

            stm = conn.prepareStatement(sqlInsert);
            stm.setInt(1, receita.getId());
            stm.setString(2, receita.getTitulo());
            stm.setString(3, receita.getDescricao());
            stm.setString(4, receita.getModoPreparo());
            stm.setString(5, receita.getEmailAutor());
            stm.execute();

            stm = conn.prepareStatement("INSERT INTO receita_ingrediente(id_receita, nome_ingrediente, quantidade) VALUES (?,?,?)");

            for (Ingrediente ingrediente : receita.getIngredientes()) {
                stm.setInt(1, receita.getId());
                stm.setString(2, ingrediente.getNome());
                stm.setInt(3, ingrediente.getQuantidade());
                stm.execute();
            }

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);

        }

    }

    @Override
    public Receita read(Integer recipe_id) {

        String sqlCarregar = "SELECT id, titulo, descricao, modo_preparo, email_usuario FROM RECEITA WHERE id = ?";
        ResultSet rs1 = null, rs2 = null;
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlCarregar);
            stm.setInt(1, recipe_id);
            rs1 = stm.executeQuery();

            stm = conn.prepareStatement("SELECT nome_ingrediente, quantidade FROM receita_ingrediente WHERE id_receita = ?");
            stm.setInt(1, recipe_id);
            rs2 = stm.executeQuery();

            if(rs1.next()) return instantiateReceita(rs1, rs2);
            return null;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally{

            DB.closeStatement(stm);
            DB.closeResultSet(rs1);
            DB.closeResultSet(rs2);

        }
    }

    @Override
    public void update(Receita receita) {

        String sqlUpdate = "UPDATE RECEITA SET Titulo = ?, Descricao = ?, modo_preparo = ?, email_usuario = ? WHERE id = ?";
        PreparedStatement stm = null;

        try{

            stm = conn.prepareStatement(sqlUpdate);
            stm.setString(1, receita.getTitulo());
            stm.setString(2, receita.getDescricao());
            stm.setString(3, receita.getModoPreparo());
            stm.setString(4, receita.getEmailAutor());
            stm.setInt(5, receita.getId());
            stm.execute();

            stm = conn.prepareStatement("DELETE FROM receita_ingrediente WHERE id_receita = ?");
            stm.setInt(1, receita.getId());
            stm.execute();

            stm = conn.prepareStatement("INSERT INTO receita_ingrediente(id_receita, nome_ingrediente, quantidade) VALUES (?,?,?)");

            for (Ingrediente ingrediente : receita.getIngredientes()) {
                stm.setInt(1, receita.getId());
                stm.setString(2, ingrediente.getNome());
                stm.setInt(3, ingrediente.getQuantidade());
                stm.execute();
            }

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

            stm = conn.prepareStatement("DELETE FROM receita_ingrediente WHERE id_receita = ?");
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
        ResultSet rs1 = null, rs2 = null;

        try {

            st = conn.prepareStatement("SELECT * FROM RECEITA ORDER BY id");

            rs1 = st.executeQuery();
            List<Receita> departmentList = new ArrayList<>();

            while(rs1.next()) {

                st = conn.prepareStatement("SELECT nome_ingrediente, quantidade FROM receita_ingrediente WHERE id_receita = ?");
                st.setInt(1, rs1.getInt(1));
                rs2 = st.executeQuery();

                departmentList.add(instantiateReceita(rs1, rs2));
            }

            return departmentList;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(st);
            DB.closeResultSet(rs1);
            DB.closeResultSet(rs2);

        }

    }

    public List<Filterable> filter(FilterStrategy filterStrategy, Integer LIMIT, Integer OFFSET) {
        return filterStrategy.filter(conn, LIMIT, OFFSET);
    }

    private Receita instantiateReceita(ResultSet rs1, ResultSet rs2) throws SQLException {

        Receita receita = new Receita();
        receita.setId(rs1.getInt(1));
        receita.setTitulo(rs1.getString(2));
        receita.setDescricao(rs1.getString(3));
        receita.setModoPreparo(rs1.getString(4));
        receita.setEmailAutor(rs1.getString(5));

        IngredienteDao ingredienteDao = DAOFactory.createIngredienteDao();
        ArrayList<Ingrediente> ingredientes = new ArrayList<>();
        while (rs2.next()) {
            Ingrediente ingrediente = ingredienteDao.read(rs2.getString(1));
            ingrediente.setQuantidade(rs2.getInt(2));
            ingredientes.add(ingrediente);
        }

        receita.setIngredientes(ingredientes);

        return receita;

    }

}
