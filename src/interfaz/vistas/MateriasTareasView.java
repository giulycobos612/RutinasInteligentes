package interfaz.vistas;

import interfaz.componentes.*;
import modelo.Materia;
import modelo.Tarea;
import negocio.GestorMateriasTareas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MateriasTareasView extends JPanel {
    private GestorMateriasTareas gestor;
    private MainFrame mainFrame;
    private JPanel mainContent;
    private CardLayout contentCard;
    private int colorIndex = 0;
    private boolean mostrarCompletadas = false;

    public MateriasTareasView(GestorMateriasTareas gestor, MainFrame mainFrame) {
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
        colorIndex = 0;

        // PANEL PRINCIPAL: lista de materias y tareas
        JPanel listaView = new JPanel(new BorderLayout(0, 15));
        listaView.setOpaque(false);
        listaView.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("Materias y Tareas");
        lblTitulo.setFont(Tema.FONT_TITULO);
        lblTitulo.setForeground(Tema.TEXTO_PRINCIPAL);
        JLabel lblDesc = new JLabel("Gestiona tus materias y las tareas de cada una");
        lblDesc.setFont(Tema.FONT_REGULAR);
        lblDesc.setForeground(Tema.TEXTO_SECUNDARIO);
        titlePanel.add(lblTitulo);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 3)));
        titlePanel.add(lblDesc);
        header.add(titlePanel, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        ModernButton btnAddMateria = new ModernButton("+ Nueva Materia");
        btnAddMateria.addActionListener(e -> mostrarFormMateria(null));

        ModernButton btnAddTarea = new ModernButton("+ Nueva Tarea");
        btnAddTarea.setColors(Tema.SECUNDARIO, Tema.SECUNDARIO.darker());
        btnAddTarea.addActionListener(e -> {
            if (gestor.getListaMaterias().isEmpty()) {
                ModernToast.show(mainFrame, "Primero crea una materia.", ModernToast.Type.WARNING);
            } else {
                mostrarFormTarea(null);
            }
        });

        actions.add(btnAddMateria);
        actions.add(btnAddTarea);
        header.add(actions, BorderLayout.EAST);
        
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        togglePanel.setOpaque(false);
        togglePanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        ModernButton btnPendientes = new ModernButton("Pendientes");
        btnPendientes.setColors(mostrarCompletadas ? Tema.TARJETA : Tema.PRIMARIO, mostrarCompletadas ? Tema.BORDE : Tema.PRIMARIO_HOVER);
        btnPendientes.setForeground(mostrarCompletadas ? Tema.TEXTO_SECUNDARIO : java.awt.Color.WHITE);
        btnPendientes.addActionListener(e -> {
            mostrarCompletadas = false;
            actualizarView();
        });

        ModernButton btnCompletadas = new ModernButton("Completadas");
        btnCompletadas.setColors(!mostrarCompletadas ? Tema.TARJETA : Tema.PRIMARIO, !mostrarCompletadas ? Tema.BORDE : Tema.PRIMARIO_HOVER);
        btnCompletadas.setForeground(!mostrarCompletadas ? Tema.TEXTO_SECUNDARIO : java.awt.Color.WHITE);
        btnCompletadas.addActionListener(e -> {
            mostrarCompletadas = true;
            actualizarView();
        });
        
        togglePanel.add(btnPendientes);
        togglePanel.add(btnCompletadas);

        JPanel northContainer = new JPanel(new BorderLayout());
        northContainer.setOpaque(false);
        northContainer.add(header, BorderLayout.NORTH);
        northContainer.add(togglePanel, BorderLayout.SOUTH);
        
        listaView.add(northContainer, BorderLayout.NORTH);

        // Lista de materias con sus tareas
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setOpaque(false);

        if (gestor.getListaMaterias().isEmpty()) {
            JPanel emptyState = crearEstadoVacio("No tienes materias registradas", "Crea tu primera materia para empezar a organizar tus tareas.");
            listaPanel.add(emptyState);
        } else {
            for (Materia m : gestor.getListaMaterias()) {
                listaPanel.add(crearMateriaCard(m));
                listaPanel.add(Box.createRigidArea(new Dimension(0, 15)));
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

    // ===================== TARJETA DE MATERIA =====================
    private ModernCard crearMateriaCard(Materia m) {
        Color cardColor = Tema.getCardColor(colorIndex++);

        ModernCard card = new ModernCard(15, true, cardColor);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(new EmptyBorder(18, 20, 15, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // Header de materia
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        namePanel.setOpaque(false);

        // Badge de dificultad
        JLabel badge = crearBadge(m.getNivelDificultad().toUpperCase(), cardColor);

        JLabel name = new JLabel(m.getNombreMateria());
        name.setFont(Tema.FONT_SUBTITULO);
        name.setForeground(Tema.TEXTO_PRINCIPAL);

        namePanel.add(name);
        namePanel.add(badge);

        // Botones
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        btns.setOpaque(false);

        ModernButton btnEdit = new ModernButton("Editar");
        btnEdit.setColors(Tema.INFO, Tema.INFO.darker());
        btnEdit.setFont(Tema.FONT_PEQUENA);
        btnEdit.setPreferredSize(new Dimension(70, 28));
        btnEdit.addActionListener(e -> mostrarFormMateria(m));

        ModernButton btnDel = new ModernButton("Eliminar");
        btnDel.setColors(Tema.PELIGRO_LIGHT, Tema.PELIGRO);
        btnDel.setForeground(Tema.PELIGRO);
        btnDel.setPreferredSize(new Dimension(80, 28));
        btnDel.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(mainFrame, "Eliminar '" + m.getNombreMateria() + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                try {
                    gestor.eliminarMateria(m);
                    actualizarView();
                    ModernToast.show(mainFrame, "Materia eliminada", ModernToast.Type.SUCCESS);
                } catch (IllegalStateException ex) {
                    ModernToast.show(mainFrame, ex.getMessage(), ModernToast.Type.ERROR);
                }
            }
        });

        btns.add(btnEdit);
        btns.add(btnDel);

        top.add(namePanel, BorderLayout.WEST);
        top.add(btns, BorderLayout.EAST);
        card.add(top, BorderLayout.NORTH);

        // Tareas de esta materia
        JPanel tareasPanel = new JPanel();
        tareasPanel.setLayout(new BoxLayout(tareasPanel, BoxLayout.Y_AXIS));
        tareasPanel.setOpaque(false);

        boolean hasTareas = false;
        for (Tarea t : gestor.getListaTareas()) {
            if (t.getMateria().equals(m) && t.isCompletada() == mostrarCompletadas) {
                hasTareas = true;
                tareasPanel.add(crearTareaItem(t, cardColor));
                tareasPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        if (!hasTareas) {
            JLabel empty = new JLabel("    Sin tareas asignadas");
            empty.setForeground(Tema.TEXTO_SECUNDARIO);
            empty.setFont(Tema.FONT_PEQUENA);
            tareasPanel.add(empty);
        }

        card.add(tareasPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel crearTareaItem(Tarea t, Color accent) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(5, 5, 5, 5));

        JCheckBox chk = new JCheckBox(t.getNombreTarea());
        chk.setOpaque(false);
        chk.setFont(Tema.FONT_REGULAR);
        chk.setSelected(t.isCompletada());
        chk.setForeground(t.isCompletada() ? Tema.TEXTO_SECUNDARIO : Tema.TEXTO_PRINCIPAL);
        chk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chk.addActionListener(e -> {
            gestor.marcarTareaCompletada(t, chk.isSelected());
            if (chk.isSelected()) {
                t.setPorcentajeAvance(100);
                String msg = gestor.obtenerMensajeAvance(t);
                ModernToast.show(mainFrame, msg, ModernToast.Type.SUCCESS, false);
            } else {
                ModernToast.show(mainFrame, "Tarea restaurada a Pendientes", ModernToast.Type.INFO);
            }
            actualizarView();
        });

        JSlider slider = new JSlider(0, 100, t.getPorcentajeAvance());
        slider.setUI(new ModernSliderUI(slider));
        slider.setOpaque(false);
        slider.setPreferredSize(new Dimension(150, 25));
        slider.setMaximumSize(new Dimension(150, 25));
        JLabel lblProgress = new JLabel(t.getPorcentajeAvance() + "%");
        lblProgress.setFont(Tema.FONT_PEQUENA.deriveFont(java.awt.Font.BOLD));
        lblProgress.setForeground(Tema.PRIMARIO);
        
        slider.addChangeListener(e -> {
            lblProgress.setText(slider.getValue() + "%");
            if (!slider.getValueIsAdjusting()) {
                t.setPorcentajeAvance(slider.getValue());
                gestor.guardarCambios();
            }
        });

        JPanel leftBox = new JPanel();
        leftBox.setLayout(new BoxLayout(leftBox, BoxLayout.Y_AXIS));
        leftBox.setOpaque(false);
        leftBox.add(chk);
        
        JPanel sliderBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        sliderBox.setOpaque(false);
        sliderBox.add(slider);
        sliderBox.add(lblProgress);
        leftBox.add(sliderBox);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        // Badge de prioridad
        JLabel prioLabel = crearBadge(t.getPrioridad(), Tema.getPrioridadColor(t.getPrioridad()));

        JLabel dateLabel = new JLabel(t.getFechaFormateada());
        dateLabel.setFont(Tema.FONT_PEQUENA);
        dateLabel.setForeground(Tema.TEXTO_SECUNDARIO);

        ModernButton btnDel = new ModernButton("Eliminar");
        btnDel.setColors(Tema.PELIGRO, Tema.PELIGRO.darker());
        btnDel.setForeground(java.awt.Color.WHITE);
        btnDel.setFont(Tema.FONT_PEQUENA);
        btnDel.setPreferredSize(new Dimension(85, 28));
        btnDel.addActionListener(e -> {
            int r = JOptionPane.showConfirmDialog(mainFrame, "¿Seguro deseas eliminar esta tarea?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (r == JOptionPane.YES_OPTION) {
                gestor.eliminarTarea(t);
                actualizarView();
            }
        });

        right.add(dateLabel);
        right.add(prioLabel);
        right.add(btnDel);

        p.add(leftBox, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    // ===================== FORM INLINE: MATERIA =====================
    private void mostrarFormMateria(Materia editando) {
        JPanel formView = new JPanel(new BorderLayout());
        formView.setOpaque(false);
        formView.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setOpaque(false);
        ModernButton btnBack = new ModernButton("< Volver");
        btnBack.setColors(Tema.TARJETA, Tema.FONDO);
        btnBack.setForeground(Tema.PRIMARIO);
        btnBack.addActionListener(e -> {
            actualizarView();
        });
        header.add(btnBack);
        formView.add(header, BorderLayout.NORTH);

        // Formulario centrado
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        ModernCard card = new ModernCard(20, true, Tema.PRIMARIO);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setPreferredSize(new Dimension(420, 520));
        card.setMaximumSize(new Dimension(420, 520));

        JLabel lblTitle = new JLabel(editando == null ? "Nueva Materia" : "Editar Materia");
        lblTitle.setFont(Tema.FONT_TITULO);
        lblTitle.setForeground(Tema.TEXTO_PRINCIPAL);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        ModernTextField txtNombre = new ModernTextField("Nombre de la materia");
        txtNombre.setMaximumSize(new Dimension(340, 42));

        String[] diffs = {"bajo", "medio", "alto"};
        JComboBox<String> cbDiff = new JComboBox<>(diffs);
        cbDiff.setFont(Tema.FONT_REGULAR);
        cbDiff.setMaximumSize(new Dimension(340, 42));

        ModernTextField txtCal = new ModernTextField("Calificacion actual (0-10)");
        txtCal.setMaximumSize(new Dimension(340, 42));

        ModernTextField txtMin = new ModernTextField("Nota minima deseada (1-10)");
        txtMin.setMaximumSize(new Dimension(340, 42));

        if (editando != null) {
            txtNombre.setText(editando.getNombreMateria());
            cbDiff.setSelectedItem(editando.getNivelDificultad());
            txtCal.setText(String.valueOf(editando.getCalificacionActual()));
            txtMin.setText(String.valueOf(editando.getNotaMinimaPersonal()));
        }

        ModernButton btnGuardar = new ModernButton(editando == null ? "Crear Materia" : "Guardar Cambios");
        btnGuardar.setColors(Tema.SECUNDARIO, Tema.SECUNDARIO.darker());
        btnGuardar.setMaximumSize(new Dimension(340, 42));
        btnGuardar.addActionListener(e -> {
            try {
                String n = txtNombre.getText();
                String diff = (String) cbDiff.getSelectedItem();
                double cal = Double.parseDouble(txtCal.getText());
                double min = Double.parseDouble(txtMin.getText());

                if (editando != null) {
                    editando.setNombreMateria(n);
                    editando.setNivelDificultad(diff);
                    editando.setCalificacionActual(cal);
                    editando.setNotaMinimaPersonal(min);
                    ModernToast.show(mainFrame, "Materia actualizada!", ModernToast.Type.SUCCESS);
                } else {
                    String msg = gestor.registrarMateria(n, diff, cal, min);
                    ModernToast.show(mainFrame, msg, ModernToast.Type.SUCCESS);
                }
                actualizarView();
            } catch (NumberFormatException ex) {
                ModernToast.show(mainFrame, "Los campos de calificacion deben ser numeros.", ModernToast.Type.ERROR);
            } catch (Exception ex) {
                ModernToast.show(mainFrame, ex.getMessage(), ModernToast.Type.ERROR);
            }
        });

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(crearLabel("Nombre"));
        card.add(txtNombre);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(crearLabel("Dificultad"));
        card.add(cbDiff);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(crearLabel("Calificacion Actual"));
        card.add(txtCal);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(crearLabel("Nota Minima Deseada"));
        card.add(txtMin);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(btnGuardar);

        centerWrapper.add(card);
        formView.add(centerWrapper, BorderLayout.CENTER);

        mainContent.add(formView, "FORM_MATERIA");
        contentCard.show(mainContent, "FORM_MATERIA");
    }

    // ===================== FORM INLINE: TAREA =====================
    private void mostrarFormTarea(Tarea editando) {
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

        ModernCard card = new ModernCard(20, true, Tema.SECUNDARIO);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 40, 30, 40));
        card.setPreferredSize(new Dimension(450, 650));
        card.setMaximumSize(new Dimension(450, 650));

        JLabel lblTitle = new JLabel(editando == null ? "Nueva Tarea" : "Editar Tarea");
        lblTitle.setFont(Tema.FONT_TITULO);
        lblTitle.setForeground(Tema.TEXTO_PRINCIPAL);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        ModernTextField txtNombre = new ModernTextField("Nombre de la tarea");
        txtNombre.setMaximumSize(new Dimension(370, 42));

        JComboBox<Materia> cbMat = new JComboBox<>(gestor.getListaMaterias().toArray(new Materia[0]));
        cbMat.setFont(Tema.FONT_REGULAR);
        cbMat.setMaximumSize(new Dimension(370, 42));

        // DatePicker
        final LocalDate[] fechaSeleccionada = {editando != null ? editando.getFechaEntrega() : null};
        JLabel lblFechaSelec = new JLabel(fechaSeleccionada[0] != null ? "Fecha: " + fechaSeleccionada[0].format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Selecciona una fecha:");
        lblFechaSelec.setFont(Tema.FONT_BOLD);
        lblFechaSelec.setForeground(Tema.PRIMARIO);
        lblFechaSelec.setAlignmentX(Component.LEFT_ALIGNMENT);

        DatePickerPanel datePicker = new DatePickerPanel(date -> {
            fechaSeleccionada[0] = date;
            lblFechaSelec.setText("Fecha: " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        });
        datePicker.setMaximumSize(new Dimension(370, 250));
        datePicker.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (fechaSeleccionada[0] != null) {
            datePicker.setSelectedDate(fechaSeleccionada[0]);
        }

        JCheckBox chkAlerts = new JCheckBox("Activar recordatorios inteligentes");
        chkAlerts.setOpaque(false);
        chkAlerts.setFont(Tema.FONT_REGULAR);
        chkAlerts.setForeground(Tema.TEXTO_PRINCIPAL);

        if (editando != null) {
            txtNombre.setText(editando.getNombreTarea());
            cbMat.setSelectedItem(editando.getMateria());
            chkAlerts.setSelected(editando.isRecordatorioActivado());
        }

        ModernButton btnGuardar = new ModernButton(editando == null ? "Crear Tarea" : "Guardar Cambios");
        btnGuardar.setColors(Tema.ACENTO, Tema.ACENTO.darker());
        btnGuardar.setMaximumSize(new Dimension(370, 42));
        btnGuardar.addActionListener(e -> {
            try {
                if (fechaSeleccionada[0] == null) {
                    ModernToast.show(mainFrame, "Selecciona una fecha en el calendario.", ModernToast.Type.WARNING);
                    return;
                }

                String n = txtNombre.getText();
                Materia mat = (Materia) cbMat.getSelectedItem();
                LocalDate date = fechaSeleccionada[0];

                if (editando != null) {
                    editando.setNombreTarea(n);
                    editando.setFechaEntrega(date);
                    editando.setMateria(mat);
                    editando.setRecordatorioActivado(chkAlerts.isSelected());
                    ModernToast.show(mainFrame, "Tarea actualizada!", ModernToast.Type.SUCCESS);
                } else {
                    String msg = gestor.registrarTarea(n, date, mat);
                    Tarea ultima = gestor.getListaTareas().get(gestor.getListaTareas().size() - 1);
                    ultima.setRecordatorioActivado(chkAlerts.isSelected());
                    ModernToast.show(mainFrame, msg, ModernToast.Type.SUCCESS);
                }
                actualizarView();
            } catch (Exception ex) {
                ModernToast.show(mainFrame, ex.getMessage(), ModernToast.Type.ERROR);
            }
        });

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(crearLabel("Nombre de la tarea"));
        card.add(txtNombre);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(crearLabel("Materia"));
        card.add(cbMat);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblFechaSelec);
        card.add(datePicker);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(chkAlerts);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(btnGuardar);

        centerWrapper.add(card);
        formView.add(centerWrapper, BorderLayout.CENTER);

        mainContent.add(formView, "FORM_TAREA");
        contentCard.show(mainContent, "FORM_TAREA");
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

    private JPanel crearEstadoVacio(String titulo, String desc) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(60, 0, 0, 0));

        JLabel lblIcon = new JLabel("\u2605", SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        lblIcon.setForeground(Tema.withAlpha(Tema.PRIMARIO, 100));
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(Tema.FONT_SUBTITULO);
        lblTit.setForeground(Tema.TEXTO_PRINCIPAL);
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblD = new JLabel(desc);
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
