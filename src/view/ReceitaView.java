package view;

import controller.ReceitaController;
import model.entities.Ingrediente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ReceitaView extends JDialog {

    private JTextField txtTitulo, txtDescricao, txtNome, txtQuantidade;
    private JButton btnPublicar, btnCancelar, btnIngrediente;
    private MainView mainView;
    private ReceitaController receitaController;

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
        JPanel panelIngredientes = new JPanel(new BorderLayout());
        JPanel panelIngredientesBtn = new JPanel(new GridLayout(1,3));
        JPanel btnPanel = new JPanel();

        // Painel Receita
        GridBagConstraints gbcReceita = new GridBagConstraints();
        gbcReceita.insets = new Insets(5, 5, 5, 5);

        JLabel lblTitulo = new JLabel("Título:");
        JLabel lblDescricao = new JLabel("Descrição:");
        txtTitulo = new JTextField(50);
        txtDescricao = new JTextField(50);

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

        // Painel Ingredientes
        txtNome = new JTextField(20);
        txtQuantidade = new JTextField(20);
        btnIngrediente = new JButton("Adicionar");
        panelIngredientesBtn.add(txtNome);
        panelIngredientesBtn.add(txtQuantidade);
        panelIngredientesBtn.add(btnIngrediente);
        JList<Ingrediente> listaIngredientes = new JList<>();
        JScrollPane scrollPaneIngredientes = new JScrollPane(listaIngredientes);
        panelIngredientes.add(scrollPaneIngredientes, BorderLayout.CENTER);
        panelIngredientes.add(panelIngredientesBtn, BorderLayout.SOUTH);

        // Painel Botões
        btnPublicar = new JButton("Publicar");
        btnCancelar = new JButton("Cancelar");
        btnPanel.add(btnPublicar);
        btnPanel.add(btnCancelar);

        // Adicionando ao painel principal
        panelPrincipal.add(panelReceita, BorderLayout.NORTH);
        panelPrincipal.add(panelIngredientes, BorderLayout.CENTER);
        panelPrincipal.add(btnPanel, BorderLayout.SOUTH);
        getContentPane().add(panelPrincipal);

        receitaController = new ReceitaController(this);
        pack();
        setResizable(false);
    }

    public void addAdicionarButtonActionListener(ActionListener listener) {
        btnPublicar.addActionListener(listener);
    }

    public void addCancelarButtonActionListener(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }

    public String getTxtTitulo() {
        return txtTitulo.getText();
    }
    public String getTxtDescricao() {
        return txtDescricao.getText();
    }
    public MainView getMainView() {
        return mainView;
    }

}
