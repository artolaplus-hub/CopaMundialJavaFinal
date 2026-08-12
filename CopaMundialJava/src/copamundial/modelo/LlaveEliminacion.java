package copamundial.modelo;

import java.util.Random;

/**
 * Representa un partido de la fase de eliminacion directa (Modulo 5).
 * A diferencia de Partido (fase de grupos), aqui NO existen los empates:
 * si el marcador regular termina igualado, el ganador se define por penales.
 *
 * MODULO 6: se agregaron asistencia y dineroRecaudado, generados al simular.
 * La asistencia y el precio por boleto aumentan segun la importancia de la ronda.
 */
public class LlaveEliminacion {

    private Pais equipoLocal;
    private Pais equipoVisitante;
    private final String ronda;       // "Octavos de Final", "Cuartos de Final", "Semifinal", "Final"
    private final int numeroLlave;    // posicion del partido dentro de su ronda (0-index)

    private int golesLocal;
    private int golesVisitante;
    private boolean definidoPorPenales;
    private int penalesLocal;
    private int penalesVisitante;

    private Pais ganador;
    private boolean jugado;

    // ---------- Campos de estadisticas Modulo 6 ----------
    private int asistencia;
    private long dineroRecaudado;

    public LlaveEliminacion(Pais equipoLocal, Pais equipoVisitante, String ronda, int numeroLlave) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.ronda = ronda;
        this.numeroLlave = numeroLlave;
    }

    /**
     * Simula el partido. Si el marcador regular queda igualado se resuelve
     * por penales (en eliminacion directa no se permiten los empates).
     * MODULO 6: genera asistencia y dinero recaudado segun la ronda.
     */
    public void simular(Random random) {
        if (jugado || equipoLocal == null || equipoVisitante == null) {
            return;
        }
        golesLocal     = random.nextInt(5);
        golesVisitante = random.nextInt(5);

        if (golesLocal == golesVisitante) {
            definidoPorPenales = true;
            do {
                penalesLocal     = 3 + random.nextInt(4);
                penalesVisitante = 3 + random.nextInt(4);
            } while (penalesLocal == penalesVisitante);
            ganador = (penalesLocal > penalesVisitante) ? equipoLocal : equipoVisitante;
        } else {
            ganador = (golesLocal > golesVisitante) ? equipoLocal : equipoVisitante;
        }

        // --- Modulo 6: asistencia y dinero segun importancia de la ronda ---
        int baseAsistencia;
        int variacionAsistencia;
        long precioBoleto;

        if (ronda.equals("Final")) {
            baseAsistencia     = 75000;
            variacionAsistencia = 15001;
            precioBoleto       = 50000L;
        } else if (ronda.equals("Semifinal")) {
            baseAsistencia     = 60000;
            variacionAsistencia = 20001;
            precioBoleto       = 35000L;
        } else if (ronda.equals("Cuartos de Final")) {
            baseAsistencia     = 50000;
            variacionAsistencia = 25001;
            precioBoleto       = 25000L;
        } else {
            // Octavos, Dieciseisavos, otras rondas
            baseAsistencia     = 40000;
            variacionAsistencia = 30001;
            precioBoleto       = 20000L;
        }
        asistencia      = baseAsistencia + random.nextInt(variacionAsistencia);
        dineroRecaudado = (long) asistencia * precioBoleto;

        jugado = true;
    }

    public String obtenerResumen() {
        String texto = ronda + " - Llave " + (numeroLlave + 1) + ": "
                + equipoLocal.getNombre() + " " + golesLocal + " - "
                + golesVisitante + " " + equipoVisitante.getNombre();
        if (definidoPorPenales) {
            texto += " (penales " + penalesLocal + "-" + penalesVisitante + ")";
        }
        texto += "  => Avanza: " + ganador.getNombre();
        return texto;
    }

    // ---------- Getters / setters ----------

    public Pais getEquipoLocal()      { return equipoLocal; }
    public Pais getEquipoVisitante()  { return equipoVisitante; }

    public void definirEquipos(Pais local, Pais visitante) {
        this.equipoLocal    = local;
        this.equipoVisitante = visitante;
    }

    public String getRonda()             { return ronda; }
    public int getNumeroLlave()          { return numeroLlave; }
    public int getGolesLocal()           { return golesLocal; }
    public int getGolesVisitante()       { return golesVisitante; }
    public boolean isDefinidoPorPenales() { return definidoPorPenales; }
    public int getPenalesLocal()          { return penalesLocal; }
    public int getPenalesVisitante()      { return penalesVisitante; }
    public Pais getGanador()             { return ganador; }
    public boolean isJugado()            { return jugado; }
    public boolean tieneEquiposDefinidos() { return equipoLocal != null && equipoVisitante != null; }

    // ---------- Getters Modulo 6 ----------
    public int getAsistencia()           { return asistencia; }
    public long getDineroRecaudado()     { return dineroRecaudado; }
}
