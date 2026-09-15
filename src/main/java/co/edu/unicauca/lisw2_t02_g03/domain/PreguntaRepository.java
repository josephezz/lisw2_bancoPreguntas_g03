package co.edu.unicauca.lisw2_t02_g03.domain;

import java.util.List;
import java.util.Optional;

/**
 * Contrato del repositorio de preguntas. La capa de dominio no conoce la implementación concreta.
 */
public interface PreguntaRepository {

    List<Pregunta> listar();

    Optional<Pregunta> buscarPorId(String id);

    boolean actualizar(Pregunta pregunta);

    /**
     * Almacena una nueva pregunta en el banco.
     *
     * @param pregunta pregunta a guardar.
     * @return {@code true} si se almacenó correctamente.
     */
    boolean guardar(Pregunta pregunta);

    /**
     * Lista preguntas filtradas por estado.
     */
    List<Pregunta> listarPorEstado(EstadoPregunta estado);

    /**
     * Lista preguntas creadas por un autor específico.
     */
    List<Pregunta> listarPorAutor(String autorLogin);
}
