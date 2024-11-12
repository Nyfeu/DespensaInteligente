package client.view;

import client.controller.MainViewController;
import shared.entities.Ingrediente;
import shared.entities.Receita;
import client.model.utils.Authenticator;
import client.view.utils.IngredienteCellRenderer;
import client.view.utils.LanguageManager;
import client.view.utils.ReceitaCellRenderer;
import client.view.utils.ViewUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainView extends JFrame {

    private JList<Ingrediente> listaDespensa;
    private JList<Receita> listaReceitas;
    private JTextField txtFiltro;
    private JLabel pageLabel;
    private int totalPages = 1, currentPage = 1;
    private JButton addIngrediente, removeIngrediente, updateIngrediente, filterByIngrediente, publishReceita, filterReceita, leftBtn, rightBtn, clearBtn;
    private MainViewController mainViewController;
    private JComboBox<String> dropdown;
    private JMenuItem logout, alterarSenhaItem, sobreItem, alterarDadosItem, addIngredientePadrao;
    private ResourceBundle bn;

    private JLabel titulo_mainView, sua_despensa;

    public MainView() {
        this.bn = LanguageManager.getInstance().getResourceBundle();
        initComponents();
        configureMainView();
        addComponentsToPane();
        fetchInitialReceitas();
        acaoDuploClique();
    }

    private void acaoDuploClique() {
        listaReceitas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Detecta um duplo clique
                    Receita receitaSelecionada = listaReceitas.getSelectedValue();
                    if (receitaSelecionada != null) {
                        // Exibe os detalhes da receita em uma nova janela
                        mostrarDetalhesReceita(receitaSelecionada);
                    }
                }
            }
        });    
    }
    
    private void mostrarDetalhesReceita(Receita receita) {
    ReceitaDetalhesView detalhesView = new ReceitaDetalhesView(this, receita, bn);
    detalhesView.setVisible(true);
}

    private void addComponentsToPane() {
        // Criar e configurar o JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(0);
        splitPane.setLeftComponent(createDespensaPanel());
        splitPane.setRightComponent(createReceitasPanel());

        // Posicionar o divisor do JSplitPane ao centro
        SwingUtilities.invokeLater(() -> splitPane.setDividerLocation(0.5));

        // Adicionar componentes ao frame principal
        titulo_mainView = ViewUtils.createTitleLabel(bn.getString("main.label.titulo"));
        add(titulo_mainView, BorderLayout.NORTH);
        add(splitPane);
    }

    private JPanel createDespensaPanel() {
        JPanel painelDespensa = new JPanel(new BorderLayout());
        JScrollPane scrollPaneDespensa = new JScrollPane(listaDespensa);
        sua_despensa = createLabel(bn.getString("main.despensa.titulo"));

        JPanel painelDespensaButtons = new JPanel(new FlowLayout());
        painelDespensaButtons.setBackground(Color.GRAY);
        painelDespensaButtons.add(addIngrediente);
        painelDespensaButtons.add(removeIngrediente);
        painelDespensaButtons.add(updateIngrediente);

        painelDespensa.setBorder(new EmptyBorder(10, 10, 10, 5));
        painelDespensa.add(sua_despensa, BorderLayout.NORTH);
        painelDespensa.add(scrollPaneDespensa, BorderLayout.CENTER);
        painelDespensa.add(painelDespensaButtons, BorderLayout.SOUTH);

        setListaDespensaData(Authenticator.getAuthenticatedUser().getDespensa());
        return painelDespensa;
    }

    private JPanel createReceitasPanel() {
        JPanel painelReceitas = new JPanel(new BorderLayout());
        JScrollPane scrollPaneReceitas = new JScrollPane(listaReceitas);
        JPanel painelReceitasButtons = new JPanel(new FlowLayout());
        
        painelReceitasButtons.setBackground(Color.GRAY);
        painelReceitasButtons.add(filterByIngrediente);
        painelReceitasButtons.add(clearBtn);
        painelReceitasButtons.add(leftBtn);
        painelReceitasButtons.add(pageLabel);
        painelReceitasButtons.add(rightBtn);
        painelReceitasButtons.add(publishReceita);

        JPanel filterOptionsPanel = createFilterOptionsPanel();

        painelReceitas.setBorder(new EmptyBorder(10, 5, 10, 10));
        painelReceitas.add(filterOptionsPanel, BorderLayout.NORTH);
        painelReceitas.add(scrollPaneReceitas, BorderLayout.CENTER);
        painelReceitas.add(painelReceitasButtons, BorderLayout.SOUTH);

        return painelReceitas;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        updatePageLabel();
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        updatePageLabel();
    }

    private void updatePageLabel() {
        pageLabel.setText(currentPage + "/" + totalPages);
        pageLabel.setForeground(Color.WHITE);
    }

    public void setLeftButtonEnabled(boolean enabled) {
        leftBtn.setEnabled(enabled);
    }

    public void setRightButtonEnabled(boolean enabled) {
        rightBtn.setEnabled(enabled);
    }

    private JPanel createFilterOptionsPanel() {
        JPanel filterOptionsPanel = new JPanel();
        filterOptionsPanel.setBackground(Color.GRAY);
        String[] options = {bn.getString("main.receita.autor"), bn.getString("main.receita.nome")};
        dropdown = new JComboBox<>(options);
        dropdown.setBackground(Color.white);
        dropdown.setFocusable(false);

        filterOptionsPanel.add(dropdown);
        filterOptionsPanel.add(txtFiltro);
        filterOptionsPanel.add(filterReceita);

        return filterOptionsPanel;
    }

    private void fetchInitialReceitas() {
        mainViewController.updateReceitasList(0, new HashMap<>());
    }

    private void configureMainView() {
        // Configurações gerais
        setTitle(bn.getString("main.titulo"));
        setSize(1200,710);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLabel titleLabel = ViewUtils.createTitleLabel(bn.getString("main.label.titulo"));
        add(titleLabel, BorderLayout.NORTH);

        setVisible(true);
    }

    private void initComponents() {
        
        pageLabel = new JLabel(currentPage + "/" + totalPages, SwingConstants.CENTER);
        
        // Configuração dos componentes da interface gráfica
        listaDespensa = new JList<>();
        listaReceitas = new JList<>();
        txtFiltro = new JTextField(20);
        addIngrediente = new JButton(bn.getString("main.despensa.botao.adicionar"));
        removeIngrediente = new JButton(bn.getString("main.despensa.botao.remover"));
        filterByIngrediente = new JButton(bn.getString("main.receita.botao.filtrarreceitas"));
        filterReceita = new JButton(bn.getString("main.receita.botao.buscar"));
        publishReceita = new JButton(bn.getString("main.receita.botao.publicar"));
        leftBtn = new JButton("<");
        rightBtn = new JButton(">");
        clearBtn = new JButton(bn.getString("main.receita.botao.limpar"));
        updateIngrediente = new JButton(bn.getString("main.despensa.botao.atualizar"));

        // Configurar componentes
        listaDespensa.setCellRenderer(new IngredienteCellRenderer(bn));
        listaReceitas.setCellRenderer(new ReceitaCellRenderer());

        // Adicionando JMenuBar
        setJMenuBar(createMenuBar());

        // Inicializando o controlador
        mainViewController = new MainViewController(this);

        // Configurando a aparência dos botões
        configureButtons();   
    }

    private JMenuBar createMenuBar() {

        JMenuBar menuBar = new JMenuBar();

        // Menu "Edit"
        JMenu editMenu = new JMenu(bn.getString("main.menu.edit"));

        JMenuItem preferenciasItem = new JMenuItem(bn.getString("main.menu.edit.preferencias"));
        addIngredientePadrao = new JMenuItem(bn.getString("main.menu.edit.adicionaringrediente"));

        editMenu.add(preferenciasItem);
        editMenu.add(addIngredientePadrao);

        // Menu "Tools"
        JMenu toolsMenu = new JMenu(bn.getString("main.menu.tools"));

        JMenuItem calculadoraMedidasItem = new JMenuItem(bn.getString("main.menu.tools.calculadorademedidas"));
        JMenuItem planejadorRefeicoesItem = new JMenuItem(bn.getString("main.menu.tools.planejadorderefeicoes"));
        JMenuItem listaComprasItem = new JMenuItem(bn.getString("main.menu.tools.listadecompras"));

        toolsMenu.add(calculadoraMedidasItem);
        toolsMenu.add(planejadorRefeicoesItem);
        toolsMenu.add(listaComprasItem);

        // Menu "Help"
        JMenu helpMenu = new JMenu(bn.getString("main.menu.help"));

        JMenuItem documentacaoItem = new JMenuItem(bn.getString("main.menu.help.documentacao"));
        JMenuItem suporteItem = new JMenuItem(bn.getString("main.menu.help.suporte"));
        sobreItem = new JMenuItem(bn.getString("main.menu.help.sobre"));

        helpMenu.add(documentacaoItem);
        helpMenu.add(suporteItem);
        helpMenu.add(sobreItem);

        // Menu "Account"
        JMenu accountMenu = new JMenu(bn.getString("main.menu.account"));

        alterarSenhaItem = new JMenuItem(bn.getString("main.menu.account.alterarsenha"));
        alterarDadosItem = new JMenuItem(bn.getString("main.menu.account.alterardados"));
        logout = new JMenuItem(bn.getString("main.menu.account.logout"));

        accountMenu.add(alterarSenhaItem);
        accountMenu.add(alterarDadosItem);
        accountMenu.add(logout);

        // Adicionando os menus à barra de menu
        menuBar.add(editMenu);
        menuBar.add(toolsMenu);
        menuBar.add(helpMenu);
        menuBar.add(accountMenu);

        return menuBar;

    }

    private void configureButtons() {
        ViewUtils.configureButton(addIngrediente);
        ViewUtils.configureButton(removeIngrediente);
        ViewUtils.configureButton(filterByIngrediente);
        ViewUtils.configureButton(publishReceita);
        ViewUtils.configureButton(leftBtn);
        ViewUtils.configureButton(rightBtn);
        ViewUtils.configureButton(filterReceita);
        ViewUtils.configureButton(clearBtn);
        ViewUtils.configureButton(updateIngrediente);
    }

    public void setListaDespensaData(ArrayList<Ingrediente> ingredientes) {
        DefaultListModel<Ingrediente> model = new DefaultListModel<>();
        for (Ingrediente ingrediente : ingredientes) {
            model.addElement(ingrediente);
        }
        listaDespensa.setModel(model);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.GRAY);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setBorder(new EmptyBorder(5, 10, 5, 10));
        return label;
    }

    public void setListaReceitasData(ArrayList<Receita> receitas) {
        DefaultListModel<Receita> model = new DefaultListModel<>();
        for (Receita receita : receitas) {
            model.addElement(receita);
        }
        listaReceitas.setModel(model);
    }

    public void addLeftButtonListener(ActionListener listener) {
        leftBtn.addActionListener(listener);
    }

    public void addRightButtonListener(ActionListener listener) {
        rightBtn.addActionListener(listener);
    }

    public void addAddIngredienteButtonListener(ActionListener listener) {
        addIngrediente.addActionListener(listener);
    }

    public void addRemoveIngredienteButtonListener(ActionListener listener) {
        removeIngrediente.addActionListener(listener);
    }

    public void addFilterByIngredienteButtonListener(ActionListener listener) {
        filterByIngrediente.addActionListener(listener);
    }

    public void addPublishReceitaButtonListener(ActionListener listener) {
        publishReceita.addActionListener(listener);
    }

    public void addFilterReceitaButtonListener(ActionListener listener) {
        filterReceita.addActionListener(listener);
    }

    public void addClearButtonListener(ActionListener listener) {
        clearBtn.addActionListener(listener);
    }

    public void addLogoutListener(ActionListener listener) {
        logout.addActionListener(listener);
    }

    public void addUpdateIngredienteListener(ActionListener listener) {
        updateIngrediente.addActionListener(listener);
    }

    public void addAlterarSenhaListener(ActionListener listener) {
        alterarSenhaItem.addActionListener(listener);
    }

    public void addSobreListener(ActionListener listener) {
        sobreItem.addActionListener(listener);
    }

    public void addAlterarDadosListener(ActionListener listener) {
        alterarDadosItem.addActionListener(listener);
    }

    public void addIngredientePadrao(ActionListener listener) {
        addIngredientePadrao.addActionListener(listener);
    }

    public int getDropdown() {
        return dropdown.getSelectedIndex();
    }

    public String getTxtFiltro() {
        return txtFiltro.getText();
    }

    public Ingrediente getIngredienteSelected() { return listaDespensa.getSelectedValue(); }

    public MainViewController getMainViewController() {
        return mainViewController;
    }
}
