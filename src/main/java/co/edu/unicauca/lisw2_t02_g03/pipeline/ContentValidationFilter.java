package co.edu.unicauca.lisw2_t02_g03.pipeline;

import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;

/**
 * Filtro 1: Validación de contenido.
 * Verifica que el título y enunciado no estén vacíos y cumplan longitud mínima.
 */
public class ContentValidationFilter implements QuestionFilter {

    private static final int LONGITUD_MINIMA_TITULO = 5;
    private static final int LONGITUD_MINIMA_ENUNCIADO = 10;

    @Override
    public Pregunta process(Pregunta pregunta) {
        String nombre = pregunta.getNombre();
        if (nombre == null || nombre.isBlank()) {
            throw new ValidationException("El título de la pregunta no puede estar vacío.");
        }
        if (nombre.length() < LONGITUD_MINIMA_TITULO) {
            throw new ValidationException(
                    "El título debe tener al menos " + LONGITUD_MINIMA_TITULO + " caracteres.");
        }

        String enunciado = pregunta.getEnunciado();
        if (enunciado == null || enunciado.isBlank()) {
            throw new ValidationException("El enunciado de la pregunta no puede estar vacío.");
        }
        if (enunciado.length() < LONGITUD_MINIMA_ENUNCIADO) {
            throw new ValidationException(
                    "El enunciado debe tener al menos " + LONGITUD_MINIMA_ENUNCIADO + " caracteres.");
        }

        return pregunta;
    }
}
