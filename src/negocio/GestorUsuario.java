package negocio;

import modelo.Usuario;

import java.util.ArrayList;

public class GestorUsuario {

    private ArrayList<Usuario> usuarios;

    public GestorUsuario() {

        usuarios = new ArrayList<>();

    }

    public boolean correoExiste(String correo) {

        for (Usuario usuario : usuarios) {

            if (usuario.getCorreoElectronico().equalsIgnoreCase(correo)) {

                return true;

            }

        }

        return false;

    }

    public String calcularPerfil(int materias, int horas) {

        int promedio = horas / materias;

        if (promedio >= 5) {

            return "EFICIENTE";

        } else if (promedio >= 3) {

            return "RESTRINGIDO";

        } else {

            return "SATURADO";

        }

    }

    public String registrarUsuario(Usuario usuario) {

        // Validar correo repetido
        if (correoExiste(usuario.getCorreoElectronico())) {

            return "Error: el correo ya está registrado.";

        }

        // Validar contraseña
        if (usuario.getContrasena().length() < 8) {

            return "Error: la contraseña debe tener mínimo 8 caracteres.";

        }

        // Validar cantidad de materias
        if (usuario.getCantidadMaterias() <= 0) {

            return "Error: la cantidad de materias debe ser mayor a cero.";

        }

        // Validar horas disponibles
        if (usuario.getHorasDisponibles() < 0) {

            return "Error: las horas disponibles no pueden ser negativas.";

        }

        // Calcular perfil académico
        String perfil = calcularPerfil(
                usuario.getCantidadMaterias(),
                usuario.getHorasDisponibles()
        );

        // Guardar perfil
        usuario.setPerfilViabilidad(perfil);

        // Guardar usuario
        usuarios.add(usuario);

        return "Usuario registrado correctamente. Perfil: " + perfil;

    }

    public Usuario iniciarSesion(String correo, String contrasena) {

        for (Usuario usuario : usuarios) {

            // Buscar correo
            if (usuario.getCorreoElectronico().equalsIgnoreCase(correo)) {

                // Verificar si la cuenta está bloqueada
                if (usuario.isCuentaBloqueada()) {

                    return null;

                }

                // Verificar contraseña
                if (usuario.getContrasena().equals(contrasena)) {

                    // Reiniciar intentos fallidos
                    usuario.setIntentosFallidos(0);

                    return usuario;

                } else {

                    // Aumentar intentos fallidos
                    usuario.setIntentosFallidos(
                            usuario.getIntentosFallidos() + 1
                    );

                    // Bloquear cuenta si llega a 3 intentos
                    if (usuario.getIntentosFallidos() >= 3) {

                        usuario.setCuentaBloqueada(true);

                    }

                }

            }

        }

        return null;

    }

}