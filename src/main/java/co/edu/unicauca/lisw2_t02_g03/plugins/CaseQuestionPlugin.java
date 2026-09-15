package co.edu.unicauca.lisw2_t02_g03.plugins;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.OpcionesPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;

import java.util.List;
import java.util.UUID;

/**
 * Plugin para generar preguntas basadas en análisis de casos.
 * <p>
 * Una pregunta tipo CASO presenta un escenario o situación que el estudiante
 * debe analizar antes de seleccionar la respuesta adecuada.
 */
public class CaseQuestionPlugin implements QuestionPlugin {

    @Override
    public String getName() {
        return "Análisis de Caso";
    }

    @Override
    public boolean supports(String tipo) {
        return "CASO".equalsIgnoreCase(tipo);
    }

    @Override
    public Pregunta generate(QuestionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser null.");
        }

        List<String> opciones = request.getOpciones();
        if (opciones == null || opciones.size() != 4) {
            throw new IllegalArgumentException("Se requieren exactamente 4 opciones para pregunta de caso.");
        }

        String id = "CASO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return new Pregunta(
                id,
                request.getTitulo(),
                request.getContenido(),
                "CASO",
                new OpcionesPregunta(opciones),
                request.getRespuestaCorrecta(),
                EstadoPregunta.BORRADOR,
                request.getCompetencia(),
                request.getCategoria(),
                request.getNivelDificultad(),
                null,
                request.getAutorLogin(),
                null);
    }
}
