package modelo;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RutinasEstudio implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int duracionMinutos;
    private boolean esDescanso;
    private boolean cumplida;
    private Tarea tarea;
    private String nombreSesion;
    private boolean recordatorioActivado;

    public RutinasEstudio(LocalDate dia, LocalTime horaInicio, LocalTime horaFin, Tarea tarea) {
        setDia(dia);
        setHoraInicio(horaInicio);
        setHoraFin(horaFin);
        calcularDuracion();
        this.tarea = tarea;
        this.esDescanso = false;
        this.cumplida = false;
        this.nombreSesion = "";
        this.recordatorioActivado = false;
    }

    public RutinasEstudio(LocalDate dia, LocalTime horaInicio, LocalTime horaFin) {
        this(dia, horaInicio, horaFin, null);
        this.esDescanso = false;
        this.nombreSesion = "Sesión de estudio";
    }

    public LocalDate getDia() { return dia; }
    public void setDia(LocalDate dia) { 
        if (dia == null) throw new IllegalArgumentException("El día no puede ser nulo.");
        this.dia = dia; 
    }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { 
        if (horaInicio == null) throw new IllegalArgumentException("La hora de inicio no puede ser nula.");
        this.horaInicio = horaInicio;
        calcularDuracion();
    }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { 
        if (horaFin == null) throw new IllegalArgumentException("La hora de fin no puede ser nula.");
        this.horaFin = horaFin;
        calcularDuracion();
    }

    private void calcularDuracion() {
        if (horaInicio != null && horaFin != null) {
            if (horaFin.isBefore(horaInicio)) {
                throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio.");
            }
            this.duracionMinutos = (int) Duration.between(horaInicio, horaFin).toMinutes();
        } else {
            this.duracionMinutos = 0;
        }
    }

    public int getDuracionMinutos() { return duracionMinutos; }

    public boolean isEsDescanso() { return esDescanso; }
    public void setEsDescanso(boolean esDescanso) { this.esDescanso = esDescanso; }

    public boolean isCumplida() { return cumplida; }
    public void setCumplida(boolean cumplida) { this.cumplida = cumplida; }

    public Tarea getTarea() { return tarea; }
    public void setTarea(Tarea tarea) { this.tarea = tarea; }

    public String getNombreSesion() { return nombreSesion; }
    public void setNombreSesion(String nombreSesion) { this.nombreSesion = nombreSesion; }

    public boolean isRecordatorioActivado() { return recordatorioActivado; }
    public void setRecordatorioActivado(boolean recordatorioActivado) { this.recordatorioActivado = recordatorioActivado; }

    public String getDiaFormateado() {
        return dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    public String getHoraInicioFormateada() {
        return horaInicio.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getHoraFinFormateada() {
        return horaFin.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    @Override
    public String toString() {
        return nombreSesion.isEmpty() ? (tarea != null ? tarea.getNombreTarea() : "Sesión") : nombreSesion;
    }
}