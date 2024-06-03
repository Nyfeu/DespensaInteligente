package view.utils;

import model.entities.Ingrediente;
import model.utils.DateParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class IngredienteCellRenderer extends JPanel implements ListCellRenderer<Ingrediente> {

    private JLabel lblNome;
    private JLabel lblQuantidade;
    private JLabel lblValidade;

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

        Date dataValidade = value.getValidade();

        if (!vencido(dataValidade)) {
            lblValidade.setText("Validade: " + DateParser.parseDate(dataValidade));
            lblValidade.setForeground(Color.black);
        } else {
            lblValidade.setText("Validade: " + DateParser.parseDate(dataValidade) + " (VENCIDO)");
            lblValidade.setForeground(Color.red);
        }

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

    private boolean vencido(Date dataValidade) {

        LocalDate now = LocalDate.now();
        Date utilDate = new Date(dataValidade.getTime());
        LocalDate data = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return data.isBefore(now);
    }

}
