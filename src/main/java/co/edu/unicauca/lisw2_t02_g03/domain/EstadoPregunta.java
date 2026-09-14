package co.edu.unicauca.lisw2_t02_g03.domain;

/**
 * Estados exigidos por el alcance específico del Taller 04.
 */
public enum EstadoPregunta {
    BORRADOR("Borrador"),
    PENDIENTE_REVISION("Pendiente de revisión"),
    ELIMINADA("Eliminada");

    private final String etiqueta;

    EstadoPregunta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
