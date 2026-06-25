package interfaz;

import modelo.Materia;
import modelo.Tarea;
import modelo.RutinasEstudio;
import modelo.Usuario;
import negocio.GestorMateriasTareas;
import negocio.GestorRutinas;
import negocio.GestorUsuario;
import negocio.GestorProgreso;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainUsuarios {

    static GestorUsuario gestorUsuario       = new GestorUsuario();
    static GestorMateriasTareas gestorMaterias = new GestorMateriasTareas();
    static GestorRutinas gestorRutinas         = new GestorRutinas();
    static GestorProgreso gestorProgreso       = new GestorProgreso();
    static Usuario usuarioActivo               = null;

    public static void main(String[] args) {
        int opcion;
        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(
                    "============================\n" +
                            "  SISTEMA DE RUTINAS DE ESTUDIO\n" +
                            "============================\n" +
                            "1. Registrarse\n" +
                            "2. Iniciar sesion\n" +
                            "0. Salir\n" +
                            "============================\n" +
                            "Seleccione una opcion:"));

            switch (opcion) {
                case 1 -> registrarUsuario();
                case 2 -> iniciarSesion();
                case 0 -> JOptionPane.showMessageDialog(null, "Hasta luego!");
                default -> JOptionPane.showMessageDialog(null, "Opcion no valida.");
            }
        } while (opcion != 0);
    }

    // ─── REGISTRO DE USUARIO ────────────────────────────
    static void registrarUsuario() {
        String nombre     = JOptionPane.showInputDialog("Nombre completo:");
        String correo     = JOptionPane.showInputDialog("Correo electronico:");
        String carrera    = JOptionPane.showInputDialog("Carrera:");
        int semestre      = Integer.parseInt(JOptionPane.showInputDialog("Semestre:"));
        int materias      = Integer.parseInt(JOptionPane.showInputDialog("Cantidad de materias:"));
        int horas         = Integer.parseInt(JOptionPane.showInputDialog("Horas disponibles por semana:"));
        String contrasena = JOptionPane.showInputDialog("Contrasena (minimo 8 caracteres):");

        Usuario usuario = new Usuario(nombre, correo, carrera, semestre, materias, horas, contrasena, "");
        String resultado = gestorUsuario.registrarUsuario(usuario);
        JOptionPane.showMessageDialog(null, resultado);
    }

    // ─── INICIO DE SESIÓN ───────────────────────────────
    static void iniciarSesion() {
        String correo     = JOptionPane.showInputDialog("Correo electronico:");
        String contrasena = JOptionPane.showInputDialog("Contrasena:");

        usuarioActivo = gestorUsuario.iniciarSesion(correo, contrasena);

        if (usuarioActivo == null) {
            JOptionPane.showMessageDialog(null, "Usuario o contrasena incorrectos, o cuenta bloqueada.");
        } else {
            JOptionPane.showMessageDialog(null, "Bienvenido, " + usuarioActivo.getNombreCompleto() + "!");
            menuPrincipal();
        }
    }

    // ─── MENÚ PRINCIPAL ─────────────────────────────────
    static void menuPrincipal() {
        int opcion;
        do {
            opcion = Integer.parseInt(JOptionPane.showInputDialog(
                    "============================\n" +
                            "        MENU PRINCIPAL\n" +
                            "============================\n" +
                            "1. Registrar materia o tarea\n" +
                            "2. Ver materias o tareas\n" +
                            "3. Eliminar materia o tarea\n" +
                            "4. Marcar tarea como completada\n" +
                            "5. Agregar sesion a rutina\n" +
                            "6. Ver rutina semanal\n" +
                            "7. Reprogramar sesion\n" +
                            "8. Marcar sesion como cumplida\n" +
                            "9. Ver reporte de progreso academico\n" +
                            "0. Cerrar sesion\n" +
                            "============================\n" +
                            "Seleccione una opcion:"));

            switch (opcion) {
                case 1 -> menuRegistrar();
                case 2 -> menuVer();
                case 3 -> menuEliminar();
                case 4 -> marcarTareaCompletada();
                case 5 -> agregarSesion();
                case 6 -> verRutina();
                case 7 -> reprogramarSesion();
                case 8 -> marcarCumplida();
                case 9 -> mostrarReporteProgreso();
                case 0 -> JOptionPane.showMessageDialog(null, "Sesion cerrada.");
                default -> JOptionPane.showMessageDialog(null, "Opcion no valida.");
            }
        } while (opcion != 0);
    }

    // ─── SUBMENÚ REGISTRAR ───────────────────────────────
    static void menuRegistrar() {
        String opcion = JOptionPane.showInputDialog(
                "Que deseas registrar?\n\n" +
                        "1. Materia\n" +
                        "2. Tarea");

        if ("1".equals(opcion)) {
            registrarMateria();
        } else if ("2".equals(opcion)) {
            registrarTarea();
        } else {
            JOptionPane.showMessageDialog(null, "Opcion no valida.");
        }
    }

    // ─── SUBMENÚ VER ─────────────────────────────────────
    static void menuVer() {
        String opcion = JOptionPane.showInputDialog(
                "Que deseas ver?\n\n" +
                        "1. Materias\n" +
                        "2. Tareas");

        if ("1".equals(opcion)) {
            JOptionPane.showMessageDialog(null, gestorMaterias.listarMaterias());
        } else if ("2".equals(opcion)) {
            JOptionPane.showMessageDialog(null, gestorMaterias.listarTareas());
        } else {
            JOptionPane.showMessageDialog(null, "Opcion no valida.");
        }
    }

    // ─── SUBMENÚ ELIMINAR ────────────────────────────────
    static void menuEliminar() {
        String opcion = JOptionPane.showInputDialog(
                "Que deseas eliminar?\n\n" +
                        "1. Materia\n" +
                        "2. Tarea");

        if ("1".equals(opcion)) {
            eliminarMateria();
        } else if ("2".equals(opcion)) {
            eliminarTarea();
        } else {
            JOptionPane.showMessageDialog(null, "Opcion no valida.");
        }
    }

    // ─── REGISTRAR MATERIA ───────────────────────────────
    static void registrarMateria() {
        String nombre      = JOptionPane.showInputDialog("Nombre de la materia:");
        String dificultad  = JOptionPane.showInputDialog("Nivel de dificultad (alto/medio/bajo):");
        double calificacion = Double.parseDouble(JOptionPane.showInputDialog("Calificacion actual (0-10):"));
        double notaMinima  = Double.parseDouble(JOptionPane.showInputDialog("Nota minima personal (0-10):"));

        String resultado = gestorMaterias.registrarMateria(nombre, dificultad, calificacion, notaMinima);
        JOptionPane.showMessageDialog(null, resultado);
    }

    // ─── REGISTRAR TAREA ─────────────────────────────────
    static void registrarTarea() {
        if (gestorMaterias.getListaMaterias().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Primero debes registrar al menos una materia.");
            return;
        }

        String nombreTarea = JOptionPane.showInputDialog("Nombre de la tarea:");

        String listaMaterias = "Seleccione la materia asociada:\n\n";
        for (int i = 0; i < gestorMaterias.getListaMaterias().size(); i++) {
            Materia m = gestorMaterias.getListaMaterias().get(i);
            listaMaterias += (i + 1) + ". " + m.getNombreMateria() + "\n";
        }

        int indiceMateria = Integer.parseInt(JOptionPane.showInputDialog(listaMaterias)) - 1;

        if (indiceMateria < 0 || indiceMateria >= gestorMaterias.getListaMaterias().size()) {
            JOptionPane.showMessageDialog(null, "Numero de materia no valido.");
            return;
        }

        String nombreMateria = gestorMaterias.getListaMaterias().get(indiceMateria).getNombreMateria();

        String fechaEntrega = "";
        while (true) {
            fechaEntrega = JOptionPane.showInputDialog("Fecha de entrega (dd/MM/yyyy):");
            String[] partes = fechaEntrega.split("/");

            if (partes.length == 3) {
                int dia  = Integer.parseInt(partes[0]);
                int mes  = Integer.parseInt(partes[1]);
                int anio = Integer.parseInt(partes[2]);

                if (dia >= 1 && dia <= 31 && mes >= 1 && mes <= 12 && anio >= 2026) {
                    LocalDate fechaIngresada = LocalDate.of(anio, mes, dia);
                    LocalDate hoy = LocalDate.now();

                    if (fechaIngresada.isBefore(hoy) || fechaIngresada.isEqual(hoy)) {
                        JOptionPane.showMessageDialog(null, "La fecha ya paso, ingrese una fecha futura.");
                    } else {
                        break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Fecha invalida, intentelo de nuevo.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Fecha invalida, intentelo de nuevo.");
            }
        }

        String resultado = gestorMaterias.registrarTarea(nombreTarea, fechaEntrega, nombreMateria);
        JOptionPane.showMessageDialog(null, resultado);
    }

    // ─── ELIMINAR MATERIA ────────────────────────────────
    static void eliminarMateria() {
        if (gestorMaterias.getListaMaterias().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay materias registradas.");
            return;
        }

        String lista = "Seleccione la materia a eliminar:\n\n";
        for (int i = 0; i < gestorMaterias.getListaMaterias().size(); i++) {
            Materia m = gestorMaterias.getListaMaterias().get(i);
            lista += (i + 1) + ". " + m.getNombreMateria() + "\n";
        }

        int indice = Integer.parseInt(JOptionPane.showInputDialog(lista)) - 1;
        String resultado = gestorMaterias.eliminarMateria(indice);
        JOptionPane.showMessageDialog(null, resultado);
    }

    // ─── ELIMINAR TAREA ──────────────────────────────────
    static void eliminarTarea() {
        if (gestorMaterias.getListaTareas().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay tareas registradas.");
            return;
        }

        String lista = "Seleccione la tarea a eliminar:\n\n";
        for (int i = 0; i < gestorMaterias.getListaTareas().size(); i++) {
            Tarea t = gestorMaterias.getListaTareas().get(i);
            lista += (i + 1) + ". " + t.getNombreTarea() +
                    " - Materia: " + t.getMateria().getNombreMateria() + "\n";
        }

        int indice = Integer.parseInt(JOptionPane.showInputDialog(lista)) - 1;
        String resultado = gestorMaterias.eliminarTarea(indice);
        JOptionPane.showMessageDialog(null, resultado);
    }

    // ─── MARCAR TAREA COMPLETADA ─────────────────────────
    static void marcarTareaCompletada() {
        if (gestorMaterias.getListaTareas().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay tareas registradas.");
            return;
        }

        String lista = "Seleccione la tarea a marcar como completada:\n\n";
        for (int i = 0; i < gestorMaterias.getListaTareas().size(); i++) {
            Tarea t = gestorMaterias.getListaTareas().get(i);
            lista += (i + 1) + ". " + t.getNombreTarea() +
                    " - Materia: " + t.getMateria().getNombreMateria() +
                    " - Estado: " + (t.isCompletada() ? "COMPLETADA" : "PENDIENTE") + "\n";
        }

        int indice = Integer.parseInt(JOptionPane.showInputDialog(lista)) - 1;
        String resultado = gestorMaterias.marcarTareaCompletada(indice);
        JOptionPane.showMessageDialog(null, resultado);

        // Recalcular progreso al completar una tarea tambien
        recalcularProgresoCompleto();
    }

    // ─── AGREGAR SESIÓN ──────────────────────────────────
    static void agregarSesion() {
        String opcionTarea = JOptionPane.showInputDialog(
                "Deseas crear una sesion con tarea o sin tarea?\n\n" +
                        "1. Con tarea\n" +
                        "2. Sin tarea");

        if ("1".equals(opcionTarea)) {
            agregarSesionConTarea();
        } else if ("2".equals(opcionTarea)) {
            agregarSesionSinTarea();
        } else {
            JOptionPane.showMessageDialog(null, "Opcion no valida.");
        }
    }

    // ─── AGREGAR SESIÓN CON TAREA ────────────────────────
    static void agregarSesionConTarea() {
        List<Tarea> tareasPendientes = new ArrayList<>();
        for (Tarea t : gestorMaterias.getListaTareas()) {
            if (!t.isCompletada()) {
                tareasPendientes.add(t);
            }
        }

        if (tareasPendientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay tareas pendientes para agregar a la rutina.");
            return;
        }

        String listaTareas = "Seleccione la tarea para la sesion:\n\n";
        for (int i = 0; i < tareasPendientes.size(); i++) {
            Tarea t = tareasPendientes.get(i);
            listaTareas += (i + 1) + ". " + t.getNombreTarea() +
                    " - Materia: " + t.getMateria().getNombreMateria() +
                    " - Fecha entrega: " + t.getFechaEntrega() +
                    " - Prioridad: " + t.getPrioridad() + "\n";
        }

        int indice = Integer.parseInt(JOptionPane.showInputDialog(listaTareas)) - 1;

        if (indice < 0 || indice >= tareasPendientes.size()) {
            JOptionPane.showMessageDialog(null, "Numero de tarea no valido.");
            return;
        }

        Tarea tareaSeleccionada = tareasPendientes.get(indice);

        String fechaSesion = "";
        while (true) {
            fechaSesion = JOptionPane.showInputDialog("Fecha de la sesion (dd/MM/yyyy):");
            String[] partes = fechaSesion.split("/");

            if (partes.length == 3) {
                int dia  = Integer.parseInt(partes[0]);
                int mes  = Integer.parseInt(partes[1]);
                int anio = Integer.parseInt(partes[2]);

                if (dia >= 1 && dia <= 31 && mes >= 1 && mes <= 12 && anio >= 2026) {
                    LocalDate fechaSesionDate = LocalDate.of(anio, mes, dia);
                    LocalDate hoy = LocalDate.now();
                    LocalDate fechaEntrega = LocalDate.parse(tareaSeleccionada.getFechaEntrega(),
                            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                    if (fechaSesionDate.isBefore(hoy)) {
                        JOptionPane.showMessageDialog(null, "La fecha ya paso, ingrese una fecha futura.");
                    } else if (fechaSesionDate.isAfter(fechaEntrega)) {
                        JOptionPane.showMessageDialog(null,
                                "La sesion no puede ser despues de la fecha de entrega (" +
                                        tareaSeleccionada.getFechaEntrega() + ").");
                    } else {
                        break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Fecha invalida, intentelo de nuevo.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Fecha invalida, intentelo de nuevo.");
            }
        }

        String horaInicio = JOptionPane.showInputDialog("Hora inicio (HH:mm):");
        String horaFin    = JOptionPane.showInputDialog("Hora fin (HH:mm):");

        // Calcular duración real en minutos a partir de las horas ingresadas
        int duracionMinutos = calcularDuracionMinutos(horaInicio, horaFin);

        for (RutinasEstudio s : gestorRutinas.getRutinaSemanal()) {
            if (s.getDia().equals(fechaSesion) && s.getHoraInicio().equals(horaInicio)) {
                JOptionPane.showMessageDialog(null, "Ya tienes una sesion en ese horario, elige otro.");
                return;
            }
        }

        // Se guarda la duración real calculada
        RutinasEstudio sesion = new RutinasEstudio(fechaSesion, horaInicio, horaFin, duracionMinutos, tareaSeleccionada);
        gestorRutinas.agregarSesion(sesion);
        JOptionPane.showMessageDialog(null, "Sesion agregada correctamente a la rutina.");
    }

    // ─── AGREGAR SESIÓN SIN TAREA ────────────────────────
    static void agregarSesionSinTarea() {
        String nombreSesion = JOptionPane.showInputDialog("Nombre de la sesion (ej: Repasar apuntes, Leer capitulo...):");

        String fechaSesion = "";
        while (true) {
            fechaSesion = JOptionPane.showInputDialog("Fecha de la sesion (dd/MM/yyyy):");
            String[] partes = fechaSesion.split("/");

            if (partes.length == 3) {
                int dia  = Integer.parseInt(partes[0]);
                int mes  = Integer.parseInt(partes[1]);
                int anio = Integer.parseInt(partes[2]);

                if (dia >= 1 && dia <= 31 && mes >= 1 && mes <= 12 && anio >= 2026) {
                    LocalDate fechaSesionDate = LocalDate.of(anio, mes, dia);
                    LocalDate hoy = LocalDate.now();

                    if (fechaSesionDate.isBefore(hoy)) {
                        JOptionPane.showMessageDialog(null, "La fecha ya paso, ingrese una fecha futura.");
                    } else {
                        break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Fecha invalida, intentelo de nuevo.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Fecha invalida, intentelo de nuevo.");
            }
        }

        String horaInicio = JOptionPane.showInputDialog("Hora inicio (HH:mm):");
        String horaFin    = JOptionPane.showInputDialog("Hora fin (HH:mm):");

        // Calcular duración real en minutos
        int duracionMinutos = calcularDuracionMinutos(horaInicio, horaFin);

        for (RutinasEstudio s : gestorRutinas.getRutinaSemanal()) {
            if (s.getDia().equals(fechaSesion) && s.getHoraInicio().equals(horaInicio)) {
                JOptionPane.showMessageDialog(null, "Ya tienes una sesion en ese horario, elige otro.");
                return;
            }
        }

        RutinasEstudio sesion = new RutinasEstudio(fechaSesion, horaInicio, horaFin, duracionMinutos, null);
        sesion.setNombreSesion(nombreSesion);
        gestorRutinas.agregarSesion(sesion);
        JOptionPane.showMessageDialog(null, "Sesion agregada correctamente a la rutina.");
    }

    // ─── VER RUTINA ──────────────────────────────────────
    static void verRutina() {
        if (gestorRutinas.getRutinaSemanal().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay sesiones en la rutina aun.");
            return;
        }
        String texto = "============================\n" +
                "       RUTINA SEMANAL\n" +
                "============================\n";
        for (RutinasEstudio s : gestorRutinas.getRutinaSemanal()) {
            String prioridad = (s.isEsDescanso() || s.getTarea() == null)
                    ? "" : gestorRutinas.calcularPrioridad(s);
            texto += s + (prioridad.isEmpty() ? "" : " | Prioridad: " + prioridad) + "\n";
        }
        JOptionPane.showMessageDialog(null, texto);
    }

    // ─── REPROGRAMAR SESIÓN ──────────────────────────────
    static void reprogramarSesion() {
        if (gestorRutinas.getRutinaSemanal().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay sesiones en la rutina.");
            return;
        }

        String lista = "Seleccione la sesion a reprogramar:\n\n";
        for (int i = 0; i < gestorRutinas.getRutinaSemanal().size(); i++) {
            RutinasEstudio s = gestorRutinas.getRutinaSemanal().get(i);
            String descripcion = (s.getTarea() == null)
                    ? s.getNombreSesion()
                    : s.getTarea().getNombreTarea() + " - Materia: " + s.getTarea().getMateria().getNombreMateria();
            lista += (i + 1) + ". " + s.getDia() + " " + s.getHoraInicio() + "-" + s.getHoraFin() +
                    " | " + descripcion + "\n";
        }

        int indice = Integer.parseInt(JOptionPane.showInputDialog(lista)) - 1;

        if (indice < 0 || indice >= gestorRutinas.getRutinaSemanal().size()) {
            JOptionPane.showMessageDialog(null, "Numero no valido.");
            return;
        }

        String nuevaFecha = "";
        while (true) {
            nuevaFecha = JOptionPane.showInputDialog("Nueva fecha (dd/MM/yyyy):");
            String[] partes = nuevaFecha.split("/");

            if (partes.length == 3) {
                int dia  = Integer.parseInt(partes[0]);
                int mes  = Integer.parseInt(partes[1]);
                int anio = Integer.parseInt(partes[2]);

                if (dia >= 1 && dia <= 31 && mes >= 1 && mes <= 12 && anio >= 2026) {
                    LocalDate fechaNueva = LocalDate.of(anio, mes, dia);
                    LocalDate hoy = LocalDate.now();

                    if (fechaNueva.isBefore(hoy)) {
                        JOptionPane.showMessageDialog(null, "La fecha ya paso, ingrese una fecha futura.");
                    } else {
                        break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Fecha invalida, intentelo de nuevo.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Fecha invalida, intentelo de nuevo.");
            }
        }

        String nuevaHoraInicio = JOptionPane.showInputDialog("Nueva hora inicio (HH:mm):");
        String nuevaHoraFin    = JOptionPane.showInputDialog("Nueva hora fin (HH:mm):");

        for (int i = 0; i < gestorRutinas.getRutinaSemanal().size(); i++) {
            RutinasEstudio s = gestorRutinas.getRutinaSemanal().get(i);
            if (i != indice && s.getDia().equals(nuevaFecha) && s.getHoraInicio().equals(nuevaHoraInicio)) {
                JOptionPane.showMessageDialog(null, "Ya tienes una sesion en ese horario, elige otro.");
                return;
            }
        }

        RutinasEstudio sesion = gestorRutinas.getRutinaSemanal().get(indice);
        sesion.setDia(nuevaFecha);
        sesion.setHoraInicio(nuevaHoraInicio);
        sesion.setHoraFin(nuevaHoraFin);
        // Recalcular duracion con el nuevo horario
        sesion.setDuracionMinutos(calcularDuracionMinutos(nuevaHoraInicio, nuevaHoraFin));

        JOptionPane.showMessageDialog(null, "Sesion reprogramada correctamente.");
    }

    // ─── MARCAR SESIÓN CUMPLIDA ──────────────────────────
    static void marcarCumplida() {
        if (gestorRutinas.getRutinaSemanal().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay sesiones en la rutina.");
            return;
        }

        String lista = "Seleccione la sesion a marcar como cumplida:\n\n";
        for (int i = 0; i < gestorRutinas.getRutinaSemanal().size(); i++) {
            RutinasEstudio s = gestorRutinas.getRutinaSemanal().get(i);
            String descripcion = (s.getTarea() == null)
                    ? s.getNombreSesion()
                    : s.getTarea().getMateria().getNombreMateria() + " - Tarea: " + s.getTarea().getNombreTarea();
            lista += (i + 1) + ". " + s.getDia() + " " + s.getHoraInicio() + "-" + s.getHoraFin() +
                    " | " + descripcion +
                    " | " + (s.isCumplida() ? "CUMPLIDA" : "PENDIENTE") + "\n";
        }

        int indice = Integer.parseInt(JOptionPane.showInputDialog(lista)) - 1;

        if (indice < 0 || indice >= gestorRutinas.getRutinaSemanal().size()) {
            JOptionPane.showMessageDialog(null, "Numero no valido.");
            return;
        }

        RutinasEstudio sesion = gestorRutinas.getRutinaSemanal().get(indice);

        if (sesion.isCumplida()) {
            JOptionPane.showMessageDialog(null, "Esta sesion ya estaba marcada como cumplida.");
            return;
        }

        // 1. Marcar como cumplida
        sesion.setCumplida(true);

        // 2. Obtener dificultad real de la materia (solo si tiene tarea asociada)
        int dificultadInt = 2; // medio por defecto
        if (sesion.getTarea() != null && sesion.getTarea().getMateria() != null) {
            String dif = sesion.getTarea().getMateria().getNivelDificultad();
            if (dif != null) {
                if (dif.equalsIgnoreCase("alto"))  dificultadInt = 3;
                else if (dif.equalsIgnoreCase("bajo")) dificultadInt = 1;
            }
        }

        // 3. Obtener duración real calculada al crear la sesión
        //    Si por algún motivo es 0 (sesiones viejas), usar 60 como respaldo
        int duracion = sesion.getDuracionMinutos();
        if (duracion <= 0) duracion = 60;

        // 4. Registrar la sesión completada (acumula minutos reales)
        gestorProgreso.registrarSesionCompletada(duracion, dificultadInt);

        // 5. Contar sesiones reales del programa
        int totalSesiones = gestorRutinas.getRutinaSemanal().size();
        int sesionesCompletadas = 0;
        for (RutinasEstudio s : gestorRutinas.getRutinaSemanal()) {
            if (s.isCumplida()) sesionesCompletadas++;
        }

        // 6. Contar tareas reales del programa (CORRECCIÓN: usar getListaTareas())
        int totalTareas = gestorMaterias.getListaTareas().size();
        int tareasCompletadas = 0;
        for (Tarea t : gestorMaterias.getListaTareas()) {
            if (t.isCompletada()) tareasCompletadas++;
        }

        // 7. Recalcular todo el progreso con datos reales
        gestorProgreso.recalcularProgreso(totalSesiones, sesionesCompletadas,
                totalTareas, tareasCompletadas);

        JOptionPane.showMessageDialog(null,
                "Sesion marcada como cumplida.\nProgreso actualizado correctamente.");
    }

    // ─── VER REPORTE DE PROGRESO ─────────────────────────
    static void mostrarReporteProgreso() {
        // Siempre recalcular con datos actuales antes de mostrar
        int totalSesiones = gestorRutinas.getRutinaSemanal().size();
        int sesionesCompletadas = 0;
        for (RutinasEstudio s : gestorRutinas.getRutinaSemanal()) {
            if (s.isCumplida()) sesionesCompletadas++;
        }

        int totalTareas = gestorMaterias.getListaTareas().size();
        int tareasCompletadas = 0;
        for (Tarea t : gestorMaterias.getListaTareas()) {
            if (t.isCompletada()) tareasCompletadas++;
        }

        gestorProgreso.recalcularProgreso(totalSesiones, sesionesCompletadas,
                totalTareas, tareasCompletadas);

        modelo.ProgresoAcademico reporte = gestorProgreso.generarReporteAcademico();
        JOptionPane.showMessageDialog(null,
                reporte.obtenerResumenProgreso(),
                "Mi Progreso Academico",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ─── HELPER: calcular duración real en minutos ────────
    /**
     * Calcula la diferencia en minutos entre horaInicio y horaFin.
     * Formato esperado: "HH:mm"
     * Si el formato es invalido, retorna 60 como valor de respaldo.
     */
    static int calcularDuracionMinutos(String horaInicio, String horaFin) {
        try {
            String[] ini = horaInicio.split(":");
            String[] fin = horaFin.split(":");
            int minutosIni = Integer.parseInt(ini[0]) * 60 + Integer.parseInt(ini[1]);
            int minutosFin = Integer.parseInt(fin[0]) * 60 + Integer.parseInt(fin[1]);
            int diff = minutosFin - minutosIni;
            return diff > 0 ? diff : 60; // si la diferencia es negativa o cero, usar 60 de respaldo
        } catch (Exception e) {
            return 60; // respaldo seguro
        }
    }

    // ─── HELPER: recalcular progreso completo ─────────────
    /**
     * Recalcula progreso con todos los datos actuales del programa.
     * Usado internamente para mantener el progreso siempre sincronizado.
     */
    static void recalcularProgresoCompleto() {
        int totalSesiones = gestorRutinas.getRutinaSemanal().size();
        int sesionesCompletadas = 0;
        for (RutinasEstudio s : gestorRutinas.getRutinaSemanal()) {
            if (s.isCumplida()) sesionesCompletadas++;
        }

        int totalTareas = gestorMaterias.getListaTareas().size();
        int tareasCompletadas = 0;
        for (Tarea t : gestorMaterias.getListaTareas()) {
            if (t.isCompletada()) tareasCompletadas++;
        }

        gestorProgreso.recalcularProgreso(totalSesiones, sesionesCompletadas,
                totalTareas, tareasCompletadas);
    }
}