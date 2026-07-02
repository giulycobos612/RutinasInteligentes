package interfaz.componentes;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernSliderUI extends BasicSliderUI {
    private boolean isHovering = false;
    private final Color trackColor = Tema.withAlpha(Tema.TEXTO_SECUNDARIO, 40);
    private final Color progressColor = Tema.PRIMARIO;

    public ModernSliderUI(JSlider b) {
        super(b);
        b.setOpaque(false);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovering = true;
                b.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovering = false;
                b.repaint();
            }
        });
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int trackHeight = 6;
        int trackY = trackRect.y + (trackRect.height - trackHeight) / 2;
        int trackX = trackRect.x;
        int trackWidth = trackRect.width;

        // Draw background track
        g2d.setColor(trackColor);
        g2d.fill(new RoundRectangle2D.Float(trackX, trackY, trackWidth, trackHeight, trackHeight, trackHeight));

        // Draw progress track
        int progressWidth = thumbRect.x + thumbRect.width / 2 - trackX;
        if (progressWidth > 0) {
            g2d.setColor(progressColor);
            g2d.fill(new RoundRectangle2D.Float(trackX, trackY, progressWidth, trackHeight, trackHeight, trackHeight));
        }

        g2d.dispose();
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int thumbSize = 16;
        int thumbX = thumbRect.x + (thumbRect.width - thumbSize) / 2;
        int thumbY = thumbRect.y + (thumbRect.height - thumbSize) / 2;

        if (isHovering || slider.getValueIsAdjusting()) {
            g2d.setColor(Tema.PRIMARIO);
            g2d.fillOval(thumbX - 2, thumbY - 2, thumbSize + 4, thumbSize + 4);
        } else {
            g2d.setColor(Tema.PRIMARIO);
            g2d.fillOval(thumbX, thumbY, thumbSize, thumbSize);
        }
        
        g2d.setColor(Color.WHITE);
        g2d.fillOval(thumbX + 4, thumbY + 4, thumbSize - 8, thumbSize - 8);

        g2d.dispose();
    }

    @Override
    public void paintFocus(Graphics g) {
        // Remove dotted focus rect
    }
}
