package negocio;

import modelo.RutinasEstudio;
import modelo.Usuario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class GestorRutinas {
    private GestorUsuario gestorUsuario;

    public GestorRutinas(GestorUsuario gestorUsuario) {
        this.gestorUsuario = gestorUsuario;
    }

    private Usuario getUsuario() {
        Usuario u = gestorUsuario.getUsuarioActivo();
        if (u == null) throw new IllegalStateException("No hay un usuario activo en sesion.");
        return u;
    }

    public java.util.List<modelo.Tarea> getTareas() {
        return getUsuario().getTareas();
    }

    public void guardarCambios() {
        gestorUsuario.guardarCambios();
    }

    public void agregarSesion(RutinasEstudio sesion) {
        if (sesion == null) throw new IllegalArgumentException("La sesion no puede ser nula.");

        validarFechaFutura(sesion.getDia());
        validarHorario(sesion.getHoraInicio(), sesion.getHoraFin());
        validarDuracionMinima(sesion.getHoraInicio(), sesion.getHoraFin());
        verificarCruceHorario(sesion, null);

        getUsuario().getRutinas().add(sesion);
        ordenarRutinas();
        gestorUsuario.guardarCambios();
    }

    public void reprogramarSesion(RutinasEstudio sesion, LocalDate nuevoDia, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin) {
        if (sesion == null) throw new IllegalArgumentException("La sesion no existe.");

        validarFechaFutura(nuevoDia);
        validarHorario(nuevaHoraInicio, nuevaHoraFin);
        validarDuracionMinima(nuevaHoraInicio, nuevaHoraFin);

        // Crear sesion temporal para validar cruce (excluyendo la sesion actual)
        RutinasEstudio temp = new RutinasEstudio(nuevoDia, nuevaHoraInicio, nuevaHoraFin);
        verificarCruceHorario(temp, sesion);

        sesion.setDia(nuevoDia);
        sesion.setHoraInicio(nuevaHoraInicio);
        sesion.setHoraFin(nuevaHoraFin);
        ordenarRutinas();
        gestorUsuario.guardarCambios();
    }

    public void marcarCumplida(RutinasEstudio sesion, boolean cumplida) {
        if (sesion == null) throw new IllegalArgumentException("La sesion no puede ser nula.");
        sesion.setCumplida(cumplida);
        gestorUsuario.guardarCambios();
    }

    public void eliminarSesion(RutinasEstudio sesion) {
        if (sesion == null) throw new IllegalArgumentException("La sesion no puede ser nula.");
        getUsuario().getRutinas().remove(sesion);
        gestorUsuario.guardarCambios();
    }

    public List<RutinasEstudio> getRutinaSemanal() {
        return getUsuario().getRutinas();
    }

    // ===================== VALIDACIONES =====================

    private void validarFechaFutura(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No puedes agendar sesiones en fechas pasadas. Selecciona hoy o una fecha futura.");
        }
    }

    private void validarHorario(LocalTime inicio, LocalTime fin) {
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Las horas de inicio y fin son obligatorias.");
        }
        if (!fin.isAfter(inicio)) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio.");
        }
    }

    private void validarDuracionMinima(LocalTime inicio, LocalTime fin) {
        long minutos = java.time.Duration.between(inicio, fin).toMinutes();
        if (minutos < 15) {
            throw new IllegalArgumentException("La sesion debe durar al menos 15 minutos.");
        }
        if (minutos > 480) {
            throw new IllegalArgumentException("La sesion no puede durar mas de 8 horas.");
        }
    }

    private void verificarCruceHorario(RutinasEstudio nueva, RutinasEstudio excluir) {
        for (RutinasEstudio existente : getUsuario().getRutinas()) {
            if (existente == excluir) continue;
            if (existente.getDia().equals(nueva.getDia())) {
                boolean cruce = nueva.getHoraInicio().isBefore(existente.getHoraFin()) &&
                                nueva.getHoraFin().isAfter(existente.getHoraInicio());
                if (cruce) {
                    throw new IllegalArgumentException("Cruce de horario: Ya tienes '" + existente.toString() +
                            "' de " + existente.getHoraInicioFormateada() + " a " + existente.getHoraFinFormateada() + " ese dia.");
                }
            }
        }
    }

    private void ordenarRutinas() {
        getUsuario().getRutinas().sort(Comparator
                .comparing(RutinasEstudio::getDia)
                .thenComparing(RutinasEstudio::getHoraInicio));
    }
}