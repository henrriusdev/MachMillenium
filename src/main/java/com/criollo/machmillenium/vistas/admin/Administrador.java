package com.criollo.machmillenium.vistas.admin;

import com.criollo.machmillenium.entidades.Material;
import com.criollo.machmillenium.entidades.TipoInsumo;
import com.criollo.machmillenium.entidades.TipoMaquinaria;
import com.criollo.machmillenium.modelos.ModeloCliente;
import com.criollo.machmillenium.modelos.ModeloMaquinaria;
import com.criollo.machmillenium.modelos.ModeloPersonal;
import com.criollo.machmillenium.repos.ClienteRepo;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.repos.TipoInsumoRepo;
import com.criollo.machmillenium.repos.TipoMaquinariaRepo;
import com.criollo.machmillenium.utilidades.TableColumnAdjuster;
import com.criollo.machmillenium.vistas.emergentes.clientes.AgregarCliente;
import com.criollo.machmillenium.vistas.emergentes.clientes.ModificarCliente;
import com.criollo.machmillenium.vistas.emergentes.maquinaria.AgregarMaquinaria;
import com.criollo.machmillenium.vistas.emergentes.maquinaria.ModificarMaquinaria;
import com.criollo.machmillenium.vistas.emergentes.material.AgregarMaterial;
import com.criollo.machmillenium.vistas.emergentes.material.EditarMaterial;
import com.criollo.machmillenium.vistas.emergentes.personal.AgregarPersonal;
import com.criollo.machmillenium.vistas.emergentes.personal.ModificarPersonal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDateTime;
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
    private JTable tablaPresupuesto;
    private JButton botonAgregarPresupuesto;
    private JPanel botonesPanelPersonal;
    private JPanel panelTipoMaquinaria;
    private JTable tablaTipoMaquinaria;
    private JButton botonAgregar;
    private JTable tablaTipoInsumos;
    private JButton botonAgregarTipoInsumo;
    private JTable tablaMateriales;
    private JButton botonAgregarMaterial;
    private JFrame jframe;
    private final PersonalRepo personalRepo;
    private final ClienteRepo clienteRepo;
    private final TipoMaquinariaRepo tipoMaquinariaRepo;
    private final TipoInsumoRepo tipoInsumoRepo;

    public Administrador(JFrame jframe) {
        this.personalRepo = new PersonalRepo();
        this.clienteRepo = new ClienteRepo();
        this.tipoMaquinariaRepo = new TipoMaquinariaRepo();
        this.tipoInsumoRepo = new TipoInsumoRepo();
        this.jframe = jframe;

        setTables();

        btnAgregarCliente.addActionListener(e -> btnAgregarClienteClick());

        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaClientesClick(e);
            }
        });

        tablaPersonal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaPersonalClick(e);
            }
        });
        agregarButton.addActionListener(e -> agregarButtonActionPerformed());
        botonAgregar.addActionListener(e -> botonAgregarActionPerformed());
        tablaTipoMaquinaria.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaTipoMaquinariaClick(e);
            }
        });
        botonAgregarMaquinaria.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(panel).setEnabled(false);
            JFrame newJframe = new JFrame("Agregar Maquinaria");
            newJframe.setContentPane(new AgregarMaquinaria().mainPanel);
            newJframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newJframe.pack();
            newJframe.setVisible(true);

            newJframe.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    // Reactivar el JFrame principal
                    SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                    SwingUtilities.getWindowAncestor(panel).setVisible(true);
                    SwingUtilities.getWindowAncestor(panel).toFront();
                    DefaultTableModel maquinariaTableModel = mapearModeloMaquinaria(tipoMaquinariaRepo.obtenerTodosMaquinaria());
                    tablaMaquinarias.setModel(maquinariaTableModel);
                    ajustarAnchoColumnas(tablaMaquinarias);
                }
            });
        });
        tablaMaquinarias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tablaMaquinarias.getSelectedRow() != -1) {
                    int selectedRow = tablaMaquinarias.getSelectedRow();
                    Long id = Long.parseLong(tablaMaquinarias.getValueAt(selectedRow, 0).toString());
                    Long tipoMaquinariaId = Long.parseLong(tablaMaquinarias.getValueAt(selectedRow, 1).toString());
                    String nombre = tablaMaquinarias.getValueAt(selectedRow, 2).toString();
                    Long horas = Long.parseLong(tablaMaquinarias.getValueAt(selectedRow, 3).toString().split(" ")[0]);
                    Long minutos = Long.parseLong(tablaMaquinarias.getValueAt(selectedRow, 3).toString().split(" ")[3]);
                    Duration tiempoEstimadoDeUso = Duration.ofHours(horas).plusMinutes(minutos);
                    Double costoPorTiempoDeUso = Double.parseDouble(tablaMaquinarias.getValueAt(selectedRow, 4).toString());
                    Double costoTotal = Double.parseDouble(tablaMaquinarias.getValueAt(selectedRow, 5).toString());

                    // preguntar al usuario si desea modificar o eliminar el tipo de maquinaria
                    String[] options = {"Modificar", "Eliminar"};
                    int option = JOptionPane.showOptionDialog(panel, "¿Qué desea hacer con la maquinaria?", "Maquinaria", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    ModeloMaquinaria maquinaria;
                    switch (option) {
                        case 1:
                            tipoMaquinariaRepo.eliminarMaquinaria(id);
                            break;
                        case 0:
                            maquinaria = new ModeloMaquinaria(id, tipoMaquinariaId, nombre, tiempoEstimadoDeUso, costoPorTiempoDeUso, costoTotal);
                            JFrame modificarMaquinariaFrame = new JFrame("Modificar Maquinaria");
                            modificarMaquinariaFrame.setContentPane(new ModificarMaquinaria(maquinaria).mainPanel);
                            modificarMaquinariaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            modificarMaquinariaFrame.pack();
                            modificarMaquinariaFrame.setVisible(true);
                            modificarMaquinariaFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                                @Override
                                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                                    DefaultTableModel maquinariaTableModel = mapearModeloMaquinaria(tipoMaquinariaRepo.obtenerTodosMaquinaria());
                                    tablaMaquinarias.setModel(maquinariaTableModel);
                                    ajustarAnchoColumnas(tablaMaquinarias);
                                }
                            });
                            break;
                        default:
                            break;
                    }

                    DefaultTableModel maquinariaTableModel = mapearModeloMaquinaria(tipoMaquinariaRepo.obtenerTodosMaquinaria());
                    tablaMaquinarias.setModel(maquinariaTableModel);

                }
            }
        });
        botonAgregarTipoInsumo.addActionListener(e -> botonAgregarTipoInsumoClick());
        tablaTipoInsumos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaTipoInsumosClick(e);
            }
        });
        botonAgregarMaterial.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
    botonAgregarMaterialClick();
            }
        });
        tablaMateriales.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaMaterialClick(e);
            }
        });
        botonAgregarPresupuesto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        tablaPresupuesto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }
        });
    }

    public void setTables(){
        try {
            setTablePersonalModel();
            setTableClienteModel();
            setTableTipoMaquinariaModel();
            setTableMaquinariaModel();
            setTableTipoInsumoModel();
            setTableMaterialModel();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void btnAgregarClienteClick() {
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
    }

    public void tablaClientesClick(MouseEvent e) {
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

    public void tablaPersonalClick(MouseEvent e){
        if (e.getClickCount() == 2 && tablaPersonal.getSelectedRow() != -1) {
            int selectedRow = tablaPersonal.getSelectedRow();
            System.out.println(selectedRow);

            // Obtener los valores de la fila seleccionada para crear el ModeloCliente
            Long id = Long.parseLong(tablaPersonal.getValueAt(selectedRow, 0).toString());
            String nombre = tablaPersonal.getValueAt(selectedRow, 1).toString();
            String cedula = tablaPersonal.getValueAt(selectedRow, 2).toString();
            String correo = tablaPersonal.getValueAt(selectedRow, 3).toString();
            Boolean fijo = Boolean.parseBoolean(tablaPersonal.getValueAt(selectedRow, 4).toString());
            String especialidad = tablaPersonal.getValueAt(selectedRow, 5).toString();
            String rol = tablaPersonal.getValueAt(selectedRow, 6).toString();
            Boolean activo = Boolean.parseBoolean(tablaPersonal.getValueAt(selectedRow, 7).toString());
            String fechaTerminoContrato = tablaPersonal.getValueAt(selectedRow, 8).toString();

            // Crear el ModeloCliente correspondiente
            ModeloPersonal personalSeleccionado = new ModeloPersonal(id, nombre, cedula, correo, fijo, especialidad, rol, activo, fechaTerminoContrato);

            // Abrir la vista ModificarCliente y pasarle el ModeloCliente
            JFrame modificarPersonalFrame = new JFrame("Modificar Personal");
            modificarPersonalFrame.setContentPane(new ModificarPersonal(personalSeleccionado).panel);
            modificarPersonalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            modificarPersonalFrame.pack();
            modificarPersonalFrame.setVisible(true);

            // Manejar el cierre de la ventana
            modificarPersonalFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    // Actualizar la tabla si es necesario tras cerrar la ventana de edición
                    DefaultTableModel personalTableModel = mapearModeloPersonal(personalRepo.obtenerTodos());

                    // Set the TableModel to the JTable
                    tablaPersonal.setModel(personalTableModel);
                    ajustarAnchoColumnas(tablaPersonal);
                }
            });
        }
    }

    public void agregarButtonActionPerformed() {
        SwingUtilities.getWindowAncestor(panel).setEnabled(false);
        JFrame newJframe = new JFrame("Agregar Personal");
        newJframe.setContentPane(new AgregarPersonal().panel);
        newJframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newJframe.pack();
        newJframe.setVisible(true);

        newJframe.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                // Reactivar el JFrame principal
                SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                SwingUtilities.getWindowAncestor(panel).toFront();
                DefaultTableModel personalTableModel = mapearModeloPersonal(personalRepo.obtenerTodos());
                tablaPersonal.setModel(personalTableModel);
                ajustarAnchoColumnas(tablaPersonal);
            }
        });
    }

    public void botonAgregarActionPerformed(){
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del tipo de maquinaria");
        if (nombre == null || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "El nombre del tipo de maquinaria no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoMaquinaria tipoMaquinaria = new TipoMaquinaria(nombre);
        tipoMaquinariaRepo.insertar(tipoMaquinaria);
        DefaultTableModel tipoMaquinariaTableModel = mapearModeloTipoMaquinaria(tipoMaquinariaRepo.obtenerTodos());
        tablaTipoMaquinaria.setModel(tipoMaquinariaTableModel);
    }

    public void tablaTipoMaquinariaClick(MouseEvent e) {
        if (e.getClickCount() == 2 && tablaTipoMaquinaria.getSelectedRow() != -1) {
            int selectedRow = tablaTipoMaquinaria.getSelectedRow();
            Long id = Long.parseLong(tablaTipoMaquinaria.getValueAt(selectedRow, 0).toString());
            String nombre = tablaTipoMaquinaria.getValueAt(selectedRow, 1).toString();

            // preguntar al usuario si desea modificar o eliminar el tipo de maquinaria
            String[] options = {"Modificar", "Eliminar"};
            int option = JOptionPane.showOptionDialog(panel, "¿Qué desea hacer con el tipo de maquinaria?", "Tipo de maquinaria", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            TipoMaquinaria tipoMaquinaria;
            switch (option) {
                case 1:
                    tipoMaquinaria = new TipoMaquinaria(nombre);
                    tipoMaquinaria.setId(id);
                    tipoMaquinaria.setEliminado(LocalDateTime.now());
                    tipoMaquinariaRepo.actualizar(tipoMaquinaria);
                    break;
                case 0:
                    String nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del tipo de maquinaria", nombre);
                    if (nuevoNombre == null || nuevoNombre.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "El nombre del tipo de maquinaria no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    tipoMaquinaria = new TipoMaquinaria(nuevoNombre);
                    tipoMaquinaria.setId(id);
                    tipoMaquinariaRepo.actualizar(tipoMaquinaria);
                    break;
            }

            DefaultTableModel tipoMaquinariaTableModel = mapearModeloTipoMaquinaria(tipoMaquinariaRepo.obtenerTodos());
            tablaTipoMaquinaria.setModel(tipoMaquinariaTableModel);
        }
    }

    public void botonAgregarTipoInsumoClick(){
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del tipo de insumo");
        if (nombre == null || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "El nombre del tipo de insumo no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TipoInsumo tipoInsumo = new TipoInsumo(nombre);
        tipoInsumoRepo.insertar(tipoInsumo);
        DefaultTableModel tipoInsumoTableModel = mapearModeloTipoInsumo(tipoInsumoRepo.obtenerTodos());
        tablaTipoInsumos.setModel(tipoInsumoTableModel);
        ajustarAnchoColumnas(tablaTipoInsumos);
    }

    public void tablaTipoInsumosClick(MouseEvent e) {
        if (e.getClickCount() == 2 && tablaTipoInsumos.getSelectedRow() != -1) {
            int selectedRow = tablaTipoInsumos.getSelectedRow();
            Long id = Long.parseLong(tablaTipoInsumos.getValueAt(selectedRow, 0).toString());
            String nombre = tablaTipoInsumos.getValueAt(selectedRow, 1).toString();

            // preguntar al usuario si desea modificar o eliminar el tipo de maquinaria
            String[] options = {"Modificar", "Eliminar"};
            int option = JOptionPane.showOptionDialog(panel, "¿Qué desea hacer con el tipo de maquinaria?", "Tipo de maquinaria", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            TipoInsumo tipoInsumo;
            switch (option) {
                case 1:
                    tipoInsumoRepo.eliminar(id);
                    break;
                case 0:
                    String nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del tipo de insumo", nombre);
                    if (nuevoNombre == null || nuevoNombre.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "El nombre del tipo de insumo no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    tipoInsumo = new TipoInsumo(nuevoNombre);
                    tipoInsumo.setId(id);
                    tipoInsumoRepo.actualizar(tipoInsumo);
                    break;
            }

            DefaultTableModel tipoInsumoTableModel = mapearModeloTipoInsumo(tipoInsumoRepo.obtenerTodos());
            tablaTipoInsumos.setModel(tipoInsumoTableModel);
            ajustarAnchoColumnas(tablaTipoInsumos);
        }
    }

    public void botonAgregarMaterialClick(){
        SwingUtilities.getWindowAncestor(panel).setEnabled(false);
        JFrame newJframe = new JFrame("Agregar Material");
        newJframe.setContentPane(new AgregarMaterial(this.tipoInsumoRepo).panel);
        newJframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newJframe.pack();
        newJframe.setVisible(true);

        newJframe.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                // Reactivar el JFrame principal
                SwingUtilities.getWindowAncestor(panel).setEnabled(true);

                SwingUtilities.getWindowAncestor(panel).toFront();
                DefaultTableModel materialTableModel = mapearModeloMaterial(tipoInsumoRepo.obtenerMateriales());
                tablaMateriales.setModel(materialTableModel);
                ajustarAnchoColumnas(tablaMateriales);
            }
        });
    }

    public void tablaMaterialClick(MouseEvent e){
        if (e.getClickCount() == 2 && tablaMateriales.getSelectedRow() != -1) {
            int selectedRow = tablaMateriales.getSelectedRow();
            Long id = Long.parseLong(tablaMateriales.getValueAt(selectedRow, 0).toString());
            String tipoInsumo = tablaMateriales.getValueAt(selectedRow, 1).toString();
            String nombre = tablaMateriales.getValueAt(selectedRow, 2).toString();
            Long cantidad = Long.parseLong(tablaMateriales.getValueAt(selectedRow, 3).toString());
            Double costo = Double.parseDouble(tablaMateriales.getValueAt(selectedRow, 4).toString());

            // preguntar al usuario si desea modificar o eliminar el tipo de maquinaria
            String[] options = {"Modificar", "Eliminar"};
            int option = JOptionPane.showOptionDialog(panel, "¿Qué desea hacer con el material?", "Material", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            Material material;
            switch (option) {
                case 1:
                    tipoInsumoRepo.eliminarMaterial(id);
                    DefaultTableModel materialTableModel = mapearModeloMaterial(tipoInsumoRepo.obtenerMateriales());
                    tablaMateriales.setModel(materialTableModel);
                    ajustarAnchoColumnas(tablaMateriales);
                    break;
                case 0:
                    material = new Material(id, tipoInsumo, nombre, cantidad, costo);
                    JFrame modificarMaterialFrame = new JFrame("Modificar Material");
                    modificarMaterialFrame.setContentPane(new EditarMaterial(material, this.tipoInsumoRepo).panel);
                    modificarMaterialFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    modificarMaterialFrame.pack();
                    modificarMaterialFrame.setVisible(true);
                    modificarMaterialFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                            SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                            SwingUtilities.getWindowAncestor(panel).setVisible(true);
                            SwingUtilities.getWindowAncestor(panel).toFront();
                            DefaultTableModel materialTableModel = mapearModeloMaterial(tipoInsumoRepo.obtenerMateriales());
                            tablaMateriales.setModel(materialTableModel);
                            ajustarAnchoColumnas(tablaMateriales);
                        }
                    });
                    break;
            }
        }
    }

    public void setTablePersonalModel() throws IllegalAccessException {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel personalTableModel = mapearModeloPersonal(personalRepo.obtenerTodos());

        // Set the TableModel to the JTable
        tablaPersonal.setModel(personalTableModel);

        ajustarAnchoColumnas(tablaPersonal);
    }

    public void ajustarAnchoColumnas(JTable tabla) {
        // Desactivar el ajuste automático de tamaño para que el JTable no redimensione automáticamente
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel columnModel = tabla.getColumnModel();

        for (int column = 0; column < tabla.getColumnCount(); column++) {
            TableColumn tableColumn = columnModel.getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < tabla.getRowCount(); row++) {
                TableCellRenderer cellRenderer = tabla.getCellRenderer(row, column);
                Component c = tabla.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + tabla.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                // Si superamos el ancho máximo, no necesitamos revisar más filas
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth);  // Ajustar ancho preferido
        }

        // Aplicar el ajuste de columnas usando el TableColumnAdjuster
        TableColumnAdjuster tca = new TableColumnAdjuster(tabla);
        tca.adjustColumns();  // Ajustar las columnas automáticamente
    }


    public void setTableClienteModel() throws IllegalAccessException {
        ClienteRepo clienteRepo = new ClienteRepo();

        // Create a DefaultTableModel with the column names and data
        DefaultTableModel clienteTableModel = mapearModeloCliente(clienteRepo.obtenerTodos());

        // Set the TableModel to the JTable
        tablaClientes.setModel(clienteTableModel);

        ajustarAnchoColumnas(tablaClientes);
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
    }

    public DefaultTableModel mapearModeloTipoInsumo(List<TipoInsumo> tipoInsumoList) {
        Field[] fields = TipoInsumo.class.getDeclaredFields();

        // Create a Vector to hold the column names
        Vector<String> columnNames = new Vector<>();
        for (Field field : fields) {
            if (field.getName().equals("id") || field.getName().equals("nombre")) {
                columnNames.add(field.getName());
            }
        }

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (TipoInsumo tipoInsumo : tipoInsumoList) {
            Vector<Object> row = new Vector<>();
            row.add(tipoInsumo.getId());
            row.add(tipoInsumo.getNombre());
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void setTableTipoInsumoModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel tipoInsumoTableModel = mapearModeloTipoInsumo(tipoInsumoRepo.obtenerTodos());

        // Set the TableModel to the JTable
        tablaTipoInsumos.setModel(tipoInsumoTableModel);

        ajustarAnchoColumnas(tablaTipoInsumos);
    }

    public void setTableMaterialModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel materialTableModel = mapearModeloMaterial(tipoInsumoRepo.obtenerMateriales());

        // Set the TableModel to the JTable
        tablaMateriales.setModel(materialTableModel);

        ajustarAnchoColumnas(tablaMateriales);
    }

    public DefaultTableModel mapearModeloMaterial(List<Material> materialList) {
        Field[] fields = Material.class.getDeclaredFields();

        // Create a Vector to hold the column names
        Vector<String> columnNames = new Vector<>();
        for (Field field : fields) {
            if (!field.getName().equals("creado") && !field.getName().equals("modificado") && !field.getName().equals("eliminado")) {
                columnNames.add(field.getName());
            }
        }

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (Material material : materialList) {
            Vector<Object> row = new Vector<>();
            row.add(material.getId());
            row.add(material.getTipoInsumo().getNombre());
            row.add(material.getNombre());
            row.add(material.getCantidad());
            row.add(material.getCosto());
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
