package client.view.utils;

import shared.entities.Ingrediente;
import shared.util.DateParser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class IngredienteCellRenderer extends JPanel implements ListCellRenderer<Ingrediente> {

    private JLabel lblNome;
    private JLabel lblQuantidade;
    private JLabel lblValidade;
    private ResourceBundle bn;

    public IngredienteCellRenderer(ResourceBundle bn) {
        this.bn = bn;
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
        lblNome.setText(bn.getString("main.despensa.ingrediente.nome")+" " + value.getNome());
        lblQuantidade.setText(bn.getString("main.despensa.ingrediente.quantidade")+" " + value.getQuantidade());

        Date dataValidade = value.getValidade();

        if (!vencido(dataValidade)) {
            lblValidade.setText(bn.getString("main.despensa.ingrediente.validade")+" " + DateParser.parseDate(dataValidade));
            lblValidade.setForeground(Color.black);
        } else {
            lblValidade.setText(bn.getString("main.despensa.ingrediente.validade")+" " + DateParser.parseDate(dataValidade) + " " + bn.getString("main.despensa.ingrediente.vencido"));
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
