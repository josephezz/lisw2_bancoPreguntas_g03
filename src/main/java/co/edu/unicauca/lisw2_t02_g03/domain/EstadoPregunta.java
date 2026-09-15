package co.edu.unicauca.lisw2_t02_g03.domain;

/**
 * Estados del ciclo de vida de una pregunta en el Banco Saber Pro.
 */
public enum EstadoPregunta {
    BORRADOR("Borrador"),
    PENDIENTE_REVISION("Pendiente de revisión"),
    APROBADA("Aprobada"),
    RECHAZADA("Rechazada"),
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
