package interfaz.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ModernButton extends JButton {

    private Color normalColor;
    private Color hoverColor;

    public ModernButton(String text) {
        super(text);
        this.normalColor = Tema.PRIMARIO;
        this.hoverColor = Tema.PRIMARIO_HOVER;
        
        setFont(Tema.FONT_REGULAR);
        setForeground(Color.WHITE);
        setBackground(normalColor);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
            }
        });
    }

    public void setColors(Color normal, Color hover) {
        this.normalColor = normal;
        this.hoverColor = hover;
        setBackground(normal);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
        g2.dispose();
        super.paintComponent(g);
    }
}
