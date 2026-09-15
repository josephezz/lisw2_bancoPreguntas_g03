package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.ResultadoCambioEstado;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel para que el autor consulte sus preguntas creadas, su estado actual y las observaciones de los revisores.
 */
public class MisPreguntasPanel extends JPanel {

    private final PreguntasController controller;
    private final Usuario autorActual;

    private JTable tablaMisPreguntas;
    private DefaultTableModel modeloTabla;
    private JTextArea areaDetalleEnunciado;
    private JTextArea areaDetalleOpciones;
    private JTextArea areaObservaciones;
    private ModernButton btnReenviar;

    public MisPreguntasPanel(PreguntasController controller, Usuario autorActual) {
        this.controller = controller;
        this.autorActual = autorActual;

        setLayout(new BorderLayout(0, 14));
        setOpaque(false);

        crearCabecera();
        crearPanelCentral();
        recargarMisPreguntas();
    }

    private void crearCabecera() {
        ModernCard cardCabecera = new ModernCard(new BorderLayout());

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);

        JLabel lblTitulo = new JLabel("📚 Mis Preguntas Formuladas");
        lblTitulo.setFont(UITheme.FONT_TITLE_MEDIUM);
        lblTitulo.setForeground(UITheme.COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Consulte el estado editorial y las observaciones emitidas por el comité revisor");
        lblSub.setFont(UITheme.FONT_SUBTITLE);
        lblSub.setForeground(UITheme.COLOR_TEXT_MUTED);

        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(4));
        panelTexto.add(lblSub);

        ModernButton btnRefrescar = new ModernButton("🔄 Refrescar", ModernButton.Variant.OUTLINE);
        btnRefrescar.addActionListener(e -> recargarMisPreguntas());

        cardCabecera.add(panelTexto, BorderLayout.WEST);
        cardCabecera.add(btnRefrescar, BorderLayout.EAST);

