package co.edu.unicauca.lisw2_t02_g03.MicroKernel;

import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.PreguntaService;
import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionPlugin;
import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionRequest;

import java.util.List;
import java.util.Map;

/**
 * Núcleo central de la arquitectura Microkernel.
 * <p>
 * Coordina el banco de preguntas, el sistema de plugins y la generación de preguntas.
 * El microkernel NO conoce los detalles internos de cada plugin; interactúa
 * exclusivamente a través del contrato {@link QuestionPlugin}.
 */
public class QuestionMicroKernel {

    private final PreguntaService preguntaService;
    private final PluginManager pluginManager;

    public QuestionMicroKernel(PreguntaService preguntaService, PluginManager pluginManager) {
        if (preguntaService == null || pluginManager == null) {
            throw new IllegalArgumentException("El servicio de preguntas y el plugin manager son obligatorios.");
        }
        this.preguntaService = preguntaService;
        this.pluginManager = pluginManager;
    }

    /**
     * Inicializa el microkernel cargando plugins desde la configuración.
     *
     * @return cantidad de plugins cargados.
     */
    public int inicializar() {
        return pluginManager.cargarYRegistrar();
    }

    // ============================
    // Generación de preguntas via plugins
    // ============================

    /**
     * Genera una pregunta usando el plugin adecuado según el tipo.
     *
     * @param tipo tipo de pregunta (e.g., "SELECCION_MULTIPLE").
     * @param request datos para generar la pregunta.
     * @return pregunta generada.
     * @throws IllegalArgumentException si no hay un plugin activo para el tipo.
     */
    public Pregunta generarPregunta(String tipo, QuestionRequest request) {
        QuestionPlugin plugin = pluginManager.buscarPluginPorTipo(tipo);
        if (plugin == null) {
            throw new IllegalArgumentException(
                    "No hay un plugin activo que soporte el tipo: " + tipo);
        }
        return plugin.generate(request);
    }

    // ============================
    // Delegación al servicio de preguntas
    // ============================

    public PreguntaService getPreguntaService() {
        return preguntaService;
    }

    // ============================
    // Delegación al plugin manager
    // ============================

    public List<QuestionPlugin> obtenerPluginsActivos() {
        return pluginManager.obtenerPluginsActivos();
    }

    public List<QuestionPlugin> obtenerTodosLosPlugins() {
        return pluginManager.obtenerTodosLosPlugins();
    }

    public Map<String, Boolean> obtenerEstadosPlugins() {
        return pluginManager.obtenerEstados();
    }

    public boolean activarPlugin(String nombre) {
        return pluginManager.activarPlugin(nombre);
    }

    public boolean desactivarPlugin(String nombre) {
        return pluginManager.desactivarPlugin(nombre);
    }

    public void registrarPlugin(QuestionPlugin plugin) {
        pluginManager.registrarPlugin(plugin);
    }
}
