package modelo;

import java.time.Duration;
import java.time.LocalTime;

public class RutinasEstudio {

    private String dia;
    private String horaInicio;
    private String horaFin;
    private int duracionMinutos;
    private boolean esDescanso;
    private boolean cumplida;
    private Tarea tarea;
    private String nombreSesion;

    public RutinasEstudio(String dia, String horaInicio, String horaFin,
                          int duracionMinutos, Tarea tarea) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        setDuracionMinutos(duracionMinutos);
        this.tarea = tarea;
        this.esDescanso = false;
        this.cumplida = false;
        this.nombreSesion = "";
    }

    public RutinasEstudio(String dia, String horaInicio, String horaFin, int duracionMinutos) {
        this(dia, horaInicio, horaFin, duracionMinutos, null);
        this.esDescanso = false;
        this.nombreSesion = "Sesión de estudio";
    }

    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    public String getHoraInicio() { return horaInicio; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }

    public String getHoraFin() { return horaFin; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }

    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) {
        try {
            if (horaInicio != null && horaFin != null && !horaInicio.isEmpty() && !horaFin.isEmpty()) {
                LocalTime inicio = LocalTime.parse(horaInicio);
                LocalTime fin = LocalTime.parse(horaFin);
                Duration duracion = Duration.between(inicio, fin);
                this.duracionMinutos = (int) duracion.toMinutes();
            } else {
                this.duracionMinutos = 0;
            }
        } catch (Exception e) {
            this.duracionMinutos = 0;
        }
    }

    public boolean isEsDescanso() { return esDescanso; }
    public void setEsDescanso(boolean esDescanso) { this.esDescanso = esDescanso; }

    public boolean isCumplida() { return cumplida; }
    public void setCumplida(boolean cumplida) { this.cumplida = cumplida; }

    public Tarea getTarea() { return tarea; }
    public void setTarea(Tarea tarea) { this.tarea = tarea; }

    public String getNombreSesion() { return nombreSesion; }
    public void setNombreSesion(String nombreSesion) { this.nombreSesion = nombreSesion; }

    @Override
    public String toString() {
        if (esDescanso) {
            return dia + " " + horaInicio + "-" + horaFin
                    + " | DESCANSO (" + duracionMinutos + " min)";
        }
        if (tarea == null) {
            return dia + " " + horaInicio + "-" + horaFin
                    + " | " + nombreSesion
                    + " | " + duracionMinutos + " min"
                    + " | Cumplida: " + cumplida;
        }
        return dia + " " + horaInicio + "-" + horaFin
                + " | " + tarea.getMateria().getNombreMateria()
                + " | Tarea: " + tarea.getNombreTarea()
                + " | " + duracionMinutos + " min"
                + " | Cumplida: " + cumplida;
    }
}