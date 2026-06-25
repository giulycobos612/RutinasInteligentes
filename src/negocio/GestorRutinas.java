package negocio;

import modelo.RutinasEstudio;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class GestorRutinas {
    private List<RutinasEstudio> rutinaSemanal;

    public GestorRutinas() {
        this.rutinaSemanal = new ArrayList<>();
    }

    public void agregarSesion(RutinasEstudio sesion) {
        rutinaSemanal.add(sesion);
    }

    public String calcularPrioridad(RutinasEstudio sesion) {
        if (sesion.isEsDescanso() || sesion.getTarea() == null) {
            return "N/A";
        }
        String fechaLimiteStr = sesion.getTarea().getFechaEntrega();
        LocalDate hoy = LocalDate.now();
        LocalDate fechaLimite = LocalDate.parse(fechaLimiteStr,
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaLimite);
        String dificultad = sesion.getTarea().getMateria().getNivelDificultad();

        if (diasRestantes < 0)                                         return "VENCIDA";
        else if (diasRestantes <= 2 && dificultad.equals("alto"))      return "CRITICA";
        else if (diasRestantes <= 5 || dificultad.equals("alto"))      return "ALTA";
        else if (diasRestantes <= 10 && dificultad.equals("medio"))    return "MEDIA";
        else                                                           return "BAJA";
    }

    public String marcarCumplida(String nombreMateria) {
        for (RutinasEstudio s : rutinaSemanal) {
            if (!s.isEsDescanso() && s.getTarea() != null
                    && s.getTarea().getMateria().getNombreMateria().equalsIgnoreCase(nombreMateria)) {
                s.setCumplida(true);
                return "Sesión de '" + nombreMateria + "' marcada como cumplida.";
            }
        }
        return "No se encontró la materia '" + nombreMateria + "' en la rutina.";
    }

    public String reprogramarSesion(String nombreMateria, String nuevoDia,
                                    String nuevaHoraInicio, String nuevaHoraFin) {
        for (RutinasEstudio s : rutinaSemanal) {
            if (!s.isEsDescanso() && s.getTarea() != null
                    && s.getTarea().getMateria().getNombreMateria().equalsIgnoreCase(nombreMateria)) {
                s.setDia(nuevoDia);
                s.setHoraInicio(nuevaHoraInicio);
                s.setHoraFin(nuevaHoraFin);
                return "Sesión de '" + nombreMateria + "' reprogramada al " + nuevoDia
                        + " de " + nuevaHoraInicio + " a " + nuevaHoraFin + ".";
            }
        }
        return "No se encontró la materia '" + nombreMateria + "' en la rutina.";
    }

    public void mostrarRutina() {
        if (rutinaSemanal.isEmpty()) {
            System.out.println("La rutina está vacía.");
            return;

        }
        System.out.println("===== RUTINA SEMANAL =====");
        for (RutinasEstudio s : rutinaSemanal) {
            String prioridad = calcularPrioridad(s);
            System.out.println(s + (s.isEsDescanso() ? "" : " | Prioridad: " + prioridad));
        }
        System.out.println("==========================");
    }

    public List<RutinasEstudio> getRutinaSemanal() {
        return rutinaSemanal;
    }
}