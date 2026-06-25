package interfaz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

public class InterfazGrafica extends JFrame {

    // =========================
    // PALETA DE COLORES
    // =========================
    private static final Color BG_MAIN = Color.decode("#1E1E2E");
    private static final Color SURFACE = Color.decode("#27293D");
    private static final Color HOVER = Color.decode("#353851");
    private static final Color PRIMARY = Color.decode("#6C63FF");
    private static final Color PRIMARY_HOVER = Color.decode("#7E74FF");
    private static final Color TEXT_MAIN = Color.decode("#FFFFFF");
    private static final Color TEXT_MUTED = Color.decode("#A7A9BE");

    // =========================
    // TIPOGRAFÍA
    // =========================
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 18);
    private static final Font BODY_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font BOLD_BODY_FONT = new Font("SansSerif", Font.BOLD, 14);

    public InterfazGrafica() {
        configurarUIGlobal();

        setTitle("Sistema de Rutinas Inteligentes");
        setSize(900, 650);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_MAIN);

        // Configuración del JTabbedPane Moderno
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BG_MAIN);
        tabbedPane.setForeground(TEXT_MAIN);
        tabbedPane.setFont(BOLD_BODY_FONT);
        tabbedPane.setUI(new ModernTabbedPaneUI());
        tabbedPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Construir Pestañas con la lógica de MainUsuarios
        tabbedPane.addTab("  Usuarios  ", crearPanelEstructurado("Gestión de Usuarios", getAccionesUsuarios()));
        tabbedPane.addTab("  Materias y Tareas  ", crearPanelEstructurado("Materias y Tareas", getAccionesMaterias()));
        tabbedPane.addTab("  Rutinas  ", crearPanelEstructurado("Rutinas Semanales", getAccionesRutinas()));
        tabbedPane.addTab("  Progreso Académico  ", crearPanelEstructurado("Progreso Académico", getAccionesProgreso()));

        add(tabbedPane, BorderLayout.CENTER);
    }

    // =========================================================
    // CONSTRUCCIÓN DE LA LÓGICA (MAIN USUARIOS)
    // =========================================================

    private List<AccionUI> getAccionesUsuarios() {
        return Arrays.asList(
            new AccionUI("👤", "Registrar Usuario", "Añadir un nuevo usuario al sistema.", () -> MainUsuarios.registrarUsuario()),
            new AccionUI("🔑", "Iniciar Sesión", "Acceder con credenciales existentes.", this::customLogin),
            new AccionUI("🔒", "Cerrar Sesión", "Finalizar la sesión actual.", this::customLogout),
            new AccionUI("🚪", "Salir", "Cerrar completamente la aplicación.", () -> System.exit(0))
        );
    }

    private List<AccionUI> getAccionesMaterias() {
        return Arrays.asList(
            new AccionUI("📘", "Registrar Materia", "Añade una nueva materia al ciclo.", () -> verificarSesion(MainUsuarios::registrarMateria)),
            new AccionUI("📝", "Registrar Tarea", "Programa una nueva tarea.", () -> verificarSesion(MainUsuarios::registrarTarea)),
            new AccionUI("👀", "Ver Materias", "Lista todas las materias.", () -> verificarSesion(() -> JOptionPane.showMessageDialog(this, MainUsuarios.gestorMaterias.listarMaterias()))),
            new AccionUI("📋", "Ver Tareas", "Lista todas las tareas pendientes.", () -> verificarSesion(() -> JOptionPane.showMessageDialog(this, MainUsuarios.gestorMaterias.listarTareas()))),
            new AccionUI("❌", "Eliminar Materia", "Borra una materia del sistema.", () -> verificarSesion(MainUsuarios::eliminarMateria)),
            new AccionUI("🗑️", "Eliminar Tarea", "Borra una tarea del sistema.", () -> verificarSesion(MainUsuarios::eliminarTarea)),
            new AccionUI("✅", "Marcar Completada", "Actualiza el estado de una tarea.", () -> verificarSesion(MainUsuarios::marcarTareaCompletada))
        );
    }

    private List<AccionUI> getAccionesRutinas() {
        return Arrays.asList(
            new AccionUI("➕", "Agregar Sesión", "Programa una sesión de estudio.", () -> verificarSesion(MainUsuarios::agregarSesion)),
            new AccionUI("📅", "Ver Rutina", "Revisa tu horario estructurado.", () -> verificarSesion(MainUsuarios::verRutina)),
            new AccionUI("⏱", "Reprogramar Sesión", "Mueve una sesión a otro horario.", () -> verificarSesion(MainUsuarios::reprogramarSesion)),
            new AccionUI("⭐", "Marcar Cumplida", "Registra una sesión finalizada.", () -> verificarSesion(MainUsuarios::marcarCumplida))
        );
    }

    private List<AccionUI> getAccionesProgreso() {
        return Arrays.asList(
            new AccionUI("📊", "Ver Reporte", "Muestra un resumen de tu progreso global.", () -> verificarSesion(MainUsuarios::mostrarReporteProgreso))
        );
    }

    // =========================================================
    // LÓGICA DE SESIÓN Y COMPONENTES
    // =========================================================

    private void customLogin() {
        String correo = JOptionPane.showInputDialog(this, "Correo electrónico:");
        if (correo == null || correo.trim().isEmpty()) return;

        String contrasena = JOptionPane.showInputDialog(this, "Contraseña:");
        if (contrasena == null || contrasena.trim().isEmpty()) return;

        MainUsuarios.usuarioActivo = MainUsuarios.gestorUsuario.iniciarSesion(correo, contrasena);

        if (MainUsuarios.usuarioActivo == null) {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "¡Bienvenido, " + MainUsuarios.usuarioActivo.getNombreCompleto() + "!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void customLogout() {
        if (MainUsuarios.usuarioActivo != null) {
            MainUsuarios.usuarioActivo = null;
            JOptionPane.showMessageDialog(this, "Sesión cerrada correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No hay ninguna sesión activa.");
        }
    }

    private void verificarSesion(Runnable accion) {
        if (MainUsuarios.usuarioActivo != null) {
            accion.run();
        } else {
            JOptionPane.showMessageDialog(this, "Debe iniciar sesión primero para acceder a esta opción.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
        }
    }

    // =========================================================
    // CONSTRUCCIÓN VISUAL ESTRUCTURADA
    // =========================================================

    private JPanel crearPanelEstructurado(String titulo, List<AccionUI> acciones) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_MAIN);
        panel.setBorder(new EmptyBorder(25, 40, 25, 40));

        // 1. Título y Separador
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_MAIN);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22)); // Un poco más grande para el header
        lblTitulo.setForeground(TEXT_MAIN);
        header.add(lblTitulo, BorderLayout.NORTH);

        JPanel separador = new JPanel();
        separador.setBackground(PRIMARY);
        separador.setPreferredSize(new Dimension(0, 3));
        
        JPanel pnlSep = new JPanel(new BorderLayout());
        pnlSep.setBackground(BG_MAIN);
        pnlSep.setBorder(new EmptyBorder(10, 0, 30, 0));
        pnlSep.add(separador, BorderLayout.CENTER);
        
        header.add(pnlSep, BorderLayout.SOUTH);
        panel.add(header, BorderLayout.NORTH);

        // 2. Formularios / Botones Centrados
        JPanel grid = new JPanel(new GridLayout(0, 2, 20, 20)); // 2 Columnas con espacio generoso
        grid.setBackground(BG_MAIN);
        
        for (AccionUI accion : acciones) {
            grid.add(new ActionCard(accion));
        }

        // Wrapper para alinear arriba y no estirar los componentes
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_MAIN);
        wrapper.add(grid, BorderLayout.NORTH);

        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(BG_MAIN);

        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private void configurarUIGlobal() {
        // Configurar popups y tablas globalmente al estilo oscuro profesional
        UIManager.put("OptionPane.background", SURFACE);
        UIManager.put("Panel.background", SURFACE);
        UIManager.put("OptionPane.messageForeground", TEXT_MAIN);
        UIManager.put("Button.background", PRIMARY);
        UIManager.put("Button.foreground", TEXT_MAIN);
        UIManager.put("Button.font", BOLD_BODY_FONT);
        UIManager.put("TextField.background", BG_MAIN);
        UIManager.put("TextField.foreground", TEXT_MAIN);
        
        // Estilos para JTable (si se llega a instanciar)
        UIManager.put("Table.background", BG_MAIN);
        UIManager.put("Table.foreground", TEXT_MAIN);
        UIManager.put("Table.gridColor", SURFACE);
        UIManager.put("Table.rowHeight", 35);
        UIManager.put("Table.showVerticalLines", false);
        UIManager.put("TableHeader.background", SURFACE);
        UIManager.put("TableHeader.foreground", TEXT_MAIN);
        UIManager.put("Table.alternateRowColor", SURFACE);
    }

    // =========================================================
    // CLASES SWING PERSONALIZADAS (INTERFAZ MODERNA)
    // =========================================================

    // Estructura de datos para armar la interfaz automáticamente
    class AccionUI {
        String icono, titulo, descripcion;
        Runnable accion;

        public AccionUI(String icono, String titulo, String descripcion, Runnable accion) {
            this.icono = icono;
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.accion = accion;
        }
    }

    // Botón en forma de Tarjeta moderna (con Sombra y Hover)
    class ActionCard extends JPanel {
        private final RoundedCard cardBase;

        public ActionCard(AccionUI accionUI) {
            setLayout(new BorderLayout());
            setOpaque(false);
            setBorder(new EmptyBorder(5, 5, 5, 5));

            cardBase = new RoundedCard(SURFACE, 16);
            cardBase.setLayout(new BorderLayout(15, 0));
            cardBase.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding interno generoso
            cardBase.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Icono
            JLabel lblIcono = new JLabel(accionUI.icono);
            lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 28));
            lblIcono.setForeground(TEXT_MAIN);
            cardBase.add(lblIcono, BorderLayout.WEST);

            // Textos
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);

            JLabel lblTit = new JLabel(accionUI.titulo);
            lblTit.setFont(TITLE_FONT);
            lblTit.setForeground(TEXT_MAIN);

            JLabel lblDesc = new JLabel(accionUI.descripcion);
            lblDesc.setFont(BODY_FONT);
            lblDesc.setForeground(TEXT_MUTED);

            textPanel.add(lblTit);
            textPanel.add(Box.createVerticalStrut(5));
            textPanel.add(lblDesc);

            cardBase.add(textPanel, BorderLayout.CENTER);

            // Flecha 
            JLabel lblArrow = new JLabel("→");
            lblArrow.setFont(TITLE_FONT);
            lblArrow.setForeground(TEXT_MUTED);
            cardBase.add(lblArrow, BorderLayout.EAST);

            // Efectos Mouse (Hover y Acción)
            cardBase.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    cardBase.setBgColor(HOVER);
                    lblArrow.setForeground(TEXT_MAIN);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    cardBase.setBgColor(SURFACE);
                    lblArrow.setForeground(TEXT_MUTED);
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    cardBase.setBgColor(PRIMARY.darker());
                    SwingUtilities.invokeLater(accionUI.accion);
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    cardBase.setBgColor(HOVER);
                }
            });

            add(cardBase, BorderLayout.CENTER);
        }
    }

    // Tarjeta con esquinas redondeadas y sombra suave
    class RoundedCard extends JPanel {
        protected int radius;
        protected Color bgColor;

        public RoundedCard(Color bgColor, int radius) {
            this.bgColor = bgColor;
            this.radius = radius;
            setOpaque(false);
        }

        public void setBgColor(Color bgColor) {
            this.bgColor = bgColor;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Sombra
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillRoundRect(3, 5, getWidth() - 6, getHeight() - 6, radius, radius);

            // Fondo principal
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, radius, radius);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Personalización Avanzada del JTabbedPane
    class ModernTabbedPaneUI extends BasicTabbedPaneUI {
        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (isSelected) {
                g2.setColor(PRIMARY);
            } else {
                g2.setColor(SURFACE);
            }
            g2.fillRoundRect(x, y + 2, w - 2, h + 8, 12, 12); // Pestañas redondeadas arriba
            g2.dispose();
        }

        @Override
        protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
            g.setFont(font);
            g.setColor(TEXT_MAIN);
            int y = textRect.y + metrics.getAscent() + 2;
            g.drawString(title, textRect.x, y);
        }

        @Override
        protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
            // Eliminar bordes por defecto
        }

        @Override
        protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
            // Eliminar cuadro de focus
        }

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            // Línea principal debajo de las pestañas
            int width = tabPane.getWidth();
            Insets insets = tabPane.getInsets();
            int x = insets.left;
            int y = insets.top + calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight);
            int w = width - insets.right - insets.left;

            g.setColor(PRIMARY);
            g.fillRect(x, y, w, 3);
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InterfazGrafica().setVisible(true);
        });
    }
}