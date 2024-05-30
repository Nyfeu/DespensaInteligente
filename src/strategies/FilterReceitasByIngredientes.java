package strategies;

import model.entities.Receita;

import java.sql.Connection;
import java.util.List;

public class FilterReceitasByIngredientes implements FilterStrategy {

    private List<Receita> receitaList;

    public FilterReceitasByIngredientes(List<Receita> receitaList) {
        this.receitaList = receitaList;
    }

    @Override
    public List<Receita> filtrar(List<Filterable> filterableList, Connection conn) {

        // Definir lógica para filtrar receitas por ingredientes

        return null;
    }

}
