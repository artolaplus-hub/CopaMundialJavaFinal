package copamundial.vista;

import copamundial.modelo.Pais;
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
import javax.swing.JTextField;

/**
 * Alta / edicion de los datos generales de un Pais. Desde aqui se accede
 * a los sub-dialogos que administran los arreglos fijos de jugadores y
 * cuerpo tecnico del mismo pais.
 */
public class DialogoPais extends JDialog {

    private static final String[] CONFEDERACIONES = {
        "UEFA", "CONMEBOL", "CONCACAF", "AFC", "CAF", "OFC"
    };

    private final Pais pais;
    private boolean guardado = false;

    private JTextField txtNombre;
    private JTextField txtSiglas;
    private JComboBox<String> cboConfederacion;

    public DialogoPais(JFrame padre, Pais pais, boolean esNuevo) {
        super(padre, esNuevo ? "Registrar Pais" : "Editar Pais", true);
        this.pais = pais;
        construirInterfaz(padre);
        cargarDatos();
        setSize(480, 320);
        setLocationRelativeTo(padre);
    }

    public boolean isGuardado() {
        return guardado;
    }

    private void construirInterfaz(JFrame padre) {
        setLayout(new BorderLayout(8, 8));

        JPanel panelForm = new JPanel(new GridLayout(0, 2, 8, 8));
        panelForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Nombre del pais:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Siglas (3 letras):"));
        txtSiglas = new JTextField();
        panelForm.add(txtSiglas);

        panelForm.add(new JLabel("Confederacion:"));
        cboConfederacion = new JComboBox<>(CONFEDERACIONES);
        panelForm.add(cboConfederacion);

        JButton btnJugadores = new JButton("Gestionar Jugadores (23)");
        btnJugadores.addActionListener(e -> new DialogoJugadores(padre, pais).setVisible(true));
        panelForm.add(btnJugadores);

        JButton btnCuerpoTecnico = new JButton("Gestionar Cuerpo Tecnico (5)");
        btnCuerpoTecnico.addActionListener(e -> new DialogoCuerpoTecnico(padre, pais).setVisible(true));
        panelForm.add(btnCuerpoTecnico);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar Pais");
        btnGuardar.addActionListener(e -> guardar());
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarDatos() {
        txtNombre.setText(pais.getNombre());
        txtSiglas.setText(pais.getSiglas());
        if (pais.getConfederacion() != null && !pais.getConfederacion().isEmpty()) {
            cboConfederacion.setSelectedItem(pais.getConfederacion());
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String siglas = txtSiglas.getText().trim().toUpperCase();
        if (nombre.isEmpty() || siglas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre y las siglas son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        pais.setNombre(nombre);
        pais.setSiglas(siglas);
        pais.setConfederacion((String) cboConfederacion.getSelectedItem());
        guardado = true;
        dispose();
    }
}
