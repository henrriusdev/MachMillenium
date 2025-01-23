package com.criollo.machmillenium.vistas.emergentes.personal;

import com.criollo.machmillenium.entidades.Especialidad;
import com.criollo.machmillenium.repos.EspecialidadRepo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;
import java.util.Arrays;

public class GestionarEspecialidades extends JDialog {
    private JPanel contentPane;
    private JTable tablaEspecialidades;
    private JButton buttonAgregar;
    private final EspecialidadRepo especialidadRepo;

    public GestionarEspecialidades(EspecialidadRepo especialidadRepo) {
        if (especialidadRepo == null) {
            throw new IllegalArgumentException("El repositorio de especialidades no puede ser nulo");
        }
        this.especialidadRepo = especialidadRepo;
        initComponents();
        loadEspecialidades();
    }

    private void initComponents() {
        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // Create table
        tablaEspecialidades = new JTable();
        tablaEspecialidades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEspecialidades.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tablaEspecialidades.getSelectedRow() != -1) {
                    handleTableClick();
                }
            }
        });

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(tablaEspecialidades);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonAgregar = new JButton("Agregar");
        buttonAgregar.setToolTipText("Agregar una nueva especialidad");
        buttonAgregar.addActionListener(e -> handleAgregar());
        buttonPanel.add(buttonAgregar);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog properties
        setTitle("Gestionar Especialidades");
        setModal(true);
        getRootPane().setDefaultButton(buttonAgregar);
        setPreferredSize(new Dimension(600, 400));

        // Add search field
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        searchField.setToolTipText("Buscar especialidad por nombre");
        searchPanel.add(new JLabel("Buscar: "));
        searchPanel.add(searchField);
        contentPane.add(searchPanel, BorderLayout.NORTH);

        // Add search functionality
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { search(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { search(); }

            private void search() {
                String text = searchField.getText().toLowerCase();
                filterTable(text);
            }
        });
    }

    private void filterTable(String searchText) {
        DefaultTableModel model = (DefaultTableModel) tablaEspecialidades.getModel();
        List<Especialidad> especialidades = especialidadRepo.obtenerTodos();
        model.setRowCount(0);

        for (Especialidad especialidad : especialidades) {
            if (especialidad.getNombre().toLowerCase().contains(searchText)) {
                Vector<Object> row = new Vector<>();
                row.add(especialidad.getId());
                row.add(especialidad.getNombre());
                row.add(especialidad.getCreado());
                row.add(especialidad.getModificado());
                model.addRow(row);
            }
        }
    }

    private void loadEspecialidades() {
        try {
            setTableModel();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar las especialidades: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void setTableModel() {
        DefaultTableModel model = mapearModeloEspecialidad(especialidadRepo.obtenerTodos());
        tablaEspecialidades.setModel(model);
        
        // Set column widths
        tablaEspecialidades.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        tablaEspecialidades.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        tablaEspecialidades.getColumnModel().getColumn(2).setPreferredWidth(150); // Creado
        tablaEspecialidades.getColumnModel().getColumn(3).setPreferredWidth(150); // Modificado
    }

    private DefaultTableModel mapearModeloEspecialidad(List<Especialidad> especialidades) {
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Nombre", "Creado", "Modificado"));

        Vector<Vector<Object>> data = new Vector<>();
        for (Especialidad especialidad : especialidades) {
            Vector<Object> row = new Vector<>();
            row.add(especialidad.getId());
            row.add(especialidad.getNombre());
            row.add(especialidad.getCreado());
            row.add(especialidad.getModificado());
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Long.class;
                    case 1: return String.class;
                    case 2:
                    case 3: return LocalDateTime.class;
                    default: return Object.class;
                }
            }
        };
    }

    private void handleAgregar() {
        try {
            String nombre = JOptionPane.showInputDialog(this, 
                "Ingrese el nombre de la especialidad:", 
                "Agregar Especialidad", 
                JOptionPane.QUESTION_MESSAGE);

            if (nombre != null) {
                nombre = nombre.trim();
                
                // Validate name
                if (nombre.isEmpty()) {
                    throw new IllegalArgumentException("El nombre de la especialidad no puede estar vacío");
                }
                
                if (nombre.length() < 3) {
                    throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
                }
                
                if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
                    throw new IllegalArgumentException("El nombre solo puede contener letras y espacios");
                }

                // Check if specialty already exists
                if (especialidadRepo.existePorNombre(nombre)) {
                    throw new IllegalArgumentException("Ya existe una especialidad con este nombre");
                }

                // Create and save specialty
                Especialidad especialidad = new Especialidad(nombre);
                especialidadRepo.insertar(especialidad);
                
                JOptionPane.showMessageDialog(this,
                    "Especialidad agregada exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                loadEspecialidades();
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al agregar la especialidad: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void handleTableClick() {
        try {
            int selectedRow = tablaEspecialidades.getSelectedRow();
            if (selectedRow == -1) {
                throw new IllegalStateException("No se ha seleccionado ninguna especialidad");
            }

            Long id = (Long) tablaEspecialidades.getValueAt(selectedRow, 0);
            String nombre = (String) tablaEspecialidades.getValueAt(selectedRow, 1);

            String[] options = {"Modificar", "Eliminar", "Cancelar"};
            int option = JOptionPane.showOptionDialog(this, 
                "¿Qué desea hacer con la especialidad '" + nombre + "'?", 
                "Gestionar Especialidad", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                options[0]);

            Especialidad especialidad;
            switch (option) {
                case 1: // Eliminar
                    // Check if specialty is in use
                    if (especialidadRepo.estaEnUso(id)) {
                        throw new IllegalStateException(
                            "No se puede eliminar la especialidad porque está siendo utilizada por uno o más personales");
                    }

                    int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro que desea eliminar la especialidad '" + nombre + "'?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        especialidad = new Especialidad(nombre);
                        especialidad.setId(id);
                        especialidad.setEliminado(LocalDateTime.now());
                        especialidadRepo.actualizar(especialidad);
                        
                        JOptionPane.showMessageDialog(this,
                            "Especialidad eliminada exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;

                case 0: // Modificar
                    String nuevoNombre = (String) JOptionPane.showInputDialog(null,
                        "Ingrese el nuevo nombre de la especialidad:",
                        "Modificar Especialidad",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        nombre);

                    if (nuevoNombre != null) {
                        nuevoNombre = nuevoNombre.trim();
                        
                        // Validate name
                        if (nuevoNombre.isEmpty()) {
                            throw new IllegalArgumentException("El nombre de la especialidad no puede estar vacío");
                        }
                        
                        if (nuevoNombre.length() < 3) {
                            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
                        }
                        
                        if (!nuevoNombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
                            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios");
                        }

                        // Check if specialty already exists
                        if (!nuevoNombre.equalsIgnoreCase(nombre) && especialidadRepo.existePorNombre(nuevoNombre)) {
                            throw new IllegalArgumentException("Ya existe una especialidad con este nombre");
                        }

                        especialidad = new Especialidad(nuevoNombre);
                        especialidad.setId(id);
                        especialidadRepo.actualizar(especialidad);
                        
                        JOptionPane.showMessageDialog(this,
                            "Especialidad modificada exitosamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    break;
            }
            
            if (option != 2) { // If not cancelled
                loadEspecialidades();
            }
            
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error de Validación",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al gestionar la especialidad: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
