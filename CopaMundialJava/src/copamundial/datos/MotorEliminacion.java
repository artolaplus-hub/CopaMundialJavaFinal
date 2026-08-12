/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package copamundial.datos;

import copamundial.modelo.LlaveEliminacion;
import copamundial.modelo.Pais;
import copamundial.modelo.PosicionGrupo;
import java.util.Random;

/**
 * Motor del Modulo 5: Fase de Eliminacion Directa y Mejores Terceros.
 *
 * Todo se maneja con arreglos de tamanio fijo (nada de List/Collection):
 *   - clasificados[]  : equipos que avanzan a la llave, ya ordenados para el sorteo.
 *   - llaves[]         : TODOS los partidos de eliminacion directa (todas las rondas),
 *                         guardados de forma consecutiva en un unico arreglo.
 *   - partidosPorRonda[] / nombresRondas[] : describen donde empieza y termina cada ronda
 *                         dentro del arreglo "llaves".
 *
 * Criterio de armado de la llave (una vez se tienen los clasificados en el orden
 * 1eros -> 2dos -> mejores terceros): se emparejan con la regla clasica de
 * seeding "1 vs ultimo, 2 vs penultimo, ...". Esto arma una llave completa y
 * determinista sin repetir equipos, separando en la medida de lo posible a los
 * punteros de cada grupo (quedan a la mayor distancia posible dentro del arreglo).
 */
public class MotorEliminacion {

    private static final int EQUIPOS_POR_GRUPO = 4;

    private final Random random = new Random();

    private Pais[] clasificados;               // equipos que entran a la llave (16 o 32)
    private LlaveEliminacion[] llaves;          // todos los partidos de todas las rondas
    private int[] partidosPorRonda;             // cantidad de partidos en cada ronda
    private String[] nombresRondas;             // nombre de cada ronda

    private int siguienteLlave;                 // puntero secuencial dentro de "llaves"
    private boolean iniciado;

    private Pais campeon;
    private Pais subcampeon;

    // ---------------- Iniciar la fase eliminatoria ----------------

    /**
     * Arma la llave a partir de las tablas finales de la fase de grupos.
     * Requiere que la fase de grupos (Modulo 4) ya haya finalizado.
     */
    public boolean iniciar(GestorDatos gestor, MotorSimulacion motorGrupos) {
        if (!motorGrupos.estaFinalizado()) {
            return false;
        }

        int cantidadGrupos = motorGrupos.getCantidadGrupos();
        int tamanioMundial = gestor.getTamanioMundial();

        int totalClasificados = calcularTotalClasificados(tamanioMundial);
        int mejoresTercerosNecesarios = calcularMejoresTercerosNecesarios(tamanioMundial);
        if (totalClasificados == 0) {
            return false;
        }

        // 1) Obtener 1ros y 2dos de cada grupo (la tabla ya viene ordenada por el motor de grupos)
        Pais[] primeros = new Pais[cantidadGrupos];
        Pais[] segundos = new Pais[cantidadGrupos];
        PosicionGrupo[] terceros = new PosicionGrupo[cantidadGrupos];

        for (int g = 0; g < cantidadGrupos; g++) {
            PosicionGrupo[] tabla = motorGrupos.obtenerTablaGrupo(g);
            primeros[g] = tabla[0].getPais();
            segundos[g] = tabla[1].getPais();
            terceros[g] = tabla[2];
        }

        // 2) Calcular mejores terceros (si el formato lo requiere)
        Pais[] mejoresTerceros = obtenerMejoresTerceros(terceros, mejoresTercerosNecesarios);

        // 3) Armar el arreglo de clasificados en orden: 1ros, 2dos, mejores terceros
        clasificados = new Pais[totalClasificados];
        int indice = 0;
        for (int i = 0; i < primeros.length; i++) clasificados[indice++] = primeros[i];
        for (int i = 0; i < segundos.length; i++) clasificados[indice++] = segundos[i];
        for (int i = 0; i < mejoresTerceros.length; i++) clasificados[indice++] = mejoresTerceros[i];

        // 4) Calcular la estructura de rondas (nombres y cantidad de partidos por ronda)
        construirEstructuraDeRondas(totalClasificados);

        // 5) Crear TODOS los partidos (los de rondas futuras se crean sin equipos aun)
        int totalLlaves = 0;
        for (int i = 0; i < partidosPorRonda.length; i++) totalLlaves += partidosPorRonda[i];
        llaves = new LlaveEliminacion[totalLlaves];

        int cursor = 0;
        for (int r = 0; r < partidosPorRonda.length; r++) {
            for (int p = 0; p < partidosPorRonda[r]; p++) {
                llaves[cursor] = new LlaveEliminacion(null, null, nombresRondas[r], p);
                cursor++;
            }
        }

        // 6) Emparejar la primera ronda con seeding "1 vs ultimo, 2 vs penultimo, ..."
        int partidosPrimeraRonda = partidosPorRonda[0];
        for (int p = 0; p < partidosPrimeraRonda; p++) {
            Pais local = clasificados[p];
            Pais visitante = clasificados[totalClasificados - 1 - p];
            llaves[p].definirEquipos(local, visitante);
        }

        siguienteLlave = 0;
        campeon = null;
        subcampeon = null;
        iniciado = true;
        return true;
    }

