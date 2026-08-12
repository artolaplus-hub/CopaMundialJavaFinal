package copamundial.modelo;

/**
 * Representa un pais participante del Mundial.
 * El plantel de jugadores y el cuerpo tecnico se manejan con
 * ARREGLOS DE TAMANIO FIJO (nada de ArrayList/List).
 */
public class Pais {

    public static final int MAX_JUGADORES = 23;
    public static final int MAX_CUERPO_TECNICO = 5;

    private String nombre;
    private String siglas;
    private String confederacion; // UEFA, CONMEBOL, CONCACAF, AFC, CAF, OFC

    private Jugador[] jugadores;
    private int cantidadJugadores;

    private MiembroCuerpoTecnico[] cuerpoTecnico;
    private int cantidadCuerpoTecnico;

    public Pais() {
        this.nombre = "";
        this.siglas = "";
        this.confederacion = "";
        this.jugadores = new Jugador[MAX_JUGADORES];
        this.cantidadJugadores = 0;
        this.cuerpoTecnico = new MiembroCuerpoTecnico[MAX_CUERPO_TECNICO];
        this.cantidadCuerpoTecnico = 0;
    }

    public Pais(String nombre, String siglas, String confederacion) {
        this();
        this.nombre = nombre;
        this.siglas = siglas;
        this.confederacion = confederacion;
    }

    // ---------- Datos generales ----------

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSiglas() {
        return siglas;
    }

    public void setSiglas(String siglas) {
        this.siglas = siglas;
    }

    public String getConfederacion() {
        return confederacion;
    }

    public void setConfederacion(String confederacion) {
        this.confederacion = confederacion;
    }

    // ---------- Gestion de jugadores (arreglo fijo) ----------

    public Jugador[] getJugadores() {
        return jugadores;
    }

    public int getCantidadJugadores() {
        return cantidadJugadores;
    }

    /**
     * Agrega un jugador en el siguiente espacio libre del arreglo.
     * @return true si se pudo agregar, false si el arreglo esta lleno.
     */
    public boolean agregarJugador(Jugador j) {
        if (cantidadJugadores >= jugadores.length) {
            return false;
        }
        jugadores[cantidadJugadores] = j;
        cantidadJugadores++;
        return true;
    }

    /**
     * Reemplaza el jugador en la posicion indicada del arreglo.
     */
    public boolean actualizarJugador(int indice, Jugador j) {
        if (indice < 0 || indice >= cantidadJugadores) {
            return false;
        }
        jugadores[indice] = j;
        return true;
    }

    /**
     * Elimina un jugador desplazando manualmente el arreglo (sin colecciones dinamicas).
     */
    public boolean eliminarJugador(int indice) {
        if (indice < 0 || indice >= cantidadJugadores) {
            return false;
        }
        for (int i = indice; i < cantidadJugadores - 1; i++) {
            jugadores[i] = jugadores[i + 1];
        }
        jugadores[cantidadJugadores - 1] = null;
        cantidadJugadores--;
        return true;
    }

    // ---------- Gestion de cuerpo tecnico (arreglo fijo) ----------

    public MiembroCuerpoTecnico[] getCuerpoTecnico() {
        return cuerpoTecnico;
    }

    public int getCantidadCuerpoTecnico() {
        return cantidadCuerpoTecnico;
    }

    public boolean agregarMiembroCuerpoTecnico(MiembroCuerpoTecnico m) {
        if (cantidadCuerpoTecnico >= cuerpoTecnico.length) {
            return false;
        }
        cuerpoTecnico[cantidadCuerpoTecnico] = m;
        cantidadCuerpoTecnico++;
        return true;
    }

    public boolean actualizarMiembroCuerpoTecnico(int indice, MiembroCuerpoTecnico m) {
        if (indice < 0 || indice >= cantidadCuerpoTecnico) {
            return false;
        }
        cuerpoTecnico[indice] = m;
        return true;
    }

    public boolean eliminarMiembroCuerpoTecnico(int indice) {
        if (indice < 0 || indice >= cantidadCuerpoTecnico) {
            return false;
        }
        for (int i = indice; i < cantidadCuerpoTecnico - 1; i++) {
            cuerpoTecnico[i] = cuerpoTecnico[i + 1];
        }
        cuerpoTecnico[cantidadCuerpoTecnico - 1] = null;
        cantidadCuerpoTecnico--;
        return true;
    }

    @Override
    public String toString() {
        return nombre + " (" + siglas + ")";
    }
}
