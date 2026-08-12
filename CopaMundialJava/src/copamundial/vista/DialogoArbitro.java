package copamundial.vista;

import copamundial.modelo.Arbitro;
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
 * Alta / edicion de un integrante del cuerpo arbitral.
 */
public class DialogoArbitro extends JDialog {

    private static final String[] CATEGORIAS = {"Central", "Asistente 1", "Asistente 2", "VAR"};

    private final Arbitro arbitro;
    private boolean guardado = false;

    private JTextField txtNombre;
    private JTextField txtNacionalidad;
    private JComboBox<String> cboCategoria;

    public DialogoArbitro(JFrame padre, Arbitro arbitro, boolean esNuevo) {
        super(padre, esNuevo ? "Registrar Arbitro" : "Editar Arbitro", true);
        this.arbitro = arbitro;
        construirInterfaz();
        cargarDatos();
        setSize(420, 250);
        setLocationRelativeTo(padre);
    }

    public boolean isGuardado() {
        return guardado;
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));

        JPanel panelForm = new JPanel(new GridLayout(0, 2, 8, 8));
        panelForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Nombre completo:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Nacionalidad:"));
        txtNacionalidad = new JTextField();
        panelForm.add(txtNacionalidad);

        panelForm.add(new JLabel("Categoria:"));
        cboCategoria = new JComboBox<>(CATEGORIAS);
        panelForm.add(cboCategoria);

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
        txtNombre.setText(arbitro.getNombre());
        txtNacionalidad.setText(arbitro.getNacionalidad());
        if (arbitro.getCategoria() != null && !arbitro.getCategoria().isEmpty()) {
            cboCategoria.setSelectedItem(arbitro.getCategoria());
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        arbitro.setNombre(nombre);
        arbitro.setNacionalidad(txtNacionalidad.getText().trim());
        arbitro.setCategoria((String) cboCategoria.getSelectedItem());
        guardado = true;
        dispose();
    }
}
