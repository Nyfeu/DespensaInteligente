package model.utils;

public enum CategoriaIngrediente {
    FRUTAS("Frutas"),
    VEGETAIS("Vegetais"),
    CARNES("Carnes"),
    PEIXES("Peixes"),
    GRAOS("Grãos"),
    LATICINIOS("Laticínios"),
    TEMPEROS("Temperos"),
    BEBIDAS("Bebidas"),
    OUTROS("Outros");

    private final String nome;

    CategoriaIngrediente(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}