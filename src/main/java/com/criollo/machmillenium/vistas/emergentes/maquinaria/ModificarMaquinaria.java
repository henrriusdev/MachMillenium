package com.criollo.machmillenium.vistas.emergentes.maquinaria;

import com.criollo.machmillenium.repos.TipoMaquinariaRepo;

import javax.swing.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ModificarMaquinaria {

    private JPanel mainPanel;
    private JTextField campoNombre;
    private JComboBox<String> comboboxTipoMaquinaria;
    private JFormattedTextField campoCostoPorTiempoUso;
    private JFormattedTextField campoTiempoEstimado;
    private JButton cerrarButton;
    private JButton agregarButton;
    private JFormattedTextField campoCostoTotal;
    private final TipoMaquinariaRepo tipoMaquinariaRepo;

    public ModificarMaquinaria() {
        this.tipoMaquinariaRepo = new TipoMaquinariaRepo();
        campoTiempoEstimado.addPropertyChangeListener("value", evt -> calcularCostoTotal());

        // Listener para actualizar el costo total cuando cambie el costo por tiempo de uso
        campoCostoPorTiempoUso.addPropertyChangeListener("value", evt -> calcularCostoTotal());
    }

    private void calcularCostoTotal() {
        try {
            // Obteniendo los valores de los campos
            double tiempoEstimado = ((Number) campoTiempoEstimado.getValue()).doubleValue();
            double costoPorTiempo = ((Number) campoCostoPorTiempoUso.getValue()).doubleValue();

            // Calculando el costo total
            double costoTotal = tiempoEstimado * costoPorTiempo;

            // Asignar el valor formateado a campoCostoTotal
            campoCostoTotal.setValue(costoTotal);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, "Error al calcular el costo total: " + ex.getMessage());
        }
    }

    private void createUIComponents() {
        // Configurar el campo de tiempo estimado (horas y minutos)
        NumberFormat formatoTiempo = NumberFormat.getNumberInstance();
        campoTiempoEstimado = new JFormattedTextField(formatoTiempo);

        // Configurar el campo de costo por tiempo (2 decimales y separador de miles) usando locale.es
        NumberFormat formatoCosto = DecimalFormat.getNumberInstance(Locale.forLanguageTag("es"));
        formatoCosto.setMaximumFractionDigits(2);
        formatoCosto.setMinimumFractionDigits(2);
        formatoCosto.setGroupingUsed(true);
        campoCostoPorTiempoUso = new JFormattedTextField(formatoCosto);

        // Configurar el campo de costo total (no editable, con 2 decimales y separador de miles)
        campoCostoTotal = new JFormattedTextField(formatoCosto);
        campoCostoTotal.setEditable(false); // No editable

        // Configurar el combo box de tipo de maquinaria
        comboboxTipoMaquinaria = new JComboBox<>();
        tipoMaquinariaRepo.obtenerTodos().forEach(tipoMaquinaria -> comboboxTipoMaquinaria.addItem(tipoMaquinaria.getNombre()));
    }
}
