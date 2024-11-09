package server.dao.interfaces;

import shared.entities.Receita;
import server.strategies.FilterStrategy;
import server.strategies.Filterable;

import java.util.List;

public interface ReceitaDao {
    void create(Receita receita);
    Receita read(Integer recipe_id);
    void update(Receita receita);
    void delete(Integer recipe_id);
    List<Receita> readAll();
    List<Filterable> filter(FilterStrategy filterStrategy, Integer LIMIT, Integer OFFSET);
    int countAll();
}
