package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.ResultadoCambioEstado;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * Vista principal del micro patrón MVC para la gestión de estados de preguntas.
 */
public class PreguntasFrame extends JFrame {

    private final PreguntasController controller;
    private final EstadisticasView estadisticasView = new EstadisticasView();
    private final GraficaPastelView graficaPastelView = new GraficaPastelView();

    private final JComboBox<Pregunta> comboPreguntas = new JComboBox<>();
    private final JComboBox<EstadoPregunta> comboNuevoEstado = new JComboBox<>(EstadoPregunta.values());
    private final JTextField campoId = crearCampoSoloLectura();
    private final JTextField campoNombre = crearCampoSoloLectura();
    private final JTextField campoRespuesta = crearCampoSoloLectura();
    private final JTextField campoEstadoActual = crearCampoSoloLectura();
    private final JTextArea areaPregunta = crearAreaSoloLectura(4);
    private final JTextArea areaOpciones = crearAreaSoloLectura(7);
    private final JButton botonCargar = new JButton("Cargar pregunta");
    private final JButton botonActualizar = new JButton("Actualizar estado");

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
        setTitle("Banco de Preguntas Saber Pro - Gestión de preguntas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(900, 610));
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void crearInterfaz() {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        raiz.add(crearPanelSeleccion(), BorderLayout.NORTH);

        JSplitPane division = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                crearPanelFormulario(),
                crearPanelObservadores());
        division.setResizeWeight(0.58);
        division.setOneTouchExpandable(true);
        division.setContinuousLayout(true);

        raiz.add(division, BorderLayout.CENTER);
        setContentPane(raiz);

        comboPreguntas.addActionListener(e -> cargarSeleccion());
        botonCargar.addActionListener(e -> cargarSeleccion());
        botonActualizar.addActionListener(e -> actualizarEstado());
    }

    private JPanel crearPanelSeleccion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Seleccionar pregunta"));

        comboPreguntas.setPreferredSize(new Dimension(410, 28));
        panel.add(new JLabel("Pregunta:"));
        panel.add(comboPreguntas);
        panel.add(botonCargar);
        return panel;
    }

    private JPanel crearPanelFormulario() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));
        contenedor.setBorder(BorderFactory.createTitledBorder("Formulario de pregunta"));

        JPanel campos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int fila = 0;
        agregarFila(campos, gbc, fila++, "Id:", campoId);
        agregarFila(campos, gbc, fila++, "Nombre:", campoNombre);
        agregarArea(campos, gbc, fila++, "Pregunta:", areaPregunta);
        agregarArea(campos, gbc, fila++, "Opciones:", areaOpciones);
        agregarFila(campos, gbc, fila++, "Respuesta correcta:", campoRespuesta);
        agregarFila(campos, gbc, fila++, "Estado actual:", campoEstadoActual);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        campos.add(new JLabel("Nuevo estado:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        campos.add(comboNuevoEstado, gbc);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(botonActualizar);

        contenedor.add(new JScrollPane(campos), BorderLayout.CENTER);
        contenedor.add(acciones, BorderLayout.SOUTH);
        return contenedor;
    }

    private JPanel crearPanelObservadores() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));
        estadisticasView.setMaximumSize(new Dimension(Integer.MAX_VALUE, 145));
        graficaPastelView.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        panel.add(estadisticasView);
        panel.add(graficaPastelView);
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
        areaPregunta.setText(pregunta.getEnunciado());
        areaPregunta.setCaretPosition(0);
        areaOpciones.setText(pregunta.getOpciones().comoTexto());
        areaOpciones.setCaretPosition(0);
        campoRespuesta.setText(pregunta.getRespuestaCorrecta());
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
                        "Estado actualizado correctamente.",
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
        areaPregunta.setText(mensaje);
        areaOpciones.setText("");
        campoRespuesta.setText("");
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
        return campo;
    }

    private static JTextArea crearAreaSoloLectura(int filas) {
        JTextArea area = new JTextArea(filas, 36);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
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
        panel.add(new JLabel(etiqueta), gbc);
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
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(area), gbc);
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
