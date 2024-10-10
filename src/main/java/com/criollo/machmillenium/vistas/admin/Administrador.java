package com.criollo.machmillenium.vistas.admin;

import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.repos.PersonalRepo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Vector;

public class Administrador {
    public JPanel panel;
    private JTabbedPane panelMenu;
    private JPanel panelClientes;
    private JPanel panelPersonal;
    private JPanel panelObras;
    private JPanel panelMaquinarias;
    private JPanel panelMonitoreo;
    private JTable tablaClientes;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JTable tablaPersonal;
    private JButton agregarButton;
    private JButton editarButton;
    private JButton inactivarButton;
    private JTable tablaObras;
    private JButton agregarButton1;
    private JButton editarButton1;
    private JButton eliminarButton;
    private JTable tablaMaquinarias;
    private JButton agregarButton2;
    private JButton editarButton2;
    private JButton eliminarButton1;
    private JButton tiposMaquinariaButton;
    private JTable table1;
    private JPanel panelPresupuesto;
    private JTable table2;
    private JButton agregarButton3;
    private JButton editarButton3;
    private JButton eliminarButton2;
    private JPanel botonesPanelPersonal;
    private JFrame jframe;

    public Administrador() {}

    public Administrador(JFrame jframe) {
        this.jframe = jframe;
        try {
            setTablePersonalModel();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setTablePersonalModel() throws IllegalAccessException {
        PersonalRepo personalRepo = new PersonalRepo();
        Field[] fields = Personal.class.getDeclaredFields();

        // Create a Vector to hold the column names
        Vector<String> columnNames = new Vector<>();
        for (Field field : fields) {
            columnNames.add(field.getName());
        }

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (Personal personal : personalRepo.obtenerTodos()) {
            Vector<Object> row = new Vector<>();
            for (Field field : fields) {
                field.setAccessible(true);
                row.add(field.get(personal));
            }
            data.add(row);
        }

        // Create a DefaultTableModel with the column names and data
        DefaultTableModel personalTableModel = new DefaultTableModel(data, columnNames);

        // Set the TableModel to the JTable
        tablaPersonal.setModel(personalTableModel);
        TableColumnModel columnModel = tablaPersonal.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setMinWidth(100);
            columnModel.getColumn(i).setPreferredWidth(100);
            columnModel.getColumn(i).setResizable(true);
        }


// Create a JScrollPane to wrap the JTable
        JScrollPane scrollPane = new JScrollPane(tablaPersonal);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

// Add the JScrollPane to the panel
        panelPersonal.add(scrollPane, BorderLayout.WEST);
    }
}
