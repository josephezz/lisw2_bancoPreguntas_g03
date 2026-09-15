package co.edu.unicauca.lisw2_t02_g03.presentation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * Panel de previsualización para recursos multimedia (imágenes locales o remotas, o indicadores de audio/video).
 */
public class ImagePreviewPanel extends JPanel {

    private Image cachedImage = null;
    private String statusText = "Sin recurso cargado";
    private boolean isImage = false;

    public ImagePreviewPanel() {
        setPreferredSize(new Dimension(280, 160));
        setMinimumSize(new Dimension(200, 120));
        setBackground(new Color(248, 250, 252));
        setBorder(new LineBorder(UITheme.COLOR_BORDER, 1, true));
    }

    public void cargarRecurso(String rutaORecurso) {
        cachedImage = null;
        isImage = false;

        if (rutaORecurso == null || rutaORecurso.isBlank()) {
            statusText = "Sin recurso multimedia";
            repaint();
            return;
        }

        String lower = rutaORecurso.trim().toLowerCase();
        if (lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".gif") || lower.endsWith(".bmp") || lower.endsWith(".webp")) {
            // Intentar cargar como imagen local o remota
            try {
                BufferedImage img;
                if (lower.startsWith("http://") || lower.startsWith("https://")) {
                    img = ImageIO.read(URI.create(rutaORecurso.trim()).toURL());
                } else {
                    File file = new File(rutaORecurso.trim());
                    if (file.exists() && file.isFile()) {
                        img = ImageIO.read(file);
                    } else {
                        statusText = "Archivo no encontrado: " + file.getName();
                        repaint();
                        return;
                    }
                }

                if (img != null) {
                    cachedImage = img;
                    isImage = true;
                    statusText = "";
                } else {
                    statusText = "Formato no legible";
                }
            } catch (Exception ex) {
                statusText = "No se pudo cargar: " + ex.getMessage();
            }
        } else if (lower.endsWith(".mp3") || lower.endsWith(".wav") || lower.endsWith(".ogg")) {
            statusText = "Recurso de Audio: " + new File(rutaORecurso).getName();
        } else if (lower.endsWith(".mp4") || lower.endsWith(".avi") || lower.endsWith(".mkv")) {
            statusText = "Recurso de Video: " + new File(rutaORecurso).getName();
        } else {
            statusText = "Recurso: " + rutaORecurso;
        }

        repaint();
    }

    public void limpiar() {
        cachedImage = null;
        isImage = false;
        statusText = "Sin recurso cargado";
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();

        if (isImage && cachedImage != null) {
            int imgW = cachedImage.getWidth(null);
            int imgH = cachedImage.getHeight(null);

            if (imgW > 0 && imgH > 0) {
                double scale = Math.min((double) (w - 16) / imgW, (double) (h - 16) / imgH);
                int drawW = (int) (imgW * scale);
                int drawH = (int) (imgH * scale);
                int x = (w - drawW) / 2;
                int y = (h - drawH) / 2;

                g2.drawImage(cachedImage, x, y, drawW, drawH, null);
            }
        } else {
            g2.setColor(UITheme.COLOR_TEXT_MUTED);
            g2.setFont(UITheme.FONT_SUBTITLE);
            FontMetrics fm = g2.getFontMetrics();
            int strW = fm.stringWidth(statusText);
            g2.drawString(statusText, Math.max(10, (w - strW) / 2), (h + fm.getAscent()) / 2 - 2);
        }

        g2.dispose();
    }
}
