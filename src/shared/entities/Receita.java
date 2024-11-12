package shared.entities;

import client.model.builder.ReceitaBuilder;
import server.strategies.*;

import java.io.Serializable;
import java.util.List;

public class Receita implements Filterable, Serializable {
    private String titulo;
    private String descricao;
    private List<Ingrediente> ingredientes;
    private String modoPreparo;
    private Double tempoPreparo;
    private Integer id;
    private String emailAutor;

    public Receita(ReceitaBuilder receitaBuilder) {
        this.id = receitaBuilder.getId();
        this.titulo = receitaBuilder.getTitulo();
        this.descricao = receitaBuilder.getDescricao();
        this.ingredientes = receitaBuilder.getIngredientes();
        this.modoPreparo = receitaBuilder.getInstrucoes();
        this.tempoPreparo = receitaBuilder.getTempoPreparo();
        this.emailAutor = receitaBuilder.getEmailAutor();
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

    public void setIngredientes(List<Ingrediente> ingredientes) {
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

    public String getEmailAutor() {
        return emailAutor;
    }

    public void setEmailAutor(String emailAutor) {
        this.emailAutor = emailAutor;
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
