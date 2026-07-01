package negocio;

import modelo.ProgresoAcademico;
import modelo.Usuario;

public class GestorProgreso {
    private GestorUsuario gestorUsuario;

    public GestorProgreso(GestorUsuario gestorUsuario) {
        this.gestorUsuario = gestorUsuario;
    }

    private Usuario getUsuario() {
        Usuario u = gestorUsuario.getUsuarioActivo();
        if (u == null) throw new IllegalStateException("No hay un usuario activo en sesion.");
        return u;
    }

    public ProgresoAcademico actualizarYObtenerProgreso() {
        Usuario u = getUsuario();
        ProgresoAcademico progreso = u.getProgreso();
        progreso.recalcularProgreso(u.getTareas(), u.getRutinas());
        progreso.recalcularMetricasExtendidas(u.getMaterias(), u.getRutinas());
        gestorUsuario.guardarCambios();
        return progreso;
    }

    public Usuario getUsuarioActivo() {
        return getUsuario();
    }
}
