package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.MicroKernel.QuestionMicroKernel;
import co.edu.unicauca.lisw2_t02_g03.domain.EstadisticasPreguntas;
import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.PreguntaService;
import co.edu.unicauca.lisw2_t02_g03.domain.ResultadoCambioEstado;
import co.edu.unicauca.lisw2_t02_g03.infra.Observer;
import co.edu.unicauca.lisw2_t02_g03.pipeline.QuestionPipeline;
import co.edu.unicauca.lisw2_t02_g03.pipeline.ValidationException;
import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionPlugin;
import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionRequest;

import java.util.List;
import java.util.Optional;

/**
 * Controlador MVC: coordina acciones de la vista y delega reglas al servicio de dominio.
 * Integra el Microkernel, los plugins y el pipeline de validación.
 */
public class PreguntasController {

    private final PreguntaService service;
    private final QuestionMicroKernel microKernel;
    private final QuestionPipeline pipeline;

    public PreguntasController(PreguntaService service, QuestionMicroKernel microKernel, QuestionPipeline pipeline) {
        if (service == null) {
            throw new IllegalArgumentException("El servicio de preguntas es obligatorio.");
        }
        this.service = service;
        this.microKernel = microKernel;
        this.pipeline = pipeline;
    }

    /**
     * Constructor de compatibilidad (sin microkernel ni pipeline).
     */
    public PreguntasController(PreguntaService service) {
        this(service, null, null);
    }

    // ============================
    // Consultas
    // ============================

    public List<Pregunta> listarPreguntas() {
        return service.listarPreguntas();
    }

    public Optional<Pregunta> buscarPregunta(String id) {
        return service.buscarPregunta(id);
    }

    public List<Pregunta> listarPorEstado(EstadoPregunta estado) {
        return service.listarPorEstado(estado);
    }

    public List<Pregunta> listarPorAutor(String autorLogin) {
        return service.listarPorAutor(autorLogin);
    }

    // ============================
    // Cambio de estado
    // ============================

    public ResultadoCambioEstado cambiarEstado(Pregunta seleccionada, EstadoPregunta nuevoEstado) {
        if (seleccionada == null) {
            return ResultadoCambioEstado.ENTRADA_INVALIDA;
        }
        return service.cambiarEstado(seleccionada.getId(), nuevoEstado);
    }

    public ResultadoCambioEstado enviarARevision(String id) {
        return service.enviarARevision(id);
    }

    public ResultadoCambioEstado aprobarPregunta(String id) {
        return service.aprobarPregunta(id);
    }

    public ResultadoCambioEstado rechazarPregunta(String id, String observaciones) {
        return service.rechazarPregunta(id, observaciones);
    }

    // ============================
    // Creación y generación de preguntas
    // ============================

    /**
     * Guarda una pregunta directamente en el banco.
     */
    public boolean guardarPregunta(Pregunta pregunta) {
        return service.guardarPregunta(pregunta);
    }

    /**
     * Genera una pregunta usando un plugin del microkernel y la valida con el pipeline.
     *
     * @param tipo tipo de pregunta.
     * @param request datos para la generación.
     * @return pregunta generada y validada.
     * @throws ValidationException si la pregunta no pasa el pipeline.
     * @throws IllegalArgumentException si no hay plugin para el tipo.
     * @throws IllegalStateException si el microkernel no está configurado.
     */
    public Pregunta generarConPlugin(String tipo, QuestionRequest request) {
        if (microKernel == null) {
            throw new IllegalStateException("El microkernel no está configurado.");
        }
        Pregunta generada = microKernel.generarPregunta(tipo, request);

        if (pipeline != null) {
            generada = pipeline.ejecutar(generada);
        }
        return generada;
    }

    /**
     * Valida una pregunta mediante el pipeline sin guardarla.
     *
     * @throws ValidationException si la pregunta no pasa algún filtro.
     */
    public Pregunta validarPregunta(Pregunta pregunta) {
        if (pipeline == null) {
            return pregunta;
        }
        return pipeline.ejecutar(pregunta);
    }

    // ============================
    // Plugins
    // ============================

    /**
     * Lista los plugins activos disponibles.
     */
    public List<QuestionPlugin> obtenerPluginsActivos() {
        if (microKernel == null) {
            return List.of();
        }
        return microKernel.obtenerPluginsActivos();
    }

    /**
     * Lista todos los plugins registrados.
     */
    public List<QuestionPlugin> obtenerTodosLosPlugins() {
        if (microKernel == null) {
            return List.of();
        }
        return microKernel.obtenerTodosLosPlugins();
    }

    public boolean activarPlugin(String nombre) {
        return microKernel != null && microKernel.activarPlugin(nombre);
    }

    public boolean desactivarPlugin(String nombre) {
        return microKernel != null && microKernel.desactivarPlugin(nombre);
    }

    // ============================
    // Estadísticas y Observadores
    // ============================

    public EstadisticasPreguntas obtenerEstadisticas() {
        return service.obtenerEstadisticas();
    }

    public void registrarObservador(Observer<EstadisticasPreguntas> observador) {
        service.registrarObservador(observador);
    }

    public void retirarObservador(Observer<EstadisticasPreguntas> observador) {
        service.retirarObservador(observador);
    }

    public void refrescarVistasObservadoras() {
        service.notificarObservadores();
    }
}
