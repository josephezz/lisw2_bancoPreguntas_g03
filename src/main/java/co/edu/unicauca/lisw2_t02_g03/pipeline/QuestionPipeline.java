package co.edu.unicauca.lisw2_t02_g03.pipeline;

import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;

import java.util.ArrayList;
import java.util.List;

/**
 * Pipeline de validación que ejecuta filtros en secuencia sobre una pregunta.
 * <p>
 * Flujo: Pregunta → ContentValidation → OptionsValidation → Classification → CorrectAnswer → Pregunta validada
 */
public class QuestionPipeline {

    private final List<QuestionFilter> filtros = new ArrayList<>();

    /**
     * Agrega un filtro al final del pipeline.
     */
    public QuestionPipeline agregarFiltro(QuestionFilter filtro) {
        if (filtro == null) {
            throw new IllegalArgumentException("El filtro no puede ser null.");
        }
        filtros.add(filtro);
        return this;
    }

    /**
     * Ejecuta todos los filtros en secuencia sobre la pregunta.
     *
     * @param pregunta pregunta a validar.
     * @return pregunta validada (la misma instancia si todos los filtros pasan).
     * @throws ValidationException si algún filtro detecta un error.
     */
    public Pregunta ejecutar(Pregunta pregunta) {
        if (pregunta == null) {
            throw new ValidationException("La pregunta no puede ser null.");
        }

        Pregunta resultado = pregunta;
        for (QuestionFilter filtro : filtros) {
            resultado = filtro.process(resultado);
        }
        return resultado;
    }

    /**
     * Cantidad de filtros en el pipeline.
     */
    public int size() {
        return filtros.size();
    }
}
