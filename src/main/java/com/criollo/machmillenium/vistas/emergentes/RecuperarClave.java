package com.criollo.machmillenium.vistas.emergentes;

import com.criollo.machmillenium.repos.PersonalRepo;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;

public class RecuperarClave {
    public JPanel mainPanel;
    private JPasswordField campoClave;
    private JButton enviarButton;
    private JButton cerrarButton;

    public RecuperarClave(String clavePersistida, Long idPersonal) {
        enviarButton.addActionListener(e -> {
            if (campoClave.getPassword().length == 0) {
                JOptionPane.showMessageDialog(null, "Debes ingresar tu clave actual", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // getText is deprecated, use getPassword and then new String
            if (BCrypt.checkpw(new String(campoClave.getPassword()), clavePersistida)) {
                // Aquí va la lógica para cambiar la contraseña
                PersonalRepo personalRepo = new PersonalRepo();
                JPasswordField newPasswordField = new JPasswordField();
                int option = JOptionPane.showConfirmDialog(null, newPasswordField, "Ingrese nueva contraseña", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.CANCEL_OPTION) {
                    return;
                }
                String newPass = new String(newPasswordField.getPassword());
                personalRepo.cambiarClave(idPersonal, BCrypt.hashpw(newPass, BCrypt.gensalt()));
                JOptionPane.showMessageDialog(null, "Clave cambiada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor(mainPanel).dispose(); // Cierra la ventana actual
            } else {
                JOptionPane.showMessageDialog(null, "Tu clave actual es incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
                campoClave.setText("");
            }
        });

        cerrarButton.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(mainPanel).dispose(); // Cierra la ventana actual
        });
    }

}
