package co.edu.unicauca.lisw2_t02_g03.domain;

import co.edu.unicauca.lisw2_t02_g03.infra.Observer;
import co.edu.unicauca.lisw2_t02_g03.infra.Subject;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Servicio de dominio equivalente a QuestionService en la guía.
 * Centraliza reglas de cambio de estado, estadísticas y notificaciones.
 */
public class PreguntaService implements Subject<EstadisticasPreguntas> {

    private final PreguntaRepository repository;
    private final List<Observer<EstadisticasPreguntas>> observadores = new CopyOnWriteArrayList<>();

    public PreguntaService(PreguntaRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("El repositorio de preguntas es obligatorio.");
        }
        this.repository = repository;
    }

    public List<Pregunta> listarPreguntas() {
        return repository.listar();
    }

    public Optional<Pregunta> buscarPregunta(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return repository.buscarPorId(id.trim());
    }

    public ResultadoCambioEstado cambiarEstado(String id, EstadoPregunta nuevoEstado) {
        if (id == null || id.isBlank() || nuevoEstado == null) {
            return ResultadoCambioEstado.ENTRADA_INVALIDA;
        }

        Optional<Pregunta> encontrada = repository.buscarPorId(id.trim());
        if (encontrada.isEmpty()) {
            return ResultadoCambioEstado.PREGUNTA_NO_ENCONTRADA;
        }

        Pregunta actual = encontrada.get();
        if (actual.getEstado() == nuevoEstado) {
            return ResultadoCambioEstado.SIN_CAMBIOS;
        }

        boolean actualizada = repository.actualizar(actual.conEstado(nuevoEstado));
        if (!actualizada) {
            return ResultadoCambioEstado.PREGUNTA_NO_ENCONTRADA;
        }

        notificarObservadores();
        return ResultadoCambioEstado.ACTUALIZADO;
    }

    public EstadisticasPreguntas obtenerEstadisticas() {
        Map<EstadoPregunta, Long> conteos = new EnumMap<>(EstadoPregunta.class);
        Map<EstadoPregunta, Double> porcentajes = new EnumMap<>(EstadoPregunta.class);

        for (EstadoPregunta estado : EstadoPregunta.values()) {
            conteos.put(estado, 0L);
        }

        List<Pregunta> preguntas = repository.listar();
        for (Pregunta pregunta : preguntas) {
            conteos.merge(pregunta.getEstado(), 1L, Long::sum);
        }

        long total = preguntas.size();
        for (EstadoPregunta estado : EstadoPregunta.values()) {
            double porcentaje = total == 0
                    ? 0.0
                    : conteos.get(estado) * 100.0 / total;
            porcentajes.put(estado, porcentaje);
        }

        return new EstadisticasPreguntas(conteos, porcentajes, total);
    }

    @Override
    public void registrarObservador(Observer<EstadisticasPreguntas> observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void retirarObservador(Observer<EstadisticasPreguntas> observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificarObservadores() {
        EstadisticasPreguntas estadisticas = obtenerEstadisticas();
        for (Observer<EstadisticasPreguntas> observador : observadores) {
            observador.actualizar(estadisticas);
        }
    }
}
