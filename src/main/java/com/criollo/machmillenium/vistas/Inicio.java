package com.criollo.machmillenium.vistas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Inicio {
    private JLabel fondo;
    private JFormattedTextField campoCorreo;
    private JPasswordField campoClave;
    public JPanel panel;
    private JButton btnIniciar;
    private JButton btnSalir;

    public Inicio() {
        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // todo: Implementar la lógica de inicio de sesión
                if (campoCorreo.getText().equals("admin") && campoClave.getText().equals("admin")) {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión exitoso", "Inicio de sesión", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Inicio de sesión fallido", "Inicio de sesión", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0); // Salir de la aplicación
            }
        });
    }

    private void createUIComponents() {

    }
}
