package co.edu.unicauca.lisw2_t02_g03.services;

import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Rol;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServicesTest {

    private FakeUsuarioRepository repository;
    private AuthServices authServices;

    @BeforeEach
    void setUp() {

        repository = new FakeUsuarioRepository();

        PasswordHasher hasher =
                new PasswordHasher();

        authServices = new AuthServices(
                repository,
                hasher::verify
        );
    }

    private void registrarUsuarioActivo() {

        PasswordHasher hasher =
                new PasswordHasher();

        Usuario usuario = new Usuario(
                "juan",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                hasher.hash("Abcdef1!")
        );

        repository.save(usuario);
    }

    @Test
    void loginDebeFuncionarConCredencialesCorrectas() {

        registrarUsuarioActivo();

        Usuario resultado =
                authServices.iniciarSesion(
                        "juan",
                        "Abcdef1!"
                );

        assertNotNull(resultado);
        assertEquals("juan", resultado.getLogin());
    }

    @Test
    void loginDebeFallarConPasswordIncorrecta() {

        registrarUsuarioActivo();

        Usuario resultado =
                authServices.iniciarSesion(
                        "juan",
                        "Incorrecta1!"
                );

        assertNull(resultado);
    }

    @Test
    void loginDebeFallarSiUsuarioNoExiste() {

        Usuario resultado =
                authServices.iniciarSesion(
                        "usuarioInexistente",
                        "Abcdef1!"
                );

        assertNull(resultado);
    }

    @Test
    void usuarioInactivoNoDebePoderIniciarSesion() {

        PasswordHasher hasher =
                new PasswordHasher();

        Usuario usuario = new Usuario(
                "juan",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.INACTIVO,
                hasher.hash("Abcdef1!")
        );

        repository.save(usuario);

        Usuario resultado =
                authServices.iniciarSesion(
                        "juan",
                        "Abcdef1!"
                );

        assertNull(resultado);
    }
}