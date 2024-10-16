package com.criollo.machmillenium.vistas.emergentes.maquinaria;

import com.criollo.machmillenium.entidades.Maquinaria;
import com.criollo.machmillenium.entidades.TipoMaquinaria;
import com.criollo.machmillenium.repos.TipoMaquinariaRepo;

import javax.swing.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;

public class AgregarMaquinaria {
    private JTextField campoNombre;
    private JComboBox<String> comboboxTipoMaquinaria;
    private JFormattedTextField campoCostoPorTiempoUso;
    private JFormattedTextField campoTiempoEstimado;
    private JButton cerrarButton;
    private JButton agregarButton;
    private JFormattedTextField campoCostoTotal;
    public JPanel mainPanel;
    private final TipoMaquinariaRepo tipoMaquinariaRepo;

    public AgregarMaquinaria() {
        this.tipoMaquinariaRepo = new TipoMaquinariaRepo();
        campoTiempoEstimado.addPropertyChangeListener("value", evt -> calcularCostoTotal());

        // Listener para actualizar el costo total cuando cambie el costo por tiempo de uso
        campoCostoPorTiempoUso.addPropertyChangeListener("value", evt -> calcularCostoTotal());
        cerrarButton.addActionListener(e -> {
            // Cerrar la ventana emergente
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.dispose();
        });
        agregarButton.addActionListener(e -> {
            // Insertar la maquinaria en la base de datos
            try {
                // Crear un objeto de ModeloMaquinaria con los valores de los campos
                Maquinaria maquinaria = new Maquinaria();
                maquinaria.setNombre(campoNombre.getText());

                TipoMaquinaria tipoMaquinaria = tipoMaquinariaRepo.obtenerPorNombre((String) comboboxTipoMaquinaria.getSelectedItem());
                maquinaria.setTipoMaquinaria(tipoMaquinaria);

                Duration tiempoEstimado = Duration.ofHours(((Number) campoTiempoEstimado.getValue()).longValue());
                maquinaria.setTiempoEstimadoDeUso(tiempoEstimado);

                // Obtener el valor de campoCostoPorTiempoUso como Double
                maquinaria.setCostoPorTiempoDeUso(((Number) campoCostoPorTiempoUso.getValue()).doubleValue());

                // Obtener el valor de campoCostoTotal como Double
                maquinaria.setCostoTotal(((Number) campoCostoTotal.getValue()).doubleValue());

                // Insertar la maquinaria en la base de datos
                tipoMaquinariaRepo.insertarMaquinaria(maquinaria);

                // Mostrar un mensaje de éxito
                JOptionPane.showMessageDialog(mainPanel, "Maquinaria insertada correctamente");
                // Cerrar la ventana emergente
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                frame.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel, "Error al insertar la maquinaria: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    private void calcularCostoTotal() {
        try {
            if (campoTiempoEstimado.getValue() == null || campoCostoPorTiempoUso.getValue() == null) {
                return;
            }
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
