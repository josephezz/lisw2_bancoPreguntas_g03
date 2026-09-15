package co.edu.unicauca.lisw2_t02_g03.plugins;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.OpcionesPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;

import java.util.List;
import java.util.UUID;

/**
 * Plugin para generar preguntas con recursos multimedia (imagen, audio, video).
 * <p>
 * El recurso multimedia se referencia mediante una ruta o URL almacenada
 * en el campo {@code recursoMultimedia} de la pregunta.
 */
public class MultimediaQuestionPlugin implements QuestionPlugin {

    @Override
    public String getName() {
        return "Multimedia";
    }

    @Override
    public boolean supports(String tipo) {
        return "MULTIMEDIA".equalsIgnoreCase(tipo);
    }

    @Override
    public Pregunta generate(QuestionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud no puede ser null.");
        }

        List<String> opciones = request.getOpciones();
        if (opciones == null || opciones.size() != 4) {
            throw new IllegalArgumentException("Se requieren exactamente 4 opciones para pregunta multimedia.");
        }

        if (request.getRecursoMultimedia() == null || request.getRecursoMultimedia().isBlank()) {
            throw new IllegalArgumentException("Se requiere una referencia al recurso multimedia.");
        }

        String id = "MM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return new Pregunta(
                id,
                request.getTitulo(),
                request.getContenido(),
                "MULTIMEDIA",
                new OpcionesPregunta(opciones),
                request.getRespuestaCorrecta(),
                EstadoPregunta.BORRADOR,
                request.getCompetencia(),
                request.getCategoria(),
                request.getNivelDificultad(),
                request.getRecursoMultimedia(),
                request.getAutorLogin(),
                null);
    }
}
