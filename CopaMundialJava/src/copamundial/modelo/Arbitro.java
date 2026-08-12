package copamundial.modelo;

/**
 * Representa a un miembro del cuerpo arbitral del Mundial.
 */
public class Arbitro {

    private String nombre;
    private String nacionalidad;
    private String categoria; // Central, Asistente 1, Asistente 2, VAR

    public Arbitro() {
        this.nombre = "";
        this.nacionalidad = "";
        this.categoria = "";
    }

    public Arbitro(String nombre, String nacionalidad, String categoria) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return nombre + " (" + categoria + ") - " + nacionalidad;
    }
}
