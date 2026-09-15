package co.edu.unicauca.lisw2_t02_g03.pipeline;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.OpcionesPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionPipelineTest {

    private ContentValidationFilter contentFilter;
    private OptionsValidationFilter optionsFilter;
    private ClassificationFilter classificationFilter;
    private CorrectAnswerValidationFilter answerFilter;
    private QuestionPipeline pipeline;

    @BeforeEach
    void setUp() {
        contentFilter = new ContentValidationFilter();
        optionsFilter = new OptionsValidationFilter();
        classificationFilter = new ClassificationFilter();
        answerFilter = new CorrectAnswerValidationFilter();

        pipeline = new QuestionPipeline()
                .agregarFiltro(contentFilter)
                .agregarFiltro(optionsFilter)
                .agregarFiltro(classificationFilter)
                .agregarFiltro(answerFilter);
    }

    private Pregunta crearPreguntaValida() {
        return new Pregunta(
                "P-101",
                "Comprensión Lectora Texto 1",
                "Lea el siguiente texto atentamente y responda la pregunta a continuación:",
                "SELECCION_MULTIPLE",
                OpcionesPregunta.de("Opción alfa", "Opción beta", "Opción gamma", "Opción delta"),
                "A",
                EstadoPregunta.BORRADOR,
                "Lectura Crítica",
                "Humanidades",
                "Medio",
                null,
                "autor1",
                null);
    }

    // ==========================================
    // Tests de Filtro 1: ContentValidationFilter
    // ==========================================

    @Test
    @DisplayName("ContentFilter: aprueba contenido válido")
    void testContentFilterValido() {
        Pregunta p = crearPreguntaValida();
        assertDoesNotThrow(() -> contentFilter.process(p));
    }

    @Test
    @DisplayName("ContentFilter: rechaza título nulo, vacío o muy corto")
    void testContentFilterTituloInvalido() {
        Pregunta p1 = crearPreguntaValida().conNombre("");
        assertThrows(ValidationException.class, () -> contentFilter.process(p1));

        Pregunta p2 = crearPreguntaValida().conNombre("Abc"); // < 5 chars
        assertThrows(ValidationException.class, () -> contentFilter.process(p2));

        Pregunta p3 = crearPreguntaValida().conNombre(null);
        assertThrows(ValidationException.class, () -> contentFilter.process(p3));
    }

    @Test
    @DisplayName("ContentFilter: rechaza enunciado nulo, vacío o muy corto")
    void testContentFilterEnunciadoInvalido() {
        Pregunta p1 = crearPreguntaValida().conEnunciado("");
        assertThrows(ValidationException.class, () -> contentFilter.process(p1));

        Pregunta p2 = crearPreguntaValida().conEnunciado("Corto"); // < 10 chars
        assertThrows(ValidationException.class, () -> contentFilter.process(p2));

        Pregunta p3 = crearPreguntaValida().conEnunciado(null);
        assertThrows(ValidationException.class, () -> contentFilter.process(p3));
    }

    // ==========================================
    // Tests de Filtro 2: OptionsValidationFilter
    // ==========================================

    @Test
    @DisplayName("OptionsFilter: aprueba 4 opciones válidas y distintas")
    void testOptionsFilterValido() {
        Pregunta p = crearPreguntaValida();
        assertDoesNotThrow(() -> optionsFilter.process(p));
    }

    @Test
    @DisplayName("OptionsFilter: rechaza opciones nulas")
    void testOptionsFilterNulo() {
        Pregunta p = new Pregunta(
                "P-1", "Título Válido", "Enunciado suficientemente largo",
                "SELECCION_MULTIPLE", null, "A", EstadoPregunta.BORRADOR);
        assertThrows(ValidationException.class, () -> optionsFilter.process(p));
    }

    @Test
    @DisplayName("OptionsFilter: valida que opciones no sean nulas")
    void testOptionsFilterRequiereOpciones() {
        Pregunta p = new Pregunta(
                "P-1", "Título Válido", "Enunciado suficientemente largo",
                "SELECCION_MULTIPLE", null, "A", EstadoPregunta.BORRADOR);
        ValidationException ex = assertThrows(ValidationException.class, () -> optionsFilter.process(p));
        assertTrue(ex.getMessage().contains("opciones"));
    }

    @Test
    @DisplayName("OptionsFilter: rechaza opciones duplicadas (case-insensitive)")
    void testOptionsFilterOpcionesDuplicadas() {
        Pregunta p = new Pregunta(
                "P-1", "Título Válido", "Enunciado suficientemente largo",
                "SELECCION_MULTIPLE",
                OpcionesPregunta.de("Opción A", "opción a", "Opción C", "Opción D"),
                "A", EstadoPregunta.BORRADOR);
        assertThrows(ValidationException.class, () -> optionsFilter.process(p));
    }

    // ==========================================
    // Tests de Filtro 3: ClassificationFilter
    // ==========================================

    @Test
    @DisplayName("ClassificationFilter: aprueba competencia, categoría y dificultad presentes")
    void testClassificationFilterValido() {
        Pregunta p = crearPreguntaValida();
        assertDoesNotThrow(() -> classificationFilter.process(p));
    }

    @Test
    @DisplayName("ClassificationFilter: rechaza si falta competencia")
    void testClassificationFilterSinCompetencia() {
        Pregunta p = new Pregunta(
                "P-1", "Título Válido", "Enunciado suficientemente largo",
                "SELECCION_MULTIPLE",
                OpcionesPregunta.de("A", "B", "C", "D"),
                "A", EstadoPregunta.BORRADOR,
                null, "Categoría", "Fácil", null, "autor", null);
        assertThrows(ValidationException.class, () -> classificationFilter.process(p));
    }

    @Test
    @DisplayName("ClassificationFilter: rechaza si falta categoría")
    void testClassificationFilterSinCategoria() {
        Pregunta p = new Pregunta(
                "P-1", "Título Válido", "Enunciado suficientemente largo",
                "SELECCION_MULTIPLE",
                OpcionesPregunta.de("A", "B", "C", "D"),
                "A", EstadoPregunta.BORRADOR,
                "Competencia", "", "Fácil", null, "autor", null);
        assertThrows(ValidationException.class, () -> classificationFilter.process(p));
    }

    @Test
    @DisplayName("ClassificationFilter: rechaza si falta dificultad")
    void testClassificationFilterSinDificultad() {
        Pregunta p = new Pregunta(
                "P-1", "Título Válido", "Enunciado suficientemente largo",
                "SELECCION_MULTIPLE",
                OpcionesPregunta.de("A", "B", "C", "D"),
                "A", EstadoPregunta.BORRADOR,
                "Competencia", "Categoría", "   ", null, "autor", null);
        assertThrows(ValidationException.class, () -> classificationFilter.process(p));
    }

    // ==========================================
    // Tests de Filtro 4: CorrectAnswerValidationFilter
    // ==========================================

    @Test
    @DisplayName("CorrectAnswerFilter: aprueba respuesta correcta A-D")
    void testCorrectAnswerFilterValido() {
        Pregunta p = crearPreguntaValida();
        assertDoesNotThrow(() -> answerFilter.process(p));
    }

    @Test
    @DisplayName("CorrectAnswerFilter: rechaza respuesta nula o vacía")
    void testCorrectAnswerFilterVacia() {
        Pregunta p1 = new Pregunta(
                "P-1", "Título Válido", "Enunciado suficientemente largo",
                "SELECCION_MULTIPLE",
                OpcionesPregunta.de("A", "B", "C", "D"),
                null, EstadoPregunta.BORRADOR);
        assertThrows(ValidationException.class, () -> answerFilter.process(p1));

        Pregunta p2 = new Pregunta(
                "P-1", "Título Válido", "Enunciado suficientemente largo",
                "SELECCION_MULTIPLE",
                OpcionesPregunta.de("A", "B", "C", "D"),
                "  ", EstadoPregunta.BORRADOR);
        assertThrows(ValidationException.class, () -> answerFilter.process(p2));
    }

    @Test
    @DisplayName("CorrectAnswerFilter: rechaza respuesta que no corresponde a A, B, C o D")
    void testCorrectAnswerFilterInexistente() {
        Pregunta p = new Pregunta(
                "P-1", "Título Válido", "Enunciado suficientemente largo",
                "SELECCION_MULTIPLE",
                OpcionesPregunta.de("A", "B", "C", "D"),
                "E", EstadoPregunta.BORRADOR);
        assertThrows(ValidationException.class, () -> answerFilter.process(p));
    }

    // ==========================================
    // Tests del Pipeline completo
    // ==========================================

    @Test
    @DisplayName("QuestionPipeline: ejecución completa exitosa en secuencia")
    void testPipelineCompletoExitoso() {
        assertEquals(4, pipeline.size());
        Pregunta p = crearPreguntaValida();

        Pregunta resultado = pipeline.ejecutar(p);
        assertNotNull(resultado);
        assertEquals(p.getId(), resultado.getId());
    }

    @Test
    @DisplayName("QuestionPipeline: falla si la pregunta es null")
    void testPipelinePreguntaNull() {
        assertThrows(ValidationException.class, () -> pipeline.ejecutar(null));
    }

    @Test
    @DisplayName("QuestionPipeline: se detiene en el primer filtro que falla")
    void testPipelineFallaTemprano() {
        // Título inválido: falla en ContentValidationFilter
        Pregunta p = new Pregunta(
                "P-1", "", "Enunciado suficientemente largo",
                "SELECCION_MULTIPLE",
                OpcionesPregunta.de("A", "B", "C", "D"),
                "A", EstadoPregunta.BORRADOR);

        ValidationException ex = assertThrows(ValidationException.class, () -> pipeline.ejecutar(p));
        assertTrue(ex.getMessage().contains("título"), "El error debe ser del filtro de contenido");
    }
}
