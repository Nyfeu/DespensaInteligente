package server.dao.interfaces;

import shared.entities.Ingrediente;

import java.util.List;

public interface IngredienteDao {

    void create(Ingrediente ingrediente);
    Ingrediente read(String name);
    void update(Ingrediente ingrediente);
    void delete(String name);
    List<Ingrediente> readAll();

}
