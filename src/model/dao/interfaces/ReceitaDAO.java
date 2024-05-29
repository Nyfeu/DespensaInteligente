package model.dao.interfaces;

import model.entities.Receita;

import java.util.List;

public interface ReceitaDAO {

    void create(Receita receita);
    Receita read(Integer recipe_id);
    void update(Receita receita);
    void delete(Integer recipe_id);
    List<Receita> readAll();

}
