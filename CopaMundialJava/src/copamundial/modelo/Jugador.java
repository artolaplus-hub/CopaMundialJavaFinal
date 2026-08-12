package copamundial.modelo;

/**
 * Representa a un jugador de futbol dentro del plantel de un pais.
 * Estructura de datos primitiva: no depende de ninguna coleccion dinamica.
 *
 * MODULO 6 — Se agregaron contadores de estadisticas individuales:
 *   goles, tarjetasAmarillas, tarjetasRojas.
 * Estos se acumulan durante la simulacion (Partido.simular()) y se leen
 * en PanelEstadisticas para construir el TOP 5 y el reporte disciplinario.
 */
public class Jugador {

    private String nombre;
    private int dorsal;
    private String posicion; // Portero, Defensa, Mediocampista, Delantero
    private int edad;

    // ---------- Campos de estadisticas (Modulo 6) ----------
    private int goles;
    private int tarjetasAmarillas;
    private int tarjetasRojas;

    public Jugador() {
        this.nombre = "";
        this.dorsal = 0;
        this.posicion = "";
        this.edad = 18;
    }

    public Jugador(String nombre, int dorsal, String posicion, int edad) {
        this.nombre = nombre;
        this.dorsal = dorsal;
        this.posicion = posicion;
        this.edad = edad;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getDorsal() { return dorsal; }
    public void setDorsal(int dorsal) { this.dorsal = dorsal; }

    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    // ---------- Estadisticas (Modulo 6) ----------

    public int getGoles() { return goles; }
    public int getTarjetasAmarillas() { return tarjetasAmarillas; }
    public int getTarjetasRojas() { return tarjetasRojas; }

    /** Suma un gol al contador del jugador. Llamado por Partido.simular(). */
    public void agregarGol() { goles++; }

    /** Suma una tarjeta amarilla. Llamado por Partido.simular(). */
    public void agregarTarjetaAmarilla() { tarjetasAmarillas++; }

    /** Suma una tarjeta roja. Llamado por Partido.simular(). */
    public void agregarTarjetaRoja() { tarjetasRojas++; }

    /**
     * Devuelve el total de infracciones (amarillas + rojas).
     * Usado para ordenar el reporte disciplinario.
     */
    public int getTotalInfracciones() { return tarjetasAmarillas + tarjetasRojas; }

    @Override
    public String toString() {
        return dorsal + " - " + nombre + " (" + posicion + ")";
    }
}
