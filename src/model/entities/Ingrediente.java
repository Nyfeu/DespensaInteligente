package model.entities;

import java.util.Date;

public class Ingrediente {

    private String nome;
    private int categoria;
    private Date validade;
    private int quantidade;

    public Ingrediente(){

    }

    public Ingrediente(String nome) {
        this.nome = nome;
    }

    public Ingrediente(String nome, int categoria, Date validade, int quantidade) {
        this.nome = nome;
        this.categoria = categoria;
        this.validade = validade;
        this.quantidade = quantidade;
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
