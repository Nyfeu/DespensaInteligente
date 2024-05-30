package app;

import model.dao.DAOFactory;
import model.dao.interfaces.ReceitaDao;
import model.entities.Receita;
import strategies.FilterReceitasByIngredientes;
import strategies.Filterable;
import utils.Authenticator;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {

            boolean logado = Authenticator.login("maria.silva@example.com", "hash_senha_123", null);

            System.out.println(logado ? "Logado" : "Falhou");

            ReceitaDao receitaDao = DAOFactory.createReceitaDao();

            FilterReceitasByIngredientes filterReceitasByIngredientes = new FilterReceitasByIngredientes();
            List<Filterable> receitas = receitaDao.filter(filterReceitasByIngredientes, 10, 0);

            for(Filterable filterable : receitas) {
                Receita receita = receitaDao.read(filterable.getId());
                System.out.println(receita);
            }

            Authenticator.logout();

        } catch (RuntimeException e) {

            System.out.println(e.getMessage());

        }

    }
}