package co.edu.unicauca.lisw2_t02_g03.MicroKernel;

import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionPlugin;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registro centralizado de plugins. Almacena cada plugin junto con su estado
 * (activo/inactivo) y permite consultar los plugins disponibles.
 */
public class PluginRegistry {

    private final Map<String, PluginEntry> plugins = new LinkedHashMap<>();

    /**
     * Registra un plugin en el registro. Por defecto queda activo.
     *
     * @param plugin plugin a registrar.
     */
    public void registrar(QuestionPlugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("El plugin no puede ser null.");
        }
        plugins.put(plugin.getName(), new PluginEntry(plugin, true));
    }

    /**
     * Activa un plugin previamente registrado.
     */
    public boolean activar(String nombre) {
        PluginEntry entry = plugins.get(nombre);
        if (entry == null) {
            return false;
        }
        entry.activo = true;
        return true;
    }

    /**
     * Desactiva un plugin sin eliminarlo del registro.
     */
    public boolean desactivar(String nombre) {
        PluginEntry entry = plugins.get(nombre);
        if (entry == null) {
            return false;
        }
        entry.activo = false;
        return true;
    }

    /**
     * Devuelve todos los plugins activos.
     */
    public List<QuestionPlugin> obtenerActivos() {
        return plugins.values().stream()
                .filter(e -> e.activo)
                .map(e -> e.plugin)
                .toList();
    }

    /**
     * Devuelve todos los plugins registrados (activos e inactivos).
     */
    public List<QuestionPlugin> obtenerTodos() {
        return plugins.values().stream()
                .map(e -> e.plugin)
                .toList();
    }

    /**
     * Verifica si un plugin está activo.
     */
    public boolean estaActivo(String nombre) {
        PluginEntry entry = plugins.get(nombre);
        return entry != null && entry.activo;
    }

    /**
     * Devuelve un mapa inmutable con nombre → activo para UI.
     */
    public Map<String, Boolean> obtenerEstados() {
        Map<String, Boolean> estados = new LinkedHashMap<>();
        for (Map.Entry<String, PluginEntry> entry : plugins.entrySet()) {
            estados.put(entry.getKey(), entry.getValue().activo);
        }
        return Collections.unmodifiableMap(estados);
    }

    /**
     * Cantidad de plugins registrados.
     */
    public int size() {
        return plugins.size();
    }

    // ============================
    // Estructura interna
    // ============================

    private static class PluginEntry {
        final QuestionPlugin plugin;
        boolean activo;

        PluginEntry(QuestionPlugin plugin, boolean activo) {
            this.plugin = plugin;
            this.activo = activo;
        }
    }
}
