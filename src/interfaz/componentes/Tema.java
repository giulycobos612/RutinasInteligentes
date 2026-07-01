package interfaz.componentes;

import java.awt.Color;
import java.awt.Font;

public class Tema {
    // Fondo y Tarjetas
    public static Color FONDO = new Color(245, 243, 255);
    public static Color TARJETA = new Color(255, 255, 255);
    public static Color TEXTO_PRINCIPAL = new Color(30, 30, 60);
    public static Color TEXTO_SECUNDARIO = new Color(120, 120, 150);
    public static Color BORDE = new Color(226, 232, 240);

    // Gradiente sidebar
    public static Color SIDEBAR_TOP = new Color(99, 102, 241);
    public static Color SIDEBAR_BOTTOM = new Color(168, 85, 247);

    // Acentos vibrantes
    public static Color PRIMARIO = new Color(99, 102, 241);       
    public static Color PRIMARIO_HOVER = new Color(79, 70, 229);
    public static Color SECUNDARIO = new Color(168, 85, 247);     
    public static Color ACENTO = new Color(236, 72, 153);        

    // Colores de estado
    public static Color EXITO = new Color(16, 185, 129);         
    public static Color EXITO_LIGHT = new Color(209, 250, 229);
    public static Color PELIGRO = new Color(239, 68, 68);        
    public static Color PELIGRO_LIGHT = new Color(254, 226, 226);
    public static Color ADVERTENCIA = new Color(245, 158, 11);   
    public static Color ADVERTENCIA_LIGHT = new Color(254, 243, 199);
    public static Color INFO = new Color(59, 130, 246);          
    public static Color INFO_LIGHT = new Color(219, 234, 254);

    public static final Color[] CARD_COLORS = {
        new Color(99, 102, 241),    // Indigo
        new Color(168, 85, 247),    // Púrpura
        new Color(236, 72, 153),    // Rosa
        new Color(245, 158, 11),    // Naranja
        new Color(16, 185, 129),    // Verde
        new Color(59, 130, 246),    // Azul
        new Color(239, 68, 68),     // Rojo
        new Color(20, 184, 166),    // Teal
    };

    public static final Color PRIORIDAD_CRITICA = new Color(239, 68, 68);
    public static final Color PRIORIDAD_ALTA = new Color(245, 158, 11);
    public static final Color PRIORIDAD_MEDIA = new Color(59, 130, 246);
    public static final Color PRIORIDAD_BAJA = new Color(16, 185, 129);

    public static final Font FONT_TITULO = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SUBTITULO = new Font("Segoe UI", Font.BOLD, 17);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font FONT_PEQUENA = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_GRANDE = new Font("Segoe UI", Font.BOLD, 40);

    public static void actualizarTema(boolean oscuro) {
        if (oscuro) {
            // Marino Intenso
            FONDO = new Color(15, 23, 42);           
            TARJETA = new Color(30, 41, 59);         
            TEXTO_PRINCIPAL = new Color(248, 250, 252);
            TEXTO_SECUNDARIO = new Color(148, 163, 184);
            BORDE = new Color(51, 65, 85);
            
            SIDEBAR_TOP = new Color(15, 23, 42);
            SIDEBAR_BOTTOM = new Color(30, 58, 138); // Marino intenso gradient
            
            EXITO_LIGHT = new Color(6, 78, 59);
            PELIGRO_LIGHT = new Color(127, 29, 29);
            ADVERTENCIA_LIGHT = new Color(120, 53, 15);
            INFO_LIGHT = new Color(30, 58, 138);
        } else {
            // Claro original
            FONDO = new Color(245, 243, 255);
            TARJETA = new Color(255, 255, 255);
            TEXTO_PRINCIPAL = new Color(30, 30, 60);
            TEXTO_SECUNDARIO = new Color(120, 120, 150);
            BORDE = new Color(226, 232, 240);
            
            SIDEBAR_TOP = new Color(99, 102, 241);
            SIDEBAR_BOTTOM = new Color(168, 85, 247);
            
            EXITO_LIGHT = new Color(209, 250, 229);
            PELIGRO_LIGHT = new Color(254, 226, 226);
            ADVERTENCIA_LIGHT = new Color(254, 243, 199);
            INFO_LIGHT = new Color(219, 234, 254);
        }
    }

    public static Color getCardColor(int index) {
        return CARD_COLORS[index % CARD_COLORS.length];
    }

    public static Color getPrioridadColor(String prioridad) {
        if (prioridad == null) return PRIORIDAD_BAJA;
        return switch (prioridad.toUpperCase()) {
            case "VENCIDA", "CRÍTICA", "CRITICA" -> PRIORIDAD_CRITICA;
            case "URGENTE" -> PELIGRO;
            case "ALTA" -> PRIORIDAD_ALTA;
            case "MEDIA" -> PRIORIDAD_MEDIA;
            default -> PRIORIDAD_BAJA;
        };
    }

    public static Color withAlpha(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.min(255, Math.max(0, alpha)));
    }
}
