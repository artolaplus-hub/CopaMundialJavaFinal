package copamundial.vista;

import copamundial.datos.GestorDatos;
import copamundial.modelo.Arbitro;
import copamundial.util.ArregloTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

/**
 * Panel del Modulo 1 para el registro y edicion del Cuerpo Arbitral.
 */
public class PanelArbitros extends JPanel {

    private final JFrame ventanaPrincipal;
    private final GestorDatos gestor;

    private JTable tabla;
    private ArregloTableModel modeloTabla;

    public PanelArbitros(JFrame ventanaPrincipal, GestorDatos gestor) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.gestor = gestor;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(6, 6));

        String[] columnas = {"#", "Nombre", "Nacionalidad", "Categoria"};
        modeloTabla = new ArregloTableModel(columnas, new String[0][columnas.length]);
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevo = new JButton("Nuevo Arbitro");
        btnNuevo.addActionListener(e -> nuevoArbitro());
        JButton btnEditar = new JButton("Editar Arbitro Seleccionado");
        btnEditar.addActionListener(e -> editarArbitro());
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> refrescar());
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnRefrescar);
        add(panelBotones, BorderLayout.NORTH);
    }

    private void nuevoArbitro() {
        if (!gestor.isTorneoDimensionado()) {
            JOptionPane.showMessageDialog(this, "Primero debe seleccionar el tamanio del mundial en la pestana Configuracion.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!gestor.hayEspacioParaArbitro()) {
            JOptionPane.showMessageDialog(this, "Ya se registraron los " + gestor.getArbitros().length + " arbitros permitidos para este mundial.", "Arreglo lleno", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Arbitro nuevo = new Arbitro();
        DialogoArbitro dialogo = new DialogoArbitro(ventanaPrincipal, nuevo, true);
        dialogo.setVisible(true);
        if (dialogo.isGuardado()) {
            gestor.agregarArbitro(nuevo);
            refrescar();
        }
    }

    private void editarArbitro() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= gestor.getTotalArbitros()) {
            JOptionPane.showMessageDialog(this, "Seleccione un arbitro registrado de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Arbitro arbitro = gestor.getArbitros()[fila];
        DialogoArbitro dialogo = new DialogoArbitro(ventanaPrincipal, arbitro, false);
        dialogo.setVisible(true);
        if (dialogo.isGuardado()) {
            gestor.actualizarArbitro(fila, arbitro);
        }
        refrescar();
    }

    public void refrescar() {
        int total = gestor.getTotalArbitros();
        String[][] matriz = new String[total][4];
        Arbitro[] arbitros = gestor.getArbitros();
        for (int i = 0; i < total; i++) {
            Arbitro a = arbitros[i];
            matriz[i][0] = String.valueOf(i + 1);
            matriz[i][1] = a.getNombre();
            matriz[i][2] = a.getNacionalidad();
            matriz[i][3] = a.getCategoria();
        }
        modeloTabla.actualizarDatos(matriz);
    }
}
