package copamundial.vista;

import copamundial.datos.GestorDatos;
import copamundial.datos.MotorSimulacion;
import copamundial.modelo.Partido;
import copamundial.modelo.PosicionGrupo;
import copamundial.util.ArregloTableModel;
import java.awt.BorderLayout;
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

/** Interfaz del Modulo 4: calendario y motor de simulacion. */
public class PanelSimulacion extends JPanel {

    private final GestorDatos gestor;
    private final MotorSimulacion motor = new MotorSimulacion();
    private final ArregloTableModel modeloTabla;
    private final JComboBox<String> selectorGrupo = new JComboBox<>();
    private final JTextArea areaResultados = new JTextArea();
    private final JLabel estado = new JLabel("Prepare el calendario para comenzar.");
    private final JButton btnUno = new JButton("Simular Partido a Partido");
    private final JButton btnFase = new JButton("Simular Fase Completa");

    public PanelSimulacion(GestorDatos gestor) {
        this.gestor = gestor;
        modeloTabla = new ArregloTableModel(
                new String[]{"Pos", "Pais", "PJ", "PG", "PE", "PP", "GF", "GC", "DG", "Pts"},
                new String[0][10]);
        construirInterfaz();
        habilitarSimulacion(false);
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel superior = new JPanel(new GridLayout(2, 1));
        JPanel comandos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnPreparar = new JButton("Preparar / Reiniciar Calendario");
        comandos.add(btnPreparar);
        comandos.add(btnUno);
        comandos.add(btnFase);
        comandos.add(new JLabel("Tabla del grupo:"));
        comandos.add(selectorGrupo);
        superior.add(comandos);
        superior.add(estado);
        add(superior, BorderLayout.NORTH);

        JTable tabla = new JTable(modeloTabla);
        areaResultados.setEditable(false);
        areaResultados.setBorder(BorderFactory.createTitledBorder("Resultados, goles y tarjetas"));
        JSplitPane divisor = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tabla), new JScrollPane(areaResultados));
        divisor.setResizeWeight(0.45);
        add(divisor, BorderLayout.CENTER);

        btnPreparar.addActionListener(e -> prepararCalendario());
        btnUno.addActionListener(e -> simularUno());
        btnFase.addActionListener(e -> simularFase());
        selectorGrupo.addActionListener(e -> refrescarTabla());
    }

    private void prepararCalendario() {
        if (!motor.preparar(gestor)) {
            JOptionPane.showMessageDialog(this,
                    "Debe registrar una cantidad de paises multiplo de 4 (minimo 4).\n"
                    + "Puede usar 'Generar Datos de Demostracion' en Configuracion.",
                    "No se puede crear el calendario", JOptionPane.WARNING_MESSAGE);
            return;
        }
        selectorGrupo.removeAllItems();
        for (int i = 0; i < motor.getCantidadGrupos(); i++) {
            selectorGrupo.addItem("Grupo " + (char) ('A' + i));
        }
        areaResultados.setText("");
        habilitarSimulacion(true);
        actualizarEstado();
        refrescarTabla();
    }

    private void simularUno() {
        Partido partido = motor.simularSiguiente();
        if (partido == null) return;
        areaResultados.append(partido.obtenerResumen() + "\n");
        selectorGrupo.setSelectedIndex(partido.getNumeroGrupo());
        refrescarTabla();
        actualizarEstado();
    }

    private void simularFase() {
        areaResultados.append(motor.simularFaseCompleta());
        refrescarTabla();
        actualizarEstado();
    }

    private void refrescarTabla() {
        int grupo = selectorGrupo.getSelectedIndex();
        if (!motor.estaPreparado() || grupo < 0) {
            modeloTabla.actualizarDatos(new String[0][10]);
            return;
        }
        PosicionGrupo[] tabla = motor.obtenerTablaGrupo(grupo);
        String[][] datos = new String[tabla.length][10];
        for (int i = 0; i < tabla.length; i++) {
            PosicionGrupo p = tabla[i];
            datos[i][0] = String.valueOf(i + 1);
            datos[i][1] = p.getPais().getNombre();
            datos[i][2] = String.valueOf(p.getJugados());
            datos[i][3] = String.valueOf(p.getGanados());
            datos[i][4] = String.valueOf(p.getEmpatados());
            datos[i][5] = String.valueOf(p.getPerdidos());
            datos[i][6] = String.valueOf(p.getGolesFavor());
            datos[i][7] = String.valueOf(p.getGolesContra());
            datos[i][8] = String.valueOf(p.getDiferencia());
            datos[i][9] = String.valueOf(p.getPuntos());
        }
        modeloTabla.actualizarDatos(datos);
    }

    private void actualizarEstado() {
        estado.setText("Partidos jugados: " + motor.getSiguientePartido()
                + " / " + motor.getTotalPartidos());
        if (motor.estaFinalizado()) {
            estado.setText(estado.getText() + " - Fase de grupos finalizada");
            habilitarSimulacion(false);
        }
    }

    private void habilitarSimulacion(boolean habilitar) {
        btnUno.setEnabled(habilitar);
        btnFase.setEnabled(habilitar);
    }
    public MotorSimulacion getMotor() {
         return motor;
    }
          
    public void invalidarCalendario() {
        habilitarSimulacion(false);
        estado.setText("Los datos cambiaron. Prepare nuevamente el calendario.");
        modeloTabla.actualizarDatos(new String[0][10]);
    }
}
