package interfaz.componentes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernConfirmDialog extends JDialog {
    private boolean confirmed = false;

    public ModernConfirmDialog(Frame owner, String message, String title) {
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

        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message.replace("\n", "<br>") + "</div></html>", SwingConstants.CENTER);
        messageLabel.setFont(Tema.FONT_REGULAR);
        messageLabel.setForeground(Tema.TEXTO_PRINCIPAL);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        ModernButton btnYes = new ModernButton("Si, Eliminar");
        btnYes.setColors(Tema.PELIGRO, Tema.PELIGRO.darker());
        btnYes.setForeground(Color.WHITE);
        btnYes.setPreferredSize(new Dimension(130, 36));
        btnYes.setFont(Tema.FONT_BOLD);
        btnYes.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        ModernButton btnNo = new ModernButton("Cancelar");
        btnNo.setColors(Tema.TARJETA, Tema.TARJETA.brighter());
        btnNo.setForeground(Tema.TEXTO_PRINCIPAL);
        btnNo.setPreferredSize(new Dimension(110, 36));
        btnNo.setFont(Tema.FONT_BOLD);
        btnNo.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        buttonPanel.add(btnNo);
        buttonPanel.add(btnYes);

        mainPanel.add(messageLabel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(owner);
    }

    public static boolean showConfirmDialog(Frame owner, String message, String title) {
        ModernConfirmDialog dialog = new ModernConfirmDialog(owner, message, title);
        dialog.setVisible(true);
        return dialog.confirmed;
    }
}
