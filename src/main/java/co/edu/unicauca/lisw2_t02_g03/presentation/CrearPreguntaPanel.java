package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.OpcionesPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.pipeline.ValidationException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.UUID;

/**
 * Panel para la creación manual de preguntas por parte del autor, validado por el pipeline.
 */
public class CrearPreguntaPanel extends JPanel {

    private final PreguntasController controller;
    private final Usuario autorActual;

    private JTextField txtTitulo;
    private JTextArea txtEnunciado;
    private JTextField txtOpcionA;
    private JTextField txtOpcionB;
    private JTextField txtOpcionC;
    private JTextField txtOpcionD;
    private JComboBox<String> comboRespuestaCorrecta;
    private JTextField txtCompetencia;
    private JTextField txtCategoria;
    private JComboBox<String> comboDificultad;

    public CrearPreguntaPanel(PreguntasController controller, Usuario autorActual) {
        this.controller = controller;
        this.autorActual = autorActual;

        setLayout(new BorderLayout(0, 16));
        setOpaque(false);

        crearCabecera();
        crearFormulario();
        crearBarraAcciones();
    }

    private void crearCabecera() {
        ModernCard cardCabecera = new ModernCard(new BorderLayout());

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);

        JLabel lblTitulo = new JLabel("✏️ Crear Nueva Pregunta Manual");
        lblTitulo.setFont(UITheme.FONT_TITLE_MEDIUM);
        lblTitulo.setForeground(UITheme.COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Complete la formulación del ítem. La pregunta será validada por el pipeline y enviada a revisión.");
        lblSub.setFont(UITheme.FONT_SUBTITLE);
        lblSub.setForeground(UITheme.COLOR_TEXT_MUTED);

        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(4));
        panelTexto.add(lblSub);

