package copamundial.modelo;

// Estadisticas de un pais dentro de la fase de grupos Benjamin Roque
public class PosicionGrupo {

    private final Pais pais;
    private final int numeroGrupo;
    private int jugados;
    private int ganados;
    private int empatados;
    private int perdidos;
    private int golesFavor;
    private int golesContra;
    private int puntos;

    public PosicionGrupo(Pais pais, int numeroGrupo) {
        this.pais = pais;
        this.numeroGrupo = numeroGrupo;
    }

    public void registrarResultado(int favor, int contra) {
        jugados++;
        golesFavor += favor;
        golesContra += contra;
        if (favor > contra) {
            ganados++;
            puntos += 3;
        } else if (favor == contra) {
            empatados++;
            puntos++;
        } else {
            perdidos++;
        }
    }

    public Pais getPais() { return pais; }
    public int getNumeroGrupo() { return numeroGrupo; }
    public int getJugados() { return jugados; }
    public int getGanados() { return ganados; }
    public int getEmpatados() { return empatados; }
    public int getPerdidos() { return perdidos; }
    public int getGolesFavor() { return golesFavor; }
    public int getGolesContra() { return golesContra; }
    public int getDiferencia() { return golesFavor - golesContra; }
    public int getPuntos() { return puntos; }
}
