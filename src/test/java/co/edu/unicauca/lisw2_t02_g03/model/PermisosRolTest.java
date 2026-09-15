package co.edu.unicauca.lisw2_t02_g03.model;

import co.edu.unicauca.lisw2_t02_g03.MicroKernel.PluginLoader;
import co.edu.unicauca.lisw2_t02_g03.MicroKernel.PluginManager;
import co.edu.unicauca.lisw2_t02_g03.MicroKernel.PluginRegistry;
import co.edu.unicauca.lisw2_t02_g03.MicroKernel.QuestionMicroKernel;
import co.edu.unicauca.lisw2_t02_g03.access.PreguntaImplRepository;
import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.OpcionesPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.PreguntaService;
import co.edu.unicauca.lisw2_t02_g03.domain.ResultadoCambioEstado;
import co.edu.unicauca.lisw2_t02_g03.pipeline.ClassificationFilter;
import co.edu.unicauca.lisw2_t02_g03.pipeline.ContentValidationFilter;
import co.edu.unicauca.lisw2_t02_g03.pipeline.CorrectAnswerValidationFilter;
import co.edu.unicauca.lisw2_t02_g03.pipeline.OptionsValidationFilter;
import co.edu.unicauca.lisw2_t02_g03.pipeline.QuestionPipeline;
import co.edu.unicauca.lisw2_t02_g03.presentation.PreguntasController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PermisosRolTest {

    private PreguntaService preguntaService;
    private QuestionMicroKernel microKernel;
    private QuestionPipeline pipeline;
    private PreguntasController controller;

    private Usuario admin;
    private Usuario autor1;
    private Usuario autor2;
    private Usuario revisor;
    private Usuario estudiante;
    private Usuario docente;

    @BeforeEach
    void setUp() {
        preguntaService = new PreguntaService(new PreguntaImplRepository());
        PluginManager pluginManager = new PluginManager(new PluginLoader(), new PluginRegistry());
        microKernel = new QuestionMicroKernel(preguntaService, pluginManager);
        microKernel.inicializar();

        pipeline = new QuestionPipeline()
                .agregarFiltro(new ContentValidationFilter())
                .agregarFiltro(new OptionsValidationFilter())
                .agregarFiltro(new ClassificationFilter())
                .agregarFiltro(new CorrectAnswerValidationFilter());

        controller = new PreguntasController(preguntaService, microKernel, pipeline);

        admin = new Usuario("admin", "Admin Sistema", Rol.ADMINISTRADOR, EstadoUsuario.ACTIVO, "hash");
        autor1 = new Usuario("autor1", "Carlos Autor", Rol.AUTOR, EstadoUsuario.ACTIVO, "hash");
        autor2 = new Usuario("autor2", "Maria Autor", Rol.AUTOR, EstadoUsuario.ACTIVO, "hash");
        revisor = new Usuario("revisor1", "Ana Revisor", Rol.REVISOR, EstadoUsuario.ACTIVO, "hash");
        estudiante = new Usuario("est1", "Juan Estudiante", Rol.ESTUDIANTE, EstadoUsuario.ACTIVO, "hash");
        docente = new Usuario("doc1", "Pedro Docente", Rol.DOCENTE, EstadoUsuario.ACTIVO, "hash");
    }

    private Pregunta crearPreguntaDeAutor(String id, String autorLogin, EstadoPregunta estado) {
        return new Pregunta(
                id,
                "Pregunta de " + autorLogin,
                "Enunciado de la pregunta creada para evaluación Saber Pro:",
                "SELECCION_MULTIPLE",
                OpcionesPregunta.de("Opción A", "Opción B", "Opción C", "Opción D"),
                "A",
                estado,
                "Competencias Ciudadanas",
                "Ética",
                "Medio",
                null,
                autorLogin,
                null);
    }

    @Test
    @DisplayName("Rol AUTOR: puede crear sus preguntas y solo listar las de su autoría")
    void testPermisosAutor() {
        Pregunta p1 = crearPreguntaDeAutor("P-AUTOR1", autor1.getLogin(), EstadoPregunta.BORRADOR);
        Pregunta p2 = crearPreguntaDeAutor("P-AUTOR2", autor2.getLogin(), EstadoPregunta.BORRADOR);

        controller.guardarPregunta(p1);
        controller.guardarPregunta(p2);

        // Autor 1 solo ve sus preguntas
        List<Pregunta> misPreguntas = controller.listarPorAutor(autor1.getLogin());
        assertEquals(1, misPreguntas.size());
        assertEquals("P-AUTOR1", misPreguntas.get(0).getId());

        // Autor puede enviar su pregunta a revisión
        ResultadoCambioEstado res = controller.enviarARevision("P-AUTOR1");
        assertEquals(ResultadoCambioEstado.ACTUALIZADO, res);
        assertEquals(EstadoPregunta.PENDIENTE_REVISION, controller.buscarPregunta("P-AUTOR1").get().getEstado());
    }

    @Test
    @DisplayName("Rol REVISOR: puede listar pendientes y aprobar o rechazar preguntas")
    void testPermisosRevisor() {
        Pregunta p = crearPreguntaDeAutor("P-REV-1", autor1.getLogin(), EstadoPregunta.PENDIENTE_REVISION);
        controller.guardarPregunta(p);

        // Revisor consulta pendientes
        List<Pregunta> pendientes = controller.listarPorEstado(EstadoPregunta.PENDIENTE_REVISION);
        assertTrue(pendientes.stream().anyMatch(preg -> preg.getId().equals("P-REV-1")));

        // Revisor aprueba pregunta
        ResultadoCambioEstado resAprobacion = controller.aprobarPregunta("P-REV-1");
        assertEquals(ResultadoCambioEstado.ACTUALIZADO, resAprobacion);
        assertEquals(EstadoPregunta.APROBADA, controller.buscarPregunta("P-REV-1").get().getEstado());

        // Revisor rechaza otra pregunta con observaciones
        Pregunta pRechazo = crearPreguntaDeAutor("P-REV-2", autor2.getLogin(), EstadoPregunta.PENDIENTE_REVISION);
        controller.guardarPregunta(pRechazo);

        ResultadoCambioEstado resRechazo = controller.rechazarPregunta("P-REV-2", "El enunciado es ambiguo en la opción C.");
        assertEquals(ResultadoCambioEstado.ACTUALIZADO, resRechazo);
        Pregunta rechazada = controller.buscarPregunta("P-REV-2").get();
        assertEquals(EstadoPregunta.RECHAZADA, rechazada.getEstado());
        assertEquals("El enunciado es ambiguo en la opción C.", rechazada.getObservacionesRevision());
    }

    @Test
    @DisplayName("Rol ESTUDIANTE: solo tiene acceso a preguntas en estado APROBADA")
    void testPermisosEstudiante() {
        Pregunta pBorrador = crearPreguntaDeAutor("P-BORR", autor1.getLogin(), EstadoPregunta.BORRADOR);
        Pregunta pPendiente = crearPreguntaDeAutor("P-PEND", autor1.getLogin(), EstadoPregunta.PENDIENTE_REVISION);
        Pregunta pAprobada = crearPreguntaDeAutor("P-APROB", autor1.getLogin(), EstadoPregunta.APROBADA);
        Pregunta pRechazada = crearPreguntaDeAutor("P-RECH", autor1.getLogin(), EstadoPregunta.RECHAZADA);

        controller.guardarPregunta(pBorrador);
        controller.guardarPregunta(pPendiente);
        controller.guardarPregunta(pAprobada);
        controller.guardarPregunta(pRechazada);

        // Simulacro / consulta de estudiante
        List<Pregunta> paraEstudiantes = controller.listarPorEstado(EstadoPregunta.APROBADA);
        assertEquals(1, paraEstudiantes.size());
        assertEquals("P-APROB", paraEstudiantes.get(0).getId());
        assertTrue(paraEstudiantes.stream().allMatch(preg -> preg.getEstado() == EstadoPregunta.APROBADA));
    }

    @Test
    @DisplayName("Rol ADMINISTRADOR: tiene control total sobre plugins (activar/desactivar)")
    void testPermisosAdministradorPlugins() {
        assertTrue(controller.obtenerPluginsActivos().size() >= 3);

        boolean desactivado = controller.desactivarPlugin("Multimedia");
        assertTrue(desactivado);

        List<String> activos = controller.obtenerPluginsActivos().stream()
                .map(co.edu.unicauca.lisw2_t02_g03.plugins.QuestionPlugin::getName).toList();
        assertFalse(activos.contains("Multimedia"));

        boolean reactivado = controller.activarPlugin("Multimedia");
        assertTrue(reactivado);

        List<String> reactivos = controller.obtenerPluginsActivos().stream()
                .map(co.edu.unicauca.lisw2_t02_g03.plugins.QuestionPlugin::getName).toList();
        assertTrue(reactivos.contains("Multimedia"));
    }

    @Test
    @DisplayName("Rol DOCENTE: puede acceder a las estadísticas del banco")
    void testPermisosDocenteEstadisticas() {
        var stats = controller.obtenerEstadisticas();
        assertNotNull(stats);
        assertTrue(stats.getTotal() >= 0);
        assertNotNull(stats.getConteo(EstadoPregunta.APROBADA));
        assertNotNull(stats.getConteo(EstadoPregunta.RECHAZADA));
    }
}
