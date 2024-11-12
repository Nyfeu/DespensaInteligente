package client.controller;

import client.facade.ClientFacade;
import server.strategies.Filterable;
import shared.enums.*;
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
import java.util.*;
import java.util.List;

public class MainViewController {

    private MainView mainView;
    private Receita receita;
    private Strategy filterStrategy;
    private int offset = 0, receitasPorPagina = 12;

    public MainViewController(MainView mainView) {
        this.mainView = mainView;

        // Inicializar listeners dos botões
        initButtonListeners();

        // Carregar dados iniciais
        mainView.setListaDespensaData(Authenticator.getAuthenticatedUser().getDespensa());
        updateReceitasList(0, new HashMap<>());
    }

    public void updateReceitasList(int newOffset, Map<String, Object> args) {

        offset = newOffset;
        if (filterStrategy == null) filterStrategy = Strategy.PAGE;

        ClientFacade.sendRequest(Entity.RECEITA, Command.COUNT_ALL, null, responsePacket -> {

            int totalReceitas = (int) responsePacket.getData().get(Attributes.RESULT.getDescription());
            int totalPages = Math.max(1, (totalReceitas + receitasPorPagina - 1) / receitasPorPagina);
            int currentPage = offset / receitasPorPagina + 1;

            // Atualizar a interface com o número de páginas

            SwingUtilities.invokeLater(() -> {

                mainView.setTotalPages(totalPages);
                mainView.setCurrentPage(currentPage);

            });

            // Enviar requisição assíncrona para buscar a lista de receitas

            args.put(Attributes.LIMIT.getDescription(), receitasPorPagina);
            args.put(Attributes.OFFSET.getDescription(), offset);
            args.put(Attributes.STRATEGY.getDescription(), filterStrategy);

            ClientFacade.sendRequest(Entity.RECEITA, Command.FILTER, args, filterResponsePacket -> {

                List<Filterable> filterableList = (List<Filterable>) filterResponsePacket.getData()
                        .get(Attributes.RESULT.getDescription());

                ArrayList<Receita> receitaList   = new ArrayList<>();

                for (Filterable filterable : filterableList) receitaList.add((Receita) filterable);

                SwingUtilities.invokeLater(() -> {

                    mainView.setListaReceitasData(receitaList);
                    mainView.setTotalPages(totalPages);
                    mainView.setCurrentPage(currentPage);

                    boolean isLastPage = currentPage >= totalPages;
                    mainView.setRightButtonEnabled(!isLastPage);
                    mainView.setLeftButtonEnabled(offset > 0);

                });

            });

        });

    }

    private void initButtonListeners() {

        mainView.addLeftButtonListener(e -> {
            if (offset >= receitasPorPagina) {
                updateReceitasList(offset - receitasPorPagina, new HashMap<>());
            }
        });

        mainView.addRightButtonListener(e -> updateReceitasList(offset + receitasPorPagina, new HashMap<>()));

        mainView.addFilterReceitaButtonListener(e -> {

            Map<String, Object> args = new HashMap<>();

            if (mainView.getDropdown() == 0) {

                filterStrategy = Strategy.AUTOR;
                args.put(Attributes.AUTOR.getDescription(), mainView.getTxtFiltro());

            } else {

                filterStrategy = Strategy.NOME;
                args.put(Attributes.NAME.getDescription(), mainView.getTxtFiltro());

            }

            updateReceitasList(0, args);

        });

        mainView.addClearButtonListener(e -> {
            filterStrategy = Strategy.PAGE;
            updateReceitasList(0, new HashMap<>());
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

                if (Objects.equals(cb.getSelectedItem(), LanguageManager.getInstance().getResourceBundle().getString("main.receita.botao.filtrarreceitas.poringrediente"))) filterStrategy = Strategy.INGREDIENTES;
                else filterStrategy = Strategy.DATA_VALIDADE_AND_INGREDIENTES;

                Map<String, Object> args = new HashMap<>();
                args.put(Attributes.USER.getDescription(), Authenticator.getAuthenticatedUser());

                updateReceitasList(0, args);

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

            Usuario usuario = Authenticator.getAuthenticatedUser();

            Ingrediente ingrediente = mainView.getIngredienteSelected();
            if (ingrediente == null) JOptionPane.showMessageDialog(mainView, LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.nenhum.ingrediente"), "ERROR_MESSAGE", JOptionPane.WARNING_MESSAGE);
            else {

                ArrayList<Ingrediente> novaDespensa = usuario.getDespensa();
                novaDespensa.remove(ingrediente);

                Map<String, Object> args = new HashMap<>();
                args.put(Attributes.USER.getDescription(), usuario);
                System.out.println(usuario);

                ClientFacade.sendRequest(Entity.USUARIO, Command.UPDATE, args, responsePacket -> {

                    if (responsePacket.getStatus().equals(Status.SUCCESS)) {

                        mainView.setListaDespensaData(novaDespensa);
                        JOptionPane.showMessageDialog(mainView, LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.removeingrediente.sucesso"), LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.removeingrediente.sucesso.titulo"), JOptionPane.INFORMATION_MESSAGE);

                    } else JOptionPane.showMessageDialog(mainView, LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.removeingrediente.erro"), "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);

                });

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

            String text = LanguageManager.getInstance().getResourceBundle().getString("aboutmsg");

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

                Validator.verifyIngrediente(nomeTxt.getText(), mainView).thenAccept(continuar -> {

                    if (continuar) JOptionPane.showMessageDialog(mainView,LanguageManager.getInstance().getResourceBundle().getString("client.controller.mainview.ingrediente.existente"));

                });

            }

        });

    }

    public void setFilterStrategy(Strategy filterStrategy) {
        this.filterStrategy = filterStrategy;
    }

}
