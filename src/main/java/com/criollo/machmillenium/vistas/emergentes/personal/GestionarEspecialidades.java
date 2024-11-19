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
        this.especialidadRepo = especialidadRepo;
        initComponents();
        setTableModel();
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
        buttonAgregar.addActionListener(e -> handleAgregar());
        buttonPanel.add(buttonAgregar);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog properties
        setTitle("Gestionar Especialidades");
        setModal(true);
        getRootPane().setDefaultButton(buttonAgregar);
        setPreferredSize(new Dimension(400, 300));
    }

    private void setTableModel() {
        DefaultTableModel model = mapearModeloEspecialidad(especialidadRepo.obtenerTodos());
        tablaEspecialidades.setModel(model);
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
        };
    }

    private void handleAgregar() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre de la especialidad", "Agregar Especialidad", JOptionPane.QUESTION_MESSAGE);
        if (nombre != null && !nombre.trim().isEmpty()) {
            Especialidad especialidad = new Especialidad(nombre);
            especialidadRepo.insertar(especialidad);
            setTableModel();
        }
    }

    private void handleTableClick() {
        int selectedRow = tablaEspecialidades.getSelectedRow();
        Long id = Long.parseLong(tablaEspecialidades.getValueAt(selectedRow, 0).toString());
        String nombre = tablaEspecialidades.getValueAt(selectedRow, 1).toString();

        String[] options = {"Modificar", "Eliminar"};
        int option = JOptionPane.showOptionDialog(this, 
            "¿Qué desea hacer con la especialidad?", 
            "Especialidad", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]);

        Especialidad especialidad;
        switch (option) {
            case 1: // Eliminar
                especialidad = new Especialidad(nombre);
                especialidad.setId(id);
                especialidad.setEliminado(LocalDateTime.now());
                especialidadRepo.actualizar(especialidad);
                break;
            case 0: // Modificar
                String nuevoNombre = JOptionPane.showInputDialog(this, 
                    "Ingrese el nuevo nombre de la especialidad", 
                    nombre);
                if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                    especialidad = new Especialidad(nuevoNombre);
                    especialidad.setId(id);
                    especialidadRepo.actualizar(especialidad);
                }
                break;
        }
        setTableModel();
    }
}
