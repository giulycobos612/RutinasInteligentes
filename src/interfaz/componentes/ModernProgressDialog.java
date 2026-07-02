package interfaz.componentes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernProgressDialog extends JDialog {
    private int selectedProgress = -1;

    public ModernProgressDialog(Frame owner, String title, String message, int currentProgress) {
        super(owner, title, true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20)) {
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
        mainPanel.setBorder(new EmptyBorder(30, 40, 25, 40));

        JLabel titleLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>", SwingConstants.CENTER);
        titleLabel.setFont(Tema.FONT_REGULAR);
        titleLabel.setForeground(Tema.TEXTO_PRINCIPAL);
        
        // Slider
        JSlider slider = new JSlider(0, 100, currentProgress);
        slider.setUI(new ModernSliderUI(slider));
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setOpaque(false);
        slider.setForeground(Tema.TEXTO_SECUNDARIO);
        slider.setFont(Tema.FONT_PEQUENA);
        
        JLabel lblValue = new JLabel(currentProgress + "%", SwingConstants.CENTER);
        lblValue.setFont(Tema.FONT_SUBTITULO);
        lblValue.setForeground(Tema.PRIMARIO);
        
        slider.addChangeListener(e -> lblValue.setText(slider.getValue() + "%"));
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        centerPanel.add(lblValue, BorderLayout.NORTH);
        centerPanel.add(slider, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        ModernButton btnYes = new ModernButton("Guardar Avance");
        btnYes.setColors(Tema.PRIMARIO, Tema.PRIMARIO.darker());
        btnYes.setForeground(Color.WHITE);
        btnYes.setPreferredSize(new Dimension(140, 36));
        btnYes.setFont(Tema.FONT_BOLD);
        btnYes.addActionListener(e -> {
            selectedProgress = slider.getValue();
            dispose();
        });

        ModernButton btnNo = new ModernButton("Cancelar");
        btnNo.setColors(Tema.TARJETA, Tema.TARJETA.brighter());
        btnNo.setForeground(Tema.TEXTO_PRINCIPAL);
        btnNo.setPreferredSize(new Dimension(110, 36));
        btnNo.setFont(Tema.FONT_BOLD);
        btnNo.addActionListener(e -> {
            selectedProgress = -1;
            dispose();
        });

        buttonPanel.add(btnNo);
        buttonPanel.add(btnYes);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        // Set fixed width if it's too small
        if (getWidth() < 400) {
            setSize(400, getHeight());
        }
        setLocationRelativeTo(owner);
    }

    public static int showProgressDialog(Frame owner, String title, String message, int currentProgress) {
        ModernProgressDialog dialog = new ModernProgressDialog(owner, title, message, currentProgress);
        dialog.setVisible(true);
        return dialog.selectedProgress;
    }
}
