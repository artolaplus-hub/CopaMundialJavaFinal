package copamundial.datos;

import copamundial.modelo.Arbitro;
import copamundial.modelo.Estadio;
import copamundial.modelo.Jugador;
import copamundial.modelo.MiembroCuerpoTecnico;
import copamundial.modelo.Pais;
import java.util.Random;

/**
 * Regla de negocio "Generar Datos de Demostracion".
 * Puebla instantaneamente los arreglos de GestorDatos con datos ficticios
 * cuya cantidad depende exclusivamente del tamanio de mundial elegido.
 * Toda la fuente de datos usada aqui son arreglos (String[]), nunca
 * colecciones dinamicas.
 */
public class GeneradorDemo {

    private static final String[] PAISES = {
        "Argentina", "Brasil", "Francia", "Alemania", "Espana", "Inglaterra",
        "Italia", "Portugal", "Paises Bajos", "Belgica", "Croacia", "Uruguay",
        "Colombia", "Mexico", "Estados Unidos", "Canada", "Japon", "Corea del Sur",
        "Australia", "Arabia Saudita", "Iran", "Qatar", "Marruecos", "Senegal",
        "Nigeria", "Ghana", "Camerun", "Tunez", "Egipto", "Argelia",
        "Costa de Marfil", "Sudafrica", "Chile", "Peru", "Ecuador", "Paraguay",
        "Bolivia", "Venezuela", "Costa Rica", "Panama", "Jamaica", "Honduras",
        "Suiza", "Polonia", "Suecia", "Dinamarca", "Noruega", "Serbia",
        "Gales", "Escocia", "Republica Checa", "Austria", "Ucrania", "Rumania",
        "Turquia", "Grecia", "China", "Nueva Zelanda", "Irak",
        "Emiratos Arabes Unidos", "Jordania", "Uzbekistan", "Islandia", "Finlandia",
        "Hungria"
    };

    private static final String[] CONFEDERACIONES = {
        "UEFA", "CONMEBOL", "CONCACAF", "AFC", "CAF", "OFC"
    };

    private static final String[] CIUDADES = {
        "San Jose", "Ciudad de Mexico", "Buenos Aires", "Madrid", "Miami",
        "Toronto", "Rio de Janeiro", "Berlin", "Paris", "Londres", "Roma",
        "Lisboa", "Amsterdam", "Bruselas", "Zurich", "Varsovia", "Estocolmo",
        "Copenhague", "Oslo", "Belgrado", "Cardiff", "Glasgow", "Praga",
        "Viena", "Kiev", "Bucarest", "Estambul", "Atenas", "Doha",
        "Riad", "Tokio", "Seul", "Sidney"
    };

    private static final String[] NOMBRES_JUGADORES = {
        "Carlos", "Luis", "Andres", "Diego", "Mateo", "Santiago", "Bruno",
        "Kevin", "Marco", "Fabian", "Julian", "Tomas", "Emiliano", "Nicolas",
        "Rodrigo", "Sebastian", "Gabriel", "Felipe", "Ivan", "Hugo", "Victor",
        "Adrian", "Pablo", "Ricardo", "Alexis"
    };

    private static final String[] APELLIDOS_JUGADORES = {
        "Gonzalez", "Rodriguez", "Fernandez", "Martinez", "Perez", "Sanchez",
        "Ramirez", "Torres", "Flores", "Rivas", "Castro", "Vargas", "Rojas",
        "Mendez", "Herrera", "Silva", "Nunez", "Cordero", "Solano", "Alvarado"
    };

    private static final String[] POSICIONES_PLANTILLA = {
        "Portero", "Portero", "Portero",
        "Defensa", "Defensa", "Defensa", "Defensa", "Defensa", "Defensa", "Defensa", "Defensa",
        "Mediocampista", "Mediocampista", "Mediocampista", "Mediocampista", "Mediocampista", "Mediocampista", "Mediocampista",
        "Delantero", "Delantero", "Delantero", "Delantero", "Delantero"
    };

    private static final String[] CARGOS_CUERPO_TECNICO = {
        "Director Tecnico", "Asistente Tecnico", "Preparador Fisico", "Medico del Equipo", "Analista Tactico"
    };

    private static final String[] NOMBRES_ARBITROS = {
        "Esteban", "Mauricio", "Rafael", "Federico", "Alberto", "Ramon",
        "Cesar", "Oscar", "Danilo", "Ernesto", "Gustavo", "Enrique",
        "Manuel", "Jorge", "Roberto", "Alfonso", "Guillermo", "Arturo",
        "Salvador", "Ignacio", "Leonardo", "Tomas", "Miguel", "Antonio"
    };

