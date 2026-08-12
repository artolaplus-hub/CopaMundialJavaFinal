package copamundial.modelo;

/**
 * Representa una sede/estadio del Mundial.
 */
public class Estadio {

    private String nombre;
    private String ciudad;
    private String paisSede;
    private int capacidad;

    public Estadio() {
        this.nombre = "";
        this.ciudad = "";
        this.paisSede = "";
        this.capacidad = 0;
    }

    public Estadio(String nombre, String ciudad, String paisSede, int capacidad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.paisSede = paisSede;
        this.capacidad = capacidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPaisSede() {
        return paisSede;
    }

    public void setPaisSede(String paisSede) {
        this.paisSede = paisSede;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    @Override
    public String toString() {
        return nombre + " - " + ciudad;
    }
}
