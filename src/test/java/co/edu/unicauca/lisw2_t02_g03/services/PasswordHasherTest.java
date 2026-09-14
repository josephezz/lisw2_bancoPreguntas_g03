package co.edu.unicauca.lisw2_t02_g03.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    private final PasswordHasher passwordHasher =
            new PasswordHasher();

    @Test
    void hashDebeGenerarUnaContraseñaDiferenteAlTextoOriginal() {

        String password = "Abcdef1!";

        String hashedPassword =
                passwordHasher.hash(password);

        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
    }

    @Test
    void verifyDebeRetornarTrueCuandoLaContraseñaEsCorrecta() {

        String password = "Abcdef1!";

        String hashedPassword =
                passwordHasher.hash(password);

        boolean resultado =
                passwordHasher.verify(password, hashedPassword);

        assertTrue(resultado);
    }

    @Test
    void verifyDebeRetornarFalseCuandoLaContraseñaEsIncorrecta() {

        String password = "Abcdef1!";
        String passwordIncorrecta = "Otra123!";

        String hashedPassword =
                passwordHasher.hash(password);

        boolean resultado =
                passwordHasher.verify(
                        passwordIncorrecta,
                        hashedPassword
                );

        assertFalse(resultado);
    }
}