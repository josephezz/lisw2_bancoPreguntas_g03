package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.ResultadoCambioEstado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Vista principal del micro patrón MVC para la gestión de estados de preguntas, modernizada con UITheme.
 */
public class PreguntasFrame extends JFrame {

    private final PreguntasController controller;
    private final EstadisticasView estadisticasView = new EstadisticasView();
    private final GraficaPastelView graficaPastelView = new GraficaPastelView();

    private final JComboBox<Pregunta> comboPreguntas = new JComboBox<>();
    private final JComboBox<EstadoPregunta> comboNuevoEstado = new JComboBox<>(EstadoPregunta.values());
    private final JTextField campoId = crearCampoSoloLectura();
    private final JTextField campoNombre = crearCampoSoloLectura();
    private final JTextField campoTipo = crearCampoSoloLectura();
    private final JTextField campoCompetencia = crearCampoSoloLectura();
    private final JTextField campoCategoria = crearCampoSoloLectura();
    private final JTextField campoDificultad = crearCampoSoloLectura();
    private final JTextField campoMultimedia = crearCampoSoloLectura();
    private final JTextField campoAutor = crearCampoSoloLectura();
    private final JTextField campoObservaciones = crearCampoSoloLectura();
    private final JTextField campoRespuesta = crearCampoSoloLectura();
    private final JTextField campoEstadoActual = crearCampoSoloLectura();
    private final JTextArea areaPregunta = crearAreaSoloLectura(4);
    private final JTextArea areaOpciones = crearAreaSoloLectura(6);

    private final ModernButton botonCargar = new ModernButton("Cargar Pregunta", ModernButton.Variant.PRIMARY);
    private final ModernButton botonActualizar = new ModernButton("Actualizar Estado", ModernButton.Variant.SUCCESS);
    private final ModernButton botonRefrescar = new ModernButton("Refrescar", ModernButton.Variant.OUTLINE);

    private boolean observadoresRegistrados;

    public PreguntasFrame(PreguntasController controller) {
        if (controller == null) {
            throw new IllegalArgumentException("El controlador de preguntas es obligatorio.");
        }
        this.controller = controller;

        configurarVentana();
        crearInterfaz();
        registrarObservadores();
        recargarBanco(null);
        controller.refrescarVistasObservadoras();
    }

