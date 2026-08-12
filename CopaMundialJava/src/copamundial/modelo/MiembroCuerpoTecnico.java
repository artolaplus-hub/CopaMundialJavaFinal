package copamundial.modelo;

/**
 * Representa a un integrante del cuerpo tecnico de un pais
 * (Director Tecnico, Asistente, Preparador Fisico, Medico, Analista, etc).
 */
public class MiembroCuerpoTecnico {

    private String nombre;
    private String cargo;
    private int edad;

    public MiembroCuerpoTecnico() {
        this.nombre = "";
        this.cargo = "";
        this.edad = 30;
    }

    public MiembroCuerpoTecnico(String nombre, String cargo, int edad) {
        this.nombre = nombre;
        this.cargo = cargo;
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    @Override
    public String toString() {
        return cargo + ": " + nombre;
    }
}
