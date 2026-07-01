package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ProgresoAcademico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int tareasCompletadas;
    private int tareasPendientes;
    private int tareasVencidas;

    private int horasEstudiadas;
    private int minutosEstudiados;
    private int sesionesCompletadas;
    private int sesionesTotales;

    // Nuevas metricas
    private int tareasUrgentes; // vencen en 3 dias o menos
    private int diasRacha;     // dias consecutivos con sesiones cumplidas
    private double promedioCalificaciones;
    private int materiasEnRiesgo; // calificacion < nota minima

    public void recalcularProgreso(List<Tarea> tareas, List<RutinasEstudio> rutinas) {
        tareasCompletadas = 0;
        tareasPendientes = 0;
        tareasVencidas = 0;
        tareasUrgentes = 0;

        LocalDate hoy = LocalDate.now();

        if (tareas != null) {
            for (Tarea t : tareas) {
                if (t.isCompletada()) {
                    tareasCompletadas++;
                } else {
                    long dias = ChronoUnit.DAYS.between(hoy, t.getFechaEntrega());
                    if (dias < 0) {
                        tareasVencidas++;
                    } else {
                        tareasPendientes++;
                        if (dias <= 3) {
                            tareasUrgentes++;
                        }
                    }
                }
            }
        }

        minutosEstudiados = 0;
        sesionesCompletadas = 0;
        sesionesTotales = 0;

        if (rutinas != null) {
            sesionesTotales = rutinas.size();
            for (RutinasEstudio r : rutinas) {
                if (r.isCumplida()) {
                    sesionesCompletadas++;
                    minutosEstudiados += r.getDuracionMinutos();
                }
            }
        }
        horasEstudiadas = minutosEstudiados / 60;
    }

    public void recalcularMetricasExtendidas(List<Materia> materias, List<RutinasEstudio> rutinas) {
        // Promedio de calificaciones
        promedioCalificaciones = 0;
        materiasEnRiesgo = 0;
        if (materias != null && !materias.isEmpty()) {
            double suma = 0;
            for (Materia m : materias) {
                suma += m.getCalificacionActual();
                if (m.getCalificacionActual() < m.getNotaMinimaPersonal()) {
                    materiasEnRiesgo++;
                }
            }
            promedioCalificaciones = suma / materias.size();
        }

        // Racha de dias
        diasRacha = calcularRacha(rutinas);
    }

    private int calcularRacha(List<RutinasEstudio> rutinas) {
        if (rutinas == null || rutinas.isEmpty()) return 0;

        LocalDate dia = LocalDate.now();
        int racha = 0;

        for (int i = 0; i < 365; i++) {
            LocalDate diaCheck = dia.minusDays(i);
            boolean tieneSesion = false;
            boolean cumplida = false;

            for (RutinasEstudio r : rutinas) {
                if (r.getDia().equals(diaCheck)) {
                    tieneSesion = true;
                    if (r.isCumplida()) cumplida = true;
                }
            }

            if (tieneSesion) {
                if (cumplida) {
                    racha++;
                } else {
                    break;
                }
            } else if (i > 0) {
                // Si no habia sesion ese dia, continuamos (no rompe racha)
                continue;
            }
        }
        return racha;
    }

    // Getters existentes
    public int getTareasCompletadas() { return tareasCompletadas; }
    public int getTareasPendientes() { return tareasPendientes; }
    public int getTareasVencidas() { return tareasVencidas; }

    public int getTareasTotales() {
        return tareasCompletadas + tareasPendientes + tareasVencidas;
    }

    public double getPorcentajeTareasCompletadas() {
        if (getTareasTotales() == 0) return 0.0;
        return (tareasCompletadas * 100.0) / getTareasTotales();
    }

    public int getHorasEstudiadas() { return horasEstudiadas; }
    public int getMinutosEstudiados() { return minutosEstudiados; }
    public int getSesionesCompletadas() { return sesionesCompletadas; }
    public int getSesionesTotales() { return sesionesTotales; }

    public double getPorcentajeSesionesCumplidas() {
        if (sesionesTotales == 0) return 0.0;
        return (sesionesCompletadas * 100.0) / sesionesTotales;
    }

    // Nuevos getters
    public int getTareasUrgentes() { return tareasUrgentes; }
    public int getDiasRacha() { return diasRacha; }
    public double getPromedioCalificaciones() { return promedioCalificaciones; }
    public int getMateriasEnRiesgo() { return materiasEnRiesgo; }

    public String getMensajeMotivacional() {
        double pct = getPorcentajeTareasCompletadas();
        if (getTareasTotales() == 0) return "Registra tus materias y tareas para comenzar!";
        if (tareasVencidas > 0) return "Tienes " + tareasVencidas + " tarea(s) vencida(s). Ponte al dia!";
        if (tareasUrgentes > 0) return "Atencion: " + tareasUrgentes + " tarea(s) vencen en menos de 3 dias.";
        if (pct >= 80) return "Excelente! Llevas un gran ritmo, sigue asi!";
        if (pct >= 50) return "Buen progreso! Ya vas por la mitad, no aflojes.";
        if (pct >= 25) return "Vas avanzando. Cada tarea completada cuenta!";
        return "Es hora de ponerse al dia con las tareas pendientes.";
    }
}
