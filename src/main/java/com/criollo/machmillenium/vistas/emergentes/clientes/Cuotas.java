package com.criollo.machmillenium.vistas.emergentes.clientes;

import com.criollo.machmillenium.entidades.Cuota;
import com.criollo.machmillenium.entidades.Pago;
import com.criollo.machmillenium.modelos.ModeloRecibo;
import com.criollo.machmillenium.repos.PagoRepo;
import com.criollo.machmillenium.utilidades.GeneradorReportes;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Cuotas extends JDialog {
    public JPanel contentPane;
    private JButton buttonOK;
    private JTable tablaCuotas;
    private PagoRepo pagoRepo;
    private Long idPago;

    public Cuotas(PagoRepo pagoRepo, Long idPago) {
        this.pagoRepo = pagoRepo;
        this.idPago = idPago;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setearTabla();

        buttonOK.addActionListener(e -> onOK());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        tablaCuotas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tablaCuotas.getSelectedRow() != -1) {
                    Pago pago = pagoRepo.obtenerPagoPorId(idPago);
                    ModeloRecibo modeloRecibo = new ModeloRecibo(pago, pagoRepo.obtenerCuotasPorPagoId(idPago));
                    GeneradorReportes.generarRecibo(modeloRecibo);
                }
            }
        });
    }

    private void onOK() {
        Double monto = Double.parseDouble(JOptionPane.showInputDialog(this, "Ingrese el monto a pagar", "Pago de cuota", JOptionPane.QUESTION_MESSAGE));
        LocalDateTime fecha = LocalDateTime.now();
        Pago pago = pagoRepo.obtenerPagoPorId(idPago);
        Cuota cuota = new Cuota(fecha, monto, pago);

        if (pago.getObra().getPresupuesto().getCosto() - monto < 0) {
            JOptionPane.showMessageDialog(this, "El monto ingresado es mayor al monto restante", "Pago de cuota", JOptionPane.ERROR_MESSAGE);
            return;
        }

        pagoRepo.registrarCuota(cuota);
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void mapearTabla(List<Cuota> cuotas) {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("Monto");
        modelo.addColumn("Fecha de Pago");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Cuota cuota : cuotas) {
            modelo.addRow(new Object[] {
                cuota.getMonto(),
                cuota.getFecha().format(formatter)
            });
        }

        Pago pago = pagoRepo.obtenerPagoPorId(idPago);
        if (pago.getCantidadCuotas() - cuotas.size() == 0) {
            buttonOK.setEnabled(false);
        }

        tablaCuotas.setModel(modelo);
    }

    private void setearTabla() {
        mapearTabla(pagoRepo.obtenerCuotasPorPagoId(idPago));
    }
}
