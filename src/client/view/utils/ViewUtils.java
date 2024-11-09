package client.view.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ViewUtils {

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

        button.addMouseListener(new MouseAdapter() {
            private final Color originalBackground = button.getBackground();
            private final Color hoverBackground = originalBackground.darker();

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBackground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBackground);
            }
        });
    }

    public static void closeView(JFrame view) {
        view.setVisible(false);
        view.dispose();
    }

    // testando uns efeitos
    public static void customButton1(JButton button) {
        button.setBackground(Color.darkGray);
        button.setForeground(Color.white);
        button.setFocusable(false);
        button.setBorderPainted(false);

        button.addMouseListener(new MouseAdapter() {
            private final Color originalBackground = button.getBackground();
            private final Color hoverBackground = originalBackground.darker();

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBackground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBackground);
            }
        });        
    }

    public static void customButton2(JButton button) {
        button.setContentAreaFilled(false); 
        button.setOpaque(true); 
        button.setBackground(new Color(100, 149, 237)); 
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new RoundedBorder(10)); 

        button.addMouseListener(new MouseAdapter() {
            private final Color originalBackground = button.getBackground();
            private final Color hoverBackground = originalBackground.darker();

            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBackground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBackground);
            }
        });
    }

    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.GRAY); 
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = radius / 2;
            insets.top = insets.bottom = radius / 2;
            return insets;
        }
    }
}
