package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadisticasPreguntas;
import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.infra.Observer;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.EnumMap;
import java.util.Map;

/**
 * Segunda vista observadora: gráfica de pastel con porcentajes y leyenda legible.
 */
public class GraficaPastelView extends JPanel implements Observer<EstadisticasPreguntas> {

    private static final Map<EstadoPregunta, Color> COLORES = crearColores();

    private volatile EstadisticasPreguntas estadisticas = estadisticasVacias();

    public GraficaPastelView() {
        setPreferredSize(new Dimension(430, 390));
        setMinimumSize(new Dimension(360, 330));
        setBorder(BorderFactory.createTitledBorder("Distribución porcentual"));
        getAccessibleContext().setAccessibleName("Gráfica de pastel de preguntas por estado");
    }

    @Override
    public void actualizar(EstadisticasPreguntas nuevasEstadisticas) {
        if (nuevasEstadisticas == null) {
            return;
        }

        Runnable actualizacion = () -> {
            estadisticas = nuevasEstadisticas;
            repaint();
        };

        if (SwingUtilities.isEventDispatchThread()) {
            actualizacion.run();
        } else {
            SwingUtilities.invokeLater(actualizacion);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g2 = (Graphics2D) graphics.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            int diametro = Math.max(120, Math.min(220, Math.min(ancho - 50, alto - 155)));
            int x = Math.max(20, (ancho - diametro) / 2);
            int y = 38;

            long total = estadisticas.getTotal();
            if (total == 0) {
                g2.setColor(Color.GRAY);
                g2.drawOval(x, y, diametro, diametro);
                centrarTexto(g2, "Banco vacío", x, y + diametro / 2 - 8, diametro);
                centrarTexto(g2, "0 preguntas", x, y + diametro / 2 + 12, diametro);
            } else {
                dibujarPastel(g2, x, y, diametro);
            }

            dibujarLeyenda(g2, 24, y + diametro + 28);
        } finally {
            g2.dispose();
        }
    }

    private void dibujarPastel(Graphics2D g2, int x, int y, int diametro) {
        int inicio = 0;
        EstadoPregunta[] estados = EstadoPregunta.values();

        for (int i = 0; i < estados.length; i++) {
            EstadoPregunta estado = estados[i];
            int arco = i == estados.length - 1
                    ? 360 - inicio
                    : (int) Math.round(estadisticas.getPorcentaje(estado) * 3.6);

            if (arco > 0) {
                g2.setColor(COLORES.get(estado));
                g2.fillArc(x, y, diametro, diametro, inicio, arco);
                dibujarPorcentajeEnSector(g2, estado, inicio, arco, x, y, diametro);
            }
            inicio += arco;
        }

        g2.setColor(Color.DARK_GRAY);
        g2.drawOval(x, y, diametro, diametro);
    }

    private void dibujarPorcentajeEnSector(
            Graphics2D g2,
            EstadoPregunta estado,
            int inicio,
            int arco,
            int x,
            int y,
            int diametro) {

        if (arco < 18) {
            return;
        }

        double anguloMedio = Math.toRadians(inicio + arco / 2.0);
        double radio = diametro * 0.30;
        int cx = x + diametro / 2;
        int cy = y + diametro / 2;
        int tx = (int) Math.round(cx + Math.cos(anguloMedio) * radio);
        int ty = (int) Math.round(cy - Math.sin(anguloMedio) * radio);

        String texto = String.format("%.1f%%", estadisticas.getPorcentaje(estado));
        Font anterior = g2.getFont();
        g2.setFont(anterior.deriveFont(Font.BOLD, 12f));
        FontMetrics fm = g2.getFontMetrics();

        g2.setColor(Color.WHITE);
        g2.drawString(texto, tx - fm.stringWidth(texto) / 2, ty + fm.getAscent() / 2);
        g2.setFont(anterior);
    }

    private void dibujarLeyenda(Graphics2D g2, int x, int y) {
        Font anterior = g2.getFont();
        g2.setFont(anterior.deriveFont(13f));

        int fila = 0;
        for (EstadoPregunta estado : EstadoPregunta.values()) {
            int yy = y + fila * 27;
            g2.setColor(COLORES.get(estado));
            g2.fillRect(x, yy - 12, 14, 14);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(x, yy - 12, 14, 14);

            String texto = String.format(
                    "%s: %d (%.1f%%)",
                    estado,
                    estadisticas.getConteo(estado),
                    estadisticas.getPorcentaje(estado));
            g2.drawString(texto, x + 23, yy);
            fila++;
        }

        g2.setFont(anterior);
    }

    private static void centrarTexto(Graphics2D g2, String texto, int x, int y, int ancho) {
        FontMetrics fm = g2.getFontMetrics();
        int tx = x + (ancho - fm.stringWidth(texto)) / 2;
        g2.drawString(texto, tx, y);
    }

    private static Map<EstadoPregunta, Color> crearColores() {
        EnumMap<EstadoPregunta, Color> colores = new EnumMap<>(EstadoPregunta.class);
        colores.put(EstadoPregunta.BORRADOR, new Color(230, 126, 34));
        colores.put(EstadoPregunta.PENDIENTE_REVISION, new Color(52, 152, 219));
        colores.put(EstadoPregunta.APROBADA, new Color(39, 174, 96));
        colores.put(EstadoPregunta.RECHAZADA, new Color(231, 76, 60));
        colores.put(EstadoPregunta.ELIMINADA, new Color(192, 57, 43));
        return colores;
    }

    private static EstadisticasPreguntas estadisticasVacias() {
        EnumMap<EstadoPregunta, Long> conteos = new EnumMap<>(EstadoPregunta.class);
        EnumMap<EstadoPregunta, Double> porcentajes = new EnumMap<>(EstadoPregunta.class);
        for (EstadoPregunta estado : EstadoPregunta.values()) {
            conteos.put(estado, 0L);
            porcentajes.put(estado, 0.0);
        }
        return new EstadisticasPreguntas(conteos, porcentajes, 0);
    }
}
