package com.criollo.machmillenium.vistas.emergentes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecuperarClave {
    private JPanel mainPanel;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JButton submitButton;
    private JPasswordField secretcode;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton backButton;

    public RecuperarClave() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (new String(secretcode.getPassword()).trim().equals("1234")) {
                    usernameLabel.setText("admin");
                    passwordLabel.setText("1234");
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter correct Secret Code \n                         or\n Contact Developer for Secret Code");
                    secretcode.setText("");
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame loginFrame = new JFrame("Login");
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.pack();
                loginFrame.setVisible(true);
                ((JFrame) SwingUtilities.getWindowAncestor(mainPanel)).dispose(); // Cierra la ventana actual
            }
        });
    }

}
