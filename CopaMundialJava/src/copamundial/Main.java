package copamundial;

import copamundial.vista.VentanaPrincipal;
import copamundial.vista.TemaMundial;
import javax.swing.SwingUtilities;

/**
 * Punto de entrada del Sistema de Gestion y Simulacion "Copa Mundial Java".
 * Modulo 1: Administracion y Parametros Iniciales.
 */
public class Main {

    public static void main(String[] args) {
        TemaMundial.instalar();

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
