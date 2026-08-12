package copamundial.vista;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/** Encabezado dibujado con Java2D; no requiere imagenes externas. */
public class EncabezadoMundial extends JPanel {

    public EncabezadoMundial() {
        setPreferredSize(new Dimension(1000, 104));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics grafico) {
        Graphics2D g = (Graphics2D) grafico.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setPaint(new GradientPaint(0, 0, TemaMundial.AZUL_NOCHE,
                getWidth(), getHeight(), TemaMundial.VERDE_OSCURO));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Lineas que evocan el campo de juego.
        g.setColor(new Color(255, 255, 255, 25));
        g.setStroke(new BasicStroke(2f));
        g.drawOval(getWidth() - 250, -70, 220, 220);
        g.drawLine(getWidth() - 140, 0, getWidth() - 140, getHeight());

        dibujarBalon(g, 48, 52, 31);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Segoe UI", Font.BOLD, 27));
        g.drawString("COPA MUNDIAL JAVA", 98, 45);
        g.setColor(TemaMundial.DORADO);
        g.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g.drawString("GESTION  •  COMPETENCIA  •  ESTADISTICAS", 100, 70);

        g.setColor(new Color(255, 255, 255, 210));
        g.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        String etapa = "CENTRO DE CONTROL DEL TORNEO";
        int ancho = g.getFontMetrics().stringWidth(etapa);
        g.drawString(etapa, getWidth() - ancho - 32, 81);
        g.dispose();
    }

    private void dibujarBalon(Graphics2D g, int centroX, int centroY, int radio) {
        g.setColor(Color.WHITE);
        g.fillOval(centroX - radio, centroY - radio, radio * 2, radio * 2);
        g.setColor(TemaMundial.AZUL_NOCHE);
        g.setStroke(new BasicStroke(2f));
        g.drawOval(centroX - radio, centroY - radio, radio * 2, radio * 2);

        int[] x = {centroX, centroX + 9, centroX + 6, centroX - 6, centroX - 9};
        int[] y = {centroY - 10, centroY - 3, centroY + 8, centroY + 8, centroY - 3};
        g.fillPolygon(x, y, 5);
        g.drawLine(centroX, centroY - 10, centroX, centroY - radio);
        g.drawLine(centroX + 9, centroY - 3, centroX + radio - 2, centroY - 9);
        g.drawLine(centroX + 6, centroY + 8, centroX + 18, centroY + 24);
        g.drawLine(centroX - 6, centroY + 8, centroX - 18, centroY + 24);
        g.drawLine(centroX - 9, centroY - 3, centroX - radio + 2, centroY - 9);
    }
}
