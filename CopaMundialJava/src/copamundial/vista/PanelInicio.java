package copamundial.vista;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** Tablero de bienvenida y guia visual del flujo del torneo. */
public class PanelInicio extends JPanel {

    public PanelInicio(Runnable abrirConfiguracion, Runnable abrirPartidos) {
        setLayout(new BorderLayout(18, 18));
        setBorder(BorderFactory.createEmptyBorder(22, 25, 24, 25));
        setBackground(TemaMundial.FONDO);

        add(crearHero(abrirConfiguracion, abrirPartidos), BorderLayout.NORTH);
        add(crearFlujo(), BorderLayout.CENTER);
    }

    private JPanel crearHero(Runnable abrirConfiguracion, Runnable abrirPartidos) {
        JPanel hero = new JPanel(new BorderLayout(25, 0));
        hero.setBackground(TemaMundial.TARJETA);
        hero.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaMundial.BORDE),
                BorderFactory.createEmptyBorder(22, 25, 22, 25)));

        JPanel texto = new JPanel();
        texto.setOpaque(false);
        texto.setLayout(new BoxLayout(texto, BoxLayout.Y_AXIS));
        JLabel etiqueta = new JLabel("PANEL GENERAL DEL CAMPEONATO");
        etiqueta.setForeground(TemaMundial.VERDE);
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel titulo = new JLabel("Construye y simula tu Copa Mundial");
        titulo.setForeground(TemaMundial.AZUL_NOCHE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        JLabel detalle = new JLabel("<html>Configura participantes, organiza grupos, disputa partidos<br>"
                + "y consulta las metricas finales desde un solo lugar.</html>");
        detalle.setForeground(new Color(78, 94, 101));
        detalle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel acciones = new JPanel();
        acciones.setOpaque(false);
        acciones.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        JButton comenzar = crearBoton("Comenzar configuracion", TemaMundial.VERDE);
        JButton partidos = crearBoton("Ir a simulacion", TemaMundial.AZUL);
        comenzar.addActionListener(e -> abrirConfiguracion.run());
        partidos.addActionListener(e -> abrirPartidos.run());
        acciones.add(comenzar);
        acciones.add(Box.createHorizontalStrut(10));
        acciones.add(partidos);

        texto.add(etiqueta);
        texto.add(Box.createVerticalStrut(6));
        texto.add(titulo);
        texto.add(Box.createVerticalStrut(9));
        texto.add(detalle);
        texto.add(Box.createVerticalStrut(18));
        texto.add(acciones);

        hero.add(texto, BorderLayout.CENTER);
        hero.add(new MiniCancha(), BorderLayout.EAST);
        return hero;
    }

    private JPanel crearFlujo() {
        JPanel contenedor = new JPanel(new BorderLayout(0, 12));
        contenedor.setOpaque(false);
        JLabel titulo = new JLabel("RUTA DEL TORNEO");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(TemaMundial.AZUL_NOCHE);
        contenedor.add(titulo, BorderLayout.NORTH);

        JPanel tarjetas = new JPanel(new GridLayout(2, 2, 14, 14));
        tarjetas.setOpaque(false);
        tarjetas.add(crearTarjeta("01", "Preparacion", "Participantes, estadios y cuerpo arbitral", TemaMundial.AZUL));
        tarjetas.add(crearTarjeta("02", "Fase de grupos", "Sorteo, calendario y tabla de posiciones", TemaMundial.VERDE));
        tarjetas.add(crearTarjeta("03", "Eliminacion", "Llaves directas hasta definir al campeon", new Color(181, 123, 22)));
        tarjetas.add(crearTarjeta("04", "Resultados", "Goleadores, disciplina y metricas finales", new Color(103, 66, 145)));
        contenedor.add(tarjetas, BorderLayout.CENTER);
        return contenedor;
    }

    private JPanel crearTarjeta(String numero, String titulo, String detalle, Color acento) {
        JPanel tarjeta = new JPanel(new BorderLayout(15, 0));
        tarjeta.setBackground(TemaMundial.TARJETA);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, acento),
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(TemaMundial.BORDE),
                        BorderFactory.createEmptyBorder(16, 18, 16, 18))));
        JLabel n = new JLabel(numero);
        n.setFont(new Font("Segoe UI", Font.BOLD, 28));
        n.setForeground(acento);
        JLabel contenido = new JLabel("<html><b style='font-size:13px'>" + titulo
                + "</b><br><span style='color:#607078'>" + detalle + "</span></html>");
        tarjeta.add(n, BorderLayout.WEST);
        tarjeta.add(contenido, BorderLayout.CENTER);
        return tarjeta;
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setBackground(fondo);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setFocusPainted(false);
        return boton;
    }

    private static class MiniCancha extends JPanel {
        MiniCancha() {
            setPreferredSize(new Dimension(245, 135));
            setOpaque(false);
        }

        @Override protected void paintComponent(Graphics grafico) {
            Graphics2D g = (Graphics2D) grafico.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(1, 121, 73));
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
            g.setColor(new Color(255, 255, 255, 175));
            g.setStroke(new BasicStroke(2f));
            g.drawRoundRect(12, 12, getWidth() - 25, getHeight() - 25, 7, 7);
            g.drawLine(getWidth() / 2, 12, getWidth() / 2, getHeight() - 13);
            g.drawOval(getWidth() / 2 - 25, getHeight() / 2 - 25, 50, 50);
            g.drawRect(12, 40, 36, 55);
            g.drawRect(getWidth() - 49, 40, 36, 55);
            g.fillOval(getWidth() / 2 - 3, getHeight() / 2 - 3, 6, 6);
            g.dispose();
        }
    }
}
