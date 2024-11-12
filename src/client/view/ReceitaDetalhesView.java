package client.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ResourceBundle;

import client.controller.ReceitaDetalhesController;
import client.view.utils.LanguageManager;
import client.view.utils.ViewUtils;
import shared.entities.Receita;
import java.awt.event.ActionListener;

public class ReceitaDetalhesView extends JDialog {

    private JLabel lblTitulo, lblDescricao, lblModoPreparo, lblIngredientes;
    private JButton btnEditar, btnVoltar, btnExcluir;
    private JMenu menuExportar;
    private JMenuItem exporta_txt, exporta_pdf;
    private ReceitaDetalhesController receitaDetalhesController;
    private ResourceBundle bn;
    private MainView mainView;

    public ReceitaDetalhesView(MainView parent, Receita receita, ResourceBundle bn) {
        super(parent, bn.getString("main.receita.detalhes.titulo"), true);
        this.bn = bn;
        this.mainView = parent;
        initComponents(receita);
        setLocationRelativeTo(parent);
        setSize(400, 400);
        setResizable(false);
    }

    private void initComponents(Receita receita) {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(245, 245, 245));
        panelPrincipal.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuExportar = new JMenu(bn.getString("main.receita.botao.exportar"));
        menuBar.add(menuExportar);
        exporta_pdf = new JMenuItem("PDF");
        exporta_txt = new JMenuItem(LanguageManager.getInstance().getResourceBundle().getString("txttexto"));
        menuExportar.add(exporta_pdf);
        menuExportar.add(exporta_txt);

        lblTitulo = new JLabel("<html><b>" + bn.getString("main.receita.label.titulo") + ":</b> " + receita.getTitulo() + "</html>");
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTitulo.setBorder(new EmptyBorder(5, 0, 5, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelConteudo = new JPanel();
        panelConteudo.setLayout(new BoxLayout(panelConteudo, BoxLayout.Y_AXIS));
        panelConteudo.setBackground(new Color(245, 245, 245));

        lblDescricao = new JLabel("<html><b>" + bn.getString("main.receita.label.descricao") + ":</b> " + receita.getDescricao() + "</html>");
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 16));
        lblDescricao.setBorder(new EmptyBorder(5, 0, 5, 0));
        panelConteudo.add(lblDescricao);

        lblIngredientes = new JLabel("<html><b>" + bn.getString("main.receita.label.ingredientes") + ":</b></html>");
        lblIngredientes.setFont(new Font("Arial", Font.PLAIN, 16));
        lblIngredientes.setBorder(new EmptyBorder(10, 0, 5, 0));
        panelConteudo.add(lblIngredientes);

        JList<String> ingredientList = new JList<>(receita.getIngredientes().stream()
            .map(ing -> ing.getNome() + " - " + ing.getQuantidade())
            .toArray(String[]::new));
        ingredientList.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        ingredientList.setFont(new Font("Arial", Font.PLAIN, 14));
        ingredientList.setVisibleRowCount(4);
        ingredientList.setFixedCellHeight(16);
        panelConteudo.add(new JScrollPane(ingredientList));

        lblModoPreparo = new JLabel("<html><b>" + bn.getString("main.receita.label.modopreparo") + ":</b><br/>" + receita.getModoPreparo().replace("\n", "<br/>") + "</html>");
        lblModoPreparo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblModoPreparo.setBorder(new EmptyBorder(5, 0, 5, 0));
        panelConteudo.add(lblModoPreparo);

        panelPrincipal.add(panelConteudo, BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnEditar = new JButton(bn.getString("main.receita.botao.editar"));
        btnExcluir = new JButton(bn.getString("main.receita.botao.excluir"));
        btnVoltar = new JButton(bn.getString("main.receita.botao.voltar"));
        panelBotoes.setBackground(Color.GRAY);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnVoltar);

        panelPrincipal.add(panelBotoes, BorderLayout.SOUTH);
        getContentPane().add(panelPrincipal);
        receitaDetalhesController = new ReceitaDetalhesController(this, receita, bn);
        configureButtons();
    }

    private void configureButtons() {
        ViewUtils.configureButton(btnEditar);
        ViewUtils.configureButton(btnVoltar);
        ViewUtils.configureButton(btnExcluir);
    }

    public void addEditarButtonActionListener(ActionListener listener) {
        btnEditar.addActionListener(listener);
    }

    public void addExportarTXTMenuItemActionListener(ActionListener listener) {
        exporta_txt.addActionListener(listener);
    }

    public void addExportarPDFMenuItemButtonActionListener(ActionListener listener) {
        exporta_pdf.addActionListener(listener);
    }

    public void addExcluirButtonActionListener(ActionListener listener) {
        btnExcluir.addActionListener(listener);
    }

    public void addVoltarButtonActionListener(ActionListener listener) {
        btnVoltar.addActionListener(listener);
    }

    public void setTitulo(String titulo) {
        lblTitulo.setText(titulo);
    }

    public void setDescricao(String descricao) {
        lblDescricao.setText(descricao);
    }

    public void setModoPreparo(String modoPreparo) {
        lblModoPreparo.setText(modoPreparo);
    }

    public MainView getMainView() {
        return mainView;
    }
}
