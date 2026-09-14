package co.edu.unicauca.lisw2_t02_g03.infra;

/**
 * Contrato Subject genérico para registrar, retirar y notificar observadores.
 */
public interface Subject<T> {

    void registrarObservador(Observer<T> observador);

    void retirarObservador(Observer<T> observador);

    void notificarObservadores();
}
