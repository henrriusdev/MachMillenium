package com.criollo.machmillenium;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class CustomFlatLaf extends FlatLightLaf {
    // Definimos las constantes de colores
    private static final Color PRIMARY_COLOR = new Color(0xCFCFCF);
    private static final Color PRIMARY_HOVER = new Color(0x9F9F9F);
    private static final Color PRIMARY_PRESSED = new Color(0x6F6F6F);
    private static final Color SECONDARY_COLOR = new Color(0xe2ac4a);
    private static final Color SECONDARY_HOVER = new Color(0xaf8539);
    private static final Color TEXT_COLOR = new Color(0x000000);

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
        defaults.put("TabbedPane.selectedBackground", PRIMARY_PRESSED);
        defaults.put("TabbedPane.hoverBackground", PRIMARY_HOVER);
        defaults.put("TabbedPane.focusColor", SECONDARY_COLOR);
        defaults.put("TabbedPane.underlineColor", SECONDARY_COLOR);

        // Botones
        defaults.put("Button.background", SECONDARY_COLOR);
        defaults.put("Button.foreground", TEXT_COLOR);
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
        defaults.put("ComboBox.foreground", TEXT_COLOR);
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