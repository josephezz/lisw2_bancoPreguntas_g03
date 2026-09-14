package co.edu.unicauca.lisw2_t02_g03.access;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.OpcionesPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.PreguntaRepository;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementación en memoria equivalente a QuestionImplRepository de la guía.
 */
public class PreguntaImplRepository implements PreguntaRepository {

    private final Map<String, Pregunta> preguntas = new LinkedHashMap<>();

    public PreguntaImplRepository() {
        this(crearBancoInicial());
    }

    public PreguntaImplRepository(Collection<Pregunta> preguntasIniciales) {
        if (preguntasIniciales == null) {
            throw new IllegalArgumentException("La colección inicial no puede ser null.");
        }

        for (Pregunta pregunta : preguntasIniciales) {
            if (pregunta == null) {
                throw new IllegalArgumentException("El banco no puede contener preguntas nulas.");
            }
            if (preguntas.putIfAbsent(pregunta.getId(), pregunta) != null) {
                throw new IllegalArgumentException("No puede haber ids de pregunta duplicados.");
            }
        }
    }

    @Override
    public List<Pregunta> listar() {
        return List.copyOf(preguntas.values());
    }

    @Override
    public Optional<Pregunta> buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(preguntas.get(id.trim()));
    }

    @Override
    public boolean actualizar(Pregunta pregunta) {
        if (pregunta == null || !preguntas.containsKey(pregunta.getId())) {
            return false;
        }
        preguntas.put(pregunta.getId(), pregunta);
        return true;
    }

    private static List<Pregunta> crearBancoInicial() {
        return List.of(
                new Pregunta(
                        "P-001",
                        "Lectura crítica - evidencia",
                        "Un texto afirma que una medida pública mejoró un indicador porque este aumentó después de aplicarla. "
                                + "¿Qué dato adicional permitiría evaluar mejor esa afirmación?",
                        OpcionesPregunta.de(
                                "El color usado en el informe.",
                                "La evolución del indicador antes de la medida y un punto de comparación pertinente.",
                                "La cantidad de páginas del documento.",
                                "La profesión de quien redactó el informe."),
                        "B",
                        EstadoPregunta.BORRADOR),
                new Pregunta(
                        "P-002",
                        "Razonamiento cuantitativo - porcentajes",
                        "En un grupo de 120 estudiantes, el 30 % respondió correctamente una pregunta. "
                                + "¿Cuántos estudiantes respondieron correctamente?",
                        OpcionesPregunta.de("24", "30", "36", "40"),
                        "C",
                        EstadoPregunta.PENDIENTE_REVISION),
                new Pregunta(
                        "P-003",
                        "Competencias ciudadanas - deliberación",
                        "Dos grupos estudiantiles discrepan sobre el uso de un espacio común. "
                                + "¿Cuál acción favorece mejor una solución democrática?",
                        OpcionesPregunta.de(
                                "Imponer la decisión del grupo más numeroso sin escuchar al otro.",
                                "Cancelar indefinidamente cualquier decisión sobre el espacio.",
                                "Escuchar las razones de ambos grupos y acordar criterios comunes de uso.",
                                "Permitir que solo decida quien presentó primero la propuesta."),
                        "C",
                        EstadoPregunta.BORRADOR),
                new Pregunta(
                        "P-004",
                        "Inglés - uso verbal",
                        "Choose the option that correctly completes the sentence: The students ___ preparing for the exam.",
                        OpcionesPregunta.de("is", "are", "am", "be"),
                        "B",
                        EstadoPregunta.PENDIENTE_REVISION),
                new Pregunta(
                        "P-005",
                        "Comunicación escrita - coherencia",
                        "¿Cuál estrategia mejora principalmente la coherencia de un texto argumentativo?",
                        OpcionesPregunta.de(
                                "Agregar ideas sin relación para aumentar la extensión.",
                                "Organizar las razones alrededor de una tesis y usar conectores pertinentes.",
                                "Cambiar de tema en cada párrafo.",
                                "Eliminar todos los signos de puntuación."),
                        "B",
                        EstadoPregunta.BORRADOR),
                new Pregunta(
                        "P-006",
                        "Razonamiento cuantitativo - interpretación",
                        "Una gráfica reporta valores 20, 25 y 30 para tres periodos consecutivos. "
                                + "¿Cuál es el aumento absoluto entre el primer y el tercer periodo?",
                        OpcionesPregunta.de("5", "10", "25", "50"),
                        "B",
                        EstadoPregunta.ELIMINADA)
        );
    }
}
