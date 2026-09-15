package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Panel interactivo para el estudiante: banco de preguntas aprobadas y simulador paso a paso tipo examen Saber Pro.
 */
public class SimulacroEstudiantePanel extends JPanel {

    private final PreguntasController controller;

    private CardLayout modoLayout;
    private JPanel contenedorModos;

    // --- MODO 1: EXPLORADOR DEL BANCO APROBADO ---
    private JTable tablaAprobadas;
    private DefaultTableModel modeloAprobadas;
    private JTextArea areaDetalleEnunciado;
    private JTextArea areaDetalleOpciones;

    // --- MODO 2: SIMULADOR DE EXAMEN ACTIVO ---
    private List<Pregunta> preguntasSimulacro = new ArrayList<>();
    private final Map<Integer, String> respuestasSeleccionadas = new HashMap<>();
    private int indicePreguntaActual = 0;

    private JLabel lblProgresoTexto;
    private JProgressBar barraProgreso;
    private JLabel lblExamTitulo;
    private JLabel lblExamMeta;
    private JTextArea areaExamEnunciado;
    private ImagePreviewPanel previewExamMultimedia;
    private JRadioButton radOpA, radOpB, radOpC, radOpD;
    private ButtonGroup grupoOpciones;
    private ModernButton btnAnterior;
    private ModernButton btnSiguiente;
    private ModernButton btnFinalizar;

    // --- MODO 3: PANTALLA DE RESULTADOS ---
    private JLabel lblPuntajeFinal;
    private JLabel lblMensajeDesempeno;
    private DefaultTableModel modeloResultados;

    public SimulacroEstudiantePanel(PreguntasController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(0, 14));
        setOpaque(false);

        crearCabecera();

        modoLayout = new CardLayout();
        contenedorModos = new JPanel(modoLayout);
        contenedorModos.setOpaque(false);

        contenedorModos.add(crearModoExplorador(), "MODO_EXPLORADOR");
        contenedorModos.add(crearModoExamen(), "MODO_EXAMEN");
        contenedorModos.add(crearModoResultados(), "MODO_RESULTADOS");

