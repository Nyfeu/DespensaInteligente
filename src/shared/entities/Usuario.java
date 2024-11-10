package shared.entities;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {
    private String nome;
    private String email;
    private String senha;
    private ArrayList<Ingrediente> despensa;
    private ArrayList<Receita> receitasPublicadas;

    public Usuario(){
        this.receitasPublicadas = new ArrayList<>();
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        despensa = new ArrayList<>();
        receitasPublicadas = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public ArrayList<Ingrediente> getDespensa() {
        return despensa;
    }

    public void setDespensa(ArrayList<Ingrediente> despensa) {
        this.despensa = despensa;
    }

    public ArrayList<Receita> getReceitasPublicadas() {
        return receitasPublicadas;
    }

    public void setReceitasPublicadas(ArrayList<Receita> receitasPublicadas) {
        this.receitasPublicadas = receitasPublicadas;
    }

    public void addIngredienteDespensa(Ingrediente ingrediente) {
        this.despensa.add(ingrediente);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", despensa=" + despensa +
                ", receitasPublicadas=" + receitasPublicadas +
                '}';
    }

}
