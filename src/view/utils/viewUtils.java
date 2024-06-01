package view.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class viewUtils {

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(Color.GRAY);
        label.setForeground(Color.WHITE);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setBorder(new EmptyBorder(10, 20, 10, 20));
        return label;
    }

    public static void configureButton(JButton button) {
        button.setBackground(Color.white);
        button.setFocusable(false);
        button.setBorderPainted(false);
    }

    public static void closeView(JFrame view) {
        view.setVisible(false);
        view.dispose();
    }

}
