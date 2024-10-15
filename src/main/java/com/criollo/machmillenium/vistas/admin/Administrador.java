package com.criollo.machmillenium.vistas.admin;

import com.criollo.machmillenium.modelos.ModeloCliente;
import com.criollo.machmillenium.modelos.ModeloPersonal;
import com.criollo.machmillenium.repos.ClienteRepo;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.vistas.emergentes.clientes.AgregarCliente;
import com.criollo.machmillenium.vistas.emergentes.clientes.ModificarCliente;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.Vector;
import java.util.List;

public class Administrador {
    public JPanel panel;
    private JTabbedPane panelMenu;
    private JPanel panelClientes;
    private JPanel panelPersonal;
    private JPanel panelObras;
    private JPanel panelMaquinarias;
    private JPanel panelMonitoreo;
    private JTable tablaClientes;
    private JButton btnAgregarCliente;
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
    private final PersonalRepo personalRepo;
    private final ClienteRepo clienteRepo;

    public Administrador(JFrame jframe) {
        this.personalRepo = new PersonalRepo();
        this.clienteRepo = new ClienteRepo();
        this.jframe = jframe;
        try {
            setTablePersonalModel();
            setTableClienteModel();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        btnAgregarCliente.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(panel).setEnabled(false);
            JFrame newJframe = new JFrame("Agregar Cliente");
            newJframe.setContentPane(new AgregarCliente().panel);
            newJframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newJframe.pack();
            newJframe.setVisible(true);

            newJframe.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    // Reactivar el JFrame principal
                    SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                    SwingUtilities.getWindowAncestor(panel).toFront();
                    DefaultTableModel clienteTableModel = mapearModeloCliente(clienteRepo.obtenerTodos());
                    tablaClientes.setModel(clienteTableModel);
                    ajustarAnchoColumnas(tablaClientes);
                }
            });
        });

        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tablaClientes.getSelectedRow() != -1) {
                    int selectedRow = tablaClientes.getSelectedRow();

                    // Obtener los valores de la fila seleccionada para crear el ModeloCliente
                    Long id = Long.parseLong(tablaClientes.getValueAt(selectedRow, 0).toString());
                    String nombre = tablaClientes.getValueAt(selectedRow, 1).toString();
                    String cedula = tablaClientes.getValueAt(selectedRow, 2).toString();
                    String telefono = tablaClientes.getValueAt(selectedRow, 3).toString();
                    String direccion = tablaClientes.getValueAt(selectedRow, 4).toString();
                    Integer edad = Integer.parseInt(tablaClientes.getValueAt(selectedRow, 5).toString());
                    String sexo = tablaClientes.getValueAt(selectedRow, 7).toString();
                    String correo = tablaClientes.getValueAt(selectedRow, 6).toString();

                    // Crear el ModeloCliente correspondiente
                    ModeloCliente clienteSeleccionado = new ModeloCliente(id, nombre, cedula, telefono, direccion, edad, correo, sexo);

                    // Abrir la vista ModificarCliente y pasarle el ModeloCliente
                    JFrame modificarClienteFrame = new JFrame("Modificar Cliente");
                    modificarClienteFrame.setContentPane(new ModificarCliente(clienteSeleccionado).panel);
                    modificarClienteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    modificarClienteFrame.pack();
                    modificarClienteFrame.setVisible(true);

                    // Manejar el cierre de la ventana
                    modificarClienteFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                            // Actualizar la tabla si es necesario tras cerrar la ventana de edición
                            DefaultTableModel clienteTableModel = mapearModeloCliente(clienteRepo.obtenerTodos());
                            tablaClientes.setModel(clienteTableModel);
                            ajustarAnchoColumnas(tablaClientes);
                        }
                    });
                }
            }
        });
    }

    public void setTablePersonalModel() throws IllegalAccessException {
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

        ajustarAnchoColumnas(tablaPersonal);

// Create a JScrollPane to wrap the JTable
        JScrollPane scrollPane = new JScrollPane(tablaPersonal);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

// Add the JScrollPane to the panel
        panelPersonal.add(scrollPane, BorderLayout.WEST);
    }

    public void ajustarAnchoColumnas(JTable tabla) {
        TableColumnModel columnModel = tabla.getColumnModel();
        int maxColumnWidth = 500; // Ancho máximo de la columna
        int extraSpace = 20; // Espacio adicional para evitar que el contenido quede muy justo

        for (int column = 0; column < tabla.getColumnCount(); column++) {
            int width = 50; // Ancho mínimo

            // Considerar el ancho del encabezado
            TableCellRenderer headerRenderer = tabla.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(tabla, tabla.getColumnName(column), false, false, -1, column);
            width = Math.max(headerComp.getPreferredSize().width + extraSpace, width);

            // Considerar el ancho del contenido de cada fila
            for (int row = 0; row < tabla.getRowCount(); row++) {
                TableCellRenderer renderer = tabla.getCellRenderer(row, column);
                Component comp = tabla.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + extraSpace, width);
            }

            // Limitar el ancho máximo de la columna si es necesario
            width = Math.min(width, maxColumnWidth);

            // Establecer el ancho preferido de la columna
            columnModel.getColumn(column).setPreferredWidth(width);
        }

        // Asegurar que la tabla no redimensione automáticamente las columnas
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }

    public void setTableClienteModel() throws IllegalAccessException {
        ClienteRepo clienteRepo = new ClienteRepo();

        // Create a DefaultTableModel with the column names and data
        DefaultTableModel clienteTableModel = mapearModeloCliente(clienteRepo.obtenerTodos());

        // Set the TableModel to the JTable
        tablaClientes.setModel(clienteTableModel);

        ajustarAnchoColumnas(tablaClientes);

        // Create a JScrollPane to wrap the JTable
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add the JScrollPane to the panel
        panelClientes.add(scrollPane, BorderLayout.WEST);
    }

    public DefaultTableModel mapearModeloCliente(List<ModeloCliente> modeloClienteList) {
        Field[] fields = ModeloCliente.class.getDeclaredFields();

        // Create a Vector to hold the column names
        Vector<String> columnNames = new Vector<>();
        for (Field field : fields) {
            columnNames.add(field.getName());
        }

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (ModeloCliente cliente : modeloClienteList) {
            Vector<Object> row = new Vector<>();
            row.add(cliente.getId());
            row.add(cliente.getNombre());
            row.add(cliente.getCedula());
            row.add(cliente.getTelefono());
            row.add(cliente.getDireccion());
            row.add(cliente.getEdad());
            row.add(cliente.getCorreo());
            row.add(cliente.getSexo());
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
