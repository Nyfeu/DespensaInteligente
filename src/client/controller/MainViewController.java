package client.controller;

import server.dao.DAOFactory;
import server.dao.interfaces.ReceitaDao;
import server.dao.interfaces.UsuarioDao;
import shared.entities.Ingrediente;
import shared.entities.Receita;
import shared.entities.Usuario;
import client.view.AuthenticationView;
import client.view.IngredienteView;
import client.view.MainView;
import client.model.utils.Authenticator;
import client.view.ReceitaView;
import client.view.utils.FilterListCellRenderer;
import client.view.utils.LanguageManager;
import client.view.utils.Validator;
import client.view.utils.ViewUtils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import server.strategies.*;

public class MainViewController {

    private MainView mainView;
    private ReceitaDao receitaDao;
    private Receita receita;
    private FilterStrategy filterStrategy;
    private int offset = 0, receitasPorPagina = 12;

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

        int totalReceitas = receitaDao.countAll();
        int totalPages = (int) Math.ceil((double) totalReceitas / receitasPorPagina);
        int currentPage = offset / receitasPorPagina + 1;

        List<Filterable> filterableList = receitaDao.filter(filterStrategy, 12, offset);
        ArrayList<Receita> receitaList = new ArrayList<>();

        for (Filterable filterable : filterableList) {
            Receita receita = receitaDao.read(filterable.getId());
            receitaList.add(receita);
        }
        mainView.setListaReceitasData(receitaList);

        mainView.setTotalPages(totalPages);
        mainView.setCurrentPage(currentPage);

        boolean isLastPage = filterableList.size() < receitasPorPagina;
        mainView.setRightButtonEnabled(!isLastPage);
        mainView.setLeftButtonEnabled(offset > 0);
    }

    private void initButtonListeners() {

        mainView.addLeftButtonListener(e -> {
            if (offset >= receitasPorPagina) {
                updateReceitasList(offset - receitasPorPagina);
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
            ViewUtils.closeView(mainView);
            new AuthenticationView().setVisible(true);
        });

        mainView.addFilterByIngredienteButtonListener(e -> {

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            model.addElement(LanguageManager.getInstance().getResourceBundle().getString("main.receita.botao.filtrarreceitas.poringrediente"));
            model.addElement(LanguageManager.getInstance().getResourceBundle().getString("main.receita.botao.filtrarreceitas.pordatadevalidade"));

            JComboBox<String> cb = new JComboBox<>(model);
            cb.setRenderer(new FilterListCellRenderer());

            URL iconUrl = getClass().getResource("/client/resources/images/filtro.png");
            ImageIcon customIcon = null;
            if (iconUrl != null) {
                customIcon = new ImageIcon(iconUrl);
            } else {
                System.out.println("Icon not found at specified path.");
            }

            int result = JOptionPane.showConfirmDialog(mainView, cb, LanguageManager.getInstance().getResourceBundle().getString("main.receita.botao.filtrarreceitas.titulo"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, customIcon);
            if (result == JOptionPane.OK_OPTION) {

                if (Objects.equals(cb.getSelectedItem(), LanguageManager.getInstance().getResourceBundle().getString("main.receita.botao.filtrarreceitas.poringrediente"))) filterStrategy = new FilterReceitasByIngredientes();
                else filterStrategy = new FilterReceitasByDataValidadeAndIngredientes();

                updateReceitasList(0);

            }

        });

        mainView.addAddIngredienteButtonListener(e -> {
            IngredienteView dialog = new IngredienteView(mainView, null,LanguageManager.getInstance().getResourceBundle());
            dialog.setVisible(true);
        });

        mainView.addPublishReceitaButtonListener(e -> {
            ReceitaView dialog = new ReceitaView(mainView, receita, LanguageManager.getInstance().getResourceBundle());
            dialog.setVisible(true);
        });


        mainView.addUpdateIngredienteListener(e -> {
            Ingrediente ingrediente = mainView.getIngredienteSelected();
            if (ingrediente == null) {
                JOptionPane.showMessageDialog(mainView, LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.nenhum.ingrediente"), "ERROR_MESSAGE", JOptionPane.WARNING_MESSAGE);
            } else {
                IngredienteView dialog = new IngredienteView(mainView, ingrediente,LanguageManager.getInstance().getResourceBundle());
                dialog.setVisible(true);
            }
        });

        mainView.addRemoveIngredienteButtonListener(e -> {

            Ingrediente ingrediente = mainView.getIngredienteSelected();
            if (ingrediente == null) {
                JOptionPane.showMessageDialog(mainView, LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.nenhum.ingrediente"), "ERROR_MESSAGE", JOptionPane.WARNING_MESSAGE);
            } else {
                UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();
                Usuario usuario = Authenticator.getAuthenticatedUser();
                ArrayList<Ingrediente> novaDespensa = usuario.getDespensa();
                novaDespensa.remove(ingrediente);
                usuarioDao.update(usuario);
                mainView.setListaDespensaData(novaDespensa);
            }

        });

        mainView.addAlterarSenhaListener(e -> {
            JPasswordField jPasswordField = new JPasswordField(10);
            JPanel jPanel = new JPanel();
            jPanel.add(new JLabel(LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.alterar.senha")+ " "));
            jPanel.add(jPasswordField);
            int option = JOptionPane.showConfirmDialog(mainView, jPanel, LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.alterar.senha.titulo"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                String password = new String(jPasswordField.getPassword()).trim();
                Authenticator.changePassword(password);
            }

        });

        mainView.addSobreListener(e -> {

            String text = """
                    DespensaInteligente é um aplicativo intuitivo que organiza sua\s
                    despensa e oferece receitas personalizadas com base nos ingredientes\s
                    disponíveis. Gerencie sua despensa, descubra novas receitas e \s
                    compartilhe suas criações com facilidade. Desfrute de uma experiência \s
                    simplificada de culinária e organização de alimentos.
                    
                    Equipe: André Maiolini, Durval Consorti e Leonardo Amadio.
                    
                    Versão: 1.0 (2024).""";

            JOptionPane.showMessageDialog(mainView, text, JOptionPane.MESSAGE_PROPERTY, JOptionPane.INFORMATION_MESSAGE);

        });

        mainView.addAlterarDadosListener(e -> {

            try {

                String novoNome = JOptionPane.showInputDialog(mainView, LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.altera.nome") + " ", "");
                if (!novoNome.isEmpty()) Authenticator.changeNome(novoNome.trim());

            } catch (RuntimeException exception) {

                System.out.println(exception.getMessage());

            }

        });

        mainView.addIngredientePadrao(e -> {

            JPanel jPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            gbc.weightx = 0.3;
            gbc.gridx = 0;
            gbc.gridy = 0;
            jPanel.add(new JLabel(LanguageManager.getInstance().getResourceBundle().getString("main.despensa.ingrediente.nome") + " "), gbc);

            JTextField nomeTxt = new JTextField(30);
            gbc.weightx = 0.7;
            gbc.gridx = 1;
            gbc.gridy = 0;
            jPanel.add(nomeTxt, gbc);

            int option = JOptionPane.showConfirmDialog(mainView, jPanel, LanguageManager.getInstance().getResourceBundle().getString("main.despensa.botao.adicionar.titulo"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                if (Validator.verifyIngrediente(nomeTxt.getText(), mainView)) JOptionPane.showMessageDialog(mainView,LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.ingrediente.existente"));
            }

        });

    }
}
