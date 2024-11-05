package com.criollo.machmillenium;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class CustomFlatLaf extends FlatLightLaf {
    // Definimos las constantes de colores
    private static final Color PRIMARY_COLOR = new Color(0xD06F28);      // Naranja
    private static final Color PRIMARY_HOVER = new Color(0xFFA500);      // Naranja más claro
    private static final Color PRIMARY_PRESSED = new Color(0xFF8C00);    // Naranja más oscuro
    private static final Color SECONDARY_COLOR = new Color(0x2889d0);    // Azul rey
    private static final Color SECONDARY_HOVER = new Color(0x5EA7DC);    // Azul más claro
    private static final Color TEXT_COLOR = new Color(0x2B2B2B);        // Texto oscuro

    @Override
    public UIDefaults getDefaults() {
        UIDefaults defaults = super.getDefaults();

        // Colores base
        defaults.put("@foreground", TEXT_COLOR);
        defaults.put("@accentColor", SECONDARY_COLOR);
        defaults.put("Component.accentColor", SECONDARY_COLOR);
        defaults.put("Component.focusColor", SECONDARY_COLOR);

        // TabbedPane
        defaults.put("TabbedPane.background", PRIMARY_COLOR);
        defaults.put("TabbedPane.foreground", TEXT_COLOR);
        defaults.put("TabbedPane.selectedBackground", SECONDARY_COLOR);
        defaults.put("TabbedPane.selectedForeground", Color.WHITE);
        defaults.put("TabbedPane.hoverBackground", PRIMARY_HOVER);
        defaults.put("TabbedPane.focusColor", SECONDARY_COLOR);
        defaults.put("TabbedPane.underlineColor", SECONDARY_COLOR);

        // Botones
        defaults.put("Button.background", SECONDARY_COLOR);
        defaults.put("Button.foreground", Color.WHITE);
        defaults.put("Button.hoverBackground", SECONDARY_HOVER);
        defaults.put("Button.hoverForeground", TEXT_COLOR);
        defaults.put("Button.pressedBackground", PRIMARY_PRESSED);
        defaults.put("Button.focusedBorderColor", SECONDARY_COLOR);
        defaults.put("Button.default.boldText", true);

        // Campos de texto
        defaults.put("TextComponent.focusedBorderColor", SECONDARY_COLOR);
        defaults.put("TextField.background", Color.WHITE);
        defaults.put("TextField.foreground", TEXT_COLOR);
        defaults.put("TextField.caretColor", SECONDARY_COLOR);

        // Bordes y arcos
        defaults.put("Component.arc", 8);
        defaults.put("Component.borderWidth", 1);
        defaults.put("Component.focusWidth", 1);


        // ComboBox
        defaults.put("ComboBox.background", SECONDARY_COLOR);
        defaults.put("ComboBox.foreground", Color.WHITE);
        defaults.put("ComboBox.selectionBackground", PRIMARY_COLOR);
        defaults.put("ComboBox.selectionForeground", TEXT_COLOR);

        return defaults;
    }

    public static boolean setup() {
        return FlatLightLaf.setup(new CustomFlatLaf());
    }

    @Override
    public String getName() {
        return "MachTheme";
    }
}