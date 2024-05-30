package strategies;

import model.entities.Receita;

import java.sql.Connection;
import java.util.List;

public interface FilterStrategy {

    List<Receita> filtrar(List<Filterable> filterableList, Connection conn);

}
