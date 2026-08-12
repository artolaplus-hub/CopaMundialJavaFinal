package copamundial.vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/** Boton del menu lateral con estado activo y respuesta al puntero. */
public class BotonMenu extends JButton {

    private boolean activo;
    private boolean encima;

    public BotonMenu(String texto) {
        super(texto);
        setHorizontalAlignment(SwingConstants.LEFT);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setForeground(new Color(221, 233, 237));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(220, 47));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { encima = true; repaint(); }
            @Override public void mouseExited(MouseEvent e) { encima = false; repaint(); }
        });
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        setForeground(activo ? Color.WHITE : new Color(221, 233, 237));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics grafico) {
        Graphics2D g = (Graphics2D) grafico.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (activo || encima) {
            g.setColor(activo ? TemaMundial.VERDE : new Color(255, 255, 255, 20));
            g.fillRoundRect(5, 3, getWidth() - 10, getHeight() - 6, 12, 12);
        }
        if (activo) {
            g.setColor(TemaMundial.DORADO);
            g.fillRoundRect(5, 10, 4, getHeight() - 20, 4, 4);
        }
        g.dispose();
        super.paintComponent(grafico);
    }
}
