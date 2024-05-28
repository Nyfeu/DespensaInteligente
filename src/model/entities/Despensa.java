package model.entities;

import java.util.ArrayList;

public class Despensa {
    private ArrayList<Ingrediente> ingredientes;

    public Despensa() {
        this.ingredientes = new ArrayList<>();
    }

    public Despensa(ArrayList<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public ArrayList<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(ArrayList<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }

    @Override
    public String toString() {
        return "Despensa{" +
                "ingredientes=" + ingredientes +
                '}';
    }

    //CRUD AQUI
}