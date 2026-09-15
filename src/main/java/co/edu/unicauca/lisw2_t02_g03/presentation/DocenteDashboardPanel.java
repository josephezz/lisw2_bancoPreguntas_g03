package co.edu.unicauca.lisw2_t02_g03.presentation;

import co.edu.unicauca.lisw2_t02_g03.domain.EstadisticasPreguntas;
import co.edu.unicauca.lisw2_t02_g03.domain.EstadoPregunta;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel para el docente: métricas globales del banco, estadísticas de estados y distribución porcentual gráfica.
 */
public class DocenteDashboardPanel extends JPanel {

    private final PreguntasController controller;

    private final EstadisticasView estadisticasView = new EstadisticasView();
    private final GraficaPastelView graficaPastelView = new GraficaPastelView();

    private JLabel lblKpiTotal;
    private JLabel lblKpiAprobadas;
    private JLabel lblKpiPendientes;
    private JLabel lblKpiRechazadas;

    public DocenteDashboardPanel(PreguntasController controller) {
        this.controller = controller;

        setLayout(new BorderLayout(0, 14));
        setOpaque(false);

        crearCabecera();
        crearKpis();
        crearPanelGraficas();

        controller.registrarObservador(estadisticasView);
        controller.registrarObservador(graficaPastelView);
        actualizarKpis();
    }

    private void crearCabecera() {
        ModernCard cardCabecera = new ModernCard(new BorderLayout());

        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);

        JLabel lblTitulo = new JLabel("📊 Panel Docente - Estadísticas del Banco de Preguntas");
        lblTitulo.setFont(UITheme.FONT_TITLE_MEDIUM);
        lblTitulo.setForeground(UITheme.COLOR_PRIMARY_DARK);

