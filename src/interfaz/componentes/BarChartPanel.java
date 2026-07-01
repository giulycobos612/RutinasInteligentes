package interfaz.componentes;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BarChartPanel extends JPanel {
    public static class Bar {
        String label;
        double value;
        Color color;
        public Bar(String label, double value, Color color) {
            this.label = label; this.value = value; this.color = color;
        }
    }

    private List<Bar> bars;
    private String title;

    public BarChartPanel(String title, List<Bar> bars) {
        this.title = title;
        this.bars = bars;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bars == null || bars.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double max = bars.stream().mapToDouble(b -> b.value).max().orElse(1);
        if (max == 0) max = 1;

        int padding = 40;
        int width = getWidth() - padding * 2;
        int height = getHeight() - padding * 2;
        int barWidth = width / bars.size() - 10;

        g2.setFont(Tema.FONT_SUBTITULO);
        g2.setColor(Tema.TEXTO_PRINCIPAL);
        g2.drawString(title, padding, 20);

        // Draw axes
        g2.setColor(Tema.BORDE);
        g2.drawLine(padding, padding, padding, padding + height);
        g2.drawLine(padding, padding + height, padding + width, padding + height);

        g2.setFont(Tema.FONT_PEQUENA);
        for (int i = 0; i < bars.size(); i++) {
            Bar b = bars.get(i);
            int barHeight = (int) ((b.value / max) * height);
            int x = padding + i * (barWidth + 10) + 5;
            int y = padding + height - barHeight;

            g2.setColor(b.color);
            g2.fillRoundRect(x, y, barWidth, barHeight, 5, 5);

            g2.setColor(Tema.TEXTO_SECUNDARIO);
            g2.drawString(b.label, x, padding + height + 15);
            g2.drawString(String.valueOf((int)b.value), x + barWidth/2 - 5, y - 5);
        }

        g2.dispose();
    }
}
