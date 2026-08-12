package copamundial.datos;

import copamundial.modelo.Arbitro;
import copamundial.modelo.Estadio;
import copamundial.modelo.Pais;
import java.util.Random;


/**
 * Nucleo de la logica de negocio del Modulo 1.
 *
 * TODA la persistencia vive en memoria RAM, exclusivamente en arreglos.
 * No se utiliza ArrayList, List, HashMap, Set ni ninguna coleccion del
 * paquete java.util. El tamanio de los arreglos principales se fija en
 * el momento en que el usuario elige la cantidad de paises del mundial
 * (24, 32, 48 o 64) mediante el metodo dimensionarTorneo(int).
 */
public class GestorDatos {

    public static final int[] TAMANIOS_VALIDOS = {24, 32, 48, 64};

    private int tamanioMundial;
    private boolean torneoDimensionado;

    private Pais[] paises;
    private int totalPaises;

    private Estadio[] estadios;
    private int totalEstadios;

    private Arbitro[] arbitros;
    private int totalArbitros;
    
    // ---------------- MODULO 2: SORTEO Y FASE DE GRUPOS ----------------
public static final int EQUIPOS_POR_GRUPO = 4;
private Pais[][] grupos;
private boolean sorteoRealizado;

    public GestorDatos() {
        this.tamanioMundial = 0;
        this.torneoDimensionado = false;
        this.paises = new Pais[0];
        this.estadios = new Estadio[0];
        this.arbitros = new Arbitro[0];
    }

    /**
     * Dimensiona (o redimensiona) TODOS los arreglos principales del sistema
     * en funcion de la cantidad de paises seleccionada. Al llamarse, reinicia
     * todos los datos previamente cargados.
     *
     * Reglas de dimensionamiento (fijas y deterministas a partir del tamanio
     * de mundial elegido):
     *   - Paises:   exactamente "tamanio" espacios.
     *   - Estadios: tamanio / 2 espacios (minimo razonable de sedes).
     *   - Arbitros: tamanio espacios (cuerpo arbitral completo).
     */
    public boolean dimensionarTorneo(int tamanio) {
        boolean valido = false;
        for (int i = 0; i < TAMANIOS_VALIDOS.length; i++) {
            if (TAMANIOS_VALIDOS[i] == tamanio) {
                valido = true;
                break;
            }
        }
        if (!valido) {
            return false;
        }

        this.tamanioMundial = tamanio;
        this.paises = new Pais[tamanio];
        this.totalPaises = 0;

        this.estadios = new Estadio[tamanio / 2];
        this.totalEstadios = 0;

        this.arbitros = new Arbitro[tamanio];
        this.totalArbitros = 0;

        this.grupos = null;
        this.sorteoRealizado = false;

        this.torneoDimensionado = true;
        return true;
    }

    public boolean isTorneoDimensionado() {
        return torneoDimensionado;
    }

    public int getTamanioMundial() {
        return tamanioMundial;
    }

    // ---------------- PAISES ----------------

    public Pais[] getPaises() {
        return paises;
    }

    public int getTotalPaises() {
        return totalPaises;
    }

    public boolean agregarPais(Pais p) {
        if (!torneoDimensionado || totalPaises >= paises.length) {
            return false;
        }
        paises[totalPaises] = p;
        totalPaises++;
        sorteoRealizado = false;
        return true;
        
        
    }

    public boolean actualizarPais(int indice, Pais p) {
        if (indice < 0 || indice >= totalPaises) {
            return false;
        }
        paises[indice] = p;
        sorteoRealizado = false;
        return true;
    }

    public boolean hayEspacioParaPais() {
        return torneoDimensionado && totalPaises < paises.length;
    }

    // ---------------- ESTADIOS ----------------

    public Estadio[] getEstadios() {
        return estadios;
    }

    public int getTotalEstadios() {
        return totalEstadios;
    }

    public boolean agregarEstadio(Estadio e) {
        if (!torneoDimensionado || totalEstadios >= estadios.length) {
            return false;
        }
        estadios[totalEstadios] = e;
        totalEstadios++;
        return true;
    }

    public boolean actualizarEstadio(int indice, Estadio e) {
        if (indice < 0 || indice >= totalEstadios) {
            return false;
        }
        estadios[indice] = e;
        return true;
    }

    public boolean hayEspacioParaEstadio() {
        return torneoDimensionado && totalEstadios < estadios.length;
    }

    // ---------------- ARBITROS ----------------

    public Arbitro[] getArbitros() {
        return arbitros;
    }

    public int getTotalArbitros() {
        return totalArbitros;
    }

    public boolean agregarArbitro(Arbitro a) {
        if (!torneoDimensionado || totalArbitros >= arbitros.length) {
            return false;
        }
        arbitros[totalArbitros] = a;
        totalArbitros++;
        return true;
    }

    public boolean actualizarArbitro(int indice, Arbitro a) {
        if (indice < 0 || indice >= totalArbitros) {
            return false;
        }
        arbitros[indice] = a;
        return true;
    }

  public boolean hayEspacioParaArbitro() {
    return torneoDimensionado && totalArbitros < arbitros.length;
}

// ---------------- MODULO 2: Sorteo y Fase de Grupos - Fabricio ----------------

public int getCantidadGrupos() {
    return torneoDimensionado ? tamanioMundial / EQUIPOS_POR_GRUPO : 0;
    
}
// Mezcla el arreglo de paises (Fisher-Yates) y arma la matriz de grupos - Fabricio

public boolean realizarSorteoGrupos() {
    if (!torneoDimensionado || totalPaises != tamanioMundial) {
        return false;
    }

    Random azar = new Random();
    for (int i = totalPaises - 1; i > 0; i--) {
        int j = azar.nextInt(i + 1);
        Pais temporal = paises[i];
        paises[i] = paises[j];
        paises[j] = temporal;
    }
// ---------------- MODULO 2: Sorteo y Fase de Grupos - Fabricio ----------------

    int cantidadGrupos = getCantidadGrupos();
    grupos = new Pais[cantidadGrupos][EQUIPOS_POR_GRUPO];
    int indice = 0;
    for (int g = 0; g < cantidadGrupos; g++) {
        for (int e = 0; e < EQUIPOS_POR_GRUPO; e++) {
            grupos[g][e] = paises[indice];
            indice++;
        }
    }

    sorteoRealizado = true;
    return true;
}

public Pais[][] getGrupos() {
    return grupos;
}

public boolean isSorteoRealizado() {
    return sorteoRealizado;
}
}
