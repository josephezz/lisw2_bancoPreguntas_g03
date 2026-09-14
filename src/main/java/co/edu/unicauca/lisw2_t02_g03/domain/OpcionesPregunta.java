package co.edu.unicauca.lisw2_t02_g03.domain;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Encapsula las cuatro opciones A-D mostradas por el prototipo del Taller 04.
 * En la guía esta responsabilidad aparece con el nombre QuestionDistractors.
 */
public final class OpcionesPregunta {

    private static final List<String> ETIQUETAS = List.of("A", "B", "C", "D");

    private final List<String> opciones;

    public OpcionesPregunta(List<String> opciones) {
        if (opciones == null || opciones.size() != ETIQUETAS.size()) {
            throw new IllegalArgumentException("La pregunta debe tener exactamente cuatro opciones A-D.");
        }

        List<String> normalizadas = opciones.stream()
                .map(OpcionesPregunta::normalizarTexto)
                .toList();

        if (normalizadas.stream().anyMatch(String::isBlank)) {
            throw new IllegalArgumentException("Ninguna opción puede estar vacía.");
        }

        this.opciones = List.copyOf(normalizadas);
    }

    public static OpcionesPregunta de(String opcionA, String opcionB, String opcionC, String opcionD) {
        return new OpcionesPregunta(List.of(opcionA, opcionB, opcionC, opcionD));
    }

    public List<String> comoLista() {
        return opciones;
    }

    public boolean contieneEtiqueta(String etiqueta) {
        if (etiqueta == null) {
            return false;
        }
        return ETIQUETAS.contains(etiqueta.trim().toUpperCase(Locale.ROOT));
    }

    public String comoTexto() {
        StringBuilder salida = new StringBuilder();
        for (int i = 0; i < opciones.size(); i++) {
            if (i > 0) {
                salida.append(System.lineSeparator());
            }
            salida.append(ETIQUETAS.get(i))
                    .append(") ")
                    .append(opciones.get(i));
        }
        return salida.toString();
    }

    private static String normalizarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OpcionesPregunta other)) {
            return false;
        }
        return opciones.equals(other.opciones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(opciones);
    }
}
