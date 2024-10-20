package com.criollo.machmillenium.vistas.emergentes.presupuesto;

import com.criollo.machmillenium.entidades.Cliente;
import com.criollo.machmillenium.entidades.Presupuesto;
import com.criollo.machmillenium.modelos.ModeloCliente;
import com.criollo.machmillenium.repos.ClienteRepo;
import com.criollo.machmillenium.repos.PresupuestoRepo;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class EditarPresupuesto {
    public JPanel panel;
    private JFormattedTextField campoCosto;
    private JFormattedTextField tiempoEstimado;
    private JComboBox comboboxCliente;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JButton cerrarButton;
    private JButton agregarButton;
    private final PresupuestoRepo presupuestoRepo;
    private final Presupuesto presupuesto;

    public EditarPresupuesto(PresupuestoRepo presupuestoRepo, Presupuesto presupuesto) {
        if (presupuesto == null) {
            JOptionPane.showMessageDialog(null, "No se puede editar un presupuesto nulo", "Error", JOptionPane.ERROR_MESSAGE);
            this.presupuestoRepo = null;
            this.presupuesto = null;
            return;
        }

        this.presupuestoRepo = presupuestoRepo;
        this.presupuesto = presupuesto;

        // Setear los valores del presupuesto en los campos
        campoCosto.setValue(presupuesto.getCosto());
        String tiempo = String.format("%03d:%02d", presupuesto.getTiempoEstimado().toHours(), presupuesto.getTiempoEstimado().toMinutes() % 60);
        tiempoEstimado.setValue(tiempo);
        textArea1.setText(presupuesto.getDescripcion());
        textArea2.setText(presupuesto.getDireccion());

        comboboxCliente.setSelectedItem(presupuesto.getCliente().getNombre());

        agregarButton.addActionListener(e -> {
            ClienteRepo clienteRepo = new ClienteRepo();
            // Crear un nuevo presupuesto con los datos ingresados
            // quitar los puntos de los miles y reemplazar la coma por punto
            String costoStr = campoCosto.getText().replaceAll("\\.", "").replace(',', '.');
            double costo;
            // si el costo tiene como decimales 00, se colocan en el Double
            if (costoStr.endsWith("00")) {
                costo = Double.parseDouble(costoStr.substring(0, costoStr.length() - 3));
            } else {
                costo = Double.parseDouble(costoStr);
            }

            String tiempoEstimadoStr = tiempoEstimado.getText();
            String[] tiempoEstimadoParts = tiempoEstimadoStr.split(":");
            int horas = Integer.parseInt(tiempoEstimadoParts[0]);
            int minutos = Integer.parseInt(tiempoEstimadoParts[1]);
            Duration tiempoEstimado = Duration.ofHours(horas).plusMinutes(minutos);
            String descripcion = textArea1.getText();
            String direccion = textArea2.getText();

            String nombreCliente = (String) comboboxCliente.getSelectedItem();
            Cliente cliente = clienteRepo.obtenerPorNombre(nombreCliente);

            // Crear el presupuesto
            Presupuesto presupuestoEditado = new Presupuesto(this.presupuesto.getId(),descripcion, direccion, tiempoEstimado, costo, cliente);
            presupuestoRepo.actualizar(presupuestoEditado);
            JOptionPane.showMessageDialog(null, "Presupuesto editado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.getWindowAncestor(agregarButton).dispose();
        });
        cerrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.getWindowAncestor(cerrarButton).dispose();
            }
        });
    }

    private void createUIComponents() {
        ClienteRepo clienteRepo = new ClienteRepo();
        NumberFormat numberFormatter = DecimalFormat.getNumberInstance(Locale.forLanguageTag("es-VE"));
        numberFormatter.setGroupingUsed(true);
        numberFormatter.setMaximumFractionDigits(2);
        numberFormatter.setMinimumFractionDigits(2);
        campoCosto = new JFormattedTextField(numberFormatter);

        try {
            MaskFormatter horaMinutoFormatter = new MaskFormatter("###:##");
            horaMinutoFormatter.setPlaceholderCharacter('0');
            tiempoEstimado = new JFormattedTextField(horaMinutoFormatter);
        } catch (Exception e) {
            e.printStackTrace();
            tiempoEstimado = new JFormattedTextField();
        }

        List<ModeloCliente> clientes = clienteRepo.obtenerTodos();
        String[] nombresclientes = clientes.stream().map(ModeloCliente::getNombre).toArray(String[]::new);
        comboboxCliente = new JComboBox<>(nombresclientes);
    }
}
