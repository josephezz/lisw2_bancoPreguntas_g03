package co.edu.unicauca.lisw2_t02_g03.pipeline;

import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;

/**
 * Contrato del patrón Tuberías/Filtros para validación de preguntas.
 * <p>
 * Cada filtro recibe una pregunta, la valida y la retorna sin modificaciones.
 * Si la validación falla, lanza {@link ValidationException}.
 */
public interface QuestionFilter {

    /**
     * Procesa/valida una pregunta.
     *
     * @param pregunta pregunta a procesar.
     * @return la misma pregunta si pasa la validación.
     * @throws ValidationException si la pregunta no cumple con los criterios del filtro.
     */
    Pregunta process(Pregunta pregunta);
}
