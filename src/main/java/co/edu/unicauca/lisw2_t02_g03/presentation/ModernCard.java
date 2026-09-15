package co.edu.unicauca.lisw2_t02_g03.presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Panel contenedor estilo tarjeta con fondo blanco, bordes redondeados y sombra sutil.
 */
public class ModernCard extends JPanel {

    private int cornerRadius = 12;
    private Color cardBackground = UITheme.COLOR_SURFACE;
    private Color borderColor = UITheme.COLOR_BORDER;

    public ModernCard(LayoutManager layout) {
        super(layout);
        init();
    }

    public ModernCard() {
        super(new BorderLayout());
        init();
    }

    private void init() {
        setOpaque(false);
        setBorder(new EmptyBorder(16, 18, 16, 18));
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    public void setCardBackground(Color color) {
        this.cardBackground = color;
        repaint();
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Relleno de tarjeta
        g2.setColor(cardBackground);
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));

        // Borde sutil
        g2.setColor(borderColor);
        g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, width - 1, height - 1, cornerRadius, cornerRadius));

        g2.dispose();
        super.paintComponent(g);
    }
}
