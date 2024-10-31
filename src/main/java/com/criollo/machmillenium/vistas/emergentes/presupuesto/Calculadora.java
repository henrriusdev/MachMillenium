package com.criollo.machmillenium.vistas.emergentes.presupuesto;

import com.criollo.machmillenium.modelos.ModeloPersonal;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.repos.TipoInsumoRepo;
import com.criollo.machmillenium.repos.TipoMaquinariaRepo;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

public class Calculadora extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<String> personal;
    private JList<String> materiales;
    private JList<String> maquinarias;
    private JTextField costoPersonal;
    private JTextField costoMateriales;
    private JTextField costoMaquinarias;
    private JTextField costoTotal;
    public Double total;
    private JButton calcularButton;

    public Calculadora() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        calcularButton.addActionListener(e -> {
            Double costo = Double.parseDouble(String.valueOf(personal.getSelectedValuesList().size())) * 145.6;
            Double material = materiales.getSelectedValuesList().stream().mapToDouble(mat -> Double.parseDouble(mat.split(" - ")[1])).sum();
            Double maquinaria = maquinarias.getSelectedValuesList().stream().mapToDouble(maq -> Double.parseDouble(maq.split(" - ")[1])).sum();
            Double total = costo + material + maquinaria;
            this.total = total;

            //format with 2 decimal places
            costoPersonal.setText(String.format("%.2f", costo));
            costoMateriales.setText(String.format("%.2f", material));
            costoMaquinarias.setText(String.format("%.2f", maquinaria));
            costoTotal.setText(String.format("%.2f", total));
        });
    }

    private void onOK() {
        int respuesta = JOptionPane.showConfirmDialog(null, "Se le copiará el total al portapapeles", "Copiar al portapapeles", JOptionPane.DEFAULT_OPTION);
        if (respuesta == JOptionPane.OK_OPTION) {
            StringSelection stringSelection = new StringSelection(String.valueOf(total));
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {
        PersonalRepo personalRepo = new PersonalRepo();
        TipoMaquinariaRepo tipoMaquinariaRepo = new TipoMaquinariaRepo();
        TipoInsumoRepo tipoInsumoRepo = new TipoInsumoRepo();

        personal = new JList<>(personalRepo.obtenerTodos().stream().map(ModeloPersonal::getNombre).toArray(String[]::new));
        materiales = new JList<>(tipoInsumoRepo.obtenerMateriales().stream().map(mat -> mat.getNombre() + " - " + mat.getCosto()).toArray(String[]::new));
        maquinarias = new JList<>(tipoMaquinariaRepo.obtenerMaquinarias().stream().map(maq -> maq.getNombre() + " - " + maq.getCostoTotal()).toArray(String[]::new));
    }
}
