package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.model.EstadoUsuario;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.services.UsuarioServices;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de administración para la gestión de usuarios del sistema.
 */
public class GestionUsuariosPanel extends JPanel {

    private final UsuarioServices usuarioServices;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboFiltroEstado;

    public GestionUsuariosPanel(UsuarioServices usuarioServices) {
        this.usuarioServices = usuarioServices;
        setLayout(new BorderLayout(0, 16));
        setOpaque(false);

        crearCabecera();
        crearTabla();
        crearBarraAcciones();
        recargarUsuarios();
    }

    private void crearCabecera() {
        ModernCard cardCabecera = new ModernCard(new BorderLayout());

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);

        JLabel lblTitulo = new JLabel("👥 Gestión de Usuarios");
        lblTitulo.setFont(UITheme.FONT_TITLE_MEDIUM);
        lblTitulo.setForeground(UITheme.COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Administre las cuentas, roles y estados de acceso de los usuarios del sistema");
        lblSub.setFont(UITheme.FONT_SUBTITLE);
        lblSub.setForeground(UITheme.COLOR_TEXT_MUTED);

        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(4));
        panelTexto.add(lblSub);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelFiltro.setOpaque(false);
        panelFiltro.add(new JLabel("Filtrar por estado:"));
        comboFiltroEstado = new JComboBox<>(new String[]{"Todos", "ACTIVOS", "INACTIVOS"});
        UITheme.styleComboBox(comboFiltroEstado);
        comboFiltroEstado.addActionListener(e -> recargarUsuarios());
        panelFiltro.add(comboFiltroEstado);

        cardCabecera.add(panelTexto, BorderLayout.WEST);
        cardCabecera.add(panelFiltro, BorderLayout.EAST);

        add(cardCabecera, BorderLayout.NORTH);
    }

    private void crearTabla() {
        ModernCard cardTabla = new ModernCard(new BorderLayout());

        String[] columnas = {"Login", "Nombre Completo", "Rol", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        UITheme.styleTable(tablaUsuarios);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        UITheme.styleScrollPane(scroll);

        cardTabla.add(scroll, BorderLayout.CENTER);
        add(cardTabla, BorderLayout.CENTER);
    }

    private void crearBarraAcciones() {
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelAcciones.setOpaque(false);

        ModernButton btnRefrescar = new ModernButton("🔄 Refrescar", ModernButton.Variant.OUTLINE);
        ModernButton btnActivar = new ModernButton("✓ Activar Seleccionado", ModernButton.Variant.SUCCESS);
        ModernButton btnDesactivar = new ModernButton("✕ Desactivar Seleccionado", ModernButton.Variant.DANGER);

        btnRefrescar.addActionListener(e -> recargarUsuarios());
        btnActivar.addActionListener(e -> cambiarEstadoSeleccionado(EstadoUsuario.ACTIVO));
        btnDesactivar.addActionListener(e -> cambiarEstadoSeleccionado(EstadoUsuario.INACTIVO));

        panelAcciones.add(btnRefrescar);
        panelAcciones.add(btnActivar);
        panelAcciones.add(btnDesactivar);

        add(panelAcciones, BorderLayout.SOUTH);
    }

    public void recargarUsuarios() {
        modeloTabla.setRowCount(0);
        List<Usuario> usuarios = usuarioServices.listarUsuarios();
        String filtro = (String) comboFiltroEstado.getSelectedItem();

        for (Usuario u : usuarios) {
            if ("ACTIVOS".equals(filtro) && u.getEstado() != EstadoUsuario.ACTIVO) {
                continue;
            }
            if ("INACTIVOS".equals(filtro) && u.getEstado() != EstadoUsuario.INACTIVO) {
                continue;
            }
            modeloTabla.addRow(new Object[]{
                    u.getLogin(),
                    u.getNombreCompleto(),
                    u.getRol().name(),
                    u.getEstado().name()
            });
        }
    }

    private void cambiarEstadoSeleccionado(EstadoUsuario nuevoEstado) {
        int fila = tablaUsuarios.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this,
                    "Por favor seleccione un usuario de la tabla.",
                    "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String login = (String) modeloTabla.getValueAt(fila, 0);
        boolean ok = usuarioServices.actualizarEstado(login, nuevoEstado);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "El estado de " + login + " se actualizó a " + nuevoEstado + ".",
                    "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            recargarUsuarios();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el estado del usuario.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
