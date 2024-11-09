package model.dao.implementations;

import model.builder.ReceitaBuilder;
import model.db.DB;
import model.db.DBException;
import model.dao.DAOFactory;
import model.dao.interfaces.IngredienteDao;
import model.dao.interfaces.ReceitaDao;
import model.entities.Ingrediente;
import model.entities.Receita;
import model.strategies.FilterStrategy;
import model.strategies.Filterable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceitaDaoJDBC implements ReceitaDao {

    @Override
    public void create(Receita receita) {

        Connection conn = null;
        String sqlInsert = "INSERT INTO RECEITA(titulo, descricao, modo_preparo, email_usuario) VALUES(?,?,?,?)";

        PreparedStatement stm = null;
        ResultSet rs = null;
        try{

            conn = DB.getConnection();

            stm = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, receita.getTitulo());
            stm.setString(2, receita.getDescricao());
            stm.setString(3, receita.getModoPreparo());
            stm.setString(4, receita.getEmailAutor());
            stm.executeUpdate();

            rs = stm.getGeneratedKeys();
            if(rs.next()) {
                int idGerado = rs.getInt(1);

                stm = conn.prepareStatement("INSERT INTO receita_ingrediente(id_receita, nome_ingrediente, quantidade) VALUES (?,?,?)");

                for (Ingrediente ingrediente : receita.getIngredientes()) {
                    stm.setInt(1, idGerado);
                    stm.setString(2, ingrediente.getNome());
                    stm.setInt(3, ingrediente.getQuantidade());
                    stm.execute();
                }
            }

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.closeResultSet(rs);
            DB.releaseConnection(conn);
        }

    }

    @Override
    public Receita read(Integer recipe_id) {

        Connection conn = null;
        String sqlCarregar = "SELECT id, titulo, descricao, modo_preparo, email_usuario FROM RECEITA WHERE id = ?";
        ResultSet rs1 = null, rs2 = null;
        PreparedStatement stm = null;

        try{

            conn = DB.getConnection();

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
            DB.releaseConnection(conn);

        }
    }

    @Override
    public void update(Receita receita) {

        Connection conn = null;
        String sqlUpdate = "UPDATE RECEITA SET Titulo = ?, Descricao = ?, modo_preparo = ?, email_usuario = ? WHERE id = ?";
        PreparedStatement stm = null;

        try{

            conn = DB.getConnection();

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
            DB.releaseConnection(conn);

        }

    }

    @Override
    public void delete(Integer recipe_id) {

        Connection conn = null;
        String sqlExcluir = "DELETE FROM RECEITA WHERE id = ?";
        PreparedStatement stm = null;

        try{

            conn = DB.getConnection();

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
            DB.releaseConnection(conn);

        }

    }

    @Override
    public List<Receita> readAll() {

        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs1 = null, rs2 = null;

        try {

            conn = DB.getConnection();

            st = conn.prepareStatement("SELECT * FROM RECEITA ORDER BY id");

            rs1 = st.executeQuery();
            List<Receita> receitaList = new ArrayList<>();

            while(rs1.next()) {

                st = conn.prepareStatement("SELECT nome_ingrediente, quantidade FROM receita_ingrediente WHERE id_receita = ?");
                st.setInt(1, rs1.getInt(1));
                rs2 = st.executeQuery();

                receitaList.add(instantiateReceita(rs1, rs2));
            }

            return receitaList;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(st);
            DB.closeResultSet(rs1);
            DB.closeResultSet(rs2);
            DB.releaseConnection(conn);

        }

    }

    @Override
    public int countAll() {

        Connection conn = DB.getConnection();

        int count = 0;
        String sql = "SELECT COUNT(*) FROM RECEITA";

        try (PreparedStatement stmt = conn.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) count = rs.getInt(1);

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.releaseConnection(conn);

        }

        return count;
    }

    public List<Filterable> filter(FilterStrategy filterStrategy, Integer LIMIT, Integer OFFSET) {
        return filterStrategy.filter(LIMIT, OFFSET);
    }

    private Receita instantiateReceita(ResultSet rs1, ResultSet rs2) throws SQLException {

        ReceitaBuilder receitaBuilder = new ReceitaBuilder(rs1.getInt(1));

        receitaBuilder.titulo(rs1.getString(2))
                .descricao(rs1.getString(3))
                .instrucoes(rs1.getString(4))
                .emailAutor(rs1.getString(5));

        Receita receita = receitaBuilder.build();

        IngredienteDao ingredienteDao = DAOFactory.createIngredienteDao();
        ArrayList<Ingrediente> ingredientes = new ArrayList<>();
        while (rs2.next()) {
            Ingrediente ingrediente = ingredienteDao.read(rs2.getString(1));
            if (ingrediente != null) {
                ingrediente.setQuantidade(rs2.getInt(2));
                ingredientes.add(ingrediente);
            }
        }

        receita.setIngredientes(ingredientes);

        return receita;

    }

}
