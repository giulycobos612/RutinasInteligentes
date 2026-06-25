package negocio;

import modelo.ProgresoAcademico;
import modelo.RutinasEstudio;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona el progreso academico del estudiante.
 * Solo trabaja con datos reales: sesiones de gestorRutinas y tareas de gestorMaterias.
 * No inventa ni asume ningun dato.
 */
public class GestorProgreso {

    private ProgresoAcademico progresoActual;
    private List<ProgresoAcademico> historialSemanas;

    public GestorProgreso() {
        this.progresoActual    = new ProgresoAcademico();
        this.historialSemanas  = new ArrayList<>();
    }

    /**
     * Llamado desde marcarCumplida() en MainUsuarios.
     * Solo acumula los minutos reales de la sesion.
     * Los porcentajes se recalculan aparte con recalcularProgreso().
     *
     * @param duracionMinutos minutos reales de la sesion (viene de sesion.getDuracionMinutos())
     * @param dificultadMateria no se usa para puntaje, se deja por firma compatible
     */
    public void registrarSesionCompletada(int duracionMinutos, int dificultadMateria) {
        progresoActual.actualizarHoras(duracionMinutos);
        // El estado se evalua despues de recalcularProgreso(), no aqui,
        // para que use los porcentajes ya actualizados.
    }

    /**
     * Recalcula todos los porcentajes usando los conteos reales del programa.
     * Debe llamarse DESPUES de registrarSesionCompletada() con los datos
     * obtenidos de gestorRutinas y gestorMaterias.
     *
     * @param totalSesiones       total de sesiones en gestorRutinas
     * @param sesionesCompletadas sesiones marcadas como cumplidas
     * @param totalTareas         total de tareas en gestorMaterias
     * @param tareasCompletadas   tareas marcadas como completadas
     */
    public void recalcularProgreso(int totalSesiones, int sesionesCompletadas,
                                   int totalTareas,   int tareasCompletadas) {
        progresoActual.calcularPorcentaje(totalSesiones, sesionesCompletadas);
        progresoActual.calcularPorcentajeTareas(totalTareas, tareasCompletadas);
        // Ahora que los porcentajes estan actualizados, evaluamos el estado
        progresoActual.setEstadoRendimiento(evaluarEstado());
    }

    /**
     * Determina el estado de rendimiento basado en el progreso global real.
     * Usa solo porcentajes calculados de datos reales, sin umbrales de horas inventados.
     */
    public String evaluarEstado() {
        double global = progresoActual.calcularProgresoGlobal();

        if (global == 0.0) {
            return "Sin iniciar";
        } else if (global < 40.0) {
            return "RIESGO: Progreso bajo. Retoma tus rutinas.";
        } else if (global < 70.0) {
            return "Moderado: Vas avanzando, mantén la constancia.";
        } else if (global < 90.0) {
            return "Bueno: Buen ritmo academico.";
        } else {
            return "OPTIMO: Excelente gestion del tiempo!";
        }
    }

    /**
     * Genera el reporte final. Asegura que el estado este actualizado antes de devolver.
     */
    public ProgresoAcademico generarReporteAcademico() {
        progresoActual.setEstadoRendimiento(evaluarEstado());
        return progresoActual;
    }

    // ─── GETTERS Y SETTERS ───────────────────────────────

    public ProgresoAcademico getProgresoActual() { return progresoActual; }
    public void setProgresoActual(ProgresoAcademico p) { this.progresoActual = p; }

    public List<ProgresoAcademico> getHistorialSemanas() { return historialSemanas; }
    public void setHistorialSemanas(List<ProgresoAcademico> h) { this.historialSemanas = h; }
}