package negocio;

import modelo.Materia;
import modelo.Tarea;
import modelo.Usuario;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class GestorMateriasTareas {
    private GestorUsuario gestorUsuario;

    public GestorMateriasTareas(GestorUsuario gestorUsuario) {
        this.gestorUsuario = gestorUsuario;
    }

    private Usuario getUsuario() {
        Usuario u = gestorUsuario.getUsuarioActivo();
        if (u == null) throw new IllegalStateException("No hay un usuario activo en sesion.");
        return u;
    }

    // ===================== MATERIAS =====================

    public String registrarMateria(String nombreMateria, String nivelDificultad,
                                   double calificacionActual, double notaMinimaPersonal) {
        // Validaciones exhaustivas
        if (nombreMateria == null || nombreMateria.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la materia es obligatorio.");
        }
        if (nombreMateria.trim().length() < 2) {
            throw new IllegalArgumentException("El nombre de la materia debe tener al menos 2 caracteres.");
        }
        if (nombreMateria.trim().length() > 50) {
            throw new IllegalArgumentException("El nombre de la materia no puede exceder 50 caracteres.");
        }
        if (calificacionActual < 0 || calificacionActual > 10) {
            throw new IllegalArgumentException("La calificacion debe estar entre 0 y 10.");
        }
        if (notaMinimaPersonal <= 0 || notaMinimaPersonal > 10) {
            throw new IllegalArgumentException("La nota minima debe estar entre 1 y 10.");
        }

        Usuario u = getUsuario();
        if (u.getMaterias().size() >= u.getCantidadMaterias()) {
            throw new IllegalArgumentException("Limite de materias alcanzado (" + u.getCantidadMaterias() + "). No puedes agregar mas.");
        }
        if (buscarMateria(nombreMateria) != null) {
            throw new IllegalArgumentException("Ya existe una materia llamada '" + nombreMateria.trim() + "'.");
        }

        Materia materia = new Materia(nombreMateria, nivelDificultad, calificacionActual, notaMinimaPersonal);
        double horas = calcularHoras(nivelDificultad, calificacionActual, notaMinimaPersonal);
        materia.setHorasRecomendadas(horas);

        u.getMaterias().add(materia);
        gestorUsuario.guardarCambios();

        return generarMensajeRendimiento(calificacionActual, notaMinimaPersonal);
    }

    // ===================== TAREAS =====================

    public String registrarTarea(String nombreTarea, LocalDate fechaEntrega, Materia materiaEncontrada) {
        // Validaciones exhaustivas
        if (nombreTarea == null || nombreTarea.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la tarea es obligatorio.");
        }
        if (nombreTarea.trim().length() < 2) {
            throw new IllegalArgumentException("El nombre de la tarea debe tener al menos 2 caracteres.");
        }
        if (nombreTarea.trim().length() > 80) {
            throw new IllegalArgumentException("El nombre de la tarea no puede exceder 80 caracteres.");
        }
        if (materiaEncontrada == null) {
            throw new IllegalArgumentException("Debes seleccionar una materia.");
        }
        if (fechaEntrega == null) {
            throw new IllegalArgumentException("La fecha de entrega es obligatoria.");
        }

        LocalDate hoy = LocalDate.now();
        if (fechaEntrega.isBefore(hoy)) {
            throw new IllegalArgumentException("No puedes registrar una tarea con fecha pasada (" + fechaEntrega + "). Selecciona hoy o una fecha futura.");
        }

        // Validar que no exista tarea duplicada con mismo nombre en misma materia
        for (Tarea existente : getUsuario().getTareas()) {
            if (existente.getNombreTarea().equalsIgnoreCase(nombreTarea.trim()) &&
                existente.getMateria().equals(materiaEncontrada)) {
                throw new IllegalArgumentException("Ya existe una tarea '" + nombreTarea.trim() + "' en " + materiaEncontrada.getNombreMateria() + ".");
            }
        }

        Tarea tarea = new Tarea(nombreTarea, fechaEntrega, materiaEncontrada);
        String prioridad = calcularPrioridad(fechaEntrega, materiaEncontrada.getNivelDificultad(),
                materiaEncontrada.getCalificacionActual(), materiaEncontrada.getNotaMinimaPersonal());
        tarea.setPrioridad(prioridad);

        Usuario u = getUsuario();
        u.getTareas().add(tarea);
        ordenarTareas();
        gestorUsuario.guardarCambios();

        return "Tarea registrada. Prioridad: " + prioridad;
    }

    public void marcarTareaCompletada(Tarea t, boolean completada) {
        if (t == null) throw new IllegalArgumentException("La tarea no puede ser nula.");
        t.setCompletada(completada);
        gestorUsuario.guardarCambios();
    }

    public void eliminarMateria(Materia materiaAEliminar) {
        if (materiaAEliminar == null) throw new IllegalArgumentException("La materia no puede ser nula.");
        
        Usuario u = getUsuario();

        boolean tieneTareas = u.getTareas().stream()
            .anyMatch(t -> t.getMateria().getNombreMateria().equalsIgnoreCase(materiaAEliminar.getNombreMateria()));
            
        if (tieneTareas) {
            throw new IllegalStateException("No se puede eliminar la materia porque aun tiene tareas asociadas. Elimina sus tareas primero.");
        }

        u.getRutinas().removeIf(r -> r.getTarea() != null && r.getTarea().getMateria().getNombreMateria().equalsIgnoreCase(materiaAEliminar.getNombreMateria()));
        u.getMaterias().remove(materiaAEliminar);
        gestorUsuario.guardarCambios();
    }

    public void eliminarTarea(Tarea tareaAEliminar) {
        if (tareaAEliminar == null) throw new IllegalArgumentException("La tarea no puede ser nula.");
        Usuario u = getUsuario();
        u.getRutinas().removeIf(r -> r.getTarea() != null && r.getTarea().equals(tareaAEliminar));
        u.getTareas().remove(tareaAEliminar);
        gestorUsuario.guardarCambios();
    }

    public String obtenerMensajeAvance(Tarea t) {
        Usuario u = getUsuario();
        long totalAvances = u.getRutinas().stream().filter(r -> r.getTarea() != null && r.getTarea().equals(t)).count();
        long avancesCompletados = u.getRutinas().stream().filter(r -> r.getTarea() != null && r.getTarea().equals(t) && r.isCumplida()).count();
        if (totalAvances > 0) {
            return "Felicidades! Tu tarea tenia " + totalAvances + " avances (rutinas), pero ya terminaste en el avance " + avancesCompletados + ".";
        }
        return "Tarea archivada en Completadas";
    }

    public Materia buscarMateria(String nombreMateria) {
        if (nombreMateria == null) return null;
        for (Materia m : getUsuario().getMaterias()) {
            if (m.getNombreMateria().equalsIgnoreCase(nombreMateria.trim())) {
                return m;
            }
        }
        return null;
    }

    public List<Materia> getListaMaterias() {
        return getUsuario().getMaterias();
    }

    public List<Tarea> getListaTareas() {
        return getUsuario().getTareas();
    }

    // ===================== UTILIDADES =====================

    private void ordenarTareas() {
        getUsuario().getTareas().sort(Comparator
                .comparing(Tarea::isCompletada)
                .thenComparing(Tarea::getFechaEntrega));
    }

    private double calcularHoras(String nivelDificultad, double calificacionActual, double notaMinimaPersonal) {
        double horasBase = 2;
        if (nivelDificultad != null) {
            if (nivelDificultad.equalsIgnoreCase("alto")) horasBase = 5;
            else if (nivelDificultad.equalsIgnoreCase("medio")) horasBase = 3;
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
            return "Materia registrada. Estas por debajo de tu nota minima, priorizala.";
        } else if (calificacionActual - notaMinimaPersonal < 1) {
            return "Materia registrada. Advertencia: estas cerca de tu nota minima.";
        } else {
            return "Materia registrada. Vas bien, manten el ritmo!";
        }
    }

    private String calcularPrioridad(LocalDate fechaEntrega, String nivelDificultad,
                                     double calificacionActual, double notaMinimaPersonal) {
        LocalDate hoy = LocalDate.now();
        long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaEntrega);

        if (diasRestantes < 0) {
            return "Vencida";
        } else if (diasRestantes <= 1) {
            return "Urgente";
        } else if (diasRestantes <= 3) {
            return "Alta";
        } else if (diasRestantes <= 7 || (nivelDificultad != null && nivelDificultad.equalsIgnoreCase("alto"))) {
            return "Alta";
        } else if (diasRestantes <= 14 && (nivelDificultad != null && nivelDificultad.equalsIgnoreCase("medio"))) {
            return "Media";
        } else {
            return "Baja";
        }
    }
}