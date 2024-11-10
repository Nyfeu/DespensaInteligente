package client.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.FileOutputStream;
import java.util.ResourceBundle;
import client.view.utils.ViewUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
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
    private JButton btnEditar, btnVoltar, btnExcluir;
    private JMenu menuExportar;
    private JMenuItem exporta_txt, exporta_pdf;
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

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuExportar = new JMenu(bn.getString("main.receita.botao.exportar"));
        menuBar.add(menuExportar);
        exporta_pdf = new JMenuItem("PDF");
        exporta_txt = new JMenuItem("Texto");
        menuExportar.add(exporta_pdf);
        menuExportar.add(exporta_txt);



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
        btnExcluir = new JButton(bn.getString("main.receita.botao.excluir"));
        btnVoltar = new JButton(bn.getString("main.receita.botao.voltar"));
        panelBotoes.setBackground(Color.GRAY);
        panelBotoes.add(btnEditar);
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

        exporta_txt.addActionListener(e -> exportarReceita(receita));

        exporta_pdf.addActionListener(e -> exportarReceitaParaPDF(receita));

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
        ViewUtils.configureButton(btnVoltar);
        ViewUtils.configureButton(btnExcluir);
    }

    public void addEditarButtonActionListener(ActionListener listener) {
        btnEditar.addActionListener(listener);
    }
    
    public void addExportarMenuItemActionListener(ActionListener listener) {
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

    public void showNotAuthorMessage() {
        JOptionPane.showMessageDialog(this, 
            bn.getString("main.receita.exibe.msg.autor"),
            bn.getString("main.receita.exibe.msg.autor.titulo"),
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
        conteudoReceita.append(bn.getString("main.receita.renderer.titulo")).append(receita.getTitulo()).append("\n");
        conteudoReceita.append(bn.getString("main.receita.renderer.descricao")).append(receita.getDescricao()).append("\n");

        conteudoReceita.append(bn.getString("main.receita.exibe.ingredientes")+ "\n");
        for (Ingrediente ingrediente : receita.getIngredientes()) {
            conteudoReceita.append("- ").append(ingrediente.getNome())
                           .append(" : ")
                           .append(ingrediente.getQuantidade())
                           .append("\n");
        }

        conteudoReceita.append(bn.getString("main.receita.exibe.modopreparo")+ " ").append(receita.getModoPreparo()).append("\n");
    
        handler.writeFile(conteudoReceita.toString(), false);
    
        JOptionPane.showMessageDialog(this, bn.getString("main.receita.exibe.msg")+ " " + filePath, bn.getString("main.receita.exibe.msg.titulo"), JOptionPane.INFORMATION_MESSAGE);
    }

    public void exportarReceitaParaPDF(Receita receita) {
        String directoryPath = "exported_recipes/";
        String filePath = directoryPath + receita.getTitulo().replaceAll("[\\\\/:*?\"<>|]", "") + ".pdf";

        try {
            // Criação do documento PDF
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Título da Receita
            document.add(new Paragraph(bn.getString("main.receita.renderer.titulo")+ " " + receita.getTitulo(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));

            // Descrição
            document.add(new Paragraph(bn.getString("main.receita.renderer.descricao") + " " + receita.getDescricao()));

            // Ingredientes
            document.add(new Paragraph(bn.getString("main.receita.exibe.ingredientes")));
            for (Ingrediente ingrediente : receita.getIngredientes()) {
                document.add(new Paragraph("- " + ingrediente.getNome() + " : " + ingrediente.getQuantidade()));
            }

            // Modo de Preparo
            document.add(new Paragraph(bn.getString("main.receita.exibe.modopreparo")+ " " + receita.getModoPreparo()));

            // Fechar o documento
            document.close();

            // Exibir mensagem de sucesso
            JOptionPane.showMessageDialog(this, bn.getString("main.receita.exibe.msg")+ " " + filePath, bn.getString("main.receita.exibe.msg.titulo"), JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, bn.getString("main.receita.exibe.msg.erro"), bn.getString("main.receita.exibe.msg.erro.titulo"), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirReceita(Receita receita) {
        int confirm = JOptionPane.showConfirmDialog(this, bn.getString("main.receita.exibe.msg.excluir"), bn.getString("main.receita.exibe.msg.excluir.titulo"), JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ReceitaDao receitaDao = DAOFactory.createReceitaDao();
            receitaDao.delete(receita.getId()); // Remove a receita do banco de dados
            JOptionPane.showMessageDialog(this, bn.getString("main.receita.exibe.msg.excluir.ok"), bn.getString("main.receita.exibe.msg.excluir.ok.titulo"), JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Fecha a tela de detalhes após a exclusão
        }
    }

}


