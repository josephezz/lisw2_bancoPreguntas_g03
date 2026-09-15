package co.edu.unicauca.lisw2_t02_g03.MicroKernel;

import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionPlugin;

import java.util.List;
import java.util.Map;

/**
 * Gestiona el ciclo de vida de los plugins: carga, registro, activación y desactivación.
 * Coordina entre {@link PluginLoader} y {@link PluginRegistry}.
 */
public class PluginManager {

    private final PluginLoader loader;
    private final PluginRegistry registry;

    public PluginManager(PluginLoader loader, PluginRegistry registry) {
        if (loader == null || registry == null) {
            throw new IllegalArgumentException("El loader y el registry son obligatorios.");
        }
        this.loader = loader;
        this.registry = registry;
    }

    /**
     * Carga los plugins desde plugins.properties y los registra automáticamente.
     *
     * @return cantidad de plugins cargados exitosamente.
     */
    public int cargarYRegistrar() {
        List<QuestionPlugin> plugins = loader.cargarPlugins();
        for (QuestionPlugin plugin : plugins) {
            registry.registrar(plugin);
        }
        return plugins.size();
    }

    /**
     * Registra un plugin manualmente (para pruebas o extensión programática).
     */
    public void registrarPlugin(QuestionPlugin plugin) {
        registry.registrar(plugin);
    }

    /**
     * Activa un plugin por nombre.
     */
    public boolean activarPlugin(String nombre) {
        return registry.activar(nombre);
    }

    /**
     * Desactiva un plugin por nombre.
     */
    public boolean desactivarPlugin(String nombre) {
        return registry.desactivar(nombre);
    }

    /**
     * Devuelve los plugins activos.
     */
    public List<QuestionPlugin> obtenerPluginsActivos() {
        return registry.obtenerActivos();
    }

    /**
     * Devuelve todos los plugins registrados.
     */
    public List<QuestionPlugin> obtenerTodosLosPlugins() {
        return registry.obtenerTodos();
    }

    /**
     * Devuelve el estado (activo/inactivo) de todos los plugins.
     */
    public Map<String, Boolean> obtenerEstados() {
        return registry.obtenerEstados();
    }

    /**
     * Busca el primer plugin activo que soporte un tipo dado.
     *
     * @param tipo tipo de pregunta.
     * @return plugin encontrado o {@code null} si ninguno soporta ese tipo.
     */
    public QuestionPlugin buscarPluginPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return null;
        }
        return registry.obtenerActivos().stream()
                .filter(p -> p.supports(tipo))
                .findFirst()
                .orElse(null);
    }
}
