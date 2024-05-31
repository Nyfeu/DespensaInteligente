package model.strategies;

import java.sql.Connection;
import java.util.List;

public interface FilterStrategy {

    List<Filterable> filter(Connection conn, Integer LIMIT, Integer OFFSET);

}
