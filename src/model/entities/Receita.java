package model.entities;

import model.builder.ReceitaBuilder;
import strategies.Filterable;

import java.util.ArrayList;
import java.util.List;

public class Receita implements Filterable {
    private String titulo;
    private String descricao;
    private List<Ingrediente> ingredientes;
    private String modoPreparo;
    private Double tempoPreparo;
    private int id;

    public Receita(){
        this.ingredientes = new ArrayList<>();
    }

    public Receita(ReceitaBuilder receitaBuilder) {
        this.titulo = receitaBuilder.getTitulo();
        this.descricao = receitaBuilder.getDescricao();
        this.ingredientes = receitaBuilder.getIngredientes();
        this.modoPreparo = receitaBuilder.getInstrucoes();
        this.tempoPreparo = receitaBuilder.getTempoPreparo();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getModoPreparo() {
        return modoPreparo;
    }

    public void setModoPreparo(String modoPreparo) {
        this.modoPreparo = modoPreparo;
    }

    public Double getTempoPreparo() {
        return tempoPreparo;
    }

    public void setTempoPreparo(Double tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Receita{" +
                "titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", ingredientes=" + ingredientes +
                ", modoPreparo='" + modoPreparo + '\'' +
                ", tempoPreparo=" + tempoPreparo +
                '}';
    }

}
