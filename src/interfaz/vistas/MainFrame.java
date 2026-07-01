package interfaz.vistas;

import interfaz.componentes.*;
import negocio.ServicioAlertas;
import negocio.GestorMateriasTareas;
import negocio.GestorProgreso;
import negocio.GestorRutinas;
import negocio.GestorUsuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private GestorUsuario gestorUsuario;
    private GestorMateriasTareas gestorMateriasTareas;
    private GestorRutinas gestorRutinas;
    private GestorProgreso gestorProgreso;

    private ServicioAlertas servicioAlertas;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private LoginView loginView;
    private DashboardView dashboardView;
    private MateriasTareasView materiasTareasView;
    private RutinasView rutinasView;
    private ConfiguracionView configuracionView;

    private JPanel sidebar;
    private String currentView = "DASHBOARD";

    public MainFrame() {
        gestorUsuario = new GestorUsuario();
        gestorMateriasTareas = new GestorMateriasTareas(gestorUsuario);
        gestorRutinas = new GestorRutinas(gestorUsuario);
        gestorProgreso = new GestorProgreso(gestorUsuario);
        servicioAlertas = new ServicioAlertas(gestorUsuario, this);

        setTitle("Rutinas Inteligentes");
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setBackground(Tema.FONDO);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Tema.FONDO);

        loginView = new LoginView(gestorUsuario, this, this::onLoginSuccess);

        contentPanel.add(loginView, "LOGIN");

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    private void onLoginSuccess() {
        Tema.actualizarTema(gestorUsuario.getUsuarioActivo().isModoOscuro());
        
        getContentPane().setBackground(Tema.FONDO);
        contentPanel.setBackground(Tema.FONDO);

        dashboardView = new DashboardView(gestorProgreso);
        materiasTareasView = new MateriasTareasView(gestorMateriasTareas, this);
        rutinasView = new RutinasView(gestorRutinas, this);
        configuracionView = new ConfiguracionView(gestorUsuario, this);

        contentPanel.add(dashboardView, "DASHBOARD");
        contentPanel.add(materiasTareasView, "MATERIAS_TAREAS");
        contentPanel.add(rutinasView, "RUTINAS");
        contentPanel.add(configuracionView, "CONFIGURACION");

        construirSidebar();
        add(sidebar, BorderLayout.WEST);

        cardLayout.show(contentPanel, "DASHBOARD");
        servicioAlertas.iniciar();
        revalidate();
        repaint();
    }

    public void onLogout() {
        servicioAlertas.detener();
        gestorUsuario.cerrarSesion();
        
        if (sidebar != null) remove(sidebar);
        sidebar = null;
        
        Tema.actualizarTema(false);
        getContentPane().setBackground(Tema.FONDO);
        contentPanel.setBackground(Tema.FONDO);
        
        cardLayout.show(contentPanel, "LOGIN");
        revalidate();
        repaint();
    }

    public void refrescarTema() {
        getContentPane().setBackground(Tema.FONDO);
        contentPanel.setBackground(Tema.FONDO);
        
        contentPanel.remove(dashboardView);
        contentPanel.remove(materiasTareasView);
        contentPanel.remove(rutinasView);
        contentPanel.remove(configuracionView);
        remove(sidebar);

        dashboardView = new DashboardView(gestorProgreso);
        materiasTareasView = new MateriasTareasView(gestorMateriasTareas, this);
        rutinasView = new RutinasView(gestorRutinas, this);
        configuracionView = new ConfiguracionView(gestorUsuario, this);

        contentPanel.add(dashboardView, "DASHBOARD");
        contentPanel.add(materiasTareasView, "MATERIAS_TAREAS");
        contentPanel.add(rutinasView, "RUTINAS");
        contentPanel.add(configuracionView, "CONFIGURACION");

        construirSidebar();
        add(sidebar, BorderLayout.WEST);

        cardLayout.show(contentPanel, "CONFIGURACION");
        currentView = "CONFIGURACION";
        
        revalidate();
        repaint();
    }

    private void construirSidebar() {
        sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Tema.SIDEBAR_TOP, 0, getHeight(), Tema.SIDEBAR_BOTTOM);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(280, getHeight()));
        sidebar.setBorder(new EmptyBorder(25, 15, 25, 15));

        // Logo / título
        JLabel title = new JLabel("Rutinas");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Estudio Inteligente");
        subtitle.setFont(Tema.FONT_PEQUENA);
        subtitle.setForeground(new Color(255, 255, 255, 180));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(title);
        sidebar.add(Box.createRigidArea(new Dimension(0, 3)));
        sidebar.add(subtitle);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // Separador
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(250, 1));
        sep.setForeground(new Color(255, 255, 255, 60));
        sidebar.add(sep);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        sidebar.add(crearSidebarBoton("Progreso Academico", "DASHBOARD"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(crearSidebarBoton("Tareas y Materias", "MATERIAS_TAREAS"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(crearSidebarBoton("Rutinas de Estudio", "RUTINAS"));
        sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        sidebar.add(crearSidebarBoton("Configuracion", "CONFIGURACION"));

        sidebar.add(Box.createVerticalGlue());

        // Info del usuario
        String nombre = gestorUsuario.getUsuarioActivo().getNombreCompleto();
        JLabel lblUser = new JLabel(nombre);
        lblUser.setFont(Tema.FONT_BOLD);
        lblUser.setForeground(Color.WHITE);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblUser);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        ModernButton btnLogout = new ModernButton("Cerrar Sesion");
        btnLogout.setColors(new Color(255, 255, 255, 30), new Color(255, 255, 255, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogout.setMaximumSize(new Dimension(250, 38));
        btnLogout.addActionListener(e -> onLogout());
        sidebar.add(btnLogout);
    }

    private JButton crearSidebarBoton(String texto, String cardName) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (currentView.equals(cardName)) {
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(Tema.FONT_REGULAR);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(250, 42));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        btn.addActionListener(e -> {
            currentView = cardName;
            cardLayout.show(contentPanel, cardName);
            if (cardName.equals("DASHBOARD") && dashboardView != null) dashboardView.actualizarView();
            if (cardName.equals("MATERIAS_TAREAS") && materiasTareasView != null) materiasTareasView.actualizarView();
            if (cardName.equals("RUTINAS") && rutinasView != null) rutinasView.actualizarView();
            sidebar.repaint();
        });
        return btn;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
