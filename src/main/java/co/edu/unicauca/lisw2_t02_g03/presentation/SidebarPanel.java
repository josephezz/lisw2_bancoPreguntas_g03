package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.model.Rol;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Barra lateral de navegación adaptativa según el rol del usuario autenticado.
 */
public class SidebarPanel extends JPanel {

    private final Consumer<String> onNavigate;
    private final List<ModernButton> navButtons = new ArrayList<>();
    private ModernButton activeBtn = null;

    public SidebarPanel(Rol rol, Consumer<String> onNavigate, Runnable onAbrirBancoEstados) {
        this.onNavigate = onNavigate;

        setPreferredSize(new Dimension(240, 0));
        setBackground(UITheme.COLOR_SIDEBAR_BG);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(18, 12, 18, 12));

        // Menú central
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setOpaque(false);

        JLabel lblSeccion = new JLabel("NAVEGACIÓN");
        lblSeccion.setFont(UITheme.FONT_SMALL_BOLD);
        lblSeccion.setForeground(UITheme.COLOR_TEXT_ON_DARK_MUTED);
        lblSeccion.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelMenu.add(lblSeccion);
        panelMenu.add(Box.createVerticalStrut(12));

        // Opciones por rol
        switch (rol) {
            case ADMINISTRADOR -> {
                agregarOpcion(panelMenu, "👥 Gestión de Usuarios", "CARD_USUARIOS", true);
                agregarOpcion(panelMenu, "🧩 Control de Plugins", "CARD_PLUGINS", false);
            }
            case AUTOR -> {
                agregarOpcion(panelMenu, "✏️ Crear Pregunta", "CARD_CREAR", true);
                agregarOpcion(panelMenu, "⚡ Generar con Plugin", "CARD_GENERAR_PLUGIN", false);
                agregarOpcion(panelMenu, "📚 Mis Preguntas", "CARD_MIS_PREGUNTAS", false);
            }
            case REVISOR -> {
                agregarOpcion(panelMenu, "🔍 Preguntas por Revisar", "CARD_REVISION", true);
            }
            case DOCENTE -> {
                agregarOpcion(panelMenu, "📊 Estadísticas del Banco", "CARD_DOCENTE", true);
            }
            case ESTUDIANTE -> {
                agregarOpcion(panelMenu, "🎓 Práctica y Simulacro", "CARD_ESTUDIANTE", true);
            }
        }

        add(panelMenu, BorderLayout.CENTER);

        // Opciones globales en la parte inferior
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setOpaque(false);

        JLabel lblUtilidades = new JLabel("SISTEMA");
        lblUtilidades.setFont(UITheme.FONT_SMALL_BOLD);
        lblUtilidades.setForeground(UITheme.COLOR_TEXT_ON_DARK_MUTED);
        lblUtilidades.setAlignmentX(Component.LEFT_ALIGNMENT);

        ModernButton btnGestionBanco = new ModernButton("📋 Gestión de Estados (MVC)", ModernButton.Variant.OUTLINE);
        btnGestionBanco.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btnGestionBanco.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGestionBanco.addActionListener(e -> {
            if (onAbrirBancoEstados != null) {
                onAbrirBancoEstados.run();
            }
        });

        panelInferior.add(lblUtilidades);
        panelInferior.add(Box.createVerticalStrut(8));
        panelInferior.add(btnGestionBanco);

        add(panelInferior, BorderLayout.SOUTH);
    }

    private void agregarOpcion(JPanel container, String texto, String cardId, boolean inicialActivo) {
        ModernButton btn = new ModernButton(texto, inicialActivo ? ModernButton.Variant.PRIMARY : ModernButton.Variant.GHOST);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);

        btn.addActionListener(e -> {
            activarBoton(btn);
            if (onNavigate != null) {
                onNavigate.accept(cardId);
            }
        });

        navButtons.add(btn);
        if (inicialActivo) {
            activeBtn = btn;
        }

        container.add(btn);
        container.add(Box.createVerticalStrut(6));
    }

    private void activarBoton(ModernButton btn) {
        if (activeBtn != null) {
            activeBtn.repaint();
        }
        activeBtn = btn;
        btn.repaint();
    }
}
