package co.edu.unicauca.lisw2_t02_g03.domain;

import java.util.Locale;
import java.util.Objects;

/**
 * Entidad de dominio equivalente a Question en el esquema del Taller 04.
 */
public final class Pregunta {

    private final String id;
    private final String nombre;
    private final String enunciado;
    private final OpcionesPregunta opciones;
    private final String respuestaCorrecta;
    private final EstadoPregunta estado;

    public Pregunta(
            String id,
            String nombre,
            String enunciado,
            OpcionesPregunta opciones,
            String respuestaCorrecta,
            EstadoPregunta estado) {

        this.id = requerirTexto(id, "El id de la pregunta es obligatorio.");
        this.nombre = requerirTexto(nombre, "El nombre de la pregunta es obligatorio.");
        this.enunciado = requerirTexto(enunciado, "El enunciado de la pregunta es obligatorio.");
        this.opciones = Objects.requireNonNull(opciones, "Las opciones son obligatorias.");
        this.respuestaCorrecta = normalizarRespuesta(respuestaCorrecta, opciones);
        this.estado = Objects.requireNonNull(estado, "El estado de la pregunta es obligatorio.");
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public OpcionesPregunta getOpciones() {
        return opciones;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public EstadoPregunta getEstado() {
        return estado;
    }

    public Pregunta conEstado(EstadoPregunta nuevoEstado) {
        return new Pregunta(
                id,
                nombre,
                enunciado,
                opciones,
                respuestaCorrecta,
                Objects.requireNonNull(nuevoEstado, "El nuevo estado es obligatorio."));
    }

    private static String requerirTexto(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }

    private static String normalizarRespuesta(String respuesta, OpcionesPregunta opciones) {
        if (respuesta == null || respuesta.isBlank()) {
            throw new IllegalArgumentException("La respuesta correcta es obligatoria.");
        }

        String normalizada = respuesta.trim().toUpperCase(Locale.ROOT);
        if (!opciones.contieneEtiqueta(normalizada)) {
            throw new IllegalArgumentException("La respuesta correcta debe ser una de las opciones A-D.");
        }
        return normalizada;
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Pregunta other)) {
            return false;
        }
        return id.equals(other.id)
                && nombre.equals(other.nombre)
                && enunciado.equals(other.enunciado)
                && opciones.equals(other.opciones)
                && respuestaCorrecta.equals(other.respuestaCorrecta)
                && estado == other.estado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, enunciado, opciones, respuestaCorrecta, estado);
    }
}
