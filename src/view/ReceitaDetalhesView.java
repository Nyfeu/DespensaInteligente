package view;

import model.entities.Receita;
import view.utils.Handler_IO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ResourceBundle;

public class ReceitaDetalhesView extends JDialog {

    private JLabel lblTitulo, lblDescricao, lblModoPreparo;
    private JButton btnEditar, btnExportar; // Declaração dos botões
    private ResourceBundle bn;

    public ReceitaDetalhesView(JFrame parent, Receita receita, ResourceBundle bn) {
        super(parent, bn.getString("main.receita.detalhes.titulo"), true);
        this.bn = bn;
        initComponents(receita);
        setLocationRelativeTo(parent);
        setSize(400, 300); // Define o tamanho da janela
        setResizable(false);
    }

    private void initComponents(Receita receita) {
        // Painel principal com BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(245, 245, 245)); // Cor de fundo suave
        panelPrincipal.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5)); // Borda cinza ao redor

        // Painel de conteúdo (para os textos) com BoxLayout
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

        // Painel Modo de Preparo
        lblModoPreparo = new JLabel("<html><b>" + bn.getString("main.receita.label.modopreparo") + ":</b><br/>" + receita.getModoPreparo().replace("\n", "<br/>") + "</html>");
        lblModoPreparo.setFont(new Font("Arial", Font.PLAIN, 16));
        lblModoPreparo.setBorder(new EmptyBorder(5, 0, 5, 0));
        panelConteudo.add(lblModoPreparo);

        // Adiciona o painel de conteúdo ao centro do painel principal
        panelPrincipal.add(panelConteudo, BorderLayout.CENTER);

        // Painel de botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnEditar = new JButton(bn.getString("main.receita.botao.editar"));
        btnExportar = new JButton(bn.getString("main.receita.botao.exportar"));
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExportar);

        // Adiciona o painel de botões ao sul do painel principal
        panelPrincipal.add(panelBotoes, BorderLayout.SOUTH);

        // Adiciona o painel principal ao conteúdo do dialog
        getContentPane().add(panelPrincipal);

        // Adiciona ação ao botão Editar para abrir ReceitaView
        btnEditar.addActionListener(e -> abrirReceitaView(receita));
        btnExportar.addActionListener(e -> exportarReceita(receita));
    }

    private void abrirReceitaView(Receita receita) {
        // Lógica para edição da receita
    }

    private void exportarReceita(Receita receita) {
        // Define o diretório onde o arquivo será salvo
        String directoryPath = "exported_recipes/";
        
        // Define o nome completo do arquivo com o diretório
        String filePath = directoryPath + receita.getTitulo().replaceAll("[\\\\/:*?\"<>|]", "") + ".txt";  
        Handler_IO<String> handler = new Handler_IO<>(filePath);
    
        // Prepara o conteúdo da receita
        StringBuilder conteudoReceita = new StringBuilder();
        conteudoReceita.append("Título: ").append(receita.getTitulo()).append("\n");
        conteudoReceita.append("Descrição: ").append(receita.getDescricao()).append("\n");
        conteudoReceita.append("Modo de Preparo: ").append(receita.getModoPreparo()).append("\n");
    
        // Grava o conteúdo no arquivo
        handler.writeFile(conteudoReceita.toString(), false);
    
        // Confirmação para o usuário
        JOptionPane.showMessageDialog(this, "Receita exportada para " + filePath, "Exportação Completa", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
