package copamundial.vista;

import copamundial.datos.GestorDatos;
import copamundial.datos.MotorEliminacion;
import copamundial.modelo.LlaveEliminacion;
import copamundial.util.ArregloTableModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/** Interfaz del Modulo 5: Fase de Eliminacion Directa y Mejores Terceros. */
public class PanelEliminacion extends JPanel {

    private static final String[] COLUMNAS =
            {"Llave", "Local", "Visitante", "Marcador", "Definicion", "Ganador"};

    private final GestorDatos gestor;
    private final PanelSimulacion panelSimulacion;
    private final MotorEliminacion motor = new MotorEliminacion();

    private final ArregloTableModel modeloTabla = new ArregloTableModel(COLUMNAS, new String[0][COLUMNAS.length]);
    private final JComboBox<String> selectorRonda = new JComboBox<>();
    private final JTextArea areaResultados = new JTextArea();
    private final JLabel estado = new JLabel("Finalice la fase de grupos (Modulo 4) y presione \"Iniciar Fase Eliminatoria\".");
    private final JLabel campeonLabel = new JLabel(" ", SwingConstants.CENTER);

    private final JButton btnIniciar = new JButton("Iniciar Fase Eliminatoria");
    private final JButton btnUno     = new JButton("Simular Siguiente Llave");
    private final JButton btnFase    = new JButton("Simular Fase Completa");

    // Referencia a la pestana de estadisticas para notificarle cuando termine el torneo
    private PanelEstadisticas panelEstadisticas;

    public PanelEliminacion(GestorDatos gestor, PanelSimulacion panelSimulacion) {
        this.gestor = gestor;
        this.panelSimulacion = panelSimulacion;
        construirInterfaz();
        habilitarSimulacion(false);
    }

    /** Permite enlazar la pestana de estadisticas despues de crearla. */
    public void setPanelEstadisticas(PanelEstadisticas panelEstadisticas) {
        this.panelEstadisticas = panelEstadisticas;
    }

    /** Devuelve el motor de eliminacion para que PanelEstadisticas pueda consultarlo. */
    public MotorEliminacion getMotor() {
        return motor;
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel superior = new JPanel(new GridLayout(2, 1));
        JPanel comandos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comandos.add(btnIniciar);
        comandos.add(btnUno);
        comandos.add(btnFase);
        comandos.add(new JLabel("Ronda:"));
        comandos.add(selectorRonda);
        superior.add(comandos);
        superior.add(estado);
        add(superior, BorderLayout.NORTH);

        JTable tabla = new JTable(modeloTabla);
        areaResultados.setEditable(false);
        areaResultados.setBorder(BorderFactory.createTitledBorder("Resultados de la fase eliminatoria"));
        JSplitPane divisor = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tabla), new JScrollPane(areaResultados));
        divisor.setResizeWeight(0.45);
        add(divisor, BorderLayout.CENTER);

        campeonLabel.setFont(campeonLabel.getFont().deriveFont(Font.BOLD, 20f));
        add(campeonLabel, BorderLayout.SOUTH);

        btnIniciar.addActionListener(e -> iniciarFaseEliminatoria());
        btnUno.addActionListener(e -> simularUno());
        btnFase.addActionListener(e -> simularFase());
        selectorRonda.addActionListener(e -> refrescarTabla());
    }

    private void iniciarFaseEliminatoria() {
        if (!panelSimulacion.getMotor().estaFinalizado()) {
            JOptionPane.showMessageDialog(this,
                    "Primero debe finalizar TODOS los partidos de la fase de grupos\n"
                    + "en la pestana \"Calendario / Simulacion\".",
                    "Fase de grupos incompleta", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!motor.iniciar(gestor, panelSimulacion.getMotor())) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo armar la llave de eliminacion directa.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectorRonda.removeAllItems();
        for (String nombreRonda : motor.getNombresRondas()) {
            selectorRonda.addItem(nombreRonda);
        }
        selectorRonda.setSelectedIndex(0);
        areaResultados.setText("");
        campeonLabel.setText(" ");
        habilitarSimulacion(true);
        actualizarEstado();
        refrescarTabla();
    }

    private void simularUno() {
        LlaveEliminacion llave = motor.simularSiguiente();
        if (llave == null) return;
        areaResultados.append(llave.obtenerResumen() + "\n");
        seleccionarRonda(llave.getRonda());
        refrescarTabla();
        actualizarEstado();
    }

    private void simularFase() {
        areaResultados.append(motor.simularFaseCompleta());
        refrescarTabla();
        actualizarEstado();
    }

    private void seleccionarRonda(String nombreRonda) {
        for (int i = 0; i < selectorRonda.getItemCount(); i++) {
            if (selectorRonda.getItemAt(i).equals(nombreRonda)) {
                selectorRonda.setSelectedIndex(i);
                return;
            }
        }
    }

    private void refrescarTabla() {
        String ronda = (String) selectorRonda.getSelectedItem();
        if (!motor.isIniciado() || ronda == null) {
            modeloTabla.actualizarDatos(new String[0][COLUMNAS.length]);
            return;
        }
        LlaveEliminacion[] llaves = motor.obtenerLlavesDeRonda(ronda);
        String[][] datos = new String[llaves.length][COLUMNAS.length];
        for (int i = 0; i < llaves.length; i++) {
            LlaveEliminacion llave = llaves[i];
            datos[i][0] = String.valueOf(i + 1);
            datos[i][1] = (llave.getEquipoLocal()     != null) ? llave.getEquipoLocal().getNombre()     : "Por definir";
            datos[i][2] = (llave.getEquipoVisitante() != null) ? llave.getEquipoVisitante().getNombre() : "Por definir";
            datos[i][3] = llave.isJugado() ? (llave.getGolesLocal() + " - " + llave.getGolesVisitante()) : "-";
            datos[i][4] = llave.isDefinidoPorPenales()
                    ? ("Penales " + llave.getPenalesLocal() + "-" + llave.getPenalesVisitante())
                    : (llave.isJugado() ? "Tiempo regular" : "-");
            datos[i][5] = llave.isJugado() ? llave.getGanador().getNombre() : "-";
        }
        modeloTabla.actualizarDatos(datos);
    }

    private void actualizarEstado() {
        estado.setText("Llaves jugadas: " + motor.getSiguienteLlave() + " / " + motor.getTotalLlaves());
        if (motor.estaFinalizado()) {
            estado.setText(estado.getText() + " - Fase eliminatoria finalizada. Ver pestana \"8. Estadisticas Finales\".");
            campeonLabel.setText("CAMPEON DEL MUNDO: " + motor.getCampeon().getNombre()
                    + "      (Subcampeon: " + motor.getSubcampeon().getNombre() + ")");
            habilitarSimulacion(false);

            // Modulo 6: notificar a la pestana de estadisticas que el torneo termino
            if (panelEstadisticas != null) {
                panelEstadisticas.notificarTorneoFinalizado();
            }
        }
    }

    private void habilitarSimulacion(boolean habilitar) {
        btnUno.setEnabled(habilitar);
        btnFase.setEnabled(habilitar);
    }

    /** Llamado cuando cambian datos anteriores (paises, sorteo, calendario). */
    public void invalidarEliminacion() {
        habilitarSimulacion(false);
        selectorRonda.removeAllItems();
        areaResultados.setText("");
        campeonLabel.setText(" ");
        estado.setText("Los datos cambiaron. Vuelva a finalizar la fase de grupos e inicie la eliminatoria de nuevo.");
        modeloTabla.actualizarDatos(new String[0][COLUMNAS.length]);
    }
}
