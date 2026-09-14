package co.edu.unicauca.lisw2_t02_g03.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpcionesPreguntaTest {

    @Test
    void debeAceptarExactamenteCuatroOpcionesYFormatearlasComoAD() {
        OpcionesPregunta opciones = OpcionesPregunta.de("Uno", "Dos", "Tres", "Cuatro");

        assertEquals(4, opciones.comoLista().size());
        assertTrue(opciones.comoTexto().contains("A) Uno"));
        assertTrue(opciones.comoTexto().contains("D) Cuatro"));
    }

    @Test
    void debeRechazarCantidadDistintaDeCuatroOpciones() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new OpcionesPregunta(List.of("A", "B", "C")));
    }

    @Test
    void debeRechazarOpcionesVacias() {
        assertThrows(
                IllegalArgumentException.class,
                () -> OpcionesPregunta.de("Uno", " ", "Tres", "Cuatro"));
    }
}
