package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadisticasPreguntas;
import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.Pregunta;
import co.edu.unicauca.lisw2_t02_g03.domain.PreguntaService;
import co.edu.unicauca.lisw2_t02_g03.domain.ResultadoCambioEstado;
import co.edu.unicauca.lisw2_t02_g03.infra.Observer;

import java.util.List;
import java.util.Optional;

/**
 * Controlador MVC: coordina acciones de la vista y delega reglas al servicio de dominio.
 */
public class PreguntasController {

    private final PreguntaService service;

    public PreguntasController(PreguntaService service) {
        if (service == null) {
            throw new IllegalArgumentException("El servicio de preguntas es obligatorio.");
        }
        this.service = service;
    }

    public List<Pregunta> listarPreguntas() {
        return service.listarPreguntas();
    }

    public Optional<Pregunta> buscarPregunta(String id) {
        return service.buscarPregunta(id);
    }

    public ResultadoCambioEstado cambiarEstado(Pregunta seleccionada, EstadoPregunta nuevoEstado) {
        if (seleccionada == null) {
            return ResultadoCambioEstado.ENTRADA_INVALIDA;
        }
        return service.cambiarEstado(seleccionada.getId(), nuevoEstado);
    }

    public EstadisticasPreguntas obtenerEstadisticas() {
        return service.obtenerEstadisticas();
    }

    public void registrarObservador(Observer<EstadisticasPreguntas> observador) {
        service.registrarObservador(observador);
    }

    public void retirarObservador(Observer<EstadisticasPreguntas> observador) {
        service.retirarObservador(observador);
    }

    public void refrescarVistasObservadoras() {
        service.notificarObservadores();
    }
}
