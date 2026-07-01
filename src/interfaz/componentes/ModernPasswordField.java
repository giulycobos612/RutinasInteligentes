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
            g2.drawString(placeholder, getInsets().left, y);
        }
        g2.dispose();
    }
}
