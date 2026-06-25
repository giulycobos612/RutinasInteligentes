package negocio;

import modelo.Materia;
import modelo.Tarea;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class GestorMateriasTareas {
    private List<Materia> listaMaterias;
    private List<Tarea> listaTareas;

    public GestorMateriasTareas() {
        this.listaMaterias = new ArrayList<>();
        this.listaTareas = new ArrayList<>();
    }

    public String registrarMateria(String nombreMateria, String nivelDificultad,
                                   double calificacionActual, double notaMinimaPersonal) {
        if (notaMinimaPersonal <= 0) {
            return "Error: La nota mínima personal debe ser mayor a 0.";
        }

        Materia materia = new Materia(nombreMateria, nivelDificultad,
                calificacionActual, notaMinimaPersonal);

        double horas = calcularHoras(nivelDificultad, calificacionActual, notaMinimaPersonal);
        materia.setHorasRecomendadas(horas);

        listaMaterias.add(materia);

        return generarMensajeRendimiento(calificacionActual, notaMinimaPersonal);
    }

    public String registrarTarea(String nombreTarea, String fechaEntrega, String nombreMateria) {
        Materia materiaEncontrada = buscarMateria(nombreMateria);

        if (materiaEncontrada == null) {
            return "Error: La materia ingresada no existe en el sistema.";
        }

        LocalDate hoy = LocalDate.now();
        LocalDate fecha = LocalDate.parse(fechaEntrega, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        if (fecha.isBefore(hoy)) {
            return "Error: No se puede registrar una tarea con fecha de entrega ya vencida.";
        }

        Tarea tarea = new Tarea(nombreTarea, fechaEntrega, materiaEncontrada);

        String prioridad = calcularPrioridad(fechaEntrega, materiaEncontrada.getNivelDificultad(),
                materiaEncontrada.getCalificacionActual(), materiaEncontrada.getNotaMinimaPersonal());
        tarea.setPrioridad(prioridad);

        listaTareas.add(tarea);

        return "Tarea registrada correctamente. Prioridad asignada: " + prioridad;
    }

    public String marcarTareaCompletada(int indice) {
        if (indice < 0 || indice >= listaTareas.size()) {
            return "Error: Número de tarea no válido.";
        }

        Tarea t = listaTareas.get(indice);

        if (t.isCompletada()) {
            return "La tarea '" + t.getNombreTarea() + "' ya estaba marcada como completada.";
        }

        t.setCompletada(true);
        return "Tarea '" + t.getNombreTarea() + "' de la materia '" +
                t.getMateria().getNombreMateria() + "' marcada como completada.";
    }

    public String eliminarMateria(int indice) {
        if (indice < 0 || indice >= listaMaterias.size()) {
            return "Error: Número de materia no válido.";
        }

        Materia materiaAEliminar = listaMaterias.get(indice);

        listaTareas.removeIf(t -> t.getMateria().getNombreMateria()
                .equalsIgnoreCase(materiaAEliminar.getNombreMateria()));

        listaMaterias.remove(indice);

        return "Materia '" + materiaAEliminar.getNombreMateria() +
                "' y sus tareas asociadas eliminadas correctamente.";
    }

    public String eliminarTarea(int indice) {
        if (indice < 0 || indice >= listaTareas.size()) {
            return "Error: Número de tarea no válido.";
        }

        Tarea tareaAEliminar = listaTareas.get(indice);
        listaTareas.remove(indice);

        return "Tarea '" + tareaAEliminar.getNombreTarea() + "' eliminada correctamente.";
    }

    private Materia buscarMateria(String nombreMateria) {
        for (Materia m : listaMaterias) {
            if (m.getNombreMateria().equalsIgnoreCase(nombreMateria)) {
                return m;
            }
        }
        return null;
    }

    private double calcularHoras(String nivelDificultad, double calificacionActual,
                                 double notaMinimaPersonal) {
        double horasBase;

        if (nivelDificultad.equalsIgnoreCase("alto")) {
            horasBase = 5;
        } else if (nivelDificultad.equalsIgnoreCase("medio")) {
            horasBase = 3;
        } else {
            horasBase = 2;
        }

        if (calificacionActual < notaMinimaPersonal) {
            horasBase += 2;
        } else if (calificacionActual - notaMinimaPersonal < 1) {
            horasBase += 1;
        }

        return horasBase;
    }

    private String generarMensajeRendimiento(double calificacionActual, double notaMinimaPersonal) {
        if (calificacionActual < notaMinimaPersonal) {
            return "Materia registrada. Estás por debajo de tu nota mínima, se recomienda priorizar esta materia.";
        } else if (calificacionActual - notaMinimaPersonal < 1) {
            return "Materia registrada. Advertencia: estás cerca de tu nota mínima.";
        } else {
            return "Materia registrada. ¡Vas bien, mantén el ritmo!";
        }
    }

    private String calcularPrioridad(String fechaEntrega, String nivelDificultad,
                                     double calificacionActual, double notaMinimaPersonal) {
        LocalDate hoy = LocalDate.now();
        LocalDate fecha = LocalDate.parse(fechaEntrega, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        long diasRestantes = ChronoUnit.DAYS.between(hoy, fecha);

        if (diasRestantes <= 2 && calificacionActual < notaMinimaPersonal) {
            return "CRÍTICA";
        } else if (diasRestantes <= 5 || nivelDificultad.equalsIgnoreCase("alto")) {
            return "ALTA";
        } else if (diasRestantes <= 10 && nivelDificultad.equalsIgnoreCase("medio")) {
            return "MEDIA";
        } else {
            return "BAJA";
        }
    }

    public List<Materia> getListaMaterias() { return listaMaterias; }
    public List<Tarea> getListaTareas() { return listaTareas; }

    public String listarMaterias() {
        if (listaMaterias.isEmpty()) {
            return "No hay materias registradas aún.";
        }
        String texto = "--- LISTA DE MATERIAS ---\n\n";
        for (Materia m : listaMaterias) {
            texto += m.mostrarDatos() + "\n\n";
        }
        return texto;
    }

    public String listarTareas() {
        if (listaTareas.isEmpty()) {
            return "No hay tareas registradas aún.";
        }
        String texto = "--- LISTA DE TAREAS ---\n\n";
        for (Tarea t : listaTareas) {
            texto += t.mostrarDatos() + "\n\n";
        }
        return texto;
    }
}