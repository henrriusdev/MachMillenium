package com.criollo.machmillenium.vistas.admin;

import com.criollo.machmillenium.entidades.TipoMaquinaria;
import com.criollo.machmillenium.modelos.ModeloCliente;
import com.criollo.machmillenium.modelos.ModeloMaquinaria;
import com.criollo.machmillenium.modelos.ModeloPersonal;
import com.criollo.machmillenium.repos.ClienteRepo;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.repos.TipoMaquinariaRepo;
import com.criollo.machmillenium.vistas.emergentes.clientes.AgregarCliente;
import com.criollo.machmillenium.vistas.emergentes.clientes.ModificarCliente;
import com.criollo.machmillenium.vistas.emergentes.maquinaria.AgregarMaquinaria;
import com.criollo.machmillenium.vistas.emergentes.maquinaria.ModificarMaquinaria;
import com.criollo.machmillenium.vistas.emergentes.personal.AgregarPersonal;
import com.criollo.machmillenium.vistas.emergentes.personal.ModificarPersonal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.List;
import java.util.Vector;

public class Administrador {
    public JPanel panel;
    private JTabbedPane panelMenu;
    private JPanel panelClientes;
    private JPanel panelPersonal;
    private JPanel panelObras;
    private JPanel panelMaquinarias;
    private JTable tablaClientes;
    private JButton btnAgregarCliente;
    private JTable tablaPersonal;
    private JButton agregarButton;
    private JTable tablaObras;
    private JButton agregarButton1;
    private JButton editarButton1;
    private JButton eliminarButton;
    private JTable tablaMaquinarias;
    private JButton botonAgregarMaquinaria;
    private JPanel panelPresupuesto;
    private JTable table2;
    private JButton agregarButton3;
    private JButton editarButton3;
    private JButton eliminarButton2;
    private JPanel botonesPanelPersonal;
    private JPanel panelTipoMaquinaria;
    private JTable tablaTipoMaquinaria;
    private JButton botonAgregar;
    private JFrame jframe;
    private final PersonalRepo personalRepo;
    private final ClienteRepo clienteRepo;
    private final TipoMaquinariaRepo tipoMaquinariaRepo;

    public Administrador(JFrame jframe) {
        this.personalRepo = new PersonalRepo();
        this.clienteRepo = new ClienteRepo();
        this.tipoMaquinariaRepo = new TipoMaquinariaRepo();
        this.jframe = jframe;

        try {
            setTableModels();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        setupActionListeners();
    }

    private void setTableModels() throws IllegalAccessException {
        setTablePersonalModel();
        setTableClienteModel();
        setTableTipoMaquinariaModel();
        setTableMaquinariaModel();
    }

    private void setupActionListeners() {
        btnAgregarCliente.addActionListener(e -> openAddClientWindow());

        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isDoubleClick(e)) {
                    handleClientDoubleClick();
                }
            }
        });

        tablaPersonal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isDoubleClick(e)) {
                    handlePersonalDoubleClick();
                }
            }
        });

        agregarButton.addActionListener(e -> openAddPersonalWindow());

        botonAgregar.addActionListener(e -> addTipoMaquinaria());

        tablaTipoMaquinaria.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isDoubleClick(e)) {
                    handleTipoMaquinariaDoubleClick();
                }
            }
        });

        botonAgregarMaquinaria.addActionListener(e -> openAddMaquinariaWindow());

        tablaMaquinarias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isDoubleClick(e)) {
                    handleMaquinariaDoubleClick();
                }
            }
        });
    }

