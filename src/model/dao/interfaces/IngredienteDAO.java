package model.dao.interfaces;

import model.entities.Ingrediente;

import java.util.List;

public interface IngredienteDAO {

    void create(Ingrediente ingrediente);
    Ingrediente read(String name);
    void update(Ingrediente ingrediente);
    void delete(String name);
    List<Ingrediente> readAll();

}