    private int calcularTotalClasificados(int tamanioMundial) {
        switch (tamanioMundial) {
            case 24: return 16;
            case 32: return 16;
            case 48: return 32;
            case 64: return 32;
            default: return 0;
        }
    }

    private int calcularMejoresTercerosNecesarios(int tamanioMundial) {
        switch (tamanioMundial) {
            case 24: return 4;
            case 48: return 8;
            default: return 0; // 32 y 64 equipos no usan mejores terceros
        }
    }

    /**
     * Ordena (algoritmo de burbuja manual, sin Arrays.sort ni Collections)
     * a todos los equipos que quedaron de terceros en su grupo, de mayor a
     * menor segun Puntos y, en caso de empate, Diferencia de Goles.
     * Devuelve solamente los "necesarios" mejores.
     */
    private Pais[] obtenerMejoresTerceros(PosicionGrupo[] terceros, int necesarios) {
        if (necesarios <= 0) {
            return new Pais[0];
        }

        // Copiamos a un arreglo de trabajo para no alterar el arreglo original
        PosicionGrupo[] ordenados = new PosicionGrupo[terceros.length];
        for (int i = 0; i < terceros.length; i++) ordenados[i] = terceros[i];

        // Ordenamiento burbuja descendente: Puntos, luego Diferencia de Goles, luego Goles a Favor
        for (int i = 0; i < ordenados.length - 1; i++) {
            for (int j = 0; j < ordenados.length - 1 - i; j++) {
                if (vaAntes(ordenados[j + 1], ordenados[j])) {
                    PosicionGrupo temporal = ordenados[j];
                    ordenados[j] = ordenados[j + 1];
                    ordenados[j + 1] = temporal;
                }
            }
        }

        int cantidad = Math.min(necesarios, ordenados.length);
        Pais[] resultado = new Pais[cantidad];
        for (int i = 0; i < cantidad; i++) resultado[i] = ordenados[i].getPais();
        return resultado;
    }

    /** true si "a" debe quedar en una posicion mejor (antes) que "b". */
    private boolean vaAntes(PosicionGrupo a, PosicionGrupo b) {
        if (a.getPuntos() != b.getPuntos()) return a.getPuntos() > b.getPuntos();
        if (a.getDiferencia() != b.getDiferencia()) return a.getDiferencia() > b.getDiferencia();
        return a.getGolesFavor() > b.getGolesFavor();
    }

    /** Calcula cuantas rondas hay y cuantos partidos/nombre tiene cada una. */
    private void construirEstructuraDeRondas(int totalClasificados) {
        int cantidadRondas = 0;
        int equipos = totalClasificados;
        while (equipos > 1) {
            equipos /= 2;
            cantidadRondas++;
        }

        partidosPorRonda = new int[cantidadRondas];
        nombresRondas = new String[cantidadRondas];

        equipos = totalClasificados;
        for (int r = 0; r < cantidadRondas; r++) {
            int partidos = equipos / 2;
            partidosPorRonda[r] = partidos;
            nombresRondas[r] = nombreDeRonda(equipos);
            equipos = partidos;
        }
    }

    private String nombreDeRonda(int equiposQueEntran) {
        switch (equiposQueEntran) {
            case 32: return "Dieciseisavos de Final";
            case 16: return "Octavos de Final";
            case 8:  return "Cuartos de Final";
            case 4:  return "Semifinal";
            case 2:  return "Final";
            default: return "Ronda de " + equiposQueEntran;
        }
    }

