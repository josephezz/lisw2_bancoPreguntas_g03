package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionPlugin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de administración para el Microkernel y plugins cargados dinámicamente mediante reflexión.
 */
public class GestionPluginsPanel extends JPanel {

    private final PreguntasController preguntasController;
    private JTable tablaPlugins;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalPlugins;

    public GestionPluginsPanel(PreguntasController preguntasController) {
        this.preguntasController = preguntasController;
        setLayout(new BorderLayout(0, 16));
        setOpaque(false);

        crearCabecera();
        crearTabla();
        crearBarraAcciones();
        recargarPlugins();
    }

    private void crearCabecera() {
        ModernCard cardCabecera = new ModernCard(new BorderLayout());

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);

        JLabel lblTitulo = new JLabel("🧩 Administración de Plugins (Microkernel)");
        lblTitulo.setFont(UITheme.FONT_TITLE_MEDIUM);
        lblTitulo.setForeground(UITheme.COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Plugins cargados dinámicamente mediante reflexión desde plugins.properties");
        lblSub.setFont(UITheme.FONT_SUBTITLE);
        lblSub.setForeground(UITheme.COLOR_TEXT_MUTED);

        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(4));
        panelTexto.add(lblSub);

        lblTotalPlugins = new JLabel("0 plugins cargados");
        lblTotalPlugins.setFont(UITheme.FONT_REGULAR_BOLD);
        lblTotalPlugins.setForeground(UITheme.COLOR_PRIMARY);

        cardCabecera.add(panelTexto, BorderLayout.WEST);
        cardCabecera.add(lblTotalPlugins, BorderLayout.EAST);

        add(cardCabecera, BorderLayout.NORTH);
    }

    private void crearTabla() {
        ModernCard cardTabla = new ModernCard(new BorderLayout());

        String[] columnas = {"Nombre del Plugin", "Tipo de Pregunta Soportado", "Clase Implementadora", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPlugins = new JTable(modeloTabla);
        UITheme.styleTable(tablaPlugins);
        tablaPlugins.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tablaPlugins);
        UITheme.styleScrollPane(scroll);

        cardTabla.add(scroll, BorderLayout.CENTER);
        add(cardTabla, BorderLayout.CENTER);
    }

    private void crearBarraAcciones() {
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelAcciones.setOpaque(false);

        ModernButton btnRefrescar = new ModernButton("🔄 Refrescar", ModernButton.Variant.OUTLINE);
        ModernButton btnActivar = new ModernButton("✓ Activar Plugin", ModernButton.Variant.SUCCESS);
        ModernButton btnDesactivar = new ModernButton("✕ Desactivar Plugin", ModernButton.Variant.DANGER);

        btnRefrescar.addActionListener(e -> recargarPlugins());
        btnActivar.addActionListener(e -> cambiarEstadoPlugin(true));
        btnDesactivar.addActionListener(e -> cambiarEstadoPlugin(false));

        panelAcciones.add(btnRefrescar);
        panelAcciones.add(btnActivar);
        panelAcciones.add(btnDesactivar);

        add(panelAcciones, BorderLayout.SOUTH);
    }

    public void recargarPlugins() {
        modeloTabla.setRowCount(0);
        List<QuestionPlugin> todos = preguntasController.obtenerTodosLosPlugins();
        List<QuestionPlugin> activos = preguntasController.obtenerPluginsActivos();

        for (QuestionPlugin p : todos) {
            boolean estaActivo = activos.stream().anyMatch(a -> a.getName().equals(p.getName()));
            String tipo = "Desconocido";
            if (p.supports("SELECCION_MULTIPLE")) tipo = "SELECCION_MULTIPLE";
            else if (p.supports("CASO")) tipo = "CASO";
            else if (p.supports("MULTIMEDIA")) tipo = "MULTIMEDIA";

            modeloTabla.addRow(new Object[]{
                    p.getName(),
                    tipo,
                    p.getClass().getName(),
                    estaActivo ? "ACTIVO" : "INACTIVO"
            });
        }

        lblTotalPlugins.setText(todos.size() + " plugin(s) registrados (" + activos.size() + " activos)");
    }

    private void cambiarEstadoPlugin(boolean activar) {
        int fila = tablaPlugins.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un plugin de la tabla para modificar su estado.",
                    "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombrePlugin = (String) modeloTabla.getValueAt(fila, 0);
        boolean ok = activar
                ? preguntasController.activarPlugin(nombrePlugin)
                : preguntasController.desactivarPlugin(nombrePlugin);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Plugin '" + nombrePlugin + "' " + (activar ? "activado" : "desactivado") + " exitosamente.",
                    "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            recargarPlugins();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cambiar el estado del plugin.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
