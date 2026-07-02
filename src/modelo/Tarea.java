package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Tarea implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombreTarea;
    private LocalDate fechaEntrega;
    private String prioridad;
    private Materia materia;
    private boolean completada;
    private boolean recordatorioActivado;
    private int porcentajeAvance;

    public Tarea(String nombreTarea, LocalDate fechaEntrega, Materia materia) {
        setNombreTarea(nombreTarea);
        setFechaEntrega(fechaEntrega);
        setMateria(materia);
        this.completada = false;
        this.recordatorioActivado = false;
        this.porcentajeAvance = 0;
        calcularPrioridadAut();
    }

    public String getNombreTarea() { return nombreTarea; }
    public void setNombreTarea(String nombreTarea) { 
        if (nombreTarea == null || nombreTarea.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la tarea no puede estar vacío.");
        }
        this.nombreTarea = nombreTarea.trim(); 
    }

    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { 
        if (fechaEntrega == null) {
            throw new IllegalArgumentException("La fecha de entrega no puede ser nula.");
        }
        this.fechaEntrega = fechaEntrega; 
        calcularPrioridadAut();
    }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { 
        if (materia == null) {
            throw new IllegalArgumentException("La materia no puede ser nula.");
        }
        this.materia = materia; 
    }

    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; }

    public boolean isRecordatorioActivado() { return recordatorioActivado; }
    public void setRecordatorioActivado(boolean recordatorioActivado) { this.recordatorioActivado = recordatorioActivado; }

    public int getPorcentajeAvance() { return porcentajeAvance; }
    public void setPorcentajeAvance(int porcentajeAvance) { 
        this.porcentajeAvance = Math.max(0, Math.min(100, porcentajeAvance)); 
    }

    private void calcularPrioridadAut() {
        if (fechaEntrega == null) return;
        long diasRestantes = LocalDate.now().until(fechaEntrega).getDays();
        if (diasRestantes < 0) {
            this.prioridad = "Vencida";
        } else if (diasRestantes <= 2) {
            this.prioridad = "Alta";
        } else if (diasRestantes <= 5) {
            this.prioridad = "Media";
        } else {
            this.prioridad = "Baja";
        }
    }

    public String getFechaFormateada() {
        return fechaEntrega.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    @Override
    public String toString() {
        return nombreTarea;
    }
}