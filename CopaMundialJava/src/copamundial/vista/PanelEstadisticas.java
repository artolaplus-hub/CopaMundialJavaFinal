package copamundial.vista;

import copamundial.datos.GestorDatos;
import copamundial.datos.MotorEliminacion;
import copamundial.datos.MotorSimulacion;
import copamundial.modelo.Jugador;
import copamundial.modelo.LlaveEliminacion;
import copamundial.modelo.Pais;
import copamundial.modelo.Partido;
import copamundial.util.ArregloTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * MODULO 6 — Estadisticas Finales y Resumen del Torneo.
 *
 * Este panel solo puede calcular estadisticas cuando el torneo ha finalizado
 * completamente (ultima llave de la fase eliminatoria ya jugada).
 * Toda la informacion se extrae de los arreglos y objetos existentes en memoria:
 *   - GestorDatos : paises, estadios
 *   - MotorSimulacion : partidos de fase de grupos (Partido[])
 *   - MotorEliminacion: llaves de eliminacion (LlaveEliminacion[]), campeon, subcampeon
 *
 * NO se usan List, ArrayList, HashMap ni ninguna estructura dinamica prohibida.
 * Todos los calculos se realizan con arreglos simples (Jugador[], String[][], int[]).
 */
public class PanelEstadisticas extends JPanel {

    private final GestorDatos gestor;
    private final PanelSimulacion panelSimulacion;
    private final PanelEliminacion panelEliminacion;

    // Limite de jugadores para los arreglos de clasificacion
    private static final int MAX_JUGADORES_TORNEO = 64 * 23; // maximo posible

    // Componentes de la interfaz
    private final JLabel lblEstado = new JLabel("El torneo aun no ha finalizado. Complete la fase eliminatoria.", SwingConstants.CENTER);
    private final JButton btnCalcular = new JButton("Calcular / Actualizar Estadisticas Finales");

    // --- Pestana Coronacion ---
    private final JTextArea areaCorona = new JTextArea();

    // --- Pestana Bota de Oro ---
    private static final String[] COL_GOLEADORES = {"Posicion", "Jugador", "Equipo", "Goles"};
    private final ArregloTableModel modeloGoleadores = new ArregloTableModel(COL_GOLEADORES, new String[0][COL_GOLEADORES.length]);

    // --- Pestana Disciplina ---
    private static final String[] COL_DISCIPLINA = {"Jugador", "Equipo", "Amarillas", "Rojas", "Total Infracciones"};
    private final ArregloTableModel modeloDisciplina = new ArregloTableModel(COL_DISCIPLINA, new String[0][COL_DISCIPLINA.length]);

    // --- Pestana Finanzas ---
    private final JTextArea areaFinanzas = new JTextArea();

    public PanelEstadisticas(GestorDatos gestor, PanelSimulacion panelSimulacion, PanelEliminacion panelEliminacion) {
        this.gestor          = gestor;
        this.panelSimulacion = panelSimulacion;
        this.panelEliminacion = panelEliminacion;
        construirInterfaz();
        btnCalcular.setEnabled(false); // bloqueado hasta que el torneo termine
    }

    // Llamado por PanelEliminacion cuando se corona el campeon
    public void notificarTorneoFinalizado() {
        btnCalcular.setEnabled(true);
        lblEstado.setText("El torneo ha finalizado. Presione el boton para ver las estadisticas.");
        lblEstado.setForeground(new Color(0, 128, 0));
    }

