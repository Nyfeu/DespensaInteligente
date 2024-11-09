package client.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ResourceBundle;
import client.view.utils.ViewUtils;
import server.dao.DAOFactory;
import server.dao.interfaces.ReceitaDao;
import shared.entities.Ingrediente;
import shared.entities.Receita;
import client.model.utils.Authenticator;
import client.view.utils.Handler_IO;
import java.awt.event.ActionListener;

public class ReceitaDetalhesView extends JDialog {

    private JLabel lblTitulo, lblDescricao, lblModoPreparo, lblIngredientes;
    private JTextArea ingredientesArea;
    private JButton btnEditar, btnExportar, btnVoltar, btnExcluir;
    private Receita receita; 
    private ResourceBundle bn;

    public ReceitaDetalhesView(JFrame parent, Receita receita, ResourceBundle bn) {
        super(parent, bn.getString("main.receita.detalhes.titulo"), true);
        this.bn = bn;
        initComponents(receita);
        setLocationRelativeTo(parent);
        setSize(400, 400); 
        setResizable(false);
    }

    private void initComponents(Receita receita) {
        // Painel principal 
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(245, 245, 245)); 
        panelPrincipal.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5)); 

        // Painel Título
        lblTitulo = new JLabel("<html><b>" + bn.getString("main.receita.label.titulo") + ":</b> " + receita.getTitulo() + "</html>");
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblTitulo.setBorder(new EmptyBorder(5, 0, 5, 0));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Painel de conteúdo 
        JPanel panelConteudo = new JPanel();
        panelConteudo.setLayout(new BoxLayout(panelConteudo, BoxLayout.Y_AXIS));
        panelConteudo.setBackground(new Color(245, 245, 245));

        // Painel Descrição
        lblDescricao = new JLabel("<html><b>" + bn.getString("main.receita.label.descricao") + ":</b> " + receita.getDescricao() + "</html>");
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 16));
        lblDescricao.setBorder(new EmptyBorder(5, 0, 5, 0));
        panelConteudo.add(lblDescricao);

        // Painel Ingredientes
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

        // Painel Modo de Preparo
        lblModoPreparo = new JLabel("<html><b>" + bn.getString("main.receita.label.modopreparo") + ":</b><br/>" + receita.getModoPreparo().replace("\n", "<br/>") + "</html>");
        lblModoPreparo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblModoPreparo.setBorder(new EmptyBorder(5, 0, 5, 0));
        panelConteudo.add(lblModoPreparo);

        panelPrincipal.add(panelConteudo, BorderLayout.CENTER);

        // Painel de botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnEditar = new JButton(bn.getString("main.receita.botao.editar"));
        btnExportar = new JButton(bn.getString("main.receita.botao.exportar"));
        btnExcluir = new JButton(bn.getString("main.receita.botao.excluir"));
        btnVoltar = new JButton(bn.getString("main.receita.botao.voltar"));
        panelBotoes.setBackground(Color.GRAY);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExportar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnVoltar);

        panelPrincipal.add(panelBotoes, BorderLayout.SOUTH);

        getContentPane().add(panelPrincipal);

        btnEditar.addActionListener(e -> {
            String emailAutor = receita.getEmailAutor();
            String emailUsuario = Authenticator.getAuthenticatedUser().getEmail();

            if (emailAutor.equalsIgnoreCase(emailUsuario)) {
                editarReceitaView(receita);
            } else {
                showNotAuthorMessage();
            }
        });

        btnExportar.addActionListener(e -> exportarReceita(receita));

        btnExcluir.addActionListener(e -> {
            String emailAutor = receita.getEmailAutor();
            String emailUsuario = Authenticator.getAuthenticatedUser().getEmail();

            if (emailAutor.equalsIgnoreCase(emailUsuario)) {
                excluirReceita(receita);
            } else {
                showNotAuthorMessage();
            }
        });

        btnVoltar.addActionListener(e -> SwingUtilities.getWindowAncestor(btnVoltar).dispose());

        configureButtons();
    }

    private void configureButtons() {
        ViewUtils.configureButton(btnEditar);
        ViewUtils.configureButton(btnExportar);
        ViewUtils.configureButton(btnVoltar);
        ViewUtils.configureButton(btnExcluir);
    }

    public void addEditarButtonActionListener(ActionListener listener) {
        btnEditar.addActionListener(listener);
    }
    
    public void addExportarButtonActionListener(ActionListener listener) {
        btnExportar.addActionListener(listener);
    }

    public void addExcluirButtonActionListener(ActionListener listener) {
        btnExcluir.addActionListener(listener);
    }

    public void addVoltarButtonActionListener(ActionListener listener) {
        btnVoltar.addActionListener(listener);
    }

    public void showNotAuthorMessage() {
        JOptionPane.showMessageDialog(this, 
            "Você não é o autor dessa receita.", 
            "Acesso Negado", 
            JOptionPane.WARNING_MESSAGE);
    }

    // PRECISA ARRUMAR ESSE MÉTODO
    public void editarReceitaView(Receita receita) {
        ReceitaView receitaView = new ReceitaView((MainView) getParent(), receita, bn);
        receitaView.setVisible(true);
        atualizarDadosReceita(receita);
        pack();  
    }

    public void atualizarDadosReceita(Receita receita) {
        lblTitulo.setText("<html><b>" + bn.getString("main.receita.label.titulo") + ":</b> " + receita.getTitulo() + "</html>");
        lblDescricao.setText("<html><b>" + bn.getString("main.receita.label.descricao") + ":</b> " + receita.getDescricao() + "</html>");
        lblModoPreparo.setText("<html><b>" + bn.getString("main.receita.label.modopreparo") + ":</b><br/>" + receita.getModoPreparo().replace("\n", "<br/>") + "</html>");
    }

    public void exportarReceita(Receita receita) {
        String directoryPath = "exported_recipes/";
        
        String filePath = directoryPath + receita.getTitulo().replaceAll("[\\\\/:*?\"<>|]", "") + ".txt";  
        Handler_IO<String> handler = new Handler_IO<>(filePath);
    
        StringBuilder conteudoReceita = new StringBuilder();
        conteudoReceita.append("Título: ").append(receita.getTitulo()).append("\n");
        conteudoReceita.append("Descrição: ").append(receita.getDescricao()).append("\n");

        conteudoReceita.append("Ingredientes:\n");
        for (Ingrediente ingrediente : receita.getIngredientes()) {
            conteudoReceita.append("- ").append(ingrediente.getNome())
                           .append(" : ")
                           .append(ingrediente.getQuantidade())
                           .append("\n");
        }

        conteudoReceita.append("Modo de Preparo: ").append(receita.getModoPreparo()).append("\n");
    
        handler.writeFile(conteudoReceita.toString(), false);
    
        JOptionPane.showMessageDialog(this, "Receita exportada para " + filePath, "Exportação Completa", JOptionPane.INFORMATION_MESSAGE);
    }

    private void excluirReceita(Receita receita) {
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza de que deseja excluir esta receita?", "Confirmação de Exclusão", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ReceitaDao receitaDao = DAOFactory.createReceitaDao();
            receitaDao.delete(receita.getId()); // Remove a receita do banco de dados
            JOptionPane.showMessageDialog(this, "Receita excluída com sucesso.", "Exclusão Completa", JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Fecha a tela de detalhes após a exclusão
        }
    }
}
