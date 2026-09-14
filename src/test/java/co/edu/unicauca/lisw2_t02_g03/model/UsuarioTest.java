package co.edu.unicauca.lisw2_t02_g03.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void usuarioDebeGuardarCorrectamenteSusDatos() {

        Usuario usuario = new Usuario(
                "juan123",
                "Juan Pérez",
                Rol.ESTUDIANTE,
                EstadoUsuario.ACTIVO,
                "Abcdef1!"
        );

        assertEquals("juan123", usuario.getLogin());
        assertEquals("Juan Pérez", usuario.getNombreCompleto());
        assertEquals(Rol.ESTUDIANTE, usuario.getRol());
        assertEquals(EstadoUsuario.ACTIVO, usuario.getEstado());
        assertEquals("Abcdef1!", usuario.getContrasena());
    }

    @Test
    void settersDebenModificarLosDatosDelUsuario() {

        Usuario usuario = new Usuario();

        usuario.setLogin("maria123");
        usuario.setNombreCompleto("María López");
        usuario.setRol(Rol.DOCENTE);
        usuario.setEstado(EstadoUsuario.INACTIVO);
        usuario.setContrasena("Nueva123!");

        assertEquals("maria123", usuario.getLogin());
        assertEquals("María López", usuario.getNombreCompleto());
        assertEquals(Rol.DOCENTE, usuario.getRol());
        assertEquals(EstadoUsuario.INACTIVO, usuario.getEstado());
        assertEquals("Nueva123!", usuario.getContrasena());
    }

    @Test
    void usuarioDebeGenerarToString() {

        Usuario usuario = new Usuario(
                "admin",
                "Administrador",
                Rol.ADMINISTRADOR,
                EstadoUsuario.ACTIVO,
                "Admin123!"
        );

        String resultado = usuario.toString();

        assertNotNull(resultado);
        assertTrue(resultado.contains("admin"));
        assertTrue(resultado.contains("Administrador"));
    }
}