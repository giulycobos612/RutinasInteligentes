package interfaz.componentes;

import javax.swing.*;
import java.awt.*;

public class ModernCard extends JPanel {
    private int radius;
    private boolean shadow;
    private Color accentColor;
    private boolean accentTop;

    public ModernCard(int radius, boolean shadow) {
        this(radius, shadow, null);
    }

    public ModernCard(int radius, boolean shadow, Color accentColor) {
        this.radius = radius;
        this.shadow = shadow;
        this.accentColor = accentColor;
        this.accentTop = true;
        setOpaque(false);
        setBackground(Tema.TARJETA);
    }

    public void setAccentColor(Color c) {
        this.accentColor = c;
        repaint();
    }

    public void setAccentTop(boolean top) {
        this.accentTop = top;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int off = shadow ? 3 : 0;

        // Sombra
        if (shadow) {
            for (int i = 0; i < 6; i++) {
                g2.setColor(new Color(0, 0, 0, 3));
                g2.fillRoundRect(i, i + 2, w - i * 2, h - i * 2, radius + i, radius + i);
            }
        }

        // Fondo
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, w - off, h - (shadow ? 4 : 0), radius, radius);

        // Acento de color (barra superior o lateral)
        if (accentColor != null) {
            g2.setColor(accentColor);
            if (accentTop) {
                g2.fillRoundRect(0, 0, w - off, 5, radius, radius);
                g2.fillRect(0, 3, w - off, 3);
            } else {
                g2.fillRoundRect(0, 0, 5, h - (shadow ? 4 : 0), radius, radius);
                g2.fillRect(3, 0, 3, h - (shadow ? 4 : 0));
            }
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
