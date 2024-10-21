package model.utils;

import view.AuthenticationView;

public enum CategoriaIngrediente {
    FRUTAS(AuthenticationView.getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.frutas")),
    VEGETAIS(AuthenticationView.getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.vegetais")),
    CARNES(AuthenticationView.getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.carnes")),
    PEIXES(AuthenticationView.getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.peixes")),
    GRAOS(AuthenticationView.getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.graos")),
    LATICINIOS(AuthenticationView.getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.laticinios")),
    TEMPEROS(AuthenticationView.getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.temperos")),
    BEBIDAS(AuthenticationView.getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.bebidas")),
    OUTROS(AuthenticationView.getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.outros"));

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