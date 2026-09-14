package co.edu.unicauca.lisw2_t02_g03.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    private final PasswordValidator validator =
            new PasswordValidator();

    @Test
    void passwordValidaDebeRetornarTrue() {

        boolean resultado =
                validator.isValid("Abcdef1!");

        assertTrue(resultado);
    }

    @Test
    void passwordMenorASeisCaracteresDebeRetornarFalse() {

        boolean resultado =
                validator.isValid("Ab1!");

        assertFalse(resultado);
    }

    @Test
    void passwordSinMayusculaDebeRetornarFalse() {

        boolean resultado =
                validator.isValid("abcdef1!");

        assertFalse(resultado);
    }

    @Test
    void passwordSinNumeroDebeRetornarFalse() {

        boolean resultado =
                validator.isValid("Abcdef!");

        assertFalse(resultado);
    }

    @Test
    void passwordSinCaracterEspecialDebeRetornarFalse() {

        boolean resultado =
                validator.isValid("Abcdef1");

        assertFalse(resultado);
    }

    @Test
    void passwordNullDebeRetornarFalse() {

        boolean resultado =
                validator.isValid(null);

        assertFalse(resultado);
    }
}