package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ResourceBundle;
import view.utils.ViewUtils;
import model.entities.Ingrediente;
import model.entities.Receita;
import model.utils.Authenticator;
import view.utils.Handler_IO;
import java.awt.event.ActionListener;

public class ReceitaDetalhesView extends JDialog {

    private JLabel lblTitulo, lblDescricao, lblModoPreparo, lblIngredientes;
    private JTextArea ingredientesArea;
    private JButton btnEditar, btnExportar, btnVoltar; 
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

        // Painel de conteúdo 
        JPanel panelConteudo = new JPanel();
        panelConteudo.setLayout(new BoxLayout(panelConteudo, BoxLayout.Y_AXIS));
        panelConteudo.setBackground(new Color(245, 245, 245));

        // Painel Título
        lblTitulo = new JLabel("<html><b>" + bn.getString("main.receita.label.titulo") + ":</b> " + receita.getTitulo() + "</html>");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(new EmptyBorder(5, 0, 5, 0));
        panelConteudo.add(lblTitulo);

        // Painel Descrição
        lblDescricao = new JLabel("<html><b>" + bn.getString("main.receita.label.descricao") + ":</b> " + receita.getDescricao() + "</html>");
        lblDescricao.setFont(new Font("Arial", Font.PLAIN, 16));
        lblDescricao.setBorder(new EmptyBorder(5, 0, 5, 0));
        panelConteudo.add(lblDescricao);

        // Painel Ingredientes
        lblIngredientes = new JLabel("<html><b>" + bn.getString("main.receita.label.ingredientes") + ":</b></html>");
        lblIngredientes.setFont(new Font("Arial", Font.PLAIN, 16));
        lblIngredientes.setBorder(new EmptyBorder(5, 0, 5, 0));
        panelConteudo.add(lblIngredientes);

        ingredientesArea = new JTextArea();
        ingredientesArea.setEditable(false);
        ingredientesArea.setLineWrap(true);
        ingredientesArea.setWrapStyleWord(true);

        for (Ingrediente ingrediente : receita.getIngredientes()) {
            ingredientesArea.append(ingrediente.getNome() + " - " + ingrediente.getQuantidade() + "\n");
        }

        JScrollPane ingredientesScrollPane = new JScrollPane(ingredientesArea);
        ingredientesScrollPane.setPreferredSize(new Dimension(360, 100)); // Tamanho ajustável
        panelConteudo.add(ingredientesScrollPane);

        panelPrincipal.add(panelConteudo, BorderLayout.CENTER);

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
        btnVoltar = new JButton(bn.getString("main.receita.botao.voltar"));
        panelBotoes.setBackground(Color.GRAY);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExportar);
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

        configureButtons();
    }

    private void configureButtons() {
        ViewUtils.configureButton(btnEditar);
        ViewUtils.configureButton(btnExportar);
        ViewUtils.configureButton(btnVoltar);
    }

    public void addEditarButtonActionListener(ActionListener listener) {
        btnEditar.addActionListener(listener);
    }
    
    public void addExportarButtonActionListener(ActionListener listener) {
        btnExportar.addActionListener(listener);
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

    public void editarReceitaView(Receita receita) {
        ReceitaView receitaView = new ReceitaView((MainView) getParent(), receita, bn);
        receitaView.setVisible(true);
        atualizarDadosReceita(receita);  
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
    
}