    private void configurarVentana() {
        setTitle("Banco de Preguntas Saber Pro - Gestión de Estados (MVC)");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(980, 700));
        setSize(1180, 800);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(UITheme.COLOR_BG);
    }

    private void crearInterfaz() {
        JPanel raiz = new JPanel(new BorderLayout(12, 12));
        raiz.setBorder(new EmptyBorder(14, 16, 14, 16));
        raiz.setOpaque(false);

        raiz.add(crearPanelSeleccion(), BorderLayout.NORTH);

        JSplitPane division = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                crearPanelFormulario(),
                crearPanelObservadores());
        division.setResizeWeight(0.58);
        division.setOneTouchExpandable(true);
        division.setContinuousLayout(true);
        division.setBorder(null);
        division.setOpaque(false);

        raiz.add(division, BorderLayout.CENTER);
        setContentPane(raiz);

        comboPreguntas.addActionListener(e -> cargarSeleccion());
        botonCargar.addActionListener(e -> cargarSeleccion());
        botonActualizar.addActionListener(e -> actualizarEstado());
        botonRefrescar.addActionListener(e -> {
            recargarBanco(null);
            controller.refrescarVistasObservadoras();
        });
    }

    private JPanel crearPanelSeleccion() {
        ModernCard panel = new ModernCard(new FlowLayout(FlowLayout.LEFT, 12, 6));

        comboPreguntas.setPreferredSize(new Dimension(460, 36));
        UITheme.styleComboBox(comboPreguntas);

        JLabel lblSel = new JLabel("Seleccionar Pregunta:");
        lblSel.setFont(UITheme.FONT_REGULAR_BOLD);

        panel.add(lblSel);
        panel.add(comboPreguntas);
        panel.add(botonCargar);
        panel.add(botonRefrescar);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        ModernCard contenedor = new ModernCard(new BorderLayout(8, 10));

        JLabel lblTitForm = new JLabel("Formulario de Inspección y Actualización de Estado");
        lblTitForm.setFont(UITheme.FONT_TITLE_SMALL);
        lblTitForm.setForeground(UITheme.COLOR_PRIMARY);
        contenedor.add(lblTitForm, BorderLayout.NORTH);

        JPanel campos = new JPanel(new GridBagLayout());
        campos.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 6, 3, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int fila = 0;
        agregarFila(campos, gbc, fila++, "Id:", campoId);
        agregarFila(campos, gbc, fila++, "Nombre:", campoNombre);
        agregarFila(campos, gbc, fila++, "Tipo:", campoTipo);
        agregarArea(campos, gbc, fila++, "Pregunta:", areaPregunta);
        agregarArea(campos, gbc, fila++, "Opciones:", areaOpciones);
        agregarFila(campos, gbc, fila++, "Respuesta correcta:", campoRespuesta);
        agregarFila(campos, gbc, fila++, "Competencia:", campoCompetencia);
        agregarFila(campos, gbc, fila++, "Categoría:", campoCategoria);
        agregarFila(campos, gbc, fila++, "Dificultad:", campoDificultad);
        agregarFila(campos, gbc, fila++, "Recurso multimedia:", campoMultimedia);
        agregarFila(campos, gbc, fila++, "Autor:", campoAutor);
        agregarFila(campos, gbc, fila++, "Observaciones:", campoObservaciones);
        agregarFila(campos, gbc, fila++, "Estado actual:", campoEstadoActual);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        JLabel lblNuevo = new JLabel("Nuevo estado:");
        lblNuevo.setFont(UITheme.FONT_REGULAR_BOLD);
        campos.add(lblNuevo, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        UITheme.styleComboBox(comboNuevoEstado);
        campos.add(comboNuevoEstado, gbc);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setOpaque(false);
        acciones.add(botonActualizar);

        JScrollPane scrollCampos = new JScrollPane(campos);
        scrollCampos.setBorder(null);
        scrollCampos.setOpaque(false);
        scrollCampos.getViewport().setOpaque(false);
        scrollCampos.getVerticalScrollBar().setUnitIncrement(16);

        contenedor.add(scrollCampos, BorderLayout.CENTER);
        contenedor.add(acciones, BorderLayout.SOUTH);
        return contenedor;
    }

    private JPanel crearPanelObservadores() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 8, 0, 0));

        ModernCard cardEstadisticas = new ModernCard(new BorderLayout());
        cardEstadisticas.add(estadisticasView, BorderLayout.CENTER);
        cardEstadisticas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        ModernCard cardPastel = new ModernCard(new BorderLayout());
        cardPastel.add(graficaPastelView, BorderLayout.CENTER);
        cardPastel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        panel.add(cardEstadisticas);
        panel.add(Box.createVerticalStrut(10));
        panel.add(cardPastel);
        return panel;
    }

    private void recargarBanco(String idPreferido) {
        List<Pregunta> preguntas = controller.listarPreguntas();

        comboPreguntas.removeAllItems();
        for (Pregunta pregunta : preguntas) {
            comboPreguntas.addItem(pregunta);
        }

        boolean hayPreguntas = !preguntas.isEmpty();
        comboPreguntas.setEnabled(hayPreguntas);
        botonCargar.setEnabled(hayPreguntas);
        botonActualizar.setEnabled(hayPreguntas);
        comboNuevoEstado.setEnabled(hayPreguntas);

        if (!hayPreguntas) {
            limpiarFormulario("No hay preguntas disponibles en el banco.");
            return;
        }

        if (idPreferido != null) {
            for (int i = 0; i < comboPreguntas.getItemCount(); i++) {
                Pregunta item = comboPreguntas.getItemAt(i);
                if (item.getId().equals(idPreferido)) {
                    comboPreguntas.setSelectedIndex(i);
                    break;
                }
            }
        }

        cargarSeleccion();
    }

    private void cargarSeleccion() {
        Pregunta seleccionada = (Pregunta) comboPreguntas.getSelectedItem();
        if (seleccionada == null) {
            limpiarFormulario("Seleccione una pregunta para consultar sus datos.");
            return;
        }

        controller.buscarPregunta(seleccionada.getId()).ifPresentOrElse(
                this::mostrarPregunta,
                () -> limpiarFormulario("La pregunta seleccionada ya no existe en el banco."));
    }

    private void mostrarPregunta(Pregunta pregunta) {
        campoId.setText(pregunta.getId());
        campoNombre.setText(pregunta.getNombre());
        campoTipo.setText(pregunta.getTipo() != null ? pregunta.getTipo() : "");
        areaPregunta.setText(pregunta.getEnunciado());
        areaPregunta.setCaretPosition(0);
        areaOpciones.setText(pregunta.getOpciones() != null ? pregunta.getOpciones().comoTexto() : "");
        areaOpciones.setCaretPosition(0);
        campoRespuesta.setText(pregunta.getRespuestaCorrecta() != null ? pregunta.getRespuestaCorrecta() : "");
        campoCompetencia.setText(pregunta.getCompetencia() != null ? pregunta.getCompetencia() : "");
        campoCategoria.setText(pregunta.getCategoria() != null ? pregunta.getCategoria() : "");
        campoDificultad.setText(pregunta.getNivelDificultad() != null ? pregunta.getNivelDificultad() : "");
        campoMultimedia.setText(pregunta.getRecursoMultimedia() != null ? pregunta.getRecursoMultimedia() : "");
        campoAutor.setText(pregunta.getAutorLogin() != null ? pregunta.getAutorLogin() : "");
        campoObservaciones.setText(pregunta.getObservaciones() != null ? pregunta.getObservaciones() : "");
        campoEstadoActual.setText(pregunta.getEstado().toString());
        comboNuevoEstado.setSelectedItem(pregunta.getEstado());
    }

    private void actualizarEstado() {
        Pregunta seleccionada = (Pregunta) comboPreguntas.getSelectedItem();
        EstadoPregunta nuevoEstado = (EstadoPregunta) comboNuevoEstado.getSelectedItem();

        ResultadoCambioEstado resultado = controller.cambiarEstado(seleccionada, nuevoEstado);
        switch (resultado) {
            case ACTUALIZADO -> {
                String id = seleccionada.getId();
                recargarBanco(id);
                JOptionPane.showMessageDialog(
                        this,
                        "Estado actualizado correctamente a " + nuevoEstado + ".",
                        "Gestión de preguntas",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            case SIN_CAMBIOS -> JOptionPane.showMessageDialog(
                    this,
                    "La pregunta ya se encuentra en el estado seleccionado. No se realizó ningún cambio.",
                    "Sin cambios",
                    JOptionPane.INFORMATION_MESSAGE);
            case PREGUNTA_NO_ENCONTRADA -> JOptionPane.showMessageDialog(
                    this,
                    "No se encontró la pregunta seleccionada.",
                    "Pregunta inexistente",
                    JOptionPane.ERROR_MESSAGE);
            case ENTRADA_INVALIDA -> JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una pregunta y un estado válidos.",
                    "Datos inválidos",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarFormulario(String mensaje) {
        campoId.setText("");
        campoNombre.setText("");
        campoTipo.setText("");
        areaPregunta.setText(mensaje);
        areaOpciones.setText("");
        campoRespuesta.setText("");
        campoCompetencia.setText("");
        campoCategoria.setText("");
        campoDificultad.setText("");
        campoMultimedia.setText("");
        campoAutor.setText("");
        campoObservaciones.setText("");
        campoEstadoActual.setText("");
    }

    private void registrarObservadores() {
        controller.registrarObservador(estadisticasView);
        controller.registrarObservador(graficaPastelView);
        observadoresRegistrados = true;
    }

    @Override
    public void dispose() {
        if (observadoresRegistrados) {
            controller.retirarObservador(estadisticasView);
            controller.retirarObservador(graficaPastelView);
            observadoresRegistrados = false;
        }
        super.dispose();
    }

    private static JTextField crearCampoSoloLectura() {
        JTextField campo = new JTextField();
        campo.setEditable(false);
        UITheme.styleTextField(campo);
        return campo;
    }

    private static JTextArea crearAreaSoloLectura(int filas) {
        JTextArea area = new JTextArea(filas, 36);
        area.setEditable(false);
        UITheme.styleTextArea(area);
        return area;
    }

    private static void agregarFila(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String etiqueta,
            JTextField campo) {
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(UITheme.FONT_REGULAR_BOLD);
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(campo, gbc);
    }

    private static void agregarArea(
            JPanel panel,
            GridBagConstraints gbc,
            int fila,
            String etiqueta,
            JTextArea area) {
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(UITheme.FONT_REGULAR_BOLD);
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JScrollPane scroll = new JScrollPane(area);
        UITheme.styleScrollPane(scroll);
        panel.add(scroll, gbc);
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
    }

    public static void mostrarEnEdt(PreguntasController controller) {
        Runnable accion = () -> new PreguntasFrame(controller).setVisible(true);
        if (SwingUtilities.isEventDispatchThread()) {
            accion.run();
        } else {
            SwingUtilities.invokeLater(accion);
        }
    }
}
