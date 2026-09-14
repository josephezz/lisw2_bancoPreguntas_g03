package co.edu.unicauca.lisw2_t02_g03;

import co.edu.unicauca.lisw2_t02_g03.access.DataBaseInitializer;
import co.edu.unicauca.lisw2_t02_g03.access.DataBaseManager;
import co.edu.unicauca.lisw2_t02_g03.access.InterfaceUsuarioRepository;
import co.edu.unicauca.lisw2_t02_g03.access.PreguntaImplRepository;
import co.edu.unicauca.lisw2_t02_g03.access.UsuarioRepository;
import co.edu.unicauca.lisw2_t02_g03.domain.PreguntaRepository;
import co.edu.unicauca.lisw2_t02_g03.domain.PreguntaService;
import co.edu.unicauca.lisw2_t02_g03.presentation.LoginFrame;
import co.edu.unicauca.lisw2_t02_g03.presentation.PreguntasController;
import co.edu.unicauca.lisw2_t02_g03.services.AuthServices;
import co.edu.unicauca.lisw2_t02_g03.services.PasswordHasher;
import co.edu.unicauca.lisw2_t02_g03.services.PasswordValidator;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import javax.swing.SwingUtilities;

/**
 * Composition root de la aplicación.
 */
public class Main {

    public static void main(String[] args) {
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

        // El banco de preguntas se crea una sola vez por sesión y se inyecta hacia la UI.
        PreguntaRepository preguntaRepository = new PreguntaImplRepository();
        PreguntaService preguntaService = new PreguntaService(preguntaRepository);
        PreguntasController preguntasController = new PreguntasController(preguntaService);

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(
                    authServices,
                    usuarioServices,
                    preguntasController);
            loginFrame.setVisible(true);
        });
    }
}
