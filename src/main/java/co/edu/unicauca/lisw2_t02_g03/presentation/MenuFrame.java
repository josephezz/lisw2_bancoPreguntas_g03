package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.model.Rol;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.services.AuthServices;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Ventana principal con arquitectura Dashboard institucional (Universidad del Cauca).
 * Integra cabecera corporativa, barra lateral con navegación adaptativa por rol y área central dinámica.
 */
public class MenuFrame extends JFrame {

    private final Usuario usuario;
    private final UsuarioServices usuarioServices;
    private final AuthServices authServices;
    private final PreguntasController preguntasController;

    private CardLayout cardLayout;
    private JPanel panelContenidoCentral;

    // Paneles dedicados
    private GestionUsuariosPanel panelUsuarios;
    private GestionPluginsPanel panelPlugins;
    private CrearPreguntaPanel panelCrearPregunta;
    private GenerarConPluginPanel panelGenerarPlugin;
    private MisPreguntasPanel panelMisPreguntas;
    private RevisionPreguntasPanel panelRevision;
    private DocenteDashboardPanel panelDocente;
    private SimulacroEstudiantePanel panelEstudiante;

    public MenuFrame(
            Usuario usuario,
            UsuarioServices usuarioServices,
            AuthServices authServices,
            PreguntasController preguntasController) {

        if (usuario == null || usuarioServices == null || authServices == null || preguntasController == null) {
            throw new IllegalArgumentException("Las dependencias de MenuFrame son obligatorias.");
        }

        this.usuario = usuario;
        this.usuarioServices = usuarioServices;
        this.authServices = authServices;
        this.preguntasController = preguntasController;

        configurarVentana();
        crearInterfaz();
    }

    private void configurarVentana() {
        setTitle("Banco de Preguntas Saber Pro - Universidad del Cauca [" + usuario.getRol() + "]");
        setSize(1180, 780);
        setMinimumSize(new Dimension(1000, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(UITheme.COLOR_BG);
    }

    private void crearInterfaz() {
        setLayout(new BorderLayout());

        // 1. Cabecera institucional
        HeaderPanel header = new HeaderPanel(usuario, this::cerrarSesion);
        add(header, BorderLayout.NORTH);

        // 2. Barra lateral de navegación
        SidebarPanel sidebar = new SidebarPanel(
                usuario.getRol(),
                this::navegarA,
                () -> PreguntasFrame.mostrarEnEdt(preguntasController)
        );
        add(sidebar, BorderLayout.WEST);

        // 3. Panel central dinámico con CardLayout
        cardLayout = new CardLayout();
        panelContenidoCentral = new JPanel(cardLayout);
        panelContenidoCentral.setOpaque(false);
        panelContenidoCentral.setBorder(new EmptyBorder(16, 16, 16, 16));

        inicializarPanelesPorRol();

        add(panelContenidoCentral, BorderLayout.CENTER);
    }

    private void inicializarPanelesPorRol() {
        Rol rol = usuario.getRol();

        switch (rol) {
            case ADMINISTRADOR -> {
                panelUsuarios = new GestionUsuariosPanel(usuarioServices);
                panelPlugins = new GestionPluginsPanel(preguntasController);
                panelContenidoCentral.add(panelUsuarios, "CARD_USUARIOS");
                panelContenidoCentral.add(panelPlugins, "CARD_PLUGINS");
                cardLayout.show(panelContenidoCentral, "CARD_USUARIOS");
            }
            case AUTOR -> {
                panelCrearPregunta = new CrearPreguntaPanel(preguntasController, usuario);
                panelGenerarPlugin = new GenerarConPluginPanel(preguntasController, usuario);
                panelMisPreguntas = new MisPreguntasPanel(preguntasController, usuario);
                panelContenidoCentral.add(panelCrearPregunta, "CARD_CREAR");
                panelContenidoCentral.add(panelGenerarPlugin, "CARD_GENERAR_PLUGIN");
                panelContenidoCentral.add(panelMisPreguntas, "CARD_MIS_PREGUNTAS");
                cardLayout.show(panelContenidoCentral, "CARD_CREAR");
            }
            case REVISOR -> {
                panelRevision = new RevisionPreguntasPanel(preguntasController);
                panelContenidoCentral.add(panelRevision, "CARD_REVISION");
                cardLayout.show(panelContenidoCentral, "CARD_REVISION");
            }
            case DOCENTE -> {
                panelDocente = new DocenteDashboardPanel(preguntasController);
                panelContenidoCentral.add(panelDocente, "CARD_DOCENTE");
                cardLayout.show(panelContenidoCentral, "CARD_DOCENTE");
            }
            case ESTUDIANTE -> {
                panelEstudiante = new SimulacroEstudiantePanel(preguntasController);
                panelContenidoCentral.add(panelEstudiante, "CARD_ESTUDIANTE");
                cardLayout.show(panelContenidoCentral, "CARD_ESTUDIANTE");
            }
        }
    }

    private void navegarA(String cardId) {
        cardLayout.show(panelContenidoCentral, cardId);

        // Refrescar datos del panel al enfocarlo si corresponde
        if ("CARD_USUARIOS".equals(cardId) && panelUsuarios != null) {
            panelUsuarios.recargarUsuarios();
        } else if ("CARD_PLUGINS".equals(cardId) && panelPlugins != null) {
            panelPlugins.recargarPlugins();
        } else if ("CARD_MIS_PREGUNTAS".equals(cardId) && panelMisPreguntas != null) {
            panelMisPreguntas.recargarMisPreguntas();
        } else if ("CARD_REVISION".equals(cardId) && panelRevision != null) {
            panelRevision.recargarPreguntasPendientes();
        } else if ("CARD_DOCENTE".equals(cardId) && panelDocente != null) {
            panelDocente.actualizarKpis();
        } else if ("CARD_ESTUDIANTE".equals(cardId) && panelEstudiante != null) {
            panelEstudiante.recargarPreguntasAprobadas();
        }
    }

    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea cerrar la sesión actual de " + usuario.getNombreCompleto() + "?",
                "Confirmar Cierre de Sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            dispose();
            LoginFrame loginFrame = new LoginFrame(
                    authServices,
                    usuarioServices,
                    preguntasController
            );
            loginFrame.setVisible(true);
        }
    }
}