package view.utils;

import model.entities.Ingrediente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

public class IngredienteCellRenderer extends JPanel implements ListCellRenderer<Ingrediente> {

    private JLabel lblNome;
    private JLabel lblQuantidade;
    private JLabel lblValidade;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public IngredienteCellRenderer() {
        setLayout(new BorderLayout(5, 5));
        lblNome = new JLabel();
        lblQuantidade = new JLabel();
        lblValidade = new JLabel();

        JPanel textPanel = new JPanel(new GridLayout(0, 1));
        textPanel.add(lblNome);
        textPanel.add(lblQuantidade);
        textPanel.add(lblValidade);
        add(textPanel, BorderLayout.CENTER);
        setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Ingrediente> list, Ingrediente value, int index, boolean isSelected, boolean cellHasFocus) {
        lblNome.setText("Nome: " + value.getNome());
        lblQuantidade.setText("Quantidade: " + value.getQuantidade());
        lblValidade.setText("Validade: " + dateFormat.format(value.getValidade()));

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
