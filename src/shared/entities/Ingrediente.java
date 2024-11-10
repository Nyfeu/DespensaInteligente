package shared.entities;

import client.model.builder.IngredienteBuilder;

import java.io.Serializable;
import java.util.Date;

public class Ingrediente implements Serializable {

    private String nome;
    private Integer categoria;
    private Date validade;
    private Integer quantidade;

    public Ingrediente(IngredienteBuilder ingredienteBuilder) {
        this.nome = ingredienteBuilder.getNome();
        this.categoria = ingredienteBuilder.getCategoria();
        this.validade = ingredienteBuilder.getValidade();
        this.quantidade = ingredienteBuilder.getQuantidade();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "nome='" + nome + '\'' +
                ", categoria=" + categoria +
                ", validade=" + validade +
                ", quantidade=" + quantidade +
                '}';
    }

}