// Helper methods

    private boolean isDoubleClick(MouseEvent e) {
        return e.getClickCount() == 2;
    }

    private void openAddClientWindow() {
        openWindow("Agregar Cliente", new AgregarCliente().panel, this::updateClienteTable);
    }

    private void openAddPersonalWindow() {
        openWindow("Agregar Personal", new AgregarPersonal().panel, this::updatePersonalTable);
    }

    private void openAddMaquinariaWindow() {
        openWindow("Agregar Maquinaria", new AgregarMaquinaria().mainPanel, this::updateMaquinariaTable);
    }

    private void openWindow(String title, JPanel content, Runnable onClose) {
        SwingUtilities.getWindowAncestor(panel).setEnabled(false);
        JFrame newJframe = new JFrame(title);
        newJframe.setContentPane(content);
        newJframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newJframe.pack();
        newJframe.setVisible(true);

        newJframe.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                onClose.run();
            }
        });
    }

    private void handleClientDoubleClick() {
        int selectedRow = tablaClientes.getSelectedRow();
        ModeloCliente clienteSeleccionado = getSelectedClientModel(selectedRow);
        openWindow("Modificar Cliente", new ModificarCliente(clienteSeleccionado).panel, this::updateClienteTable);
    }

    private ModeloCliente getSelectedClientModel(int row) {
        Long id = Long.parseLong(tablaClientes.getValueAt(row, 0).toString());
        String nombre = tablaClientes.getValueAt(row, 1).toString();
        String cedula = tablaClientes.getValueAt(row, 2).toString();
        String telefono = tablaClientes.getValueAt(row, 3).toString();
        String direccion = tablaClientes.getValueAt(row, 4).toString();
        Integer edad = Integer.parseInt(tablaClientes.getValueAt(row, 5).toString());
        String correo = tablaClientes.getValueAt(row, 6).toString();
        String sexo = tablaClientes.getValueAt(row, 7).toString();

        return new ModeloCliente(id, nombre, cedula, telefono, direccion, edad, correo, sexo);
    }

    private void handlePersonalDoubleClick() {
        int selectedRow = tablaPersonal.getSelectedRow();
        ModeloPersonal personalSeleccionado = getSelectedPersonalModel(selectedRow);
        openWindow("Modificar Personal", new ModificarPersonal(personalSeleccionado).panel, this::updatePersonalTable);
    }

    private ModeloPersonal getSelectedPersonalModel(int row) {
        Long id = Long.parseLong(tablaPersonal.getValueAt(row, 0).toString());
        String nombre = tablaPersonal.getValueAt(row, 1).toString();
        String cedula = tablaPersonal.getValueAt(row, 2).toString();
        String correo = tablaPersonal.getValueAt(row, 3).toString();
        Boolean fijo = Boolean.parseBoolean(tablaPersonal.getValueAt(row, 4).toString());
        String especialidad = tablaPersonal.getValueAt(row, 5).toString();
        String rol = tablaPersonal.getValueAt(row, 6).toString();
        Boolean activo = Boolean.parseBoolean(tablaPersonal.getValueAt(row, 7).toString());
        String fechaTerminoContrato = tablaPersonal.getValueAt(row, 8).toString();

        return new ModeloPersonal(id, nombre, cedula, correo, fijo, especialidad, rol, activo, fechaTerminoContrato);
    }

    private void handleTipoMaquinariaDoubleClick() {
        int selectedRow = tablaTipoMaquinaria.getSelectedRow();
        Long id = Long.parseLong(tablaTipoMaquinaria.getValueAt(selectedRow, 0).toString());
        String nombre = tablaTipoMaquinaria.getValueAt(selectedRow, 1).toString();

        String[] options = {"Modificar", "Eliminar"};
        int option = JOptionPane.showOptionDialog(panel, "¿Qué desea hacer con el tipo de maquinaria?", "Tipo de maquinaria", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (option == 1) {
            eliminarTipoMaquinaria(id);
        } else if (option == 0) {
            modificarTipoMaquinaria(id, nombre);
        }

        updateTipoMaquinariaTable();
    }

    private void addTipoMaquinaria() {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del tipo de maquinaria");
        if (nombre == null || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "El nombre del tipo de maquinaria no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoMaquinaria tipoMaquinaria = new TipoMaquinaria(nombre);
        tipoMaquinariaRepo.insertar(tipoMaquinaria);
        updateTipoMaquinariaTable();
    }

    private void handleMaquinariaDoubleClick() {
        int selectedRow = tablaMaquinarias.getSelectedRow();
        ModeloMaquinaria maquinariaSeleccionada = getSelectedMaquinariaModel(selectedRow);

        String[] options = {"Modificar", "Eliminar"};
        int option = JOptionPane.showOptionDialog(panel, "¿Qué desea hacer con la maquinaria?", "Maquinaria", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (option == 1) {
            eliminarMaquinaria(maquinariaSeleccionada.getId());
        } else if (option == 0) {
            openWindow("Modificar Maquinaria", new ModificarMaquinaria(maquinariaSeleccionada).mainPanel, this::updateMaquinariaTable);
        }

        updateMaquinariaTable();
    }

    private ModeloMaquinaria getSelectedMaquinariaModel(int row) {
        Long id = Long.parseLong(tablaMaquinarias.getValueAt(row, 0).toString());
        Long tipoMaquinariaId = Long.parseLong(tablaMaquinarias.getValueAt(row, 1).toString());
        String nombre = tablaMaquinarias.getValueAt(row, 2).toString();
        String tiempoEstimadoStr = tablaMaquinarias.getValueAt(row, 3).toString();
        long horas = Long.parseLong(tiempoEstimadoStr.split(" ")[0]);
        long minutos = Long.parseLong(tiempoEstimadoStr.split(" ")[3]);
        Duration tiempoEstimadoDeUso = Duration.ofHours(horas).plusMinutes(minutos);
        Double costoPorTiempoDeUso = Double.parseDouble(tablaMaquinarias.getValueAt(row, 4).toString());
        Double costoTotal = Double.parseDouble(tablaMaquinarias.getValueAt(row, 5).toString());

        return new ModeloMaquinaria(id, tipoMaquinariaId, nombre, tiempoEstimadoDeUso, costoPorTiempoDeUso, costoTotal);
    }

// Methods to update tables

    private void updateClienteTable() {
        DefaultTableModel clienteTableModel = mapearModeloCliente(clienteRepo.obtenerTodos());
        tablaClientes.setModel(clienteTableModel);
        ajustarAnchoColumnas(tablaClientes);
    }

    private void updatePersonalTable() {
        DefaultTableModel personalTableModel = mapearModeloPersonal(personalRepo.obtenerTodos());
        tablaPersonal.setModel(personalTableModel);
        ajustarAnchoColumnas(tablaPersonal);
    }

    private void updateTipoMaquinariaTable() {
        DefaultTableModel tipoMaquinariaTableModel = mapearModeloTipoMaquinaria(tipoMaquinariaRepo.obtenerTodos());
        tablaTipoMaquinaria.setModel(tipoMaquinariaTableModel);
        ajustarAnchoColumnas(tablaTipoMaquinaria);
    }

    private void updateMaquinariaTable() {
        List<ModeloMaquinaria> maquinariaList = tipoMaquinariaRepo.obtenerTodosMaquinaria();
        DefaultTableModel maquinariaTableModel = mapearModeloMaquinaria(maquinariaList);

        // Reasignar el modelo a la tabla
        tablaMaquinarias.setModel(maquinariaTableModel);

        // Ajustar el ancho de las columnas
        ajustarAnchoColumnas(tablaMaquinarias);

        // Forzar el repintado de la tabla para asegurarse de que los cambios se reflejen
        tablaMaquinarias.repaint();
    }

    private void eliminarTipoMaquinaria(Long id) {
        int confirm = JOptionPane.showConfirmDialog(panel,
            "¿Estás seguro de que deseas eliminar este tipo de maquinaria?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tipoMaquinariaRepo.eliminar(id);
            JOptionPane.showMessageDialog(panel, "Tipo de maquinaria eliminado correctamente.");
        }
    }

    private void modificarTipoMaquinaria(Long id, String nombre) {
        String nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del tipo de maquinaria", nombre);
        if (nuevoNombre == null || nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "El nombre del tipo de maquinaria no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoMaquinaria tipoMaquinaria = new TipoMaquinaria(id, nuevoNombre);
        tipoMaquinariaRepo.actualizar(tipoMaquinaria);
        JOptionPane.showMessageDialog(panel, "Tipo de maquinaria actualizado correctamente.");
    }

    private void eliminarMaquinaria(Long id) {
        int confirm = JOptionPane.showConfirmDialog(panel,
            "¿Estás seguro de que deseas eliminar esta maquinaria?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tipoMaquinariaRepo.eliminarMaquinaria(id);
            JOptionPane.showMessageDialog(panel, "Maquinaria eliminada correctamente.");
        }
    }

    public void setTablePersonalModel() throws IllegalAccessException {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel personalTableModel = mapearModeloPersonal(personalRepo.obtenerTodos());

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

    public DefaultTableModel mapearModeloPersonal(List<ModeloPersonal> modeloPersonalList) {
        Field[] fields = ModeloPersonal.class.getDeclaredFields();

        // Create a Vector to hold the column names
        Vector<String> columnNames = new Vector<>();
        for (Field field : fields) {
            columnNames.add(field.getName());
        }

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (ModeloPersonal personal : modeloPersonalList) {
            Vector<Object> row = new Vector<>();
            row.add(personal.getId());
            row.add(personal.getNombre());
            row.add(personal.getCedula());
            row.add(personal.getCorreo());
            row.add(personal.getFijo());
            row.add(personal.getEspecialidad());
            row.add(personal.getRol());
            row.add(personal.getActivo());
            row.add(personal.getFechaFinContrato());
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void setTableTipoMaquinariaModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel tipoMaquinariaTableModel = mapearModeloTipoMaquinaria(tipoMaquinariaRepo.obtenerTodos());

        // Set the TableModel to the JTable
        tablaTipoMaquinaria.setModel(tipoMaquinariaTableModel);

        ajustarAnchoColumnas(tablaTipoMaquinaria);

        // Create a JScrollPane to wrap the JTable
        JScrollPane scrollPane = new JScrollPane(tablaTipoMaquinaria);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add the JScrollPane to the panel
        panelTipoMaquinaria.add(scrollPane, BorderLayout.WEST);
    }

    public DefaultTableModel mapearModeloTipoMaquinaria(List<TipoMaquinaria> tipoMaquinariaList) {
        Field[] fields = TipoMaquinaria.class.getDeclaredFields();

        // Create a Vector to hold the column names
        Vector<String> columnNames = new Vector<>();
        for (Field field : fields) {
            if (field.getName().equals("id") || field.getName().equals("nombre")) {
                columnNames.add(field.getName());
            }
        }

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (TipoMaquinaria tipoMaquinaria : tipoMaquinariaList) {
            Vector<Object> row = new Vector<>();
            row.add(tipoMaquinaria.getId());
            row.add(tipoMaquinaria.getNombre());
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public DefaultTableModel mapearModeloMaquinaria(List<ModeloMaquinaria> modeloMaquinariaList) {
        Field[] fields = ModeloMaquinaria.class.getDeclaredFields();

        // Create a Vector to hold the column names
        Vector<String> columnNames = new Vector<>();
        for (Field field : fields) {
            columnNames.add(field.getName());
        }

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (ModeloMaquinaria maquinaria : modeloMaquinariaList) {
            Vector<Object> row = new Vector<>();
            row.add(maquinaria.getId());
            row.add(maquinaria.getTipoMaquinariaId());
            row.add(maquinaria.getNombre());
            row.add(maquinaria.getTiempoEstimadoDeUso());
            row.add(maquinaria.getCostoPorTiempoDeUso());
            row.add(maquinaria.getCostoTotal());
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void setTableMaquinariaModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel maquinariaTableModel = mapearModeloMaquinaria(tipoMaquinariaRepo.obtenerTodosMaquinaria());

        // Set the TableModel to the JTable
        tablaMaquinarias.setModel(maquinariaTableModel);

        ajustarAnchoColumnas(tablaMaquinarias);

        // Create a JScrollPane to wrap the JTable
        JScrollPane scrollPane = new JScrollPane(tablaMaquinarias);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add the JScrollPane to the panel
        panelMaquinarias.add(scrollPane, BorderLayout.WEST);
    }
}
