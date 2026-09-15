package co.edu.unicauca.lisw2_t02_g03.plugins;

import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;

/**
 * Contrato común que todo plugin de preguntas debe implementar.
 * El núcleo del microkernel interactúa con los plugins exclusivamente a través de esta interfaz.
 */
public interface QuestionPlugin {

    /**
     * Nombre legible del plugin.
     */
    String getName();

    /**
     * Indica si este plugin soporta un tipo de pregunta específico.
     *
     * @param tipo tipo de pregunta (e.g., "SELECCION_MULTIPLE", "CASO", "MULTIMEDIA").
     * @return {@code true} si el plugin puede generar ese tipo.
     */
    boolean supports(String tipo);

    /**
     * Genera una pregunta a partir de los datos proporcionados en la solicitud.
     *
     * @param request datos de entrada para generar la pregunta.
     * @return pregunta generada, nunca {@code null}.
     */
    Pregunta generate(QuestionRequest request);
}
