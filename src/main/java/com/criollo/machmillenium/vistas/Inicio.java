package com.criollo.machmillenium.vistas;

import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.vistas.admin.Administrador;
import com.criollo.machmillenium.vistas.gestor.GestorProyectos;
import com.criollo.machmillenium.vistas.usuario.UsuarioOperativo;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Inicio {
    private JLabel fondo;
    private JFormattedTextField campoCorreo;
    private JPasswordField campoClave;
    public JPanel panelPrincipal;
    private JButton btnIniciar;
    private JButton btnSalir;
    private JPanel panel;
    private JFrame jframe;

    public Inicio() {}

    public Inicio(JFrame jframe) throws UnsupportedLookAndFeelException {
        this.jframe = jframe;
        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String correo = campoCorreo.getText();
                String clave = new String(campoClave.getPassword());
                Long rol = iniciarSesion(correo, clave);
                System.out.println(rol);

                switch (rol.intValue()) {
                    case 1:
                        jframe.setContentPane(new Administrador(jframe).panel);
                        jframe.setTitle("Administrador");
                        break;
                    case 2:
                        jframe.setContentPane(new GestorProyectos(jframe).panel);
                        jframe.setTitle("Gestor de Proyectos");
                        break;
                    case 3:
                        jframe.setContentPane(new UsuarioOperativo(jframe).panel);
                        jframe.setTitle("Usuario");
                        break;
                    default:
                        break;
                }

                jframe.pack();
                jframe.setVisible(true);
                jframe.repaint();
            }
        });

        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }

    private Long iniciarSesion(String correo, String clave) {
        PersonalRepo personalRepo = new PersonalRepo();
        Personal personal = personalRepo.obtenerPorCorreo(correo);
        if (personal == null) {
            JOptionPane.showMessageDialog(null, "Correo y/o contraseña incorrectos");
            return 0L;
        }

        if (!BCrypt.checkpw(clave, personal.getClave())) {
            JOptionPane.showMessageDialog(null, "Correo y/o contraseña incorrectos");
            return 0L;
        }

        return personal.getRol().getId();
    }
}