package client.model.utils;

import client.view.utils.LanguageManager;

public enum CategoriaIngrediente {
    FRUTAS(LanguageManager.getInstance().getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.frutas")),
    VEGETAIS(LanguageManager.getInstance().getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.vegetais")),
    CARNES(LanguageManager.getInstance().getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.carnes")),
    PEIXES(LanguageManager.getInstance().getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.peixes")),
    GRAOS(LanguageManager.getInstance().getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.graos")),
    LATICINIOS(LanguageManager.getInstance().getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.laticinios")),
    TEMPEROS(LanguageManager.getInstance().getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.temperos")),
    BEBIDAS(LanguageManager.getInstance().getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.bebidas")),
    OUTROS(LanguageManager.getInstance().getResourceBundle().getString("main.despensa.botao.adicionar.validation.selecaocategoria.outros"));

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