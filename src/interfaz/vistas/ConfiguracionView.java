package interfaz.vistas;

import interfaz.componentes.*;
import modelo.Usuario;
import negocio.GestorUsuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ConfiguracionView extends JPanel {
    private GestorUsuario gestorUsuario;
    private MainFrame mainFrame;

    private ModernTextField txtNombre;
    private ModernTextField txtCarrera;
    private ModernTextField txtSemestre;
    private JCheckBox chkModoOscuro;

    public ConfiguracionView(GestorUsuario gestorUsuario, MainFrame mainFrame) {
        this.gestorUsuario = gestorUsuario;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(40, 50, 40, 50));
        actualizarView();
    }

    public void actualizarView() {
        removeAll();
        Usuario u = gestorUsuario.getUsuarioActivo();
        if (u == null) return;

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel lblTitle = new JLabel("Configuracion");
        lblTitle.setFont(Tema.FONT_TITULO);
        lblTitle.setForeground(Tema.TEXTO_PRINCIPAL);
        
        JLabel lblSub = new JLabel("Ajusta tus datos y preferencias");
        lblSub.setFont(Tema.FONT_REGULAR);
        lblSub.setForeground(Tema.TEXTO_SECUNDARIO);
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(lblTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(lblSub);
        
        header.add(titlePanel, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        JPanel formCard = new JPanel();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBackground(Tema.TARJETA);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Tema.BORDE, 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));
        formCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.setMaximumSize(new Dimension(500, 800));

        txtNombre = new ModernTextField("Nombre Completo");
        txtNombre.setText(u.getNombreCompleto());
        txtNombre.setMaximumSize(new Dimension(450, 42));

        txtCarrera = new ModernTextField("Carrera");
        txtCarrera.setText(u.getCarrera());
        txtCarrera.setMaximumSize(new Dimension(450, 42));

        txtSemestre = new ModernTextField("Semestre");
        txtSemestre.setText(String.valueOf(u.getSemestre()));
        txtSemestre.setMaximumSize(new Dimension(450, 42));

        chkModoOscuro = new JCheckBox("Modo Oscuro (Marino Intenso)");
        chkModoOscuro.setFont(Tema.FONT_REGULAR);
        chkModoOscuro.setForeground(Tema.TEXTO_PRINCIPAL);
        chkModoOscuro.setOpaque(false);
        chkModoOscuro.setSelected(u.isModoOscuro());

        ModernPasswordField txtPassActual = new ModernPasswordField("Contrasena Actual");
        txtPassActual.setMaximumSize(new Dimension(450, 42));
        ModernPasswordField txtPassNueva = new ModernPasswordField("Nueva Contrasena");
        txtPassNueva.setMaximumSize(new Dimension(450, 42));
        ModernPasswordField txtPassConfirmar = new ModernPasswordField("Confirmar Nueva Contrasena");
        txtPassConfirmar.setMaximumSize(new Dimension(450, 42));

        formCard.add(crearLabel("Datos Personales"));
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        formCard.add(txtNombre);
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        formCard.add(txtCarrera);
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        formCard.add(txtSemestre);
        formCard.add(Box.createRigidArea(new Dimension(0, 25)));
        
        formCard.add(crearLabel("Seguridad"));
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        formCard.add(txtPassActual);
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        formCard.add(txtPassNueva);
        formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        formCard.add(txtPassConfirmar);
        formCard.add(Box.createRigidArea(new Dimension(0, 25)));

        formCard.add(crearLabel("Apariencia"));
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));
        formCard.add(chkModoOscuro);
        
        formCard.add(Box.createRigidArea(new Dimension(0, 30)));
        
        ModernButton btnGuardar = new ModernButton("Guardar Cambios");
        btnGuardar.setColors(Tema.PRIMARIO, Tema.PRIMARIO_HOVER);
        btnGuardar.setMaximumSize(new Dimension(450, 42));
        btnGuardar.addActionListener(e -> guardarCambios(txtPassActual, txtPassNueva, txtPassConfirmar));
        formCard.add(btnGuardar);

        centerPanel.add(formCard);
        
        JScrollPane scroll = new JScrollPane(centerPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(Tema.FONT_BOLD);
        lbl.setForeground(Tema.TEXTO_PRINCIPAL);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void guardarCambios(ModernPasswordField txtPassActual, ModernPasswordField txtPassNueva, ModernPasswordField txtPassConfirmar) {
        Usuario u = gestorUsuario.getUsuarioActivo();
        if (u == null) return;
        
        try {
            // Validar contrasena
            String passActual = new String(txtPassActual.getPassword());
            String passNueva = new String(txtPassNueva.getPassword());
            String passConfirm = new String(txtPassConfirmar.getPassword());

            if (!passActual.isEmpty() || !passNueva.isEmpty() || !passConfirm.isEmpty()) {
                if (!u.getContrasena().equals(passActual)) {
                    ModernToast.show(mainFrame, "La contrasena actual es incorrecta.", ModernToast.Type.ERROR);
                    return;
                }
                if (passNueva.length() < 8) {
                    ModernToast.show(mainFrame, "La nueva contrasena debe tener al menos 8 caracteres.", ModernToast.Type.ERROR);
                    return;
                }
                if (!passNueva.equals(passConfirm)) {
                    ModernToast.show(mainFrame, "Las nuevas contrasenas no coinciden.", ModernToast.Type.ERROR);
                    return;
                }
                u.setContrasena(passNueva);
            }

            u.setNombreCompleto(txtNombre.getText().trim());
            u.setCarrera(txtCarrera.getText().trim());
            u.setSemestre(Integer.parseInt(txtSemestre.getText().trim()));
            
            boolean temaCambiado = (u.isModoOscuro() != chkModoOscuro.isSelected());
            u.setModoOscuro(chkModoOscuro.isSelected());
            
            gestorUsuario.guardarCambios();
            ModernToast.show(mainFrame, "Configuracion guardada exitosamente", ModernToast.Type.SUCCESS);
            
            txtPassActual.setText("");
            txtPassNueva.setText("");
            txtPassConfirmar.setText("");

            if (temaCambiado) {
                Tema.actualizarTema(u.isModoOscuro());
                mainFrame.refrescarTema();
            }
        } catch (NumberFormatException e) {
            ModernToast.show(mainFrame, "El semestre debe ser un numero valido", ModernToast.Type.ERROR);
        }
    }
}
