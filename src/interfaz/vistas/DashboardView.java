package interfaz.vistas;

import interfaz.componentes.*;
import modelo.ProgresoAcademico;
import modelo.Tarea;
import modelo.RutinasEstudio;
import modelo.Usuario;
import negocio.GestorProgreso;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class DashboardView extends JPanel {
    private GestorProgreso gestorProgreso;
    private YearMonth currentMonth = YearMonth.now();

    public DashboardView(GestorProgreso gestorProgreso) {
        this.gestorProgreso = gestorProgreso;
        setBackground(Tema.FONDO);
        setLayout(new BorderLayout(0, 0));
        actualizarView();
    }

    public void actualizarView() {
        removeAll();
        ProgresoAcademico p = gestorProgreso.actualizarYObtenerProgreso();
        Usuario u = gestorProgreso.getUsuarioActivo();

        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setOpaque(false);
        scrollContent.setBorder(new EmptyBorder(25, 30, 25, 30));

        // ========== HEADER CON SALUDO ==========
        JPanel headerP = new JPanel(new BorderLayout());
        headerP.setOpaque(false);
        headerP.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        String saludo = obtenerSaludo();
        JLabel lblSaludo = new JLabel(saludo + ", " + u.getNombreCompleto().split(" ")[0] + "!");
        lblSaludo.setFont(Tema.FONT_TITULO);
        lblSaludo.setForeground(Tema.TEXTO_PRINCIPAL);

        JLabel lblMotivacion = new JLabel(p.getMensajeMotivacional());
        lblMotivacion.setFont(Tema.FONT_REGULAR);
        lblMotivacion.setForeground(Tema.TEXTO_SECUNDARIO);

        titlePanel.add(lblSaludo);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(lblMotivacion);
        headerP.add(titlePanel, BorderLayout.WEST);

        // Badge racha
        if (p.getDiasRacha() > 0) {
            JPanel rachaP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            rachaP.setOpaque(false);
            ModernCard rachaCard = new ModernCard(12, true, Tema.ADVERTENCIA);
            rachaCard.setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));
            rachaCard.setPreferredSize(new Dimension(130, 55));
            JLabel rachaIcon = new JLabel("Racha");
            rachaIcon.setFont(Tema.FONT_PEQUENA);
            rachaIcon.setForeground(Tema.ADVERTENCIA);
            JLabel rachaNum = new JLabel(p.getDiasRacha() + " dias");
            rachaNum.setFont(Tema.FONT_BOLD);
            rachaNum.setForeground(Tema.ADVERTENCIA);
            rachaCard.add(rachaIcon);
            rachaCard.add(rachaNum);
            rachaP.add(rachaCard);
            headerP.add(rachaP, BorderLayout.EAST);
        }

        scrollContent.add(headerP);
        scrollContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // ========== KPIs ==========
        JPanel kpiPanel = new JPanel(new GridLayout(1, 5, 12, 0));
        kpiPanel.setOpaque(false);
        kpiPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        kpiPanel.setPreferredSize(new Dimension(0, 110));

        kpiPanel.add(crearKpiCard("Tareas", String.valueOf(p.getTareasTotales()), Tema.PRIMARIO));
        kpiPanel.add(crearKpiCard("Completadas", String.valueOf(p.getTareasCompletadas()), Tema.EXITO));
        kpiPanel.add(crearKpiCard("Urgentes", String.valueOf(p.getTareasUrgentes()), Tema.PELIGRO));
        kpiPanel.add(crearKpiCard("Promedio", String.format("%.1f", p.getPromedioCalificaciones()), Tema.SECUNDARIO));
        kpiPanel.add(crearKpiCard("Horas Estudio", p.getHorasEstudiadas() + "h " + (p.getMinutosEstudiados() % 60) + "m", Tema.ACENTO));

        scrollContent.add(kpiPanel);
        scrollContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // ========== BARRAS DE PROGRESO ==========
        ModernCard progressCard = new ModernCard(12, true, Tema.EXITO);
        progressCard.setLayout(new BoxLayout(progressCard, BoxLayout.Y_AXIS));
        progressCard.setBorder(new EmptyBorder(18, 22, 18, 22));
        progressCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 125));
        progressCard.setPreferredSize(new Dimension(0, 125));

        JLabel lblProgreso = new JLabel("Progreso General");
        lblProgreso.setFont(Tema.FONT_BOLD);
        lblProgreso.setForeground(Tema.TEXTO_PRINCIPAL);
        progressCard.add(lblProgreso);
        progressCard.add(Box.createRigidArea(new Dimension(0, 10)));
        progressCard.add(crearBarraProgreso("Tareas completadas", p.getPorcentajeTareasCompletadas(), Tema.EXITO));
        progressCard.add(Box.createRigidArea(new Dimension(0, 8)));
        progressCard.add(crearBarraProgreso("Sesiones cumplidas", p.getPorcentajeSesionesCumplidas(), Tema.SECUNDARIO));

        scrollContent.add(progressCard);
        scrollContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // ========== FILA: CALENDARIO + PROXIMOS VENCIMIENTOS ==========
        JPanel midRow = new JPanel(new GridLayout(1, 2, 15, 0));
        midRow.setOpaque(false);
        midRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));
        midRow.setPreferredSize(new Dimension(0, 340));

        // Calendario academico
        midRow.add(crearCalendarioAcademico(u));

        // Proximos vencimientos
        midRow.add(crearProximosVencimientos(u));

        scrollContent.add(midRow);
        scrollContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // ========== GRAFICOS ==========
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        chartsPanel.setOpaque(false);
        chartsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        chartsPanel.setPreferredSize(new Dimension(0, 260));

        // Pie Chart
        ModernCard pieCard = new ModernCard(15, true, Tema.PRIMARIO);
        pieCard.setLayout(new BorderLayout(0, 5));
        pieCard.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel pieLabel = new JLabel("Estado de Tareas");
        pieLabel.setFont(Tema.FONT_SUBTITULO);
        pieLabel.setForeground(Tema.TEXTO_PRINCIPAL);
        pieCard.add(pieLabel, BorderLayout.NORTH);

        PieChartPanel pie = new PieChartPanel(Arrays.asList(
                new PieChartPanel.Slice(p.getTareasCompletadas(), Tema.EXITO, "Completadas"),
                new PieChartPanel.Slice(p.getTareasPendientes(), Tema.PRIMARIO, "Pendientes"),
                new PieChartPanel.Slice(p.getTareasVencidas(), Tema.PELIGRO, "Vencidas")
        ));
        pieCard.add(pie, BorderLayout.CENTER);

        // Bar Chart
        ModernCard barCard = new ModernCard(15, true, Tema.SECUNDARIO);
        barCard.setLayout(new BorderLayout(0, 5));
        barCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        BarChartPanel bar = new BarChartPanel("Rendimiento", Arrays.asList(
                new BarChartPanel.Bar("Tareas", p.getTareasTotales(), Tema.PRIMARIO),
                new BarChartPanel.Bar("Hechas", p.getTareasCompletadas(), Tema.EXITO),
                new BarChartPanel.Bar("Sesiones", p.getSesionesTotales(), Tema.SECUNDARIO),
                new BarChartPanel.Bar("Cumplidas", p.getSesionesCompletadas(), Tema.ACENTO)
        ));
        barCard.add(bar, BorderLayout.CENTER);

        chartsPanel.add(pieCard);
        chartsPanel.add(barCard);

        scrollContent.add(chartsPanel);

        // Scroll
        JScrollPane scroll = new JScrollPane(scrollContent);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(scroll, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // ========== CALENDARIO ACADEMICO ==========
    private ModernCard crearCalendarioAcademico(Usuario u) {
        ModernCard card = new ModernCard(15, true, Tema.ACENTO);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Calendario Academico");
        titulo.setFont(Tema.FONT_SUBTITULO);
        titulo.setForeground(Tema.TEXTO_PRINCIPAL);
        card.add(titulo, BorderLayout.NORTH);

        // Construir mapa de fechas con eventos
        Map<LocalDate, List<String>> eventosMap = new HashMap<>();
        Map<LocalDate, Color> colorMap = new HashMap<>();

        for (Tarea t : u.getTareas()) {
            LocalDate f = t.getFechaEntrega();
            eventosMap.computeIfAbsent(f, k -> new ArrayList<>()).add("Tarea: " + t.getNombreTarea());
            if (!t.isCompletada()) {
                colorMap.put(f, Tema.getPrioridadColor(t.getPrioridad()));
            } else {
                colorMap.putIfAbsent(f, Tema.EXITO);
            }
        }

        for (RutinasEstudio r : u.getRutinas()) {
            LocalDate f = r.getDia();
            eventosMap.computeIfAbsent(f, k -> new ArrayList<>()).add("Sesion: " + r.toString());
            colorMap.putIfAbsent(f, Tema.INFO);
        }

        // Panel del calendario
        JPanel calPanel = new JPanel(new BorderLayout(0, 5));
        calPanel.setOpaque(false);

        // No redifinir currentMonth local, usar el estado
        // YearMonth currentMonth = YearMonth.now();
        // Header mes con navegacion
        JPanel headerMes = new JPanel(new BorderLayout());
        headerMes.setOpaque(false);
        
        ModernButton btnPrev = new ModernButton("<");
        btnPrev.setColors(Tema.TARJETA, Tema.FONDO);
        btnPrev.setForeground(Tema.PRIMARIO);
        btnPrev.setPreferredSize(new Dimension(40, 25));
        btnPrev.addActionListener(e -> { currentMonth = currentMonth.minusMonths(1); actualizarView(); });
        
        ModernButton btnNext = new ModernButton(">");
        btnNext.setColors(Tema.TARJETA, Tema.FONDO);
        btnNext.setForeground(Tema.PRIMARIO);
        btnNext.setPreferredSize(new Dimension(40, 25));
        btnNext.addActionListener(e -> { currentMonth = currentMonth.plusMonths(1); actualizarView(); });

        JLabel lblMes = new JLabel(
                currentMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("es")).substring(0, 1).toUpperCase() +
                currentMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("es")).substring(1) + " " + currentMonth.getYear(),
                SwingConstants.CENTER);
        lblMes.setFont(Tema.FONT_BOLD);
        lblMes.setForeground(Tema.ACENTO);
        
        headerMes.add(btnPrev, BorderLayout.WEST);
        headerMes.add(lblMes, BorderLayout.CENTER);
        headerMes.add(btnNext, BorderLayout.EAST);
        calPanel.add(headerMes, BorderLayout.NORTH);

        // Grid
        JPanel grid = new JPanel(new GridLayout(0, 7, 2, 2));
        grid.setOpaque(false);

        String[] dias = {"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};
        for (String d : dias) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            lbl.setForeground(Tema.TEXTO_SECUNDARIO);
            grid.add(lbl);
        }

        LocalDate first = currentMonth.atDay(1);
        int dayOfWeek = first.getDayOfWeek().getValue();
        for (int i = 1; i < dayOfWeek; i++) {
            grid.add(new JLabel(""));
        }

        LocalDate today = LocalDate.now();
        int daysInMonth = currentMonth.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            final LocalDate date = currentMonth.atDay(day);
            final boolean hasEvent = eventosMap.containsKey(date);
            final Color eventColor = colorMap.getOrDefault(date, Tema.PRIMARIO);
            final List<String> eventos = eventosMap.getOrDefault(date, Collections.emptyList());

            JLabel dayLabel = new JLabel(String.valueOf(day), SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (date.equals(today)) {
                        g2.setColor(Tema.PRIMARIO);
                        g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 6, 6);
                    }
                    g2.dispose();
                    super.paintComponent(g);

                    // Punto indicador de evento
                    if (hasEvent) {
                        Graphics2D g3 = (Graphics2D) g.create();
                        g3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g3.setColor(eventColor);
                        g3.fillOval(getWidth() / 2 - 3, getHeight() - 8, 6, 6);
                        g3.dispose();
                    }
                }
            };

            dayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            dayLabel.setOpaque(false);
            dayLabel.setPreferredSize(new Dimension(28, 28));

            if (date.equals(today)) {
                dayLabel.setForeground(Color.WHITE);
            } else if (date.isBefore(today)) {
                dayLabel.setForeground(Tema.withAlpha(Tema.TEXTO_SECUNDARIO, 120));
            } else {
                dayLabel.setForeground(Tema.TEXTO_PRINCIPAL);
            }

            // Tooltip bonito y colorido con HTML
            if (hasEvent) {
                String hexBg = String.format("#%02x%02x%02x", Tema.TARJETA.getRed(), Tema.TARJETA.getGreen(), Tema.TARJETA.getBlue());
                String hexText = String.format("#%02x%02x%02x", Tema.TEXTO_PRINCIPAL.getRed(), Tema.TEXTO_PRINCIPAL.getGreen(), Tema.TEXTO_PRINCIPAL.getBlue());
                String hexAccent = String.format("#%02x%02x%02x", Tema.ACENTO.getRed(), Tema.ACENTO.getGreen(), Tema.ACENTO.getBlue());
                
                StringBuilder sb = new StringBuilder();
                sb.append("<html><div style='background-color:").append(hexBg).append("; color:").append(hexText).append("; padding: 10px; margin: -2px; border-radius: 5px;'>");
                sb.append("<b style='color:").append(hexAccent).append("; font-size:11px; margin-bottom: 5px; display:block;'>").append(date).append("</b>");
                for (String ev : eventos) {
                    sb.append("<div style='font-size:10px; margin-top:2px;'>● ").append(ev).append("</div>");
                }
                sb.append("</div></html>");
                dayLabel.setToolTipText(sb.toString());
                dayLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                StringBuilder msgSb = new StringBuilder("Actividades para el " + date + ":\n\n");
                for (String ev : eventos) {
                    msgSb.append("● ").append(ev).append("\n");
                }
                
                dayLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(DashboardView.this);
                        ModernInfoDialog.showInfoDialog(parentFrame, date.toString(), eventos);
                    }
                });
            }

            grid.add(dayLabel);
        }

        calPanel.add(grid, BorderLayout.CENTER);

        // Leyenda
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leyenda.setOpaque(false);
        leyenda.add(crearLeyendaItem(Tema.PELIGRO, "Urgente"));
        leyenda.add(crearLeyendaItem(Tema.ADVERTENCIA, "Alta"));
        leyenda.add(crearLeyendaItem(Tema.INFO, "Sesion"));
        leyenda.add(crearLeyendaItem(Tema.EXITO, "Hecho"));
        calPanel.add(leyenda, BorderLayout.SOUTH);

        card.add(calPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel crearLeyendaItem(Color color, String texto) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        p.setOpaque(false);
        JLabel dot = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillOval(1, 2, 8, 8);
                g2.dispose();
            }
        };
        dot.setPreferredSize(new Dimension(12, 12));
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lbl.setForeground(Tema.TEXTO_SECUNDARIO);
        p.add(dot);
        p.add(lbl);
        return p;
    }

    // ========== PROXIMOS VENCIMIENTOS ==========
    private ModernCard crearProximosVencimientos(Usuario u) {
        ModernCard card = new ModernCard(15, true, Tema.PELIGRO);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Proximos Vencimientos");
        titulo.setFont(Tema.FONT_SUBTITULO);
        titulo.setForeground(Tema.TEXTO_PRINCIPAL);
        card.add(titulo, BorderLayout.NORTH);

        JPanel listaP = new JPanel();
        listaP.setLayout(new BoxLayout(listaP, BoxLayout.Y_AXIS));
        listaP.setOpaque(false);

        LocalDate hoy = LocalDate.now();
        List<Tarea> proximasTareas = new ArrayList<>();
        for (Tarea t : u.getTareas()) {
            if (!t.isCompletada()) {
                proximasTareas.add(t);
            }
        }
        proximasTareas.sort(Comparator.comparing(Tarea::getFechaEntrega));

        if (proximasTareas.isEmpty()) {
            JLabel vacio = new JLabel("No tienes tareas pendientes!");
            vacio.setFont(Tema.FONT_REGULAR);
            vacio.setForeground(Tema.EXITO);
            vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            listaP.add(Box.createVerticalGlue());
            listaP.add(vacio);
            listaP.add(Box.createVerticalGlue());
        } else {
            int count = 0;
            for (Tarea t : proximasTareas) {
                if (count >= 6) break;
                listaP.add(crearItemVencimiento(t, hoy));
                listaP.add(Box.createRigidArea(new Dimension(0, 6)));
                count++;
            }
        }

        JScrollPane scroll = new JScrollPane(listaP);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    private JPanel crearItemVencimiento(Tarea t, LocalDate hoy) {
        JPanel item = new JPanel(new BorderLayout(8, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.withAlpha(Tema.getPrioridadColor(t.getPrioridad()), 15));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
            }
        };
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(8, 10, 8, 10));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        // Indicador de color lateral
        JLabel colorDot = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.getPrioridadColor(t.getPrioridad()));
                g2.fillRoundRect(0, 0, 4, getHeight(), 2, 2);
                g2.dispose();
            }
        };
        colorDot.setPreferredSize(new Dimension(4, 0));

        JPanel textP = new JPanel();
        textP.setLayout(new BoxLayout(textP, BoxLayout.Y_AXIS));
        textP.setOpaque(false);

        JLabel nombre = new JLabel(t.getNombreTarea());
        nombre.setFont(Tema.FONT_REGULAR);
        nombre.setForeground(Tema.TEXTO_PRINCIPAL);

        long dias = ChronoUnit.DAYS.between(hoy, t.getFechaEntrega());
        String tiempoTexto;
        Color tiempoColor;
        if (dias < 0) {
            tiempoTexto = "Vencida hace " + Math.abs(dias) + " dia(s)";
            tiempoColor = Tema.PELIGRO;
        } else if (dias == 0) {
            tiempoTexto = "Vence HOY";
            tiempoColor = Tema.PELIGRO;
        } else if (dias == 1) {
            tiempoTexto = "Vence MANANA";
            tiempoColor = Tema.ADVERTENCIA;
        } else {
            tiempoTexto = "En " + dias + " dias (" + t.getFechaFormateada() + ")";
            tiempoColor = dias <= 3 ? Tema.ADVERTENCIA : Tema.TEXTO_SECUNDARIO;
        }

        JLabel fecha = new JLabel(tiempoTexto + " | " + t.getMateria().getNombreMateria());
        fecha.setFont(Tema.FONT_PEQUENA);
        fecha.setForeground(tiempoColor);

        textP.add(nombre);
        textP.add(fecha);

        item.add(colorDot, BorderLayout.WEST);
        item.add(textP, BorderLayout.CENTER);
        return item;
    }

    // ========== HELPERS ==========
    private String obtenerSaludo() {
        int hora = java.time.LocalTime.now().getHour();
        if (hora < 12) return "Buenos dias";
        if (hora < 18) return "Buenas tardes";
        return "Buenas noches";
    }

    private ModernCard crearKpiCard(String titulo, String valor, Color accent) {
        ModernCard card = new ModernCard(12, true, accent);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(Tema.FONT_PEQUENA);
        lblTit.setForeground(Tema.TEXTO_SECUNDARIO);
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblVal = new JLabel(valor);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblVal.setForeground(accent);
        lblVal.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblTit);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(lblVal);
        card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel crearBarraProgreso(String label, double porcentaje, Color color) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JLabel lbl = new JLabel(label + " (" + String.format("%.0f", porcentaje) + "%)");
        lbl.setFont(Tema.FONT_PEQUENA);
        lbl.setForeground(Tema.TEXTO_PRINCIPAL);
        lbl.setPreferredSize(new Dimension(200, 18));

        JPanel barContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.withAlpha(color, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                int barWidth = (int) (getWidth() * (porcentaje / 100.0));
                if (barWidth > 0) {
                    GradientPaint gp = new GradientPaint(0, 0, color, barWidth, 0, color.brighter());
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, barWidth, getHeight(), 8, 8);
                }
                g2.dispose();
            }
        };
        barContainer.setPreferredSize(new Dimension(0, 12));
        barContainer.setOpaque(false);

        p.add(lbl, BorderLayout.WEST);
        p.add(barContainer, BorderLayout.CENTER);
        return p;
    }
}
