package com.criollo.machmillenium.vistas.admin;

import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.modelos.ModeloPersonal;
import com.criollo.machmillenium.repos.PersonalRepo;

import javax.swing.*;
import javax.swing.table.*;
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
        Field[] fields = ModeloPersonal.class.getDeclaredFields();

        // Create a Vector to hold the column names
        Vector<String> columnNames = new Vector<>();
        for (Field field : fields) {
            columnNames.add(field.getName());
        }

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (ModeloPersonal personal : personalRepo.obtenerTodos()) {
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

        ajustarAnchoColumnas();

// Create a JScrollPane to wrap the JTable
        JScrollPane scrollPane = new JScrollPane(tablaPersonal);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

// Add the JScrollPane to the panel
        panelPersonal.add(scrollPane, BorderLayout.WEST);
    }

    public void ajustarAnchoColumnas() {
        TableColumnModel columnModel = tablaPersonal.getColumnModel();
        int maxColumnWidth = 500; // Ancho máximo de la columna
        int extraSpace = 20; // Espacio adicional para evitar que el contenido quede muy justo

        for (int column = 0; column < tablaPersonal.getColumnCount(); column++) {
            int width = 50; // Ancho mínimo

            // Considerar el ancho del encabezado
            TableCellRenderer headerRenderer = tablaPersonal.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(tablaPersonal, tablaPersonal.getColumnName(column), false, false, -1, column);
            width = Math.max(headerComp.getPreferredSize().width + extraSpace, width);

            // Considerar el ancho del contenido de cada fila
            for (int row = 0; row < tablaPersonal.getRowCount(); row++) {
                TableCellRenderer renderer = tablaPersonal.getCellRenderer(row, column);
                Component comp = tablaPersonal.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + extraSpace, width);
            }

            // Limitar el ancho máximo de la columna si es necesario
            width = Math.min(width, maxColumnWidth);

            // Establecer el ancho preferido de la columna
            columnModel.getColumn(column).setPreferredWidth(width);
        }

        // Asegurar que la tabla no redimensione automáticamente las columnas
        tablaPersonal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
}