        add(contenedorModos, BorderLayout.CENTER);
        recargarPreguntasAprobadas();
    }

    private void crearCabecera() {
        ModernCard cardCabecera = new ModernCard(new BorderLayout());

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);

        JLabel lblTitulo = new JLabel("🎓 Portal de Práctica y Simulacros Saber Pro");
        lblTitulo.setFont(UITheme.FONT_TITLE_MEDIUM);
        lblTitulo.setForeground(UITheme.COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Consulte el banco de preguntas aprobadas y realice simulacros cronometrados interactivos");
        lblSub.setFont(UITheme.FONT_SUBTITLE);
        lblSub.setForeground(UITheme.COLOR_TEXT_MUTED);

        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(4));
        panelTexto.add(lblSub);

        ModernButton btnIniciarSimulacro = new ModernButton("🚀 Iniciar Simulacro de Examen", ModernButton.Variant.SUCCESS);
        btnIniciarSimulacro.addActionListener(e -> iniciarExamen());

        cardCabecera.add(panelTexto, BorderLayout.WEST);
        cardCabecera.add(btnIniciarSimulacro, BorderLayout.EAST);

        add(cardCabecera, BorderLayout.NORTH);
    }

    // =========================================================================
    // MODO 1: EXPLORADOR DE PREGUNTAS APROBADAS
    // =========================================================================
    private JPanel crearModoExplorador() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setResizeWeight(0.55);
        split.setContinuousLayout(true);
        split.setBorder(null);
        split.setOpaque(false);

        // Tabla izquierda
        ModernCard cardTabla = new ModernCard(new BorderLayout(0, 8));
        JLabel lblTitTabla = new JLabel("Preguntas Aprobadas para Estudio");
        lblTitTabla.setFont(UITheme.FONT_TITLE_SMALL);
        lblTitTabla.setForeground(UITheme.COLOR_PRIMARY);

        String[] columnas = {"ID", "Título", "Tipo", "Competencia", "Dificultad"};
        modeloAprobadas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaAprobadas = new JTable(modeloAprobadas);
        UITheme.styleTable(tablaAprobadas);
        tablaAprobadas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAprobadas.getSelectionModel().addListSelectionListener(e -> mostrarDetalleExplorador());

        JScrollPane scrollTabla = new JScrollPane(tablaAprobadas);
        UITheme.styleScrollPane(scrollTabla);

        ModernButton btnRefrescar = new ModernButton("🔄 Refrescar", ModernButton.Variant.OUTLINE);
        btnRefrescar.addActionListener(e -> recargarPreguntasAprobadas());

        cardTabla.add(lblTitTabla, BorderLayout.NORTH);
        cardTabla.add(scrollTabla, BorderLayout.CENTER);
        cardTabla.add(btnRefrescar, BorderLayout.SOUTH);

        // Detalle derecho
        ModernCard cardDetalle = new ModernCard(new BorderLayout(0, 10));
        JLabel lblTitDetalle = new JLabel("Vista Previa del Ítem");
        lblTitDetalle.setFont(UITheme.FONT_TITLE_SMALL);
        lblTitDetalle.setForeground(UITheme.COLOR_PRIMARY);

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setOpaque(false);

        areaDetalleEnunciado = new JTextArea(4, 20);
        UITheme.styleTextArea(areaDetalleEnunciado);
        areaDetalleEnunciado.setEditable(false);
        JScrollPane scrollEnun = new JScrollPane(areaDetalleEnunciado);
        UITheme.styleScrollPane(scrollEnun);

        areaDetalleOpciones = new JTextArea(6, 20);
        UITheme.styleTextArea(areaDetalleOpciones);
        areaDetalleOpciones.setEditable(false);
        JScrollPane scrollOpc = new JScrollPane(areaDetalleOpciones);
        UITheme.styleScrollPane(scrollOpc);

        panelContenido.add(new JLabel("Enunciado:"));
        panelContenido.add(scrollEnun);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(new JLabel("Opciones de Respuesta:"));
        panelContenido.add(scrollOpc);

        cardDetalle.add(lblTitDetalle, BorderLayout.NORTH);
        cardDetalle.add(panelContenido, BorderLayout.CENTER);

        split.setLeftComponent(cardTabla);
        split.setRightComponent(cardDetalle);

        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================================
    // MODO 2: SIMULADOR DE EXAMEN PASO A PASO
    // =========================================================================
    private JPanel crearModoExamen() {
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setOpaque(false);

        // Barra superior de progreso
        ModernCard cardProgreso = new ModernCard(new BorderLayout(0, 8));
        lblProgresoTexto = new JLabel("Pregunta 1 de 1");
        lblProgresoTexto.setFont(UITheme.FONT_TITLE_SMALL);
        lblProgresoTexto.setForeground(UITheme.COLOR_PRIMARY_DARK);

        barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setValue(0);
        barraProgreso.setStringPainted(true);
        barraProgreso.setForeground(UITheme.COLOR_PRIMARY);
        barraProgreso.setPreferredSize(new Dimension(barraProgreso.getWidth(), 18));

        cardProgreso.add(lblProgresoTexto, BorderLayout.NORTH);
        cardProgreso.add(barraProgreso, BorderLayout.CENTER);

        // Tarjeta central de pregunta
        ModernCard cardPregunta = new ModernCard(new BorderLayout(0, 10));

        JPanel panelCabeceraPregunta = new JPanel(new BorderLayout());
        panelCabeceraPregunta.setOpaque(false);
        lblExamTitulo = new JLabel("Título");
        lblExamTitulo.setFont(UITheme.FONT_TITLE_SMALL);
        lblExamTitulo.setForeground(UITheme.COLOR_PRIMARY);
        lblExamMeta = new JLabel("Competencia");
        lblExamMeta.setFont(UITheme.FONT_REGULAR_BOLD);
        lblExamMeta.setForeground(UITheme.COLOR_SECONDARY);
        panelCabeceraPregunta.add(lblExamTitulo, BorderLayout.WEST);
        panelCabeceraPregunta.add(lblExamMeta, BorderLayout.EAST);

        // Enunciado y multimedia
        JPanel panelPreguntaCuerpo = new JPanel();
        panelPreguntaCuerpo.setLayout(new BoxLayout(panelPreguntaCuerpo, BoxLayout.Y_AXIS));
        panelPreguntaCuerpo.setOpaque(false);

        areaExamEnunciado = new JTextArea(4, 30);
        UITheme.styleTextArea(areaExamEnunciado);
        areaExamEnunciado.setEditable(false);
        JScrollPane scrollExamEnun = new JScrollPane(areaExamEnunciado);
        UITheme.styleScrollPane(scrollExamEnun);

        previewExamMultimedia = new ImagePreviewPanel();
        previewExamMultimedia.setPreferredSize(new Dimension(280, 140));

        panelPreguntaCuerpo.add(scrollExamEnun);
        panelPreguntaCuerpo.add(Box.createVerticalStrut(6));
        panelPreguntaCuerpo.add(previewExamMultimedia);
        panelPreguntaCuerpo.add(Box.createVerticalStrut(10));

        // Opciones con radio buttons
        JPanel panelOpciones = new JPanel(new GridLayout(4, 1, 0, 8));
        panelOpciones.setOpaque(false);
        panelOpciones.setBorder(BorderFactory.createTitledBorder("Seleccione su respuesta:"));

        radOpA = new JRadioButton("A) ");
        radOpB = new JRadioButton("B) ");
        radOpC = new JRadioButton("C) ");
        radOpD = new JRadioButton("D) ");

        radOpA.setFont(UITheme.FONT_REGULAR);
        radOpB.setFont(UITheme.FONT_REGULAR);
        radOpC.setFont(UITheme.FONT_REGULAR);
        radOpD.setFont(UITheme.FONT_REGULAR);

        grupoOpciones = new ButtonGroup();
        grupoOpciones.add(radOpA);
        grupoOpciones.add(radOpB);
        grupoOpciones.add(radOpC);
        grupoOpciones.add(radOpD);

        radOpA.addActionListener(e -> guardarRespuestaActual("A"));
        radOpB.addActionListener(e -> guardarRespuestaActual("B"));
        radOpC.addActionListener(e -> guardarRespuestaActual("C"));
        radOpD.addActionListener(e -> guardarRespuestaActual("D"));

        panelOpciones.add(radOpA);
        panelOpciones.add(radOpB);
        panelOpciones.add(radOpC);
        panelOpciones.add(radOpD);

        panelPreguntaCuerpo.add(panelOpciones);

        JScrollPane scrollCentro = new JScrollPane(panelPreguntaCuerpo);
        scrollCentro.setBorder(null);
        scrollCentro.setOpaque(false);
        scrollCentro.getViewport().setOpaque(false);

        cardPregunta.add(panelCabeceraPregunta, BorderLayout.NORTH);
        cardPregunta.add(scrollCentro, BorderLayout.CENTER);

        // Barra inferior de navegación
        JPanel panelNavegacion = new JPanel(new BorderLayout());
        panelNavegacion.setOpaque(false);

        ModernButton btnCancelar = new ModernButton("✕ Salir del Simulacro", ModernButton.Variant.OUTLINE);
        btnCancelar.addActionListener(e -> salirExamen());

        JPanel panelPasos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelPasos.setOpaque(false);

        btnAnterior = new ModernButton("◀ Anterior", ModernButton.Variant.SECONDARY);
        btnSiguiente = new ModernButton("Siguiente ▶", ModernButton.Variant.PRIMARY);
        btnFinalizar = new ModernButton("🏁 Finalizar y Calificar", ModernButton.Variant.SUCCESS);

        btnAnterior.addActionListener(e -> navegarPregunta(-1));
        btnSiguiente.addActionListener(e -> navegarPregunta(1));
        btnFinalizar.addActionListener(e -> finalizarExamen());

        panelPasos.add(btnAnterior);
        panelPasos.add(btnSiguiente);
        panelPasos.add(btnFinalizar);

        panelNavegacion.add(btnCancelar, BorderLayout.WEST);
        panelNavegacion.add(panelPasos, BorderLayout.EAST);

        root.add(cardProgreso, BorderLayout.NORTH);
        root.add(cardPregunta, BorderLayout.CENTER);
        root.add(panelNavegacion, BorderLayout.SOUTH);

        return root;
    }

    // =========================================================================
    // MODO 3: PANTALLA DE RESULTADOS DEL SIMULACRO
    // =========================================================================
    private JPanel crearModoResultados() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setOpaque(false);

        ModernCard cardScore = new ModernCard(new BorderLayout());
        JPanel panelScoreText = new JPanel();
        panelScoreText.setLayout(new BoxLayout(panelScoreText, BoxLayout.Y_AXIS));
        panelScoreText.setOpaque(false);

        JLabel lblScoreTitle = new JLabel("📊 Resultados del Simulacro");
        lblScoreTitle.setFont(UITheme.FONT_TITLE_MEDIUM);
        lblScoreTitle.setForeground(UITheme.COLOR_PRIMARY_DARK);

        lblPuntajeFinal = new JLabel("Puntaje: 0 / 0 (0%)");
        lblPuntajeFinal.setFont(UITheme.FONT_TITLE_LARGE);
        lblPuntajeFinal.setForeground(UITheme.COLOR_SUCCESS);

        lblMensajeDesempeno = new JLabel("Evaluación completada.");
        lblMensajeDesempeno.setFont(UITheme.FONT_REGULAR);
        lblMensajeDesempeno.setForeground(UITheme.COLOR_TEXT_MUTED);

        panelScoreText.add(lblScoreTitle);
        panelScoreText.add(Box.createVerticalStrut(6));
        panelScoreText.add(lblPuntajeFinal);
        panelScoreText.add(Box.createVerticalStrut(4));
        panelScoreText.add(lblMensajeDesempeno);

        ModernButton btnVolverInicio = new ModernButton("🔄 Realizar Nuevo Simulacro", ModernButton.Variant.PRIMARY);
        btnVolverInicio.addActionListener(e -> modoLayout.show(contenedorModos, "MODO_EXPLORADOR"));

        cardScore.add(panelScoreText, BorderLayout.WEST);
        cardScore.add(btnVolverInicio, BorderLayout.EAST);

        // Tabla de detalle pregunta por pregunta
        ModernCard cardDetalle = new ModernCard(new BorderLayout(0, 8));
        JLabel lblTitTabla = new JLabel("Desglose Pregunta por Pregunta");
        lblTitTabla.setFont(UITheme.FONT_TITLE_SMALL);
        lblTitTabla.setForeground(UITheme.COLOR_PRIMARY);

        String[] cols = {"#", "Pregunta", "Tu Respuesta", "Respuesta Correcta", "Resultado"};
        modeloResultados = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaResultados = new JTable(modeloResultados);
        UITheme.styleTable(tablaResultados);

        JScrollPane scrollResultados = new JScrollPane(tablaResultados);
        UITheme.styleScrollPane(scrollResultados);

        cardDetalle.add(lblTitTabla, BorderLayout.NORTH);
        cardDetalle.add(scrollResultados, BorderLayout.CENTER);

        root.add(cardScore, BorderLayout.NORTH);
        root.add(cardDetalle, BorderLayout.CENTER);

        return root;
    }

    // =========================================================================
    // LÓGICA DEL SIMULADOR
    // =========================================================================
    public void recargarPreguntasAprobadas() {
        modeloAprobadas.setRowCount(0);
        List<Pregunta> aprobadas = controller.listarPorEstado(EstadoPregunta.APROBADA);

        for (Pregunta p : aprobadas) {
            modeloAprobadas.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getTipo(),
                    p.getCompetencia() != null ? p.getCompetencia() : "General",
                    p.getNivelDificultad() != null ? p.getNivelDificultad() : "Media"
            });
        }
    }

    private void mostrarDetalleExplorador() {
        int fila = tablaAprobadas.getSelectedRow();
        if (fila < 0) return;

        String id = (String) modeloAprobadas.getValueAt(fila, 0);
        controller.buscarPregunta(id).ifPresent(p -> {
            areaDetalleEnunciado.setText(p.getEnunciado());
            areaDetalleOpciones.setText(p.getOpciones() != null ? p.getOpciones().comoTexto() : "(Sin opciones)");
        });
    }

    private void iniciarExamen() {
        preguntasSimulacro = controller.listarPorEstado(EstadoPregunta.APROBADA);

        if (preguntasSimulacro.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay preguntas aprobadas disponibles para iniciar el simulacro en este momento.",
                    "Banco Vacío", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        respuestasSeleccionadas.clear();
        indicePreguntaActual = 0;
        mostrarPreguntaExamen(0);
        modoLayout.show(contenedorModos, "MODO_EXAMEN");
    }

    private void mostrarPreguntaExamen(int indice) {
        if (indice < 0 || indice >= preguntasSimulacro.size()) return;

        indicePreguntaActual = indice;
        Pregunta p = preguntasSimulacro.get(indice);

        int total = preguntasSimulacro.size();
        lblProgresoTexto.setText("Pregunta " + (indice + 1) + " de " + total);
        int porcentaje = (int) Math.round(((double) (indice + 1) / total) * 100);
        barraProgreso.setValue(porcentaje);
        barraProgreso.setString(porcentaje + "% Completado");

        lblExamTitulo.setText(p.getNombre());
        lblExamMeta.setText("Competencia: " + (p.getCompetencia() != null ? p.getCompetencia() : "General"));
        areaExamEnunciado.setText(p.getEnunciado());

        // Multimedia
        if (p.getRecursoMultimedia() != null && !p.getRecursoMultimedia().isBlank()) {
            previewExamMultimedia.setVisible(true);
            previewExamMultimedia.cargarRecurso(p.getRecursoMultimedia());
        } else {
            previewExamMultimedia.setVisible(false);
        }

        // Opciones
        List<String> ops = p.getOpciones() != null ? p.getOpciones().comoLista() : List.of("", "", "", "");
        radOpA.setText("A) " + (ops.size() > 0 ? ops.get(0) : ""));
        radOpB.setText("B) " + (ops.size() > 1 ? ops.get(1) : ""));
        radOpC.setText("C) " + (ops.size() > 2 ? ops.get(2) : ""));
        radOpD.setText("D) " + (ops.size() > 3 ? ops.get(3) : ""));

        // Restaurar respuesta previa
        grupoOpciones.clearSelection();
        String previa = respuestasSeleccionadas.get(indice);
        if ("A".equals(previa)) radOpA.setSelected(true);
        else if ("B".equals(previa)) radOpB.setSelected(true);
        else if ("C".equals(previa)) radOpC.setSelected(true);
        else if ("D".equals(previa)) radOpD.setSelected(true);

        btnAnterior.setEnabled(indice > 0);
        btnSiguiente.setEnabled(indice < total - 1);
    }

    private void guardarRespuestaActual(String opcion) {
        respuestasSeleccionadas.put(indicePreguntaActual, opcion);
    }

    private void navegarPregunta(int delta) {
        int nuevoIndice = indicePreguntaActual + delta;
        if (nuevoIndice >= 0 && nuevoIndice < preguntasSimulacro.size()) {
            mostrarPreguntaExamen(nuevoIndice);
        }
    }

    private void finalizarExamen() {
        int noRespondidas = preguntasSimulacro.size() - respuestasSeleccionadas.size();
        if (noRespondidas > 0) {
            int conf = JOptionPane.showConfirmDialog(this,
                    "Tiene " + noRespondidas + " pregunta(s) sin responder.\n¿Desea finalizar el simulacro de todos modos?",
                    "Preguntas pendientes", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (conf != JOptionPane.YES_OPTION) return;
        }

        calcularYMostrarResultados();
    }

    private void calcularYMostrarResultados() {
        int correctas = 0;
        int total = preguntasSimulacro.size();
        modeloResultados.setRowCount(0);

        for (int i = 0; i < total; i++) {
            Pregunta p = preguntasSimulacro.get(i);
            String respuestaEstudiante = respuestasSeleccionadas.getOrDefault(i, "(Sin respuesta)");
            String respuestaCorrecta = p.getRespuestaCorrecta() != null ? p.getRespuestaCorrecta().trim() : "";

            boolean esCorrecta = respuestaEstudiante.equalsIgnoreCase(respuestaCorrecta);
            if (esCorrecta) {
                correctas++;
            }

            modeloResultados.addRow(new Object[]{
                    (i + 1),
                    p.getNombre(),
                    respuestaEstudiante,
                    respuestaCorrecta,
                    esCorrecta ? "✓ Correcta" : "✕ Incorrecta"
            });
        }

        double pct = (double) correctas * 100.0 / total;
        lblPuntajeFinal.setText(String.format("Puntaje Obtenido: %d / %d  (%.1f%%)", correctas, total, pct));

        if (pct >= 80.0) {
            lblPuntajeFinal.setForeground(new Color(4, 120, 87));
            lblMensajeDesempeno.setText("🏆 ¡Excelente nivel de preparación en competencias Saber Pro!");
        } else if (pct >= 60.0) {
            lblPuntajeFinal.setForeground(UITheme.COLOR_PRIMARY);
            lblMensajeDesempeno.setText("👍 Buen desempeño. Se recomienda repasar los ítems con fallo.");
        } else {
            lblPuntajeFinal.setForeground(UITheme.COLOR_DANGER);
            lblMensajeDesempeno.setText("⚠️ Nivel básico. Es conveniente reforzar el análisis de las preguntas.");
        }

        modoLayout.show(contenedorModos, "MODO_RESULTADOS");
    }

    private void salirExamen() {
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Desea salir del simulacro? El progreso actual no se guardará.",
                "Confirmar Salida", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (conf == JOptionPane.YES_OPTION) {
            modoLayout.show(contenedorModos, "MODO_EXPLORADOR");
        }
    }
}
