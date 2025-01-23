package com.criollo.machmillenium.vistas.emergentes.maquinaria;

import com.criollo.machmillenium.entidades.TipoMaquinaria;
import com.criollo.machmillenium.repos.TipoMaquinariaRepo;
import com.criollo.machmillenium.utilidades.Utilidades;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.List;

public class VerTipoMaquinaria extends JDialog {
    public JPanel mainPanel;
    private JTable tablaTipoMaquinaria;
    private JButton botonAgregar;
    private final TipoMaquinariaRepo tipoMaquinariaRepo;

    public VerTipoMaquinaria() {
        this.tipoMaquinariaRepo = new TipoMaquinariaRepo();
        setContentPane(mainPanel);
        setModal(true);
        setTitle("Tipos de Maquinaria");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        setTableModel();

        botonAgregar.addActionListener(e -> botonAgregarActionPerformed());
        tablaTipoMaquinaria.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaTipoMaquinariaClick(e);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                getParent().setEnabled(true);
            }
        });
    }

    private void setTableModel() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("ID");
        model.addColumn("Nombre");

        List<TipoMaquinaria> tipoMaquinarias = tipoMaquinariaRepo.obtenerTodos();

        for (TipoMaquinaria tipoMaquinaria : tipoMaquinarias) {
            model.addRow(new Object[]{
                    tipoMaquinaria.getId(),
                    tipoMaquinaria.getNombre(),
            });
        }

        tablaTipoMaquinaria.setModel(model);
    }

    private void botonAgregarActionPerformed() {
        String nombre = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre del tipo de maquinaria:", 
            "Agregar Tipo de Maquinaria", 
            JOptionPane.QUESTION_MESSAGE);

        if (nombre != null && !nombre.trim().isEmpty()) {
            
            TipoMaquinaria tipoMaquinaria = new TipoMaquinaria();
            tipoMaquinaria.setNombre(nombre.trim());

            tipoMaquinariaRepo.insertar(tipoMaquinaria);
            setTableModel();
        }
    }

    private void tablaTipoMaquinariaClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            int row = tablaTipoMaquinaria.getSelectedRow();
            if (row != -1) {
                Long id = (Long) tablaTipoMaquinaria.getValueAt(row, 0);
                TipoMaquinaria tipoMaquinaria = tipoMaquinariaRepo.obtenerPorId(id);
                if (tipoMaquinaria != null) {
                    String[] options = {"Modificar", "Eliminar"};
                    int option = JOptionPane.showOptionDialog(this, 
                        "¿Qué desea hacer con el tipo de maquinaria?", 
                        "Tipo de Maquinaria", 
                        JOptionPane.DEFAULT_OPTION, 
                        JOptionPane.QUESTION_MESSAGE, 
                        null, 
                        options, 
                        options[0]);

                    switch (option) {
                        case 0: // Modificar
                            String nuevoNombre = (String) JOptionPane.showInputDialog(this,
                                "Ingrese el nuevo nombre del tipo de maquinaria:",
                                "Modificar Tipo de Maquinaria",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                null,
                                tipoMaquinaria.getNombre());

                            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                                tipoMaquinaria.setNombre(nuevoNombre.trim());
                                tipoMaquinariaRepo.actualizar(tipoMaquinaria);
                                setTableModel();
                            }
                            break;

                        case 1: // Eliminar
                            int confirm = JOptionPane.showConfirmDialog(this,
                                "¿Está seguro que desea eliminar este tipo de maquinaria?",
                                "Confirmar Eliminación",
                                JOptionPane.YES_NO_OPTION);
                                
                            if (confirm == JOptionPane.YES_OPTION) {
                                tipoMaquinaria.setEliminado(LocalDateTime.now());
                                tipoMaquinariaRepo.actualizar(tipoMaquinaria);
                                setTableModel();
                            }
                            break;
                    }
                }
            }
        }
    }
}
