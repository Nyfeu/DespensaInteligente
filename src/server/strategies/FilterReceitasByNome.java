package server.strategies;

import server.db.DB;
import server.db.DBException;
import server.dao.DAOFactory;
import server.dao.interfaces.ReceitaDao;
import shared.entities.Receita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilterReceitasByNome implements FilterStrategy {

    private String nome_receita;

    public FilterReceitasByNome(String nome_receita) { this.nome_receita = nome_receita; }

    @Override
    public List<Filterable> filter(Integer LIMIT, Integer OFFSET) {

        Connection conn = DB.getConnection();

        String filterSQL = """
                SELECT r.id
                FROM receita r
                WHERE r.titulo LIKE CONCAT('%', ?, '%')
                LIMIT ? OFFSET ?;""";

        ResultSet rs = null;
        PreparedStatement stm = null;

        try {

            stm = conn.prepareStatement(filterSQL);
            stm.setString(1, nome_receita);
            stm.setInt(2, LIMIT);
            stm.setInt(3, OFFSET);
            rs = stm.executeQuery();

            ReceitaDao receitaDao = DAOFactory.createReceitaDao();

            ArrayList<Filterable> receitas = new ArrayList<>();
            while (rs.next()) {
                Receita receita = receitaDao.read(rs.getInt(1));
                receitas.add(receita);
            }

            return receitas;

        } catch (SQLException e) {

            throw new DBException(e.getMessage());

        } finally {

            DB.closeStatement(stm);
            DB.closeResultSet(rs);
            DB.releaseConnection(conn);

        }

    }

}
