package com.criollo.machmillenium.vistas.emergentes.obra;

import com.criollo.machmillenium.entidades.Objetivo;
import com.criollo.machmillenium.entidades.Obra;
import com.criollo.machmillenium.repos.ObraRepo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ObjetivosObra extends JDialog {
    public JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel checkboxPanel;
    private JTextField nuevoObjetivoField;
    private JButton agregarButton;
    private final ObraRepo obraRepo;
    private final Long idObra;
    private List<Objetivo> objetivos;
    private Map<JCheckBox, Objetivo> checkboxMap;

    private static final int MAX_OBJETIVO_LENGTH = 200;
    private static final int MIN_OBJETIVO_LENGTH = 5;
    private static final Pattern OBJETIVO_PATTERN = Pattern.compile("^[A-Za-zÁáÉéÍíÓóÚúÑñ0-9\\s.,;:-]{" + MIN_OBJETIVO_LENGTH + ",}$");

    public ObjetivosObra(Long idObra) {
        if (idObra == null) {
            throw new IllegalArgumentException("El ID de la obra no puede ser nulo");
        }

        this.idObra = idObra;
        this.obraRepo = new ObraRepo();
        this.checkboxMap = new HashMap<>();
        
        try {
            Obra obra = obraRepo.obtenerObraPorId(idObra);
            if (obra == null) {
                throw new IllegalArgumentException("No se encontró la obra con ID: " + idObra);
            }
            
            initComponents();
            cargarObjetivos();
            
            setContentPane(contentPane);
            setModal(true);
            setTitle("Objetivos de la Obra: " + obra.getNombre());
            setSize(400, 300);
            setLocationRelativeTo(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                "Error al inicializar la ventana de objetivos: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void initComponents() {
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior para agregar nuevos objetivos
        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        nuevoObjetivoField = new JTextField();
        nuevoObjetivoField.setToolTipText("Ingrese un nuevo objetivo (mínimo " + MIN_OBJETIVO_LENGTH + " caracteres)");
        
        // Limitar longitud del campo de texto
        nuevoObjetivoField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) 
                    throws javax.swing.text.BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= MAX_OBJETIVO_LENGTH) {
                    super.insertString(offs, str, a);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        agregarButton = new JButton("Agregar");
        agregarButton.setToolTipText("Agregar nuevo objetivo");
        topPanel.add(nuevoObjetivoField, BorderLayout.CENTER);
        topPanel.add(agregarButton, BorderLayout.EAST);
        contentPane.add(topPanel, BorderLayout.NORTH);

        // Panel central con scroll para los checkboxes
        checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(checkboxPanel);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Panel inferior para los botones OK y Cancel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonOK = new JButton("Guardar");
        buttonOK.setToolTipText("Guardar todos los cambios");
        buttonCancel = new JButton("Cancelar");
        buttonCancel.setToolTipText("Cancelar y descartar cambios");
        buttonPanel.add(buttonOK);
        buttonPanel.add(buttonCancel);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Configurar listeners
        agregarButton.addActionListener(e -> agregarObjetivo());
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        nuevoObjetivoField.addActionListener(e -> agregarObjetivo());
        
        // Validación en tiempo real del campo de texto
        nuevoObjetivoField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = nuevoObjetivoField.getText().trim();
                boolean valido = texto.length() >= MIN_OBJETIVO_LENGTH && 
                               texto.length() <= MAX_OBJETIVO_LENGTH &&
                               OBJETIVO_PATTERN.matcher(texto).matches();
                agregarButton.setEnabled(valido);
                
                if (!valido && texto.length() > 0) {
                    nuevoObjetivoField.setBackground(new Color(255, 235, 235));
                } else {
                    nuevoObjetivoField.setBackground(Color.WHITE);
                }
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void cargarObjetivos() {
        try {
            objetivos = obraRepo.obtenerObjetivosPorObra(idObra);
            checkboxPanel.removeAll();
            checkboxMap.clear();
            
            if (objetivos.isEmpty()) {
                JLabel mensajeVacio = new JLabel("No hay objetivos definidos para esta obra");
                mensajeVacio.setHorizontalAlignment(SwingConstants.CENTER);
                mensajeVacio.setForeground(Color.GRAY);
                checkboxPanel.add(mensajeVacio);
            } else {
                for (Objetivo objetivo : objetivos) {
                    JCheckBox checkBox = crearCheckBoxObjetivo(objetivo);
                    checkboxMap.put(checkBox, objetivo);
                    checkboxPanel.add(checkBox);
                }
            }
            
            checkboxPanel.revalidate();
            checkboxPanel.repaint();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar los objetivos: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JCheckBox crearCheckBoxObjetivo(Objetivo objetivo) {
        JCheckBox checkBox = new JCheckBox(objetivo.getObjetivo());
        checkBox.setSelected(objetivo.isCompletado());
        checkBox.setToolTipText(objetivo.isCompletado() ? 
            "Objetivo completado" : "Marcar como completado");
        
        // Aplicar estilo tachado si está completado
        actualizarEstiloCheckbox(checkBox);
        
        checkBox.addActionListener(e -> {
            actualizarEstiloCheckbox(checkBox);
            checkBox.setToolTipText(checkBox.isSelected() ? 
                "Objetivo completado" : "Marcar como completado");
        });
        
        return checkBox;
    }

    private void actualizarEstiloCheckbox(JCheckBox checkBox) {
        if (checkBox.isSelected()) {
            checkBox.setFont(new Font(checkBox.getFont().getName(), 
                    Font.BOLD | Font.ITALIC, 
                    checkBox.getFont().getSize()));
            checkBox.setText("<html><strike>" + checkBox.getText().replaceAll("<[^>]*>", "") + "</strike></html>");
        } else {
            checkBox.setFont(new Font(checkBox.getFont().getName(), 
                    Font.PLAIN, 
                    checkBox.getFont().getSize()));
            checkBox.setText(checkBox.getText().replaceAll("<[^>]*>", ""));
        }
    }

    private void agregarObjetivo() {
        String textoObjetivo = nuevoObjetivoField.getText().trim();
        
        // Validación del texto del objetivo
        if (textoObjetivo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El objetivo no puede estar vacío",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            nuevoObjetivoField.requestFocus();
            return;
        }

        if (textoObjetivo.length() < MIN_OBJETIVO_LENGTH) {
            JOptionPane.showMessageDialog(this,
                "El objetivo debe tener al menos " + MIN_OBJETIVO_LENGTH + " caracteres",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            nuevoObjetivoField.requestFocus();
            return;
        }

        if (!OBJETIVO_PATTERN.matcher(textoObjetivo).matches()) {
            JOptionPane.showMessageDialog(this,
                "El objetivo contiene caracteres no válidos",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            nuevoObjetivoField.requestFocus();
            return;
        }

        // Verificar si el objetivo ya existe
        boolean objetivoExistente = checkboxMap.values().stream()
            .anyMatch(obj -> obj.getObjetivo().equalsIgnoreCase(textoObjetivo));
        
        if (objetivoExistente) {
            JOptionPane.showMessageDialog(this,
                "Este objetivo ya existe",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            nuevoObjetivoField.requestFocus();
            return;
        }

        try {
            Obra obra = obraRepo.obtenerObraPorId(idObra);
            Objetivo nuevoObjetivo = new Objetivo(textoObjetivo, obra);
            objetivos.add(nuevoObjetivo);
            
            JCheckBox checkBox = crearCheckBoxObjetivo(nuevoObjetivo);
            checkboxMap.put(checkBox, nuevoObjetivo);
            
            // Remover el mensaje de "No hay objetivos" si existe
            if (checkboxPanel.getComponentCount() == 1 && 
                checkboxPanel.getComponent(0) instanceof JLabel) {
                checkboxPanel.removeAll();
            }
            
            checkboxPanel.add(checkBox);
            
            // Asegurar que el nuevo checkbox sea visible
            checkBox.scrollRectToVisible(checkBox.getBounds());
            checkboxPanel.revalidate();
            checkboxPanel.repaint();
            
            // Limpiar y enfocar el campo de texto
            nuevoObjetivoField.setText("");
            nuevoObjetivoField.requestFocus();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al agregar el objetivo: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarDialogo() {
        this.setVisible(false);
        this.dispose();
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof ObjetivosObra) {
                window.dispose();
            }
        }
    }

    private void guardarCambios() {
        try {
            boolean hayErrores = false;
            StringBuilder mensajesError = new StringBuilder();

            for (Map.Entry<JCheckBox, Objetivo> entry : checkboxMap.entrySet()) {
                try {
                    JCheckBox checkBox = entry.getKey();
                    Objetivo objetivo = entry.getValue();
                    objetivo.setCompletado(checkBox.isSelected());
                    
                    if (objetivo.getId() == null) {
                        obraRepo.insertarObjetivo(objetivo);
                    } else {
                        obraRepo.actualizarObjetivo(objetivo);
                    }
                } catch (Exception ex) {
                    hayErrores = true;
                    mensajesError.append("- Error al guardar objetivo '")
                               .append(entry.getValue().getObjetivo())
                               .append("': ")
                               .append(ex.getMessage())
                               .append("\n");
                }
            }

            if (hayErrores) {
                throw new Exception("Se encontraron errores al guardar los cambios:\n" + mensajesError);
            }

            JOptionPane.showMessageDialog(this,
                "Cambios guardados exitosamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            cerrarDialogo();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error al guardar",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onOK() {
        // Confirmar si hay cambios sin guardar
        boolean hayCambios = checkboxMap.entrySet().stream()
            .anyMatch(entry -> entry.getKey().isSelected() != entry.getValue().isCompletado());
            
        if (hayCambios) {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea guardar los cambios realizados?",
                "Guardar Cambios",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) {
                guardarCambios();
            } else if (opcion == JOptionPane.NO_OPTION) {
                cerrarDialogo();
            }
            // Si es CANCEL_OPTION, no hace nada y se mantiene en la ventana
        } else {
            cerrarDialogo();
        }
    }

    private void onCancel() {
        // Verificar si hay cambios sin guardar
        boolean hayCambios = checkboxMap.entrySet().stream()
            .anyMatch(entry -> entry.getKey().isSelected() != entry.getValue().isCompletado());
            
        if (hayCambios) {
            int opcion = JOptionPane.showConfirmDialog(
                this,
                "Hay cambios sin guardar. ¿Está seguro que desea salir?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) {
                cerrarDialogo();
            }
        } else {
            cerrarDialogo();
        }
    }
}
