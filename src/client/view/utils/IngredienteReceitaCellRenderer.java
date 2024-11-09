package client.view.utils;

import shared.entities.Ingrediente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class IngredienteReceitaCellRenderer extends JPanel implements ListCellRenderer<Ingrediente> {

    private JLabel lblNome;
    private JLabel lblQuantidade;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private ResourceBundle bn;

    public IngredienteReceitaCellRenderer(ResourceBundle bn) {
        this.bn = LanguageManager.getInstance().getResourceBundle();
        setLayout(new BorderLayout(5, 5));
        lblNome = new JLabel();
        lblQuantidade = new JLabel();

        JPanel textPanel = new JPanel(new GridLayout(0, 1));
        textPanel.add(lblNome);
        textPanel.add(lblQuantidade);
        add(textPanel, BorderLayout.CENTER);
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Ingrediente> list, Ingrediente value, int index, boolean isSelected, boolean cellHasFocus) {
        lblNome.setText(bn.getString("main.receita.botao.publicar.ingredientereceitarenderer.nome") + " " + value.getNome());
        lblQuantidade.setText(bn.getString("main.receita.botao.publicar.ingredientereceitarenderer.quantidade")+ " " + value.getQuantidade());

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setMaximumSize(new Dimension(list.getWidth(), getPreferredSize().height));

        return this;
    }

}
