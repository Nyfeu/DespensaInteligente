package client.view;

import client.controller.ReceitaController;
import shared.entities.Ingrediente;
import shared.entities.Receita;
import client.view.utils.IngredienteReceitaCellRenderer;
import client.view.utils.LanguageManager;
import client.view.utils.ViewUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReceitaView extends JDialog {

    private JTextField txtTitulo, txtDescricao, txtNome, txtQuantidade;
    private JTextArea txtModoPreparo;
    private JButton btnPublicar, btnCancelar, btnIngrediente;
    private MainView mainView;
    private ReceitaController receitaController;
    private JList<Ingrediente> listaIngredientes;
    private ResourceBundle bn;
    private Receita receita;
    private ReceitaDetalhesView receitaDetalhesView;

    public ReceitaView(MainView mainView, Receita receita, ResourceBundle bn) {
        super(mainView, bn.getString("main.receita.botao.publicar.titulo"), true);
        this.mainView = mainView;
        this.receita = receita;
        this.bn = bn;
        this.receitaDetalhesView = null;
        initComponents(bn);
        setLocationRelativeTo(mainView);

        if (receita != null) {
            carregarDadosReceita();
        }
    }

    public ReceitaView(MainView mainView, Receita receita, ResourceBundle bn, ReceitaDetalhesView receitaDetalhesView) {
        super(mainView, bn.getString("main.receita.botao.publicar.titulo"), true);
        this.mainView = mainView;
        this.receita = receita;
        this.bn = bn;
        this.receitaDetalhesView = receitaDetalhesView;
        initComponents(bn);
        setLocationRelativeTo(mainView);

        if (receita != null) {
            carregarDadosReceita();
        }
    }

    private void initComponents(ResourceBundle bn) {

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
        JLabel lblPublish = ViewUtils.createTitleLabel(bn.getString("main.receita.botao.publicar.label.titulo"));
        JLabel lblTitulo = new JLabel(bn.getString("main.receita.botao.publicar.tituloreceita"));
        JLabel lblDescricao = new JLabel(bn.getString("main.receita.botao.publicar.descricaoreceita"));
        JLabel lblModoPreparo = new JLabel(bn.getString("main.receita.botao.publicar.modopreparoreceita"));
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
        txtNome = new JTextField(bn.getString("main.receita.botao.publicar.nomeingrediente"), 20);
        txtNome.setHorizontalAlignment(JTextField.CENTER);
        txtQuantidade = new JTextField(bn.getString("main.receita.botao.publicar.quantidadeingrediente"),20);
        txtQuantidade.setHorizontalAlignment(JTextField.CENTER);
        btnIngrediente = new JButton(bn.getString("main.receita.botao.publicar.adicionaringrediente"));
        panelIngredientesBtn.add(txtNome);
        panelIngredientesBtn.add(txtQuantidade);
        panelIngredientesBtn.add(btnIngrediente);
        listaIngredientes = new JList<>();
        listaIngredientes.setCellRenderer(new IngredienteReceitaCellRenderer(LanguageManager.getInstance().getResourceBundle()));
        JScrollPane scrollPaneIngredientes = new JScrollPane(listaIngredientes);
        panelIngredientes.add(scrollPaneIngredientes, BorderLayout.CENTER);
        panelIngredientes.add(panelIngredientesBtn, BorderLayout.SOUTH);
        panelIngredientes.setBorder(new EmptyBorder(5,5,5,5));
        panelIngredientes.setBackground(Color.gray);

        // Painel Botões
        btnPublicar = new JButton(bn.getString("main.receita.botao.publicar.botao.publicar"));
        btnCancelar = new JButton(bn.getString("main.receita.botao.publicar.botao.cancelar"));
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

    private void carregarDadosReceita() {
        // Preenche os campos com os dados da receita para edição
        txtTitulo.setText(receita.getTitulo());
        txtDescricao.setText(receita.getDescricao());
        txtModoPreparo.setText(receita.getModoPreparo());
        setListaIngredientesData(new ArrayList<>(receita.getIngredientes()));
    }

    private void configureButtons() {
        ViewUtils.configureButton(btnCancelar);
        ViewUtils.customButton1(btnIngrediente);
        ViewUtils.customButton2(btnPublicar);
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
    
    // GETTERS
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
    public String getTxtModoPreparo(){
        return txtModoPreparo.getText();
    }
    public MainView getMainView() {
        return mainView;
    }

    public ReceitaDetalhesView getReceitaDetalhesView() {
        return receitaDetalhesView;
    }

    public Receita getReceita() {
        return receita;
    }

    public List<Ingrediente> getListaIngredientes() {

        List<Ingrediente> list = new ArrayList<>();

        ListModel<Ingrediente> model = listaIngredientes.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            list.add(model.getElementAt(i));
        }

        return list;

    }

    // SETTERS
    public void setTxtNome(String text) {
        txtNome.setText(text);
    }
    public void setTxtQuantidade(String text) {
        txtQuantidade.setText(text);
    }
    public void setListaIngredientesData(List<Ingrediente> ingredientes) {
        DefaultListModel<Ingrediente> model = new DefaultListModel<>();
        for (Ingrediente ingrediente : ingredientes) {
            model.addElement(ingrediente);
        }
        listaIngredientes.setModel(model);
    }

    public boolean isNewRecipe() {
        return receitaDetalhesView == null;
    }
}
