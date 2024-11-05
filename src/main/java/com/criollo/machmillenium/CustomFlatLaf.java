package com.criollo.machmillenium;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class CustomFlatLaf extends FlatLightLaf {
    // Definimos las constantes de colores
    private static final Color PRIMARY_COLOR = new Color(0xFFD700);      // Amarillo
    private static final Color PRIMARY_HOVER = new Color(0xFFE44D);      // Amarillo más claro
    private static final Color PRIMARY_PRESSED = new Color(0xFFC700);    // Amarillo más oscuro
    private static final Color SECONDARY_COLOR = new Color(0x87CEEB);    // Azul celeste
    private static final Color SECONDARY_HOVER = new Color(0xA7DEFD);    // Azul celeste más claro
    private static final Color TEXT_COLOR = new Color(0x2B2B2B);        // Texto oscuro

    @Override
    public UIDefaults getDefaults() {
        UIDefaults defaults = super.getDefaults();

        // Colores base
        defaults.put("@background", PRIMARY_COLOR);
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

        // Paneles
        defaults.put("Panel.background", PRIMARY_COLOR);

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
        return "Yellow Sky Theme";
    }
}