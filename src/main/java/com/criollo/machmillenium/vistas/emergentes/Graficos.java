package com.criollo.machmillenium.vistas.emergentes;

import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class Graficos extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JPanel panelGraficos;

    public Graficos(String titulo, List<ChartPanel> graficos) {
        setContentPane(contentPane);
        setModal(true);
        setTitle(titulo);
        getRootPane().setDefaultButton(buttonOK);

        // Agregar los gráficos al panel
        for (ChartPanel grafico : graficos) {
            panelGraficos.add(grafico);
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

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
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