        cardCabecera.add(panelTexto, BorderLayout.CENTER);
        add(cardCabecera, BorderLayout.NORTH);
    }

    private void crearFormulario() {
        JPanel contenedorCards = new JPanel();
        contenedorCards.setLayout(new BoxLayout(contenedorCards, BoxLayout.Y_AXIS));
        contenedorCards.setOpaque(false);

        // Tarjeta 1: Enunciado
        ModernCard cardEnunciado = new ModernCard(new BorderLayout(0, 10));
        JLabel lblSeccion1 = new JLabel("1. Formulación del Enunciado");
        lblSeccion1.setFont(UITheme.FONT_TITLE_SMALL);
        lblSeccion1.setForeground(UITheme.COLOR_PRIMARY);

        JPanel panelCampos1 = new JPanel(new GridLayout(0, 1, 0, 6));
        panelCampos1.setOpaque(false);

        JLabel lblTit = new JLabel("Título descriptivo del ítem:");
        lblTit.setFont(UITheme.FONT_REGULAR_BOLD);
        txtTitulo = new JTextField();
        UITheme.styleTextField(txtTitulo);

        JLabel lblEnun = new JLabel("Enunciado o contexto de la pregunta:");
        lblEnun.setFont(UITheme.FONT_REGULAR_BOLD);
        txtEnunciado = new JTextArea(4, 30);
        UITheme.styleTextArea(txtEnunciado);
        JScrollPane scrollEnun = new JScrollPane(txtEnunciado);
        UITheme.styleScrollPane(scrollEnun);

        panelCampos1.add(lblTit);
        panelCampos1.add(txtTitulo);
        panelCampos1.add(lblEnun);
        panelCampos1.add(scrollEnun);

        cardEnunciado.add(lblSeccion1, BorderLayout.NORTH);
        cardEnunciado.add(panelCampos1, BorderLayout.CENTER);

        // Tarjeta 2: Opciones
        ModernCard cardOpciones = new ModernCard(new BorderLayout(0, 10));
        JLabel lblSeccion2 = new JLabel("2. Opciones de Respuesta y Clave Correcta");
        lblSeccion2.setFont(UITheme.FONT_TITLE_SMALL);
        lblSeccion2.setForeground(UITheme.COLOR_PRIMARY);

        JPanel panelCampos2 = new JPanel(new GridLayout(0, 2, 12, 8));
        panelCampos2.setOpaque(false);

        txtOpcionA = new JTextField();
        UITheme.styleTextField(txtOpcionA);
        txtOpcionB = new JTextField();
        UITheme.styleTextField(txtOpcionB);
        txtOpcionC = new JTextField();
        UITheme.styleTextField(txtOpcionC);
        txtOpcionD = new JTextField();
        UITheme.styleTextField(txtOpcionD);

        panelCampos2.add(new JLabel("Opción A:"));
        panelCampos2.add(txtOpcionA);
        panelCampos2.add(new JLabel("Opción B:"));
        panelCampos2.add(txtOpcionB);
        panelCampos2.add(new JLabel("Opción C:"));
        panelCampos2.add(txtOpcionC);
        panelCampos2.add(new JLabel("Opción D:"));
        panelCampos2.add(txtOpcionD);

        JPanel panelRespuesta = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelRespuesta.setOpaque(false);
        JLabel lblResp = new JLabel("Respuesta Correcta:");
        lblResp.setFont(UITheme.FONT_REGULAR_BOLD);
        comboRespuestaCorrecta = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        UITheme.styleComboBox(comboRespuestaCorrecta);
        panelRespuesta.add(lblResp);
        panelRespuesta.add(comboRespuestaCorrecta);

        JPanel wrapperOpciones = new JPanel(new BorderLayout(0, 8));
        wrapperOpciones.setOpaque(false);
        wrapperOpciones.add(panelCampos2, BorderLayout.CENTER);
        wrapperOpciones.add(panelRespuesta, BorderLayout.SOUTH);

        cardOpciones.add(lblSeccion2, BorderLayout.NORTH);
        cardOpciones.add(wrapperOpciones, BorderLayout.CENTER);

        // Tarjeta 3: Clasificación
        ModernCard cardClasificacion = new ModernCard(new BorderLayout(0, 10));
        JLabel lblSeccion3 = new JLabel("3. Clasificación Saber Pro");
        lblSeccion3.setFont(UITheme.FONT_TITLE_SMALL);
        lblSeccion3.setForeground(UITheme.COLOR_PRIMARY);

        JPanel panelCampos3 = new JPanel(new GridLayout(1, 3, 12, 0));
        panelCampos3.setOpaque(false);

        JPanel colComp = new JPanel(new BorderLayout(0, 4));
        colComp.setOpaque(false);
        colComp.add(new JLabel("Competencia evaluada:"), BorderLayout.NORTH);
        txtCompetencia = new JTextField("Lectura crítica");
        UITheme.styleTextField(txtCompetencia);
        colComp.add(txtCompetencia, BorderLayout.CENTER);

        JPanel colCat = new JPanel(new BorderLayout(0, 4));
        colCat.setOpaque(false);
        colCat.add(new JLabel("Categoría / Componente:"), BorderLayout.NORTH);
        txtCategoria = new JTextField("Genéricas");
        UITheme.styleTextField(txtCategoria);
        colCat.add(txtCategoria, BorderLayout.CENTER);

        JPanel colDif = new JPanel(new BorderLayout(0, 4));
        colDif.setOpaque(false);
        colDif.add(new JLabel("Nivel de dificultad:"), BorderLayout.NORTH);
        comboDificultad = new JComboBox<>(new String[]{"Fácil", "Medio", "Difícil"});
        UITheme.styleComboBox(comboDificultad);
        colDif.add(comboDificultad, BorderLayout.CENTER);

        panelCampos3.add(colComp);
        panelCampos3.add(colCat);
        panelCampos3.add(colDif);

        cardClasificacion.add(lblSeccion3, BorderLayout.NORTH);
        cardClasificacion.add(panelCampos3, BorderLayout.CENTER);

        contenedorCards.add(cardEnunciado);
        contenedorCards.add(Box.createVerticalStrut(12));
        contenedorCards.add(cardOpciones);
        contenedorCards.add(Box.createVerticalStrut(12));
        contenedorCards.add(cardClasificacion);

        JScrollPane scrollForm = new JScrollPane(contenedorCards);
        scrollForm.setBorder(null);
        scrollForm.setOpaque(false);
        scrollForm.getViewport().setOpaque(false);
        scrollForm.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollForm, BorderLayout.CENTER);
    }

    private void crearBarraAcciones() {
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelAcciones.setOpaque(false);
        panelAcciones.setBorder(new EmptyBorder(8, 0, 0, 0));

        ModernButton btnLimpiar = new ModernButton("Limpiar Campos", ModernButton.Variant.OUTLINE);
        ModernButton btnGuardar = new ModernButton("💾 Guardar y Enviar a Revisión", ModernButton.Variant.SUCCESS);

        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnGuardar.addActionListener(e -> guardarPregunta());

        panelAcciones.add(btnLimpiar);
        panelAcciones.add(btnGuardar);

        add(panelAcciones, BorderLayout.SOUTH);
    }

    private void guardarPregunta() {
        String titulo = txtTitulo.getText().trim();
        String enunciado = txtEnunciado.getText().trim();
        String opA = txtOpcionA.getText().trim();
        String opB = txtOpcionB.getText().trim();
        String opC = txtOpcionC.getText().trim();
        String opD = txtOpcionD.getText().trim();
        String respuesta = (String) comboRespuestaCorrecta.getSelectedItem();
        String competencia = txtCompetencia.getText().trim();
        String categoria = txtCategoria.getText().trim();
        String dificultad = (String) comboDificultad.getSelectedItem();

        if (titulo.isBlank() || enunciado.isBlank() || opA.isBlank() || opB.isBlank() || opC.isBlank() || opD.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos de título, enunciado y las 4 opciones son obligatorios.",
                    "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String id = "P-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            Pregunta pregunta = new Pregunta(
                    id,
                    titulo,
                    enunciado,
                    "SELECCION_MULTIPLE",
                    OpcionesPregunta.de(opA, opB, opC, opD),
                    respuesta,
                    EstadoPregunta.BORRADOR,
                    competencia,
                    categoria,
                    dificultad,
                    null,
                    autorActual != null ? autorActual.getLogin() : "autor",
                    null
            );

            // Validar mediante el pipeline de tuberías y filtros
            Pregunta validada = controller.validarPregunta(pregunta);

            // Guardar en el repositorio
            boolean guardada = controller.guardarPregunta(validada);

            if (guardada) {
                // Enviar a revisión
                controller.enviarARevision(id);

                JOptionPane.showMessageDialog(this,
                        "¡Pregunta validada con éxito por el pipeline y enviada a revisión!\nID Asignado: " + id,
                        "Pregunta Creada", JOptionPane.INFORMATION_MESSAGE);

                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar la pregunta en el banco.",
                        "Error de Guardado", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this,
                    "El Pipeline de validación rechazó la pregunta:\n• " + ex.getMessage(),
                    "Fallo en Validación del Pipeline", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ocurrió un error al procesar la pregunta:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtEnunciado.setText("");
        txtOpcionA.setText("");
        txtOpcionB.setText("");
        txtOpcionC.setText("");
        txtOpcionD.setText("");
        comboRespuestaCorrecta.setSelectedIndex(0);
        txtCompetencia.setText("Lectura crítica");
        txtCategoria.setText("Genéricas");
        comboDificultad.setSelectedIndex(0);
    }
}
