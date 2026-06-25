package modelo;

import java.util.ArrayList;

public class Usuario {

    private String nombreCompleto;
    private String correoElectronico;
    private String carrera;
    private int semestre;
    private int cantidadMaterias;
    private int horasDisponibles;
    private String contrasena;
    private String perfilViabilidad;
    private int intentosFallidos;
    private boolean cuentaBloqueada;

    // RELACIONES
    private ArrayList<Materia> materias;
    private ArrayList<RutinasEstudio> rutinas;

    public Usuario(String nombreCompleto,
                   String correoElectronico,
                   String carrera,
                   int semestre,
                   int cantidadMaterias,
                   int horasDisponibles,
                   String contrasena,
                   String perfilViabilidad) {

        this.nombreCompleto = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.carrera = carrera;
        this.semestre = semestre;
        this.cantidadMaterias = cantidadMaterias;
        this.horasDisponibles = horasDisponibles;
        this.contrasena = contrasena;
        this.perfilViabilidad = perfilViabilidad;

        this.intentosFallidos = 0;
        this.cuentaBloqueada = false;

        // Inicializar relaciones
        this.materias = new ArrayList<>();
        this.rutinas = new ArrayList<>();
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getCantidadMaterias() {
        return cantidadMaterias;
    }

    public void setCantidadMaterias(int cantidadMaterias) {
        this.cantidadMaterias = cantidadMaterias;
    }

    public int getHorasDisponibles() {
        return horasDisponibles;
    }

    public void setHorasDisponibles(int horasDisponibles) {
        this.horasDisponibles = horasDisponibles;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getPerfilViabilidad() {
        return perfilViabilidad;
    }

    public void setPerfilViabilidad(String perfilViabilidad) {
        this.perfilViabilidad = perfilViabilidad;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(int intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public boolean isCuentaBloqueada() {
        return cuentaBloqueada;
    }

    public void setCuentaBloqueada(boolean cuentaBloqueada) {
        this.cuentaBloqueada = cuentaBloqueada;
    }

    // GETTERS Y SETTERS DE RELACIONES

    public ArrayList<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(ArrayList<Materia> materias) {
        this.materias = materias;
    }

    public ArrayList<RutinasEstudio> getRutinas() {
        return rutinas;
    }

    public void setRutinas(ArrayList<RutinasEstudio> rutinas) {
        this.rutinas = rutinas;
    }
}