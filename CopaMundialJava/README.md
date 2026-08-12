# Copa Mundial Java - Modulos 1 y 4

## Interfaz visual Mundial

El proyecto incorpora una identidad visual deportiva construida solamente con
Swing y Java2D: encabezado de campeonato, balon vectorial, paleta verde/azul
con acentos dorados, navegacion numerada, tablas y controles uniformes. El
estilo se centraliza en `vista/TemaMundial.java`, por lo que no necesita
librerias ni imagenes externas y tambien se aplica a los dialogos.

La ventana principal (`JFrame`) utiliza un menu lateral y un `CardLayout` para
mostrar los modulos (`JPanel`) sin abrir ventanas repetidas. La pantalla de
inicio incluye accesos rapidos, una guia del flujo del torneo y una mini cancha
dibujada mediante Java2D. `BotonMenu` conserva visualmente la seccion activa.

## Modulo 4: Calendario y Motor de Simulacion

La pestana `5. Calendario / Simulacion` crea grupos de cuatro con los paises
registrados y un calendario de seis encuentros por grupo. Sus dos modalidades
permiten simular el siguiente partido pendiente o completar toda la fase. Cada
encuentro genera marcador, goleadores y tarjetas al azar, y actualiza la tabla
con 3 puntos por victoria y 1 por empate.

Antes de iniciar, dimensione el torneo y cargue los paises (la opcion mas
rapida es `Generar Datos de Demostracion`). Luego pulse
`Preparar / Reiniciar Calendario` en el Modulo 4.

## Como abrir el proyecto en NetBeans
1. Apache NetBeans -> File -> Open Project...
2. Selecciona la carpeta `CopaMundialJava` (la que contiene `nbproject`, `src`, `build.xml`).
3. NetBeans reconoce automaticamente el proyecto como un proyecto Java SE
   (Ant) y genera por si solo `nbproject/build-impl.xml` la primera vez que
   se abre; no hace falta configurar nada mas.
4. Boton derecho sobre el proyecto -> Run (o F6). La clase principal ya esta
   configurada: `copamundial.Main`.

Requisito: JDK 17 o superior instalado y configurado como plataforma por
defecto en NetBeans (Tools -> Java Platforms).

## Estructura del codigo

```
src/copamundial/
  Main.java                     -> punto de entrada
  modelo/
    Jugador.java
    MiembroCuerpoTecnico.java
    Pais.java                   -> arreglos fijos: Jugador[23], MiembroCuerpoTecnico[5]
    Estadio.java
    Arbitro.java
  datos/
    GestorDatos.java            -> "base de datos" en RAM, solo arreglos (Pais[], Estadio[], Arbitro[])
    GeneradorDemo.java          -> regla de negocio "Generar Datos de Demostracion"
  util/
    ArregloTableModel.java      -> TableModel propio respaldado por String[][] (no usa Vector/DefaultTableModel)
  vista/
    VentanaPrincipal.java       -> JFrame con JTabbedPane (4 pestanas del Modulo 1)
    PanelConfiguracion.java     -> Requerimiento 1: tamanio del mundial (24/32/48/64) + boton de carga masiva
    PanelPaises.java            -> Requerimiento 2: alta/edicion de paises
    PanelEstadios.java          -> Requerimiento 2: alta/edicion de sedes
    PanelArbitros.java          -> Requerimiento 2: alta/edicion de arbitros
    DialogoPais.java, DialogoJugadores.java, DialogoCuerpoTecnico.java,
    DialogoEstadio.java, DialogoArbitro.java  -> formularios modales de alta/edicion
```

## Cumplimiento de las restricciones tecnicas

- **Cero estructuras dinamicas**: no se usa `ArrayList`, `List`, `HashMap`
  ni ningun tipo del paquete `java.util.Collection`/`Map` en la logica de
  negocio. Toda la persistencia de `GestorDatos` y de `Pais` usa arreglos
  (`Jugador[]`, `MiembroCuerpoTecnico[]`, `Pais[]`, `Estadio[]`, `Arbitro[]`)
  con contadores manuales (`cantidadX`) para saber cuantos espacios estan
  ocupados, y desplazamientos manuales de arreglo para eliminar elementos.
- **Cero bases de datos**: todo vive en los atributos de `GestorDatos`
  (memoria RAM); se pierde al cerrar la aplicacion.
- **Solo arreglos y matrices**: incluso las tablas de la interfaz (`JTable`)
  usan un `TableModel` propio (`ArregloTableModel`) respaldado por una
  matriz `String[][]`, en vez de `DefaultTableModel` (que internamente usa
  `Vector`).
- **Nota honesta sobre Swing**: componentes nativos de la libreria como
  `JComboBox` o `JTabbedPane` usan estructuras internas propias de Swing
  (fuera del control del programador); la restriccion se cumple en el 100%
  del codigo de gestion de datos escrito para este sistema.

## Reglas de dimensionamiento al elegir el tamanio del mundial

Al presionar "Dimensionar / Reiniciar Torneo" con un tamanio `N`
(24, 32, 48 o 64), se crean:

- `Pais[N]`        -> N espacios para paises.
- `Estadio[N/2]`   -> N/2 espacios para sedes.
- `Arbitro[N]`     -> N espacios para el cuerpo arbitral.

Cada `Pais` siempre reserva `Jugador[23]` (plantilla completa) y
`MiembroCuerpoTecnico[5]` (DT, asistente, preparador fisico, medico,
analista tactico).

## Boton "Generar Datos de Demostracion"

Pobla instantaneamente TODOS los espacios de los arreglos ya
dimensionados (paises con plantel y cuerpo tecnico completos, sedes y
arbitros) con datos ficticios tomados de arreglos de nombres predefinidos.
Despues de generarlos, cualquier registro puede seleccionarse y
modificarse manualmente desde las pestanas "Paises", "Sedes / Estadios" y
"Cuerpo Arbitral".
