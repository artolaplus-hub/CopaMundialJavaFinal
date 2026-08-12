/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package copamundial.vista;

/**
 *
 * @author fab
 */
// Interfaz del Modulo 2: Sorteo y Fase de Grupos - Fabricio
import copamundial.datos.GestorDatos;
import copamundial.modelo.Pais;
import copamundial.util.ArregloTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class PanelGrupos extends JPanel {

    private static final String[] COLUMNAS =
            {"Pos", "Pais", "PJ", "PG", "PE", "PP", "GF", "GC", "DG", "Pts"};

    private final GestorDatos gestor;
    private final JPanel contenedorGrupos = new JPanel();
    private final JLabel estado = new JLabel("Registre o genere los paises y presione \"Realizar Sorteo de Grupos\".");

    public PanelGrupos(GestorDatos gestor) {
        this.gestor = gestor;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel superior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSorteo = new JButton("Realizar Sorteo de Grupos");
        superior.add(btnSorteo);
        superior.add(estado);
        add(superior, BorderLayout.NORTH);

        contenedorGrupos.setLayout(new GridLayout(0, 2, 10, 10));
        add(new JScrollPane(contenedorGrupos), BorderLayout.CENTER);

        btnSorteo.addActionListener(e -> realizarSorteo());
    }

    private void realizarSorteo() {
        if (!gestor.isTorneoDimensionado()) {
            JOptionPane.showMessageDialog(this,
                    "Primero dimensione el torneo en la pestana \"Configuracion\".",
                    "Torneo no dimensionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (gestor.getTotalPaises() != gestor.getTamanioMundial()) {
            JOptionPane.showMessageDialog(this,
                    "Faltan paises por registrar (" + gestor.getTotalPaises() + " de "
                    + gestor.getTamanioMundial() + ").\n"
                    + "Puede completarlos manualmente o usar \"Generar Datos de Demostracion\".",
                    "Paises incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        gestor.realizarSorteoGrupos();
        dibujarGrupos();
        estado.setText("Sorteo realizado: " + gestor.getCantidadGrupos() + " grupos de "
                + GestorDatos.EQUIPOS_POR_GRUPO + " equipos cada uno.");
    }

    private void dibujarGrupos() {
        contenedorGrupos.removeAll();
        Pais[][] grupos = gestor.getGrupos();
        for (int g = 0; g < grupos.length; g++) {
            contenedorGrupos.add(construirTablaGrupo(g, grupos[g]));
        }
        contenedorGrupos.revalidate();
        contenedorGrupos.repaint();
    }

    private JPanel construirTablaGrupo(int numeroGrupo, Pais[] equipos) {
        String[][] datos = new String[equipos.length][COLUMNAS.length];
        for (int i = 0; i < equipos.length; i++) {
            datos[i][0] = String.valueOf(i + 1);
            datos[i][1] = equipos[i].getNombre();
            for (int columna = 2; columna < COLUMNAS.length; columna++) {
                datos[i][columna] = "0";
            }
        }
        ArregloTableModel modelo = new ArregloTableModel(COLUMNAS, datos);
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(22);

        JPanel panelGrupo = new JPanel(new BorderLayout());
        panelGrupo.setBorder(BorderFactory.createTitledBorder("Grupo " + (char) ('A' + numeroGrupo)));
        panelGrupo.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panelGrupo;
    }

    public void invalidarSorteo() {
        contenedorGrupos.removeAll();
        contenedorGrupos.revalidate();
        contenedorGrupos.repaint();
        estado.setText("Los datos cambiaron. Realice el sorteo nuevamente.");
    }
}