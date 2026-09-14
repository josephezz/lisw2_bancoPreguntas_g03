package co.edu.unicauca.lisw2_t02_g03.domain;

import co.edu.unicauca.lisw2_t02_g03.access.PreguntaImplRepository;
import co.edu.unicauca.lisw2_t02_g03.infra.Observer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PreguntaServiceTest {

    private PreguntaImplRepository repository;
    private PreguntaService service;

    @BeforeEach
    void setUp() {
        repository = new PreguntaImplRepository(List.of(
                pregunta("P-001", EstadoPregunta.BORRADOR),
                pregunta("P-002", EstadoPregunta.PENDIENTE_REVISION),
                pregunta("P-003", EstadoPregunta.BORRADOR)));
        service = new PreguntaService(repository);
    }

    @Test
    void debePermitirConsultarYSeleccionarPreguntas() {
        assertEquals(3, service.listarPreguntas().size());
        assertEquals("P-002", service.buscarPregunta("P-002").orElseThrow().getId());
        assertTrue(service.buscarPregunta("P-999").isEmpty());
        assertTrue(service.buscarPregunta(null).isEmpty());
    }

    @Test
    void cambioValidoDebeActualizarYNotificarUnaVez() {
        AtomicInteger notificaciones = new AtomicInteger();
        service.registrarObservador(estadisticas -> notificaciones.incrementAndGet());

        ResultadoCambioEstado resultado = service.cambiarEstado("P-001", EstadoPregunta.ELIMINADA);

        assertEquals(ResultadoCambioEstado.ACTUALIZADO, resultado);
        assertEquals(EstadoPregunta.ELIMINADA, service.buscarPregunta("P-001").orElseThrow().getEstado());
        assertEquals(1, notificaciones.get());
    }

    @Test
    void entradasInvalidasNoDebenAlterarDatosNiNotificar() {
        AtomicInteger notificaciones = new AtomicInteger();
        service.registrarObservador(estadisticas -> notificaciones.incrementAndGet());
        EstadoPregunta estadoInicial = service.buscarPregunta("P-001").orElseThrow().getEstado();

        assertEquals(ResultadoCambioEstado.ENTRADA_INVALIDA, service.cambiarEstado(null, EstadoPregunta.ELIMINADA));
        assertEquals(ResultadoCambioEstado.ENTRADA_INVALIDA, service.cambiarEstado("P-001", null));
        assertEquals(ResultadoCambioEstado.PREGUNTA_NO_ENCONTRADA, service.cambiarEstado("P-999", EstadoPregunta.ELIMINADA));

        assertEquals(estadoInicial, service.buscarPregunta("P-001").orElseThrow().getEstado());
        assertEquals(0, notificaciones.get());
    }

    @Test
    void actualizarAlMismoEstadoDebeSerUnNoOpSinNotificacion() {
        AtomicInteger notificaciones = new AtomicInteger();
        service.registrarObservador(estadisticas -> notificaciones.incrementAndGet());

        ResultadoCambioEstado resultado = service.cambiarEstado("P-001", EstadoPregunta.BORRADOR);

        assertEquals(ResultadoCambioEstado.SIN_CAMBIOS, resultado);
        assertEquals(0, notificaciones.get());
        assertEquals(2, service.obtenerEstadisticas().getConteo(EstadoPregunta.BORRADOR));
    }

    @Test
    void conteosYPorcentajesDebenCambiarDesdeElMismoEstadoDelModelo() {
        EstadisticasPreguntas antes = service.obtenerEstadisticas();
        assertEquals(2, antes.getConteo(EstadoPregunta.BORRADOR));
        assertEquals(1, antes.getConteo(EstadoPregunta.PENDIENTE_REVISION));
        assertEquals(0, antes.getConteo(EstadoPregunta.ELIMINADA));
        assertEquals(66.666, antes.getPorcentaje(EstadoPregunta.BORRADOR), 0.01);
        assertEquals(33.333, antes.getPorcentaje(EstadoPregunta.PENDIENTE_REVISION), 0.01);

        service.cambiarEstado("P-001", EstadoPregunta.ELIMINADA);
        EstadisticasPreguntas despues = service.obtenerEstadisticas();

        assertEquals(1, despues.getConteo(EstadoPregunta.BORRADOR));
        assertEquals(1, despues.getConteo(EstadoPregunta.PENDIENTE_REVISION));
        assertEquals(1, despues.getConteo(EstadoPregunta.ELIMINADA));
        assertEquals(33.333, despues.getPorcentaje(EstadoPregunta.ELIMINADA), 0.01);
        assertEquals(3, despues.getTotal());
    }

    @Test
    void bancoVacioDebeGenerarCerosSinDivisionPorCero() {
        PreguntaService servicioVacio = new PreguntaService(new PreguntaImplRepository(List.of()));

        EstadisticasPreguntas estadisticas = servicioVacio.obtenerEstadisticas();

        assertEquals(0, estadisticas.getTotal());
        for (EstadoPregunta estado : EstadoPregunta.values()) {
            assertEquals(0, estadisticas.getConteo(estado));
            assertEquals(0.0, estadisticas.getPorcentaje(estado));
        }
    }

    @Test
    void debeRegistrarRetirarYNotificarObservadores() {
        AtomicInteger notificaciones = new AtomicInteger();
        Observer<EstadisticasPreguntas> observador = estadisticas -> notificaciones.incrementAndGet();

        service.registrarObservador(observador);
        service.registrarObservador(observador);
        service.notificarObservadores();
        assertEquals(1, notificaciones.get(), "Un observador duplicado no debe registrarse dos veces.");

        service.retirarObservador(observador);
        service.cambiarEstado("P-001", EstadoPregunta.ELIMINADA);
        assertEquals(1, notificaciones.get());
    }

    @Test
    void dosObservadoresDebenRecibirLaMismaFotografiaEstadistica() {
        AtomicReference<EstadisticasPreguntas> primeraVista = new AtomicReference<>();
        AtomicReference<EstadisticasPreguntas> segundaVista = new AtomicReference<>();

        service.registrarObservador(primeraVista::set);
        service.registrarObservador(segundaVista::set);

        service.cambiarEstado("P-002", EstadoPregunta.ELIMINADA);

        assertSame(primeraVista.get(), segundaVista.get());
        assertEquals(3, primeraVista.get().getTotal());
        assertEquals(1, primeraVista.get().getConteo(EstadoPregunta.ELIMINADA));
    }

    @Test
    void preguntaEliminadaDebeConservarseComoRegistroYContarEnEstadisticas() {
        int cantidadInicial = service.listarPreguntas().size();

        service.cambiarEstado("P-001", EstadoPregunta.ELIMINADA);

        assertEquals(cantidadInicial, service.listarPreguntas().size());
        assertTrue(service.buscarPregunta("P-001").isPresent());
        assertEquals(1, service.obtenerEstadisticas().getConteo(EstadoPregunta.ELIMINADA));
        assertFalse(service.listarPreguntas().isEmpty());
    }

    private static Pregunta pregunta(String id, EstadoPregunta estado) {
        return new Pregunta(
                id,
                "Pregunta " + id,
                "Enunciado de prueba",
                OpcionesPregunta.de("Uno", "Dos", "Tres", "Cuatro"),
                "A",
                estado);
    }
}
