package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Rol;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.services.AuthServices;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuFrame extends JFrame {

    private final Usuario usuario;
    private final UsuarioServices usuarioServices;
    private final AuthServices authServices;
    private final PreguntasController preguntasController;

    private JTextArea areaContenido;

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

        setTitle(
                "Sistema de Gestión - "
                        + usuario.getRol()
        );

        setSize(750, 600);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void crearInterfaz() {

        JPanel principal =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        principal.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 20, 25
                )
        );

        // ==========================================
        // CABECERA
        // ==========================================

        JPanel panelCabecera =
                new JPanel(
                        new GridLayout(2, 1)
                );

        JLabel bienvenida =
                new JLabel(
                        "Bienvenido, "
                                + usuario.getNombreCompleto(),
                        SwingConstants.CENTER
                );

        bienvenida.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        20
                )
        );

        JLabel rol =
                new JLabel(
                        "Rol: "
                                + usuario.getRol(),
                        SwingConstants.CENTER
                );

        rol.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        panelCabecera.add(bienvenida);
        panelCabecera.add(rol);

        principal.add(
                panelCabecera,
                BorderLayout.NORTH
        );

        // ==========================================
        // CONTENIDO
        // ==========================================

        areaContenido =
                new JTextArea();

        areaContenido.setEditable(false);

        areaContenido.setLineWrap(true);

        areaContenido.setWrapStyleWord(true);

        areaContenido.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        areaContenido.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        areaContenido.setText(
                obtenerMensajeInicial()
        );

        principal.add(
                new JScrollPane(areaContenido),
                BorderLayout.CENTER
        );

        // ==========================================
        // MENU SEGUN EL ROL
        // ==========================================

        JPanel panelMenu =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                10
                        )
                );

        crearMenuSegunRol(panelMenu);
        JButton btnPreguntas = new JButton("Gestión de preguntas");
        btnPreguntas.addActionListener(e -> PreguntasFrame.mostrarEnEdt(preguntasController));
        panelMenu.add(btnPreguntas);

        JButton btnCerrarSesion =
                new JButton("Cerrar sesión");

        btnCerrarSesion.addActionListener(
                e -> cerrarSesion()
        );

        panelMenu.add(btnCerrarSesion);

        principal.add(
                panelMenu,
                BorderLayout.SOUTH
        );

        add(principal);
    }

    // ==========================================
    // CREAR MENU SEGUN EL ROL
    // ==========================================

    private void crearMenuSegunRol(
            JPanel panelMenu) {

        Rol rol = usuario.getRol();

        switch (rol) {

            case ADMINISTRADOR:

                crearMenuAdministrador(
                        panelMenu
                );

                break;

            case AUTOR:

                crearMenuAutor(
                        panelMenu
                );

                break;

            case REVISOR:

                crearMenuRevisor(
                        panelMenu
                );

                break;

            case DOCENTE:

                crearMenuDocente(
                        panelMenu
                );

                break;

            case ESTUDIANTE:

                crearMenuEstudiante(
                        panelMenu
                );

                break;

            default:

                break;
        }
    }

    // ==========================================
    // ADMINISTRADOR
    // ==========================================

    private void crearMenuAdministrador(
            JPanel panelMenu) {

        JButton btnUsuarios =
                new JButton(
                        "Gestionar usuarios"
                );

        btnUsuarios.addActionListener(
                e -> mostrarGestionUsuarios()
        );

        panelMenu.add(btnUsuarios);
    }

    // ==========================================
    // AUTOR
    // ==========================================

    private void crearMenuAutor(
            JPanel panelMenu) {

        JButton btnCrearPregunta =
                new JButton(
                        "Crear pregunta"
                );

        JButton btnMisPreguntas =
                new JButton(
                        "Mis preguntas"
                );

        btnCrearPregunta.addActionListener(
                e -> mostrarProximamente(
                        "Crear pregunta"
                )
        );

        btnMisPreguntas.addActionListener(
                e -> mostrarProximamente(
                        "Mis preguntas"
                )
        );

        panelMenu.add(btnCrearPregunta);
        panelMenu.add(btnMisPreguntas);
    }

    // ==========================================
    // REVISOR
    // ==========================================

    private void crearMenuRevisor(
            JPanel panelMenu) {

        JButton btnPreguntas =
                new JButton(
                        "Preguntas por revisar"
                );

        JButton btnHistorial =
                new JButton(
                        "Historial de revisiones"
                );

        btnPreguntas.addActionListener(
                e -> mostrarProximamente(
                        "Preguntas por revisar"
                )
        );

        btnHistorial.addActionListener(
                e -> mostrarProximamente(
                        "Historial de revisiones"
                )
        );

        panelMenu.add(btnPreguntas);
        panelMenu.add(btnHistorial);
    }

    // ==========================================
    // DOCENTE
    // ==========================================

    private void crearMenuDocente(
            JPanel panelMenu) {

        JButton btnEstadisticas =
                new JButton(
                        "Estadísticas"
                );

        JButton btnReportes =
                new JButton(
                        "Reportes"
                );

        btnEstadisticas.addActionListener(
                e -> mostrarProximamente(
                        "Estadísticas"
                )
        );

        btnReportes.addActionListener(
                e -> mostrarProximamente(
                        "Reportes"
                )
        );

        panelMenu.add(btnEstadisticas);
        panelMenu.add(btnReportes);
    }

    // ==========================================
    // ESTUDIANTE
    // ==========================================

    private void crearMenuEstudiante(
            JPanel panelMenu) {

        JButton btnSimulacro =
                new JButton(
                        "Realizar simulacro"
                );

        JButton btnProgreso =
                new JButton(
                        "Mi progreso"
                );

        btnSimulacro.addActionListener(
                e -> mostrarProximamente(
                        "Realizar simulacro"
                )
        );

        btnProgreso.addActionListener(
                e -> mostrarProximamente(
                        "Mi progreso"
                )
        );

        panelMenu.add(btnSimulacro);
        panelMenu.add(btnProgreso);
    }

    // ==========================================
    // MENSAJE INICIAL
    // ==========================================

    private String obtenerMensajeInicial() {

        return switch (usuario.getRol()) {

            case ADMINISTRADOR ->
                    "Panel de administración.\n\n"
                    + "Desde aquí puede gestionar "
                    + "los usuarios del sistema.";

            case AUTOR ->
                    "Panel del autor.\n\n"
                    + "Desde aquí podrá acceder "
                    + "a las funciones relacionadas "
                    + "con las preguntas.";

            case REVISOR ->
                    "Panel del revisor.\n\n"
                    + "Desde aquí podrá acceder "
                    + "a las funciones de revisión.";

            case DOCENTE ->
                    "Panel del docente.\n\n"
                    + "Desde aquí podrá consultar "
                    + "información y reportes.";

            case ESTUDIANTE ->
                    "Panel del estudiante.\n\n"
                    + "Desde aquí podrá acceder "
                    + "a las funciones de preparación.";

        };
    }

    // ==========================================
    // GESTION DE USUARIOS
    // ==========================================

    private void mostrarGestionUsuarios() {

        areaContenido.setText(
                "GESTIÓN DE USUARIOS\n\n"
                        + "Seleccione una acción:\n\n"
                        + "• Listar usuarios\n"
                        + "• Activar usuario\n"
                        + "• Desactivar usuario"
        );

        JPanel panelGestion =
                new JPanel(
                        new FlowLayout()
                );

        JButton btnListar =
                new JButton(
                        "Listar usuarios"
                );

        JButton btnActivar =
                new JButton(
                        "Activar usuario"
                );

        JButton btnDesactivar =
                new JButton(
                        "Desactivar usuario"
                );

        btnListar.addActionListener(
                e -> listarUsuarios()
        );

        btnActivar.addActionListener(
                e -> cambiarEstado(
                        EstadoUsuario.ACTIVO
                )
        );

        btnDesactivar.addActionListener(
                e -> cambiarEstado(
                        EstadoUsuario.INACTIVO
                )
        );

        panelGestion.add(btnListar);
        panelGestion.add(btnActivar);
        panelGestion.add(btnDesactivar);

        JOptionPane.showMessageDialog(
                this,
                panelGestion,
                "Gestión de usuarios",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    // ==========================================
    // LISTAR USUARIOS
    // ==========================================

    private void listarUsuarios() {

    List<Usuario> usuarios =
            usuarioServices.listarUsuarios();

    String[] columnas = {
        "Login",
        "Nombre completo",
        "Rol",
        "Estado"
    };

    Object[][] datos =
            new Object[usuarios.size()][4];

    for (int i = 0; i < usuarios.size(); i++) {

        Usuario u = usuarios.get(i);

        datos[i][0] = u.getLogin();
        datos[i][1] = u.getNombreCompleto();
        datos[i][2] = u.getRol();
        datos[i][3] = u.getEstado();
    }

    JTable tabla =
            new JTable(datos, columnas);

    // No permitir editar las celdas
    tabla.setEnabled(false);

    // Altura de las filas
    tabla.setRowHeight(30);

    // Tamaño de la fuente
    tabla.setFont(
            new Font(
                    "Arial",
                    Font.PLAIN,
                    14
            )
    );

    tabla.getTableHeader().setFont(
            new Font(
                    "Arial",
                    Font.BOLD,
                    14
            )
    );

    // Ancho de cada columna
    tabla.getColumnModel()
            .getColumn(0)
            .setPreferredWidth(100);

    tabla.getColumnModel()
            .getColumn(1)
            .setPreferredWidth(250);

    tabla.getColumnModel()
            .getColumn(2)
            .setPreferredWidth(150);

    tabla.getColumnModel()
            .getColumn(3)
            .setPreferredWidth(120);

    // Scroll para la tabla
    JScrollPane scrollPane =
            new JScrollPane(tabla);

    scrollPane.setPreferredSize(
            new Dimension(
                    650,
                    350
            )
    );

    JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Lista de usuarios",
            JOptionPane.PLAIN_MESSAGE
    );
}

    // ==========================================
    // CAMBIAR ESTADO
    // ==========================================

    private void cambiarEstado(
            EstadoUsuario nuevoEstado) {

        String login =
                JOptionPane.showInputDialog(
                        this,
                        "Ingrese el login del usuario:"
                );

        if (
                login == null
                        || login.isBlank()
        ) {

            return;
        }

        boolean actualizado =
                usuarioServices.actualizarEstado(
                        login.trim(),
                        nuevoEstado
                );

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "El usuario "
                            + login
                            + " ahora está "
                            + nuevoEstado + ".",
                    "Operación exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

            listarUsuarios();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el usuario.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==========================================
    // FUNCIONES TODAVÍA NO IMPLEMENTADAS
    // ==========================================

    private void mostrarProximamente(
            String funcionalidad) {

        JOptionPane.showMessageDialog(
                this,
                "La funcionalidad \""
                        + funcionalidad
                        + "\" será implementada "
                        + "en una siguiente etapa.",
                "Próximamente",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ==========================================
    // CERRAR SESION
    // ==========================================

    private void cerrarSesion() {

        int respuesta =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea cerrar sesión?",
                        "Cerrar sesión",
                        JOptionPane.YES_NO_OPTION
                );

        if (
                respuesta
                        == JOptionPane.YES_OPTION
        ) {

            dispose();

            LoginFrame loginFrame =
                    new LoginFrame(
                            authServices,
                            usuarioServices,
                            preguntasController
                    );

            loginFrame.setVisible(true);
        }
    }
}