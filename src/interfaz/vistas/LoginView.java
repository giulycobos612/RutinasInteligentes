package interfaz.vistas;

import interfaz.componentes.*;
import modelo.Usuario;
import negocio.GestorUsuario;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
    private GestorUsuario gestorUsuario;
    private JFrame mainFrame;
    private Runnable onLoginSuccess;

    public LoginView(GestorUsuario gestorUsuario, JFrame mainFrame, Runnable onLoginSuccess) {
        this.gestorUsuario = gestorUsuario;
        this.mainFrame = mainFrame;
        this.onLoginSuccess = onLoginSuccess;
        setLayout(new BorderLayout());
        mostrarLogin();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        GradientPaint gp = new GradientPaint(0, 0, Tema.SIDEBAR_TOP, getWidth(), getHeight(), Tema.ACENTO);
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }

    private void mostrarLogin() {
        removeAll();

        ModernCard card = new ModernCard(20, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        card.setPreferredSize(new Dimension(480, 420));
        card.setMaximumSize(new Dimension(480, 420));

        // Icono removido por problemas de fuente

        JLabel lblTitulo = new JLabel("Rutinas Inteligentes");
        lblTitulo.setFont(Tema.FONT_TITULO);
        lblTitulo.setForeground(Tema.TEXTO_PRINCIPAL);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Inicia sesion para continuar");
        lblSub.setFont(Tema.FONT_REGULAR);
        lblSub.setForeground(Tema.TEXTO_SECUNDARIO);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        ModernTextField txtCorreo = new ModernTextField("Correo electronico");
        txtCorreo.setMaximumSize(new Dimension(380, 42));
        txtCorreo.setAlignmentX(Component.CENTER_ALIGNMENT);

        ModernPasswordField txtPass = new ModernPasswordField("Contrasena");
        txtPass.setMaximumSize(new Dimension(380, 42));
        txtPass.setAlignmentX(Component.CENTER_ALIGNMENT);

        ModernButton btnLogin = new ModernButton("Iniciar Sesion");
        btnLogin.setMaximumSize(new Dimension(380, 42));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.addActionListener(e -> {
            try {
                String correo = txtCorreo.getText();
                String pass = new String(txtPass.getPassword());
                Usuario u = gestorUsuario.iniciarSesion(correo, pass);
                ModernToast.show(mainFrame, "Bienvenido, " + u.getNombreCompleto() + "!", ModernToast.Type.SUCCESS);
                onLoginSuccess.run();
            } catch (Exception ex) {
                ModernToast.show(mainFrame, ex.getMessage(), ModernToast.Type.ERROR);
            }
        });

        ModernButton btnIrRegistro = new ModernButton("No tienes cuenta? Registrate");
        btnIrRegistro.setColors(Tema.TARJETA, Tema.FONDO);
        btnIrRegistro.setForeground(Tema.PRIMARIO);
        btnIrRegistro.setMaximumSize(new Dimension(380, 42));
        btnIrRegistro.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIrRegistro.addActionListener(e -> mostrarRegistro());

        // card.add(lblIcon); (removido)
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblSub);
        card.add(Box.createRigidArea(new Dimension(0, 30)));
        card.add(txtCorreo);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(txtPass);
        card.add(Box.createRigidArea(new Dimension(0, 25)));
        card.add(btnLogin);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(btnIrRegistro);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(card);

        add(centerPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrarRegistro() {
        removeAll();

        ModernCard card = new ModernCard(20, true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel lblTitulo = new JLabel("Crear Cuenta");
        lblTitulo.setFont(Tema.FONT_TITULO);
        lblTitulo.setForeground(Tema.TEXTO_PRINCIPAL);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Completa tus datos para registrarte");
        lblSub.setFont(Tema.FONT_REGULAR);
        lblSub.setForeground(Tema.TEXTO_SECUNDARIO);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        ModernTextField txtNombre = new ModernTextField("Nombre completo");
        ModernTextField txtCorreo = new ModernTextField("Correo electronico");
        ModernTextField txtCarrera = new ModernTextField("Carrera");
        ModernTextField txtSemestre = new ModernTextField("Semestre (numero)");
        ModernTextField txtMaterias = new ModernTextField("Cantidad de materias");
        ModernTextField txtHoras = new ModernTextField("Horas de estudio/semana");
        ModernPasswordField txtPass = new ModernPasswordField("Contrasena (min 8 caracteres)");

        Component[] fields = {txtNombre, txtCorreo, txtCarrera, txtSemestre, txtMaterias, txtHoras, txtPass};
        for (Component c : fields) {
            ((JComponent) c).setMaximumSize(new Dimension(380, 42));
            ((JComponent) c).setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        ModernButton btnRegistrar = new ModernButton("Registrarse");
        btnRegistrar.setColors(Tema.SECUNDARIO, Tema.SECUNDARIO.darker());
        btnRegistrar.setMaximumSize(new Dimension(380, 42));
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.addActionListener(e -> {
            try {
                int sem = Integer.parseInt(txtSemestre.getText().trim());
                int mat = Integer.parseInt(txtMaterias.getText().trim());
                int hor = Integer.parseInt(txtHoras.getText().trim());

                Usuario nuevo = new Usuario(
                        txtNombre.getText(), txtCorreo.getText(), txtCarrera.getText(),
                        sem, mat, hor, new String(txtPass.getPassword()), ""
                );
                String msg = gestorUsuario.registrarUsuario(nuevo);
                ModernToast.show(mainFrame, msg, ModernToast.Type.SUCCESS);
                mostrarLogin();
            } catch (NumberFormatException ex) {
                ModernToast.show(mainFrame, "Los campos numericos son invalidos.", ModernToast.Type.ERROR);
            } catch (Exception ex) {
                ModernToast.show(mainFrame, ex.getMessage(), ModernToast.Type.ERROR);
            }
        });

        ModernButton btnVolver = new ModernButton("Volver al Login");
        btnVolver.setColors(Tema.TARJETA, Tema.FONDO);
        btnVolver.setForeground(Tema.TEXTO_SECUNDARIO);
        btnVolver.setMaximumSize(new Dimension(380, 42));
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.addActionListener(e -> mostrarLogin());

        card.add(lblTitulo);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblSub);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        for (Component c : fields) {
            card.add(c);
            card.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(btnRegistrar);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(btnVolver);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(card);

        add(centerPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
