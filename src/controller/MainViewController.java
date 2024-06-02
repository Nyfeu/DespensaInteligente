package controller;

import model.dao.implementations.IngredienteDaoJDBC;
import model.dao.interfaces.IngredienteDao;
import model.dao.interfaces.UsuarioDao;
import model.entities.Usuario;
import model.utils.DateParser;
import model.dao.DAOFactory;
import model.dao.interfaces.ReceitaDao;
import model.entities.Ingrediente;
import model.entities.Receita;
import model.strategies.*;
import view.AuthenticationView;
import view.MainView;
import model.utils.Authenticator;
import view.utils.viewUtils;

import javax.swing.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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

        mainView.addAddIngredienteButtonListener(e -> {
            String ingrediente_novo = JOptionPane.showInputDialog("Digite o nome:");
            if (ingrediente_novo == null) return;
            String quantidade_novo = JOptionPane.showInputDialog("Digite a quantidade:");
            int qtd = Integer.parseInt(quantidade_novo);
            String data_nova = JOptionPane.showInputDialog("Digite a data nova:");
            Date data_convertida;
            try {
                data_convertida = DateParser.parseString(data_nova);
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

            IngredienteDao ingredienteDao = DAOFactory.createIngredienteDao();
            ingredienteDao.create(new Ingrediente(ingrediente_novo, 0, data_convertida, qtd));
            UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();
            Usuario user = Authenticator.getAuthenticatedUser();
            ArrayList<Ingrediente> novaDespensa = user.getDespensa();
            novaDespensa.add(new Ingrediente(ingrediente_novo, 0, data_convertida, qtd));
            user.setDespensa(novaDespensa);
            usuarioDao.update(user);






        });

    }
}
