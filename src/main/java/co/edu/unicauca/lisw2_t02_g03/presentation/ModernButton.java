package co.edu.unicauca.lisw2_t02_g03.presentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Botón moderno con bordes redondeados, soporte de variantes temáticas y animaciones de hover.
 */
public class ModernButton extends JButton {

    public enum Variant {
        PRIMARY,
        SECONDARY,
        SUCCESS,
        DANGER,
        OUTLINE,
        ACCENT,
        GHOST
    }

    private final Variant variant;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private int cornerRadius = 8;

    public ModernButton(String text, Variant variant) {
        super(text);
        this.variant = variant;
        init();
    }

    public ModernButton(String text) {
        this(text, Variant.PRIMARY);
    }

    private void init() {
        setFont(UITheme.FONT_BUTTON);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setMargin(new Insets(8, 16, 8, 16));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    isHovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    isPressed = true;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        Color bg = getBackgroundColor();
        Color fg = getForegroundColor();

        // Fondo
        if (variant != Variant.GHOST && (variant != Variant.OUTLINE || isHovered)) {
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));
        }

        // Borde si es outline
        if (variant == Variant.OUTLINE) {
            g2.setColor(isHovered ? UITheme.COLOR_PRIMARY_LIGHT : UITheme.COLOR_BORDER);
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, width - 1, height - 1, cornerRadius, cornerRadius));
        }

        // Texto e ícono
        g2.setColor(isEnabled() ? fg : UITheme.COLOR_TEXT_MUTED);
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();

        int stringWidth = fm.stringWidth(getText());
        int stringHeight = fm.getAscent();
        int x = (width - stringWidth) / 2;
        int y = (height + stringHeight) / 2 - 2;

        g2.drawString(getText(), x, y);
        g2.dispose();
    }

    private Color getBackgroundColor() {
        if (!isEnabled()) {
            return new Color(226, 232, 240);
        }
        return switch (variant) {
            case PRIMARY -> isPressed ? UITheme.COLOR_PRIMARY_DARK :
                    (isHovered ? UITheme.COLOR_PRIMARY_LIGHT : UITheme.COLOR_PRIMARY);
            case SECONDARY -> isPressed ? new Color(51, 65, 85) :
                    (isHovered ? new Color(100, 116, 139) : UITheme.COLOR_SECONDARY);
            case SUCCESS -> isPressed ? new Color(4, 120, 87) :
                    (isHovered ? new Color(5, 150, 105) : UITheme.COLOR_SUCCESS);
            case DANGER -> isPressed ? new Color(185, 28, 28) :
                    (isHovered ? new Color(220, 38, 38) : UITheme.COLOR_DANGER);
            case ACCENT -> isPressed ? new Color(180, 83, 9) :
                    (isHovered ? new Color(217, 119, 6) : UITheme.COLOR_WARNING);
            case OUTLINE -> isHovered ? new Color(241, 245, 249) : Color.WHITE;
            case GHOST -> isHovered ? new Color(241, 245, 249) : new Color(0, 0, 0, 0);
        };
    }

    private Color getForegroundColor() {
        if (!isEnabled()) {
            return UITheme.COLOR_TEXT_MUTED;
        }
        return switch (variant) {
            case PRIMARY, SECONDARY, SUCCESS, DANGER, ACCENT -> Color.WHITE;
            case OUTLINE -> isHovered ? UITheme.COLOR_PRIMARY : UITheme.COLOR_TEXT_PRIMARY;
            case GHOST -> UITheme.COLOR_TEXT_PRIMARY;
        };
    }
}
