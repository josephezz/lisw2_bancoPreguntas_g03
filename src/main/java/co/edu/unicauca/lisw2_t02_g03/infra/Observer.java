package co.edu.unicauca.lisw2_t02_g03.infra;

/**
 * Contrato Observer propio del proyecto. No utiliza java.util.Observer.
 */
@FunctionalInterface
public interface Observer<T> {

    void actualizar(T estado);
}
