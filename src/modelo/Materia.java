package modelo;

public class Materia {
    private String nombreMateria;
    private String nivelDificultad;
    private double calificacionActual;
    private double notaMinimaPersonal;
    private double horasRecomendadas;

    public Materia(String nombreMateria, String nivelDificultad,
                   double calificacionActual, double notaMinimaPersonal) {
        this.nombreMateria = nombreMateria;
        setNivelDificultad(nivelDificultad);
        setCalificacionActual(calificacionActual);
        setNotaMinimaPersonal(notaMinimaPersonal);
    }

    public String getNombreMateria() { return nombreMateria; }
    public void setNombreMateria(String nombreMateria) { this.nombreMateria = nombreMateria; }

    public String getNivelDificultad() { return nivelDificultad; }
    public void setNivelDificultad(String nivelDificultad) {
        if (nivelDificultad.equalsIgnoreCase("alto") ||
                nivelDificultad.equalsIgnoreCase("medio") ||
                nivelDificultad.equalsIgnoreCase("bajo")) {
            this.nivelDificultad = nivelDificultad;
        } else {
            this.nivelDificultad = "medio";
        }
    }

    public double getCalificacionActual() { return calificacionActual; }
    public void setCalificacionActual(double calificacionActual) {
        if (calificacionActual >= 0 && calificacionActual <= 10)
            this.calificacionActual = calificacionActual;
    }

    public double getNotaMinimaPersonal() { return notaMinimaPersonal; }
    public void setNotaMinimaPersonal(double notaMinimaPersonal) {
        if (notaMinimaPersonal > 0 && notaMinimaPersonal <= 10)
            this.notaMinimaPersonal = notaMinimaPersonal;
    }

    public double getHorasRecomendadas() { return horasRecomendadas; }
    public void setHorasRecomendadas(double horasRecomendadas) {
        this.horasRecomendadas = horasRecomendadas;
    }

    public String mostrarDatos() {
        return "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "MATERIA: " + nombreMateria + "\n" +
                "Dificultad: " + nivelDificultad + "\n" +
                "Calificación actual: " + calificacionActual + "\n" +
                "Nota mínima deseada: " + notaMinimaPersonal + "\n" +
                "Horas recomendadas: " + horasRecomendadas + " hrs\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━";
    }
}