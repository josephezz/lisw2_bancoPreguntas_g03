package co.edu.unicauca.lisw2_t02_g03.presentation;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Sistema de diseño centralizado para el Banco de Preguntas Saber Pro
 * (Universidad del Cauca).
 * Define paleta de colores institucional, tipografías, bordes y estilos
 * uniformes para componentes Swing.
 */
public final class UITheme {

    private UITheme() {
    }

    // ==========================================
    // PALETA DE COLORES INSTITUCIONAL
    // ==========================================
    public static final Color COLOR_PRIMARY_DARK = new Color(13, 32, 64); // #0D2040 Azul noche institucional
    public static final Color COLOR_PRIMARY = new Color(27, 73, 131); // #1B4983 Azul Unicauca
    public static final Color COLOR_PRIMARY_LIGHT = new Color(59, 130, 246); // #3B82F6 Azul acento claro
    public static final Color COLOR_SECONDARY = new Color(71, 85, 105); // #475569 Slate profesional

    // Fondos y superficies
    public static final Color COLOR_BG = new Color(245, 247, 250); // #F5F7FA Gris claro suave
    public static final Color COLOR_SURFACE = Color.WHITE; // #FFFFFF Superficie tarjetas
    public static final Color COLOR_SIDEBAR_BG = new Color(17, 24, 39); // #111827 Fondo barra lateral
    public static final Color COLOR_SIDEBAR_HOVER = new Color(31, 41, 55); // #1F2937 Hover menú

    // Textos
    public static final Color COLOR_TEXT_PRIMARY = new Color(30, 41, 59); // #1E293B Texto principal
    public static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139); // #64748B Texto secundario/hints
    public static final Color COLOR_TEXT_ON_DARK = new Color(241, 245, 249); // #F1F5F9 Texto sobre fondo oscuro
    public static final Color COLOR_TEXT_ON_DARK_MUTED = new Color(148, 163, 184); // #94A3B8

    // Bordes y divisores
    public static final Color COLOR_BORDER = new Color(226, 232, 240); // #E2E8F0 Bordes suaves
    public static final Color COLOR_BORDER_FOCUS = new Color(59, 130, 246); // #3B82F6 Borde activo

    // Estados de preguntas y usuarios
    public static final Color COLOR_SUCCESS = new Color(16, 185, 129); // #10B981 Verde aprobada/activo
    public static final Color COLOR_SUCCESS_BG = new Color(236, 253, 245); // Verde claro fondo badge
    public static final Color COLOR_WARNING = new Color(245, 158, 11); // #F59E0B Ámbar pendiente/borrador
    public static final Color COLOR_WARNING_BG = new Color(254, 243, 199); // Ámbar claro fondo badge
    public static final Color COLOR_DANGER = new Color(239, 68, 68); // #EF4444 Rojo rechazada/inactivo
    public static final Color COLOR_DANGER_BG = new Color(254, 226, 226); // Rojo claro fondo badge
    public static final Color COLOR_INFO = new Color(14, 165, 233); // #0EA5E9 Azul información
    public static final Color COLOR_INFO_BG = new Color(224, 242, 254); // Azul claro fondo badge

    // ==========================================
    // TIPOGRAFÍAS
    // ==========================================
    private static final String FONT_FAMILY = "Segoe UI";
    private static final String FALLBACK_FAMILY = "Arial";

    private static String getAvailableFontFamily() {
        Font f = new Font(FONT_FAMILY, Font.PLAIN, 12);
        return f.getFamily().equalsIgnoreCase(FONT_FAMILY) ? FONT_FAMILY : FALLBACK_FAMILY;
    }

    private static final String FAMILIA = getAvailableFontFamily();

    public static final Font FONT_TITLE_LARGE = new Font(FAMILIA, Font.BOLD, 22);
    public static final Font FONT_TITLE_MEDIUM = new Font(FAMILIA, Font.BOLD, 17);
    public static final Font FONT_TITLE_SMALL = new Font(FAMILIA, Font.BOLD, 14);

    public static final Font FONT_SUBTITLE = new Font(FAMILIA, Font.PLAIN, 13);
    public static final Font FONT_REGULAR = new Font(FAMILIA, Font.PLAIN, 13);
    public static final Font FONT_REGULAR_BOLD = new Font(FAMILIA, Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font(FAMILIA, Font.PLAIN, 11);
    public static final Font FONT_SMALL_BOLD = new Font(FAMILIA, Font.BOLD, 11);
    public static final Font FONT_BUTTON = new Font(FAMILIA, Font.BOLD, 13);

    // ==========================================
    // ESTILOS DE COMPONENTES
    // ==========================================

    /**
     * Aplica el estilo moderno a un campo de texto simple.
     */
    public static void styleTextField(JTextField textField) {
        textField.setFont(FONT_REGULAR);
        textField.setForeground(COLOR_TEXT_PRIMARY);
        textField.setBackground(Color.WHITE);
        textField.setCaretColor(COLOR_PRIMARY);
        textField.setBorder(createInputBorder());
    }

    /**
     * Aplica el estilo moderno a un campo de contraseña.
     */
    public static void stylePasswordField(JPasswordField passwordField) {
        passwordField.setFont(FONT_REGULAR);
        passwordField.setForeground(COLOR_TEXT_PRIMARY);
        passwordField.setBackground(Color.WHITE);
        passwordField.setCaretColor(COLOR_PRIMARY);
        passwordField.setBorder(createInputBorder());
    }

    /**
     * Aplica el estilo moderno a un JTextArea.
     */
    public static void styleTextArea(JTextArea textArea) {
        textArea.setFont(FONT_REGULAR);
        textArea.setForeground(COLOR_TEXT_PRIMARY);
        textArea.setBackground(Color.WHITE);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(new EmptyBorder(8, 10, 8, 10));
    }

    /**
     * Aplica el estilo moderno a un JComboBox.
     */
    public static <T> void styleComboBox(JComboBox<T> comboBox) {
        comboBox.setFont(FONT_REGULAR);
        comboBox.setForeground(COLOR_TEXT_PRIMARY);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(createInputBorder());
    }

    /**
     * Crea un borde estándar para cajas de entrada de texto.
     */
    public static Border createInputBorder() {
        return new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(7, 10, 7, 10));
    }

    /**
     * Crea un borde para tarjetas y paneles contenedores.
     */
    public static Border createCardBorder() {
        return new CompoundBorder(
                new LineBorder(COLOR_BORDER, 1, true),
                new EmptyBorder(16, 18, 16, 18));
    }

    /**
     * Aplica estilo institucional moderno a un JTable.
     */
    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setRowHeight(34);
        table.setForeground(COLOR_TEXT_PRIMARY);
        table.setSelectionBackground(new Color(224, 242, 254));
        table.setSelectionForeground(COLOR_PRIMARY_DARK);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(241, 245, 249));

        // Encabezado
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_REGULAR_BOLD);
        header.setBackground(new Color(248, 250, 252));
        header.setForeground(COLOR_SECONDARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COLOR_BORDER));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getWidth(), 38));

        // Renderizador con padding para celdas
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                }
                setBorder(new EmptyBorder(4, 10, 4, 10));
                return c;
            }
        };
        table.setDefaultRenderer(Object.class, cellRenderer);
    }

    /**
     * Aplica estilo limpio a un JScrollPane envolvente.
     */
    public static void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(new LineBorder(COLOR_BORDER, 1, true));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    /**
     * Configura el Look & Feel del sistema para optimizar el renderizado de Swing.
     */
    public static void aplicarLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Continúa de forma segura con el Look and Feel predeterminado si no se puede cambiar
        }
    }
}
