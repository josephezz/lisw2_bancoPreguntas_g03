package co.edu.unicauca.lisw2_t02_g03.plugins;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionPluginsTest {

    private MultipleChoiceQuestionPlugin multipleChoicePlugin;
    private CaseQuestionPlugin casePlugin;
    private MultimediaQuestionPlugin multimediaPlugin;

    @BeforeEach
    void setUp() {
        multipleChoicePlugin = new MultipleChoiceQuestionPlugin();
        casePlugin = new CaseQuestionPlugin();
        multimediaPlugin = new MultimediaQuestionPlugin();
    }

    // ==========================================
    // MultipleChoiceQuestionPlugin Tests
    // ==========================================

    @Test
    @DisplayName("MultipleChoicePlugin: soporte y nombre correcto")
    void testMultipleChoiceSoporte() {
        assertEquals("Selección Múltiple", multipleChoicePlugin.getName());
        assertTrue(multipleChoicePlugin.supports("SELECCION_MULTIPLE"));
        assertTrue(multipleChoicePlugin.supports("seleccion_multiple"));
        assertFalse(multipleChoicePlugin.supports("CASO"));
    }

    @Test
    @DisplayName("MultipleChoicePlugin: genera pregunta con 4 opciones")
    void testMultipleChoiceGeneracionExitosa() {
        QuestionRequest request = new QuestionRequest();
        request.setTitulo("Capital de Colombia");
        request.setContenido("¿Cuál es la capital de Colombia?");
        request.setOpciones(List.of("Bogotá", "Medellín", "Cali", "Barranquilla"));
        request.setRespuestaCorrecta("A");
        request.setCompetencia("Ciencias Sociales");
        request.setCategoria("Geografía");
        request.setNivelDificultad("Fácil");
        request.setAutorLogin("profesor1");

        Pregunta pregunta = multipleChoicePlugin.generate(request);

        assertNotNull(pregunta);
        assertTrue(pregunta.getId().startsWith("P-"));
        assertEquals("Capital de Colombia", pregunta.getNombre());
        assertEquals("¿Cuál es la capital de Colombia?", pregunta.getEnunciado());
        assertEquals("SELECCION_MULTIPLE", pregunta.getTipo());
        assertEquals("A", pregunta.getRespuestaCorrecta());
        assertEquals(EstadoPregunta.BORRADOR, pregunta.getEstado());
        assertEquals("Ciencias Sociales", pregunta.getCompetencia());
        assertEquals("Geografía", pregunta.getCategoria());
        assertEquals("Fácil", pregunta.getNivelDificultad());
        assertEquals("profesor1", pregunta.getAutorLogin());
        assertNull(pregunta.getRecursoMultimedia());
    }

    @Test
    @DisplayName("MultipleChoicePlugin: falla con menos o más de 4 opciones")
    void testMultipleChoiceOpcionesInvalidas() {
        QuestionRequest request = new QuestionRequest();
        request.setTitulo("Test");
        request.setContenido("Contenido de prueba");
        request.setOpciones(List.of("A", "B", "C")); // solo 3
        request.setRespuestaCorrecta("A");

        assertThrows(IllegalArgumentException.class, () ->
                multipleChoicePlugin.generate(request));

        request.setOpciones(null);
        assertThrows(IllegalArgumentException.class, () ->
                multipleChoicePlugin.generate(request));
    }

    // ==========================================
    // CaseQuestionPlugin Tests
    // ==========================================

    @Test
    @DisplayName("CaseQuestionPlugin: soporte y nombre correcto")
    void testCasePluginSoporte() {
        assertEquals("Análisis de Caso", casePlugin.getName());
        assertTrue(casePlugin.supports("CASO"));
        assertTrue(casePlugin.supports("caso"));
        assertFalse(casePlugin.supports("SELECCION_MULTIPLE"));
    }

    @Test
    @DisplayName("CaseQuestionPlugin: genera pregunta tipo CASO correctamente")
    void testCasePluginGeneracionExitosa() {
        QuestionRequest request = new QuestionRequest();
        request.setTitulo("Caso Clínico Paciente X");
        request.setContenido("Un paciente de 45 años presenta síntomas febriles y dolor lumbar...");
        request.setOpciones(List.of(
                "Administrar antibiótico de amplio espectro",
                "Solicitar urocultivo y esperar resultados",
                "Realizar ecografía renal urgente",
                "Dar de alta con analgésicos"));
        request.setRespuestaCorrecta("B");
        request.setCompetencia("Toma de Decisiones");
        request.setCategoria("Medicina Interna");
        request.setNivelDificultad("Difícil");
        request.setAutorLogin("dr_medina");

        Pregunta pregunta = casePlugin.generate(request);

        assertNotNull(pregunta);
        assertTrue(pregunta.getId().startsWith("CASO-"));
        assertEquals("CASO", pregunta.getTipo());
        assertEquals("Caso Clínico Paciente X", pregunta.getNombre());
        assertEquals(4, pregunta.getOpciones().comoLista().size());
        assertEquals("B", pregunta.getRespuestaCorrecta());
        assertEquals("dr_medina", pregunta.getAutorLogin());
    }

    // ==========================================
    // MultimediaQuestionPlugin Tests
    // ==========================================

    @Test
    @DisplayName("MultimediaPlugin: soporte y nombre correcto")
    void testMultimediaPluginSoporte() {
        assertEquals("Multimedia", multimediaPlugin.getName());
        assertTrue(multimediaPlugin.supports("MULTIMEDIA"));
        assertTrue(multimediaPlugin.supports("multimedia"));
        assertFalse(multimediaPlugin.supports("CASO"));
    }

    @Test
    @DisplayName("MultimediaPlugin: genera pregunta con recurso multimedia")
    void testMultimediaPluginGeneracionExitosa() {
        QuestionRequest request = new QuestionRequest();
        request.setTitulo("Análisis de Gráfica Poblacional");
        request.setContenido("Observe la pirámide poblacional adjunta y determine la tendencia:");
        request.setOpciones(List.of(
                "Población en envejecimiento",
                "Crecimiento acelerado",
                "Mortalidad infantil alta",
                "Estabilidad demográfica"));
        request.setRespuestaCorrecta("A");
        request.setCompetencia("Lectura Crítica");
        request.setCategoria("Demografía");
        request.setNivelDificultad("Medio");
        request.setRecursoMultimedia("/imagenes/piramide_2025.png");
        request.setAutorLogin("prof_sociales");

        Pregunta pregunta = multimediaPlugin.generate(request);

        assertNotNull(pregunta);
        assertTrue(pregunta.getId().startsWith("MM-"));
        assertEquals("MULTIMEDIA", pregunta.getTipo());
        assertEquals("/imagenes/piramide_2025.png", pregunta.getRecursoMultimedia());
        assertEquals("A", pregunta.getRespuestaCorrecta());
    }

    @Test
    @DisplayName("MultimediaPlugin: falla sin recurso multimedia")
    void testMultimediaPluginSinRecurso() {
        QuestionRequest request = new QuestionRequest();
        request.setTitulo("Test Multimedia");
        request.setContenido("Observe la siguiente imagen:");
        request.setOpciones(List.of("Op1", "Op2", "Op3", "Op4"));
        request.setRespuestaCorrecta("A");
        request.setRecursoMultimedia(null);

        assertThrows(IllegalArgumentException.class, () ->
                multimediaPlugin.generate(request));

        request.setRecursoMultimedia("   ");
        assertThrows(IllegalArgumentException.class, () ->
                multimediaPlugin.generate(request));
    }

    @Test
    @DisplayName("Todos los plugins validan que el request no sea null")
    void testRequestNull() {
        assertThrows(IllegalArgumentException.class, () -> multipleChoicePlugin.generate(null));
        assertThrows(IllegalArgumentException.class, () -> casePlugin.generate(null));
        assertThrows(IllegalArgumentException.class, () -> multimediaPlugin.generate(null));
    }
}
