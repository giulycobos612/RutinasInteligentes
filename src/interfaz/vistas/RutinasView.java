package interfaz.vistas;

import interfaz.componentes.*;
import modelo.RutinasEstudio;
import negocio.GestorRutinas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class RutinasView extends JPanel {
    private GestorRutinas gestor;
    private JFrame mainFrame;
    private JPanel mainContent;
    private CardLayout contentCard;

    public RutinasView(GestorRutinas gestor, JFrame mainFrame) {
        this.gestor = gestor;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Tema.FONDO);

        contentCard = new CardLayout();
        mainContent = new JPanel(contentCard);
        mainContent.setOpaque(false);
        add(mainContent, BorderLayout.CENTER);

        actualizarView();
    }

    public void actualizarView() {
        mainContent.removeAll();

        JPanel listaView = new JPanel(new BorderLayout(0, 15));
        listaView.setOpaque(false);
        listaView.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("Rutinas de Estudio");
        lblTitulo.setFont(Tema.FONT_TITULO);
        lblTitulo.setForeground(Tema.TEXTO_PRINCIPAL);
        JLabel lblDesc = new JLabel("Organiza tus sesiones de estudio y recibe recordatorios");
        lblDesc.setFont(Tema.FONT_REGULAR);
        lblDesc.setForeground(Tema.TEXTO_SECUNDARIO);
        titlePanel.add(lblTitulo);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 3)));
        titlePanel.add(lblDesc);
        header.add(titlePanel, BorderLayout.WEST);

        ModernButton btnAdd = new ModernButton("+ Agendar Sesion");
        btnAdd.setColors(Tema.SECUNDARIO, Tema.SECUNDARIO.darker());
        btnAdd.addActionListener(e -> mostrarFormSesion(null));
        JPanel actionsP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsP.setOpaque(false);
        actionsP.add(btnAdd);
        header.add(actionsP, BorderLayout.EAST);

        listaView.add(header, BorderLayout.NORTH);

        // Lista de sesiones
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setOpaque(false);

        if (gestor.getRutinaSemanal().isEmpty()) {
            listaPanel.add(crearEstadoVacio());
        } else {
            // Separar en: Hoy, Proximas, Pasadas
            LocalDate hoy = LocalDate.now();

            // Hoy
            boolean hayHoy = false;
            for (RutinasEstudio r : gestor.getRutinaSemanal()) {
                if (r.getDia().equals(hoy)) {
                    if (!hayHoy) {
                        listaPanel.add(crearSeccionHeader("Hoy", Tema.ACENTO));
                        hayHoy = true;
                    }
                    listaPanel.add(crearSesionCard(r, Tema.ACENTO));
                    listaPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            // Proximas
            boolean hayProximas = false;
            for (RutinasEstudio r : gestor.getRutinaSemanal()) {
                if (r.getDia().isAfter(hoy)) {
                    if (!hayProximas) {
                        listaPanel.add(crearSeccionHeader("Proximas sesiones", Tema.PRIMARIO));
                        hayProximas = true;
                    }
                    listaPanel.add(crearSesionCard(r, Tema.PRIMARIO));
                    listaPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            // Pasadas
            boolean hayPasadas = false;
            for (RutinasEstudio r : gestor.getRutinaSemanal()) {
                if (r.getDia().isBefore(hoy)) {
                    if (!hayPasadas) {
                        listaPanel.add(crearSeccionHeader("Sesiones pasadas", Tema.TEXTO_SECUNDARIO));
                        hayPasadas = true;
                    }
                    listaPanel.add(crearSesionCard(r, Tema.TEXTO_SECUNDARIO));
                    listaPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }

        JScrollPane scroll = new JScrollPane(listaPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        listaView.add(scroll, BorderLayout.CENTER);

        mainContent.add(listaView, "LISTA");
        contentCard.show(mainContent, "LISTA");
        revalidate();
        repaint();
    }

    // ===================== TARJETA DE SESION =====================
    private ModernCard crearSesionCard(RutinasEstudio r, Color accent) {
        ModernCard card = new ModernCard(12, true, accent);
        card.setAccentTop(false); // barra lateral izquierda
        card.setLayout(new BorderLayout(15, 0));
        card.setBorder(new EmptyBorder(15, 20, 15, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // Izquierda: checkbox + nombre
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        String nombre = r.getNombreSesion().isEmpty()
                ? (r.getTarea() != null ? r.getTarea().getNombreTarea() : "Sesion")
                : r.getNombreSesion();

        JCheckBox chk = new JCheckBox(nombre);
        chk.setFont(Tema.FONT_SUBTITULO);
        chk.setOpaque(false);
        chk.setSelected(r.isCumplida());
        chk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (r.isCumplida()) {
            chk.setForeground(Tema.TEXTO_SECUNDARIO);
        } else {
            chk.setForeground(Tema.TEXTO_PRINCIPAL);
        }
        chk.addActionListener(e -> {
            gestor.marcarCumplida(r, chk.isSelected());
            actualizarView();
        });

        JLabel timeInfo = new JLabel(r.getDiaFormateado() + "  |  " + r.getHoraInicioFormateada() + " - " + r.getHoraFinFormateada() + "  (" + r.getDuracionMinutos() + " min)");
        timeInfo.setFont(Tema.FONT_PEQUENA);
        timeInfo.setForeground(Tema.TEXTO_SECUNDARIO);
        timeInfo.setBorder(new EmptyBorder(0, 26, 0, 0));

        left.add(chk);
        left.add(timeInfo);

        // Derecha: botones editar y eliminar
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);

        // Badge estado
        if (r.isCumplida()) {
            JLabel badgeCumplida = crearBadge("Cumplida", Tema.EXITO);
            right.add(badgeCumplida);
        } else if (r.isRecordatorioActivado()) {
            JLabel badgeAlerta = crearBadge("Recordatorio", Tema.INFO);
            right.add(badgeAlerta);
        }

        ModernButton btnEdit = new ModernButton("Editar");
        btnEdit.setColors(Tema.INFO, Tema.INFO.darker());
        btnEdit.setFont(Tema.FONT_PEQUENA);
        btnEdit.setPreferredSize(new Dimension(65, 28));
        btnEdit.addActionListener(e -> mostrarFormSesion(r));

        ModernButton btnDel = new ModernButton("Eliminar");
        btnDel.setColors(Tema.PELIGRO, Tema.PELIGRO.darker());
        btnDel.setFont(Tema.FONT_PEQUENA);
        btnDel.setPreferredSize(new Dimension(75, 28));
        btnDel.addActionListener(e -> {
            gestor.eliminarSesion(r);
            actualizarView();
        });

        right.add(btnEdit);
        right.add(btnDel);

        card.add(left, BorderLayout.CENTER);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    // ===================== FORM INLINE: SESION =====================
    private void mostrarFormSesion(RutinasEstudio editando) {
        JPanel formView = new JPanel(new BorderLayout());
        formView.setOpaque(false);
        formView.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        ModernButton btnBack = new ModernButton("< Volver");
        btnBack.setColors(Tema.TARJETA, Tema.FONDO);
        btnBack.setForeground(Tema.PRIMARIO);
        btnBack.addActionListener(e -> actualizarView());
        header.add(btnBack);
        formView.add(header, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        ModernCard card = new ModernCard(20, true, Tema.ACENTO);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setPreferredSize(new Dimension(460, 650));
        card.setMaximumSize(new Dimension(460, 650));

        JLabel lblTitle = new JLabel(editando == null ? "Nueva Sesion de Estudio" : "Editar Sesion");
        lblTitle.setFont(Tema.FONT_TITULO);
        lblTitle.setForeground(Tema.TEXTO_PRINCIPAL);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        ModernTextField txtNombre = new ModernTextField("Nombre de la sesion");
        txtNombre.setMaximumSize(new Dimension(380, 42));

        // DatePicker
        final LocalDate[] fechaSel = {editando != null ? editando.getDia() : null};
        JLabel lblFecha = new JLabel(fechaSel[0] != null ? "Fecha: " + fechaSel[0].format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Selecciona la fecha:");
        lblFecha.setFont(Tema.FONT_BOLD);
        lblFecha.setForeground(Tema.ACENTO);
        lblFecha.setAlignmentX(Component.LEFT_ALIGNMENT);

        DatePickerPanel datePicker = new DatePickerPanel(date -> {
            fechaSel[0] = date;
            lblFecha.setText("Fecha: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        });
        datePicker.setMaximumSize(new Dimension(380, 250));
        datePicker.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (fechaSel[0] != null) datePicker.setSelectedDate(fechaSel[0]);

        // Horas
        JPanel horasPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        horasPanel.setOpaque(false);
        horasPanel.setMaximumSize(new Dimension(380, 42));

        String[] horas = new String[24];
        for (int i = 0; i < 24; i++) horas[i] = String.format("%02d", i);
        String[] minutos = new String[60];
        for (int i = 0; i < 60; i++) minutos[i] = String.format("%02d", i);

        JComboBox<String> cbHoraIni = new JComboBox<>(horas);
        JComboBox<String> cbMinIni = new JComboBox<>(minutos);
        JComboBox<String> cbHoraFin = new JComboBox<>(horas);
        JComboBox<String> cbMinFin = new JComboBox<>(minutos);

        cbHoraIni.setFont(Tema.FONT_REGULAR);
        cbMinIni.setFont(Tema.FONT_REGULAR);
        cbHoraFin.setFont(Tema.FONT_REGULAR);
        cbMinFin.setFont(Tema.FONT_REGULAR);

        JPanel pnlIni = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        pnlIni.setOpaque(false);
        pnlIni.add(crearLabel("Inicio:"));
        pnlIni.add(cbHoraIni);
        pnlIni.add(crearLabel(":"));
        pnlIni.add(cbMinIni);

        JPanel pnlFin = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        pnlFin.setOpaque(false);
        pnlFin.add(crearLabel("Fin:"));
        pnlFin.add(cbHoraFin);
        pnlFin.add(crearLabel(":"));
        pnlFin.add(cbMinFin);
        
        horasPanel.add(pnlIni);
        horasPanel.add(pnlFin);

        JCheckBox chkAlerts = new JCheckBox("Activar recordatorios de sesion");
        chkAlerts.setOpaque(false);
        chkAlerts.setFont(Tema.FONT_REGULAR);
        chkAlerts.setForeground(Tema.TEXTO_PRINCIPAL);

        if (editando != null) {
            txtNombre.setText(editando.getNombreSesion());
            cbHoraIni.setSelectedIndex(editando.getHoraInicio().getHour());
            cbMinIni.setSelectedIndex(editando.getHoraInicio().getMinute());
            cbHoraFin.setSelectedIndex(editando.getHoraFin().getHour());
            cbMinFin.setSelectedIndex(editando.getHoraFin().getMinute());
            chkAlerts.setSelected(editando.isRecordatorioActivado());
        } else {
            LocalTime now = LocalTime.now();
            int hIni = (now.getHour() + 1) % 24;
            int hFin = (now.getHour() + 2) % 24;
            if (hFin <= hIni) hFin = 23;
            cbHoraIni.setSelectedIndex(hIni);
            cbMinIni.setSelectedIndex(0);
            cbHoraFin.setSelectedIndex(hFin);
            cbMinFin.setSelectedIndex(0);
        }

        ModernButton btnGuardar = new ModernButton(editando == null ? "Agendar Sesion" : "Guardar Cambios");
        btnGuardar.setColors(Tema.ACENTO, Tema.ACENTO.darker());
        btnGuardar.setMaximumSize(new Dimension(380, 42));
        btnGuardar.addActionListener(e -> {
            try {
                if (fechaSel[0] == null) {
                    ModernToast.show(mainFrame, "Selecciona una fecha en el calendario.", ModernToast.Type.WARNING);
                    return;
                }
                LocalTime hIni = LocalTime.of(cbHoraIni.getSelectedIndex(), cbMinIni.getSelectedIndex());
                LocalTime hFin = LocalTime.of(cbHoraFin.getSelectedIndex(), cbMinFin.getSelectedIndex());

                if (editando != null) {
                    gestor.reprogramarSesion(editando, fechaSel[0], hIni, hFin);
                    if (!txtNombre.getText().trim().isEmpty()) editando.setNombreSesion(txtNombre.getText());
                    editando.setRecordatorioActivado(chkAlerts.isSelected());
                    ModernToast.show(mainFrame, "Sesion actualizada!", ModernToast.Type.SUCCESS);
                } else {
                    RutinasEstudio nueva = new RutinasEstudio(fechaSel[0], hIni, hFin);
                    if (!txtNombre.getText().trim().isEmpty()) nueva.setNombreSesion(txtNombre.getText());
                    nueva.setRecordatorioActivado(chkAlerts.isSelected());
                    gestor.agregarSesion(nueva);
                    ModernToast.show(mainFrame, "Sesion agendada correctamente!", ModernToast.Type.SUCCESS);
                }
                actualizarView();
            } catch (DateTimeParseException ex) {
                ModernToast.show(mainFrame, "Formato de hora invalido. Usa HH:mm (ej: 14:30)", ModernToast.Type.ERROR);
            } catch (Exception ex) {
                ModernToast.show(mainFrame, ex.getMessage(), ModernToast.Type.ERROR);
            }
        });

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(crearLabel("Nombre (opcional)"));
        card.add(txtNombre);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblFecha);
        card.add(datePicker);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(crearLabel("Horario"));
        card.add(horasPanel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(chkAlerts);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(btnGuardar);

        centerWrapper.add(card);
        formView.add(centerWrapper, BorderLayout.CENTER);

        mainContent.add(formView, "FORM_SESION");
        contentCard.show(mainContent, "FORM_SESION");
    }

    // ===================== HELPERS =====================
    private JLabel crearLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Tema.FONT_PEQUENA);
        lbl.setForeground(Tema.TEXTO_SECUNDARIO);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0, 2, 3, 0));
        return lbl;
    }

    private JLabel crearBadge(String text, Color color) {
        JLabel badge = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.withAlpha(color, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(Tema.FONT_PEQUENA);
        badge.setForeground(color);
        badge.setBorder(new EmptyBorder(3, 10, 3, 10));
        badge.setOpaque(false);
        return badge;
    }

    private JPanel crearSeccionHeader(String titulo, Color color) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(10, 0, 5, 0));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(Tema.FONT_BOLD);
        lbl.setForeground(color);
        p.add(lbl);
        return p;
    }

    private JPanel crearEstadoVacio() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(60, 0, 0, 0));

        JLabel lblIcon = new JLabel("\u2611", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        lblIcon.setForeground(Tema.withAlpha(Tema.SECUNDARIO, 100));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTit = new JLabel("Sin sesiones agendadas");
        lblTit.setFont(Tema.FONT_SUBTITULO);
        lblTit.setForeground(Tema.TEXTO_PRINCIPAL);
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblD = new JLabel("Agenda tu primera sesion de estudio para empezar.");
        lblD.setFont(Tema.FONT_REGULAR);
        lblD.setForeground(Tema.TEXTO_SECUNDARIO);
        lblD.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(lblIcon);
        p.add(Box.createRigidArea(new Dimension(0, 10)));
        p.add(lblTit);
        p.add(Box.createRigidArea(new Dimension(0, 5)));
        p.add(lblD);
        return p;
    }
}
