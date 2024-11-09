package server.strategies;

import server.dao.DAOFactory;
import server.dao.interfaces.ReceitaDao;
import server.db.DB;
import server.db.DBException;
import shared.entities.Receita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilterReceitasByPage implements FilterStrategy {

    @Override
    public List<Filterable> filter(Integer LIMIT, Integer OFFSET) {

        Connection conn = DB.getConnection();

        String filterSQL = """
                SELECT *
                FROM receita r
                LIMIT ? OFFSET ?;""";

        ResultSet rs = null;
        PreparedStatement stm = null;

        try {

            stm = conn.prepareStatement(filterSQL);
            stm.setInt(1, LIMIT);
            stm.setInt(2, OFFSET);
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
