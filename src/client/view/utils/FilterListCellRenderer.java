package client.view.utils;

import javax.swing.*;
import java.awt.*;

public class FilterListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        String string = (String) value;
        setText(string);
        return this;

    }

}
