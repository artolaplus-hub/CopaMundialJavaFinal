package copamundial.util;

import javax.swing.table.AbstractTableModel;

/**
 * Modelo de tabla para JTable respaldado UNICAMENTE por una matriz
 * (String[][]) y un arreglo de nombres de columnas (String[]).
 *
 * Se implementa manualmente en lugar de usar DefaultTableModel porque
 * este ultimo administra sus datos internamente con un Vector, lo cual
 * viola la restriccion de "solo arreglos y matrices" para la gestion de
 * datos del sistema. AbstractTableModel unicamente aporta el mecanismo
 * de notificacion de Swing, no almacena datos por si mismo.
 */
public class ArregloTableModel extends AbstractTableModel {

    private String[] columnas;
    private String[][] datos;

    public ArregloTableModel(String[] columnas, String[][] datosIniciales) {
        this.columnas = columnas;
        this.datos = (datosIniciales != null) ? datosIniciales : new String[0][columnas.length];
    }

    public void actualizarDatos(String[][] nuevosDatos) {
        this.datos = (nuevosDatos != null) ? nuevosDatos : new String[0][columnas.length];
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return datos.length;
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int columna) {
        return columnas[columna];
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        return datos[fila][columna];
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }
}
