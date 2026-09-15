package co.edu.unicauca.lisw2_t02_g03.pipeline;

/**
 * Excepción lanzada cuando una pregunta no pasa un filtro de validación en el pipeline.
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
