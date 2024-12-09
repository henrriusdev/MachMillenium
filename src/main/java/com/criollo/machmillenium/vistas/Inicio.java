package com.criollo.machmillenium.vistas;

import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.utilidades.Utilidades;
import com.criollo.machmillenium.vistas.admin.Administrador;
import com.criollo.machmillenium.vistas.gestor.GestorProyectos;
import com.criollo.machmillenium.vistas.usuario.UsuarioOperativo;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Inicio {
    private JFormattedTextField campoCorreo;
    private JPasswordField campoClave;
    public JPanel panelPrincipal;
    private JButton btnIniciar;
    private JButton btnSalir;
    private JButton btnRecuperarClave;
    private JLabel label1;
    private final JFrame jframe;

    public Inicio(JFrame jframe) {
        this.jframe = jframe;
        btnIniciar.addActionListener(actionEvent -> {
            String correo = campoCorreo.getText();
            String clave = new String(campoClave.getPassword());
            Personal personal = iniciarSesion(correo, clave);

            if (personal == null) {
                campoClave.setText("");
                campoCorreo.setText("");
                campoCorreo.requestFocus();
                return;
            }

            switch (personal.getRol().getId().intValue()) {
                case 1:
                    jframe.setContentPane(new Administrador(personal).panel);
                    break;
                case 2:
                    jframe.setContentPane(new GestorProyectos(personal).panel);
                    break;
                case 3:
                    jframe.setContentPane(new UsuarioOperativo(personal).panel);
                    break;
                case 4:
                    JOptionPane.showMessageDialog(null, "No tiene permisos para acceder a esta aplicación");
                    return;
                default:
                    break;
            }

            jframe.pack();
            jframe.setVisible(true);
            jframe.repaint();

            jframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            jframe.setTitle("Bienvenido, " + personal.getNombre());
            jframe.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    int option = JOptionPane.showConfirmDialog(jframe, "¿Está seguro que desea salir?", "Salir", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            });
        });

        btnSalir.addActionListener(actionEvent -> System.exit(0));

        btnRecuperarClave.addActionListener(actionEvent -> {
            String correo = JOptionPane.showInputDialog(null, "Ingrese su correo electrónico:", 
                "Recuperar Contraseña", JOptionPane.QUESTION_MESSAGE);
            
            if (correo == null || correo.trim().isEmpty()) {
                return;
            }

            // Ask for security question answers
            String respuesta1 = JOptionPane.showInputDialog(null, 
                "¿En qué ciudad naciste?", "Pregunta de Seguridad 1", JOptionPane.QUESTION_MESSAGE);
            if (respuesta1 == null) return;

            String respuesta2 = JOptionPane.showInputDialog(null, 
                "¿Cuál es tu color favorito?", "Pregunta de Seguridad 2", JOptionPane.QUESTION_MESSAGE);
            if (respuesta2 == null) return;

            String respuesta3 = JOptionPane.showInputDialog(null, 
                "¿Cual es tu comida favorita?", "Pregunta de Seguridad 3", JOptionPane.QUESTION_MESSAGE);
            if (respuesta3 == null) return;

            if (Utilidades.restablecerContrasena(correo, respuesta1, respuesta2, respuesta3)) {
                JOptionPane.showMessageDialog(null, 
                    "Su contraseña ha sido actualizada exitosamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "No se pudo restablecer la contraseña. Verifique sus respuestas", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private Personal iniciarSesion(String correo, String clave) {
        PersonalRepo personalRepo = new PersonalRepo();
        Personal personal = personalRepo.obtenerPorCorreo(correo);
        if (personal == null) {
            JOptionPane.showMessageDialog(null, "Correo y/o contraseña incorrectos");
            return null;
        }

        if (!BCrypt.checkpw(clave, personal.getClave())) {
            JOptionPane.showMessageDialog(null, "Correo y/o contraseña incorrectos");
            return null;
        }

        return personal;
    }

    private void createUIComponents() {
        label1 = new JLabel(new ImageIcon("main.jpg"));
    }
}