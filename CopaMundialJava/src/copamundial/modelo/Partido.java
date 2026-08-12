package copamundial.modelo;

import java.util.Random;

// Partido del calendario; los eventos se almacenan en arreglos fijos — Benjamin Roque
// MODULO 6: se actualizan las estadisticas individuales de cada Jugador y
// se genera asistencia/dinero recaudado por partido para el resumen financiero.
public class Partido {

    private static final int MAX_EVENTOS = 24;

    private final PosicionGrupo local;
    private final PosicionGrupo visitante;
    private final int numeroGrupo;
    private final String[] eventos = new String[MAX_EVENTOS];
    private int cantidadEventos;
    private int golesLocal;
    private int golesVisitante;
    private boolean jugado;

    // ---------- Campos de estadisticas Modulo 6 ----------
    private int asistencia;          // aficionados presentes en el partido
    private long dineroRecaudado;    // colones recaudados por venta de entradas

    public Partido(PosicionGrupo local, PosicionGrupo visitante, int numeroGrupo) {
        this.local = local;
        this.visitante = visitante;
        this.numeroGrupo = numeroGrupo;
    }

    public void simular(Random random) {
        if (jugado) return;

        golesLocal    = random.nextInt(6);
        golesVisitante = random.nextInt(6);

        registrarGoles(local.getPais(),     golesLocal,     random);
        registrarGoles(visitante.getPais(), golesVisitante, random);
        registrarTarjetas(local.getPais(),     random);
        registrarTarjetas(visitante.getPais(), random);

        local.registrarResultado(golesLocal,     golesVisitante);
        visitante.registrarResultado(golesVisitante, golesLocal);

        // --- Modulo 6: generar asistencia y dinero recaudado ---
        // Fase de grupos: entre 30 000 y 70 000 aficionados, precio ₡10 000
        asistencia     = 30000 + random.nextInt(40001);
        dineroRecaudado = (long) asistencia * 10000L;

        jugado = true;
    }

    // Registra los goles de un equipo: guarda el evento de texto Y
    // actualiza el contador de goles del objeto Jugador correspondiente.
    private void registrarGoles(Pais pais, int cantidad, Random random) {
        for (int i = 0; i < cantidad; i++) {
            Jugador jugador = jugadorAleatorio(pais, random);
            agregarEvento("GOL - " + nombreJugador(jugador, pais));
            // Modulo 6: acumular el gol en el jugador real
            if (jugador != null) {
                jugador.agregarGol();
            }
        }
    }

    // Registra las tarjetas de un equipo: guarda el evento de texto Y
    // actualiza los contadores de tarjetas del objeto Jugador correspondiente.
    private void registrarTarjetas(Pais pais, Random random) {
        int amarillas = random.nextInt(5);
        int rojas     = random.nextInt(2);

        for (int i = 0; i < amarillas; i++) {
            Jugador jugador = jugadorAleatorio(pais, random);
            agregarEvento("Amarilla - " + nombreJugador(jugador, pais));
            // Modulo 6: acumular tarjeta amarilla en el jugador real
            if (jugador != null) {
                jugador.agregarTarjetaAmarilla();
            }
        }
        for (int i = 0; i < rojas; i++) {
            Jugador jugador = jugadorAleatorio(pais, random);
            agregarEvento("Roja - " + nombreJugador(jugador, pais));
            // Modulo 6: acumular tarjeta roja en el jugador real
            if (jugador != null) {
                jugador.agregarTarjetaRoja();
            }
        }
    }

    private Jugador jugadorAleatorio(Pais pais, Random random) {
        if (pais.getCantidadJugadores() == 0) return null;
        return pais.getJugadores()[random.nextInt(pais.getCantidadJugadores())];
    }

    private String nombreJugador(Jugador jugador, Pais pais) {
        return (jugador == null ? "Jugador sin registrar" : jugador.getNombre())
                + " (" + pais.getNombre() + ")";
    }

    private void agregarEvento(String evento) {
        if (cantidadEventos < eventos.length) eventos[cantidadEventos++] = evento;
    }

    public String obtenerResumen() {
        String texto = "Grupo " + (char) ('A' + numeroGrupo) + ": "
                + local.getPais().getNombre() + " " + golesLocal + " - "
                + golesVisitante + " " + visitante.getPais().getNombre() + "\n";
        for (int i = 0; i < cantidadEventos; i++) texto += "   " + eventos[i] + "\n";
        return texto;
    }

    public boolean isJugado()          { return jugado; }
    public PosicionGrupo getLocal()    { return local; }
    public PosicionGrupo getVisitante() { return visitante; }
    public int getNumeroGrupo()        { return numeroGrupo; }

    // ---------- Getters Modulo 6 ----------
    public int getAsistencia()         { return asistencia; }
    public long getDineroRecaudado()   { return dineroRecaudado; }
}
