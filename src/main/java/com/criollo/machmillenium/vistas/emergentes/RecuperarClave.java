package com.criollo.machmillenium.vistas.emergentes;

import com.criollo.machmillenium.repos.PersonalRepo;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RecuperarClave {
    public JPanel mainPanel;
    private JPasswordField campoClave;
    private JButton enviarButton;
    private JButton cerrarButton;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 50;

    public RecuperarClave(String clavePersistida, Long idPersonal) {
        if (clavePersistida == null || idPersonal == null) {
            throw new IllegalArgumentException("La clave persistida y el ID de personal no pueden ser nulos");
        }

        // Configurar eventos de teclado
        campoClave.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enviarButton.doClick();
                }
            }
        });

        enviarButton.addActionListener(e -> {
            try {
                // Validar clave actual
                char[] claveActual = campoClave.getPassword();
                if (claveActual == null || claveActual.length == 0) {
                    JOptionPane.showMessageDialog(mainPanel,
                        "Por favor ingrese su clave actual",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    campoClave.requestFocus();
                    return;
                }

                String claveActualStr = new String(claveActual);
                
                // Verificar clave actual
                if (!BCrypt.checkpw(claveActualStr, clavePersistida)) {
                    JOptionPane.showMessageDialog(mainPanel,
                        "La clave actual es incorrecta",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    campoClave.setText("");
                    campoClave.requestFocus();
                    return;
                }

                // Solicitar y validar nueva clave
                JPasswordField newPasswordField = new JPasswordField();
                JPasswordField confirmPasswordField = new JPasswordField();

                JPanel panel = new JPanel(new GridLayout(4, 1));
                panel.add(new JLabel("Nueva contraseña:"));
                panel.add(newPasswordField);
                panel.add(new JLabel("Confirmar contraseña:"));
                panel.add(confirmPasswordField);

                int option = JOptionPane.showConfirmDialog(mainPanel,
                    panel,
                    "Cambiar Contraseña",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

                if (option != JOptionPane.OK_OPTION) {
                    return;
                }

                char[] newPass = newPasswordField.getPassword();
                char[] confirmPass = confirmPasswordField.getPassword();

                // Validar nueva clave
                if (newPass == null || newPass.length == 0) {
                    JOptionPane.showMessageDialog(mainPanel,
                        "La nueva contraseña no puede estar vacía",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String newPassStr = new String(newPass);
                String confirmPassStr = new String(confirmPass);

                // Validar longitud
                if (newPassStr.length() < MIN_PASSWORD_LENGTH || newPassStr.length() > MAX_PASSWORD_LENGTH) {
                    JOptionPane.showMessageDialog(mainPanel,
                        String.format("La contraseña debe tener entre %d y %d caracteres",
                            MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar que las contraseñas coincidan
                if (!newPassStr.equals(confirmPassStr)) {
                    JOptionPane.showMessageDialog(mainPanel,
                        "Las contraseñas no coinciden",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar que la nueva clave sea diferente a la actual
                if (newPassStr.equals(claveActualStr)) {
                    JOptionPane.showMessageDialog(mainPanel,
                        "La nueva contraseña debe ser diferente a la actual",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validar complejidad de la contraseña
                if (!validarComplejidadClave(newPassStr)) {
                    JOptionPane.showMessageDialog(mainPanel,
                        "La contraseña debe contener al menos:\n" +
                        "- Una letra mayúscula\n" +
                        "- Una letra minúscula\n" +
                        "- Un número\n" +
                        "- Un carácter especial (!@#$%^&*(),.?\":{}|<>)",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Cambiar la clave
                try {
                    PersonalRepo personalRepo = new PersonalRepo();
                    personalRepo.cambiarClave(idPersonal, BCrypt.hashpw(newPassStr, BCrypt.gensalt()));
                    
                    JOptionPane.showMessageDialog(mainPanel,
                        "La contraseña ha sido cambiada exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    Window window = SwingUtilities.getWindowAncestor(mainPanel);
                    if (window != null) {
                        window.dispose();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(mainPanel,
                        "Error al cambiar la contraseña: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Error inesperado: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                // Limpiar datos sensibles de la memoria
                campoClave.setText("");
            }
        });

        cerrarButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(mainPanel);
            if (window != null) {
                window.dispose();
            }
        });
    }

    private boolean validarComplejidadClave(String clave) {
        boolean tieneMayuscula = false;
        boolean tieneMinuscula = false;
        boolean tieneNumero = false;
        boolean tieneEspecial = false;
        String caracteresEspeciales = "!@#$%^&*(),.?\":{}|<>";

        for (char c : clave.toCharArray()) {
            if (Character.isUpperCase(c)) tieneMayuscula = true;
            else if (Character.isLowerCase(c)) tieneMinuscula = true;
            else if (Character.isDigit(c)) tieneNumero = true;
            else if (caracteresEspeciales.indexOf(c) >= 0) tieneEspecial = true;
        }

        return tieneMayuscula && tieneMinuscula && tieneNumero && tieneEspecial;
    }
}
