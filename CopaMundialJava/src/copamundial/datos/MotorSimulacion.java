package copamundial.datos;

import copamundial.modelo.Pais;
import copamundial.modelo.Partido;
import copamundial.modelo.PosicionGrupo;
import java.util.Random;

// Calendario y motor de simulacion con Arreglos — Benjamin Roque
// MODULO 6: se expone getCalendario() para que PanelEstadisticas pueda
// recorrer los partidos y sumar asistencia y dinero recaudado.
public class MotorSimulacion {

    public static final int EQUIPOS_POR_GRUPO = 4;
    private PosicionGrupo[] posiciones = new PosicionGrupo[0];
    private Partido[] calendario = new Partido[0];
    private int siguientePartido;
    private int cantidadGrupos;
    private final Random random = new Random();

    public boolean preparar(GestorDatos gestor) {
        int total = gestor.getTotalPaises();
        if (total < EQUIPOS_POR_GRUPO || total % EQUIPOS_POR_GRUPO != 0) return false;

        cantidadGrupos = total / EQUIPOS_POR_GRUPO;
        posiciones = new PosicionGrupo[total];
        for (int i = 0; i < total; i++) {
            posiciones[i] = new PosicionGrupo(gestor.getPaises()[i], i / EQUIPOS_POR_GRUPO);
        }

        calendario = new Partido[cantidadGrupos * 6];
        int indicePartido = 0;
        for (int grupo = 0; grupo < cantidadGrupos; grupo++) {
            int inicio = grupo * EQUIPOS_POR_GRUPO;
            for (int i = 0; i < EQUIPOS_POR_GRUPO - 1; i++) {
                for (int j = i + 1; j < EQUIPOS_POR_GRUPO; j++) {
                    calendario[indicePartido++] = new Partido(
                            posiciones[inicio + i], posiciones[inicio + j], grupo);
                }
            }
        }
        siguientePartido = 0;
        return true;
    }

    public Partido simularSiguiente() {
        if (siguientePartido >= calendario.length) return null;
        Partido partido = calendario[siguientePartido++];
        partido.simular(random);
        return partido;
    }

    public String simularFaseCompleta() {
        String resultado = "";
        Partido partido;
        while ((partido = simularSiguiente()) != null) resultado += partido.obtenerResumen() + "\n";
        return resultado;
    }

    public PosicionGrupo[] obtenerTablaGrupo(int grupo) {
        PosicionGrupo[] tabla = new PosicionGrupo[EQUIPOS_POR_GRUPO];
        int inicio = grupo * EQUIPOS_POR_GRUPO;
        for (int i = 0; i < EQUIPOS_POR_GRUPO; i++) tabla[i] = posiciones[inicio + i];
        for (int i = 0; i < tabla.length - 1; i++) {
            for (int j = 0; j < tabla.length - 1 - i; j++) {
                if (vaDespues(tabla[j], tabla[j + 1])) {
                    PosicionGrupo temporal = tabla[j];
                    tabla[j] = tabla[j + 1];
                    tabla[j + 1] = temporal;
                }
            }
        }
        return tabla;
    }

    private boolean vaDespues(PosicionGrupo a, PosicionGrupo b) {
        if (a.getPuntos()    != b.getPuntos())    return a.getPuntos()    < b.getPuntos();
        if (a.getDiferencia() != b.getDiferencia()) return a.getDiferencia() < b.getDiferencia();
        return a.getGolesFavor() < b.getGolesFavor();
    }

    public int getCantidadGrupos()   { return cantidadGrupos; }
    public int getSiguientePartido() { return siguientePartido; }
    public int getTotalPartidos()    { return calendario.length; }
    public boolean estaPreparado()   { return calendario.length > 0; }
    public boolean estaFinalizado()  { return estaPreparado() && siguientePartido == calendario.length; }

    /**
     * Devuelve el arreglo completo de partidos de la fase de grupos.
     * Usado por PanelEstadisticas (Modulo 6) para calcular asistencia y
     * dinero recaudado de la fase de grupos.
     */
    public Partido[] getCalendario() { return calendario; }
}
