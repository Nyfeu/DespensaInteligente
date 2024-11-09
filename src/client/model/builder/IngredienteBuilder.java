package client.model.builder;

import shared.entities.Ingrediente;

import java.util.Date;

public class IngredienteBuilder {

    private String nome;
    private Integer categoria;
    private Date validade;
    private Integer quantidade;

    public String getNome() {
        return nome;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Date getValidade() {
        return validade;
    }

    public IngredienteBuilder nome(String nome) {
        this.nome = nome;
        return this;
    }

    public IngredienteBuilder categoria(int categoria) {
        this.categoria = categoria;
        return this;
    }

    public IngredienteBuilder validade(Date validade) {
        this.validade = validade;
        return this;
    }

    public IngredienteBuilder quantidade(int quantidade) {
        this.quantidade = quantidade;
        return this;
    }

    public Ingrediente build() {
        return new Ingrediente(this);
    }

}
