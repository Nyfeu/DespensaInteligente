package view;

import model.entities.Ingrediente;
import model.entities.Receita;
import model.utils.Authenticator;
import view.utils.IngredienteCellRenderer;
import view.utils.viewUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class MainView extends JFrame {

    private JList<Ingrediente> listaDespensa;
    private JList<Receita> listaReceitas;
    private JTextField txtFiltro;
    private JButton addIngrediente, removeIngrediente, filterByIngrediente, publishReceita, filterReceita;

    public MainView() {

        // Configurações gerais
        setTitle("Despensa Inteligente");
        JLabel titleLabel = viewUtils.createTitleLabel("Despensa Inteligente");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Criar o JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        SwingUtilities.invokeLater(() -> splitPane.setDividerLocation(0.5));
        splitPane.setDividerSize(0);

        // Criar componentes da lista da despensa
        JPanel painelDespensa = new JPanel(new BorderLayout());
        listaDespensa = new JList<>();
        listaDespensa.setCellRenderer(new IngredienteCellRenderer());
        setListaDespensaData(Authenticator.getAuthenticatedUser().getDespensa());
        JScrollPane scrollPaneDespensa = new JScrollPane(listaDespensa);
        painelDespensa.setBorder(new EmptyBorder(10,10,10,5));
        JLabel suaDespensa = new JLabel("Sua Despensa", SwingConstants.CENTER);
        suaDespensa.setOpaque(true);
        suaDespensa.setBackground(Color.GRAY);
        suaDespensa.setForeground(Color.WHITE);
        suaDespensa.setFont(new Font("Arial", Font.BOLD, 20));
        suaDespensa.setBorder(new EmptyBorder(5,10,5,10));
        addIngrediente = new JButton("Adicionar");
        viewUtils.configureButton(addIngrediente);
        removeIngrediente = new JButton("Remover");
        viewUtils.configureButton(removeIngrediente);
        filterByIngrediente = new JButton("Filtrar Receitas");
        viewUtils.configureButton(filterByIngrediente);
        JPanel painelDespensaButtons = new JPanel(new FlowLayout());
        painelDespensaButtons.setBackground(Color.GRAY);
        painelDespensaButtons.add(addIngrediente);
        painelDespensaButtons.add(removeIngrediente);
        painelDespensaButtons.add(filterByIngrediente);
        painelDespensa.add(painelDespensaButtons, BorderLayout.SOUTH);
        painelDespensa.add(scrollPaneDespensa, BorderLayout.CENTER);
        painelDespensa.add(suaDespensa, BorderLayout.NORTH);

        // Criar componentes da lista de receitas
        JPanel painelReceitas = new JPanel(new BorderLayout());
        listaReceitas = new JList<>();
        JScrollPane scrollPaneReceitas = new JScrollPane(listaReceitas);
        painelReceitas.setBorder(new EmptyBorder(10,5,10,10));
        publishReceita = new JButton("Publicar!");
        viewUtils.configureButton(publishReceita);
        JPanel painelReceitasButtons = new JPanel(new FlowLayout());
        painelReceitasButtons.setBackground(Color.GRAY);
        painelReceitasButtons.add(publishReceita);
        JPanel filterOptionsPanel = new JPanel();
        filterOptionsPanel.setBackground(Color.GRAY);
        String[] options = {"Autor", "Nome"};
        JComboBox<String> dropdown = new JComboBox<>(options);
        dropdown.setBackground(Color.white);
        dropdown.setFocusable(false);
        filterOptionsPanel.add(dropdown);
        txtFiltro = new JTextField(20);
        filterOptionsPanel.add(txtFiltro);
        filterReceita = new JButton("Buscar!");
        viewUtils.configureButton(filterReceita);
        filterOptionsPanel.add(filterReceita);

        // Adicione outros componentes, como filtros e botões para publicar novas receitas
        painelReceitas.add(filterOptionsPanel, BorderLayout.NORTH);
        painelReceitas.add(scrollPaneReceitas, BorderLayout.CENTER);
        painelReceitas.add(painelReceitasButtons, BorderLayout.SOUTH);

        // Adicionar os painéis ao JSplitPane
        splitPane.setLeftComponent(painelDespensa);
        splitPane.setRightComponent(painelReceitas);

        // Colocando na View corretamente
        add(titleLabel, BorderLayout.NORTH);
        add(splitPane);
    }

    private void setListaDespensaData(ArrayList<Ingrediente> ingredientes) {
        DefaultListModel<Ingrediente> model = new DefaultListModel<>();
        for (Ingrediente ingrediente : ingredientes) {
            model.addElement(ingrediente);
        }
        listaDespensa.setModel(model);
    }

}
