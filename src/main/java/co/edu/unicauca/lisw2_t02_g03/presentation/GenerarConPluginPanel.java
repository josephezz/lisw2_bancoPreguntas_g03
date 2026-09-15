package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.model.Usuario;
import co.edu.unicauca.lisw2_t02_g03.pipeline.ValidationException;
import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionPlugin;
import co.edu.unicauca.lisw2_t02_g03.plugins.QuestionRequest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

/**
 * Panel para la generación de preguntas mediante plugins del microkernel.
 * Implementa interfaces gráficas dedicadas e independientes para cada plugin soportado:
 * Selección Múltiple, Análisis de Caso y Multimedia (con vista previa).
 */
public class GenerarConPluginPanel extends JPanel {

    private final PreguntasController controller;
    private final Usuario autorActual;

    private CardLayout cardLayout;
    private JPanel panelFormularios;

    // Botones de selección de plugin
    private ModernButton btnTabSeleccion;
    private ModernButton btnTabCaso;
    private ModernButton btnTabMultimedia;

    // --- FORMULARIO SELECCIÓN MÚLTIPLE ---
    private JTextField txtSmTitulo;
    private JTextArea txtSmEnunciado;
    private JTextField txtSmOpA, txtSmOpB, txtSmOpC, txtSmOpD;
    private JComboBox<String> comboSmRespuesta;
    private JTextField txtSmCompetencia, txtSmCategoria;
    private JComboBox<String> comboSmDificultad;

    // --- FORMULARIO ANÁLISIS DE CASO ---
    private JTextField txtCasoNombre;
    private JTextArea txtCasoContexto;
    private JTextArea txtCasoEscenario;
    private JTextField txtCasoPreguntaAsociada;
    private JTextField txtCasoOpA, txtCasoOpB, txtCasoOpC, txtCasoOpD;
    private JComboBox<String> comboCasoRespuesta;
    private JTextField txtCasoCompetencia, txtCasoCategoria;
    private JComboBox<String> comboCasoDificultad;

    // --- FORMULARIO MULTIMEDIA ---
    private JTextField txtMmTitulo;
    private JTextArea txtMmEnunciado;
    private JComboBox<String> comboMmTipoRecurso;
    private JTextField txtMmRutaArchivo;
    private ImagePreviewPanel panelVistaPrevia;
    private JTextField txtMmOpA, txtMmOpB, txtMmOpC, txtMmOpD;
    private JComboBox<String> comboMmRespuesta;
    private JTextField txtMmCompetencia, txtMmCategoria;
    private JComboBox<String> comboMmDificultad;

    public GenerarConPluginPanel(PreguntasController controller, Usuario autorActual) {
        this.controller = controller;
        this.autorActual = autorActual;

        setLayout(new BorderLayout(0, 14));
        setOpaque(false);

        crearCabecera();
        crearSelectorPestanas();
        crearContenedorFormularios();
    }

    private void crearCabecera() {
        ModernCard cardCabecera = new ModernCard(new BorderLayout());

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);

        JLabel lblTitulo = new JLabel("🧩 Generación Mediante Plugins (Microkernel)");
        lblTitulo.setFont(UITheme.FONT_TITLE_MEDIUM);
        lblTitulo.setForeground(UITheme.COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Seleccione el plugin deseado para desplegar su formulario especializado de generación");
        lblSub.setFont(UITheme.FONT_SUBTITLE);
        lblSub.setForeground(UITheme.COLOR_TEXT_MUTED);

        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(4));
        panelTexto.add(lblSub);

