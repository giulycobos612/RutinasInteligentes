package interfaz.componentes;

import modelo.RutinasEstudio;
import negocio.GestorRutinas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ModernScheduleDialog extends JDialog {
    private LocalDate currentDate;
    private GestorRutinas gestor;
    private JLabel lblDate;
    private ScheduleCanvas canvas;

    public ModernScheduleDialog(Frame owner, GestorRutinas gestor) {
        super(owner, "Horario del Dia", true);
        this.gestor = gestor;
        this.currentDate = LocalDate.now();
        
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.FONDO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                g2.setColor(Tema.PRIMARIO);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 20, 20));
                g2.dispose();
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // --- Header Navigation ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        ModernButton btnPrev = new ModernButton("<");
        btnPrev.setColors(Tema.TARJETA, Tema.TARJETA.darker());
        btnPrev.setForeground(Tema.TEXTO_PRINCIPAL);
        btnPrev.setFont(Tema.FONT_BOLD);
        btnPrev.setPreferredSize(new Dimension(40, 40));
        btnPrev.addActionListener(e -> changeDate(-1));
        
        ModernButton btnNext = new ModernButton(">");
        btnNext.setColors(Tema.TARJETA, Tema.TARJETA.darker());
        btnNext.setForeground(Tema.TEXTO_PRINCIPAL);
        btnNext.setFont(Tema.FONT_BOLD);
        btnNext.setPreferredSize(new Dimension(40, 40));
        btnNext.addActionListener(e -> changeDate(1));
        
        lblDate = new JLabel("", SwingConstants.CENTER);
        lblDate.setFont(Tema.FONT_SUBTITULO);
        lblDate.setForeground(Tema.TEXTO_PRINCIPAL);
        updateDateLabel();
        
        ModernButton btnClose = new ModernButton("X");
        btnClose.setColors(Tema.PELIGRO, Tema.PELIGRO.darker());
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(Tema.FONT_BOLD);
        btnClose.setPreferredSize(new Dimension(40, 40));
        btnClose.addActionListener(e -> dispose());
        
        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftNav.setOpaque(false);
        leftNav.add(btnPrev);
        leftNav.add(btnNext);
        
        headerPanel.add(leftNav, BorderLayout.WEST);
        headerPanel.add(lblDate, BorderLayout.CENTER);
        headerPanel.add(btnClose, BorderLayout.EAST);
        
        // --- Canvas ---
        canvas = new ScheduleCanvas();
        JScrollPane scroll = new JScrollPane(canvas);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        setSize(450, 600);
        setLocationRelativeTo(owner);
        
        // Scroll to approx current time if viewing today
        SwingUtilities.invokeLater(() -> {
            int currentHour = LocalTime.now().getHour();
            int yPos = Math.max(0, (currentHour - 2) * 60); // 60px per hour
            scroll.getVerticalScrollBar().setValue(yPos);
        });
    }
    
    private void changeDate(int days) {
        currentDate = currentDate.plusDays(days);
        updateDateLabel();
        canvas.repaint();
    }
    
    private void updateDateLabel() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM", new java.util.Locale("es", "ES"));
        String f = currentDate.format(dtf);
        f = f.substring(0, 1).toUpperCase() + f.substring(1);
        lblDate.setText(f);
    }
    
    public static void showDialog(Frame owner, GestorRutinas gestor) {
        ModernScheduleDialog dialog = new ModernScheduleDialog(owner, gestor);
        dialog.setVisible(true);
    }
    
    // --- Custom Canvas Class ---
    private class ScheduleCanvas extends JPanel {
        private final int ROW_HEIGHT = 60; // 60px per hour
        private final int TIME_WIDTH = 50; // Width for the time labels
        
        public ScheduleCanvas() {
            setOpaque(false);
            setPreferredSize(new Dimension(400, 24 * ROW_HEIGHT + 30));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            int width = getWidth();
            
            // Draw grid and time labels
            g2.setFont(Tema.FONT_PEQUENA);
            for (int i = 0; i <= 24; i++) {
                int y = i * ROW_HEIGHT;
                
                // Draw time text
                g2.setColor(Tema.TEXTO_SECUNDARIO);
                String timeStr = String.format("%02d:00", i == 24 ? 0 : i);
                g2.drawString(timeStr, 5, y + 15);
                
                // Draw line
                g2.setColor(Tema.BORDE);
                g2.drawLine(TIME_WIDTH, y, width, y);
            }
            
            // Fetch sessions for current date
            List<RutinasEstudio> todaysSessions = gestor.getRutinaSemanal().stream()
                .filter(r -> r.getDia().equals(currentDate))
                .collect(Collectors.toList());
                
            // Draw blocks
            for (RutinasEstudio r : todaysSessions) {
                int startHour = r.getHoraInicio().getHour();
                int startMinute = r.getHoraInicio().getMinute();
                int duracion = r.getDuracionMinutos();
                
                int yStart = (startHour * ROW_HEIGHT) + (int)((startMinute / 60.0) * ROW_HEIGHT);
                int height = (int)((duracion / 60.0) * ROW_HEIGHT);
                if (height < 20) height = 20; // Min height for visibility
                
                int xStart = TIME_WIDTH + 10;
                int rectWidth = width - TIME_WIDTH - 20;
                
                // Draw block
                g2.setColor(Tema.PRIMARIO);
                g2.fill(new RoundRectangle2D.Float(xStart, yStart, rectWidth, height, 12, 12));
                
                // Draw text inside block
                g2.setColor(Color.WHITE);
                g2.setFont(Tema.FONT_PEQUENA.deriveFont(Font.BOLD));
                
                String title = r.getNombreSesion().isEmpty() 
                    ? (r.getTarea() != null ? r.getTarea().getNombreTarea() : "Sesion de Estudio") 
                    : r.getNombreSesion();
                    
                g2.drawString(title, xStart + 10, yStart + 15);
                
                // Draw duration text
                g2.setFont(Tema.FONT_PEQUENA);
                String durStr = r.getHoraInicioFormateada() + " - " + r.getHoraFinFormateada();
                if (height >= 35) {
                    g2.drawString(durStr, xStart + 10, yStart + 30);
                } else {
                    g2.drawString(" (" + durStr + ")", xStart + 10 + g2.getFontMetrics().stringWidth(title) + 5, yStart + 15);
                }
            }
        }
    }
}
