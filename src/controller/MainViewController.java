package controller;

import model.dao.DAOFactory;
import model.dao.interfaces.ReceitaDao;
import model.dao.interfaces.UsuarioDao;
import model.entities.Ingrediente;
import model.entities.Receita;
import model.entities.Usuario;
import model.strategies.*;
import view.AuthenticationView;
import view.IngredienteView;
import view.MainView;
import model.utils.Authenticator;
import view.ReceitaView;
import view.utils.FilterListCellRenderer;
import view.utils.VerifyCadastro;
import view.utils.ViewUtils;

import javax.swing.*;
import java.awt.*;
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
            ViewUtils.closeView(mainView);
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
            IngredienteView dialog = new IngredienteView(mainView, null);
            dialog.setVisible(true);
        });

        mainView.addPublishReceitaButtonListener(e -> {
            ReceitaView dialog = new ReceitaView(mainView);
            dialog.setVisible(true);
        });


        mainView.addUpdateIngredienteListener(e -> {
            Ingrediente ingrediente = mainView.getIngredienteSelected();
            if (ingrediente == null) {
                JOptionPane.showMessageDialog(mainView, "Nenhum ingrediente foi selecionado!", "ERROR_MESSAGE", JOptionPane.WARNING_MESSAGE);
            } else {
                IngredienteView dialog = new IngredienteView(mainView, ingrediente);
                dialog.setVisible(true);
            }
        });

        mainView.addRemoveIngredienteButtonListener(e -> {

            Ingrediente ingrediente = mainView.getIngredienteSelected();
            if (ingrediente == null) {
                JOptionPane.showMessageDialog(mainView, "Nenhum ingrediente foi selecionado!", "ERROR_MESSAGE", JOptionPane.WARNING_MESSAGE);
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
            jPanel.add(new JLabel("Digite a nova senha: "));
            jPanel.add(jPasswordField);
            int option = JOptionPane.showConfirmDialog(mainView, jPanel, "Alteração de senha", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
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

                String novoNome = JOptionPane.showInputDialog(mainView, "Digite o novo nome: ", "");
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
            jPanel.add(new JLabel("Nome: "), gbc);

            JTextField nomeTxt = new JTextField(30);
            gbc.weightx = 0.7;
            gbc.gridx = 1;
            gbc.gridy = 0;
            jPanel.add(nomeTxt, gbc);

            int option = JOptionPane.showConfirmDialog(mainView, jPanel, "Adicionar Ingrediente", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                if (VerifyCadastro.ingrediente(nomeTxt.getText(), mainView)) JOptionPane.showMessageDialog(mainView,"Ingrediente já cadastrado no banco de dados.");
            }

        });

    }
}
