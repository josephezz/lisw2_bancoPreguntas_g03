package co.edu.unicauca.lisw2_t02_g03.plugins;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.OpcionesPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;

import java.util.List;
import java.util.UUID;

/**
 * Plugin para generar preguntas de selección múltiple con cuatro opciones A-D.
 */
public class MultipleChoiceQuestionPlugin implements QuestionPlugin {

    @Override
    public String getName() {
        return "Selección Múltiple";
    }

    @Override
    public boolean supports(String tipo) {
        return "SELECCION_MULTIPLE".equalsIgnoreCase(tipo);
    }

    @Override
    public Pregunta generate(QuestionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser null.");
        }

        List<String> opciones = request.getOpciones();
        if (opciones == null || opciones.size() != 4) {
            throw new IllegalArgumentException("Se requieren exactamente 4 opciones para selección múltiple.");
        }

        String id = "P-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return new Pregunta(
                id,
                request.getTitulo(),
                request.getContenido(),
                "SELECCION_MULTIPLE",
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
