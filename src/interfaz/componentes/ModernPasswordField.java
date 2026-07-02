package interfaz.componentes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class ModernPasswordField extends JPasswordField {

    private String placeholder;
    private boolean isFocused;

    public ModernPasswordField(String placeholder) {
        this.placeholder = placeholder;
        setFont(Tema.FONT_REGULAR);
        setForeground(Tema.TEXTO_PRINCIPAL);
        setBackground(Tema.TARJETA);
        setOpaque(false);
        setBorder(new EmptyBorder(10, 15, 10, 15));
        setEchoChar('•'); // Enmascaramiento

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });

        // Toggle visibility icon
        setLayout(null);
        JLabel eyeIcon = new JLabel("Ver"); 
        eyeIcon.setFont(Tema.FONT_PEQUENA.deriveFont(Font.BOLD));
        eyeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeIcon.setForeground(Tema.TEXTO_SECUNDARIO);
        eyeIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        eyeIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (getEchoChar() == (char) 0) {
                    setEchoChar('•'); // Hide
                    eyeIcon.setText("Ver");
                    eyeIcon.setForeground(Tema.TEXTO_SECUNDARIO);
                } else {
                    setEchoChar((char) 0); // Show
                    eyeIcon.setText("Ocultar");
                    eyeIcon.setForeground(Tema.PRIMARIO);
                }
            }
        });
        add(eyeIcon);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int iconWidth = 50;
                int iconHeight = 24;
                eyeIcon.setBounds(getWidth() - iconWidth - 10, (getHeight() - iconHeight) / 2, iconWidth, iconHeight);
            }
        });
    }

    @Override
    public Insets getInsets() {
        Insets i = super.getInsets();
        return new Insets(i.top, i.left, i.bottom, i.right + 45);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

        if (isFocused) {
            g2.setColor(Tema.PRIMARIO);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 10, 10);
        } else {
            g2.setColor(Tema.BORDE);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        }

        super.paintComponent(g);

        if (getPassword().length == 0 && !isFocused) {
            g2.setColor(Tema.TEXTO_SECUNDARIO);
            g2.setFont(Tema.FONT_REGULAR);
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            
            // Evitar que el placeholder se superponga con el boton
            Shape oldClip = g2.getClip();
            g2.clipRect(getInsets().left, 0, getWidth() - getInsets().left - 60, getHeight());
            g2.drawString(placeholder, getInsets().left, y);
            g2.setClip(oldClip);
        }
        g2.dispose();
    }
}
