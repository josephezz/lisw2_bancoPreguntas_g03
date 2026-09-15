package co.edu.unicauca.lisw2_t02_g03.pipeline;

import co.edu.unicauca.lisw2_t02_g03.domain.OpcionesPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Filtro 2: Validación de opciones de respuesta.
 * Verifica cantidad correcta, no vacías, no duplicadas.
 */
public class OptionsValidationFilter implements QuestionFilter {

    @Override
    public Pregunta process(Pregunta pregunta) {
        OpcionesPregunta opciones = pregunta.getOpciones();
        if (opciones == null) {
            throw new ValidationException("Las opciones de respuesta son obligatorias.");
        }

        List<String> lista = opciones.comoLista();
        if (lista.size() != 4) {
            throw new ValidationException("Se requieren exactamente 4 opciones de respuesta.");
        }

        for (int i = 0; i < lista.size(); i++) {
            String opcion = lista.get(i);
            if (opcion == null || opcion.isBlank()) {
                throw new ValidationException("La opción " + (char) ('A' + i) + " no puede estar vacía.");
            }
        }

        // Verificar duplicados
        Set<String> unicas = new HashSet<>();
        for (String opcion : lista) {
            if (!unicas.add(opcion.toLowerCase())) {
                throw new ValidationException("Las opciones de respuesta no pueden estar duplicadas: " + opcion);
            }
        }

        return pregunta;
    }
}