        JLabel lblSub = new JLabel("Monitoreo en tiempo real de la cobertura y madurez de los ítems mediante patrón Observador");
        lblSub.setFont(UITheme.FONT_SUBTITLE);
        lblSub.setForeground(UITheme.COLOR_TEXT_MUTED);

        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(4));
        panelTexto.add(lblSub);

        ModernButton btnRefrescar = new ModernButton("🔄 Actualizar Métricas", ModernButton.Variant.OUTLINE);
        btnRefrescar.addActionListener(e -> {
            controller.refrescarVistasObservadoras();
            actualizarKpis();
        });

        cardCabecera.add(panelTexto, BorderLayout.WEST);
        cardCabecera.add(btnRefrescar, BorderLayout.EAST);

        add(cardCabecera, BorderLayout.NORTH);
    }

    private void crearKpis() {
        JPanel panelKpis = new JPanel(new GridLayout(1, 4, 12, 0));
        panelKpis.setOpaque(false);

        lblKpiTotal = new JLabel("0", SwingConstants.CENTER);
        lblKpiAprobadas = new JLabel("0", SwingConstants.CENTER);
        lblKpiPendientes = new JLabel("0", SwingConstants.CENTER);
        lblKpiRechazadas = new JLabel("0", SwingConstants.CENTER);

        panelKpis.add(crearTarjetaKpi("Total Preguntas", lblKpiTotal, UITheme.COLOR_PRIMARY));
        panelKpis.add(crearTarjetaKpi("Aprobadas (Listas)", lblKpiAprobadas, UITheme.COLOR_SUCCESS));
        panelKpis.add(crearTarjetaKpi("En Revisión", lblKpiPendientes, UITheme.COLOR_INFO));
        panelKpis.add(crearTarjetaKpi("Rechazadas", lblKpiRechazadas, UITheme.COLOR_DANGER));

        add(panelKpis, BorderLayout.BEFORE_FIRST_LINE); // will place between header and center in composite container
    }

    private JPanel crearTarjetaKpi(String etiqueta, JLabel valorLabel, Color colorValor) {
        ModernCard card = new ModernCard(new BorderLayout(0, 4));
        card.setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel lbl = new JLabel(etiqueta, SwingConstants.CENTER);
        lbl.setFont(UITheme.FONT_SMALL_BOLD);
        lbl.setForeground(UITheme.COLOR_TEXT_MUTED);

        valorLabel.setFont(UITheme.FONT_TITLE_LARGE);
        valorLabel.setForeground(colorValor);

        card.add(lbl, BorderLayout.NORTH);
        card.add(valorLabel, BorderLayout.CENTER);
        return card;
    }

    private void crearPanelGraficas() {
        JPanel panelSuperior = new JPanel(new BorderLayout(0, 10));
        panelSuperior.setOpaque(false);

        // Armamos cabecera + KPIs en panelSuperior
        // Reorganizamos:
        removeAll();

        ModernCard cardCabecera = new ModernCard(new BorderLayout());
        JPanel panelTexto = new JPanel();
        panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
        panelTexto.setOpaque(false);
        JLabel lblTitulo = new JLabel("📊 Panel Docente - Estadísticas del Banco de Preguntas");
        lblTitulo.setFont(UITheme.FONT_TITLE_MEDIUM);
        lblTitulo.setForeground(UITheme.COLOR_PRIMARY_DARK);
        JLabel lblSub = new JLabel("Monitoreo en tiempo real de la cobertura y madurez de los ítems mediante patrón Observador");
        lblSub.setFont(UITheme.FONT_SUBTITLE);
        lblSub.setForeground(UITheme.COLOR_TEXT_MUTED);
        panelTexto.add(lblTitulo);
        panelTexto.add(Box.createVerticalStrut(4));
        panelTexto.add(lblSub);

        ModernButton btnRefrescar = new ModernButton("🔄 Actualizar Métricas", ModernButton.Variant.OUTLINE);
        btnRefrescar.addActionListener(e -> {
            controller.refrescarVistasObservadoras();
            actualizarKpis();
        });
        cardCabecera.add(panelTexto, BorderLayout.WEST);
        cardCabecera.add(btnRefrescar, BorderLayout.EAST);

        JPanel panelKpis = new JPanel(new GridLayout(1, 4, 12, 0));
        panelKpis.setOpaque(false);
        lblKpiTotal = new JLabel("0", SwingConstants.CENTER);
        lblKpiAprobadas = new JLabel("0", SwingConstants.CENTER);
        lblKpiPendientes = new JLabel("0", SwingConstants.CENTER);
        lblKpiRechazadas = new JLabel("0", SwingConstants.CENTER);

        panelKpis.add(crearTarjetaKpi("Total Preguntas", lblKpiTotal, UITheme.COLOR_PRIMARY));
        panelKpis.add(crearTarjetaKpi("Aprobadas (Listas)", lblKpiAprobadas, UITheme.COLOR_SUCCESS));
        panelKpis.add(crearTarjetaKpi("En Revisión", lblKpiPendientes, UITheme.COLOR_INFO));
        panelKpis.add(crearTarjetaKpi("Rechazadas", lblKpiRechazadas, UITheme.COLOR_DANGER));

        panelSuperior.add(cardCabecera, BorderLayout.NORTH);
        panelSuperior.add(panelKpis, BorderLayout.SOUTH);
        add(panelSuperior, BorderLayout.NORTH);

        // Centro: Gráficas y tabla de estados (EstadisticasView + GraficaPastelView)
        JPanel panelObservadores = new JPanel(new GridLayout(1, 2, 12, 0));
        panelObservadores.setOpaque(false);

        ModernCard cardStats = new ModernCard(new BorderLayout());
        cardStats.add(estadisticasView, BorderLayout.CENTER);

        ModernCard cardPastel = new ModernCard(new BorderLayout());
        cardPastel.add(graficaPastelView, BorderLayout.CENTER);

        panelObservadores.add(cardStats);
        panelObservadores.add(cardPastel);

        add(panelObservadores, BorderLayout.CENTER);
    }

    public void actualizarKpis() {
        EstadisticasPreguntas stats = controller.obtenerEstadisticas();
        lblKpiTotal.setText(String.valueOf(stats.getTotal()));
        lblKpiAprobadas.setText(String.valueOf(stats.getConteo(EstadoPregunta.APROBADA)));
        lblKpiPendientes.setText(String.valueOf(stats.getConteo(EstadoPregunta.PENDIENTE_REVISION)));
        lblKpiRechazadas.setText(String.valueOf(stats.getConteo(EstadoPregunta.RECHAZADA)));
    }
}
