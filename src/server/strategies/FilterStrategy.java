package server.strategies;

import java.util.List;

public interface FilterStrategy {

    List<Filterable> filter(Integer LIMIT, Integer OFFSET);

}
