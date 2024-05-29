package model.entities;

import java.util.ArrayList;
//*******************************************************************//
//OBS: tempoPreparo FOI AMASSADO NO LIXO ; INSERI O ID COMO ATRIBUTO
//*******************************************************************//
public class Receita {
    private String titulo;
    private String descricao;
    private ArrayList<Ingrediente> ingredientes;
    private String modoPreparo;
    private Double tempoPreparo;
    private int id;

    public Receita(){
        this.ingredientes = new ArrayList<>();
    }

    public Receita(String titulo, String descricao, ArrayList<Ingrediente> ingredientes, String modoPreparo, Double tempoPreparo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.ingredientes = ingredientes;
        this.modoPreparo = modoPreparo;
        this.tempoPreparo = tempoPreparo;
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

    public ArrayList<Ingrediente> getIngredientes() {
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
