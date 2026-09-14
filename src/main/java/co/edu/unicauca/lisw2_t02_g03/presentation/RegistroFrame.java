package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Rol;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import javax.swing.*;
import java.awt.*;

public class RegistroFrame extends JFrame {

    private final UsuarioServices usuarioServices;
    private final JFrame ventanaAnterior;

    private JTextField txtLogin;
    private JTextField txtNombre;
    private JComboBox<Rol> comboRol;
    private JComboBox<EstadoUsuario> comboEstado;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;

    public RegistroFrame(
            UsuarioServices usuarioServices,
            JFrame ventanaAnterior) {

        this.usuarioServices = usuarioServices;
        this.ventanaAnterior = ventanaAnterior;

        configurarVentana();
        crearInterfaz();
    }

    private void configurarVentana() {

        setTitle("Registro de Usuario");
        setSize(500, 600);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void crearInterfaz() {

        JPanel panelPrincipal =
                new JPanel(new BorderLayout(10, 10));

        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 35, 20, 35
                )
        );

        // ==========================================
        // TITULO
        // ==========================================

        JLabel titulo =
                new JLabel(
                        "REGISTRAR USUARIO",
                        SwingConstants.CENTER
                );

        titulo.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        23
                )
        );

        panelPrincipal.add(
                titulo,
                BorderLayout.NORTH
        );

        // ==========================================
        // FORMULARIO
        // ==========================================

        JPanel formulario =
                new JPanel(
                        new GridLayout(
                                12,
                                1,
                                5,
                                5
                        )
                );

        // Login
        formulario.add(
                new JLabel("Login:")
        );

        txtLogin = new JTextField();

        formulario.add(txtLogin);

        // Nombre
        formulario.add(
                new JLabel("Nombre completo:")
        );

        txtNombre = new JTextField();

        formulario.add(txtNombre);

        // Rol
        formulario.add(
                new JLabel("Rol:")
        );

        comboRol =
                new JComboBox<>(
                        Rol.values()
                );

        formulario.add(comboRol);

        // Estado
        formulario.add(
                new JLabel("Estado:")
        );

        comboEstado =
                new JComboBox<>(
                        EstadoUsuario.values()
                );

        // Por seguridad/usabilidad, dejamos ACTIVO
        // como opción inicial.
        comboEstado.setSelectedItem(
                EstadoUsuario.ACTIVO
        );

        formulario.add(comboEstado);

        // Contraseña
        formulario.add(
                new JLabel("Contraseña:")
        );

        txtPassword =
                new JPasswordField();

        formulario.add(txtPassword);

        // Confirmar contraseña
        formulario.add(
                new JLabel("Confirmar contraseña:")
        );

        txtConfirmPassword =
                new JPasswordField();

        formulario.add(txtConfirmPassword);

        panelPrincipal.add(
                formulario,
                BorderLayout.CENTER
        );

        // ==========================================
        // BOTONES
        // ==========================================

        JButton btnRegistrar =
                new JButton("Registrar");

        JButton btnVolver =
                new JButton("Volver");

        JPanel panelBotones =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                10,
                                0
                        )
                );

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);

        panelPrincipal.add(
                panelBotones,
                BorderLayout.SOUTH
        );

        // ==========================================
        // EVENTOS
        // ==========================================

        btnRegistrar.addActionListener(
                e -> registrarUsuario()
        );

        btnVolver.addActionListener(
                e -> volver()
        );

        add(panelPrincipal);
    }

    private void registrarUsuario() {

        String login =
                txtLogin.getText().trim();

        String nombre =
                txtNombre.getText().trim();

        Rol rol =
                (Rol) comboRol.getSelectedItem();

        EstadoUsuario estado =
                (EstadoUsuario) comboEstado.getSelectedItem();

        String password =
                new String(
                        txtPassword.getPassword()
                );

        String confirmPassword =
                new String(
                        txtConfirmPassword.getPassword()
                );

        // ==========================================
        // VALIDACIONES BASICAS
        // ==========================================

        if (
                login.isBlank()
                        || nombre.isBlank()
                        || password.isBlank()
                        || confirmPassword.isBlank()
        ) {

            JOptionPane.showMessageDialog(
                    this,
                    "Todos los campos son obligatorios.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!password.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Las contraseñas no coinciden.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // ==========================================
        // CREAR USUARIO
        // ==========================================

        Usuario usuario =
                new Usuario(
                        login,
                        nombre,
                        rol,
                        estado,
                        password
                );

        boolean creado =
                usuarioServices.crearUsuario(
                        usuario
                );

        if (creado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Usuario registrado correctamente.",
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limpiarCampos();

            volver();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar el usuario.\n"
                            + "El login puede existir o "
                            + "los datos pueden ser inválidos.",
                    "Error de registro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarCampos() {

        txtLogin.setText("");
        txtNombre.setText("");

        comboRol.setSelectedIndex(0);

        comboEstado.setSelectedItem(
                EstadoUsuario.ACTIVO
        );

        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }

    private void volver() {

        dispose();

        if (ventanaAnterior != null) {
            ventanaAnterior.setVisible(true);
        }
    }
}