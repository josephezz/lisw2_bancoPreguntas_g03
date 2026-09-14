package co.edu.unicauca.lisw2_t02_g03.access;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.OpcionesPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PreguntaImplRepositoryTest {

    @Test
    void debeListarYBuscarPorIdentificador() {
        Pregunta pregunta = pregunta("P-001", EstadoPregunta.BORRADOR);
        PreguntaImplRepository repository = new PreguntaImplRepository(List.of(pregunta));

        assertEquals(1, repository.listar().size());
        assertTrue(repository.buscarPorId("P-001").isPresent());
        assertTrue(repository.buscarPorId("NO-EXISTE").isEmpty());
    }

    @Test
    void debeActualizarSoloPreguntasExistentes() {
        Pregunta original = pregunta("P-001", EstadoPregunta.BORRADOR);
        PreguntaImplRepository repository = new PreguntaImplRepository(List.of(original));

        assertTrue(repository.actualizar(original.conEstado(EstadoPregunta.ELIMINADA)));
        assertEquals(EstadoPregunta.ELIMINADA, repository.buscarPorId("P-001").orElseThrow().getEstado());
        assertFalse(repository.actualizar(pregunta("P-999", EstadoPregunta.BORRADOR)));
    }

    @Test
    void debeRechazarIdentificadoresDuplicadosEnLaCargaInicial() {
        Pregunta primera = pregunta("P-001", EstadoPregunta.BORRADOR);
        Pregunta segunda = pregunta("P-001", EstadoPregunta.PENDIENTE_REVISION);

        assertThrows(
                IllegalArgumentException.class,
                () -> new PreguntaImplRepository(List.of(primera, segunda)));
    }

    private static Pregunta pregunta(String id, EstadoPregunta estado) {
        return new Pregunta(
                id,
                "Pregunta " + id,
                "Enunciado",
                OpcionesPregunta.de("Uno", "Dos", "Tres", "Cuatro"),
                "A",
                estado);
    }
}
