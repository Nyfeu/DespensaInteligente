package controller;

import model.dao.DAOFactory;
import model.dao.interfaces.ReceitaDao;
import model.entities.Receita;
import model.strategies.*;
import view.AuthenticationView;
import view.IngredientView;
import view.MainView;
import model.utils.Authenticator;
import view.utils.FilterListCellRenderer;
import view.utils.viewUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        mainView.addFilterReceitaButtonListener(e -> {

            if (mainView.getDropdown() == 0) filterStrategy = new FilterReceitasByAutor(mainView.getTxtFiltro());
            else filterStrategy = new FilterReceitasByNome(mainView.getTxtFiltro());
            updateReceitasList(0);

        });

        mainView.addClearButtonListener(e -> {
            filterStrategy = new FilterReceitasByPage();
            updateReceitasList(0);
        });

        mainView.addLogoutListener(e -> {
            Authenticator.logout();
            viewUtils.closeView(mainView);
            new AuthenticationView().setVisible(true);
        });

        mainView.addFilterByIngredienteButtonListener(e -> {

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement("Por Ingredientes");
            model.addElement("Por Data de Validade");

            JComboBox<String> cb = new JComboBox<>(model);
            cb.setRenderer(new FilterListCellRenderer());

            int result = JOptionPane.showConfirmDialog(mainView, cb, "Selecione o método de filtragem", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {

                if (Objects.equals(cb.getSelectedItem(), "Por Ingredientes")) filterStrategy = new FilterReceitasByIngredientes();
                else filterStrategy = new FilterReceitasByDataValidadeAndIngredientes();

                updateReceitasList(0);

            }

        });

        mainView.addAddIngredienteButtonListener(e -> {
            IngredientView dialog = new IngredientView(mainView);
            dialog.setVisible(true);
        });


    }
}
