package client.view.utils;

import shared.entities.Receita;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ResourceBundle;

public class ReceitaCellRenderer extends JPanel implements ListCellRenderer<Receita> {

    private JLabel lblNome;
    private JLabel lblDescription;


    public ReceitaCellRenderer() {
        setLayout(new BorderLayout(5, 5));
        lblNome = new JLabel();
        lblDescription = new JLabel();

        JPanel textPanel = new JPanel(new GridLayout(0, 1));
        textPanel.add(lblNome);
        textPanel.add(lblDescription);
        add(textPanel, BorderLayout.CENTER);
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Receita> list, Receita value, int index, boolean isSelected, boolean cellHasFocus) {

        ResourceBundle bn = LanguageManager.getInstance().getResourceBundle();
        lblNome.setText(bn.getString("main.receita.renderer.titulo") + " " + value.getTitulo());
        lblDescription.setText(bn.getString("main.receita.renderer.descricao") + " " + truncateString(value.getDescricao(),40));

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

    private String truncateString(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength) + "...";
    }

}
