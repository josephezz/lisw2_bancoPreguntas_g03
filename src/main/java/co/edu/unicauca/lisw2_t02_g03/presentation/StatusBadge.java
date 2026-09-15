package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Rol;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Componente tipo "chip/pill" para renderizar estados y roles con fondo suave y texto coloreado.
 */
public class StatusBadge extends JLabel {

    private Color backgroundColor = UITheme.COLOR_INFO_BG;
    private Color textColor = UITheme.COLOR_INFO;

    public StatusBadge(String text) {
        super(text, SwingConstants.CENTER);
        init();
    }

    private void init() {
        setFont(UITheme.FONT_SMALL_BOLD);
        setOpaque(false);
        setBorder(new EmptyBorder(3, 10, 3, 10));
    }

    public static StatusBadge fromEstadoPregunta(EstadoPregunta estado) {
        if (estado == null) {
            StatusBadge badge = new StatusBadge("SIN ESTADO");
            badge.setColorScheme(new Color(241, 245, 249), UITheme.COLOR_TEXT_MUTED);
            return badge;
        }
        StatusBadge badge = new StatusBadge(formatearEstadoPregunta(estado));
        switch (estado) {
            case BORRADOR -> badge.setColorScheme(UITheme.COLOR_WARNING_BG, new Color(180, 83, 9));
            case PENDIENTE_REVISION -> badge.setColorScheme(UITheme.COLOR_INFO_BG, new Color(3, 105, 161));
            case APROBADA -> badge.setColorScheme(UITheme.COLOR_SUCCESS_BG, new Color(4, 120, 87));
            case RECHAZADA -> badge.setColorScheme(UITheme.COLOR_DANGER_BG, new Color(185, 28, 28));
            case ELIMINADA -> badge.setColorScheme(new Color(241, 245, 249), new Color(100, 116, 139));
        }
        return badge;
    }

    public static StatusBadge fromEstadoUsuario(EstadoUsuario estado) {
        if (estado == null) {
            return new StatusBadge("DESCONOCIDO");
        }
        StatusBadge badge = new StatusBadge(estado.name());
        if (estado == EstadoUsuario.ACTIVO) {
            badge.setColorScheme(UITheme.COLOR_SUCCESS_BG, new Color(4, 120, 87));
        } else {
            badge.setColorScheme(UITheme.COLOR_DANGER_BG, new Color(185, 28, 28));
        }
        return badge;
    }

    public static StatusBadge fromRol(Rol rol) {
        if (rol == null) {
            return new StatusBadge("SIN ROL");
        }
        StatusBadge badge = new StatusBadge(rol.name());
        switch (rol) {
            case ADMINISTRADOR -> badge.setColorScheme(new Color(243, 232, 255), new Color(107, 33, 168)); // Morado
            case AUTOR -> badge.setColorScheme(new Color(224, 231, 255), new Color(55, 48, 163));         // Índigo
            case REVISOR -> badge.setColorScheme(new Color(254, 243, 199), new Color(180, 83, 9));       // Ámbar
            case DOCENTE -> badge.setColorScheme(new Color(204, 251, 241), new Color(15, 118, 110));      // Teal
            case ESTUDIANTE -> badge.setColorScheme(new Color(219, 234, 254), new Color(29, 78, 216));   // Azul
        }
        return badge;
    }

    public void setColorScheme(Color bg, Color text) {
        this.backgroundColor = bg;
        this.textColor = text;
        setForeground(text);
        repaint();
    }

    private static String formatearEstadoPregunta(EstadoPregunta estado) {
        return switch (estado) {
            case BORRADOR -> "BORRADOR";
            case PENDIENTE_REVISION -> "PENDIENTE REVISIÓN";
            case APROBADA -> "APROBADA";
            case RECHAZADA -> "RECHAZADA";
            case ELIMINADA -> "ELIMINADA";
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        g2.setColor(backgroundColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, height, height));

        g2.dispose();
        super.paintComponent(g);
    }
}