        cardCabecera.add(panelTexto, BorderLayout.CENTER);
        add(cardCabecera, BorderLayout.NORTH);
    }

    private void crearSelectorPestanas() {
        JPanel panelTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelTabs.setOpaque(false);

        btnTabSeleccion = new ModernButton("🔘 1. Selección Múltiple", ModernButton.Variant.PRIMARY);
        btnTabCaso = new ModernButton("📋 2. Análisis de Caso", ModernButton.Variant.OUTLINE);
        btnTabMultimedia = new ModernButton("🖼️ 3. Multimedia", ModernButton.Variant.OUTLINE);

        btnTabSeleccion.addActionListener(e -> seleccionarTab("TAB_SELECCION", btnTabSeleccion));
        btnTabCaso.addActionListener(e -> seleccionarTab("TAB_CASO", btnTabCaso));
        btnTabMultimedia.addActionListener(e -> seleccionarTab("TAB_MULTIMEDIA", btnTabMultimedia));

        panelTabs.add(btnTabSeleccion);
        panelTabs.add(btnTabCaso);
        panelTabs.add(btnTabMultimedia);

        add(panelTabs, BorderLayout.BEFORE_FIRST_LINE); // Just below header in layout structure
        // But since we use BorderLayout.NORTH for header, let's wrap header and tabs in a northContainer
    }

    private void crearContenedorFormularios() {
        // Envolver cabecera y tabs en un panel superior único
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));
        panelNorte.setOpaque(false);

        ModernCard cardCabecera = new ModernCard(new BorderLayout());
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);
        JLabel lblTitulo = new JLabel("🧩 Generación Mediante Plugins (Microkernel)");
        lblTitulo.setFont(UITheme.FONT_TITLE_MEDIUM);
        lblTitulo.setForeground(UITheme.COLOR_PRIMARY_DARK);
        JLabel lblSub = new JLabel("Seleccione el plugin deseado para desplegar su formulario especializado de generación");
        lblSub.setFont(UITheme.FONT_SUBTITLE);
        lblSub.setForeground(UITheme.COLOR_TEXT_MUTED);
        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(4));
        panelTexto.add(lblSub);
        cardCabecera.add(panelTexto, BorderLayout.CENTER);

        JPanel panelTabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelTabs.setOpaque(false);
        panelTabs.setBorder(new EmptyBorder(8, 0, 8, 0));

        btnTabSeleccion = new ModernButton("🔘 Selección Múltiple", ModernButton.Variant.PRIMARY);
        btnTabCaso = new ModernButton("📋 Análisis de Caso", ModernButton.Variant.OUTLINE);
        btnTabMultimedia = new ModernButton("🖼️ Multimedia", ModernButton.Variant.OUTLINE);

        btnTabSeleccion.addActionListener(e -> seleccionarTab("TAB_SELECCION", btnTabSeleccion));
        btnTabCaso.addActionListener(e -> seleccionarTab("TAB_CASO", btnTabCaso));
        btnTabMultimedia.addActionListener(e -> seleccionarTab("TAB_MULTIMEDIA", btnTabMultimedia));

        panelTabs.add(btnTabSeleccion);
        panelTabs.add(btnTabCaso);
        panelTabs.add(btnTabMultimedia);

        panelNorte.add(cardCabecera);
        panelNorte.add(panelTabs);

        add(panelNorte, BorderLayout.NORTH);

        // Contenedor dinámico de formularios específicos
        cardLayout = new CardLayout();
        panelFormularios = new JPanel(cardLayout);
        panelFormularios.setOpaque(false);

        panelFormularios.add(crearFormularioSeleccionMultiple(), "TAB_SELECCION");
        panelFormularios.add(crearFormularioAnalisisCaso(), "TAB_CASO");
        panelFormularios.add(crearFormularioMultimedia(), "TAB_MULTIMEDIA");

        add(panelFormularios, BorderLayout.CENTER);
    }

    private void seleccionarTab(String nombreCard, ModernButton btnActivo) {
        cardLayout.show(panelFormularios, nombreCard);

        btnTabSeleccion.setBackground(Color.WHITE); // will repaint with variant
        // Refresh buttons visually by replacing text or switching style if needed
        btnTabSeleccion.repaint();
        btnTabCaso.repaint();
        btnTabMultimedia.repaint();
    }

    // =========================================================================
    // 1. INTERFAZ ESPECÍFICA: SELECCIÓN MÚLTIPLE (MultipleChoiceQuestionPlugin)
    // =========================================================================
    private JPanel crearFormularioSeleccionMultiple() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setOpaque(false);

        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setOpaque(false);

        ModernCard cardInfo = new ModernCard(new BorderLayout(0, 10));
        JLabel lblTituloPlugin = new JLabel("Formulario: Plugin de Selección Múltiple (MultipleChoiceQuestionPlugin)");
        lblTituloPlugin.setFont(UITheme.FONT_TITLE_SMALL);
        lblTituloPlugin.setForeground(UITheme.COLOR_PRIMARY);

        JPanel campos = new JPanel(new GridLayout(0, 1, 0, 6));
        campos.setOpaque(false);

        JLabel lblTit = new JLabel("Título del Ítem:");
        lblTit.setFont(UITheme.FONT_REGULAR_BOLD);
        txtSmTitulo = new JTextField();
        UITheme.styleTextField(txtSmTitulo);

        JLabel lblEnun = new JLabel("Enunciado de la Pregunta:");
        lblEnun.setFont(UITheme.FONT_REGULAR_BOLD);
        txtSmEnunciado = new JTextArea(3, 30);
        UITheme.styleTextArea(txtSmEnunciado);
        JScrollPane scrollEnun = new JScrollPane(txtSmEnunciado);
        UITheme.styleScrollPane(scrollEnun);

        campos.add(lblTit);
        campos.add(txtSmTitulo);
        campos.add(lblEnun);
        campos.add(scrollEnun);

        cardInfo.add(lblTituloPlugin, BorderLayout.NORTH);
        cardInfo.add(campos, BorderLayout.CENTER);

        // Opciones
        ModernCard cardOpciones = new ModernCard(new BorderLayout(0, 10));
        JLabel lblOpc = new JLabel("Opciones A-D y Respuesta Correcta");
        lblOpc.setFont(UITheme.FONT_TITLE_SMALL);
        lblOpc.setForeground(UITheme.COLOR_PRIMARY);

        JPanel panelOps = new JPanel(new GridLayout(0, 2, 10, 6));
        panelOps.setOpaque(false);

        txtSmOpA = new JTextField(); UITheme.styleTextField(txtSmOpA);
        txtSmOpB = new JTextField(); UITheme.styleTextField(txtSmOpB);
        txtSmOpC = new JTextField(); UITheme.styleTextField(txtSmOpC);
        txtSmOpD = new JTextField(); UITheme.styleTextField(txtSmOpD);

        panelOps.add(new JLabel("Opción A:")); panelOps.add(txtSmOpA);
        panelOps.add(new JLabel("Opción B:")); panelOps.add(txtSmOpB);
        panelOps.add(new JLabel("Opción C:")); panelOps.add(txtSmOpC);
        panelOps.add(new JLabel("Opción D:")); panelOps.add(txtSmOpD);

        JPanel pResp = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pResp.setOpaque(false);
        comboSmRespuesta = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        UITheme.styleComboBox(comboSmRespuesta);
        pResp.add(new JLabel("Respuesta Correcta:"));
        pResp.add(comboSmRespuesta);

        JPanel wrapperOps = new JPanel(new BorderLayout(0, 8));
        wrapperOps.setOpaque(false);
        wrapperOps.add(panelOps, BorderLayout.CENTER);
        wrapperOps.add(pResp, BorderLayout.SOUTH);

        cardOpciones.add(lblOpc, BorderLayout.NORTH);
        cardOpciones.add(wrapperOps, BorderLayout.CENTER);

        // Metadatos
        ModernCard cardMeta = new ModernCard(new GridLayout(1, 3, 10, 0));
        txtSmCompetencia = new JTextField("Lectura crítica"); UITheme.styleTextField(txtSmCompetencia);
        txtSmCategoria = new JTextField("Genéricas"); UITheme.styleTextField(txtSmCategoria);
        comboSmDificultad = new JComboBox<>(new String[]{"Fácil", "Medio", "Difícil"}); UITheme.styleComboBox(comboSmDificultad);

        JPanel pC = new JPanel(new BorderLayout(0, 4)); pC.setOpaque(false); pC.add(new JLabel("Competencia:"), BorderLayout.NORTH); pC.add(txtSmCompetencia);
        JPanel pCat = new JPanel(new BorderLayout(0, 4)); pCat.setOpaque(false); pCat.add(new JLabel("Categoría:"), BorderLayout.NORTH); pCat.add(txtSmCategoria);
        JPanel pD = new JPanel(new BorderLayout(0, 4)); pD.setOpaque(false); pD.add(new JLabel("Dificultad:"), BorderLayout.NORTH); pD.add(comboSmDificultad);
        cardMeta.add(pC); cardMeta.add(pCat); cardMeta.add(pD);

        contenedor.add(cardInfo);
        contenedor.add(Box.createVerticalStrut(10));
        contenedor.add(cardOpciones);
        contenedor.add(Box.createVerticalStrut(10));
        contenedor.add(cardMeta);

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null); scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // Botón generar
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelAcciones.setOpaque(false);
        ModernButton btnGenerarSm = new ModernButton("⚡ Generar con Plugin Selección Múltiple", ModernButton.Variant.SUCCESS);
        btnGenerarSm.addActionListener(e -> generarSeleccionMultiple());
        panelAcciones.add(btnGenerarSm);

        root.add(scroll, BorderLayout.CENTER);
        root.add(panelAcciones, BorderLayout.SOUTH);
        return root;
    }

    private void generarSeleccionMultiple() {
        String titulo = txtSmTitulo.getText().trim();
        String enun = txtSmEnunciado.getText().trim();
        String opA = txtSmOpA.getText().trim();
        String opB = txtSmOpB.getText().trim();
        String opC = txtSmOpC.getText().trim();
        String opD = txtSmOpD.getText().trim();
        String resp = (String) comboSmRespuesta.getSelectedItem();
        String comp = txtSmCompetencia.getText().trim();
        String cat = txtSmCategoria.getText().trim();
        String dif = (String) comboSmDificultad.getSelectedItem();

        if (titulo.isBlank() || enun.isBlank() || opA.isBlank() || opB.isBlank() || opC.isBlank() || opD.isBlank()) {
            JOptionPane.showMessageDialog(this, "Todos los campos y opciones son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        QuestionRequest request = new QuestionRequest();
        request.setTitulo(titulo);
        request.setContenido(enun);
        request.setOpciones(List.of(opA, opB, opC, opD));
        request.setRespuestaCorrecta(resp);
        request.setCompetencia(comp);
        request.setCategoria(cat);
        request.setNivelDificultad(dif);
        request.setAutorLogin(autorActual != null ? autorActual.getLogin() : "autor");

        procesarGeneracionPlugin("SELECCION_MULTIPLE", request, "Selección Múltiple");
    }

    // =========================================================================
    // 2. INTERFAZ ESPECÍFICA: ANÁLISIS DE CASO (CaseQuestionPlugin)
    // =========================================================================
    private JPanel crearFormularioAnalisisCaso() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setOpaque(false);

        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setOpaque(false);

        ModernCard cardCaso = new ModernCard(new BorderLayout(0, 10));
        JLabel lblTituloPlugin = new JLabel("Formulario Especializado: Plugin Análisis de Caso (CaseQuestionPlugin)");
        lblTituloPlugin.setFont(UITheme.FONT_TITLE_SMALL);
        lblTituloPlugin.setForeground(UITheme.COLOR_PRIMARY);

        JPanel campos = new JPanel(new GridLayout(0, 1, 0, 6));
        campos.setOpaque(false);

        JLabel lblNom = new JLabel("Nombre o Título del Caso:");
        lblNom.setFont(UITheme.FONT_REGULAR_BOLD);
        txtCasoNombre = new JTextField(); UITheme.styleTextField(txtCasoNombre);

        JLabel lblCtx = new JLabel("Contexto General del Caso (Antecedentes / Marco):");
        lblCtx.setFont(UITheme.FONT_REGULAR_BOLD);
        txtCasoContexto = new JTextArea(3, 30); UITheme.styleTextArea(txtCasoContexto);
        JScrollPane scrollCtx = new JScrollPane(txtCasoContexto); UITheme.styleScrollPane(scrollCtx);

        JLabel lblEsc = new JLabel("Descripción del Escenario / Situación Problemática:");
        lblEsc.setFont(UITheme.FONT_REGULAR_BOLD);
        txtCasoEscenario = new JTextArea(3, 30); UITheme.styleTextArea(txtCasoEscenario);
        JScrollPane scrollEsc = new JScrollPane(txtCasoEscenario); UITheme.styleScrollPane(scrollEsc);

        JLabel lblPreg = new JLabel("Pregunta de Decisión o Análisis Asociada al Caso:");
        lblPreg.setFont(UITheme.FONT_REGULAR_BOLD);
        txtCasoPreguntaAsociada = new JTextField(); UITheme.styleTextField(txtCasoPreguntaAsociada);

        campos.add(lblNom); campos.add(txtCasoNombre);
        campos.add(lblCtx); campos.add(scrollCtx);
        campos.add(lblEsc); campos.add(scrollEsc);
        campos.add(lblPreg); campos.add(txtCasoPreguntaAsociada);

        cardCaso.add(lblTituloPlugin, BorderLayout.NORTH);
        cardCaso.add(campos, BorderLayout.CENTER);

        // Opciones analíticas
        ModernCard cardOpciones = new ModernCard(new BorderLayout(0, 10));
        JLabel lblOpc = new JLabel("Opciones de Acción / Alternativas Analíticas");
        lblOpc.setFont(UITheme.FONT_TITLE_SMALL); lblOpc.setForeground(UITheme.COLOR_PRIMARY);

        JPanel panelOps = new JPanel(new GridLayout(0, 2, 10, 6));
        panelOps.setOpaque(false);
        txtCasoOpA = new JTextField(); UITheme.styleTextField(txtCasoOpA);
        txtCasoOpB = new JTextField(); UITheme.styleTextField(txtCasoOpB);
        txtCasoOpC = new JTextField(); UITheme.styleTextField(txtCasoOpC);
        txtCasoOpD = new JTextField(); UITheme.styleTextField(txtCasoOpD);

        panelOps.add(new JLabel("Alternativa A:")); panelOps.add(txtCasoOpA);
        panelOps.add(new JLabel("Alternativa B:")); panelOps.add(txtCasoOpB);
        panelOps.add(new JLabel("Alternativa C:")); panelOps.add(txtCasoOpC);
        panelOps.add(new JLabel("Alternativa D:")); panelOps.add(txtCasoOpD);

        JPanel pResp = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pResp.setOpaque(false);
        comboCasoRespuesta = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        UITheme.styleComboBox(comboCasoRespuesta);
        pResp.add(new JLabel("Respuesta Esperada / Decisión Óptima:"));
        pResp.add(comboCasoRespuesta);

        JPanel wrapperOps = new JPanel(new BorderLayout(0, 8));
        wrapperOps.setOpaque(false);
        wrapperOps.add(panelOps, BorderLayout.CENTER);
        wrapperOps.add(pResp, BorderLayout.SOUTH);

        cardOpciones.add(lblOpc, BorderLayout.NORTH);
        cardOpciones.add(wrapperOps, BorderLayout.CENTER);

        // Metadatos
        ModernCard cardMeta = new ModernCard(new GridLayout(1, 3, 10, 0));
        txtCasoCompetencia = new JTextField("Competencias ciudadanas"); UITheme.styleTextField(txtCasoCompetencia);
        txtCasoCategoria = new JTextField("Análisis situacional"); UITheme.styleTextField(txtCasoCategoria);
        comboCasoDificultad = new JComboBox<>(new String[]{"Fácil", "Medio", "Difícil"}); UITheme.styleComboBox(comboCasoDificultad);

        JPanel pC = new JPanel(new BorderLayout(0, 4)); pC.setOpaque(false); pC.add(new JLabel("Competencia:"), BorderLayout.NORTH); pC.add(txtCasoCompetencia);
        JPanel pCat = new JPanel(new BorderLayout(0, 4)); pCat.setOpaque(false); pCat.add(new JLabel("Categoría:"), BorderLayout.NORTH); pCat.add(txtCasoCategoria);
        JPanel pD = new JPanel(new BorderLayout(0, 4)); pD.setOpaque(false); pD.add(new JLabel("Dificultad:"), BorderLayout.NORTH); pD.add(comboCasoDificultad);
        cardMeta.add(pC); cardMeta.add(pCat); cardMeta.add(pD);

        contenedor.add(cardCaso);
        contenedor.add(Box.createVerticalStrut(10));
        contenedor.add(cardOpciones);
        contenedor.add(Box.createVerticalStrut(10));
        contenedor.add(cardMeta);

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null); scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelAcciones.setOpaque(false);
        ModernButton btnGenerarCaso = new ModernButton("⚡ Generar con Plugin Análisis de Caso", ModernButton.Variant.SUCCESS);
        btnGenerarCaso.addActionListener(e -> generarAnalisisCaso());
        panelAcciones.add(btnGenerarCaso);

        root.add(scroll, BorderLayout.CENTER);
        root.add(panelAcciones, BorderLayout.SOUTH);
        return root;
    }

    private void generarAnalisisCaso() {
        String nombre = txtCasoNombre.getText().trim();
        String contexto = txtCasoContexto.getText().trim();
        String escenario = txtCasoEscenario.getText().trim();
        String pregunta = txtCasoPreguntaAsociada.getText().trim();
        String opA = txtCasoOpA.getText().trim();
        String opB = txtCasoOpB.getText().trim();
        String opC = txtCasoOpC.getText().trim();
        String opD = txtCasoOpD.getText().trim();
        String resp = (String) comboCasoRespuesta.getSelectedItem();
        String comp = txtCasoCompetencia.getText().trim();
        String cat = txtCasoCategoria.getText().trim();
        String dif = (String) comboCasoDificultad.getSelectedItem();

        if (nombre.isBlank() || contexto.isBlank() || escenario.isBlank() || pregunta.isBlank()
                || opA.isBlank() || opB.isBlank() || opC.isBlank() || opD.isBlank()) {
            JOptionPane.showMessageDialog(this, "Todos los datos del caso, escenario y opciones son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String contenidoArmado = "CONTEXTO DEL CASO:\n" + contexto + "\n\nESCENARIO:\n" + escenario + "\n\nPREGUNTA:\n" + pregunta;

        QuestionRequest request = new QuestionRequest();
        request.setTitulo(nombre);
        request.setContenido(contenidoArmado);
        request.setOpciones(List.of(opA, opB, opC, opD));
        request.setRespuestaCorrecta(resp);
        request.setCompetencia(comp);
        request.setCategoria(cat);
        request.setNivelDificultad(dif);
        request.setAutorLogin(autorActual != null ? autorActual.getLogin() : "autor");

        procesarGeneracionPlugin("CASO", request, "Análisis de Caso");
    }

    // =========================================================================
    // 3. INTERFAZ ESPECÍFICA: MULTIMEDIA (MultimediaQuestionPlugin)
    // =========================================================================
    private JPanel crearFormularioMultimedia() {
        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setOpaque(false);

        JPanel contenedor = new JPanel();
        contenedor.setLayout(new BoxLayout(contenedor, BoxLayout.Y_AXIS));
        contenedor.setOpaque(false);

        ModernCard cardMm = new ModernCard(new BorderLayout(0, 10));
        JLabel lblTituloPlugin = new JLabel("Formulario Especializado: Plugin Multimedia (MultimediaQuestionPlugin)");
        lblTituloPlugin.setFont(UITheme.FONT_TITLE_SMALL);
        lblTituloPlugin.setForeground(UITheme.COLOR_PRIMARY);

        JPanel campos = new JPanel(new GridLayout(0, 1, 0, 6));
        campos.setOpaque(false);

        JLabel lblTit = new JLabel("Título del Ítem Multimedia:");
        lblTit.setFont(UITheme.FONT_REGULAR_BOLD);
        txtMmTitulo = new JTextField(); UITheme.styleTextField(txtMmTitulo);

        JLabel lblEnun = new JLabel("Enunciado de la Pregunta:");
        lblEnun.setFont(UITheme.FONT_REGULAR_BOLD);
        txtMmEnunciado = new JTextArea(3, 30); UITheme.styleTextArea(txtMmEnunciado);
        JScrollPane scrollEnun = new JScrollPane(txtMmEnunciado); UITheme.styleScrollPane(scrollEnun);

        campos.add(lblTit); campos.add(txtMmTitulo);
        campos.add(lblEnun); campos.add(scrollEnun);

        // Subpanel Recurso Multimedia con Examinar y Vista Previa
        JPanel panelRecurso = new JPanel(new BorderLayout(10, 10));
        panelRecurso.setOpaque(false);
        panelRecurso.setBorder(BorderFactory.createTitledBorder("Recurso Multimedia Vinculado"));

        JPanel panelFilaArchivo = new JPanel(new BorderLayout(8, 0));
        panelFilaArchivo.setOpaque(false);

        comboMmTipoRecurso = new JComboBox<>(new String[]{"Imagen (JPG, PNG)", "Audio (MP3, WAV)", "Video (MP4)", "Enlace / URL"});
        UITheme.styleComboBox(comboMmTipoRecurso);

        txtMmRutaArchivo = new JTextField();
        UITheme.styleTextField(txtMmRutaArchivo);
        txtMmRutaArchivo.setToolTipText("Ruta del archivo local o URL web");

        ModernButton btnExaminar = new ModernButton("Examinar...", ModernButton.Variant.OUTLINE);
        btnExaminar.addActionListener(e -> examinarArchivoMultimedia());

        JPanel panelTipoYRuta = new JPanel(new BorderLayout(8, 0));
        panelTipoYRuta.setOpaque(false);
        panelTipoYRuta.add(comboMmTipoRecurso, BorderLayout.WEST);
        panelTipoYRuta.add(txtMmRutaArchivo, BorderLayout.CENTER);
        panelTipoYRuta.add(btnExaminar, BorderLayout.EAST);

        panelVistaPrevia = new ImagePreviewPanel();
        txtMmRutaArchivo.addActionListener(e -> panelVistaPrevia.cargarRecurso(txtMmRutaArchivo.getText().trim()));

        panelRecurso.add(panelTipoYRuta, BorderLayout.NORTH);
        panelRecurso.add(panelVistaPrevia, BorderLayout.CENTER);

        cardMm.add(lblTituloPlugin, BorderLayout.NORTH);
        cardMm.add(campos, BorderLayout.CENTER);
        cardMm.add(panelRecurso, BorderLayout.SOUTH);

        // Opciones
        ModernCard cardOpciones = new ModernCard(new BorderLayout(0, 10));
        JLabel lblOpc = new JLabel("Opciones A-D y Respuesta Correcta");
        lblOpc.setFont(UITheme.FONT_TITLE_SMALL); lblOpc.setForeground(UITheme.COLOR_PRIMARY);

        JPanel panelOps = new JPanel(new GridLayout(0, 2, 10, 6));
        panelOps.setOpaque(false);
        txtMmOpA = new JTextField(); UITheme.styleTextField(txtMmOpA);
        txtMmOpB = new JTextField(); UITheme.styleTextField(txtMmOpB);
        txtMmOpC = new JTextField(); UITheme.styleTextField(txtMmOpC);
        txtMmOpD = new JTextField(); UITheme.styleTextField(txtMmOpD);

        panelOps.add(new JLabel("Opción A:")); panelOps.add(txtMmOpA);
        panelOps.add(new JLabel("Opción B:")); panelOps.add(txtMmOpB);
        panelOps.add(new JLabel("Opción C:")); panelOps.add(txtMmOpC);
        panelOps.add(new JLabel("Opción D:")); panelOps.add(txtMmOpD);

        JPanel pResp = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pResp.setOpaque(false);
        comboMmRespuesta = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        UITheme.styleComboBox(comboMmRespuesta);
        pResp.add(new JLabel("Respuesta Correcta:"));
        pResp.add(comboMmRespuesta);

        JPanel wrapperOps = new JPanel(new BorderLayout(0, 8));
        wrapperOps.setOpaque(false);
        wrapperOps.add(panelOps, BorderLayout.CENTER);
        wrapperOps.add(pResp, BorderLayout.SOUTH);

        cardOpciones.add(lblOpc, BorderLayout.NORTH);
        cardOpciones.add(wrapperOps, BorderLayout.CENTER);

        // Metadatos
        ModernCard cardMeta = new ModernCard(new GridLayout(1, 3, 10, 0));
        txtMmCompetencia = new JTextField("Razonamiento cuantitativo"); UITheme.styleTextField(txtMmCompetencia);
        txtMmCategoria = new JTextField("Interpretación gráfica"); UITheme.styleTextField(txtMmCategoria);
        comboMmDificultad = new JComboBox<>(new String[]{"Fácil", "Medio", "Difícil"}); UITheme.styleComboBox(comboMmDificultad);

        JPanel pC = new JPanel(new BorderLayout(0, 4)); pC.setOpaque(false); pC.add(new JLabel("Competencia:"), BorderLayout.NORTH); pC.add(txtMmCompetencia);
        JPanel pCat = new JPanel(new BorderLayout(0, 4)); pCat.setOpaque(false); pCat.add(new JLabel("Categoría:"), BorderLayout.NORTH); pCat.add(txtMmCategoria);
        JPanel pD = new JPanel(new BorderLayout(0, 4)); pD.setOpaque(false); pD.add(new JLabel("Dificultad:"), BorderLayout.NORTH); pD.add(comboMmDificultad);
        cardMeta.add(pC); cardMeta.add(pCat); cardMeta.add(pD);

        contenedor.add(cardMm);
        contenedor.add(Box.createVerticalStrut(10));
        contenedor.add(cardOpciones);
        contenedor.add(Box.createVerticalStrut(10));
        contenedor.add(cardMeta);

        JScrollPane scroll = new JScrollPane(contenedor);
        scroll.setBorder(null); scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelAcciones.setOpaque(false);
        ModernButton btnGenerarMm = new ModernButton("⚡ Generar con Plugin Multimedia", ModernButton.Variant.SUCCESS);
        btnGenerarMm.addActionListener(e -> generarMultimedia());
        panelAcciones.add(btnGenerarMm);

        root.add(scroll, BorderLayout.CENTER);
        root.add(panelAcciones, BorderLayout.SOUTH);
        return root;
    }

    private void examinarArchivoMultimedia() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar recurso multimedia");
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            txtMmRutaArchivo.setText(selected.getAbsolutePath());
            panelVistaPrevia.cargarRecurso(selected.getAbsolutePath());
        }
    }

    private void generarMultimedia() {
        String titulo = txtMmTitulo.getText().trim();
        String enun = txtMmEnunciado.getText().trim();
        String ruta = txtMmRutaArchivo.getText().trim();
        String opA = txtMmOpA.getText().trim();
        String opB = txtMmOpB.getText().trim();
        String opC = txtMmOpC.getText().trim();
        String opD = txtMmOpD.getText().trim();
        String resp = (String) comboMmRespuesta.getSelectedItem();
        String comp = txtMmCompetencia.getText().trim();
        String cat = txtMmCategoria.getText().trim();
        String dif = (String) comboMmDificultad.getSelectedItem();

        if (titulo.isBlank() || enun.isBlank() || ruta.isBlank() || opA.isBlank() || opB.isBlank() || opC.isBlank() || opD.isBlank()) {
            JOptionPane.showMessageDialog(this, "Todos los campos, la ruta multimedia y las 4 opciones son requeridos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        QuestionRequest request = new QuestionRequest();
        request.setTitulo(titulo);
        request.setContenido(enun);
        request.setRecursoMultimedia(ruta);
        request.setOpciones(List.of(opA, opB, opC, opD));
        request.setRespuestaCorrecta(resp);
        request.setCompetencia(comp);
        request.setCategoria(cat);
        request.setNivelDificultad(dif);
        request.setAutorLogin(autorActual != null ? autorActual.getLogin() : "autor");

        procesarGeneracionPlugin("MULTIMEDIA", request, "Multimedia");
    }

    // =========================================================================
    // PROCESAMIENTO COMÚN CON EL CONTROLADOR, MICROKERNEL Y PIPELINE
    // =========================================================================
    private void procesarGeneracionPlugin(String tipoPlugin, QuestionRequest request, String nombreLegible) {
        try {
            Pregunta generada = controller.generarConPlugin(tipoPlugin, request);
            boolean guardada = controller.guardarPregunta(generada);

            if (guardada) {
                controller.enviarARevision(generada.getId());
                JOptionPane.showMessageDialog(this,
                        "¡Pregunta generada con éxito mediante el plugin '" + nombreLegible + "'!\n"
                                + "• ID Asignado: " + generada.getId() + "\n"
                                + "• Validada por el Pipeline de Tuberías/Filtros.\n"
                                + "• Estado actual: PENDIENTE DE REVISIÓN.",
                        "Generación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo guardar la pregunta generada en el banco.",
                        "Error al Guardar", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this,
                    "El Pipeline rechazó la pregunta generada:\n" + ex.getMessage(),
                    "Fallo de Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al generar con el plugin " + nombreLegible + ":\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
