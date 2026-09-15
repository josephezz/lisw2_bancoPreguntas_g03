package co.edu.unicauca.lisw2_t02_g03.domain;

import java.util.Objects;

/**
 * Entidad de dominio principal del Banco de Preguntas Saber Pro.
 * <p>
 * Soporta múltiples tipos de preguntas: selección múltiple, análisis de casos,
 * multimedia, importadas, generadas automáticamente, etc.
 * Los campos opcionales (competencia, categoria, nivelDificultad, recursoMultimedia)
 * pueden ser {@code null} cuando no aplican al tipo de pregunta.
 */
public final class Pregunta {

    private final String id;
    private final String nombre;
    private final String enunciado;
    private final String tipo;
    private final OpcionesPregunta opciones;
    private final String respuestaCorrecta;
    private final EstadoPregunta estado;

    // --- Campos opcionales de clasificación y enriquecimiento ---
    private final String competencia;
    private final String categoria;
    private final String nivelDificultad;
    private final String recursoMultimedia;
    private final String autorLogin;
    private final String observaciones;

    /**
     * Constructor completo con todos los campos.
     */
    public Pregunta(
            String id,
            String nombre,
            String enunciado,
            String tipo,
            OpcionesPregunta opciones,
            String respuestaCorrecta,
            EstadoPregunta estado,
            String competencia,
            String categoria,
            String nivelDificultad,
            String recursoMultimedia,
            String autorLogin,
            String observaciones) {

        this.id = requerirTexto(id, "El id de la pregunta es obligatorio.");
        this.nombre = nombre != null ? nombre.trim() : "";
        this.enunciado = enunciado != null ? enunciado.trim() : "";
        this.tipo = (tipo != null && !tipo.isBlank()) ? tipo.trim() : "SELECCION_MULTIPLE";
        this.opciones = opciones;
        this.respuestaCorrecta = normalizarOpcional(respuestaCorrecta);
        this.estado = Objects.requireNonNull(estado, "El estado de la pregunta es obligatorio.");

        // Campos opcionales — se normalizan pero pueden ser null
        this.competencia = normalizarOpcional(competencia);
        this.categoria = normalizarOpcional(categoria);
        this.nivelDificultad = normalizarOpcional(nivelDificultad);
        this.recursoMultimedia = normalizarOpcional(recursoMultimedia);
        this.autorLogin = normalizarOpcional(autorLogin);
        this.observaciones = normalizarOpcional(observaciones);
    }

    /**
     * Constructor de 7 parámetros con tipo explícito.
     */
    public Pregunta(
            String id,
            String nombre,
            String enunciado,
            String tipo,
            OpcionesPregunta opciones,
            String respuestaCorrecta,
            EstadoPregunta estado) {

        this(id, nombre, enunciado, tipo,
                opciones, respuestaCorrecta, estado,
                null, null, null, null, null, null);
    }

    /**
     * Constructor compatible con el uso existente en tests y vistas (6 parámetros).
     * Tipo se infiere como "SELECCION_MULTIPLE" por defecto.
     * Valida opciones y respuesta coherente para compatibilidad con pruebas unitarias existentes.
     */
    public Pregunta(
            String id,
            String nombre,
            String enunciado,
            OpcionesPregunta opciones,
            String respuestaCorrecta,
            EstadoPregunta estado) {

        this(id, nombre, enunciado, "SELECCION_MULTIPLE",
                opciones, respuestaCorrecta, estado,
                null, null, null, null, null, null);

        if (opciones == null) {
            throw new IllegalArgumentException("Las opciones son obligatorias.");
        }
        if (respuestaCorrecta == null || !opciones.contieneEtiqueta(respuestaCorrecta.trim())) {
            throw new IllegalArgumentException(
                    "La respuesta correcta '" + respuestaCorrecta + "' no corresponde a ninguna etiqueta válida (A-D).");
        }
    }

    /**
     * Constructor de 5 parámetros (id, nombre, enunciado, tipo, estado).
     */
    public Pregunta(
            String id,
            String nombre,
            String enunciado,
            String tipo,
            EstadoPregunta estado) {

        this(id, nombre, enunciado, tipo, null, null, estado,
                null, null, null, null, null, null);
    }

    // ============================
    // Getters
    // ============================

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getTipo() {
        return tipo;
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

    public String getCompetencia() {
        return competencia;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public String getRecursoMultimedia() {
        return recursoMultimedia;
    }

    public String getAutorLogin() {
        return autorLogin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getObservacionesRevision() {
        return observaciones;
    }

    // ============================
    // Métodos de copia inmutable
    // ============================

    /**
     * Crea una nueva instancia con un estado diferente, conservando todos los demás campos.
     */
    public Pregunta conEstado(EstadoPregunta nuevoEstado) {
        return new Pregunta(
                id, nombre, enunciado, tipo, opciones, respuestaCorrecta,
                Objects.requireNonNull(nuevoEstado, "El nuevo estado es obligatorio."),
                competencia, categoria, nivelDificultad,
                recursoMultimedia, autorLogin, observaciones);
    }

    /**
     * Crea una nueva instancia con un nombre diferente.
     */
    public Pregunta conNombre(String nuevoNombre) {
        return new Pregunta(
                id, nuevoNombre, enunciado, tipo, opciones, respuestaCorrecta, estado,
                competencia, categoria, nivelDificultad,
                recursoMultimedia, autorLogin, observaciones);
    }

    /**
     * Crea una nueva instancia con un enunciado diferente.
     */
    public Pregunta conEnunciado(String nuevoEnunciado) {
        return new Pregunta(
                id, nombre, nuevoEnunciado, tipo, opciones, respuestaCorrecta, estado,
                competencia, categoria, nivelDificultad,
                recursoMultimedia, autorLogin, observaciones);
    }

    /**
     * Crea una nueva instancia con observaciones actualizadas.
     */
    public Pregunta conObservaciones(String nuevasObservaciones) {
        return new Pregunta(
                id, nombre, enunciado, tipo, opciones, respuestaCorrecta, estado,
                competencia, categoria, nivelDificultad,
                recursoMultimedia, autorLogin, nuevasObservaciones);
    }

    // ============================
    // Validación interna
    // ============================

    private static String requerirTexto(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }

    private static String normalizarOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }

    // ============================
    // Object overrides
    // ============================

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
                && tipo.equals(other.tipo)
                && Objects.equals(opciones, other.opciones)
                && Objects.equals(respuestaCorrecta, other.respuestaCorrecta)
                && estado == other.estado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, enunciado, tipo, opciones, respuestaCorrecta, estado);
    }
}
