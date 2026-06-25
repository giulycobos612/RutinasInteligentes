package modelo;

/**
 * Entidad que almacena los datos de rendimiento del estudiante.
 * Solo usa datos reales registrados en el programa, sin inventar nada.
 */
public class ProgresoAcademico {

    // Minutos acumulados reales (lo que ingresa el sistema, no horas ficticias)
    private int minutosAcumulados;

    // Datos de sesiones (vienen de gestorRutinas)
    private int totalSesiones;
    private int sesionesCompletadas;
    private double porcentajeCumplimiento;

    // Datos de tareas (vienen de gestorMaterias)
    private int totalTareas;
    private int tareasCompletadas;
    private double porcentajeTareas;

    // Estado evaluado por GestorProgreso
    private String estadoRendimiento;

    // Constructor: todo en cero, sin inventar nada
    public ProgresoAcademico() {
        this.minutosAcumulados     = 0;
        this.totalSesiones         = 0;
        this.sesionesCompletadas   = 0;
        this.porcentajeCumplimiento= 0.0;
        this.totalTareas           = 0;
        this.tareasCompletadas     = 0;
        this.porcentajeTareas      = 0.0;
        this.estadoRendimiento     = "Sin evaluar";
    }

    // ─── MÉTODOS DE NEGOCIO ──────────────────────────────

    /**
     * Acumula los minutos reales de la sesión completada.
     * El parámetro viene de sesion.getDuracionMinutos() (o 60 si es 0).
     */
    public void actualizarHoras(int minutosAgregados) {
        if (minutosAgregados > 0) {
            this.minutosAcumulados += minutosAgregados;
        }
    }

    /**
     * Calcula el % de sesiones cumplidas sobre el total real del programa.
     */
    public void calcularPorcentaje(int totalSesiones, int completadas) {
        this.totalSesiones       = totalSesiones;
        this.sesionesCompletadas = completadas;
        if (totalSesiones > 0) {
            this.porcentajeCumplimiento = ((double) completadas / totalSesiones) * 100.0;
        } else {
            this.porcentajeCumplimiento = 0.0;
        }
    }

    /**
     * Calcula el % de tareas completadas sobre el total real del programa.
     */
    public void calcularPorcentajeTareas(int totalTareas, int completadas) {
        this.totalTareas       = totalTareas;
        this.tareasCompletadas = completadas;
        if (totalTareas > 0) {
            this.porcentajeTareas = ((double) completadas / totalTareas) * 100.0;
        } else {
            this.porcentajeTareas = 0.0;
        }
    }

    /**
     * Progreso global: promedio entre sesiones y tareas.
     * Si solo hay uno de los dos, usa ese solo.
     */
    public double calcularProgresoGlobal() {
        boolean haySesiones = totalSesiones > 0;
        boolean hayTareas   = totalTareas   > 0;

        if (!haySesiones && !hayTareas) return 0.0;
        if (!haySesiones)               return porcentajeTareas;
        if (!hayTareas)                 return porcentajeCumplimiento;
        return (porcentajeCumplimiento + porcentajeTareas) / 2.0;
    }

    /**
     * Convierte los minutos acumulados reales a texto legible.
     * Ej: 90 min → "1 h 30 min"
     */
    public String getTiempoFormateado() {
        if (minutosAcumulados <= 0) return "0 min";
        int horas   = minutosAcumulados / 60;
        int minutos = minutosAcumulados % 60;
        if (horas == 0)   return minutos + " min";
        if (minutos == 0) return horas + " h";
        return horas + " h " + minutos + " min";
    }

    /**
     * Reporte limpio con datos reales, alineado para JOptionPane (fuente proporcional).
     * Usa solo = y - para separadores, y texto con saltos de linea claros.
     */
    public String obtenerResumenProgreso() {
        double global = calcularProgresoGlobal();

        // Barra de progreso con 20 bloques ASCII simples
        int bloques = (int) (global / 5.0);
        StringBuilder barra = new StringBuilder("[");
        for (int i = 0; i < 20; i++) {
            barra.append(i < bloques ? "#" : ".");
        }
        barra.append("]");

        // Si no hay datos aún
        if (totalSesiones == 0 && totalTareas == 0) {
            return "==============================\n"
                    + "  PROGRESO ACADEMICO\n"
                    + "==============================\n"
                    + "\n"
                    + "  No hay datos registrados aun.\n"
                    + "  Completa sesiones o tareas\n"
                    + "  para ver tu progreso.\n"
                    + "\n"
                    + "==============================";
        }

        return "==============================\n"
                + "    PROGRESO ACADEMICO\n"
                + "==============================\n"
                + "\n"
                + "  PROGRESO GLOBAL\n"
                + "  " + barra + "\n"
                + "  " + String.format("%.1f", global) + "% completado\n"
                + "\n"
                + "------------------------------\n"
                + "  SESIONES DE ESTUDIO\n"
                + "  Cumplidas : " + sesionesCompletadas + " de " + totalSesiones + "\n"
                + "  Avance    : " + String.format("%.1f", porcentajeCumplimiento) + "%\n"
                + "\n"
                + "------------------------------\n"
                + "  TAREAS\n"
                + "  Completadas : " + tareasCompletadas + " de " + totalTareas + "\n"
                + "  Avance      : " + String.format("%.1f", porcentajeTareas) + "%\n"
                + "\n"
                + "------------------------------\n"
                + "  Tiempo de estudio : " + getTiempoFormateado() + "\n"
                + "\n"
                + "  Estado : " + estadoRendimiento + "\n"
                + "==============================";
    }

    // ─── GETTERS Y SETTERS ───────────────────────────────

    public int getMinutosAcumulados() { return minutosAcumulados; }
    public void setMinutosAcumulados(int m) { this.minutosAcumulados = m; }

    // Compatibilidad con GestorProgreso que llama getHorasAcumuladas()
    public int getHorasAcumuladas() { return minutosAcumulados / 60; }

    public double getPorcentajeCumplimiento() { return porcentajeCumplimiento; }
    public void setPorcentajeCumplimiento(double p) { this.porcentajeCumplimiento = p; }

    public double getPorcentajeTareas() { return porcentajeTareas; }
    public void setPorcentajeTareas(double p) { this.porcentajeTareas = p; }

    public int getTareasCompletadas() { return tareasCompletadas; }
    public void setTareasCompletadas(int t) { this.tareasCompletadas = t; }

    public int getTotalTareas() { return totalTareas; }
    public void setTotalTareas(int t) { this.totalTareas = t; }

    public int getSesionesCompletadas() { return sesionesCompletadas; }
    public void setSesionesCompletadas(int s) { this.sesionesCompletadas = s; }

    public int getTotalSesiones() { return totalSesiones; }
    public void setTotalSesiones(int t) { this.totalSesiones = t; }

    public String getEstadoRendimiento() { return estadoRendimiento; }
    public void setEstadoRendimiento(String e) { this.estadoRendimiento = e; }
}