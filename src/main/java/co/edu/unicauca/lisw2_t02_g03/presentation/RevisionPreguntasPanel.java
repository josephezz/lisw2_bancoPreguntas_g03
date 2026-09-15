package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.ResultadoCambioEstado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel profesional para el Revisor: evaluación, observaciones, aprobación y rechazo de preguntas.
 */
public class RevisionPreguntasPanel extends JPanel {

    private final PreguntasController controller;

    private JTable tablaPendientes;
    private DefaultTableModel modeloTabla;
    private JLabel lblContador;

    // Componentes del inspector de detalle
    private JLabel lblDetalleId;
    private JLabel lblDetalleTitulo;
    private JLabel lblDetalleTipo;
    private JLabel lblDetalleAutor;
    private JLabel lblDetalleMeta;
    private JTextArea areaDetalleEnunciado;
    private JTextArea areaDetalleOpciones;
    private JLabel lblDetalleRespuesta;
    private ImagePreviewPanel previewRecurso;
    private JTextArea areaObservaciones;
    private ModernButton btnAprobar;
    private ModernButton btnRechazar;

    private String idPreguntaSeleccionada = null;

    public RevisionPreguntasPanel(PreguntasController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(0, 14));
        setOpaque(false);

        crearCabecera();
        crearPanelCentral();
        recargarPreguntasPendientes();
    }

    private void crearCabecera() {
        ModernCard cardCabecera = new ModernCard(new BorderLayout());

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);

        JLabel lblTitulo = new JLabel("🔍 Comité Revisor - Evaluación y Aprobación de Preguntas");
        lblTitulo.setFont(UITheme.FONT_TITLE_MEDIUM);
        lblTitulo.setForeground(UITheme.COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Revise la coherencia psicométrica y de contenido antes de incorporar los ítems al banco oficial");
        lblSub.setFont(UITheme.FONT_SUBTITLE);
        lblSub.setForeground(UITheme.COLOR_TEXT_MUTED);

        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(4));
        panelTexto.add(lblSub);

        lblContador = new JLabel("0 pendientes");
        lblContador.setFont(UITheme.FONT_REGULAR_BOLD);
        lblContador.setForeground(UITheme.COLOR_INFO);

        cardCabecera.add(panelTexto, BorderLayout.WEST);
        cardCabecera.add(lblContador, BorderLayout.EAST);

        add(cardCabecera, BorderLayout.NORTH);
    }

    private void crearPanelCentral() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.38);
        split.setContinuousLayout(true);
        split.setBorder(null);
        split.setOpaque(false);

        // Panel Izquierdo: Tabla de pendientes
        ModernCard cardLista = new ModernCard(new BorderLayout(0, 10));
        JLabel lblTituloLista = new JLabel("Ítems Pendientes de Revisión");
        lblTituloLista.setFont(UITheme.FONT_TITLE_SMALL);
        lblTituloLista.setForeground(UITheme.COLOR_PRIMARY);

        String[] columnas = {"ID", "Título", "Tipo", "Autor"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPendientes = new JTable(modeloTabla);
        UITheme.styleTable(tablaPendientes);
        tablaPendientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaPendientes.getSelectionModel().addListSelectionListener(e -> cargarPreguntaSeleccionada());

        JScrollPane scrollTabla = new JScrollPane(tablaPendientes);
        UITheme.styleScrollPane(scrollTabla);

        ModernButton btnRefrescar = new ModernButton("🔄 Refrescar Lista", ModernButton.Variant.OUTLINE);
        btnRefrescar.addActionListener(e -> recargarPreguntasPendientes());

        cardLista.add(lblTituloLista, BorderLayout.NORTH);
        cardLista.add(scrollTabla, BorderLayout.CENTER);
        cardLista.add(btnRefrescar, BorderLayout.SOUTH);

        // Panel Derecho: Inspector de la pregunta seleccionada
        ModernCard cardInspector = new ModernCard(new BorderLayout(0, 12));

        JPanel panelInfoBasica = new JPanel();
        panelInfoBasica.setLayout(new BoxLayout(panelInfoBasica, BoxLayout.Y_AXIS));
        panelInfoBasica.setOpaque(false);

        lblDetalleId = new JLabel("Seleccione una pregunta para revisar");
        lblDetalleId.setFont(UITheme.FONT_TITLE_SMALL);
        lblDetalleId.setForeground(UITheme.COLOR_PRIMARY_DARK);

        lblDetalleTitulo = new JLabel(" ");
        lblDetalleTitulo.setFont(UITheme.FONT_REGULAR_BOLD);

        JPanel filaDatos = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 2));
        filaDatos.setOpaque(false);
        lblDetalleTipo = new JLabel("Tipo: -");
        lblDetalleAutor = new JLabel("Autor: -");
        lblDetalleMeta = new JLabel("Competencia: -");
        filaDatos.add(lblDetalleTipo);
        filaDatos.add(lblDetalleAutor);
        filaDatos.add(lblDetalleMeta);

        panelInfoBasica.add(lblDetalleId);
        panelInfoBasica.add(lblDetalleTitulo);
        panelInfoBasica.add(filaDatos);

        // Área central del inspector (con scroll)
        JPanel panelCuerpoInspector = new JPanel();
        panelCuerpoInspector.setLayout(new BoxLayout(panelCuerpoInspector, BoxLayout.Y_AXIS));
        panelCuerpoInspector.setOpaque(false);

        JLabel lblEnun = new JLabel("Enunciado Completo:");
        lblEnun.setFont(UITheme.FONT_REGULAR_BOLD);
        areaDetalleEnunciado = new JTextArea(4, 25);
        UITheme.styleTextArea(areaDetalleEnunciado);
        areaDetalleEnunciado.setEditable(false);
        JScrollPane scrollEnun = new JScrollPane(areaDetalleEnunciado);
        UITheme.styleScrollPane(scrollEnun);

        JLabel lblOpc = new JLabel("Opciones Formuladas:");
        lblOpc.setFont(UITheme.FONT_REGULAR_BOLD);
        areaDetalleOpciones = new JTextArea(4, 25);
        UITheme.styleTextArea(areaDetalleOpciones);
        areaDetalleOpciones.setEditable(false);
        JScrollPane scrollOpc = new JScrollPane(areaDetalleOpciones);
        UITheme.styleScrollPane(scrollOpc);

        lblDetalleRespuesta = new JLabel("Respuesta Correcta: -");
        lblDetalleRespuesta.setFont(UITheme.FONT_REGULAR_BOLD);
        lblDetalleRespuesta.setForeground(new Color(4, 120, 87));

        previewRecurso = new ImagePreviewPanel();
        previewRecurso.setPreferredSize(new Dimension(220, 110));

        JLabel lblObs = new JLabel("Observaciones de Revisión (Obligatorio para rechazo):");
        lblObs.setFont(UITheme.FONT_REGULAR_BOLD);
        areaObservaciones = new JTextArea(3, 25);
        UITheme.styleTextArea(areaObservaciones);
        JScrollPane scrollObs = new JScrollPane(areaObservaciones);
        UITheme.styleScrollPane(scrollObs);

        panelCuerpoInspector.add(lblEnun);
        panelCuerpoInspector.add(scrollEnun);
        panelCuerpoInspector.add(Box.createVerticalStrut(8));
        panelCuerpoInspector.add(lblOpc);
        panelCuerpoInspector.add(scrollOpc);
        panelCuerpoInspector.add(Box.createVerticalStrut(6));
        panelCuerpoInspector.add(lblDetalleRespuesta);
        panelCuerpoInspector.add(Box.createVerticalStrut(6));
        panelCuerpoInspector.add(previewRecurso);
        panelCuerpoInspector.add(Box.createVerticalStrut(8));
        panelCuerpoInspector.add(lblObs);
        panelCuerpoInspector.add(scrollObs);

        JScrollPane scrollInspector = new JScrollPane(panelCuerpoInspector);
        scrollInspector.setBorder(null);
        scrollInspector.setOpaque(false);
        scrollInspector.getViewport().setOpaque(false);
        scrollInspector.getVerticalScrollBar().setUnitIncrement(16);

        // Acciones de aprobación y rechazo
        JPanel panelBotonesDecision = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotonesDecision.setOpaque(false);

        btnAprobar = new ModernButton("✓ Aprobar Pregunta", ModernButton.Variant.SUCCESS);
        btnRechazar = new ModernButton("✕ Rechazar Pregunta", ModernButton.Variant.DANGER);

        btnAprobar.setEnabled(false);
        btnRechazar.setEnabled(false);

        btnAprobar.addActionListener(e -> aprobarSeleccionada());
        btnRechazar.addActionListener(e -> rechazarSeleccionada());

        panelBotonesDecision.add(btnRechazar);
        panelBotonesDecision.add(btnAprobar);

        cardInspector.add(panelInfoBasica, BorderLayout.NORTH);
        cardInspector.add(scrollInspector, BorderLayout.CENTER);
        cardInspector.add(panelBotonesDecision, BorderLayout.SOUTH);

        split.setLeftComponent(cardLista);
        split.setRightComponent(cardInspector);

        add(split, BorderLayout.CENTER);
    }

    public void recargarPreguntasPendientes() {
        modeloTabla.setRowCount(0);
        List<Pregunta> pendientes = controller.listarPorEstado(EstadoPregunta.PENDIENTE_REVISION);

        for (Pregunta p : pendientes) {
            modeloTabla.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getTipo(),
                    p.getAutorLogin() != null ? p.getAutorLogin() : "Anónimo"
            });
        }

        lblContador.setText(pendientes.size() + " pregunta(s) por revisar");
        limpiarInspector();
    }

    private void cargarPreguntaSeleccionada() {
        int fila = tablaPendientes.getSelectedRow();
        if (fila < 0) {
            limpiarInspector();
            return;
        }

        idPreguntaSeleccionada = (String) modeloTabla.getValueAt(fila, 0);
        controller.buscarPregunta(idPreguntaSeleccionada).ifPresent(p -> {
            lblDetalleId.setText("Ítem: " + p.getId());
            lblDetalleTitulo.setText(p.getNombre());
            lblDetalleTipo.setText("Tipo: " + p.getTipo());
            lblDetalleAutor.setText("Autor: " + (p.getAutorLogin() != null ? p.getAutorLogin() : "N/A"));
            lblDetalleMeta.setText("Dificultad: " + (p.getNivelDificultad() != null ? p.getNivelDificultad() : "Media"));

            areaDetalleEnunciado.setText(p.getEnunciado());
            areaDetalleOpciones.setText(p.getOpciones() != null ? p.getOpciones().comoTexto() : "Sin opciones registradas");
            lblDetalleRespuesta.setText("✓ Respuesta Correcta Registrada: Opción " + p.getRespuestaCorrecta());

            if (p.getRecursoMultimedia() != null && !p.getRecursoMultimedia().isBlank()) {
                previewRecurso.setVisible(true);
                previewRecurso.cargarRecurso(p.getRecursoMultimedia());
            } else {
                previewRecurso.setVisible(false);
            }

            areaObservaciones.setText(p.getObservaciones() != null ? p.getObservaciones() : "");

            btnAprobar.setEnabled(true);
            btnRechazar.setEnabled(true);
        });
    }

    private void aprobarSeleccionada() {
        if (idPreguntaSeleccionada == null) return;

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Confirma la APROBACIÓN de la pregunta " + idPreguntaSeleccionada + "?\n"
                        + "Pasará a estar disponible para simulacros de estudiantes.",
                "Confirmar Aprobación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (conf != JOptionPane.YES_OPTION) return;

        ResultadoCambioEstado res = controller.aprobarPregunta(idPreguntaSeleccionada);
        if (res == ResultadoCambioEstado.ACTUALIZADO) {
            JOptionPane.showMessageDialog(this,
                    "Pregunta " + idPreguntaSeleccionada + " APROBADA exitosamente.",
                    "Aprobada", JOptionPane.INFORMATION_MESSAGE);
            recargarPreguntasPendientes();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo aprobar la pregunta: " + res,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void rechazarSeleccionada() {
        if (idPreguntaSeleccionada == null) return;

        String obs = areaObservaciones.getText().trim();
        if (obs.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar las observaciones explicando el motivo del rechazo para que el autor pueda corregirla.",
                    "Observaciones Requeridas", JOptionPane.WARNING_MESSAGE);
            areaObservaciones.requestFocusInWindow();
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this,
                "¿Confirma el RECHAZO de la pregunta " + idPreguntaSeleccionada + "?\n"
                        + "Las observaciones serán enviadas al autor.",
                "Confirmar Rechazo", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (conf != JOptionPane.YES_OPTION) return;

        ResultadoCambioEstado res = controller.rechazarPregunta(idPreguntaSeleccionada, obs);
        if (res == ResultadoCambioEstado.ACTUALIZADO) {
            JOptionPane.showMessageDialog(this,
                    "Pregunta " + idPreguntaSeleccionada + " RECHAZADA. Se enviaron las observaciones al autor.",
                    "Rechazada", JOptionPane.INFORMATION_MESSAGE);
            recargarPreguntasPendientes();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo rechazar la pregunta: " + res,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarInspector() {
        idPreguntaSeleccionada = null;
        lblDetalleId.setText("Seleccione una pregunta para revisar");
        lblDetalleTitulo.setText(" ");
        lblDetalleTipo.setText("Tipo: -");
        lblDetalleAutor.setText("Autor: -");
        lblDetalleMeta.setText("Competencia: -");
        areaDetalleEnunciado.setText("");
        areaDetalleOpciones.setText("");
        lblDetalleRespuesta.setText("Respuesta Correcta: -");
        previewRecurso.limpiar();
        previewRecurso.setVisible(false);
        areaObservaciones.setText("");
        btnAprobar.setEnabled(false);
        btnRechazar.setEnabled(false);
    }
}
