package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Rol;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Ventana de registro de usuario con diseño moderno estructurado y validación visual.
 */
public class RegistroFrame extends JFrame {

    private final UsuarioServices usuarioServices;
    private final JFrame ventanaAnterior;

    private JTextField txtLogin;
    private JTextField txtNombre;
    private JComboBox<Rol> comboRol;
    private JComboBox<EstadoUsuario> comboEstado;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JLabel lblRolDescripcion;
    private JLabel lblMensaje;

    public RegistroFrame(
            UsuarioServices usuarioServices,
            JFrame ventanaAnterior) {

        this.usuarioServices = usuarioServices;
        this.ventanaAnterior = ventanaAnterior;

        configurarVentana();
        crearInterfaz();
    }

    private void configurarVentana() {
        setTitle("Registro de Usuario - Banco de Preguntas Saber Pro");
        setSize(560, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UITheme.COLOR_BG);
    }

    private void crearInterfaz() {
        setLayout(new GridBagLayout());

        ModernCard card = new ModernCard(new BorderLayout(0, 16));
        card.setPreferredSize(new Dimension(500, 620));
        card.setBorder(new EmptyBorder(22, 26, 20, 26));

        // ==========================================
        // CABECERA
        // ==========================================
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        JLabel lblEmblema = new JLabel("🏛️ UNIVERSIDAD DEL CAUCA");
        lblEmblema.setFont(UITheme.FONT_SMALL_BOLD);
        lblEmblema.setForeground(UITheme.COLOR_PRIMARY);
        lblEmblema.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Crear Nueva Cuenta");
        lblTitulo.setFont(UITheme.FONT_TITLE_LARGE);
        lblTitulo.setForeground(UITheme.COLOR_PRIMARY_DARK);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Complete los datos requeridos para registrarse en el sistema");
        lblSubtitulo.setFont(UITheme.FONT_SMALL);
        lblSubtitulo.setForeground(UITheme.COLOR_TEXT_MUTED);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblEmblema);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(lblTitulo);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(lblSubtitulo);

        card.add(headerPanel, BorderLayout.NORTH);

