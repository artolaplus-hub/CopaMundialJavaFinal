package copamundial.vista;

import copamundial.datos.GestorDatos;
import copamundial.modelo.Pais;
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
 * Panel del Modulo 1 para el registro y edicion de Paises.
 * Los datos que muestra la tabla siempre se reconstruyen a partir del
 * arreglo Pais[] de GestorDatos (nunca se guarda una copia dinamica aparte).
 */
public class PanelPaises extends JPanel {

    private final JFrame ventanaPrincipal;
    private final GestorDatos gestor;

    private JTable tabla;
    private ArregloTableModel modeloTabla;

    public PanelPaises(JFrame ventanaPrincipal, GestorDatos gestor) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.gestor = gestor;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(6, 6));

        String[] columnas = {"#", "Nombre", "Siglas", "Confederacion", "Jugadores", "Cuerpo Tecnico"};
        modeloTabla = new ArregloTableModel(columnas, new String[0][columnas.length]);
        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNuevo = new JButton("Nuevo Pais");
        btnNuevo.addActionListener(e -> nuevoPais());
        JButton btnEditar = new JButton("Editar Pais Seleccionado");
        btnEditar.addActionListener(e -> editarPais());
        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> refrescar());
        panelBotones.add(btnNuevo);
        panelBotones.add(btnEditar);
        panelBotones.add(btnRefrescar);
        add(panelBotones, BorderLayout.NORTH);
    }

    private void nuevoPais() {
        if (!gestor.isTorneoDimensionado()) {
            JOptionPane.showMessageDialog(this, "Primero debe seleccionar el tamanio del mundial en la pestana Configuracion.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!gestor.hayEspacioParaPais()) {
            JOptionPane.showMessageDialog(this, "Ya se registraron los " + gestor.getPaises().length + " paises permitidos para este mundial.", "Arreglo lleno", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Pais nuevo = new Pais();
        DialogoPais dialogo = new DialogoPais(ventanaPrincipal, nuevo, true);
        dialogo.setVisible(true);
        if (dialogo.isGuardado()) {
            gestor.agregarPais(nuevo);
            refrescar();
        }
    }

    private void editarPais() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= gestor.getTotalPaises()) {
            JOptionPane.showMessageDialog(this, "Seleccione un pais registrado de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Pais pais = gestor.getPaises()[fila];
        DialogoPais dialogo = new DialogoPais(ventanaPrincipal, pais, false);
        dialogo.setVisible(true);
        if (dialogo.isGuardado()) {
            gestor.actualizarPais(fila, pais);
        }
        refrescar();
    }

    public void refrescar() {
        int total = gestor.getTotalPaises();
        String[][] matriz = new String[total][6];
        Pais[] paises = gestor.getPaises();
        for (int i = 0; i < total; i++) {
            Pais p = paises[i];
            matriz[i][0] = String.valueOf(i + 1);
            matriz[i][1] = p.getNombre();
            matriz[i][2] = p.getSiglas();
            matriz[i][3] = p.getConfederacion();
            matriz[i][4] = p.getCantidadJugadores() + " / " + Pais.MAX_JUGADORES;
            matriz[i][5] = p.getCantidadCuerpoTecnico() + " / " + Pais.MAX_CUERPO_TECNICO;
        }
        modeloTabla.actualizarDatos(matriz);
    }
}
