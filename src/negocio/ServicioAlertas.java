package negocio;

import interfaz.componentes.ModernToast;
import modelo.RutinasEstudio;
import modelo.Tarea;
import modelo.Usuario;

import javax.swing.JFrame;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServicioAlertas {
    private GestorUsuario gestorUsuario;
    private JFrame mainFrame;
    private ScheduledExecutorService scheduler;

    public ServicioAlertas(GestorUsuario gestorUsuario, JFrame mainFrame) {
        this.gestorUsuario = gestorUsuario;
        this.mainFrame = mainFrame;
    }

    public void iniciar() {
        if (scheduler != null && !scheduler.isShutdown()) return;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        // Revisar cada minuto
        scheduler.scheduleAtFixedRate(this::revisarAlertas, 0, 1, TimeUnit.MINUTES);
    }

    public void detener() {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }

    private void revisarAlertas() {
        Usuario u = gestorUsuario.getUsuarioActivo();
        if (u == null) return;

        LocalDateTime ahora = LocalDateTime.now();
        LocalDate hoy = ahora.toLocalDate();
        LocalTime horaActual = ahora.toLocalTime();

        // Alertas de Tareas
        for (Tarea t : u.getTareas()) {
            if (t.isCompletada() || !t.isRecordatorioActivado()) continue;
            
            // Asumimos que la tarea vence al final del día (23:59) de su fecha de entrega
            LocalDateTime limite = LocalDateTime.of(t.getFechaEntrega(), LocalTime.of(23, 59));
            long horasRestantes = ChronoUnit.HOURS.between(ahora, limite);
            long minutosRestantes = ChronoUnit.MINUTES.between(ahora, limite);

            if (minutosRestantes > 0) {
                if (minutosRestantes == 48 * 60) enviarAlerta("Faltan 48 horas para: " + t.getNombreTarea(), ModernToast.Type.INFO);
                else if (minutosRestantes == 24 * 60) enviarAlerta("Faltan 24 horas para: " + t.getNombreTarea(), ModernToast.Type.WARNING);
                else if (minutosRestantes == 6 * 60) enviarAlerta("🚨 Faltan 6 horas para: " + t.getNombreTarea(), ModernToast.Type.WARNING);
                else if (minutosRestantes == 60) enviarAlerta("⚠️ Queda 1 hora para: " + t.getNombreTarea(), ModernToast.Type.ERROR);
                else if (minutosRestantes == 30) enviarAlerta("🔥 Quedan 30 minutos para: " + t.getNombreTarea(), ModernToast.Type.ERROR);
                else if (minutosRestantes == 15) enviarAlerta("⏰ ¡Últimos 15 minutos! " + t.getNombreTarea(), ModernToast.Type.ERROR);
            }
        }

        // Alertas de Rutinas (Sesiones)
        for (RutinasEstudio r : u.getRutinas()) {
            if (r.isCumplida() || !r.isRecordatorioActivado()) continue;
            if (r.getDia().equals(hoy)) {
                long minutosRestantes = ChronoUnit.MINUTES.between(horaActual, r.getHoraInicio());
                if (minutosRestantes == 60) enviarAlerta("Tu sesión '" + r.toString() + "' empieza en 1 hora.", ModernToast.Type.INFO);
                else if (minutosRestantes == 30) enviarAlerta("Tu sesión '" + r.toString() + "' empieza en 30 minutos.", ModernToast.Type.INFO);
                else if (minutosRestantes == 15) enviarAlerta("Tu sesión '" + r.toString() + "' empieza en 15 minutos.", ModernToast.Type.WARNING);
                else if (minutosRestantes == 0) enviarAlerta("¡Es hora de empezar tu sesión '" + r.toString() + "'!", ModernToast.Type.SUCCESS);
            }
        }
    }

    private void enviarAlerta(String mensaje, ModernToast.Type tipo) {
        if (mainFrame != null && mainFrame.isVisible()) {
            ModernToast.show(mainFrame, mensaje, tipo, false);
        } else {
            System.out.println("ALERTA: " + mensaje);
        }
    }
}
