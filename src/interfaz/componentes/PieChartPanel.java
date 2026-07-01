package interfaz.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.List;

public class PieChartPanel extends JPanel {
    public static class Slice {
        double value;
        Color color;
        String label;
        public Slice(double value, Color color, String label) {
            this.value = value; this.color = color; this.label = label;
        }
    }

    private List<Slice> slices;

    public PieChartPanel(List<Slice> slices) {
        this.slices = slices;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (slices == null || slices.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double total = slices.stream().mapToDouble(s -> s.value).sum();
        
        int legendWidth = 110;
        int padding = 20;
        int size = Math.min(getWidth() - legendWidth, getHeight()) - padding * 2;
        int x = Math.max(10, (getWidth() - legendWidth - size) / 2);
        int y = (getHeight() - size) / 2;

        if (total == 0) {
            g2.setColor(Tema.BORDE);
            g2.fill(new Arc2D.Double(x, y, size, size, 0, 360, Arc2D.PIE));
        } else {
            double curValue = 0.0;
            for (Slice slice : slices) {
                if (slice.value == 0) continue;
                int startAngle = (int) (curValue * 360 / total);
                int arcAngle = (int) Math.round(slice.value * 360 / total);
                
                g2.setColor(slice.color);
                g2.fill(new Arc2D.Double(x, y, size, size, startAngle, arcAngle, Arc2D.PIE));
                curValue += slice.value;
            }
        }

        // Draw legend always on the right side of the pie
        int legendX = x + size + 15;
        int legendY = y + 20;
        g2.setFont(Tema.FONT_PEQUENA);
        for (Slice slice : slices) {
            g2.setColor(slice.color);
            g2.fillRect(legendX, legendY - 10, 10, 10);
            g2.setColor(Tema.TEXTO_PRINCIPAL);
            String percent = total == 0 ? "0.0%" : String.format(java.util.Locale.US, "%.1f%%", (slice.value / total) * 100);
            g2.drawString(slice.label + " (" + percent + ")", legendX + 15, legendY);
            legendY += 20;
        }
        g2.dispose();
    }
}
