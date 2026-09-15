package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadisticasPreguntas;
import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;
import co.edu.unicauca.lisw2_t02_g03.infra.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;

/**
 * Primera vista observadora: muestra el número de preguntas por estado.
 */
public class EstadisticasView extends JPanel implements Observer<EstadisticasPreguntas> {

    private final JLabel borrador = new JLabel("0");
    private final JLabel pendiente = new JLabel("0");
    private final JLabel aprobada = new JLabel("0");
    private final JLabel rechazada = new JLabel("0");
    private final JLabel eliminada = new JLabel("0");
    private final JLabel total = new JLabel("0");

    public EstadisticasView() {
        setBorder(BorderFactory.createTitledBorder("Preguntas por estado"));
        setLayout(new GridLayout(6, 2, 8, 5));

        add(new JLabel("Borrador:"));
        add(borrador);
        add(new JLabel("Pendiente de revisión:"));
        add(pendiente);
        add(new JLabel("Aprobada:"));
        add(aprobada);
        add(new JLabel("Rechazada:"));
        add(rechazada);
        add(new JLabel("Eliminada:"));
        add(eliminada);
        add(new JLabel("Total:"));
        add(total);
    }

    @Override
    public void actualizar(EstadisticasPreguntas estadisticas) {
        if (estadisticas == null) {
            return;
        }

        Runnable actualizacion = () -> {
            borrador.setText(Long.toString(estadisticas.getConteo(EstadoPregunta.BORRADOR)));
            pendiente.setText(Long.toString(estadisticas.getConteo(EstadoPregunta.PENDIENTE_REVISION)));
            aprobada.setText(Long.toString(estadisticas.getConteo(EstadoPregunta.APROBADA)));
            rechazada.setText(Long.toString(estadisticas.getConteo(EstadoPregunta.RECHAZADA)));
            eliminada.setText(Long.toString(estadisticas.getConteo(EstadoPregunta.ELIMINADA)));
            total.setText(Long.toString(estadisticas.getTotal()));
        };

        ejecutarEnEdt(actualizacion);
    }

    private static void ejecutarEnEdt(Runnable accion) {
        if (SwingUtilities.isEventDispatchThread()) {
            accion.run();
        } else {
            SwingUtilities.invokeLater(accion);
        }
    }
}
