package client.model.builder;

import shared.entities.Ingrediente;
import shared.entities.Receita;

import java.util.List;

public class ReceitaBuilder {

    private int id;
    private String titulo;
    private String descricao;
    private List<Ingrediente> ingredientes;
    private String instrucoes;
    private Double tempoPreparo;
    private String emailAutor;


    public ReceitaBuilder() {
    }
    public ReceitaBuilder(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Double getTempoPreparo() {
        return tempoPreparo;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getInstrucoes() {
        return instrucoes;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getEmailAutor() {
        return emailAutor;
    }

    public ReceitaBuilder id(int id) {
        this.id = id;
        return this;
    }

    public ReceitaBuilder titulo(String titulo) {
        this.titulo = titulo;
        return this;
    }

    public ReceitaBuilder descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public ReceitaBuilder ingredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
        return this;
    }

    public ReceitaBuilder instrucoes(String instrucoes) {
        this.instrucoes = instrucoes;
        return this;
    }

    public ReceitaBuilder tempoPreparo(Double tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
        return this;
    }

    public ReceitaBuilder emailAutor(String autor) {
        this.emailAutor = autor;
        return this;
    }

    public Receita build() {
        return new Receita(this);
    }

}
