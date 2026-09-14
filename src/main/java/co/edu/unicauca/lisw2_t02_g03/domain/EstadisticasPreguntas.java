package co.edu.unicauca.lisw2_t02_g03.domain;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Fotografía inmutable de los conteos y porcentajes del banco de preguntas.
 */
public final class EstadisticasPreguntas {

    private final Map<EstadoPregunta, Long> conteos;
    private final Map<EstadoPregunta, Double> porcentajes;
    private final long total;

    public EstadisticasPreguntas(
            Map<EstadoPregunta, Long> conteos,
            Map<EstadoPregunta, Double> porcentajes,
            long total) {

        if (conteos == null || porcentajes == null || total < 0) {
            throw new IllegalArgumentException("Las estadísticas no pueden ser nulas ni negativas.");
        }

        EnumMap<EstadoPregunta, Long> copiaConteos = new EnumMap<>(EstadoPregunta.class);
        EnumMap<EstadoPregunta, Double> copiaPorcentajes = new EnumMap<>(EstadoPregunta.class);

        for (EstadoPregunta estado : EstadoPregunta.values()) {
            long conteo = conteos.getOrDefault(estado, 0L);
            double porcentaje = porcentajes.getOrDefault(estado, 0.0);

            if (conteo < 0 || porcentaje < 0.0 || porcentaje > 100.0) {
                throw new IllegalArgumentException("Los valores estadísticos están fuera de rango.");
            }

            copiaConteos.put(estado, conteo);
            copiaPorcentajes.put(estado, porcentaje);
        }

        this.conteos = Collections.unmodifiableMap(copiaConteos);
        this.porcentajes = Collections.unmodifiableMap(copiaPorcentajes);
        this.total = total;
    }

    public long getConteo(EstadoPregunta estado) {
        return conteos.getOrDefault(estado, 0L);
    }

    public double getPorcentaje(EstadoPregunta estado) {
        return porcentajes.getOrDefault(estado, 0.0);
    }

    public long getTotal() {
        return total;
    }

    public Map<EstadoPregunta, Long> getConteos() {
        return conteos;
    }

    public Map<EstadoPregunta, Double> getPorcentajes() {
        return porcentajes;
    }
}
