package negocio;

import modelo.Usuario;
import java.util.ArrayList;

public class GestorUsuario {

    private ArrayList<Usuario> usuarios;
    private Usuario usuarioActivo;

    public GestorUsuario() {
        usuarios = ManejadorDatos.cargarUsuarios();
    }

    public boolean correoExiste(String correo) {
        if (correo == null) return false;
        return usuarios.stream().anyMatch(u -> u.getCorreoElectronico().equalsIgnoreCase(correo.trim()));
    }

    public String calcularPerfil(int materias, int horas) {
        if (materias <= 0) return "INDEFINIDO";
        int promedio = horas / materias;
        if (promedio >= 5) return "EFICIENTE";
        if (promedio >= 3) return "RESTRINGIDO";
        return "SATURADO";
    }

    public String registrarUsuario(Usuario usuario) throws IllegalArgumentException {
        if (correoExiste(usuario.getCorreoElectronico())) {
            throw new IllegalArgumentException("El correo ya está registrado.");
        }
        if (usuario.getContrasena() == null || usuario.getContrasena().length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener mínimo 8 caracteres.");
        }
        if (usuario.getCantidadMaterias() <= 0) {
            throw new IllegalArgumentException("La cantidad de materias debe ser mayor a cero.");
        }
        if (usuario.getHorasDisponibles() < 0) {
            throw new IllegalArgumentException("Las horas disponibles no pueden ser negativas.");
        }
        if (!usuario.getCorreoElectronico().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }

        String perfil = calcularPerfil(usuario.getCantidadMaterias(), usuario.getHorasDisponibles());
        usuario.setPerfilViabilidad(perfil);
        usuarios.add(usuario);
        
        ManejadorDatos.guardarUsuarios(usuarios);
        return "Usuario registrado correctamente. Perfil: " + perfil;
    }

    public Usuario iniciarSesion(String correo, String contrasena) throws IllegalArgumentException {
        if (correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("Correo y contraseña son obligatorios.");
        }

        for (Usuario usuario : usuarios) {
            if (usuario.getCorreoElectronico().equalsIgnoreCase(correo.trim())) {
                if (usuario.isCuentaBloqueada()) {
                    throw new IllegalArgumentException("La cuenta está bloqueada por demasiados intentos fallidos.");
                }
                if (usuario.getContrasena().equals(contrasena)) {
                    usuario.setIntentosFallidos(0);
                    usuarioActivo = usuario;
                    ManejadorDatos.guardarUsuarios(usuarios);
                    return usuario;
                } else {
                    usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);
                    if (usuario.getIntentosFallidos() >= 3) {
                        usuario.setCuentaBloqueada(true);
                        ManejadorDatos.guardarUsuarios(usuarios);
                        throw new IllegalArgumentException("Cuenta bloqueada por múltiples intentos fallidos.");
                    }
                    ManejadorDatos.guardarUsuarios(usuarios);
                    throw new IllegalArgumentException("Contraseña incorrecta. Intentos restantes: " + (3 - usuario.getIntentosFallidos()));
                }
            }
        }
        throw new IllegalArgumentException("El usuario no existe.");
    }
    
    public void cerrarSesion() {
        if (usuarioActivo != null) {
            ManejadorDatos.guardarUsuarios(usuarios);
            usuarioActivo = null;
        }
    }

    public Usuario getUsuarioActivo() {
        return usuarioActivo;
    }
    
    public void setUsuarioActivo(Usuario usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
    
    public void guardarCambios() {
        ManejadorDatos.guardarUsuarios(usuarios);
    }
}