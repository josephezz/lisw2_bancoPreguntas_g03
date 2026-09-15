package co.edu.unicauca.lisw2_t02_g03.MicroKernel;

import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Carga dinámica de plugins mediante reflexión.
 * <p>
 * Lee el archivo {@code plugins.properties} del classpath, donde cada clave
 * es un nombre lógico y el valor es el nombre completo de la clase (FQCN).
 * Instancia cada clase usando {@code Class.forName()} y
 * {@code getDeclaredConstructor().newInstance()}.
 */
public class PluginLoader {

    private static final Logger LOGGER = Logger.getLogger(PluginLoader.class.getName());
    private static final String ARCHIVO_CONFIGURACION = "plugins.properties";

    /**
     * Carga todos los plugins definidos en {@code plugins.properties}.
     *
     * @return lista de plugins instanciados exitosamente.
     */
    public List<QuestionPlugin> cargarPlugins() {
        Properties props = new Properties();
        List<QuestionPlugin> plugins = new ArrayList<>();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            if (input == null) {
                LOGGER.warning("No se encontró el archivo de configuración: " + ARCHIVO_CONFIGURACION);
                return plugins;
            }
            props.load(input);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al leer " + ARCHIVO_CONFIGURACION, e);
            return plugins;
        }

        for (String nombre : props.stringPropertyNames()) {
            String className = props.getProperty(nombre).trim();
            try {
                QuestionPlugin plugin = cargarPlugin(className);
                plugins.add(plugin);
                LOGGER.info("Plugin cargado: " + plugin.getName() + " (" + className + ")");
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "No se pudo cargar el plugin '" + nombre + "': " + className, e);
            }
        }

        return plugins;
    }

    /**
     * Carga un plugin individual por nombre de clase completo usando reflexión.
     *
     * @param className nombre completo de la clase (FQCN).
     * @return instancia del plugin.
     */
    public QuestionPlugin cargarPlugin(String className) throws ReflectiveOperationException {
        Class<?> clazz = Class.forName(className);

        if (!QuestionPlugin.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(
                    "La clase " + className + " no implementa QuestionPlugin.");
        }

        return (QuestionPlugin) clazz.getDeclaredConstructor().newInstance();
    }
}
