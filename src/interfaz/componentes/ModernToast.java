package interfaz.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ModernToast extends JDialog {

    public enum Type { SUCCESS, ERROR, WARNING, INFO }

    public static void show(JFrame parent, String message, Type type) {
        show(parent, message, type, true);
    }

    public static void show(JFrame parent, String message, Type type, boolean autoClose) {
        ModernToast toast = new ModernToast(parent, message, type);
        toast.setVisible(true);

        if (autoClose) {
            new Thread(() -> {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                // Fade out
                for (float i = 1.0f; i > 0.0f; i -= 0.1f) {
                    try {
                        toast.setOpacity(i);
                        Thread.sleep(30);
                    } catch (Exception ignored) {}
                }
                toast.dispose();
            }).start();
        }
    }

    private ModernToast(JFrame parent, String message, Type type) {
        super(parent, false);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel panel = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel iconLabel = new JLabel();
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        switch (type) {
            case SUCCESS -> {
                panel.setBackground(Tema.EXITO);
                iconLabel.setText("✅");
            }
            case ERROR -> {
                panel.setBackground(Tema.PELIGRO);
                iconLabel.setText("❌");
            }
            case WARNING -> {
                panel.setBackground(Tema.ADVERTENCIA);
                iconLabel.setText("⚠️");
            }
            case INFO -> {
                panel.setBackground(Tema.PRIMARIO);
                iconLabel.setText("ℹ️");
            }
        }

        JLabel textLabel = new JLabel(message);
        textLabel.setFont(Tema.FONT_SUBTITULO);
        textLabel.setForeground(Color.WHITE);

        JLabel closeLabel = new JLabel("✕");
        closeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeLabel.setForeground(new Color(255, 255, 255, 150));
        closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { closeLabel.setForeground(Color.WHITE); }
            public void mouseExited(java.awt.event.MouseEvent e) { closeLabel.setForeground(new Color(255, 255, 255, 150)); }
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Fade out before dispose
                new Thread(() -> {
                    for (float i = 1.0f; i > 0.0f; i -= 0.1f) {
                        try {
                            setOpacity(i);
                            Thread.sleep(20);
                        } catch (Exception ignored) {}
                    }
                    dispose();
                }).start();
            }
        });

        panel.add(iconLabel, BorderLayout.WEST);
        panel.add(textLabel, BorderLayout.CENTER);
        panel.add(closeLabel, BorderLayout.EAST);

        setContentPane(panel);
        pack();
        
        setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        if (parent != null) {
            int x = parent.getX() + (parent.getWidth() - getWidth()) / 2;
            int y = parent.getY() + parent.getHeight() - getHeight() - 50; // Bottom center
            setLocation(x, y);
        } else {
            setLocationRelativeTo(null);
        }
    }
}
