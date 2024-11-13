package com.criollo.machmillenium.vistas.emergentes.clientes;

import com.criollo.machmillenium.entidades.Obra;
import com.criollo.machmillenium.entidades.Pago;
import com.criollo.machmillenium.repos.ObraRepo;
import com.criollo.machmillenium.repos.PagoRepo;
import com.criollo.machmillenium.utilidades.Utilidades;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;

public class RegistrarPago {
    private JRadioButton siRadioButton;
    private JRadioButton noRadioButton;
    private JFormattedTextField campoCuotas;
    private JTextField campoMonto;
    private JButton cancelarButton;
    private JButton registrarButton;
    private JComboBox<String> obraCombo;
    private JComboBox<String> metodoCombo;
    private JTextField campoCliente;
    public JPanel panel;
    private JTextField campoCostoCuota;
    public PagoRepo pagoRepo;
    public ObraRepo obraRepo;

    private double monto;

    public RegistrarPago() {
        this.pagoRepo = new PagoRepo();
        this.obraRepo = new ObraRepo();
        registrarButton.addActionListener(e -> {
            if (campoCliente.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una obra", "Pago no registrado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!siRadioButton.isSelected() && !noRadioButton.isSelected()) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar si el pago se realizará en cuotas o no", "Pago no registrado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (siRadioButton.isSelected() && campoCuotas.getValue() == null) {
                JOptionPane.showMessageDialog(null, "Debe ingresar la cantidad de cuotas", "Pago no registrado", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String obraSeleccionada = (String) obraCombo.getSelectedItem();
            String metodo = (String) metodoCombo.getSelectedItem();
            boolean pagoCuotas = siRadioButton.isSelected();

            Obra obra = obraRepo.obtenerPorNombre(obraSeleccionada);

            Pago pago = new Pago();
            pago.setObra(obra);
            pago.setMetodoPago(metodo);
            pago.setCuotas(pagoCuotas);
            pago.setCreado(LocalDateTime.now());

            if (pagoCuotas) {
                Long cuotas = (Long) campoCuotas.getValue();
                pago.setCantidadCuotas(cuotas.intValue());
                pago.setMonto(obra.getPresupuesto().getCosto() / cuotas);
            } else {
                pago.setCantidadCuotas(1);
                pago.setMonto(obra.getPresupuesto().getCosto());
            }

            pagoRepo.registrarPago(pago);

            JOptionPane.showMessageDialog(null, "Pago registrado con éxito", "Pago registrado", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.getWindowAncestor(registrarButton).dispose();
        });
        cancelarButton.addActionListener(e -> SwingUtilities.getWindowAncestor(cancelarButton).dispose());
        obraCombo.addActionListener(arg0 -> {
            String obraSeleccionada = (String) obraCombo.getSelectedItem();
            Obra obra = obraRepo.obtenerPorNombre(obraSeleccionada);
            campoCliente.setText(obra.getPresupuesto().getCliente().getNombre());
            this.monto = obra.getPresupuesto().getCosto();
            campoMonto.setText(String.valueOf(this.monto));
        });
        siRadioButton.addActionListener(e -> campoCuotas.setEnabled(true));
        noRadioButton.addActionListener(e -> {
            campoCuotas.setValue(1);
            campoCuotas.setEnabled(false);
            campoCostoCuota.setText(String.valueOf(this.monto));
        });
        campoCuotas.addPropertyChangeListener("value", evt -> {
            if (evt.getNewValue() == null) {
                campoCostoCuota.setText("0");
                return;
            }
            Long cuotas = (Long) campoCuotas.getValue();
            campoCostoCuota.setText(String.valueOf(this.monto / cuotas));
            campoCuotas.setValue(cuotas);
        });
    }

    private void createUIComponents() {
        ObraRepo obraRepo = new ObraRepo();
        List<String> metodos = List.of("Efectivo", "Tarjeta de crédito", "Tarjeta de débito", "Transferencia bancaria");

        metodoCombo = new JComboBox<>(metodos.toArray(new String[0]));
        obraCombo = new JComboBox<>(obraRepo.obtenerObras().stream().map(Obra::getNombre).toArray(String[]::new));

        NumberFormatter formateador = Utilidades.getNumberFormatterSinGrupo();
        campoCuotas = new JFormattedTextField(formateador);
    }
}
