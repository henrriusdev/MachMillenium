package com.criollo.machmillenium.vistas.emergentes.maquinaria;

import com.criollo.machmillenium.entidades.Maquinaria;
import com.criollo.machmillenium.entidades.TipoMaquinaria;
import com.criollo.machmillenium.modelos.ModeloMaquinaria;
import com.criollo.machmillenium.repos.TipoMaquinariaRepo;

import javax.swing.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;

public class ModificarMaquinaria {

    public JPanel mainPanel;
    private JTextField campoNombre;
    private JComboBox<String> comboboxTipoMaquinaria;
    private JFormattedTextField campoCostoPorTiempoUso;
    private JFormattedTextField campoTiempoEstimado;
    private JButton cerrarButton;
    private JButton editarButton;
    private JFormattedTextField campoCostoTotal;
    private final TipoMaquinariaRepo tipoMaquinariaRepo;
    private final ModeloMaquinaria modeloMaquinaria;

    public ModificarMaquinaria(ModeloMaquinaria modeloMaquinaria) {
        if (modeloMaquinaria == null) {
            JOptionPane.showMessageDialog(mainPanel, "No se puede modificar una maquinaria nula");
            this.modeloMaquinaria = null;
            this.tipoMaquinariaRepo = null;
            return;
        }

        this.modeloMaquinaria = modeloMaquinaria;
        this.tipoMaquinariaRepo = new TipoMaquinariaRepo();

        // Configurar los campos con los valores de la maquinaria
        campoNombre.setText(modeloMaquinaria.getNombre());

        TipoMaquinaria tipoMaquinaria = tipoMaquinariaRepo.obtenerPorId(modeloMaquinaria.getTipoMaquinariaId());
        comboboxTipoMaquinaria.setSelectedItem(tipoMaquinaria.getNombre());

        long horas = Long.parseLong(modeloMaquinaria.getTiempoEstimadoDeUso().split(" ")[0]);
        long minutos = Long.parseLong(modeloMaquinaria.getTiempoEstimadoDeUso().split(" ")[3]);
        campoTiempoEstimado.setValue(horas + minutos / 60);

        campoCostoPorTiempoUso.setValue(modeloMaquinaria.getCostoPorTiempoDeUso());
        campoCostoTotal.setValue(modeloMaquinaria.getCostoTotal());

        cerrarButton.addActionListener(e -> {
            // Cerrar la ventana emergente
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.dispose();
        });

        editarButton.addActionListener(e -> {
            // Actualizar la maquinaria en la base de datos
            try {
                // Crear un objeto de Maquinaria con los valores de los campos
                Maquinaria maquinaria = new Maquinaria();
                maquinaria.setId(modeloMaquinaria.getId());
                maquinaria.setNombre(campoNombre.getText());

                TipoMaquinaria tipoMaquinariaNuevo = tipoMaquinariaRepo.obtenerPorNombre((String) comboboxTipoMaquinaria.getSelectedItem());
                maquinaria.setTipoMaquinaria(tipoMaquinariaNuevo);

                double tiempoEstimado = ((Number) campoTiempoEstimado.getValue()).doubleValue();
                maquinaria.setTiempoEstimadoDeUso(Duration.ofHours((long) tiempoEstimado));

                maquinaria.setCostoPorTiempoDeUso(((Number) campoCostoPorTiempoUso.getValue()).doubleValue());
                maquinaria.setCostoTotal(((Number) campoCostoTotal.getValue()).doubleValue());

                // Actualizar la maquinaria en la base de datos
                tipoMaquinariaRepo.actualizarMaquinaria(maquinaria);

                // Mostrar un mensaje de éxito
                JOptionPane.showMessageDialog(mainPanel, "Maquinaria actualizada correctamente");
                // Cerrar la ventana emergente
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel, "Error al actualizar la maquinaria: " + ex.getMessage());
            }
        });

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
        TipoMaquinariaRepo tipoMaquinariaRepo = new TipoMaquinariaRepo();
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
