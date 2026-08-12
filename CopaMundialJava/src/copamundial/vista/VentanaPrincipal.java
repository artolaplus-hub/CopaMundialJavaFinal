package copamundial.vista;

import copamundial.datos.GestorDatos;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 * Ventana principal del Sistema de Gestion y Simulacion "Copa Mundial Java".
 * MODULO 6: se agrego la pestana "8. Estadisticas Finales" (PanelEstadisticas).
 */
public class VentanaPrincipal extends JFrame {

    private final GestorDatos gestor;

    private PanelConfiguracion panelConfiguracion;
    private PanelPaises panelPaises;
    private PanelEstadios panelEstadios;
    private PanelArbitros panelArbitros;
    private PanelGrupos panelGrupos;
    private PanelSimulacion panelSimulacion;
    private PanelEliminacion panelEliminacion;
    private PanelEstadisticas panelEstadisticas; // Modulo 6
    private final CardLayout navegador = new CardLayout();
    private final JPanel panelContenido = new JPanel(navegador);
    private final BotonMenu[] botonesMenu = new BotonMenu[9];
    private final String[] claves = {"INICIO", "CONFIGURACION", "PAISES", "ESTADIOS",
        "ARBITROS", "GRUPOS", "PARTIDOS", "ELIMINACION", "ESTADISTICAS"};

    public VentanaPrincipal() {
        super("Copa Mundial Java - Gestion y Simulacion del Torneo");
        this.gestor = new GestorDatos();
        construirInterfaz();
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1280, 820);
        setMinimumSize(new Dimension(1050, 700));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panelConfiguracion = new PanelConfiguracion(this, gestor);
        panelPaises        = new PanelPaises(this, gestor);
        panelEstadios      = new PanelEstadios(this, gestor);
        panelArbitros      = new PanelArbitros(this, gestor);
        panelGrupos        = new PanelGrupos(gestor);
        panelSimulacion    = new PanelSimulacion(gestor);
        panelEliminacion   = new PanelEliminacion(gestor, panelSimulacion);

        // Modulo 6: crear el panel de estadisticas y enlazarlo con la eliminacion
        panelEstadisticas  = new PanelEstadisticas(gestor, panelSimulacion, panelEliminacion);
        panelEliminacion.setPanelEstadisticas(panelEstadisticas);

        panelContenido.setBackground(TemaMundial.FONDO);
        panelContenido.add(new PanelInicio(() -> mostrarVista(1), () -> mostrarVista(6)), claves[0]);
        panelContenido.add(panelConfiguracion, claves[1]);
        panelContenido.add(panelPaises, claves[2]);
        panelContenido.add(panelEstadios, claves[3]);
        panelContenido.add(panelArbitros, claves[4]);
        panelContenido.add(panelGrupos, claves[5]);
        panelContenido.add(panelSimulacion, claves[6]);
        panelContenido.add(panelEliminacion, claves[7]);
        panelContenido.add(panelEstadisticas, claves[8]);

        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.add(crearMenuLateral(), BorderLayout.WEST);
        cuerpo.add(panelContenido, BorderLayout.CENTER);

        JLabel pie = new JLabel("  SISTEMA OFICIAL  |  Todos los datos se gestionan en memoria mediante arreglos",
                SwingConstants.LEFT);
        pie.setOpaque(true);
        pie.setBackground(TemaMundial.AZUL_NOCHE);
        pie.setForeground(new java.awt.Color(205, 220, 225));
        pie.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        pie.setPreferredSize(new Dimension(100, 27));

        add(new EncabezadoMundial(), BorderLayout.NORTH);
        add(cuerpo, BorderLayout.CENTER);
        add(pie, BorderLayout.SOUTH);
        mostrarVista(0);
    }

    private JPanel crearMenuLateral() {
        JPanel menu = new JPanel();
        menu.setBackground(TemaMundial.AZUL_NOCHE);
        menu.setPreferredSize(new Dimension(235, 100));
        menu.setBorder(BorderFactory.createEmptyBorder(18, 8, 14, 8));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("  NAVEGACION");
        titulo.setForeground(TemaMundial.DORADO);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        menu.add(titulo);
        menu.add(Box.createVerticalStrut(8));

        String[] textos = {"00   Inicio", "01   Configuracion", "02   Paises",
            "03   Sedes y estadios", "04   Cuerpo arbitral", "05   Sorteo y grupos",
            "06   Calendario y partidos", "07   Eliminacion directa", "08   Estadisticas finales"};
        for (int i = 0; i < botonesMenu.length; i++) {
            final int indice = i;
            botonesMenu[i] = new BotonMenu(textos[i]);
            botonesMenu[i].setAlignmentX(LEFT_ALIGNMENT);
            botonesMenu[i].addActionListener(e -> mostrarVista(indice));
            menu.add(botonesMenu[i]);
            menu.add(Box.createVerticalStrut(2));
        }

        menu.add(Box.createVerticalGlue());
        JLabel version = new JLabel("  MODULO 6  •  JAVA + SWING");
        version.setForeground(new Color(145, 170, 178));
        version.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        version.setAlignmentX(LEFT_ALIGNMENT);
        menu.add(version);
        return menu;
    }

    private void mostrarVista(int indice) {
        navegador.show(panelContenido, claves[indice]);
        for (int i = 0; i < botonesMenu.length; i++) {
            botonesMenu[i].setActivo(i == indice);
        }
    }

    /**
     * Refresca todos los paneles dependientes despues de dimensionar el torneo
     * o de ejecutar la carga masiva de datos de demostracion.
     */
    public void actualizarTodo() {
        panelPaises.refrescar();
        panelEstadios.refrescar();
        panelArbitros.refrescar();
        panelGrupos.invalidarSorteo();
        panelSimulacion.invalidarCalendario();
        panelEliminacion.invalidarEliminacion();
        panelEstadisticas.invalidar(); // Modulo 6
    }
}