    // ---------------- Simulacion ----------------

    /** Simula unicamente el siguiente partido pendiente (modalidad "partido a partido"). */
    public LlaveEliminacion simularSiguiente() {
        if (!iniciado || estaFinalizado()) {
            return null;
        }
        LlaveEliminacion llave = llaves[siguienteLlave];
        if (!llave.tieneEquiposDefinidos()) {
            return null; // aun no se conoce el rival (falta que termine la ronda anterior)
        }
        llave.simular(random);
        siguienteLlave++;

        completarRondaSiTermino(llave.getRonda());
        return llave;
    }

    /** Simula todos los partidos pendientes de la fase (modalidad "fase completa"). */
    public String simularFaseCompleta() {
        StringBuilder resultado = new StringBuilder();
        LlaveEliminacion llave;
        while ((llave = simularSiguiente()) != null) {
            resultado.append(llave.obtenerResumen()).append("\n");
        }
        return resultado.toString();
    }

    /**
     * Cuando termina de jugarse el ultimo partido de una ronda, arma
     * automaticamente los enfrentamientos de la siguiente ronda con los
     * ganadores (equipo del partido 2k contra el del partido 2k+1).
     * Si la ronda que termino era la Final, se corona al campeon.
     */
    private void completarRondaSiTermino(String rondaQueSeJugo) {
        int inicioRonda = indiceInicioDeRonda(rondaQueSeJugo);
        int cantidad = partidosPorRonda[indiceDeRonda(rondaQueSeJugo)];

        for (int i = 0; i < cantidad; i++) {
            if (!llaves[inicioRonda + i].isJugado()) {
                return; // la ronda actual todavia no termina
            }
        }

        if (rondaQueSeJugo.equals("Final")) {
            LlaveEliminacion partidoFinal = llaves[inicioRonda];
            campeon = partidoFinal.getGanador();
            subcampeon = (partidoFinal.getGanador() == partidoFinal.getEquipoLocal())
                    ? partidoFinal.getEquipoVisitante() : partidoFinal.getEquipoLocal();
            return;
        }

        int indiceRondaSiguiente = indiceDeRonda(rondaQueSeJugo) + 1;
        int inicioRondaSiguiente = 0;
        for (int r = 0; r < indiceRondaSiguiente; r++) inicioRondaSiguiente += partidosPorRonda[r];

        for (int i = 0; i < partidosPorRonda[indiceRondaSiguiente]; i++) {
            Pais local = llaves[inicioRonda + (2 * i)].getGanador();
            Pais visitante = llaves[inicioRonda + (2 * i) + 1].getGanador();
            llaves[inicioRondaSiguiente + i].definirEquipos(local, visitante);
        }
    }

    private int indiceDeRonda(String nombreRonda) {
        for (int r = 0; r < nombresRondas.length; r++) {
            if (nombresRondas[r].equals(nombreRonda)) return r;
        }
        return -1;
    }

    private int indiceInicioDeRonda(String nombreRonda) {
        int r = indiceDeRonda(nombreRonda);
        int inicio = 0;
        for (int i = 0; i < r; i++) inicio += partidosPorRonda[i];
        return inicio;
    }

    // ---------------- Consultas para la interfaz ----------------

    public boolean isIniciado() { return iniciado; }

    public boolean estaFinalizado() { return iniciado && campeon != null; }

    public Pais getCampeon() { return campeon; }

    public Pais getSubcampeon() { return subcampeon; }

    public String[] getNombresRondas() { return nombresRondas; }

    /** Devuelve todas las llaves (partidos) de una ronda especifica. */
    public LlaveEliminacion[] obtenerLlavesDeRonda(String nombreRonda) {
        int r = indiceDeRonda(nombreRonda);
        if (r < 0) return new LlaveEliminacion[0];
        int inicio = indiceInicioDeRonda(nombreRonda);
        LlaveEliminacion[] resultado = new LlaveEliminacion[partidosPorRonda[r]];
        for (int i = 0; i < resultado.length; i++) resultado[i] = llaves[inicio + i];
        return resultado;
    }

    /** Devuelve TODAS las llaves de todas las rondas, en orden. Util para la tabla general. */
    public LlaveEliminacion[] obtenerTodasLasLlaves() {
        return llaves;
    }

    public int getSiguienteLlave() { return siguienteLlave; }
    public int getTotalLlaves() { return (llaves == null) ? 0 : llaves.length; }
}
