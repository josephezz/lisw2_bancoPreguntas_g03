package co.edu.unicauca.lisw2_t02_g03.pipeline;

import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;

/**
 * Filtro 4: Validación de respuesta correcta.
 * Verifica existencia de respuesta correcta y coherencia con las opciones.
 */
public class CorrectAnswerValidationFilter implements QuestionFilter {

    @Override
    public Pregunta process(Pregunta pregunta) {
        String respuesta = pregunta.getRespuestaCorrecta();
        if (respuesta == null || respuesta.isBlank()) {
            throw new ValidationException("La respuesta correcta es obligatoria.");
        }

        if (pregunta.getOpciones() == null) {
            throw new ValidationException("No se pueden validar respuestas sin opciones.");
        }

        if (!pregunta.getOpciones().contieneEtiqueta(respuesta)) {
            throw new ValidationException(
                    "La respuesta correcta '" + respuesta + "' no corresponde a una opción válida (A-D).");
        }

        return pregunta;
    }
}
