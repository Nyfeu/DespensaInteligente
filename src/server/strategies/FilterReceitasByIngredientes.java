package server.strategies;

import server.db.DB;
import server.db.DBException;
import server.dao.DAOFactory;
import server.dao.interfaces.ReceitaDao;
import shared.entities.Receita;
import shared.entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilterReceitasByIngredientes implements FilterStrategy {

    private Usuario usuario;

    public FilterReceitasByIngredientes(Usuario usuario) { this.usuario = usuario; }

    @Override
    public List<Filterable> filter(Integer LIMIT, Integer OFFSET) {

        Connection conn = DB.getConnection();

        String filterSQL = """
                SELECT r.id
                FROM receita r
                JOIN receita_ingrediente ri ON r.id = ri.id_receita
                LEFT JOIN despensa d ON ri.nome_ingrediente = d.nome_ingrediente AND d.email = ?
                GROUP BY r.id
                HAVING SUM(CASE WHEN d.quantidade IS NULL OR d.quantidade < ri.quantidade THEN 1 ELSE 0 END) = 0
                LIMIT ? OFFSET ?;""";

        ResultSet rs = null;
        PreparedStatement stm = null;

        try {

            stm = conn.prepareStatement(filterSQL);
            stm.setString(1, usuario.getEmail());
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
