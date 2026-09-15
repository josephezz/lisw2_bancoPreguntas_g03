package co.edu.unicauca.lisw2_t02_g03.microkernel;

import co.edu.unicauca.lisw2_t02_g03.MicroKernel.PluginLoader;
import co.edu.unicauca.lisw2_t02_g03.MicroKernel.PluginManager;
import co.edu.unicauca.lisw2_t02_g03.MicroKernel.PluginRegistry;
import co.edu.unicauca.lisw2_t02_g03.MicroKernel.QuestionMicroKernel;
import co.edu.unicauca.lisw2_t02_g03.access.PreguntaImplRepository;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.PreguntaService;
import co.edu.unicauca.lisw2_t02_g03.plugins.MultipleChoiceQuestionPlugin;
import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionPlugin;
import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class QuestionMicroKernelTest {

    private QuestionMicroKernel microKernel;
    private PluginManager pluginManager;
    private PluginRegistry pluginRegistry;
    private PluginLoader pluginLoader;
    private PreguntaService preguntaService;

    @BeforeEach
    void setUp() {
        pluginLoader = new PluginLoader();
        pluginRegistry = new PluginRegistry();
        pluginManager = new PluginManager(pluginLoader, pluginRegistry);
        preguntaService = new PreguntaService(new PreguntaImplRepository());
        microKernel = new QuestionMicroKernel(preguntaService, pluginManager);
    }

    @Test
    @DisplayName("Carga dinámica de plugins mediante reflexión desde plugins.properties")
    void testCargaDinamicaPorReflexion() {
        int cargados = microKernel.inicializar();
        assertTrue(cargados >= 3, "Deben cargarse al menos 3 plugins por reflexión");

        List<QuestionPlugin> activos = microKernel.obtenerPluginsActivos();
        assertEquals(cargados, activos.size());

        List<String> nombres = activos.stream().map(QuestionPlugin::getName).toList();
        assertTrue(nombres.contains("Selección Múltiple"));
        assertTrue(nombres.contains("Análisis de Caso"));
        assertTrue(nombres.contains("Multimedia"));
    }

    @Test
    @DisplayName("PluginLoader instancia clase vía reflexión directamente")
    void testPluginLoaderInstanciacionDirecta() throws ReflectiveOperationException {
        QuestionPlugin plugin = pluginLoader.cargarPlugin(
                "co.edu.unicauca.lisw2_t02_g03.plugins.MultipleChoiceQuestionPlugin");
        assertNotNull(plugin);
        assertEquals("Selección Múltiple", plugin.getName());
        assertTrue(plugin.supports("SELECCION_MULTIPLE"));
    }

    @Test
    @DisplayName("PluginLoader lanza excepción al cargar clase que no implementa QuestionPlugin")
    void testPluginLoaderClaseInvalida() {
        assertThrows(IllegalArgumentException.class, () ->
                pluginLoader.cargarPlugin("java.lang.String"));
    }

    @Test
    @DisplayName("PluginLoader lanza excepción para clase inexistente")
    void testPluginLoaderClaseInexistente() {
        assertThrows(ClassNotFoundException.class, () ->
                pluginLoader.cargarPlugin("co.edu.unicauca.inexistente.FakePlugin"));
    }

    @Test
    @DisplayName("Registro, activación y desactivación de plugins en el Microkernel")
    void testActivacionYDesactivacionPlugins() {
        microKernel.inicializar();
        String pluginName = "Selección Múltiple";

        assertTrue(pluginRegistry.estaActivo(pluginName));

        boolean desactivado = microKernel.desactivarPlugin(pluginName);
        assertTrue(desactivado);
        assertFalse(pluginRegistry.estaActivo(pluginName));

        // Ya no debe estar en plugins activos
        List<QuestionPlugin> activos = microKernel.obtenerPluginsActivos();
        assertFalse(activos.stream().anyMatch(p -> p.getName().equals(pluginName)));

        // Debe seguir en todos los plugins
        List<QuestionPlugin> todos = microKernel.obtenerTodosLosPlugins();
        assertTrue(todos.stream().anyMatch(p -> p.getName().equals(pluginName)));

        // Reactivar
        boolean activado = microKernel.activarPlugin(pluginName);
        assertTrue(activado);
        assertTrue(pluginRegistry.estaActivo(pluginName));
    }

    @Test
    @DisplayName("Generación exitosa de pregunta a través del Microkernel")
    void testGenerarPreguntaExitosa() {
        microKernel.inicializar();

        QuestionRequest request = new QuestionRequest();
        request.setTitulo("Pregunta de Prueba");
        request.setContenido("¿Cuál es el resultado de 2 + 2 en aritmética estándar?");
        request.setOpciones(List.of("1", "2", "3", "4"));
        request.setRespuestaCorrecta("D");
        request.setCompetencia("Razonamiento Cuantitativo");
        request.setCategoria("Matemáticas Básicas");
        request.setNivelDificultad("Fácil");
        request.setAutorLogin("autor1");

        Pregunta generada = microKernel.generarPregunta("SELECCION_MULTIPLE", request);

        assertNotNull(generada);
        assertEquals("Pregunta de Prueba", generada.getNombre());
        assertEquals("SELECCION_MULTIPLE", generada.getTipo());
        assertEquals("D", generada.getRespuestaCorrecta());
        assertEquals("autor1", generada.getAutorLogin());
    }

    @Test
    @DisplayName("Generar pregunta falla si el tipo no tiene un plugin activo")
    void testGenerarPreguntaTipoNoSoportado() {
        microKernel.inicializar();

        QuestionRequest request = new QuestionRequest();
        request.setTitulo("Test");

        assertThrows(IllegalArgumentException.class, () ->
                microKernel.generarPregunta("TIPO_INEXISTENTE", request));
    }

    @Test
    @DisplayName("Generar pregunta falla si el plugin está desactivado")
    void testGenerarPreguntaPluginDesactivado() {
        microKernel.inicializar();
        microKernel.desactivarPlugin("Selección Múltiple");

        QuestionRequest request = new QuestionRequest();
        request.setTitulo("Test");
        request.setOpciones(List.of("A", "B", "C", "D"));
        request.setRespuestaCorrecta("A");

        assertThrows(IllegalArgumentException.class, () ->
                microKernel.generarPregunta("SELECCION_MULTIPLE", request));
    }

    @Test
    @DisplayName("Obtener estados de plugins devuelve mapa correcto")
    void testObtenerEstadosPlugins() {
        microKernel.inicializar();
        Map<String, Boolean> estados = microKernel.obtenerEstadosPlugins();
        assertNotNull(estados);
        assertTrue(estados.containsKey("Selección Múltiple"));
        assertTrue(estados.get("Selección Múltiple"));
    }

    @Test
    @DisplayName("Validación de argumentos nulos en el constructor de QuestionMicroKernel")
    void testConstructorValidaciones() {
        assertThrows(IllegalArgumentException.class, () ->
                new QuestionMicroKernel(null, pluginManager));
        assertThrows(IllegalArgumentException.class, () ->
                new QuestionMicroKernel(preguntaService, null));
    }
}
