package model.strategies;

import model.db.DB;
import model.db.DBException;
import model.dao.DAOFactory;
import model.dao.interfaces.ReceitaDao;
import model.entities.Receita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilterReceitasByAutor implements FilterStrategy {

    private String emailAutor;

    public FilterReceitasByAutor(String emailAutor) { this.emailAutor = emailAutor; }

    @Override
    public List<Filterable> filter(Connection conn, Integer LIMIT, Integer OFFSET) {

        String filterSQL = """
                SELECT r.id
                FROM receita r
                WHERE r.email_usuario = ?
                LIMIT ? OFFSET ?;""";

        ResultSet rs = null;
        PreparedStatement stm = null;

        try {

            stm = conn.prepareStatement(filterSQL);
            stm.setString(1, emailAutor);
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

        }

    }

}
