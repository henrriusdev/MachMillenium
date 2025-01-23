package com.criollo.machmillenium.vistas.emergentes.personal;

import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.PreguntasSeguridad;
import com.criollo.machmillenium.repos.PreguntasSeguridadRepo;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class ConfigurarPreguntasSeguridad extends JDialog {
    private final Personal personal;
    private final PreguntasSeguridadRepo preguntasSeguridadRepo;
    
    private JTextField pregunta1Field;
    private JTextField pregunta2Field;
    private JTextField pregunta3Field;
    private JPasswordField respuesta1Field;
    private JPasswordField respuesta2Field;
    private JPasswordField respuesta3Field;

    public ConfigurarPreguntasSeguridad(Personal personal) {
        this.personal = personal;
        this.preguntasSeguridadRepo = new PreguntasSeguridadRepo();
        
        setTitle("Configurar Preguntas de Seguridad");
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        initComponents();
        loadExistingQuestions();
        
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Panel principal con GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Pregunta 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Pregunta 1:"), gbc);
        
        gbc.gridx = 1;
        pregunta1Field = new JTextField(30);
        mainPanel.add(pregunta1Field, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Respuesta 1:"), gbc);
        
        gbc.gridx = 1;
        respuesta1Field = new JPasswordField(30);
        mainPanel.add(respuesta1Field, gbc);
        
        // Pregunta 2
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Pregunta 2:"), gbc);
        
        gbc.gridx = 1;
        pregunta2Field = new JTextField(30);
        mainPanel.add(pregunta2Field, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Respuesta 2:"), gbc);
        
        gbc.gridx = 1;
        respuesta2Field = new JPasswordField(30);
        mainPanel.add(respuesta2Field, gbc);
        
        // Pregunta 3
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Pregunta 3:"), gbc);
        
        gbc.gridx = 1;
        pregunta3Field = new JTextField(30);
        mainPanel.add(pregunta3Field, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Respuesta 3:"), gbc);
        
        gbc.gridx = 1;
        respuesta3Field = new JPasswordField(30);
        mainPanel.add(respuesta3Field, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardarButton = new JButton("Guardar");
        JButton cancelarButton = new JButton("Cancelar");
        
        guardarButton.addActionListener(e -> guardarPreguntas());
        cancelarButton.addActionListener(e -> dispose());
        
        buttonPanel.add(guardarButton);
        buttonPanel.add(cancelarButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadExistingQuestions() {
        PreguntasSeguridad preguntas = preguntasSeguridadRepo.obtenerPorPersonal(personal);
        if (preguntas != null) {
            pregunta1Field.setText(preguntas.getPregunta1());
            pregunta2Field.setText(preguntas.getPregunta2());
            pregunta3Field.setText(preguntas.getPregunta3());
            respuesta1Field.setText(preguntas.getRespuesta1());
            respuesta2Field.setText(preguntas.getRespuesta2());
            respuesta3Field.setText(preguntas.getRespuesta3());
        }
    }

    private void guardarPreguntas() {
        // Validar campos
        if (pregunta1Field.getText().trim().isEmpty() || pregunta2Field.getText().trim().isEmpty() || 
            pregunta3Field.getText().trim().isEmpty() || respuesta1Field.getPassword().length == 0 ||
            respuesta2Field.getPassword().length == 0 || respuesta3Field.getPassword().length == 0) {
            
            JOptionPane.showMessageDialog(this,
                "Todos los campos son obligatorios",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        PreguntasSeguridad preguntas = preguntasSeguridadRepo.obtenerPorPersonal(personal);
        boolean esNuevo = preguntas == null;

        if (esNuevo) {
            preguntas = new PreguntasSeguridad();
            preguntas.setPersonal(personal);
        }

        preguntas.setPregunta1(pregunta1Field.getText().trim());
        preguntas.setPregunta2(pregunta2Field.getText().trim());
        preguntas.setPregunta3(pregunta3Field.getText().trim());
        preguntas.setRespuesta1(new String(respuesta1Field.getPassword()).trim());
        preguntas.setRespuesta2(new String(respuesta2Field.getPassword()).trim());
        preguntas.setRespuesta3(new String(respuesta3Field.getPassword()).trim());
        preguntas.setModificado(LocalDateTime.now());

        if (esNuevo) {
            preguntasSeguridadRepo.insertar(preguntas);
        } else {
            preguntasSeguridadRepo.actualizar(preguntas);
        }

        JOptionPane.showMessageDialog(this,
            "Preguntas de seguridad guardadas exitosamente",
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
