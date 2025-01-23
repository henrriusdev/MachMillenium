package com.criollo.machmillenium.vistas.emergentes.personal;

import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.PreguntasSeguridad;
import com.criollo.machmillenium.repos.PreguntasSeguridadRepo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDateTime;

public class ConfigurarPreguntasSeguridad extends JDialog {
    private JPanel mainPanel;
    private JTextField pregunta1Field;
    private JTextField pregunta2Field;
    private JTextField pregunta3Field;
    private JPasswordField respuesta1Field;
    private JPasswordField respuesta2Field;
    private JPasswordField respuesta3Field;
    private JButton guardarButton;
    private JButton cancelarButton;

    private final Personal personal;
    private final PreguntasSeguridadRepo preguntasSeguridadRepo;

    public ConfigurarPreguntasSeguridad(Personal personal) {
        if (personal == null) {
            throw new IllegalArgumentException("El personal no puede ser nulo");
        }
        
        this.personal = personal;
        this.preguntasSeguridadRepo = new PreguntasSeguridadRepo();
        
        setContentPane(mainPanel);
        setTitle("Configurar Preguntas de Seguridad - " + personal.getNombre());
        setModal(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Add tooltips
        pregunta1Field.setToolTipText("Ingrese una pregunta de seguridad personal");
        pregunta2Field.setToolTipText("Ingrese una pregunta diferente a la primera");
        pregunta3Field.setToolTipText("Ingrese una pregunta diferente a las anteriores");
        respuesta1Field.setToolTipText("Ingrese la respuesta a la primera pregunta");
        respuesta2Field.setToolTipText("Ingrese la respuesta a la segunda pregunta");
        respuesta3Field.setToolTipText("Ingrese la respuesta a la tercera pregunta");
        
        try {
            loadExistingQuestions();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar las preguntas existentes: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
        
        initializeListeners();
        addValidationListeners();
        
        pack();
        setLocationRelativeTo(null);
    }

    private void addValidationListeners() {
        // Add real-time validation for questions
        pregunta1Field.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                validateQuestion(pregunta1Field, "primera");
            }
        });

