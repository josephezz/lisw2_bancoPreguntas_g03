package co.edu.unicauca.lisw2_t02_g03.services;

import co.edu.unicauca.lisw2_t02_g03.access.InterfaceUsuarioRepository;
import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;

import java.util.function.BiPredicate;


public class AuthServices {

    private final InterfaceUsuarioRepository usuarioRepository;
    private final BiPredicate<String, String> passwordVerifier;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioRepository repositorio de usuarios.
     * @param passwordVerifier estrategia para verificar contraseñas.
     */
    public AuthServices(
            InterfaceUsuarioRepository usuarioRepository,
            BiPredicate<String, String> passwordVerifier) {

        if (usuarioRepository == null) {
            throw new IllegalArgumentException(
                    "El repositorio de usuarios no puede ser null."
            );
        }

        if (passwordVerifier == null) {
            throw new IllegalArgumentException(
                    "El verificador de contraseñas no puede ser null."
            );
        }

        this.usuarioRepository = usuarioRepository;
        this.passwordVerifier = passwordVerifier;
    }

    /**
     * Autentica un usuario.
     *
     * @param login login ingresado.
     * @param contrasena contraseña ingresada.
     * @return usuario autenticado o null si la autenticación falla.
     */
    public Usuario iniciarSesion(
            String login,
            String contrasena) {

        if (login == null ||
                login.isBlank() ||
                contrasena == null ||
                contrasena.isBlank()) {

            return null;
        }

        Usuario usuario =
                usuarioRepository.findByLogin(login);

        if (usuario == null) {
            return null;
        }

        if (usuario.getEstado() != EstadoUsuario.ACTIVO) {
            return null;
        }

        boolean contrasenaCorrecta =
                passwordVerifier.test(
                        contrasena,
                        usuario.getContrasena()
                );

        if (!contrasenaCorrecta) {
            return null;
        }

        return usuario;
    }
}
