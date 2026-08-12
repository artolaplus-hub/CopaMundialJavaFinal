package copamundial.vista;

import copamundial.modelo.Estadio;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 * Alta / edicion de una sede (Estadio).
 */
public class DialogoEstadio extends JDialog {

    private final Estadio estadio;
    private boolean guardado = false;

    private JTextField txtNombre;
    private JTextField txtCiudad;
    private JTextField txtPaisSede;
    private JSpinner spnCapacidad;

    public DialogoEstadio(JFrame padre, Estadio estadio, boolean esNuevo) {
        super(padre, esNuevo ? "Registrar Sede" : "Editar Sede", true);
        this.estadio = estadio;
        construirInterfaz();
        cargarDatos();
        setSize(420, 280);
        setLocationRelativeTo(padre);
    }

    public boolean isGuardado() {
        return guardado;
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));

        JPanel panelForm = new JPanel(new GridLayout(0, 2, 8, 8));
        panelForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Nombre del estadio:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Ciudad:"));
        txtCiudad = new JTextField();
        panelForm.add(txtCiudad);

        panelForm.add(new JLabel("Pais sede:"));
        txtPaisSede = new JTextField();
        panelForm.add(txtPaisSede);

        panelForm.add(new JLabel("Capacidad:"));
        spnCapacidad = new JSpinner(new SpinnerNumberModel(40000, 1000, 200000, 1000));
        panelForm.add(spnCapacidad);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        txtNombre.setText(estadio.getNombre());
        txtCiudad.setText(estadio.getCiudad());
        txtPaisSede.setText(estadio.getPaisSede());
        if (estadio.getCapacidad() > 0) {
            spnCapacidad.setValue(estadio.getCapacidad());
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String ciudad = txtCiudad.getText().trim();
        if (nombre.isEmpty() || ciudad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre y la ciudad son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        estadio.setNombre(nombre);
        estadio.setCiudad(ciudad);
        estadio.setPaisSede(txtPaisSede.getText().trim());
        estadio.setCapacidad((Integer) spnCapacidad.getValue());
        guardado = true;
        dispose();
    }
}
