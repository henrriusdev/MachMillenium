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

public class EditarMaterial {
    public JPanel panel;
    private JFormattedTextField campoNombre;
    private JFormattedTextField campoCantidad;
    private JFormattedTextField campoCosto;
    private JComboBox comboboxTipoInsumo;
    private JButton cerrarButton;
    private JButton editarButton;
    private final Material material;
    private final TipoInsumoRepo tipoInsumoRepo;

    public EditarMaterial(Material material, TipoInsumoRepo tipoInsumoRepo) {
        if (material == null) {
            JOptionPane.showMessageDialog(null, "Material no encontrado");
            this.material = null;
            this.tipoInsumoRepo = null;
            SwingUtilities.getWindowAncestor(editarButton).dispose();
            return;
        }

        this.material = material;
        this.tipoInsumoRepo = tipoInsumoRepo;
        campoNombre.setText(material.getNombre());
        campoCantidad.setValue(material.getCantidad());
        campoCosto.setValue(material.getCosto());
        comboboxTipoInsumo.setSelectedItem(material.getTipoInsumo().getNombre());

        cerrarButton.addActionListener(e -> SwingUtilities.getWindowAncestor(cerrarButton).dispose());
        editarButton.addActionListener(e -> {
            TipoInsumo tipoInsumo = tipoInsumoRepo.obtenerPorNombre((String) comboboxTipoInsumo.getSelectedItem());
            String nombre = campoNombre.getText();
            Long cantidad = (Long) campoCantidad.getValue();
            Double costo = (Double) campoCosto.getValue();

            Material materialEditado = new Material(tipoInsumo, cantidad, costo, nombre);
            materialEditado.setId(this.material.getId());
            tipoInsumoRepo.actualizarMaterial(materialEditado);


            JOptionPane.showMessageDialog(null, "Material modificado correctamente");
            SwingUtilities.getWindowAncestor(editarButton).dispose();
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
