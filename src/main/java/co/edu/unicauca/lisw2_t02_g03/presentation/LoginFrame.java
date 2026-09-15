package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.services.AuthServices;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Ventana de inicio de sesión con diseño universitario moderno institucional (Universidad del Cauca).
 */
public class LoginFrame extends JFrame {

    private final AuthServices authServices;
    private final UsuarioServices usuarioServices;
    private final PreguntasController preguntasController;

    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JLabel lblError;

    public LoginFrame(
            AuthServices authServices,
            UsuarioServices usuarioServices,
            PreguntasController preguntasController) {

        if (authServices == null || usuarioServices == null || preguntasController == null) {
            throw new IllegalArgumentException("Las dependencias de LoginFrame son obligatorias.");
        }

        this.authServices = authServices;
        this.usuarioServices = usuarioServices;
        this.preguntasController = preguntasController;

        configurarVentana();
        crearInterfaz();
    }

    private void configurarVentana() {
        setTitle("Banco de Preguntas Saber Pro - Universidad del Cauca");
        setSize(480, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UITheme.COLOR_BG);
    }

    private void crearInterfaz() {
        setLayout(new GridBagLayout());

        // Tarjeta central elevada
        ModernCard card = new ModernCard(new BorderLayout(0, 16));
        card.setPreferredSize(new Dimension(410, 480));
        card.setBorder(new EmptyBorder(26, 28, 24, 28));

        // ==========================================
        // CABECERA CON IDENTIDAD INSTITUCIONAL
        // ==========================================
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        // Insignia / Emblema institucional
        JLabel lblEmblema = new JLabel("🏛️ UNIVERSIDAD DEL CAUCA");
        lblEmblema.setFont(UITheme.FONT_SMALL_BOLD);
        lblEmblema.setForeground(UITheme.COLOR_PRIMARY);
        lblEmblema.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitulo = new JLabel("Banco de Preguntas");
        lblTitulo.setFont(UITheme.FONT_TITLE_LARGE);
        lblTitulo.setForeground(UITheme.COLOR_PRIMARY_DARK);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSaberPro = new JLabel("Módulo Saber Pro");
        lblSaberPro.setFont(UITheme.FONT_TITLE_SMALL);
        lblSaberPro.setForeground(UITheme.COLOR_SECONDARY);
        lblSaberPro.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Ingrese sus credenciales para acceder a la plataforma");
        lblSubtitulo.setFont(UITheme.FONT_SMALL);
        lblSubtitulo.setForeground(UITheme.COLOR_TEXT_MUTED);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblEmblema);
        headerPanel.add(Box.createVerticalStrut(4));
        headerPanel.add(lblTitulo);
        headerPanel.add(lblSaberPro);
        headerPanel.add(Box.createVerticalStrut(6));
        headerPanel.add(lblSubtitulo);

        card.add(headerPanel, BorderLayout.NORTH);

        // ==========================================
        // FORMULARIO DE ACCESO
        // ==========================================
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        JLabel lblLogin = new JLabel("Usuario o Login:");
        lblLogin.setFont(UITheme.FONT_REGULAR_BOLD);
        lblLogin.setForeground(UITheme.COLOR_TEXT_PRIMARY);
        lblLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtLogin = new JTextField();
        UITheme.styleTextField(txtLogin);
        txtLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtLogin.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(UITheme.FONT_REGULAR_BOLD);
        lblPassword.setForeground(UITheme.COLOR_TEXT_PRIMARY);
        lblPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        UITheme.stylePasswordField(txtPassword);
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Mensaje de error integrado
        lblError = new JLabel(" ");
        lblError.setFont(UITheme.FONT_SMALL);
        lblError.setForeground(UITheme.COLOR_DANGER);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(lblLogin);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(txtLogin);
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(lblPassword);
        formPanel.add(Box.createVerticalStrut(4));
        formPanel.add(txtPassword);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(lblError);

        card.add(formPanel, BorderLayout.CENTER);

        // ==========================================
        // BOTONES DE ACCIÓN
        // ==========================================
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setOpaque(false);

        ModernButton btnIngresar = new ModernButton("Iniciar Sesión", ModernButton.Variant.PRIMARY);
        btnIngresar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);

        ModernButton btnRegistrarse = new ModernButton("Registrarse como nuevo usuario", ModernButton.Variant.OUTLINE);
        btnRegistrarse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnRegistrarse.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblFooter = new JLabel("© 2026 Universidad del Cauca • Popayán");
        lblFooter.setFont(UITheme.FONT_SMALL);
        lblFooter.setForeground(UITheme.COLOR_TEXT_MUTED);
        lblFooter.setAlignmentX(Component.CENTER_ALIGNMENT);

        actionsPanel.add(btnIngresar);
        actionsPanel.add(Box.createVerticalStrut(8));
        actionsPanel.add(btnRegistrarse);
        actionsPanel.add(Box.createVerticalStrut(14));
        actionsPanel.add(lblFooter);

        card.add(actionsPanel, BorderLayout.SOUTH);

        // Eventos
        btnIngresar.addActionListener(e -> iniciarSesion());
        btnRegistrarse.addActionListener(e -> abrirRegistro());
        txtPassword.addActionListener(e -> iniciarSesion());
        txtLogin.addActionListener(e -> txtPassword.requestFocusInWindow());

        add(card);
    }

    private void iniciarSesion() {
        lblError.setText(" ");
        String login = txtLogin.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (login.isBlank() || password.isBlank()) {
            lblError.setText("⚠️ Ingrese su usuario y contraseña.");
            return;
        }

        Usuario usuario = authServices.iniciarSesion(login, password);

        if (usuario != null) {
            MenuFrame menuFrame = new MenuFrame(
                    usuario,
                    usuarioServices,
                    authServices,
                    preguntasController
            );
            menuFrame.setVisible(true);
            dispose();
        } else {
            lblError.setText("❌ Credenciales incorrectas o usuario inactivo.");
            txtPassword.setText("");
            txtPassword.requestFocusInWindow();
        }
    }

    private void abrirRegistro() {
        RegistroFrame registroFrame = new RegistroFrame(
                usuarioServices,
                this
        );
        registroFrame.setVisible(true);
        setVisible(false);
    }
}