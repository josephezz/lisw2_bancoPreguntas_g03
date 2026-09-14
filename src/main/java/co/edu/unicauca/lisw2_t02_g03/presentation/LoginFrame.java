package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.services.AuthServices;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final AuthServices authServices;
    private final UsuarioServices usuarioServices;
    private final PreguntasController preguntasController;

    private JTextField txtLogin;
    private JPasswordField txtPassword;

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

        setTitle("Sistema de Usuarios");
        setSize(450, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void crearInterfaz() {

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout(10, 10));
        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 35, 25, 35
                )
        );

        // ==============================
        // TITULO
        // ==============================

        JLabel lblTitulo =
                new JLabel(
                        "SISTEMA DE USUARIOS",
                        SwingConstants.CENTER
                );

        lblTitulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        JLabel lblSubtitulo =
                new JLabel(
                        "Inicio de sesión",
                        SwingConstants.CENTER
                );

        lblSubtitulo.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        JPanel panelTitulo =
                new JPanel(new GridLayout(2, 1));

        panelTitulo.add(lblTitulo);
        panelTitulo.add(lblSubtitulo);

        panelPrincipal.add(
                panelTitulo,
                BorderLayout.NORTH
        );

        // ==============================
        // FORMULARIO
        // ==============================

        JPanel panelFormulario =
                new JPanel(
                        new GridLayout(
                                4,
                                1,
                                5,
                                5
                        )
                );

        JLabel lblLogin =
                new JLabel("Usuario:");

        txtLogin =
                new JTextField();

        JLabel lblPassword =
                new JLabel("Contraseña:");

        txtPassword =
                new JPasswordField();

        panelFormulario.add(lblLogin);
        panelFormulario.add(txtLogin);
        panelFormulario.add(lblPassword);
        panelFormulario.add(txtPassword);

        panelPrincipal.add(
                panelFormulario,
                BorderLayout.CENTER
        );

        // ==============================
        // BOTONES
        // ==============================

        JButton btnIngresar =
                new JButton("Ingresar");

        JButton btnRegistrarse =
                new JButton("Registrarse");

        btnIngresar.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        btnRegistrarse.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        JPanel panelBotones =
                new JPanel(
                        new GridLayout(
                                2,
                                1,
                                5,
                                8
                        )
                );

        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistrarse);

        panelPrincipal.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        // ==============================
        // EVENTOS
        // ==============================

        btnIngresar.addActionListener(
                e -> iniciarSesion()
        );

        btnRegistrarse.addActionListener(
                e -> abrirRegistro()
        );

        txtPassword.addActionListener(
                e -> iniciarSesion()
        );

        add(panelPrincipal);
    }

    private void iniciarSesion() {

        String login =
                txtLogin.getText().trim();

        String password =
                new String(
                        txtPassword.getPassword()
                );

        if (login.isBlank() || password.isBlank()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar usuario y contraseña.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Usuario usuario =
                authServices.iniciarSesion(
                        login,
                        password
                );

        if (usuario != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bienvenido, "
                            + usuario.getNombreCompleto(),
                    "Inicio de sesión",
                    JOptionPane.INFORMATION_MESSAGE
            );

            MenuFrame menuFrame =
                    new MenuFrame(
                            usuario,
                            usuarioServices,
                            authServices,
                            preguntasController
                    );

            menuFrame.setVisible(true);

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario o contraseña incorrectos.\n"
                            + "También puede que el usuario "
                            + "esté inactivo.",
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE
            );

            txtPassword.setText("");
        }
    }

    private void abrirRegistro() {

        RegistroFrame registroFrame =
                new RegistroFrame(
                        usuarioServices,
                        this
                );

        registroFrame.setVisible(true);

        setVisible(false);
    }
}