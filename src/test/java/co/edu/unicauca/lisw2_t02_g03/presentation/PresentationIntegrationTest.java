package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.MicroKernel.PluginLoader;
import co.edu.unicauca.lisw2_t02_g03.MicroKernel.PluginManager;
import co.edu.unicauca.lisw2_t02_g03.MicroKernel.PluginRegistry;
import co.edu.unicauca.lisw2_t02_g03.MicroKernel.QuestionMicroKernel;
import co.edu.unicauca.lisw2_t02_g03.access.InterfaceUsuarioRepository;
import co.edu.unicauca.lisw2_t02_g03.access.PreguntaImplRepository;
import co.edu.unicauca.lisw2_t02_g03.domain.PreguntaRepository;
import co.edu.unicauca.lisw2_t02_g03.domain.PreguntaService;
import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Rol;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.pipeline.ClassificationFilter;
import co.edu.unicauca.lisw2_t02_g03.pipeline.ContentValidationFilter;
import co.edu.unicauca.lisw2_t02_g03.pipeline.CorrectAnswerValidationFilter;
import co.edu.unicauca.lisw2_t02_g03.pipeline.OptionsValidationFilter;
import co.edu.unicauca.lisw2_t02_g03.pipeline.QuestionPipeline;
import co.edu.unicauca.lisw2_t02_g03.services.AuthServices;
import co.edu.unicauca.lisw2_t02_g03.services.PasswordHasher;
import co.edu.unicauca.lisw2_t02_g03.services.PasswordValidator;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PresentationIntegrationTest {

    private AuthServices authServices;
    private UsuarioServices usuarioServices;
    private PreguntasController preguntasController;

    private static class TestUsuarioRepo implements InterfaceUsuarioRepository {
        private final List<Usuario> usuarios = new ArrayList<>();

        @Override
        public boolean save(Usuario usuario) {
            if (usuario == null || findByLogin(usuario.getLogin()) != null) return false;
            usuarios.add(usuario);
            return true;
        }

        @Override
        public List<Usuario> list() {
            return new ArrayList<>(usuarios);
        }

        @Override
        public Usuario findByLogin(String login) {
            return usuarios.stream().filter(u -> u.getLogin().equals(login)).findFirst().orElse(null);
        }

        @Override
        public boolean updateEstado(EstadoUsuario estado, String login) {
            Usuario u = findByLogin(login);
            if (u == null) return false;
            u.setEstado(estado);
            return true;
        }
    }

    @BeforeEach
    void setUp() {
        InterfaceUsuarioRepository usuarioRepo = new TestUsuarioRepo();
        PasswordValidator validator = new PasswordValidator();
        PasswordHasher hasher = new PasswordHasher();

        usuarioServices = new UsuarioServices(usuarioRepo, validator::isValid, hasher::hash);
        authServices = new AuthServices(usuarioRepo, hasher::verify);

        PreguntaRepository preguntaRepo = new PreguntaImplRepository();
        PreguntaService preguntaService = new PreguntaService(preguntaRepo);

        PluginLoader pluginLoader = new PluginLoader();
        PluginRegistry pluginRegistry = new PluginRegistry();
        PluginManager pluginManager = new PluginManager(pluginLoader, pluginRegistry);

        QuestionMicroKernel microKernel = new QuestionMicroKernel(preguntaService, pluginManager);
        microKernel.inicializar();

        QuestionPipeline pipeline = new QuestionPipeline()
                .agregarFiltro(new ContentValidationFilter())
                .agregarFiltro(new OptionsValidationFilter())
                .agregarFiltro(new ClassificationFilter())
                .agregarFiltro(new CorrectAnswerValidationFilter());

        preguntasController = new PreguntasController(preguntaService, microKernel, pipeline);
    }

    @Test
    void debeInicializarLoginYRegistro() {
        LoginFrame loginFrame = new LoginFrame(authServices, usuarioServices, preguntasController);
        assertNotNull(loginFrame);
        loginFrame.dispose();

        RegistroFrame registroFrame = new RegistroFrame(usuarioServices, null);
        assertNotNull(registroFrame);
        registroFrame.dispose();
    }

    @Test
    void debeInicializarMenuParaCadaRol() {
        for (Rol rol : Rol.values()) {
            Usuario u = new Usuario("user_" + rol.name(), "Nombre " + rol.name(), rol, EstadoUsuario.ACTIVO, "pass");
            MenuFrame menu = new MenuFrame(u, usuarioServices, authServices, preguntasController);
            assertNotNull(menu);
            menu.dispose();
        }
    }

    @Test
    void debeInicializarPreguntasFrame() {
        PreguntasFrame frame = new PreguntasFrame(preguntasController);
        assertNotNull(frame);
        frame.dispose();
    }

    @Test
    void debeInicializarTodosLosPaneles() {
        Usuario autor = new Usuario("autor1", "Autor Uno", Rol.AUTOR, EstadoUsuario.ACTIVO, "pass");

        GestionUsuariosPanel usuariosPanel = new GestionUsuariosPanel(usuarioServices);
        assertNotNull(usuariosPanel);

        GestionPluginsPanel pluginsPanel = new GestionPluginsPanel(preguntasController);
        assertNotNull(pluginsPanel);

        CrearPreguntaPanel crearPanel = new CrearPreguntaPanel(preguntasController, autor);
        assertNotNull(crearPanel);

        GenerarConPluginPanel genPanel = new GenerarConPluginPanel(preguntasController, autor);
        assertNotNull(genPanel);

        MisPreguntasPanel misPreguntasPanel = new MisPreguntasPanel(preguntasController, autor);
        assertNotNull(misPreguntasPanel);

        RevisionPreguntasPanel revisionPanel = new RevisionPreguntasPanel(preguntasController);
        assertNotNull(revisionPanel);

        DocenteDashboardPanel docentePanel = new DocenteDashboardPanel(preguntasController);
        assertNotNull(docentePanel);

        SimulacroEstudiantePanel estudiantePanel = new SimulacroEstudiantePanel(preguntasController);
        assertNotNull(estudiantePanel);
    }
}
