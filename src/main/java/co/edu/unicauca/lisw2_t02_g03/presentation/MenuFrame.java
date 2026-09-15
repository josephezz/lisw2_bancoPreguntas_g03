package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.OpcionesPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.ResultadoCambioEstado;
import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Rol;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.pipeline.ValidationException;
import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionPlugin;
import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionRequest;
import co.edu.unicauca.lisw2_t02_g03.services.AuthServices;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                "Banco de Preguntas Saber Pro - "
                        + usuario.getRol()
        );

        setSize(800, 650);

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
                crearMenuAdministrador(panelMenu);
                break;

            case AUTOR:
                crearMenuAutor(panelMenu);
                break;

            case REVISOR:
                crearMenuRevisor(panelMenu);
                break;

            case DOCENTE:
                crearMenuDocente(panelMenu);
                break;

            case ESTUDIANTE:
                crearMenuEstudiante(panelMenu);
                break;

            default:
                break;
        }
    }

    // ==========================================
    // ADMINISTRADOR
    // ==========================================

    private void crearMenuAdministrador(JPanel panelMenu) {
        JButton btnUsuarios = new JButton("Gestionar usuarios");
        btnUsuarios.addActionListener(e -> mostrarGestionUsuarios());
        panelMenu.add(btnUsuarios);

        JButton btnPlugins = new JButton("Administrar plugins");
        btnPlugins.addActionListener(e -> mostrarGestionPlugins());
        panelMenu.add(btnPlugins);
    }

    // ==========================================
    // AUTOR
    // ==========================================

    private void crearMenuAutor(JPanel panelMenu) {
        JButton btnCrearPregunta = new JButton("Crear pregunta");
        btnCrearPregunta.addActionListener(e -> mostrarFormularioCrearPregunta());
        panelMenu.add(btnCrearPregunta);

        JButton btnGenerarPlugin = new JButton("Generar con plugin");
        btnGenerarPlugin.addActionListener(e -> mostrarGenerarConPlugin());
        panelMenu.add(btnGenerarPlugin);

        JButton btnMisPreguntas = new JButton("Mis preguntas");
        btnMisPreguntas.addActionListener(e -> mostrarMisPreguntas());
        panelMenu.add(btnMisPreguntas);
    }

    // ==========================================
    // REVISOR
    // ==========================================

    private void crearMenuRevisor(JPanel panelMenu) {
        JButton btnPendientes = new JButton("Preguntas por revisar");
        btnPendientes.addActionListener(e -> mostrarPreguntasPendientes());
        panelMenu.add(btnPendientes);
    }

    // ==========================================
    // DOCENTE
    // ==========================================

    private void crearMenuDocente(JPanel panelMenu) {
        JButton btnEstadisticas = new JButton("Estadísticas");
        btnEstadisticas.addActionListener(e -> {
            var stats = preguntasController.obtenerEstadisticas();
            StringBuilder sb = new StringBuilder("ESTADÍSTICAS DEL BANCO\n\n");
            sb.append("Total de preguntas: ").append(stats.getTotal()).append("\n\n");
            for (EstadoPregunta estado : EstadoPregunta.values()) {
                sb.append(estado).append(": ").append(stats.getConteo(estado))
                  .append(" (").append(String.format("%.1f%%", stats.getPorcentaje(estado))).append(")\n");
            }
            areaContenido.setText(sb.toString());
        });
        panelMenu.add(btnEstadisticas);
    }

    // ==========================================
    // ESTUDIANTE
    // ==========================================

    private void crearMenuEstudiante(JPanel panelMenu) {
        JButton btnSimulacro = new JButton("Realizar simulacro");
        btnSimulacro.addActionListener(e -> mostrarSimulacro());
        panelMenu.add(btnSimulacro);
    }

    // ==========================================
    // FUNCIONALIDADES IMPLEMENTADAS
    // ==========================================

    private void mostrarFormularioCrearPregunta() {
        JTextField txtTitulo = new JTextField();
        JTextArea txtEnunciado = new JTextArea(3, 30);
        txtEnunciado.setLineWrap(true);
        JTextField txtOpcionA = new JTextField();
        JTextField txtOpcionB = new JTextField();
        JTextField txtOpcionC = new JTextField();
        JTextField txtOpcionD = new JTextField();
        JComboBox<String> comboRespuesta = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        JTextField txtCompetencia = new JTextField();
        JTextField txtCategoria = new JTextField();
        JComboBox<String> comboNivel = new JComboBox<>(new String[]{"Fácil", "Medio", "Difícil"});

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Título:"));          form.add(txtTitulo);
        form.add(new JLabel("Enunciado:"));        form.add(new JScrollPane(txtEnunciado));
        form.add(new JLabel("Opción A:"));         form.add(txtOpcionA);
        form.add(new JLabel("Opción B:"));         form.add(txtOpcionB);
        form.add(new JLabel("Opción C:"));         form.add(txtOpcionC);
        form.add(new JLabel("Opción D:"));         form.add(txtOpcionD);
        form.add(new JLabel("Respuesta correcta:")); form.add(comboRespuesta);
        form.add(new JLabel("Competencia:"));      form.add(txtCompetencia);
        form.add(new JLabel("Categoría:"));        form.add(txtCategoria);
        form.add(new JLabel("Nivel dificultad:")); form.add(comboNivel);

        form.setPreferredSize(new Dimension(500, 400));

        int result = JOptionPane.showConfirmDialog(this, form, "Crear pregunta",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        try {
            String id = "P-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Pregunta pregunta = new Pregunta(
                    id,
                    txtTitulo.getText().trim(),
                    txtEnunciado.getText().trim(),
                    "SELECCION_MULTIPLE",
                    OpcionesPregunta.de(
                            txtOpcionA.getText().trim(),
                            txtOpcionB.getText().trim(),
                            txtOpcionC.getText().trim(),
                            txtOpcionD.getText().trim()),
                    (String) comboRespuesta.getSelectedItem(),
                    EstadoPregunta.BORRADOR,
                    txtCompetencia.getText().trim(),
                    txtCategoria.getText().trim(),
                    (String) comboNivel.getSelectedItem(),
                    null,
                    usuario.getLogin(),
                    null);

            // Validar con pipeline
            preguntasController.validarPregunta(pregunta);

            // Guardar
            boolean guardada = preguntasController.guardarPregunta(pregunta);
            if (guardada) {
                // Enviar a revisión automáticamente
                preguntasController.enviarARevision(id);
                JOptionPane.showMessageDialog(this,
                        "Pregunta creada y enviada a revisión.\nID: " + id,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo guardar la pregunta.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error de validación:\n" + ex.getMessage(),
                    "Validación fallida", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Datos inválidos:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarGenerarConPlugin() {
        List<QuestionPlugin> plugins = preguntasController.obtenerPluginsActivos();
        if (plugins.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay plugins activos disponibles.",
                    "Sin plugins", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] nombres = plugins.stream().map(QuestionPlugin::getName).toArray(String[]::new);
        String seleccion = (String) JOptionPane.showInputDialog(this,
                "Seleccione un plugin:", "Generar con plugin",
                JOptionPane.QUESTION_MESSAGE, null, nombres, nombres[0]);

        if (seleccion == null) return;

        QuestionPlugin plugin = plugins.stream()
                .filter(p -> p.getName().equals(seleccion))
                .findFirst().orElse(null);

        if (plugin == null) return;

        // Formulario simplificado para la generación
        JTextField txtTitulo = new JTextField();
        JTextArea txtEnunciado = new JTextArea(3, 30);
        txtEnunciado.setLineWrap(true);
        JTextField txtOpcionA = new JTextField();
        JTextField txtOpcionB = new JTextField();
        JTextField txtOpcionC = new JTextField();
        JTextField txtOpcionD = new JTextField();
        JComboBox<String> comboRespuesta = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        JTextField txtCompetencia = new JTextField();
        JTextField txtCategoria = new JTextField();
        JComboBox<String> comboNivel = new JComboBox<>(new String[]{"Fácil", "Medio", "Difícil"});
        JTextField txtRecurso = new JTextField();

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Título:"));          form.add(txtTitulo);
        form.add(new JLabel("Enunciado:"));        form.add(new JScrollPane(txtEnunciado));
        form.add(new JLabel("Opción A:"));         form.add(txtOpcionA);
        form.add(new JLabel("Opción B:"));         form.add(txtOpcionB);
        form.add(new JLabel("Opción C:"));         form.add(txtOpcionC);
        form.add(new JLabel("Opción D:"));         form.add(txtOpcionD);
        form.add(new JLabel("Respuesta correcta:")); form.add(comboRespuesta);
        form.add(new JLabel("Competencia:"));      form.add(txtCompetencia);
        form.add(new JLabel("Categoría:"));        form.add(txtCategoria);
        form.add(new JLabel("Nivel dificultad:")); form.add(comboNivel);
        form.add(new JLabel("Recurso multimedia:")); form.add(txtRecurso);

        form.setPreferredSize(new Dimension(500, 440));

        int result = JOptionPane.showConfirmDialog(this, form,
                "Generar con: " + seleccion,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        try {
            QuestionRequest request = new QuestionRequest();
            request.setTitulo(txtTitulo.getText().trim());
            request.setContenido(txtEnunciado.getText().trim());
            request.setOpciones(List.of(
                    txtOpcionA.getText().trim(),
                    txtOpcionB.getText().trim(),
                    txtOpcionC.getText().trim(),
                    txtOpcionD.getText().trim()));
            request.setRespuestaCorrecta((String) comboRespuesta.getSelectedItem());
            request.setCompetencia(txtCompetencia.getText().trim());
            request.setCategoria(txtCategoria.getText().trim());
            request.setNivelDificultad((String) comboNivel.getSelectedItem());
            request.setRecursoMultimedia(txtRecurso.getText().trim().isEmpty() ? null : txtRecurso.getText().trim());
            request.setAutorLogin(usuario.getLogin());

            // Detectar tipo según plugin
            String tipo = "SELECCION_MULTIPLE";
            if (plugin.supports("CASO")) tipo = "CASO";
            else if (plugin.supports("MULTIMEDIA")) tipo = "MULTIMEDIA";

            Pregunta generada = preguntasController.generarConPlugin(tipo, request);
            boolean guardada = preguntasController.guardarPregunta(generada);
            if (guardada) {
                preguntasController.enviarARevision(generada.getId());
                JOptionPane.showMessageDialog(this,
                        "Pregunta generada y enviada a revisión.\nPlugin: " + seleccion
                                + "\nID: " + generada.getId(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error de validación:\n" + ex.getMessage(),
                    "Validación fallida", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al generar:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarMisPreguntas() {
        List<Pregunta> misPreguntas = preguntasController.listarPorAutor(usuario.getLogin());
        if (misPreguntas.isEmpty()) {
            areaContenido.setText("No tiene preguntas registradas.");
            return;
        }

        StringBuilder sb = new StringBuilder("MIS PREGUNTAS\n\n");
        for (Pregunta p : misPreguntas) {
            sb.append("ID: ").append(p.getId())
              .append(" | ").append(p.getNombre())
              .append(" | Estado: ").append(p.getEstado())
              .append("\n");
            if (p.getObservaciones() != null) {
                sb.append("   Observaciones: ").append(p.getObservaciones()).append("\n");
            }
        }
        areaContenido.setText(sb.toString());
    }

    private void mostrarPreguntasPendientes() {
        List<Pregunta> pendientes = preguntasController.listarPorEstado(EstadoPregunta.PENDIENTE_REVISION);
        if (pendientes.isEmpty()) {
            areaContenido.setText("No hay preguntas pendientes de revisión.");
            return;
        }

        StringBuilder sb = new StringBuilder("PREGUNTAS PENDIENTES DE REVISIÓN\n\n");
        for (Pregunta p : pendientes) {
            sb.append("─────────────────────────────────\n");
            sb.append("ID: ").append(p.getId()).append("\n");
            sb.append("Título: ").append(p.getNombre()).append("\n");
            sb.append("Enunciado: ").append(p.getEnunciado()).append("\n");
            sb.append("Opciones:\n").append(p.getOpciones().comoTexto()).append("\n");
            sb.append("Respuesta: ").append(p.getRespuestaCorrecta()).append("\n");
            if (p.getCompetencia() != null) sb.append("Competencia: ").append(p.getCompetencia()).append("\n");
            if (p.getCategoria() != null) sb.append("Categoría: ").append(p.getCategoria()).append("\n");
            if (p.getNivelDificultad() != null) sb.append("Nivel: ").append(p.getNivelDificultad()).append("\n");
            sb.append("\n");
        }
        areaContenido.setText(sb.toString());

        // Panel de acciones para el revisor
        JPanel panelAcciones = new JPanel(new FlowLayout());
        JTextField txtId = new JTextField(12);
        JButton btnAprobar = new JButton("Aprobar");
        JButton btnRechazar = new JButton("Rechazar");

        panelAcciones.add(new JLabel("ID de la pregunta:"));
        panelAcciones.add(txtId);
        panelAcciones.add(btnAprobar);
        panelAcciones.add(btnRechazar);

        btnAprobar.addActionListener(e -> {
            ResultadoCambioEstado resultado = preguntasController.aprobarPregunta(txtId.getText().trim());
            mostrarResultadoRevision(resultado, "aprobada");
            mostrarPreguntasPendientes();
        });

        btnRechazar.addActionListener(e -> {
            String obs = JOptionPane.showInputDialog(this, "Observaciones del rechazo:");
            if (obs != null) {
                ResultadoCambioEstado resultado = preguntasController.rechazarPregunta(txtId.getText().trim(), obs);
                mostrarResultadoRevision(resultado, "rechazada");
                mostrarPreguntasPendientes();
            }
        });

        JOptionPane.showMessageDialog(this, panelAcciones,
                "Acciones de revisión", JOptionPane.PLAIN_MESSAGE);
    }

    private void mostrarResultadoRevision(ResultadoCambioEstado resultado, String accion) {
        switch (resultado) {
            case ACTUALIZADO -> JOptionPane.showMessageDialog(this,
                    "Pregunta " + accion + " correctamente.",
                    "Revisión exitosa", JOptionPane.INFORMATION_MESSAGE);
            case PREGUNTA_NO_ENCONTRADA -> JOptionPane.showMessageDialog(this,
                    "No se encontró la pregunta.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            case ENTRADA_INVALIDA -> JOptionPane.showMessageDialog(this,
                    "Datos de entrada inválidos.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            default -> JOptionPane.showMessageDialog(this,
                    "Resultado: " + resultado,
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void mostrarSimulacro() {
        List<Pregunta> aprobadas = preguntasController.listarPorEstado(EstadoPregunta.APROBADA);
        if (aprobadas.isEmpty()) {
            areaContenido.setText("No hay preguntas aprobadas disponibles para el simulacro.");
            return;
        }

        StringBuilder sb = new StringBuilder("SIMULACRO - PREGUNTAS DISPONIBLES\n\n");
        int num = 1;
        for (Pregunta p : aprobadas) {
            sb.append("Pregunta ").append(num++).append(": ").append(p.getNombre()).append("\n");
            sb.append(p.getEnunciado()).append("\n\n");
            sb.append(p.getOpciones().comoTexto()).append("\n\n");
        }
        areaContenido.setText(sb.toString());
    }

    private void mostrarGestionPlugins() {
        List<QuestionPlugin> todos = preguntasController.obtenerTodosLosPlugins();
        if (todos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay plugins registrados.",
                    "Plugins", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Map<String, Boolean> estados = preguntasController.obtenerPluginsActivos().isEmpty()
                ? Map.of()
                : null; // dummy check

        StringBuilder sb = new StringBuilder("PLUGINS REGISTRADOS\n\n");
        for (QuestionPlugin p : todos) {
            boolean activo = preguntasController.obtenerPluginsActivos().stream()
                    .anyMatch(a -> a.getName().equals(p.getName()));
            sb.append("• ").append(p.getName())
              .append(" [").append(activo ? "ACTIVO" : "INACTIVO").append("]\n");
        }
        areaContenido.setText(sb.toString());

        // Panel de acciones
        JPanel panel = new JPanel(new FlowLayout());
        JTextField txtNombre = new JTextField(20);
        JButton btnActivar = new JButton("Activar");
        JButton btnDesactivar = new JButton("Desactivar");

        panel.add(new JLabel("Nombre del plugin:"));
        panel.add(txtNombre);
        panel.add(btnActivar);
        panel.add(btnDesactivar);

        btnActivar.addActionListener(e -> {
            boolean ok = preguntasController.activarPlugin(txtNombre.getText().trim());
            JOptionPane.showMessageDialog(this,
                    ok ? "Plugin activado." : "Plugin no encontrado.",
                    "Resultado", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            mostrarGestionPlugins();
        });

        btnDesactivar.addActionListener(e -> {
            boolean ok = preguntasController.desactivarPlugin(txtNombre.getText().trim());
            JOptionPane.showMessageDialog(this,
                    ok ? "Plugin desactivado." : "Plugin no encontrado.",
                    "Resultado", ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            mostrarGestionPlugins();
        });

        JOptionPane.showMessageDialog(this, panel,
                "Administrar plugins", JOptionPane.PLAIN_MESSAGE);
    }

    // ==========================================
    // MENSAJE INICIAL
    // ==========================================

    private String obtenerMensajeInicial() {

        return switch (usuario.getRol()) {

            case ADMINISTRADOR ->
                    "Panel de administración.\n\n"
                    + "Desde aquí puede gestionar "
                    + "los usuarios del sistema y administrar plugins.";

            case AUTOR ->
                    "Panel del autor.\n\n"
                    + "Desde aquí podrá crear preguntas, "
                    + "generar preguntas mediante plugins "
                    + "y consultar el estado de sus preguntas.";

            case REVISOR ->
                    "Panel del revisor.\n\n"
                    + "Desde aquí podrá revisar preguntas pendientes, "
                    + "aprobar o rechazar preguntas.";

            case DOCENTE ->
                    "Panel del docente.\n\n"
                    + "Desde aquí podrá consultar "
                    + "información y estadísticas.";

            case ESTUDIANTE ->
                    "Panel del estudiante.\n\n"
                    + "Desde aquí podrá realizar simulacros "
                    + "con preguntas aprobadas.";

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

        tabla.setEnabled(false);
        tabla.setRowHeight(30);

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