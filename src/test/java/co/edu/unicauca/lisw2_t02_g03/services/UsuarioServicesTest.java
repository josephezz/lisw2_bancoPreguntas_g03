package co.edu.unicauca.lisw2_t02_g03.services;

import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Rol;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServicesTest {

    private FakeUsuarioRepository repository;
    private UsuarioServices usuarioServices;

    @BeforeEach
    void setUp() {

        repository = new FakeUsuarioRepository();

        PasswordValidator validator =
                new PasswordValidator();

        PasswordHasher hasher =
                new PasswordHasher();

        usuarioServices = new UsuarioServices(
                repository,
                validator::isValid,
                hasher::hash
        );
    }

    @Test
    void debeRegistrarUsuarioValido() {

        Usuario usuario = new Usuario(
                "juan",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                "Abcdef1!"
        );

        boolean resultado =
                usuarioServices.crearUsuario(usuario);

        assertTrue(resultado);
        assertNotNull(repository.findByLogin("juan"));
    }

    @Test
    void noDebeRegistrarUsuarioNulo() {

        boolean resultado =
                usuarioServices.crearUsuario(null);

        assertFalse(resultado);
    }

    @Test
    void noDebeRegistrarUsuarioConPasswordInvalida() {

        Usuario usuario = new Usuario(
                "juan",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                "abc"
        );

        boolean resultado =
                usuarioServices.crearUsuario(usuario);

        assertFalse(resultado);
    }

    @Test
    void noDebeRegistrarUsuarioDuplicado() {

        Usuario usuario1 = new Usuario(
                "juan",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                "Abcdef1!"
        );

        Usuario usuario2 = new Usuario(
                "juan",
                "Otro Nombre",
                Rol.DOCENTE,
                EstadoUsuario.ACTIVO,
                "Abcdef2!"
        );

        assertTrue(usuarioServices.crearUsuario(usuario1));

        assertFalse(usuarioServices.crearUsuario(usuario2));
    }

    @Test
    void passwordDebeQuedarHasheada() {

        Usuario usuario = new Usuario(
                "juan",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                "Abcdef1!"
        );

        boolean resultado =
                usuarioServices.crearUsuario(usuario);

        assertTrue(resultado);

        Usuario guardado =
                repository.findByLogin("juan");

        assertNotNull(guardado);

        assertNotEquals(
                "Abcdef1!",
                guardado.getContrasena()
        );
    }
}