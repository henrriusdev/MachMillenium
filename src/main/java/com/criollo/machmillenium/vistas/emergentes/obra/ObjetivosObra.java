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

    public ObjetivosObra(Long idObra) {
        this.idObra = idObra;
        this.obraRepo = new ObraRepo();
        this.checkboxMap = new HashMap<>();
        
        initComponents();
        cargarObjetivos();
        
        setContentPane(contentPane);
        setModal(true);
        setTitle("Objetivos de la Obra");
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior para agregar nuevos objetivos
        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        nuevoObjetivoField = new JTextField();
        agregarButton = new JButton("Agregar");
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
        buttonOK = new JButton("OK");
        buttonCancel = new JButton("Cancel");
        buttonPanel.add(buttonOK);
        buttonPanel.add(buttonCancel);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Configurar listeners
        agregarButton.addActionListener(e -> agregarObjetivo());
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        nuevoObjetivoField.addActionListener(e -> agregarObjetivo());

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
        objetivos = obraRepo.obtenerObjetivosPorObra(idObra);
        checkboxPanel.removeAll();
        checkboxMap.clear();
        
        for (Objetivo objetivo : objetivos) {
            JCheckBox checkBox = crearCheckBoxObjetivo(objetivo);
            checkboxMap.put(checkBox, objetivo);
            checkboxPanel.add(checkBox);
        }
        
        checkboxPanel.revalidate();
        checkboxPanel.repaint();
    }

    private JCheckBox crearCheckBoxObjetivo(Objetivo objetivo) {
        JCheckBox checkBox = new JCheckBox(objetivo.getObjetivo());
        checkBox.setSelected(objetivo.isCompletado());
        
        // Aplicar estilo tachado si está completado
        actualizarEstiloCheckbox(checkBox);
        
        checkBox.addActionListener(e -> actualizarEstiloCheckbox(checkBox));
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
        if (!textoObjetivo.isEmpty()) {
            Obra obra = obraRepo.obtenerObraPorId(idObra);
            Objetivo nuevoObjetivo = new Objetivo(textoObjetivo, obra);
            objetivos.add(nuevoObjetivo);
            
            JCheckBox checkBox = crearCheckBoxObjetivo(nuevoObjetivo);
            checkboxMap.put(checkBox, nuevoObjetivo);
            checkboxPanel.add(checkBox);
            
            // Asegurar que el nuevo checkbox sea visible
            checkBox.scrollRectToVisible(checkBox.getBounds());
            checkboxPanel.revalidate();
            checkboxPanel.repaint();
            
            // Limpiar y enfocar el campo de texto
            nuevoObjetivoField.setText("");
            nuevoObjetivoField.requestFocus();
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
        for (Map.Entry<JCheckBox, Objetivo> entry : checkboxMap.entrySet()) {
            JCheckBox checkBox = entry.getKey();
            Objetivo objetivo = entry.getValue();
            objetivo.setCompletado(checkBox.isSelected());
            
            if (objetivo.getId() == null) {
                obraRepo.insertarObjetivo(objetivo);
            } else {
                obraRepo.actualizarObjetivo(objetivo);
            }
        }

        cerrarDialogo();
    }

    private void onOK() {
        guardarCambios();
    }

    private void onCancel() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea cancelar? Los cambios no guardados se perderán.",
            "Confirmar Cancelación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            cerrarDialogo();
        }
    }
}
