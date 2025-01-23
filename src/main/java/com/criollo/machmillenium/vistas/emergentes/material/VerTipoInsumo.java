package com.criollo.machmillenium.vistas.emergentes.material;

import com.criollo.machmillenium.entidades.TipoInsumo;
import com.criollo.machmillenium.repos.TipoInsumoRepo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.List;

public class VerTipoInsumo extends JDialog {
    public JPanel mainPanel;
    private JTable tablaTipoInsumo;
    private JButton botonAgregar;
    private final TipoInsumoRepo tipoInsumoRepo;

    public VerTipoInsumo() {
        this.tipoInsumoRepo = new TipoInsumoRepo();
        setContentPane(mainPanel);
        setModal(true);
        setTitle("Tipos de Insumo");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        setTableModel();

        botonAgregar.addActionListener(e -> botonAgregarActionPerformed());
        tablaTipoInsumo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaTipoInsumoClick(e);
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

        List<TipoInsumo> tipoInsumos = tipoInsumoRepo.obtenerTodos();

        for (TipoInsumo tipoInsumo : tipoInsumos) {
            model.addRow(new Object[]{
                    tipoInsumo.getId(),
                    tipoInsumo.getNombre()
            });
        }

        tablaTipoInsumo.setModel(model);
    }

    private void botonAgregarActionPerformed() {
        String nombre = JOptionPane.showInputDialog(this, 
            "Ingrese el nombre del tipo de insumo:", 
            "Agregar Tipo de Insumo", 
            JOptionPane.QUESTION_MESSAGE);

        if (nombre != null && !nombre.trim().isEmpty()) {
            TipoInsumo tipoInsumo = new TipoInsumo();
            tipoInsumo.setNombre(nombre.trim());

            tipoInsumoRepo.insertar(tipoInsumo);
            setTableModel();
        }
    }

    private void tablaTipoInsumoClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            int row = tablaTipoInsumo.getSelectedRow();
            if (row != -1) {
                Long id = (Long) tablaTipoInsumo.getValueAt(row, 0);
                TipoInsumo tipoInsumo = tipoInsumoRepo.obtenerPorId(id);

                if (tipoInsumo != null) {
                    String[] options = {"Modificar", "Eliminar"};
                    int option = JOptionPane.showOptionDialog(this, 
                        "¿Qué desea hacer con el tipo de insumo?", 
                        "Tipo de Insumo", 
                        JOptionPane.DEFAULT_OPTION, 
                        JOptionPane.QUESTION_MESSAGE, 
                        null, 
                        options, 
                        options[0]);

                    switch (option) {
                        case 0: // Modificar
                            String nuevoNombre = (String) JOptionPane.showInputDialog(this,
                                "Ingrese el nuevo nombre del tipo de insumo:",
                                "Modificar Tipo de Insumo",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                null,
                                tipoInsumo.getNombre());

                            if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                                tipoInsumo.setNombre(nuevoNombre.trim());
                                tipoInsumoRepo.actualizar(tipoInsumo);
                                setTableModel();
                            }
                            break;

                        case 1: // Eliminar
                            int confirm = JOptionPane.showConfirmDialog(this,
                                "¿Está seguro que desea eliminar este tipo de insumo?",
                                "Confirmar Eliminación",
                                JOptionPane.YES_NO_OPTION);
                                
                            if (confirm == JOptionPane.YES_OPTION) {
                                tipoInsumo.setEliminado(LocalDateTime.now());
                                tipoInsumoRepo.actualizar(tipoInsumo);
                                setTableModel();
                            }
                            break;
                    }
                }
            }
        }
    }
}
