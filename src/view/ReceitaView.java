package view;

import controller.ReceitaController;
import model.entities.Ingrediente;
import view.utils.IngredienteReceitaCellRenderer;
import view.utils.ViewUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class ReceitaView extends JDialog {

    private JTextField txtTitulo, txtDescricao, txtNome, txtQuantidade;
    private JTextArea txtModoPreparo;
    private JButton btnPublicar, btnCancelar, btnIngrediente;
    private MainView mainView;
    private ReceitaController receitaController;
    private JList<Ingrediente> listaIngredientes;

    public ReceitaView(MainView mainView) {
        super(mainView, "Adicionar Receita", true);
        this.mainView = mainView;
        initComponents();
        setLocationRelativeTo(mainView);
    }

    private void initComponents() {

        // Painéis
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelReceita = new JPanel(new GridBagLayout());
        JPanel panelReceitaTitle = new JPanel(new BorderLayout());
        JPanel panelIngredientes = new JPanel(new BorderLayout());
        JPanel panelIngredientesBtn = new JPanel(new GridLayout(1,3));
        JPanel btnPanel = new JPanel(new GridLayout(1,2,5,0));

        // Painel Receita
        GridBagConstraints gbcReceita = new GridBagConstraints();
        gbcReceita.insets = new Insets(5, 5, 5, 5);
        JLabel lblPublish = ViewUtils.createTitleLabel("Publicar Receita");
        JLabel lblTitulo = new JLabel("Título:");
        JLabel lblDescricao = new JLabel("Descrição:");
        JLabel lblModoPreparo = new JLabel("Modo Preparo:");
        txtTitulo = new JTextField(50);
        txtDescricao = new JTextField(50);
        txtModoPreparo = new JTextArea(3, 50);
        JScrollPane txtModoPreparoScroll = new JScrollPane(txtModoPreparo);
        txtModoPreparoScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        txtModoPreparo.setLineWrap(true);
        txtModoPreparo.setWrapStyleWord(true);

        gbcReceita.gridx = 0;
        gbcReceita.gridy = 0;
        gbcReceita.weightx = 0.30;
        panelReceita.add(lblTitulo, gbcReceita);

        gbcReceita.gridx = 1;
        gbcReceita.gridy = 0;
        gbcReceita.weightx = 0.70;
        panelReceita.add(txtTitulo, gbcReceita);

        gbcReceita.gridx = 0;
        gbcReceita.gridy = 1;
        gbcReceita.weightx = 0.30;
        panelReceita.add(lblDescricao, gbcReceita);

        gbcReceita.gridx = 1;
        gbcReceita.gridy = 1;
        gbcReceita.weightx = 0.70;
        panelReceita.add(txtDescricao, gbcReceita);

        gbcReceita.gridx = 0;
        gbcReceita.gridy = 2;
        gbcReceita.weightx = 0.30;
        panelReceita.add(lblModoPreparo, gbcReceita);

        gbcReceita.gridx = 1;
        gbcReceita.gridy = 2;
        gbcReceita.weightx = 0.70;
        panelReceita.add(txtModoPreparoScroll, gbcReceita);

        panelReceita.setBorder(new EmptyBorder(5, 5, 5, 5));

        panelReceitaTitle.add(lblPublish, BorderLayout.NORTH);
        panelReceitaTitle.add(panelReceita, BorderLayout.CENTER);

        // Painel Ingredientes
        txtNome = new JTextField("Nome do ingrediente", 20);
        txtNome.setHorizontalAlignment(JTextField.CENTER);
        txtQuantidade = new JTextField("Quantidade",20);
        txtQuantidade.setHorizontalAlignment(JTextField.CENTER);
        btnIngrediente = new JButton("Adicionar");
        panelIngredientesBtn.add(txtNome);
        panelIngredientesBtn.add(txtQuantidade);
        panelIngredientesBtn.add(btnIngrediente);
        listaIngredientes = new JList<>();
        listaIngredientes.setCellRenderer(new IngredienteReceitaCellRenderer());
        JScrollPane scrollPaneIngredientes = new JScrollPane(listaIngredientes);
        panelIngredientes.add(scrollPaneIngredientes, BorderLayout.CENTER);
        panelIngredientes.add(panelIngredientesBtn, BorderLayout.SOUTH);
        panelIngredientes.setBorder(new EmptyBorder(5,5,5,5));
        panelIngredientes.setBackground(Color.gray);

        // Painel Botões
        btnPublicar = new JButton("Publicar");
        btnCancelar = new JButton("Cancelar");
        btnPanel.setBackground(Color.GRAY);
        btnPanel.setBorder(new EmptyBorder(0, 5, 5, 5));
        btnPanel.add(btnPublicar);
        btnPanel.add(btnCancelar);

        // Adicionando ao painel principal
        panelPrincipal.add(panelReceitaTitle, BorderLayout.NORTH);
        panelPrincipal.add(panelIngredientes, BorderLayout.CENTER);
        panelPrincipal.add(btnPanel, BorderLayout.SOUTH);
        getContentPane().add(panelPrincipal);

        receitaController = new ReceitaController(this);
        configureButtons();
        pack();
        setResizable(false);
    }

    private void configureButtons() {
        ViewUtils.configureButton(btnCancelar);
        ViewUtils.configureButton(btnPublicar);
        ViewUtils.configureButton(btnIngrediente);
        btnIngrediente.setBackground(Color.darkGray);
        btnIngrediente.setForeground(Color.white);
    }

    public void addPublicarButtonActionListener(ActionListener listener) {
        btnPublicar.addActionListener(listener);
    }
    public void addAdicionarButtonActionListener(ActionListener listener) {
        btnIngrediente.addActionListener(listener);
    }
    public void addCancelarButtonActionListener(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }
    public void addNomeFocusListener(FocusListener listener) {
        txtNome.addFocusListener(listener);
    }
    public void addQuantidadeFocusListener(FocusListener listener) {
        txtQuantidade.addFocusListener(listener);
    }
    public String getTxtTitulo() {
        return txtTitulo.getText();
    }
    public String getTxtDescricao() {
        return txtDescricao.getText();
    }
    public String getTxtNome() {
        return txtNome.getText();
    }
    public String getTxtQuantidade() {
        return txtQuantidade.getText();
    }
    public void setTxtNome(String text) {
        txtNome.setText(text);
    }
    public void setTxtQuantidade(String text) {
        txtQuantidade.setText(text);
    }
    public void setListaIngredientesData(ArrayList<Ingrediente> ingredientes) {
        DefaultListModel<Ingrediente> model = new DefaultListModel<>();
        for (Ingrediente ingrediente : ingredientes) {
            model.addElement(ingrediente);
        }
        listaIngredientes.setModel(model);
    }
    public MainView getMainView() {
        return mainView;
    }

}
