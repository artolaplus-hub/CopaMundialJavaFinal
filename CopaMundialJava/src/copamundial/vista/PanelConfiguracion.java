package copamundial.vista;

import copamundial.datos.GeneradorDemo;
import copamundial.datos.GestorDatos;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Panel de Configuracion y Parametros Iniciales (Requerimiento 1 y 3).
 * Permite elegir la cantidad de paises participantes (24/32/48/64), lo que
 * dimensiona TODOS los arreglos principales del sistema, y ofrece el boton
 * de carga masiva de datos ficticios de demostracion.
 */
public class PanelConfiguracion extends JPanel {

    private final VentanaPrincipal ventanaPrincipal;
    private final GestorDatos gestor;

    private JRadioButton rb24;
    private JRadioButton rb32;
    private JRadioButton rb48;
    private JRadioButton rb64;

    private JLabel lblEstado;
    private JButton btnGenerarDemo;

    public PanelConfiguracion(VentanaPrincipal ventanaPrincipal, GestorDatos gestor) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.gestor = gestor;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Configuracion Inicial del Mundial Java");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        add(titulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new javax.swing.BoxLayout(panelCentro, javax.swing.BoxLayout.Y_AXIS));

        JPanel panelSeleccion = new JPanel(new GridLayout(0, 1, 6, 6));
        panelSeleccion.setBorder(BorderFactory.createTitledBorder("1. Cantidad de paises participantes"));

        rb24 = new JRadioButton("24 equipos");
        rb32 = new JRadioButton("32 equipos");
        rb48 = new JRadioButton("48 equipos");
        rb64 = new JRadioButton("64 equipos");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rb24);
        grupo.add(rb32);
        grupo.add(rb48);
        grupo.add(rb64);
        rb32.setSelected(true);

        panelSeleccion.add(rb24);
        panelSeleccion.add(rb32);
        panelSeleccion.add(rb48);
        panelSeleccion.add(rb64);

        JButton btnDimensionar = new JButton("Dimensionar / Reiniciar Torneo");
        btnDimensionar.addActionListener(e -> dimensionarTorneo());

        JPanel panelBotonDimensionar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotonDimensionar.add(btnDimensionar);

        JPanel panelDemo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDemo.setBorder(BorderFactory.createTitledBorder("2. Carga masiva de datos"));
        btnGenerarDemo = new JButton("Generar Datos de Demostracion");
        btnGenerarDemo.setEnabled(false);
        btnGenerarDemo.addActionListener(e -> generarDemo());
        panelDemo.add(btnGenerarDemo);

        lblEstado = new JLabel("Estado: torneo aun no dimensionado.");
        lblEstado.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panelCentro.add(panelSeleccion);
        panelCentro.add(panelBotonDimensionar);
        panelCentro.add(panelDemo);
        panelCentro.add(lblEstado);

        add(panelCentro, BorderLayout.CENTER);
    }

    private void dimensionarTorneo() {
        int tamanio = obtenerTamanioSeleccionado();

        if (gestor.isTorneoDimensionado()) {
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "Ya existe un torneo dimensionado. Si continua, se BORRARAN todos los paises,\n"
                    + "sedes y arbitros registrados hasta ahora. Desea continuar?",
                    "Confirmar reinicio", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }
        }

        boolean exito = gestor.dimensionarTorneo(tamanio);
        if (exito) {
            btnGenerarDemo.setEnabled(true);
            actualizarEstado();
            ventanaPrincipal.actualizarTodo();
            JOptionPane.showMessageDialog(this,
                    "Torneo dimensionado para " + tamanio + " equipos.\n"
                    + "Paises: " + gestor.getPaises().length + " espacios\n"
                    + "Sedes: " + gestor.getEstadios().length + " espacios\n"
                    + "Arbitros: " + gestor.getArbitros().length + " espacios");
        }
    }

    private void generarDemo() {
        if (!gestor.isTorneoDimensionado()) {
            JOptionPane.showMessageDialog(this, "Primero debe dimensionar el torneo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this,
                "Esto llenara todos los arreglos (paises, jugadores, cuerpo tecnico,\n"
                + "sedes y arbitros) con datos ficticios. Los datos que ya haya\n"
                + "ingresado manualmente permaneceran y se completaran los espacios\n"
                + "restantes. Desea continuar?",
                "Generar datos de demostracion", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        GeneradorDemo.poblarDatosDemo(gestor);
        actualizarEstado();
        ventanaPrincipal.actualizarTodo();
        JOptionPane.showMessageDialog(this, "Datos de demostracion generados correctamente.\n"
                + "Puede modificarlos manualmente en las demas pestanas.");
    }

    private int obtenerTamanioSeleccionado() {
        if (rb24.isSelected()) {
            return 24;
        }
        if (rb48.isSelected()) {
            return 48;
        }
        if (rb64.isSelected()) {
            return 64;
        }
        return 32;
    }

    private void actualizarEstado() {
        lblEstado.setText("<html>Estado: mundial de " + gestor.getTamanioMundial() + " equipos dimensionado.<br>"
                + "Paises registrados: " + gestor.getTotalPaises() + " / " + gestor.getPaises().length + "<br>"
                + "Sedes registradas: " + gestor.getTotalEstadios() + " / " + gestor.getEstadios().length + "<br>"
                + "Arbitros registrados: " + gestor.getTotalArbitros() + " / " + gestor.getArbitros().length + "</html>");
    }
}
