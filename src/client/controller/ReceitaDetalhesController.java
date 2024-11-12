package client.controller;

import client.facade.ClientFacade;
import client.view.utils.LanguageManager;
import shared.entities.Ingrediente;
import shared.entities.Receita;
import client.view.ReceitaDetalhesView;
import client.view.ReceitaView;
import client.view.utils.Handler_IO;
import client.model.utils.Authenticator;
import client.view.MainView;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import shared.enums.Attributes;
import shared.enums.Command;
import shared.enums.Entity;
import shared.enums.Strategy;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ReceitaDetalhesController {

    private ReceitaDetalhesView detalhesView;
    private Receita receita;
    private ResourceBundle bn;

    public ReceitaDetalhesController(ReceitaDetalhesView detalhesView, Receita receita, ResourceBundle bn) {
        this.detalhesView = detalhesView;
        this.receita = receita;
        this.bn = bn;
        initButtonListeners();
    }

    private void initButtonListeners() {
        detalhesView.addEditarButtonActionListener(e -> {
            String emailAutor = receita.getEmailAutor();
            String emailUsuario = Authenticator.getAuthenticatedUser().getEmail();

            if (emailAutor.equalsIgnoreCase(emailUsuario)) {
                editarReceita();
            } else {
                showNotAuthorMessage();
            }
        });

        detalhesView.addExportarTXTMenuItemActionListener(e -> exportarReceitaTXT());
        detalhesView.addExportarPDFMenuItemButtonActionListener(e -> exportarReceitaParaPDF());
        detalhesView.addExcluirButtonActionListener(e -> {
            String emailAutor = receita.getEmailAutor();
            String emailUsuario = Authenticator.getAuthenticatedUser().getEmail();

            if (emailAutor.equalsIgnoreCase(emailUsuario)) {
                excluirReceita();
            } else {
                showNotAuthorMessage();
            }
        });

        detalhesView.addVoltarButtonActionListener(e -> detalhesView.dispose());
    }

    private void showNotAuthorMessage() {
        JOptionPane.showMessageDialog(detalhesView,
            bn.getString("main.receita.exibe.msg.autor"),
            bn.getString("main.receita.exibe.msg.autor.titulo"),
            JOptionPane.WARNING_MESSAGE);
    }

    private void editarReceita() {
        ReceitaView receitaView = new ReceitaView((MainView) detalhesView.getParent(), receita, bn, detalhesView);
        receitaView.setVisible(true);

        atualizarDadosReceita(receita);
    }

    private void atualizarDadosReceita(Receita receita) {
        detalhesView.setTitulo("<html><b>" + bn.getString("main.receita.label.titulo") + ":</b> " + receita.getTitulo() + "</html>");
        detalhesView.setDescricao("<html><b>" + bn.getString("main.receita.label.descricao") + ":</b> " + receita.getDescricao() + "</html>");
        detalhesView.setModoPreparo("<html><b>" + bn.getString("main.receita.label.modopreparo") + ":</b><br/>" + receita.getModoPreparo().replace("\n", "<br/>") + "</html>");
    }

    public void exportarReceitaTXT() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle(LanguageManager.getInstance().getResourceBundle().getString("diretoriotxt"));
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    int userSelection = fileChooser.showSaveDialog(detalhesView);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File directory = fileChooser.getSelectedFile();
        String filePath = directory.getAbsolutePath() + "\\" + receita.getTitulo().replaceAll("[\\\\/:*?\"<>|]", "") + ".txt";
        
        Handler_IO<String> handler = new Handler_IO<>(filePath);
        StringBuilder conteudoReceita = new StringBuilder();

        conteudoReceita.append(bn.getString("main.receita.renderer.titulo")).append(receita.getTitulo()).append("\n");
        conteudoReceita.append(bn.getString("main.receita.renderer.descricao")).append(receita.getDescricao()).append("\n");

        conteudoReceita.append(bn.getString("main.receita.exibe.ingredientes")).append("\n");
        for (Ingrediente ingrediente : receita.getIngredientes()) {
            conteudoReceita.append("- ").append(ingrediente.getNome())
                           .append(" : ")
                           .append(ingrediente.getQuantidade())
                           .append("\n");
        }

        conteudoReceita.append(bn.getString("main.receita.exibe.modopreparo")).append(" ").append(receita.getModoPreparo()).append("\n");

        handler.writeFile(conteudoReceita.toString(), false);
        JOptionPane.showMessageDialog(detalhesView, bn.getString("main.receita.exibe.msg") + " " + filePath, bn.getString("main.receita.exibe.msg.titulo"), JOptionPane.INFORMATION_MESSAGE);
    }
}

       

    public void exportarReceitaParaPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(LanguageManager.getInstance().getResourceBundle().getString("diretoriopdf"));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int userSelection = fileChooser.showSaveDialog(detalhesView);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File directory = fileChooser.getSelectedFile();
            String filePath = directory.getAbsolutePath() + "\\" + receita.getTitulo().replaceAll("[\\\\/:*?\"<>|]", "") + ".pdf";

            try {
                // Criação do documento PDF
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // Título da Receita
                document.add(new Paragraph(bn.getString("main.receita.renderer.titulo") + " " + receita.getTitulo(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));

                // Descrição
                document.add(new Paragraph(bn.getString("main.receita.renderer.descricao") + " " + receita.getDescricao()));

                // Ingredientes
                document.add(new Paragraph(bn.getString("main.receita.exibe.ingredientes")));
                for (Ingrediente ingrediente : receita.getIngredientes()) {
                    document.add(new Paragraph("- " + ingrediente.getNome() + " : " + ingrediente.getQuantidade()));
                }

                // Modo de Preparo
                document.add(new Paragraph(bn.getString("main.receita.exibe.modopreparo") + " " + receita.getModoPreparo()));

                // Fechar o documento
                document.close();

                // Exibir mensagem de sucesso
                JOptionPane.showMessageDialog(detalhesView, bn.getString("main.receita.exibe.msg") + " " + filePath, bn.getString("main.receita.exibe.msg.titulo"), JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(detalhesView, bn.getString("main.receita.exibe.msg.erro"), bn.getString("main.receita.exibe.msg.erro.titulo"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }



    private void excluirReceita() {
        URL iconUrl = getClass().getResource("/client/resources/images/trash.png");
        ImageIcon customIcon = iconUrl != null ? new ImageIcon(iconUrl) : null;

        int confirm = JOptionPane.showConfirmDialog(detalhesView, bn.getString("main.receita.exibe.msg.excluir"), bn.getString("main.receita.exibe.msg.excluir.titulo"), JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, customIcon);
        if (confirm == JOptionPane.YES_OPTION) {

            Map<String, Object> args = new HashMap<>();
            args.put(Attributes.RECIPE_ID.getDescription(), receita.getId());

            ClientFacade.sendRequest(Entity.RECEITA, Command.DELETE, args, e -> {


                JOptionPane.showMessageDialog(detalhesView, bn.getString("main.receita.exibe.msg.excluir.ok"), bn.getString("main.receita.exibe.msg.excluir.ok.titulo"), JOptionPane.INFORMATION_MESSAGE);

                detalhesView.getMainView().getMainViewController().setFilterStrategy(Strategy.PAGE);
                detalhesView.getMainView().getMainViewController().updateReceitasList(0, new HashMap<>());

                detalhesView.dispose();

            });

        }
    }
}
