package copamundial.vista;

import copamundial.modelo.MiembroCuerpoTecnico;
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
 * Administra el arreglo de tamanio fijo MiembroCuerpoTecnico[MAX_CUERPO_TECNICO]
 * de un Pais (DT, asistente, preparador fisico, medico, analista).
 */
public class DialogoCuerpoTecnico extends JDialog {

    private static final String[] CARGOS = {
        "Director Tecnico", "Asistente Tecnico", "Preparador Fisico", "Medico del Equipo", "Analista Tactico"
    };

    private final Pais pais;

    private JTable tabla;
    private ArregloTableModel modeloTabla;

    private JTextField txtNombre;
    private JComboBox<String> cboCargo;
    private JSpinner spnEdad;
    private JLabel lblEstado;

    private int filaSeleccionada = -1;

    public DialogoCuerpoTecnico(JFrame padre, Pais pais) {
        super(padre, "Cuerpo Tecnico - " + pais.getNombre(), true);
        this.pais = pais;
        construirInterfaz();
        cargarTabla();
        setSize(600, 400);
        setLocationRelativeTo(padre);
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));

        String[] columnas = {"#", "Cargo", "Nombre", "Edad"};
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
        panelForm.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del miembro (arreglo fijo de 5 espacios)"));

        panelForm.add(new JLabel("Nombre completo:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Cargo:"));
        cboCargo = new JComboBox<>(CARGOS);
        panelForm.add(cboCargo);

        panelForm.add(new JLabel("Edad:"));
        spnEdad = new JSpinner(new SpinnerNumberModel(30, 20, 75, 1));
        panelForm.add(spnEdad);

        lblEstado = new JLabel("Seleccione una fila de la tabla.");
        panelForm.add(lblEstado);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar en Posicion Seleccionada");
        btnGuardar.addActionListener(e -> guardarMiembro());
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
        if (fila < pais.getCantidadCuerpoTecnico()) {
            MiembroCuerpoTecnico m = pais.getCuerpoTecnico()[fila];
            txtNombre.setText(m.getNombre());
            cboCargo.setSelectedItem(m.getCargo());
            spnEdad.setValue(m.getEdad());
            lblEstado.setText("Editando miembro existente en la fila " + (fila + 1));
        } else {
            txtNombre.setText("");
            cboCargo.setSelectedIndex(Math.min(fila, CARGOS.length - 1));
            spnEdad.setValue(30);
            lblEstado.setText("Espacio vacio: se creara un miembro nuevo en la fila " + (fila + 1));
        }
    }

    private void guardarMiembro() {
        if (filaSeleccionada < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione primero una fila de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String cargo = (String) cboCargo.getSelectedItem();
        int edad = (Integer) spnEdad.getValue();
        MiembroCuerpoTecnico miembro = new MiembroCuerpoTecnico(nombre, cargo, edad);

        boolean exito;
        if (filaSeleccionada < pais.getCantidadCuerpoTecnico()) {
            exito = pais.actualizarMiembroCuerpoTecnico(filaSeleccionada, miembro);
        } else if (filaSeleccionada == pais.getCantidadCuerpoTecnico()) {
            exito = pais.agregarMiembroCuerpoTecnico(miembro);
        } else {
            JOptionPane.showMessageDialog(this, "Debe llenar los espacios en orden. Seleccione la primera fila vacia.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (exito) {
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Miembro del cuerpo tecnico guardado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "El cuerpo tecnico ya alcanzo el maximo de " + Pais.MAX_CUERPO_TECNICO + " integrantes.", "Arreglo lleno", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla() {
        int total = Pais.MAX_CUERPO_TECNICO;
        String[][] matriz = new String[total][4];
        MiembroCuerpoTecnico[] cuerpo = pais.getCuerpoTecnico();
        for (int i = 0; i < total; i++) {
            matriz[i][0] = String.valueOf(i + 1);
            if (i < pais.getCantidadCuerpoTecnico() && cuerpo[i] != null) {
                MiembroCuerpoTecnico m = cuerpo[i];
                matriz[i][1] = m.getCargo();
                matriz[i][2] = m.getNombre();
                matriz[i][3] = String.valueOf(m.getEdad());
            } else {
                matriz[i][1] = "-- vacio --";
                matriz[i][2] = "-";
                matriz[i][3] = "-";
            }
        }
        modeloTabla.actualizarDatos(matriz);
    }
}
