package copamundial.vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

/**
 * Identidad visual del sistema. Centraliza colores, tipografia y estilos
 * para mantener todos los modulos y dialogos con la misma apariencia.
 */
public final class TemaMundial {

    public static final Color AZUL_NOCHE = new Color(7, 27, 46);
    public static final Color AZUL = new Color(13, 56, 92);
    public static final Color VERDE = new Color(0, 133, 82);
    public static final Color VERDE_OSCURO = new Color(0, 91, 61);
    public static final Color DORADO = new Color(241, 183, 52);
    public static final Color FONDO = new Color(240, 245, 243);
    public static final Color TARJETA = Color.WHITE;
    public static final Color TEXTO = new Color(27, 43, 51);
    public static final Color BORDE = new Color(205, 218, 214);

    private TemaMundial() {
    }

    /** Instala Nimbus y ajusta sus propiedades sin librerias externas. */
    public static void instalar() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // El programa conserva el Look & Feel disponible en el sistema.
        }

        FontUIResource normal = new FontUIResource("Segoe UI", Font.PLAIN, 14);
        FontUIResource semibold = new FontUIResource("Segoe UI", Font.BOLD, 14);
        ColorUIResource fondo = new ColorUIResource(FONDO);
        ColorUIResource blanco = new ColorUIResource(TARJETA);
        ColorUIResource texto = new ColorUIResource(TEXTO);
        ColorUIResource verde = new ColorUIResource(VERDE);
        ColorUIResource azul = new ColorUIResource(AZUL_NOCHE);

        UIManager.put("defaultFont", normal);
        UIManager.put("control", fondo);
        UIManager.put("info", blanco);
        UIManager.put("nimbusBase", azul);
        UIManager.put("nimbusBlueGrey", new ColorUIResource(107, 125, 132));
        UIManager.put("nimbusFocus", new ColorUIResource(DORADO));
        UIManager.put("nimbusLightBackground", blanco);
        UIManager.put("text", texto);

        UIManager.put("Panel.background", fondo);
        UIManager.put("Label.font", normal);
        UIManager.put("Label.foreground", texto);
        UIManager.put("Button.font", semibold);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.background", verde);
        UIManager.put("Button.margin", new Insets(8, 14, 8, 14));
        UIManager.put("Button.focus", new ColorUIResource(DORADO));
        UIManager.put("ToggleButton.font", semibold);
        UIManager.put("RadioButton.font", normal);
        UIManager.put("ComboBox.font", normal);
        UIManager.put("TextField.font", normal);
        UIManager.put("TextArea.font", new FontUIResource("Consolas", Font.PLAIN, 13));

        UIManager.put("Table.font", normal);
        UIManager.put("Table.background", blanco);
        UIManager.put("Table.foreground", texto);
        UIManager.put("Table.selectionBackground", new ColorUIResource(213, 239, 228));
        UIManager.put("Table.selectionForeground", texto);
        UIManager.put("Table.gridColor", new ColorUIResource(226, 234, 231));
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("TableHeader.font", semibold);
        UIManager.put("TableHeader.background", azul);
        UIManager.put("TableHeader.foreground", Color.WHITE);

        UIManager.put("TabbedPane.font", semibold);
        UIManager.put("TabbedPane.background", fondo);
        UIManager.put("TabbedPane.foreground", texto);
        UIManager.put("TabbedPane.selected", blanco);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(8, 0, 0, 0));

        UIManager.put("ScrollPane.background", blanco);
        UIManager.put("ScrollPane.border", BorderFactory.createLineBorder(BORDE));
        UIManager.put("TitledBorder.font", semibold);
        UIManager.put("TitledBorder.titleColor", VERDE_OSCURO);
        UIManager.put("OptionPane.messageFont", normal);
        UIManager.put("OptionPane.buttonFont", semibold);
    }
}
