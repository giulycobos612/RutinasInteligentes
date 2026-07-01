package modelo;

import java.io.Serializable;

public class Materia implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nombreMateria;
    private String nivelDificultad;
    private double calificacionActual;
    private double notaMinimaPersonal;
    private double horasRecomendadas;

    public Materia(String nombreMateria, String nivelDificultad,
                   double calificacionActual, double notaMinimaPersonal) {
        setNombreMateria(nombreMateria);
        setNivelDificultad(nivelDificultad);
        setCalificacionActual(calificacionActual);
        setNotaMinimaPersonal(notaMinimaPersonal);
    }

    public String getNombreMateria() { return nombreMateria; }
    public void setNombreMateria(String nombreMateria) { 
        if (nombreMateria == null || nombreMateria.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la materia no puede estar vacío.");
        }
        this.nombreMateria = nombreMateria.trim(); 
    }

    public String getNivelDificultad() { return nivelDificultad; }
    public void setNivelDificultad(String nivelDificultad) {
        if (nivelDificultad != null && (nivelDificultad.equalsIgnoreCase("alto") ||
                nivelDificultad.equalsIgnoreCase("medio") ||
                nivelDificultad.equalsIgnoreCase("bajo"))) {
            this.nivelDificultad = nivelDificultad.toLowerCase();
        } else {
            this.nivelDificultad = "medio";
        }
    }

    public double getCalificacionActual() { return calificacionActual; }
    public void setCalificacionActual(double calificacionActual) {
        if (calificacionActual >= 0 && calificacionActual <= 10) {
            this.calificacionActual = calificacionActual;
        } else {
            throw new IllegalArgumentException("La calificación debe estar entre 0 y 10.");
        }
    }

    public double getNotaMinimaPersonal() { return notaMinimaPersonal; }
    public void setNotaMinimaPersonal(double notaMinimaPersonal) {
        if (notaMinimaPersonal > 0 && notaMinimaPersonal <= 10) {
            this.notaMinimaPersonal = notaMinimaPersonal;
        } else {
            throw new IllegalArgumentException("La nota mínima debe estar entre 1 y 10.");
        }
    }

    public double getHorasRecomendadas() { return horasRecomendadas; }
    public void setHorasRecomendadas(double horasRecomendadas) {
        if (horasRecomendadas >= 0) {
            this.horasRecomendadas = horasRecomendadas;
        }
    }

    @Override
    public String toString() {
        return nombreMateria;
    }
}