package controller;

import model.dao.DAOFactory;
import model.dao.interfaces.ReceitaDao;
import model.entities.Receita;
import model.strategies.FilterReceitasByIngredientes;
import model.strategies.FilterReceitasByPage;
import model.strategies.FilterStrategy;
import model.strategies.Filterable;
import view.MainView;
import model.utils.Authenticator;

import java.util.ArrayList;
import java.util.List;

public class MainViewController {

    private MainView mainView;
    private ReceitaDao receitaDao;
    private FilterStrategy filterStrategy;
    private int offset = 0;

    public MainViewController(MainView mainView) {
        this.mainView = mainView;
        this.receitaDao = DAOFactory.createReceitaDao();

        // Inicializar listeners dos botões
        initButtonListeners();

        // Carregar dados iniciais
        mainView.setListaDespensaData(Authenticator.getAuthenticatedUser().getDespensa());
        updateReceitasList(0);
    }

    public void updateReceitasList(int newOffset) {
        offset = newOffset;
        if (filterStrategy == null) {
            filterStrategy = new FilterReceitasByPage();
        }
        List<Filterable> filterableList = receitaDao.filter(filterStrategy, 12, offset);
        ArrayList<Receita> receitaList = new ArrayList<>();
        for (Filterable filterable : filterableList) {
            Receita receita = receitaDao.read(filterable.getId());
            receitaList.add(receita);
        }
        mainView.setListaReceitasData(receitaList);
    }

    private void initButtonListeners() {

        mainView.addLeftButtonListener(e -> {
            if (offset >= 12) {
                updateReceitasList(offset - 12);
            }
        });

        mainView.addRightButtonListener(e -> updateReceitasList(offset + 12));

        mainView.addFilterByIngredienteButtonListener(e -> {
            filterStrategy = new FilterReceitasByIngredientes();
            updateReceitasList(0);
        });
    }
}
