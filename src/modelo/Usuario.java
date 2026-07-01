package modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private boolean modoOscuro;

    // RELACIONES
    private ArrayList<Materia> materias;
    private ArrayList<Tarea> tareas;
    private ArrayList<RutinasEstudio> rutinas;
    
    private ProgresoAcademico progreso;

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
        this.tareas = new ArrayList<>();
        this.rutinas = new ArrayList<>();
        this.progreso = new ProgresoAcademico();
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }

    public int getCantidadMaterias() { return cantidadMaterias; }
    public void setCantidadMaterias(int cantidadMaterias) { this.cantidadMaterias = cantidadMaterias; }

    public int getHorasDisponibles() { return horasDisponibles; }
    public void setHorasDisponibles(int horasDisponibles) { this.horasDisponibles = horasDisponibles; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getPerfilViabilidad() { return perfilViabilidad; }
    public void setPerfilViabilidad(String perfilViabilidad) { this.perfilViabilidad = perfilViabilidad; }

    public int getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    public boolean isCuentaBloqueada() { return cuentaBloqueada; }
    public void setCuentaBloqueada(boolean cuentaBloqueada) { this.cuentaBloqueada = cuentaBloqueada; }

    public boolean isModoOscuro() { return modoOscuro; }
    public void setModoOscuro(boolean modoOscuro) { this.modoOscuro = modoOscuro; }

    // GETTERS Y SETTERS DE RELACIONES
    public ArrayList<Materia> getMaterias() { return materias; }
    public void setMaterias(ArrayList<Materia> materias) { this.materias = materias; }

    public ArrayList<Tarea> getTareas() { return tareas; }
    public void setTareas(ArrayList<Tarea> tareas) { this.tareas = tareas; }

    public ArrayList<RutinasEstudio> getRutinas() { return rutinas; }
    public void setRutinas(ArrayList<RutinasEstudio> rutinas) { this.rutinas = rutinas; }

    public ProgresoAcademico getProgreso() { 
        if (progreso == null) progreso = new ProgresoAcademico();
        return progreso; 
    }
}