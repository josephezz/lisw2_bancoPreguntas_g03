package co.edu.unicauca.lisw2_t02_g03.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PreguntaTest {

    @Test
    void debeCrearPreguntaConsistente() {
        Pregunta pregunta = crearPregunta(EstadoPregunta.BORRADOR);

        assertEquals("P-100", pregunta.getId());
        assertEquals("B", pregunta.getRespuestaCorrecta());
        assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
    }

    @Test
    void debeRechazarRespuestaQueNoPerteneceAAd() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Pregunta(
                        "P-100",
                        "Prueba",
                        "Enunciado",
                        OpcionesPregunta.de("Uno", "Dos", "Tres", "Cuatro"),
                        "E",
                        EstadoPregunta.BORRADOR));
    }

    @Test
    void debeRechazarCamposObligatoriosVacios() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Pregunta(
                        " ",
                        "Prueba",
                        "Enunciado",
                        OpcionesPregunta.de("Uno", "Dos", "Tres", "Cuatro"),
                        "A",
                        EstadoPregunta.BORRADOR));
    }

    @Test
    void conEstadoDebeCrearUnaNuevaEntidadSinAlterarLaOriginal() {
        Pregunta original = crearPregunta(EstadoPregunta.BORRADOR);

        Pregunta actualizada = original.conEstado(EstadoPregunta.ELIMINADA);

        assertNotSame(original, actualizada);
        assertEquals(EstadoPregunta.BORRADOR, original.getEstado());
        assertEquals(EstadoPregunta.ELIMINADA, actualizada.getEstado());
    }

    private static Pregunta crearPregunta(EstadoPregunta estado) {
        return new Pregunta(
                "P-100",
                "Prueba",
                "Enunciado de prueba",
                OpcionesPregunta.de("Uno", "Dos", "Tres", "Cuatro"),
                "B",
                estado);
    }
}
