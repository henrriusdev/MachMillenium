package com.criollo.machmillenium.vistas;

import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.PreguntasSeguridad;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.repos.PreguntasSeguridadRepo;
import com.criollo.machmillenium.utilidades.Utilidades;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Inicio {
    private JFormattedTextField campoCorreo;
    private JPasswordField campoClave;
    public JPanel panelPrincipal;
    private JButton btnIniciar;
    private JButton btnSalir;
    private JButton btnRecuperarClave;
    private JLabel label1;
    private final JFrame jframe;
    private int intentosFallidos = 0;
    private static final int MAX_INTENTOS = 3;
    private long tiempoBloqueo = 0;
    private static final long TIEMPO_ESPERA = 5 * 60 * 1000; // 5 minutos en milisegundos

    public Inicio(JFrame jframe) {
        this.jframe = jframe;
        
        // Configurar eventos de teclado
        campoCorreo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    campoClave.requestFocus();
                }
            }
        });

        campoClave.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    btnIniciar.doClick();
                }
            }
        });

        btnIniciar.addActionListener(actionEvent -> {
            if (tiempoBloqueo > 0 && System.currentTimeMillis() < tiempoBloqueo) {
                long tiempoRestante = (tiempoBloqueo - System.currentTimeMillis()) / 1000;
                JOptionPane.showMessageDialog(null,
                    String.format("Cuenta bloqueada. Intente nuevamente en %d minutos y %d segundos",
                        tiempoRestante / 60, tiempoRestante % 60),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            String correo = campoCorreo.getText().trim().toLowerCase();
            String clave = new String(campoClave.getPassword());

            // Validar campos vacíos
            if (correo.isEmpty() || clave.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "Por favor complete todos los campos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validar formato de correo
            if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(null,
                    "Por favor ingrese un correo electrónico válido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                campoCorreo.requestFocus();
                return;
            }

            Personal personal = iniciarSesion(correo, clave);

            if (personal == null) {
                intentosFallidos++;
                if (intentosFallidos >= MAX_INTENTOS) {
                    tiempoBloqueo = System.currentTimeMillis() + TIEMPO_ESPERA;
                    JOptionPane.showMessageDialog(null,
                        "Ha excedido el número máximo de intentos. La cuenta será bloqueada por 5 minutos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    intentosFallidos = 0;
                } else {
                    JOptionPane.showMessageDialog(null,
                        String.format("Credenciales incorrectas. Intentos restantes: %d",
                            MAX_INTENTOS - intentosFallidos),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                campoClave.setText("");
                return;
            }

            intentosFallidos = 0;
            tiempoBloqueo = 0;

            // Crear y mostrar la vista Mach
            try {
                Mach mach = new Mach(personal);
                jframe.setContentPane(mach.panel);
                jframe.pack();
                jframe.setLocationRelativeTo(null);
                jframe.setVisible(true);
                jframe.repaint();

                jframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                jframe.setTitle("Bienvenido, " + personal.getNombre());
                jframe.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        int option = JOptionPane.showConfirmDialog(jframe,
                            "¿Está seguro que desea salir?",
                            "Salir",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                        if (option == JOptionPane.YES_OPTION) {
                            System.exit(0);
                        }
                    }
                });
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Error al cargar la aplicación: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });

        btnSalir.addActionListener(actionEvent -> {
            int option = JOptionPane.showConfirmDialog(jframe,
                "¿Está seguro que desea salir?",
                "Salir",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        btnRecuperarClave.addActionListener(actionEvent -> {
            try {
                String correo = JOptionPane.showInputDialog(null,
                    "Ingrese su correo electrónico:",
                    "Recuperar Contraseña",
                    JOptionPane.QUESTION_MESSAGE);

                if (correo == null || correo.trim().isEmpty()) {
                    return;
                }

                correo = correo.trim().toLowerCase();
                
                // Validar formato de correo
                if (!correo.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    JOptionPane.showMessageDialog(null,
                        "Por favor ingrese un correo electrónico válido",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PersonalRepo personalRepo = new PersonalRepo();
                Personal personal = personalRepo.obtenerPorCorreo(correo);
                
                if (personal == null) {
                    JOptionPane.showMessageDialog(null,
                        "No se encontró ninguna cuenta asociada a este correo",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PreguntasSeguridadRepo preguntasSeguridadRepo = new PreguntasSeguridadRepo();
                PreguntasSeguridad preguntasSeguridad = preguntasSeguridadRepo.obtenerPorPersonal(personal);

                if (preguntasSeguridad == null) {
                    JOptionPane.showMessageDialog(null,
                        "No se encontraron preguntas de seguridad configuradas para esta cuenta",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Ask for security question answers
                String respuesta1 = JOptionPane.showInputDialog(null,
                    preguntasSeguridad.getPregunta1(),
                    "Pregunta de Seguridad 1",
                    JOptionPane.QUESTION_MESSAGE);
                if (respuesta1 == null) return;

                String respuesta2 = JOptionPane.showInputDialog(null,
                    preguntasSeguridad.getPregunta2(),
                    "Pregunta de Seguridad 2",
                    JOptionPane.QUESTION_MESSAGE);
                if (respuesta2 == null) return;

                String respuesta3 = JOptionPane.showInputDialog(null,
                    preguntasSeguridad.getPregunta3(),
                    "Pregunta de Seguridad 3",
                    JOptionPane.QUESTION_MESSAGE);
                if (respuesta3 == null) return;

                if (Utilidades.restablecerContrasena(correo, respuesta1.trim().toLowerCase(), 
                    respuesta2.trim().toLowerCase(), respuesta3.trim().toLowerCase())) {
                    JOptionPane.showMessageDialog(null,
                        "Su contraseña ha sido restablecida a: 12345678\n" +
                        "Por favor, cambie su contraseña al iniciar sesión",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                        "Las respuestas proporcionadas no coinciden con las registradas",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                    "Error al procesar la solicitud: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    private Personal iniciarSesion(String correo, String clave) {
        try {
            PersonalRepo personalRepo = new PersonalRepo();
            Personal personal = personalRepo.obtenerPorCorreo(correo);
            
            if (personal == null || !BCrypt.checkpw(clave, personal.getClave())) {
                return null;
            }

            return personal;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error al verificar credenciales: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    private void createUIComponents() {
        try {
            label1 = new JLabel(new ImageIcon("main.jpg"));
        } catch (Exception e) {
            label1 = new JLabel("Error al cargar imagen");
            e.printStackTrace();
        }
    }
}