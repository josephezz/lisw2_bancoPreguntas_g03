package co.edu.unicauca.lisw2_t02_g03;

import co.edu.unicauca.lisw2_t02_g03.MicroKernel.PluginLoader;
import co.edu.unicauca.lisw2_t02_g03.MicroKernel.PluginManager;
import co.edu.unicauca.lisw2_t02_g03.MicroKernel.PluginRegistry;
import co.edu.unicauca.lisw2_t02_g03.MicroKernel.QuestionMicroKernel;
import co.edu.unicauca.lisw2_t02_g03.access.DataBaseInitializer;
import co.edu.unicauca.lisw2_t02_g03.access.DataBaseManager;
import co.edu.unicauca.lisw2_t02_g03.access.InterfaceUsuarioRepository;
import co.edu.unicauca.lisw2_t02_g03.access.PreguntaImplRepository;
import co.edu.unicauca.lisw2_t02_g03.access.UsuarioRepository;
import co.edu.unicauca.lisw2_t02_g03.domain.PreguntaRepository;
import co.edu.unicauca.lisw2_t02_g03.domain.PreguntaService;
import co.edu.unicauca.lisw2_t02_g03.pipeline.ClassificationFilter;
import co.edu.unicauca.lisw2_t02_g03.pipeline.ContentValidationFilter;
import co.edu.unicauca.lisw2_t02_g03.pipeline.CorrectAnswerValidationFilter;
import co.edu.unicauca.lisw2_t02_g03.pipeline.OptionsValidationFilter;
import co.edu.unicauca.lisw2_t02_g03.pipeline.QuestionPipeline;
import co.edu.unicauca.lisw2_t02_g03.presentation.LoginFrame;
import co.edu.unicauca.lisw2_t02_g03.presentation.PreguntasController;
import co.edu.unicauca.lisw2_t02_g03.services.AuthServices;
import co.edu.unicauca.lisw2_t02_g03.services.PasswordHasher;
import co.edu.unicauca.lisw2_t02_g03.services.PasswordValidator;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import javax.swing.SwingUtilities;

/**
 * Composition root de la aplicación.
 * Configura e inyecta: BD, usuarios, autenticación, microkernel, plugins y pipeline.
 */
public class Main {

    public static void main(String[] args) {
        // ============================
        // Base de datos y usuarios
        // ============================
        DataBaseManager databaseManager = new DataBaseManager();
        DataBaseInitializer databaseInitializer = new DataBaseInitializer(databaseManager);
        databaseInitializer.initialize();

        InterfaceUsuarioRepository usuarioRepository = new UsuarioRepository(databaseManager);
        PasswordValidator passwordValidator = new PasswordValidator();
        PasswordHasher passwordHasher = new PasswordHasher();

        UsuarioServices usuarioServices = new UsuarioServices(
                usuarioRepository,
                passwordValidator::isValid,
                passwordHasher::hash);

        AuthServices authServices = new AuthServices(
                usuarioRepository,
                passwordHasher::verify);

        // ============================
        // Banco de preguntas (dominio)
        // ============================
        PreguntaRepository preguntaRepository = new PreguntaImplRepository();
        PreguntaService preguntaService = new PreguntaService(preguntaRepository);

        // ============================
        // Microkernel + Plugins (reflexión)
        // ============================
        PluginLoader pluginLoader = new PluginLoader();
        PluginRegistry pluginRegistry = new PluginRegistry();
        PluginManager pluginManager = new PluginManager(pluginLoader, pluginRegistry);

        QuestionMicroKernel microKernel = new QuestionMicroKernel(preguntaService, pluginManager);
        int pluginsCargados = microKernel.inicializar();
        System.out.println("Plugins cargados mediante reflexión: " + pluginsCargados);

        // ============================
        // Pipeline Tuberías/Filtros
        // ============================
        QuestionPipeline pipeline = new QuestionPipeline()
                .agregarFiltro(new ContentValidationFilter())
                .agregarFiltro(new OptionsValidationFilter())
                .agregarFiltro(new ClassificationFilter())
                .agregarFiltro(new CorrectAnswerValidationFilter());

        // ============================
        // Controlador con todo integrado
        // ============================
        PreguntasController preguntasController = new PreguntasController(
                preguntaService, microKernel, pipeline);

        // ============================
        // Interfaz Swing
        // ============================
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(
                    authServices,
                    usuarioServices,
                    preguntasController);
            loginFrame.setVisible(true);
        });
    }
}
