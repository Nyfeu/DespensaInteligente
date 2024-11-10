package server.strategies;

import server.dao.DAOFactory;
import server.dao.interfaces.ReceitaDao;
import server.db.DB;
import server.db.DBException;
import shared.entities.Receita;
import shared.entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilterReceitasByDataValidadeAndIngredientes implements FilterStrategy {

    private Usuario usuario;

    public FilterReceitasByDataValidadeAndIngredientes(Usuario usuario) { this.usuario = usuario; }

    @Override
    public List<Filterable> filter(Integer LIMIT, Integer OFFSET) {

        Connection conn = DB.getConnection();

        String filterSQL = """
                SELECT r.id, r.titulo, r.descricao, r.modo_preparo
                FROM receita r
                JOIN receita_ingrediente ri ON r.id = ri.id_receita
                JOIN despensa d ON ri.nome_ingrediente = d.nome_ingrediente AND d.email = ?
                WHERE NOT EXISTS (
                    SELECT 1
                    FROM receita_ingrediente ri2
                    LEFT JOIN despensa d2 ON ri2.nome_ingrediente = d2.nome_ingrediente AND d2.email = ?
                    WHERE ri2.id_receita = r.id
                    AND (d2.quantidade IS NULL OR d2.quantidade < ri2.quantidade)
                )
                GROUP BY r.id
                ORDER BY MIN(d.validade) ASC
                LIMIT ? OFFSET ?;
                """;

        ResultSet rs = null;
        PreparedStatement stm = null;

        try {

            stm = conn.prepareStatement(filterSQL);
            stm.setString(1, usuario.getEmail());
            stm.setString(2, usuario.getEmail());
            stm.setInt(3, LIMIT);
            stm.setInt(4, OFFSET);
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
