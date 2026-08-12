package copamundial.vista;

import copamundial.modelo.Jugador;
import copamundial.modelo.Pais;
import copamundial.util.ArregloTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.ListSelectionModel;

/**
 * Administra el arreglo de tamanio fijo Jugador[MAX_JUGADORES] de un Pais.
 * Permite crear (llenar el siguiente espacio libre) y editar cualquier
 * jugador ya registrado.
 */
public class DialogoJugadores extends JDialog {

    private static final String[] POSICIONES = {"Portero", "Defensa", "Mediocampista", "Delantero"};

    private final Pais pais;

    private JTable tabla;
    private ArregloTableModel modeloTabla;

    private JTextField txtNombre;
    private JSpinner spnDorsal;
    private JComboBox<String> cboPosicion;
    private JSpinner spnEdad;
    private JLabel lblPosicionSeleccionada;

    private int filaSeleccionada = -1;

    public DialogoJugadores(JFrame padre, Pais pais) {
        super(padre, "Plantel de Jugadores - " + pais.getNombre(), true);
        this.pais = pais;
        construirInterfaz();
        cargarTabla();
        setSize(650, 500);
        setLocationRelativeTo(padre);
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));

        String[] columnas = {"#", "Dorsal", "Nombre", "Posicion", "Edad"};
        modeloTabla = new ArregloTableModel(columnas, new String[0][columnas.length]);
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                seleccionarFila(tabla.getSelectedRow());
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelForm = new JPanel(new GridLayout(0, 2, 6, 6));
        panelForm.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del jugador (arreglo fijo de 23 espacios)"));

        panelForm.add(new JLabel("Nombre completo:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Dorsal:"));
        spnDorsal = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        panelForm.add(spnDorsal);

        panelForm.add(new JLabel("Posicion:"));
        cboPosicion = new JComboBox<>(POSICIONES);
        panelForm.add(cboPosicion);

        panelForm.add(new JLabel("Edad:"));
        spnEdad = new JSpinner(new SpinnerNumberModel(18, 15, 45, 1));
        panelForm.add(spnEdad);

        lblPosicionSeleccionada = new JLabel("Seleccione una fila de la tabla para editar, o la primera fila vacia para agregar.");
        panelForm.add(lblPosicionSeleccionada);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar en Posicion Seleccionada");
        btnGuardar.addActionListener(e -> guardarJugador());
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCerrar);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.add(panelForm, BorderLayout.CENTER);
        panelSur.add(panelBotones, BorderLayout.SOUTH);

        add(panelSur, BorderLayout.SOUTH);
    }

    private void seleccionarFila(int fila) {
        filaSeleccionada = fila;
        if (fila < pais.getCantidadJugadores()) {
            Jugador j = pais.getJugadores()[fila];
            txtNombre.setText(j.getNombre());
            spnDorsal.setValue(j.getDorsal());
            cboPosicion.setSelectedItem(j.getPosicion());
            spnEdad.setValue(j.getEdad());
            lblPosicionSeleccionada.setText("Editando jugador existente en la fila " + (fila + 1));
        } else {
            txtNombre.setText("");
            spnDorsal.setValue(fila + 1);
            cboPosicion.setSelectedIndex(0);
            spnEdad.setValue(18);
            lblPosicionSeleccionada.setText("Espacio vacio: se creara un jugador nuevo en la fila " + (fila + 1));
        }
    }

    private void guardarJugador() {
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione primero una fila de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del jugador es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int dorsal = (Integer) spnDorsal.getValue();
        String posicion = (String) cboPosicion.getSelectedItem();
        int edad = (Integer) spnEdad.getValue();
        Jugador jugador = new Jugador(nombre, dorsal, posicion, edad);

        boolean exito;
        if (filaSeleccionada < pais.getCantidadJugadores()) {
            exito = pais.actualizarJugador(filaSeleccionada, jugador);
        } else if (filaSeleccionada == pais.getCantidadJugadores()) {
            exito = pais.agregarJugador(jugador);
        } else {
            JOptionPane.showMessageDialog(this, "Debe llenar los espacios en orden. Seleccione la primera fila vacia.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (exito) {
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Jugador guardado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "El plantel ya alcanzo el maximo de " + Pais.MAX_JUGADORES + " jugadores.", "Arreglo lleno", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        int total = Pais.MAX_JUGADORES;
        String[][] matriz = new String[total][5];
        Jugador[] jugadores = pais.getJugadores();
        for (int i = 0; i < total; i++) {
            matriz[i][0] = String.valueOf(i + 1);
            if (i < pais.getCantidadJugadores() && jugadores[i] != null) {
                Jugador j = jugadores[i];
                matriz[i][1] = String.valueOf(j.getDorsal());
                matriz[i][2] = j.getNombre();
                matriz[i][3] = j.getPosicion();
                matriz[i][4] = String.valueOf(j.getEdad());
            } else {
                matriz[i][1] = "-";
                matriz[i][2] = "-- vacio --";
                matriz[i][3] = "-";
                matriz[i][4] = "-";
            }
        }
        modeloTabla.actualizarDatos(matriz);
    }
}
