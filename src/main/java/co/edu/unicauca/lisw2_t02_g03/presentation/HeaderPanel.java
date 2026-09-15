package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.model.Usuario;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Barra superior institucional con branding de la Universidad del Cauca, datos del usuario activo y control de sesión.
 */
public class HeaderPanel extends JPanel {

    public HeaderPanel(Usuario usuario, Runnable onCerrarSesion) {
        setLayout(new BorderLayout(16, 0));
        setBackground(UITheme.COLOR_PRIMARY_DARK);
        setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 2, 0, UITheme.COLOR_PRIMARY),
                new EmptyBorder(12, 22, 12, 22)
        ));

        // Lado Izquierdo: Branding institucional
        JPanel panelBranding = new JPanel();
        panelBranding.setLayout(new BoxLayout(panelBranding, BoxLayout.Y_AXIS));
        panelBranding.setOpaque(false);

        JLabel lblUniversidad = new JLabel("🏛️ UNIVERSIDAD DEL CAUCA");
        lblUniversidad.setFont(UITheme.FONT_SMALL_BOLD);
        lblUniversidad.setForeground(new Color(147, 197, 253));

        JLabel lblSistema = new JLabel("Banco de Preguntas Saber Pro");
        lblSistema.setFont(UITheme.FONT_TITLE_MEDIUM);
        lblSistema.setForeground(Color.WHITE);

        panelBranding.add(lblUniversidad);
        panelBranding.add(Box.createVerticalStrut(2));
        panelBranding.add(lblSistema);

        // Lado Derecho: Usuario activo, Badge de Rol y Cerrar Sesión
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        panelUsuario.setOpaque(false);

        JPanel panelDatosUsuario = new JPanel();
        panelDatosUsuario.setLayout(new BoxLayout(panelDatosUsuario, BoxLayout.Y_AXIS));
        panelDatosUsuario.setOpaque(false);

        JLabel lblNombre = new JLabel(usuario.getNombreCompleto());
        lblNombre.setFont(UITheme.FONT_REGULAR_BOLD);
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JLabel lblLogin = new JLabel("@" + usuario.getLogin());
        lblLogin.setFont(UITheme.FONT_SMALL);
        lblLogin.setForeground(new Color(203, 213, 225));
        lblLogin.setAlignmentX(Component.RIGHT_ALIGNMENT);

        panelDatosUsuario.add(lblNombre);
        panelDatosUsuario.add(lblLogin);

        StatusBadge badgeRol = StatusBadge.fromRol(usuario.getRol());

        ModernButton btnCerrar = new ModernButton("Cerrar Sesión", ModernButton.Variant.DANGER);
        btnCerrar.setFont(UITheme.FONT_SMALL_BOLD);
        btnCerrar.addActionListener(e -> {
            if (onCerrarSesion != null) {
                onCerrarSesion.run();
            }
        });

        panelUsuario.add(panelDatosUsuario);
        panelUsuario.add(badgeRol);
        panelUsuario.add(btnCerrar);

        add(panelBranding, BorderLayout.WEST);
        add(panelUsuario, BorderLayout.EAST);
    }
}