    private static final String[] APELLIDOS_ARBITROS = {
        "Salazar", "Guzman", "Montero", "Aguilar", "Chaves", "Zamora",
        "Blanco", "Delgado", "Campos", "Vega", "Mora", "Rojas",
        "Quiros", "Barrantes", "Jimenez", "Villalobos", "Araya", "Ureña"
    };

    private static final String[] CATEGORIAS_ARBITRO = {
        "Central", "Asistente 1", "Asistente 2", "VAR"
    };

    private static final Random RANDOM = new Random();

    private GeneradorDemo() {
    }

    /**
     * Puebla instantaneamente TODOS los arreglos del gestor (paises con su
     * plantilla y cuerpo tecnico completos, sedes y cuerpo arbitral) segun
     * el tamanio de mundial ya dimensionado. Los datos generados quedan
     * disponibles para modificarse manualmente despues.
     */
    public static void poblarDatosDemo(GestorDatos gestor) {
        if (gestor == null || !gestor.isTorneoDimensionado()) {
            return;
        }

        int cantidadPaises = gestor.getPaises().length;
        for (int i = 0; i < cantidadPaises; i++) {
            String nombrePais = PAISES[i % PAISES.length];
            String siglas = generarSiglas(nombrePais);
            String confederacion = CONFEDERACIONES[i % CONFEDERACIONES.length];

            Pais pais = new Pais(nombrePais + (i >= PAISES.length ? " " + (i / PAISES.length + 1) : ""), siglas, confederacion);

            // Plantilla completa (23 jugadores)
            for (int j = 0; j < Pais.MAX_JUGADORES; j++) {
                String nombreJugador = NOMBRES_JUGADORES[RANDOM.nextInt(NOMBRES_JUGADORES.length)]
                        + " " + APELLIDOS_JUGADORES[RANDOM.nextInt(APELLIDOS_JUGADORES.length)];
                String posicion = POSICIONES_PLANTILLA[j % POSICIONES_PLANTILLA.length];
                int edad = 18 + RANDOM.nextInt(21); // 18 a 38 anios
                pais.agregarJugador(new Jugador(nombreJugador, j + 1, posicion, edad));
            }

            // Cuerpo tecnico completo (5 miembros)
            for (int c = 0; c < Pais.MAX_CUERPO_TECNICO; c++) {
                String nombreMiembro = NOMBRES_ARBITROS[RANDOM.nextInt(NOMBRES_ARBITROS.length)]
                        + " " + APELLIDOS_ARBITROS[RANDOM.nextInt(APELLIDOS_ARBITROS.length)];
                int edad = 30 + RANDOM.nextInt(30); // 30 a 59 anios
                pais.agregarMiembroCuerpoTecnico(new MiembroCuerpoTecnico(nombreMiembro, CARGOS_CUERPO_TECNICO[c], edad));
            }

            gestor.agregarPais(pais);
        }

        int cantidadEstadios = gestor.getEstadios().length;
        for (int i = 0; i < cantidadEstadios; i++) {
            String ciudad = CIUDADES[i % CIUDADES.length];
            String nombreEstadio = "Estadio " + ciudad;
            int capacidad = 40000 + RANDOM.nextInt(50001); // 40,000 a 90,000
            String paisSede = PAISES[i % PAISES.length];
            gestor.agregarEstadio(new Estadio(nombreEstadio, ciudad, paisSede, capacidad));
        }

        int cantidadArbitros = gestor.getArbitros().length;
        for (int i = 0; i < cantidadArbitros; i++) {
            String nombreArbitro = NOMBRES_ARBITROS[RANDOM.nextInt(NOMBRES_ARBITROS.length)]
                    + " " + APELLIDOS_ARBITROS[RANDOM.nextInt(APELLIDOS_ARBITROS.length)];
            String nacionalidad = PAISES[RANDOM.nextInt(PAISES.length)];
            String categoria = CATEGORIAS_ARBITRO[i % CATEGORIAS_ARBITRO.length];
            gestor.agregarArbitro(new Arbitro(nombreArbitro, nacionalidad, categoria));
        }
    }

    private static String generarSiglas(String nombrePais) {
        String limpio = nombrePais.replace(" ", "");
        int longitud = Math.min(3, limpio.length());
        return limpio.substring(0, longitud).toUpperCase();
    }
}