        pregunta2Field.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                validateQuestion(pregunta2Field, "segunda");
                validateDuplicateQuestions();
            }
        });

        pregunta3Field.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                validateQuestion(pregunta3Field, "tercera");
                validateDuplicateQuestions();
            }
        });

        // Add real-time validation for answers
        respuesta1Field.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                validateAnswer(respuesta1Field, "primera");
            }
        });

        respuesta2Field.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                validateAnswer(respuesta2Field, "segunda");
            }
        });

        respuesta3Field.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                validateAnswer(respuesta3Field, "tercera");
            }
        });
    }

    private void validateQuestion(JTextField field, String questionNumber) {
        String question = field.getText().trim();
        
        if (question.isEmpty()) {
            field.setBackground(new Color(255, 200, 200));
            JOptionPane.showMessageDialog(this,
                "La " + questionNumber + " pregunta no puede estar vacía",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (question.length() < 5) {
            field.setBackground(new Color(255, 200, 200));
            JOptionPane.showMessageDialog(this,
                "La " + questionNumber + " pregunta debe tener al menos 5 caracteres",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!question.endsWith("?")) {
            field.setBackground(new Color(255, 200, 200));
            JOptionPane.showMessageDialog(this,
                "La " + questionNumber + " pregunta debe terminar con un signo de interrogación (?)",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        field.setBackground(Color.WHITE);
    }

    private void validateAnswer(JPasswordField field, String answerNumber) {
        String answer = new String(field.getPassword()).trim();
        
        if (answer.isEmpty()) {
            field.setBackground(new Color(255, 200, 200));
            JOptionPane.showMessageDialog(this,
                "La respuesta " + answerNumber + " no puede estar vacía",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (answer.length() < 3) {
            field.setBackground(new Color(255, 200, 200));
            JOptionPane.showMessageDialog(this,
                "La respuesta " + answerNumber + " debe tener al menos 3 caracteres",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        field.setBackground(Color.WHITE);
    }

    private void validateDuplicateQuestions() {
        String q1 = pregunta1Field.getText().trim();
        String q2 = pregunta2Field.getText().trim();
        String q3 = pregunta3Field.getText().trim();
        
        if (!q1.isEmpty() && !q2.isEmpty() && q1.equalsIgnoreCase(q2)) {
            pregunta2Field.setBackground(new Color(255, 200, 200));
            JOptionPane.showMessageDialog(this,
                "La segunda pregunta no puede ser igual a la primera",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
        }
        
        if (!q1.isEmpty() && !q3.isEmpty() && q1.equalsIgnoreCase(q3)) {
            pregunta3Field.setBackground(new Color(255, 200, 200));
            JOptionPane.showMessageDialog(this,
                "La tercera pregunta no puede ser igual a la primera",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
        }
        
        if (!q2.isEmpty() && !q3.isEmpty() && q2.equalsIgnoreCase(q3)) {
            pregunta3Field.setBackground(new Color(255, 200, 200));
            JOptionPane.showMessageDialog(this,
                "La tercera pregunta no puede ser igual a la segunda",
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeListeners() {
        guardarButton.addActionListener(e -> guardarPreguntas());
        cancelarButton.addActionListener(e -> {
            if (hasUnsavedChanges()) {
                int option = JOptionPane.showConfirmDialog(this,
                    "Hay cambios sin guardar. ¿Desea salir sin guardar?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    dispose();
                }
            } else {
                dispose();
            }
        });
    }

    private boolean hasUnsavedChanges() {
        PreguntasSeguridad preguntas = preguntasSeguridadRepo.obtenerPorPersonal(personal);
        if (preguntas == null) {
            return !pregunta1Field.getText().trim().isEmpty() ||
                   !pregunta2Field.getText().trim().isEmpty() ||
                   !pregunta3Field.getText().trim().isEmpty() ||
                   respuesta1Field.getPassword().length > 0 ||
                   respuesta2Field.getPassword().length > 0 ||
                   respuesta3Field.getPassword().length > 0;
        }
        
        return !pregunta1Field.getText().trim().equals(preguntas.getPregunta1()) ||
               !pregunta2Field.getText().trim().equals(preguntas.getPregunta2()) ||
               !pregunta3Field.getText().trim().equals(preguntas.getPregunta3()) ||
               !new String(respuesta1Field.getPassword()).trim().equals(preguntas.getRespuesta1()) ||
               !new String(respuesta2Field.getPassword()).trim().equals(preguntas.getRespuesta2()) ||
               !new String(respuesta3Field.getPassword()).trim().equals(preguntas.getRespuesta3());
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
        try {
            // Validate all fields
            validateQuestion(pregunta1Field, "primera");
            validateQuestion(pregunta2Field, "segunda");
            validateQuestion(pregunta3Field, "tercera");
            validateAnswer(respuesta1Field, "primera");
            validateAnswer(respuesta2Field, "segunda");
            validateAnswer(respuesta3Field, "tercera");
            validateDuplicateQuestions();

            // Get current values
            String p1 = pregunta1Field.getText().trim();
            String p2 = pregunta2Field.getText().trim();
            String p3 = pregunta3Field.getText().trim();
            String r1 = new String(respuesta1Field.getPassword()).trim().toLowerCase();
            String r2 = new String(respuesta2Field.getPassword()).trim().toLowerCase();
            String r3 = new String(respuesta3Field.getPassword()).trim().toLowerCase();

            // Validate all fields are filled
            if (p1.isEmpty() || p2.isEmpty() || p3.isEmpty() || 
                r1.isEmpty() || r2.isEmpty() || r3.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios",
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get or create PreguntasSeguridad
            PreguntasSeguridad preguntas = preguntasSeguridadRepo.obtenerPorPersonal(personal);
            boolean esNuevo = preguntas == null;

            if (esNuevo) {
                preguntas = new PreguntasSeguridad();
                preguntas.setPersonal(personal);
            }

            // Update values
            preguntas.setPregunta1(p1);
            preguntas.setPregunta2(p2);
            preguntas.setPregunta3(p3);
            preguntas.setRespuesta1(r1);
            preguntas.setRespuesta2(r2);
            preguntas.setRespuesta3(r3);
            preguntas.setModificado(LocalDateTime.now());

            // Save changes
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
            
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al guardar las preguntas: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void createUIComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        
        // Panel principal con GridBagLayout
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Pregunta 1
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelPrincipal.add(new JLabel("Pregunta 1:"), gbc);
        
        gbc.gridx = 1;
        pregunta1Field = new JTextField(30);
        panelPrincipal.add(pregunta1Field, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelPrincipal.add(new JLabel("Respuesta 1:"), gbc);
        
        gbc.gridx = 1;
        respuesta1Field = new JPasswordField(30);
        panelPrincipal.add(respuesta1Field, gbc);
        
        // Pregunta 2
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelPrincipal.add(new JLabel("Pregunta 2:"), gbc);
        
        gbc.gridx = 1;
        pregunta2Field = new JTextField(30);
        panelPrincipal.add(pregunta2Field, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelPrincipal.add(new JLabel("Respuesta 2:"), gbc);
        
        gbc.gridx = 1;
        respuesta2Field = new JPasswordField(30);
        panelPrincipal.add(respuesta2Field, gbc);
        
        // Pregunta 3
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelPrincipal.add(new JLabel("Pregunta 3:"), gbc);
        
        gbc.gridx = 1;
        pregunta3Field = new JTextField(30);
        panelPrincipal.add(pregunta3Field, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelPrincipal.add(new JLabel("Respuesta 3:"), gbc);
        
        gbc.gridx = 1;
        respuesta3Field = new JPasswordField(30);
        panelPrincipal.add(respuesta3Field, gbc);
        
        mainPanel.add(panelPrincipal, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        guardarButton = new JButton("Guardar");
        cancelarButton = new JButton("Cancelar");
        
        buttonPanel.add(guardarButton);
        buttonPanel.add(cancelarButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
}
