package com.criollo.machmillenium.vistas.emergentes.material;

import com.criollo.machmillenium.entidades.Material;
import com.criollo.machmillenium.entidades.TipoInsumo;
import com.criollo.machmillenium.repos.TipoInsumoRepo;
import com.criollo.machmillenium.utilidades.Utilidades;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AgregarMaterial {
    public JPanel panel;
    private JFormattedTextField campoNombre;
    private JComboBox<String> comboboxTipoInsumo;
    private JButton cerrarButton;
    private JButton agregarButton;
    private JFormattedTextField campoCantidad;
    private JFormattedTextField campoCosto;
    private final TipoInsumoRepo tipoInsumoRepo;

    public AgregarMaterial(TipoInsumoRepo tipoInsumoRepo) {
        this.tipoInsumoRepo = tipoInsumoRepo;
        cerrarButton.addActionListener(e -> SwingUtilities.getWindowAncestor(cerrarButton).dispose());
        agregarButton.addActionListener(e -> {
            TipoInsumo tipoInsumo = tipoInsumoRepo.obtenerPorNombre((String) comboboxTipoInsumo.getSelectedItem());
            String nombre = campoNombre.getText();
            Long cantidad = (Long) campoCantidad.getValue();
            Double costo = (Double) campoCosto.getValue();

            Material material = new Material(tipoInsumo, cantidad, costo, nombre);
            tipoInsumoRepo.insertarMaterial(material);

            JOptionPane.showMessageDialog(null, "Material agregado correctamente");
            SwingUtilities.getWindowAncestor(agregarButton).dispose();
        });
    }

    private void createUIComponents() {
        TipoInsumoRepo tipoInsumoRepo = new TipoInsumoRepo();
        NumberFormatter numberFormatter = Utilidades.getNumberFormatter();
        campoCantidad = new JFormattedTextField(numberFormatter);
        NumberFormat formatoCosto = DecimalFormat.getNumberInstance(Locale.forLanguageTag("es"));
        formatoCosto.setMaximumFractionDigits(2);
        formatoCosto.setMinimumFractionDigits(2);
        formatoCosto.setGroupingUsed(true);
        campoCosto = new JFormattedTextField(formatoCosto);

        List<TipoInsumo> tipoInsumos = tipoInsumoRepo.obtenerTodos();
        String[] nombres = tipoInsumos.stream().map(TipoInsumo::getNombre).toArray(String[]::new);
        comboboxTipoInsumo = new JComboBox<>(nombres);
    }
}