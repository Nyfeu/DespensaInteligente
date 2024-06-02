package view;

import model.dao.DAOFactory;
import model.dao.interfaces.UsuarioDao;
import model.entities.Ingrediente;
import model.entities.Usuario;
import model.utils.Authenticator;
import model.utils.DateParser;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class IngredientView extends JDialog {

    private JTextField txtNome;
    private JTextField txtQuantidade;
    private JTextField txtDataValidade;

    public IngredientView(Frame parent) {
        super(parent, "Adicionar Ingrediente", true);
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JLabel lblNome = new JLabel("Nome:");
        JLabel lblQuantidade = new JLabel("Quantidade:");
        JLabel lblDataValidade = new JLabel("Data de Validade (opcional):");

        txtNome = new JTextField(20);
        txtQuantidade = new JTextField(10);
        txtDataValidade = new JTextField(10);

        panel.add(lblNome);
        panel.add(txtNome);
        panel.add(lblQuantidade);
        panel.add(txtQuantidade);
        panel.add(lblDataValidade);
        panel.add(txtDataValidade);

        JButton btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(e -> {

            String nome = txtNome.getText();
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            try {
                Date dataValidade = DateParser.parseString(txtDataValidade.getText());
                Ingrediente ingrediente = new Ingrediente(nome,0,dataValidade,quantidade);
                Usuario user = Authenticator.getAuthenticatedUser();
                ArrayList<Ingrediente> novaDespensa = user.getDespensa();
                novaDespensa.add(ingrediente);
                user.setDespensa(novaDespensa);
                UsuarioDao usuarioDao = DAOFactory.createUsuarioDao();
                usuarioDao.update(user);
                dispose();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }

        });

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnAdicionar);
        btnPanel.add(btnCancelar);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
    }
}