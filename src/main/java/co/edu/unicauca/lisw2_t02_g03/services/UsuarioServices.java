package co.edu.unicauca.lisw2_t02_g03.services;

import co.edu.unicauca.lisw2_t02_g03.access.InterfaceUsuarioRepository;
import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


public class UsuarioServices {

    private final InterfaceUsuarioRepository usuarioRepository;
    private final Predicate<String> passwordValidator;
    private final Function<String, String> passwordHasher;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param usuarioRepository repositorio utilizado para persistir usuarios.
     * @param passwordValidator estrategia para validar contraseñas.
     * @param passwordHasher estrategia para generar hashes de contraseñas.
     */
    public UsuarioServices(
            InterfaceUsuarioRepository usuarioRepository,
            Predicate<String> passwordValidator,
            Function<String, String> passwordHasher) {

        if (usuarioRepository == null) {
            throw new IllegalArgumentException(
                    "El repositorio de usuarios no puede ser null."
            );
        }

        if (passwordValidator == null) {
            throw new IllegalArgumentException(
                    "El validador de contraseñas no puede ser null."
            );
        }

        if (passwordHasher == null) {
            throw new IllegalArgumentException(
                    "El hasher de contraseñas no puede ser null."
            );
        }

        this.usuarioRepository = usuarioRepository;
        this.passwordValidator = passwordValidator;
        this.passwordHasher = passwordHasher;
    }

    /**
     * Crea un nuevo usuario aplicando las reglas de negocio.
     *
     * @param usuario usuario que se desea crear.
     * @return true si el usuario fue creado correctamente.
     */
    public boolean crearUsuario(Usuario usuario) {

        if (!datosUsuarioValidos(usuario)) {
            return false;
        }

        if (usuarioRepository.findByLogin(usuario.getLogin()) != null) {
            return false;
        }

        if (!passwordValidator.test(usuario.getContrasena())) {
            return false;
        }

        String passwordHash =
                passwordHasher.apply(usuario.getContrasena());

        usuario.setContrasena(passwordHash);

        return usuarioRepository.save(usuario);
    }

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de usuarios.
     */
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.list();
    }

    /**
     * Busca un usuario por su login.
     *
     * @param login login del usuario.
     * @return usuario encontrado o null si no existe.
     */
    public Usuario buscarPorLogin(String login) {

        if (login == null || login.isBlank()) {
            return null;
        }

        return usuarioRepository.findByLogin(login);
    }

    /**
     * Actualiza el estado de un usuario.
     *
     * @param login login del usuario.
     * @param estado nuevo estado.
     * @return true si se realizó la actualización.
     */
    public boolean actualizarEstado(
            String login,
            EstadoUsuario estado) {

        if (login == null ||
                login.isBlank() ||
                estado == null) {

            return false;
        }

        return usuarioRepository.updateEstado(
                estado,
                login
        );
    }

  
    private boolean datosUsuarioValidos(Usuario usuario) {

        if (usuario == null) {
            return false;
        }

        if (usuario.getLogin() == null ||
                usuario.getLogin().isBlank()) {

            return false;
        }

        if (usuario.getNombreCompleto() == null ||
                usuario.getNombreCompleto().isBlank()) {

            return false;
        }

        if (usuario.getRol() == null) {
            return false;
        }

        if (usuario.getEstado() == null) {
            return false;
        }

        if (usuario.getContrasena() == null ||
                usuario.getContrasena().isBlank()) {

            return false;
        }

        return true;
    }
}