        add(cardCabecera, BorderLayout.NORTH);
    }

    private void crearPanelCentral() {
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setResizeWeight(0.5);
        split.setContinuousLayout(true);
        split.setBorder(null);
        split.setOpaque(false);

        // Superior: Tabla
        ModernCard cardTabla = new ModernCard(new BorderLayout());
        String[] columnas = {"ID", "Título", "Tipo", "Estado", "Observaciones del Revisor"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaMisPreguntas = new JTable(modeloTabla);
        UITheme.styleTable(tablaMisPreguntas);
        tablaMisPreguntas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMisPreguntas.getSelectionModel().addListSelectionListener(e -> mostrarDetalleSeleccion());

        JScrollPane scrollTabla = new JScrollPane(tablaMisPreguntas);
        UITheme.styleScrollPane(scrollTabla);
        cardTabla.add(scrollTabla, BorderLayout.CENTER);

        // Inferior: Detalle y Observaciones
        ModernCard cardDetalle = new ModernCard(new BorderLayout(12, 10));

        JPanel panelDetalleContenido = new JPanel(new GridLayout(1, 3, 10, 0));
        panelDetalleContenido.setOpaque(false);

        // Columna 1: Enunciado
        JPanel colEnun = new JPanel(new BorderLayout(0, 4));
        colEnun.setOpaque(false);
        colEnun.add(new JLabel("Enunciado de la Pregunta:"), BorderLayout.NORTH);
        areaDetalleEnunciado = new JTextArea(4, 20);
        UITheme.styleTextArea(areaDetalleEnunciado);
        areaDetalleEnunciado.setEditable(false);
        JScrollPane scrollEnun = new JScrollPane(areaDetalleEnunciado);
        UITheme.styleScrollPane(scrollEnun);
        colEnun.add(scrollEnun, BorderLayout.CENTER);

        // Columna 2: Opciones
        JPanel colOps = new JPanel(new BorderLayout(0, 4));
        colOps.setOpaque(false);
        colOps.add(new JLabel("Opciones registradas:"), BorderLayout.NORTH);
        areaDetalleOpciones = new JTextArea(4, 20);
        UITheme.styleTextArea(areaDetalleOpciones);
        areaDetalleOpciones.setEditable(false);
        JScrollPane scrollOps = new JScrollPane(areaDetalleOpciones);
        UITheme.styleScrollPane(scrollOps);
        colOps.add(scrollOps, BorderLayout.CENTER);

        // Columna 3: Observaciones del revisor
        JPanel colObs = new JPanel(new BorderLayout(0, 4));
        colObs.setOpaque(false);
        colObs.add(new JLabel("Observaciones de Revisión:"), BorderLayout.NORTH);
        areaObservaciones = new JTextArea(4, 20);
        UITheme.styleTextArea(areaObservaciones);
        areaObservaciones.setEditable(false);
        areaObservaciones.setBackground(new Color(254, 242, 242));
        areaObservaciones.setForeground(new Color(185, 28, 28));
        JScrollPane scrollObs = new JScrollPane(areaObservaciones);
        UITheme.styleScrollPane(scrollObs);
        colObs.add(scrollObs, BorderLayout.CENTER);

        panelDetalleContenido.add(colEnun);
        panelDetalleContenido.add(colOps);
        panelDetalleContenido.add(colObs);

        JPanel panelBotonera = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBotonera.setOpaque(false);
        btnReenviar = new ModernButton("📤 Reenviar Pregunta a Revisión", ModernButton.Variant.PRIMARY);
        btnReenviar.setEnabled(false);
        btnReenviar.addActionListener(e -> reenviarSeleccionada());
        panelBotonera.add(btnReenviar);

        cardDetalle.add(panelDetalleContenido, BorderLayout.CENTER);
        cardDetalle.add(panelBotonera, BorderLayout.SOUTH);

        split.setTopComponent(cardTabla);
        split.setBottomComponent(cardDetalle);

        add(split, BorderLayout.CENTER);
    }

    public void recargarMisPreguntas() {
        modeloTabla.setRowCount(0);
        String login = autorActual != null ? autorActual.getLogin() : "";
        List<Pregunta> misPreguntas = controller.listarPorAutor(login);

        for (Pregunta p : misPreguntas) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getTipo(),
                    p.getEstado().name(),
                    p.getObservaciones() != null ? p.getObservaciones() : "Sin observaciones"
            });
        }

        limpiarDetalle();
    }

    private void mostrarDetalleSeleccion() {
        int fila = tablaMisPreguntas.getSelectedRow();
        if (fila < 0) {
            limpiarDetalle();
            return;
        }

        String id = (String) modeloTabla.getValueAt(fila, 0);
        controller.buscarPregunta(id).ifPresent(p -> {
            areaDetalleEnunciado.setText(p.getEnunciado());
            areaDetalleOpciones.setText(p.getOpciones() != null ? p.getOpciones().comoTexto() : "(Sin opciones)");
            String obs = p.getObservaciones();
            areaObservaciones.setText(obs != null && !obs.isBlank() ? obs : "No se han emitido observaciones aún.");

            boolean puedeReenviar = (p.getEstado() == EstadoPregunta.BORRADOR || p.getEstado() == EstadoPregunta.RECHAZADA);
            btnReenviar.setEnabled(puedeReenviar);
        });
    }

    private void reenviarSeleccionada() {
        int fila = tablaMisPreguntas.getSelectedRow();
        if (fila < 0) return;

        String id = (String) modeloTabla.getValueAt(fila, 0);
        ResultadoCambioEstado res = controller.enviarARevision(id);

        if (res == ResultadoCambioEstado.ACTUALIZADO) {
            JOptionPane.showMessageDialog(this,
                    "La pregunta " + id + " ha sido enviada al panel de revisión.",
                    "Enviada a Revisión", JOptionPane.INFORMATION_MESSAGE);
            recargarMisPreguntas();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo reenviar la pregunta: " + res,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarDetalle() {
        areaDetalleEnunciado.setText("");
        areaDetalleOpciones.setText("");
        areaObservaciones.setText("");
        btnReenviar.setEnabled(false);
    }
}
