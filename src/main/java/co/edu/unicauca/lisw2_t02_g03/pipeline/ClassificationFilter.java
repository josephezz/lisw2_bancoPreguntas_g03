package co.edu.unicauca.lisw2_t02_g03.pipeline;

import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;

/**
 * Filtro 3: Validación de clasificación.
 * Verifica que la pregunta tenga competencia, categoría y nivel de dificultad asignados.
 * <p>
 * Nota: estos campos son opcionales en la entidad Pregunta, pero el pipeline
 * los exige antes de enviar a revisión para garantizar preguntas bien clasificadas.
 */
public class ClassificationFilter implements QuestionFilter {

    @Override
    public Pregunta process(Pregunta pregunta) {
        if (pregunta.getCompetencia() == null || pregunta.getCompetencia().isBlank()) {
            throw new ValidationException("La competencia es obligatoria para enviar a revisión.");
        }

        if (pregunta.getCategoria() == null || pregunta.getCategoria().isBlank()) {
            throw new ValidationException("La categoría es obligatoria para enviar a revisión.");
        }

        if (pregunta.getNivelDificultad() == null || pregunta.getNivelDificultad().isBlank()) {
            throw new ValidationException("El nivel de dificultad es obligatorio para enviar a revisión.");
        }

        return pregunta;
    }
}