        // ==========================================
        // FORMULARIO
        // ==========================================
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 0, 8));
        formPanel.setOpaque(false);

        // Login
        JLabel lblLogin = new JLabel("Login de usuario:");
        lblLogin.setFont(UITheme.FONT_REGULAR_BOLD);
        txtLogin = new JTextField();
        UITheme.styleTextField(txtLogin);

        // Nombre Completo
        JLabel lblNombre = new JLabel("Nombre completo:");
        lblNombre.setFont(UITheme.FONT_REGULAR_BOLD);
        txtNombre = new JTextField();
        UITheme.styleTextField(txtNombre);

        // Fila doble: Rol y Estado
        JPanel panelRolEstado = new JPanel(new GridLayout(1, 2, 12, 0));
        panelRolEstado.setOpaque(false);

        JPanel panelRol = new JPanel(new BorderLayout(0, 4));
        panelRol.setOpaque(false);
        JLabel lblRol = new JLabel("Rol en el sistema:");
        lblRol.setFont(UITheme.FONT_REGULAR_BOLD);
        comboRol = new JComboBox<>(Rol.values());
        UITheme.styleComboBox(comboRol);
        panelRol.add(lblRol, BorderLayout.NORTH);
        panelRol.add(comboRol, BorderLayout.CENTER);

        JPanel panelEstado = new JPanel(new BorderLayout(0, 4));
        panelEstado.setOpaque(false);
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(UITheme.FONT_REGULAR_BOLD);
        comboEstado = new JComboBox<>(EstadoUsuario.values());
        UITheme.styleComboBox(comboEstado);
        comboEstado.setSelectedItem(EstadoUsuario.ACTIVO);
        panelEstado.add(lblEstado, BorderLayout.NORTH);
        panelEstado.add(comboEstado, BorderLayout.CENTER);

        panelRolEstado.add(panelRol);
        panelRolEstado.add(panelEstado);

        // Descripción dinámica de rol
        lblRolDescripcion = new JLabel(" ");
        lblRolDescripcion.setFont(UITheme.FONT_SMALL);
        lblRolDescripcion.setForeground(UITheme.COLOR_PRIMARY);
        actualizarDescripcionRol();
        comboRol.addActionListener(e -> actualizarDescripcionRol());

        // Contraseñas
        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setFont(UITheme.FONT_REGULAR_BOLD);
        txtPassword = new JPasswordField();
        UITheme.stylePasswordField(txtPassword);

        JLabel lblConfirm = new JLabel("Confirmar contraseña:");
        lblConfirm.setFont(UITheme.FONT_REGULAR_BOLD);
        txtConfirmPassword = new JPasswordField();
        UITheme.stylePasswordField(txtConfirmPassword);

        // Mensaje de estado
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(UITheme.FONT_SMALL);
        lblMensaje.setForeground(UITheme.COLOR_DANGER);

        formPanel.add(lblLogin);
        formPanel.add(txtLogin);
        formPanel.add(lblNombre);
        formPanel.add(txtNombre);
        formPanel.add(panelRolEstado);
        formPanel.add(lblRolDescripcion);
        formPanel.add(lblPass);
        formPanel.add(txtPassword);
        formPanel.add(lblConfirm);
        formPanel.add(txtConfirmPassword);
        formPanel.add(lblMensaje);

        card.add(formPanel, BorderLayout.CENTER);

        // ==========================================
        // BOTONES DE ACCIÓN
        // ==========================================
        JPanel actionsPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        actionsPanel.setOpaque(false);

        ModernButton btnRegistrar = new ModernButton("Completar Registro", ModernButton.Variant.SUCCESS);
        ModernButton btnVolver = new ModernButton("Volver al Login", ModernButton.Variant.SECONDARY);

        actionsPanel.add(btnRegistrar);
        actionsPanel.add(btnVolver);

        card.add(actionsPanel, BorderLayout.SOUTH);

        // Eventos
        btnRegistrar.addActionListener(e -> registrarUsuario());
        btnVolver.addActionListener(e -> volver());

        add(card);
    }

    private void actualizarDescripcionRol() {
        Rol rol = (Rol) comboRol.getSelectedItem();
        if (rol == null) return;
        String desc = switch (rol) {
            case ADMINISTRADOR -> "Permisos completos: Gestión de usuarios y control de plugins.";
            case AUTOR -> "Crea y genera preguntas con plugins, y envía a revisión.";
            case REVISOR -> "Bandeja de revisión: Evalúa, aprueba o rechaza preguntas con notas.";
            case DOCENTE -> "Consulta métricas estadísticas y gráficas del banco.";
            case ESTUDIANTE -> "Realiza simulacros interactivos de evaluación tipo Saber Pro.";
        };
        lblRolDescripcion.setText("ℹ️ " + desc);
    }

    private void registrarUsuario() {
        lblMensaje.setText(" ");
        String login = txtLogin.getText().trim();
        String nombre = txtNombre.getText().trim();
        Rol rol = (Rol) comboRol.getSelectedItem();
        EstadoUsuario estado = (EstadoUsuario) comboEstado.getSelectedItem();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());

        if (login.isBlank() || nombre.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            lblMensaje.setText("⚠️ Todos los campos son obligatorios.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            lblMensaje.setText("❌ Las contraseñas no coinciden.");
            txtConfirmPassword.setText("");
            txtConfirmPassword.requestFocusInWindow();
            return;
        }

        Usuario usuario = new Usuario(login, nombre, rol, estado, password);

        boolean creado = usuarioServices.crearUsuario(usuario);

        if (creado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Usuario " + nombre + " (" + login + ") registrado con éxito.\nAhora puede iniciar sesión con su contraseña.",
                    "Registro Exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );
            limpiarCampos();
            volver();
        } else {
            lblMensaje.setText("❌ No fue posible registrar: el usuario ya existe o la contraseña no cumple requisitos.");
        }
    }

    private void limpiarCampos() {
        txtLogin.setText("");
        txtNombre.setText("");
        comboRol.setSelectedIndex(0);
        comboEstado.setSelectedItem(EstadoUsuario.ACTIVO);
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        lblMensaje.setText(" ");
    }

    private void volver() {
        dispose();
        if (ventanaAnterior != null) {
            ventanaAnterior.setVisible(true);
        }
    }
}