package copamundial.vista;

import copamundial.datos.GestorDatos;
import copamundial.modelo.Estadio;
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
 * Panel del Modulo 1 para el registro y edicion de Sedes/Estadios.
 */
public class PanelEstadios extends JPanel {

    private final JFrame ventanaPrincipal;
    private final GestorDatos gestor;

    private JTable tabla;
    private ArregloTableModel modeloTabla;

    public PanelEstadios(JFrame ventanaPrincipal, GestorDatos gestor) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.gestor = gestor;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(6, 6));

        String[] columnas = {"#", "Nombre del Estadio", "Ciudad", "Pais Sede", "Capacidad"};
        modeloTabla = new ArregloTableModel(columnas, new String[0][columnas.length]);
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevo = new JButton("Nueva Sede");
        btnNuevo.addActionListener(e -> nuevaSede());
        JButton btnEditar = new JButton("Editar Sede Seleccionada");
        btnEditar.addActionListener(e -> editarSede());
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> refrescar());
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnRefrescar);
        add(panelBotones, BorderLayout.NORTH);
    }

    private void nuevaSede() {
        if (!gestor.isTorneoDimensionado()) {
            JOptionPane.showMessageDialog(this, "Primero debe seleccionar el tamanio del mundial en la pestana Configuracion.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!gestor.hayEspacioParaEstadio()) {
            JOptionPane.showMessageDialog(this, "Ya se registraron las " + gestor.getEstadios().length + " sedes permitidas para este mundial.", "Arreglo lleno", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Estadio nuevo = new Estadio();
        DialogoEstadio dialogo = new DialogoEstadio(ventanaPrincipal, nuevo, true);
        dialogo.setVisible(true);
        if (dialogo.isGuardado()) {
            gestor.agregarEstadio(nuevo);
            refrescar();
        }
    }

    private void editarSede() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= gestor.getTotalEstadios()) {
            JOptionPane.showMessageDialog(this, "Seleccione una sede registrada de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Estadio estadio = gestor.getEstadios()[fila];
        DialogoEstadio dialogo = new DialogoEstadio(ventanaPrincipal, estadio, false);
        dialogo.setVisible(true);
        if (dialogo.isGuardado()) {
            gestor.actualizarEstadio(fila, estadio);
        }
        refrescar();
    }

    public void refrescar() {
        int total = gestor.getTotalEstadios();
        String[][] matriz = new String[total][5];
        Estadio[] estadios = gestor.getEstadios();
        for (int i = 0; i < total; i++) {
            Estadio e = estadios[i];
            matriz[i][0] = String.valueOf(i + 1);
            matriz[i][1] = e.getNombre();
            matriz[i][2] = e.getCiudad();
            matriz[i][3] = e.getPaisSede();
            matriz[i][4] = String.valueOf(e.getCapacidad());
        }
        modeloTabla.actualizarDatos(matriz);
    }
}
