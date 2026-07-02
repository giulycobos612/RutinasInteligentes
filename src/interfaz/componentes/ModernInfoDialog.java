package interfaz.componentes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;

public class ModernInfoDialog extends JDialog {
    
    private static final String[] FRASES = {
        "El exito es la suma de pequenos esfuerzos repetidos dia tras dia.",
        "La educacion es el arma mas poderosa que puedes usar para cambiar el mundo.",
        "No te detengas hasta que te sientas orgulloso.",
        "Cree en ti mismo y en lo que eres. Se consciente de que hay algo en tu interior que es mas grande que cualquier obstaculo.",
        "La disciplina es el puente entre las metas y los logros.",
        "Estudia no para saber una cosa mas, sino para saberla mejor.",
        "El secreto del exito es empezar antes de estar listo.",
        "Tu futuro depende de lo que hagas hoy, no manana."
    };

    public ModernInfoDialog(Frame owner, String date, java.util.List<String> eventos) {
        super(owner, "Detalles del Dia", true);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15)) {
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
        mainPanel.setBorder(new EmptyBorder(25, 30, 20, 30));

        // Header
        JLabel titleLabel = new JLabel("Actividades para el " + date, SwingConstants.CENTER);
        titleLabel.setFont(Tema.FONT_SUBTITULO);
        titleLabel.setForeground(Tema.PRIMARIO);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Actividades
        JPanel actsPanel = new JPanel();
        actsPanel.setLayout(new BoxLayout(actsPanel, BoxLayout.Y_AXIS));
        actsPanel.setOpaque(false);
        actsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        for (String ev : eventos) {
            JLabel lbl = new JLabel("● " + ev);
            lbl.setFont(Tema.FONT_REGULAR);
            lbl.setForeground(Tema.TEXTO_PRINCIPAL);
            lbl.setBorder(new EmptyBorder(3, 0, 3, 0));
            actsPanel.add(lbl);
        }
        
        mainPanel.add(actsPanel, BorderLayout.CENTER);

        // Frase motivacional y boton
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 15));
        bottomPanel.setOpaque(false);
        
        String frase = FRASES[new Random().nextInt(FRASES.length)];
        JLabel quoteLabel = new JLabel("<html><div style='text-align: center; font-style: italic;'>\"" + frase + "\"</div></html>", SwingConstants.CENTER);
        quoteLabel.setFont(Tema.FONT_PEQUENA);
        quoteLabel.setForeground(Tema.TEXTO_SECUNDARIO);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);

        ModernButton btnClose = new ModernButton("Aceptar");
        btnClose.setColors(Tema.PRIMARIO, Tema.PRIMARIO_HOVER);
        btnClose.setForeground(Color.WHITE);
        btnClose.setPreferredSize(new Dimension(100, 32));
        btnClose.setFont(Tema.FONT_BOLD);
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);

        bottomPanel.add(quoteLabel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        pack();
        
        // Ajustar ancho minimo si es muy angosto
        if (getWidth() < 350) {
            setSize(350, getHeight());
        }
        
        setLocationRelativeTo(owner);
    }

    public static void showInfoDialog(Frame owner, String date, java.util.List<String> eventos) {
        ModernInfoDialog dialog = new ModernInfoDialog(owner, date, eventos);
        dialog.setVisible(true);
    }
}
