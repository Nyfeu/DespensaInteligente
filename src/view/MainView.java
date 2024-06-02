package view;

import controller.MainViewController;
import model.entities.Ingrediente;
import model.entities.Receita;
import model.utils.Authenticator;
import view.utils.IngredienteCellRenderer;
import view.utils.ReceitaCellRenderer;
import view.utils.ViewUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainView extends JFrame {

    private JList<Ingrediente> listaDespensa;
    private JList<Receita> listaReceitas;
    private JTextField txtFiltro;
    private JButton addIngrediente, removeIngrediente, updateIngrediente, filterByIngrediente, publishReceita, filterReceita, leftBtn, rightBtn, clearBtn;
    private MainViewController mainViewController;
    private JComboBox<String> dropdown;
    private JMenuItem logout, alterarSenhaItem, sobreItem;

    public MainView() {

        initComponents();
        configureMainView();
        addComponentsToPane();
        fetchInitialReceitas();

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
        JLabel titleLabel = ViewUtils.createTitleLabel("Despensa Inteligente");
        add(titleLabel, BorderLayout.NORTH);
        add(splitPane);
    }

    private JPanel createDespensaPanel() {
        JPanel painelDespensa = new JPanel(new BorderLayout());
        JScrollPane scrollPaneDespensa = new JScrollPane(listaDespensa);
        JLabel suaDespensa = createLabel("Sua Despensa");

        JPanel painelDespensaButtons = new JPanel(new FlowLayout());
        painelDespensaButtons.setBackground(Color.GRAY);
        painelDespensaButtons.add(addIngrediente);
        painelDespensaButtons.add(removeIngrediente);
        painelDespensaButtons.add(updateIngrediente);

        painelDespensa.setBorder(new EmptyBorder(10, 10, 10, 5));
        painelDespensa.add(suaDespensa, BorderLayout.NORTH);
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
        painelReceitasButtons.add(rightBtn);
        painelReceitasButtons.add(publishReceita);

        JPanel filterOptionsPanel = createFilterOptionsPanel();

        painelReceitas.setBorder(new EmptyBorder(10, 5, 10, 10));
        painelReceitas.add(filterOptionsPanel, BorderLayout.NORTH);
        painelReceitas.add(scrollPaneReceitas, BorderLayout.CENTER);
        painelReceitas.add(painelReceitasButtons, BorderLayout.SOUTH);

        return painelReceitas;
    }

    private JPanel createFilterOptionsPanel() {
        JPanel filterOptionsPanel = new JPanel();
        filterOptionsPanel.setBackground(Color.GRAY);
        String[] options = {"Autor", "Nome"};
        dropdown = new JComboBox<>(options);
        dropdown.setBackground(Color.white);
        dropdown.setFocusable(false);

        filterOptionsPanel.add(dropdown);
        filterOptionsPanel.add(txtFiltro);
        filterOptionsPanel.add(filterReceita);

        return filterOptionsPanel;
    }

    private void fetchInitialReceitas() {
        mainViewController.updateReceitasList(0);
    }

    private void configureMainView() {

        // Configurações gerais
        setTitle("Despensa Inteligente");
        JLabel titleLabel = ViewUtils.createTitleLabel("Despensa Inteligente");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(titleLabel, BorderLayout.NORTH);

    }

    private void initComponents() {

        // Configuração dos componentes da interface gráfica
        listaDespensa = new JList<>();
        listaReceitas = new JList<>();
        txtFiltro = new JTextField(20);
        addIngrediente = new JButton("Adicionar");
        removeIngrediente = new JButton("Remover");
        filterByIngrediente = new JButton("Filtrar Receitas");
        filterReceita = new JButton("Buscar!");
        publishReceita = new JButton("Publicar!");
        leftBtn = new JButton("<");
        rightBtn = new JButton(">");
        clearBtn = new JButton("Limpar");
        updateIngrediente = new JButton("Atualizar");

        // Configurar componentes
        listaDespensa.setCellRenderer(new IngredienteCellRenderer());
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
        JMenu editMenu = new JMenu("Edit");

        JMenuItem preferenciasItem = new JMenuItem("Preferências");

        editMenu.add(preferenciasItem);

        // Menu "Visualizar"
        JMenu visualizarMenu = new JMenu("Visualizar");

        JMenuItem atualizarItem = new JMenuItem("Atualizar");

        visualizarMenu.add(atualizarItem);

        // Menu "Tools"
        JMenu toolsMenu = new JMenu("Tools");

        JMenuItem calculadoraMedidasItem = new JMenuItem("Calculadora de Medidas");
        JMenuItem planejadorRefeicoesItem = new JMenuItem("Planejador de Refeições");
        JMenuItem listaComprasItem = new JMenuItem("Lista de Compras");

        toolsMenu.add(calculadoraMedidasItem);
        toolsMenu.add(planejadorRefeicoesItem);
        toolsMenu.add(listaComprasItem);

        // Menu "Help"
        JMenu helpMenu = new JMenu("Help");

        JMenuItem documentacaoItem = new JMenuItem("Documentação");
        JMenuItem suporteItem = new JMenuItem("Suporte");
        sobreItem = new JMenuItem("Sobre");

        helpMenu.add(documentacaoItem);
        helpMenu.add(suporteItem);
        helpMenu.add(sobreItem);

        // Menu "Account"
        JMenu accountMenu = new JMenu("Account");

        alterarSenhaItem = new JMenuItem("Alterar Senha");
        JMenuItem alterarDadosItem = new JMenuItem("Alterar Dados");
        logout = new JMenuItem("Logout");

        accountMenu.add(alterarSenhaItem);
        accountMenu.add(alterarDadosItem);
        accountMenu.add(logout);

        // Adicionando os menus à barra de menu
        menuBar.add(editMenu);
        menuBar.add(visualizarMenu);
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

    public int getDropdown() {
        return dropdown.getSelectedIndex();
    }

    public String getTxtFiltro() {
        return txtFiltro.getText();
    }

    public Ingrediente getIngredienteSelected() { return listaDespensa.getSelectedValue(); }
}
