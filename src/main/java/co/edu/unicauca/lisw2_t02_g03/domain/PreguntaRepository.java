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
}
