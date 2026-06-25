package modelo;

public class Tarea {
    private String nombreTarea;
    private String fechaEntrega;
    private String prioridad;
    private Materia materia;
    private boolean completada;

    public Tarea(String nombreTarea, String fechaEntrega, Materia materia) {
        this.nombreTarea = nombreTarea;
        this.fechaEntrega = fechaEntrega;
        this.materia = materia;
        this.completada = false;
    }

    public String getNombreTarea() { return nombreTarea; }
    public void setNombreTarea(String nombreTarea) { this.nombreTarea = nombreTarea; }

    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public Materia getMateria() { return materia; }
    public void setMateria(Materia materia) { this.materia = materia; }

    public boolean isCompletada() { return completada; }
    public void setCompletada(boolean completada) { this.completada = completada; }

    public String mostrarDatos() {
        return "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "TAREA: " + nombreTarea + "\n" +
                "Materia: " + materia.getNombreMateria() + "\n" +
                "Fecha de entrega: " + fechaEntrega + "\n" +
                "Prioridad: " + prioridad + "\n" +
                "Estado: " + (completada ? "COMPLETADA" : "PENDIENTE") + "\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
    }
}