    // Llamado por VentanaPrincipal cuando cambian datos (reinicio del torneo)
    public void invalidar() {
        btnCalcular.setEnabled(false);
        lblEstado.setText("El torneo aun no ha finalizado. Complete la fase eliminatoria.");
        lblEstado.setForeground(Color.BLACK);
        areaCorona.setText("");
        areaFinanzas.setText("");
        modeloGoleadores.actualizarDatos(new String[0][COL_GOLEADORES.length]);
        modeloDisciplina.actualizarDatos(new String[0][COL_DISCIPLINA.length]);
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // --- Panel superior: estado y boton ---
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1, 4, 4));
        lblEstado.setFont(lblEstado.getFont().deriveFont(Font.ITALIC, 13f));
        panelSuperior.add(lblEstado);
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnCalcular.setFont(btnCalcular.getFont().deriveFont(Font.BOLD, 13f));
        btnCalcular.addActionListener(e -> calcularEstadisticas());
        panelBoton.add(btnCalcular);
        panelSuperior.add(panelBoton);
        add(panelSuperior, BorderLayout.NORTH);

        // --- Pestanas internas ---
        JTabbedPane pestanas = new JTabbedPane();

        // 1. Coronacion
        areaCorona.setEditable(false);
        areaCorona.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        pestanas.addTab("Coronacion", new JScrollPane(areaCorona));

        // 2. Bota de Oro
        JTable tablaGoleadores = new JTable(modeloGoleadores);
        tablaGoleadores.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        tablaGoleadores.setRowHeight(22);
        pestanas.addTab("Bota de Oro - TOP 5", new JScrollPane(tablaGoleadores));

        // 3. Disciplina
        JTable tablaDisciplina = new JTable(modeloDisciplina);
        tablaDisciplina.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        tablaDisciplina.setRowHeight(22);
        pestanas.addTab("Reporte Disciplinario", new JScrollPane(tablaDisciplina));

        // 4. Finanzas
        areaFinanzas.setEditable(false);
        areaFinanzas.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        pestanas.addTab("Resumen Financiero", new JScrollPane(areaFinanzas));

        add(pestanas, BorderLayout.CENTER);
    }

    // =====================================================================
    // CALCULO PRINCIPAL
    // =====================================================================

    private void calcularEstadisticas() {
        MotorEliminacion motorElim = panelEliminacion.getMotor();
        MotorSimulacion  motorGrup = panelSimulacion.getMotor();

        if (!motorElim.estaFinalizado()) {
            JOptionPane.showMessageDialog(this,
                    "El torneo aun no ha finalizado. Complete todos los partidos de la fase eliminatoria.",
                    "Torneo incompleto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        mostrarCorona(motorElim);
        mostrarTop5Goleadores();
        mostrarReporteDisciplinario();
        mostrarResumenFinanciero(motorGrup, motorElim);

        lblEstado.setText("Estadisticas calculadas correctamente a partir de los datos del torneo.");
        lblEstado.setForeground(new Color(0, 100, 0));
    }

    // =====================================================================
    // 1. CORONACION
    // =====================================================================

    private void mostrarCorona(MotorEliminacion motorElim) {
        Pais campeon    = motorElim.getCampeon();
        Pais subcampeon = motorElim.getSubcampeon();

        String texto =
            "========================================\n" +
            "         COPA MUNDIAL JAVA\n" +
            "========================================\n\n" +
            "   ** CAMPEON DEL MUNDO **\n\n" +
            "     " + campeon.getNombre().toUpperCase() + "\n\n" +
            "========================================\n\n" +
            "   SUBCAMPEON:\n" +
            "     " + subcampeon.getNombre() + "\n\n" +
            "========================================\n";

        areaCorona.setText(texto);
    }

    // =====================================================================
    // 2. BOTA DE ORO — TOP 5 goleadores
    // =====================================================================

    private void mostrarTop5Goleadores() {
        // Arreglos paralelos: jugadores y nombre de su equipo
        // Se usan arreglos de tamanio maximo; se lleva un contador de ocupacion
        Jugador[] jugadores = new Jugador[MAX_JUGADORES_TORNEO];
        String[]  equipos   = new String[MAX_JUGADORES_TORNEO];
        int total = 0;

        // Recorrer todos los paises y sus plantillas
        int totalPaises = gestor.getTotalPaises();
        Pais[] paises   = gestor.getPaises();
        for (int p = 0; p < totalPaises; p++) {
            Pais pais = paises[p];
            int cantJugadores = pais.getCantidadJugadores();
            Jugador[] plantel = pais.getJugadores();
            for (int j = 0; j < cantJugadores; j++) {
                if (plantel[j] != null && plantel[j].getGoles() > 0) {
                    jugadores[total] = plantel[j];
                    equipos[total]   = pais.getNombre();
                    total++;
                }
            }
        }

        // Ordenar de mayor a menor goles (burbuja)
        for (int i = 0; i < total - 1; i++) {
            for (int j = 0; j < total - 1 - i; j++) {
                if (jugadores[j].getGoles() < jugadores[j + 1].getGoles()) {
                    Jugador tempJ = jugadores[j]; jugadores[j] = jugadores[j + 1]; jugadores[j + 1] = tempJ;
                    String  tempE = equipos[j];   equipos[j]   = equipos[j + 1];   equipos[j + 1]   = tempE;
                }
            }
        }

        // Tomar el TOP 5 (o menos si hay pocos goleadores)
        int top = Math.min(5, total);
        String[][] datos = new String[top][COL_GOLEADORES.length];
        for (int i = 0; i < top; i++) {
            datos[i][0] = String.valueOf(i + 1);
            datos[i][1] = jugadores[i].getNombre();
            datos[i][2] = equipos[i];
            datos[i][3] = jugadores[i].getGoles() + (jugadores[i].getGoles() == 1 ? " gol" : " goles");
        }
        modeloGoleadores.actualizarDatos(datos);
    }

    // =====================================================================
    // 3. REPORTE DISCIPLINARIO
    // =====================================================================

    private void mostrarReporteDisciplinario() {
        // Solo incluir jugadores que hayan recibido al menos una tarjeta
        Jugador[] jugadores = new Jugador[MAX_JUGADORES_TORNEO];
        String[]  equipos   = new String[MAX_JUGADORES_TORNEO];
        int total = 0;

        int totalPaises = gestor.getTotalPaises();
        Pais[] paises   = gestor.getPaises();
        for (int p = 0; p < totalPaises; p++) {
            Pais pais = paises[p];
            int cantJugadores = pais.getCantidadJugadores();
            Jugador[] plantel = pais.getJugadores();
            for (int j = 0; j < cantJugadores; j++) {
                if (plantel[j] != null && plantel[j].getTotalInfracciones() > 0) {
                    jugadores[total] = plantel[j];
                    equipos[total]   = pais.getNombre();
                    total++;
                }
            }
        }

        // Ordenar: primero por total de infracciones (desc), luego por rojas (desc)
        for (int i = 0; i < total - 1; i++) {
            for (int j = 0; j < total - 1 - i; j++) {
                boolean intercambiar = false;
                if (jugadores[j].getTotalInfracciones() < jugadores[j + 1].getTotalInfracciones()) {
                    intercambiar = true;
                } else if (jugadores[j].getTotalInfracciones() == jugadores[j + 1].getTotalInfracciones()
                        && jugadores[j].getTarjetasRojas() < jugadores[j + 1].getTarjetasRojas()) {
                    intercambiar = true;
                }
                if (intercambiar) {
                    Jugador tempJ = jugadores[j]; jugadores[j] = jugadores[j + 1]; jugadores[j + 1] = tempJ;
                    String  tempE = equipos[j];   equipos[j]   = equipos[j + 1];   equipos[j + 1]   = tempE;
                }
            }
        }

        String[][] datos = new String[total][COL_DISCIPLINA.length];
        for (int i = 0; i < total; i++) {
            datos[i][0] = jugadores[i].getNombre();
            datos[i][1] = equipos[i];
            datos[i][2] = String.valueOf(jugadores[i].getTarjetasAmarillas());
            datos[i][3] = String.valueOf(jugadores[i].getTarjetasRojas());
            datos[i][4] = String.valueOf(jugadores[i].getTotalInfracciones());
        }
        modeloDisciplina.actualizarDatos(datos);
    }

    // =====================================================================
    // 4. RESUMEN FINANCIERO Y ASISTENCIA
    // =====================================================================

    private void mostrarResumenFinanciero(MotorSimulacion motorGrup, MotorEliminacion motorElim) {
        long   dineroTotal     = 0;
        long   asistenciaTotal = 0;
        int    partidosFase    = 0;
        int    llavesElim      = 0;

        // Sumar partidos de la fase de grupos
        Partido[] calendario = motorGrup.getCalendario();
        for (int i = 0; i < calendario.length; i++) {
            if (calendario[i] != null && calendario[i].isJugado()) {
                dineroTotal     += calendario[i].getDineroRecaudado();
                asistenciaTotal += calendario[i].getAsistencia();
                partidosFase++;
            }
        }

        // Sumar llaves de la fase eliminatoria
        LlaveEliminacion[] llaves = motorElim.obtenerTodasLasLlaves();
        if (llaves != null) {
            for (int i = 0; i < llaves.length; i++) {
                if (llaves[i] != null && llaves[i].isJugado()) {
                    dineroTotal     += llaves[i].getDineroRecaudado();
                    asistenciaTotal += llaves[i].getAsistencia();
                    llavesElim++;
                }
            }
        }

        int totalPartidos = partidosFase + llavesElim;

        // Formatear numero con separadores de miles (manual, sin String.format ni Locale)
        String dineroStr     = formatearNumero(dineroTotal);
        String asistenciaStr = formatearNumero(asistenciaTotal);

        String texto =
            "========================================\n" +
            "        RESUMEN FINANCIERO FINAL\n" +
            "========================================\n\n" +
            "Partidos fase de grupos:   " + partidosFase + "\n" +
            "Partidos fase eliminatoria:" + llavesElim + "\n" +
            "Total de partidos jugados: " + totalPartidos + "\n\n" +
            "----------------------------------------\n" +
            "Dinero total recaudado:\n" +
            "\u20a1" + dineroStr + "\n\n" +
            "----------------------------------------\n" +
            "Asistencia total:\n" +
            asistenciaStr + " aficionados\n\n" +
            "========================================\n";

        areaFinanzas.setText(texto);
    }

    /**
     * Convierte un numero largo en una cadena con separadores de miles.
     * Ejemplo: 1234567890 -> "1,234,567,890"
     * Se implementa manualmente para no usar NumberFormat ni Locale.
     */
    private String formatearNumero(long numero) {
        String crudo = String.valueOf(numero);
        String resultado = "";
        int inicio = crudo.length() % 3;
        if (inicio > 0) {
            resultado = crudo.substring(0, inicio);
        }
        for (int i = inicio; i < crudo.length(); i += 3) {
            if (!resultado.isEmpty()) resultado += ",";
            resultado += crudo.substring(i, i + 3);
        }
        return resultado.isEmpty() ? "0" : resultado;
    }
}
