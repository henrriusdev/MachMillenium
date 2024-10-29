package com.criollo.machmillenium.vistas.gestor;

import com.criollo.machmillenium.entidades.*;
import com.criollo.machmillenium.modelos.ModeloCliente;
import com.criollo.machmillenium.modelos.ModeloInasistencia;
import com.criollo.machmillenium.modelos.ModeloMaquinaria;
import com.criollo.machmillenium.modelos.ModeloPersonal;
import com.criollo.machmillenium.repos.*;
import com.criollo.machmillenium.utilidades.GeneradorGraficos;
import com.criollo.machmillenium.utilidades.GeneradorReportes;
import com.criollo.machmillenium.utilidades.TableColumnAdjuster;
import com.criollo.machmillenium.utilidades.Utilidades;
import com.criollo.machmillenium.vistas.emergentes.Graficos;
import com.criollo.machmillenium.vistas.emergentes.clientes.AgregarCliente;
import com.criollo.machmillenium.vistas.emergentes.clientes.ModificarCliente;
import com.criollo.machmillenium.vistas.emergentes.maquinaria.AgregarMaquinaria;
import com.criollo.machmillenium.vistas.emergentes.maquinaria.ModificarMaquinaria;
import com.criollo.machmillenium.vistas.emergentes.material.AgregarMaterial;
import com.criollo.machmillenium.vistas.emergentes.material.EditarMaterial;
import com.criollo.machmillenium.vistas.emergentes.obra.ModificarRegistroObra;
import com.criollo.machmillenium.vistas.emergentes.obra.RegistrarObra;
import com.criollo.machmillenium.vistas.emergentes.personal.AgregarPersonal;
import com.criollo.machmillenium.vistas.emergentes.personal.ModificarPersonal;
import com.criollo.machmillenium.vistas.emergentes.presupuesto.CrearPresupuesto;
import com.criollo.machmillenium.vistas.emergentes.presupuesto.EditarPresupuesto;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class GestorProyectos {
    public JPanel panel;
    private JTable tablaClientes;
    private JButton btnAgregarCliente;
    private JTable tablaInasistencia;
    private JTable tablaObras;
    private JButton botonAgregarObra;
    private JTable tablaMaquinarias;
    private JButton botonAgregarMaquinaria;
    private JTable tablaPresupuesto;
    private JButton botonAgregarPresupuesto;
    private JTable tablaTipoMaquinaria;
    private JButton botonAgregar;
    private JTable tablaTipoInsumos;
    private JButton botonAgregarTipoInsumo;
    private JTable tablaMateriales;
    private JButton botonAgregarMaterial;
    private JPanel inicio;
    private JButton recuperarClaveButton;
    private JButton verGraficosClientesButton;
    private JButton imprimirClientesButton;
    private JButton imprimirInasistenciaButton;
    private JButton imprimirTablaButton;
    private JButton verGraficasMateriales;
    private JButton imprimirTablaButton1;
    private JButton verGraficasObras;
    private JButton imprimirTablaButton2;
    private JButton verGraficasMaquinarias;
    private JButton imprimirMaquinarias;
    private JButton registrarInasistencia;
    private final PersonalRepo personalRepo;
    private final ClienteRepo clienteRepo;
    private final TipoMaquinariaRepo tipoMaquinariaRepo;
    private final TipoInsumoRepo tipoInsumoRepo;
    private final PresupuestoRepo presupuestoRepo;
    private final ObraRepo obraRepo;

    public GestorProyectos(Personal personal) {
        this.personalRepo = new PersonalRepo();
        this.clienteRepo = new ClienteRepo();
        this.tipoMaquinariaRepo = new TipoMaquinariaRepo();
        this.tipoInsumoRepo = new TipoInsumoRepo();
        this.presupuestoRepo = new PresupuestoRepo();
        this.obraRepo = new ObraRepo();
        Utilidades.cambiarClaveOriginal(personal.getClave(), personal.getId(), true);

        setTables();

        btnAgregarCliente.addActionListener(e -> btnAgregarClienteClick());

        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaClientesClick(e);
            }
        });

        cargarGraficos();

        tablaInasistencia.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaInasistenciaClick(e);
            }
        });
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

            newJframe.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent windowEvent) {
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
                            modificarMaquinariaFrame.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosed(WindowEvent windowEvent) {
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
        botonAgregarPresupuesto.addActionListener(e -> {
            SwingUtilities.getWindowAncestor(panel).setEnabled(false);
            JFrame newJframe = new JFrame("Agregar Presupuesto");
            newJframe.setContentPane(new CrearPresupuesto(presupuestoRepo).panel);
            newJframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newJframe.pack();
            newJframe.setVisible(true);

            newJframe.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent windowEvent) {
                    // Reactivar el JFrame principal
                    SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                    SwingUtilities.getWindowAncestor(panel).toFront();
                    DefaultTableModel presupuestoTableModel = mapearModeloPresupuesto(presupuestoRepo.obtenerTodos());
                    tablaPresupuesto.setModel(presupuestoTableModel);
                    ajustarAnchoColumnas(tablaPresupuesto);
                }
            });
        });
        tablaPresupuesto.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaPresupuestoClick(e);
            }
        });
        botonAgregarObra.addActionListener(e -> botonAgregarObraClick());
        tablaObras.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaObrasClick(e);
            }
        });
        recuperarClaveButton.addActionListener(e -> Utilidades.cambiarClaveOriginal(personal.getClave(), personal.getId(), false));
        imprimirClientesButton.addActionListener(e -> GeneradorReportes.generarReporteClientes());
//        imprimirInasistenciaButton.addActionListener(e -> GeneradorReportes.generarReporteInasistencia());
        verGraficosClientesButton.addActionListener(e -> {
            List<ModeloCliente> clientes = clienteRepo.obtenerTodos();
            List<Obra> obras = obraRepo.obtenerObras();
            String[] clientesObras = obras.stream().map(obra -> obra.getPresupuesto().getCliente().getNombre()).toArray(String[]::new);

            Map<String, Long> clienteObrasMap = clientes.stream()
                    .collect(Collectors.toMap(ModeloCliente::getNombre, cliente -> 0L));

// Luego, contamos las obras asignadas a cada cliente
            for (String clienteObra : clientesObras) {
                clienteObrasMap.put(clienteObra, clienteObrasMap.getOrDefault(clienteObra, 0L) + 1);
            }

// Convertimos el mapa a los arreglos necesarios
            String[] nombresClientes = clienteObrasMap.keySet().toArray(new String[0]);
            double[] cantidadObras = clienteObrasMap.values().stream().mapToDouble(Long::doubleValue).toArray();
            ChartPanel chartPanel = GeneradorGraficos.generarGraficoBarras("Obras por cliente", "Clientes", "Cantidad de obras", nombresClientes, cantidadObras, 385, 300);

            double[] costosClientes = obras.stream()
                    .mapToDouble(obra -> obra.getPresupuesto().getCosto())
                    .toArray();

// Definir las desviaciones como un porcentaje del costo
            double[] desvios = new double[costosClientes.length];
            for (int i = 0; i < costosClientes.length; i++) {
                desvios[i] = costosClientes[i] * 0.1; // Ejemplo: 10% del costo como desviación
            }

// Generar el gráfico de líneas con desviación
            ChartPanel chartPanelCostos = GeneradorGraficos.generarGraficoDesviacion(
                    "Costos de obras por cliente", "Clientes", "Costo", "Costo", costosClientes, desvios, 790, 300);

            List<ChartPanel> graficos = List.of(chartPanel, chartPanelCostos);
            Graficos graficosDialog = new Graficos("Gráficos", graficos);
            graficosDialog.pack();
            graficosDialog.setVisible(true);
        });
        verGraficasMaquinarias.addActionListener(e -> {
            List<Maquinaria> maquinarias = tipoMaquinariaRepo.obtenerMaquinarias();
            String[] tiposMaquinaria = maquinarias.stream().map(maquinaria -> maquinaria.getTipoMaquinaria().getNombre()).toArray(String[]::new);
            double[] cantidadMaquinarias = maquinarias.stream().collect(Collectors.groupingBy(maquinaria -> maquinaria.getTipoMaquinaria().getNombre(), Collectors.counting()))
                    .values().stream().mapToDouble(Long::doubleValue).toArray();
            tiposMaquinaria = Arrays.stream(tiposMaquinaria).distinct().toArray(String[]::new);
            ChartPanel chartPanel = GeneradorGraficos.generarGraficoPastel("Maquinarias por tipo", tiposMaquinaria, cantidadMaquinarias, 380, 250);

            Graficos graficosDialog = new Graficos("Gráficos", List.of(chartPanel));
            graficosDialog.pack();
            graficosDialog.setVisible(true);
        });
        verGraficasMateriales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Material> materiales = tipoInsumoRepo.obtenerMateriales();
                String[] tiposInsumo = materiales.stream().map(material -> material.getTipoInsumo().getNombre()).toArray(String[]::new);
                double[] cantidadMateriales = materiales.stream().collect(Collectors.groupingBy(material -> material.getTipoInsumo().getNombre(), Collectors.counting()))
                        .values().stream().mapToDouble(Long::doubleValue).toArray();
                tiposInsumo = Arrays.stream(tiposInsumo).distinct().toArray(String[]::new);
                ChartPanel chartPanel = GeneradorGraficos.generarGraficoPastel("Materiales por tipo", tiposInsumo, cantidadMateriales, 380, 250);

                Graficos graficosDialog = new Graficos("Gráficos", List.of(chartPanel));
                graficosDialog.pack();
                graficosDialog.setVisible(true);
            }
        });
        verGraficasObras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Obra> obras = obraRepo.obtenerObras();
                String[] estados = obras.stream().map(Obra::getEstado).toArray(String[]::new);
                double[] cantidadObras = obras.stream().collect(Collectors.groupingBy(Obra::getEstado, Collectors.counting()))
                        .values().stream().mapToDouble(Long::doubleValue).toArray();
                estados = Arrays.stream(estados).distinct().toArray(String[]::new);
                ChartPanel chartPanel = GeneradorGraficos.generarGraficoPastel("Obras por estado", estados, cantidadObras, 380, 250);

                String[] tiposObra = obras.stream().map(obra -> obra.getTipoObra().getNombre()).toArray(String[]::new);
                double[] cantidadTiposObra = obras.stream().collect(Collectors.groupingBy(obra -> obra.getTipoObra().getNombre(), Collectors.counting()))
                        .values().stream().mapToDouble(Long::doubleValue).toArray();
                tiposObra = Arrays.stream(tiposObra).distinct().toArray(String[]::new);
                ChartPanel chartPanelTiposObra = GeneradorGraficos.generarGraficoPastel("Obras por tipo", tiposObra, cantidadTiposObra, 380, 250);

                String[] nombresObras = obras.stream().map(obra -> obra.getNombre()).toArray(String[]::new);
                double[] costosObras = obras.stream().mapToDouble(obra -> obra.getPresupuesto().getCosto()).toArray();
                ChartPanel chartPanelCostos = GeneradorGraficos.generarGraficoBarras("Costos de obras", "Obras", "Costo", nombresObras, costosObras, 380, 250);

                String[] nombresClientes = obras.stream().map(obra -> obra.getPresupuesto().getCliente().getNombre()).toArray(String[]::new);
                double[] cantidadObrasClientes = obras.stream().collect(Collectors.groupingBy(obra -> obra.getPresupuesto().getCliente().getNombre(), Collectors.counting()))
                        .values().stream().mapToDouble(Long::doubleValue).toArray();
                nombresClientes = Arrays.stream(nombresClientes).distinct().toArray(String[]::new);
                ChartPanel chartPanelClientes = GeneradorGraficos.generarGraficoPastel("Obras por cliente", nombresClientes, cantidadObrasClientes, 380, 250);

                Graficos graficosDialog = new Graficos("Gráficos", List.of(chartPanel, chartPanelTiposObra, chartPanelCostos, chartPanelClientes));
                graficosDialog.pack();
                graficosDialog.setVisible(true);
            }
        });
        imprimirMaquinarias.addActionListener(e -> GeneradorReportes.generarReporteMaquinaria());
        imprimirTablaButton.addActionListener(e -> GeneradorReportes.generarReporteMateriales());
        imprimirTablaButton2.addActionListener(e -> GeneradorReportes.generarReporteObras());
        registrarInasistencia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog((JFrame) null, "Registrar Inasistencia", true);
                dialog.setContentPane(new RegistrarInasistencia(personalRepo).contentPane);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                DefaultTableModel inasistenciaTableModel = mapearModeloInasistencia(personalRepo.obtenerInasistenciasModelo());
                tablaInasistencia.setModel(inasistenciaTableModel);
                ajustarAnchoColumnas(tablaInasistencia);
            }
        });
    }

    public void setTables(){
        try {
            setTableInasistenciaModel();
            setTableClienteModel();
            setTableTipoMaquinariaModel();
            setTableMaquinariaModel();
            setTableTipoInsumoModel();
            setTableMaterialModel();
            setTablePresupuestoModel();
            setTableObraModel();
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

        newJframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                // Reactivar el JFrame principal
                SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                SwingUtilities.getWindowAncestor(panel).toFront();
                DefaultTableModel clienteTableModel = mapearModeloCliente(clienteRepo.obtenerTodos());
                tablaClientes.setModel(clienteTableModel);
                ajustarAnchoColumnas(tablaClientes);
                cargarGraficos();
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
            modificarClienteFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent windowEvent) {
                    // Actualizar la tabla si es necesario tras cerrar la ventana de edición
                    DefaultTableModel clienteTableModel = mapearModeloCliente(clienteRepo.obtenerTodos());
                    tablaClientes.setModel(clienteTableModel);
                    ajustarAnchoColumnas(tablaClientes);
                    cargarGraficos();
                }
            });
        }
    }

    public void tablaInasistenciaClick(MouseEvent e){
        if (e.getClickCount() == 2 && tablaInasistencia.getSelectedRow() != -1) {
            int selectedRow = tablaInasistencia.getSelectedRow();
            System.out.println(selectedRow);

            // Obtener los valores de la fila seleccionada para crear el ModeloCliente
            Long id = Long.parseLong(tablaInasistencia.getValueAt(selectedRow, 0).toString());
            String nuevoMotivo = JOptionPane.showInputDialog("Ingrese el nuevo motivo de la inasistencia");
            while (nuevoMotivo == null || nuevoMotivo.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "El motivo de la inasistencia no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                nuevoMotivo = JOptionPane.showInputDialog("Ingrese el nuevo motivo de la inasistencia");
            }
            personalRepo.actualizarMotivoInasistencia(id, nuevoMotivo);

                    DefaultTableModel personalTableModel = mapearModeloInasistencia(personalRepo.obtenerInasistenciasModelo());

                    // Set the TableModel to the JTable
                    tablaInasistencia.setModel(personalTableModel);
                    ajustarAnchoColumnas(tablaInasistencia);
                    cargarGraficos();
        }
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
        cargarGraficos();
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
            cargarGraficos();
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

        newJframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
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
                    modificarMaterialFrame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent windowEvent) {
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

    public void tablaPresupuestoClick(MouseEvent e){
        if (e.getClickCount() == 2 && tablaPresupuesto.getSelectedRow() != -1) {
            int selectedRow = tablaPresupuesto.getSelectedRow();
            Long id = Long.parseLong(tablaPresupuesto.getValueAt(selectedRow, 0).toString());
            String descripcion = tablaPresupuesto.getValueAt(selectedRow, 1).toString();
            String direccion = tablaPresupuesto.getValueAt(selectedRow, 2).toString();

            String tiempo = tablaPresupuesto.getValueAt(selectedRow, 3).toString();
            String[] tiempoParts = tiempo.split(" ");
            long horas = Long.parseLong(tiempoParts[0]);
            long minutos = Long.parseLong(tiempoParts[2]);
            Duration tiempoEstimado = Duration.ofHours(horas).plusMinutes(minutos);
            Double costo = Double.parseDouble(tablaPresupuesto.getValueAt(selectedRow, 4).toString());
            String nombreCliente = tablaPresupuesto.getValueAt(selectedRow, 5).toString();

            // preguntar al usuario si desea modificar o eliminar el tipo de maquinaria
            String[] options = {"Modificar", "Eliminar"};
            int option = JOptionPane.showOptionDialog(panel, "¿Qué desea hacer con el presupuesto?", "Presupuesto", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            Presupuesto presupuesto;
            switch (option) {
                case 1:
                    presupuestoRepo.eliminar(id);
                    DefaultTableModel presupuestoTableModel = mapearModeloPresupuesto(presupuestoRepo.obtenerTodos());
                    tablaPresupuesto.setModel(presupuestoTableModel);
                    ajustarAnchoColumnas(tablaPresupuesto);
                    cargarGraficos();
                    break;
                case 0:
                    ClienteRepo clienteRepo = new ClienteRepo();
                    Cliente cliente = clienteRepo.obtenerPorNombre(nombreCliente);
                    presupuesto = new Presupuesto(id, descripcion, direccion, tiempoEstimado, costo, cliente);
                    JFrame modificarPresupuestoFrame = new JFrame("Modificar Presupuesto");
                    modificarPresupuestoFrame.setContentPane(new EditarPresupuesto(presupuestoRepo, presupuesto).panel);
                    modificarPresupuestoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    modificarPresupuestoFrame.pack();
                    modificarPresupuestoFrame.setVisible(true);
                    modificarPresupuestoFrame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent windowEvent) {
                            SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                            SwingUtilities.getWindowAncestor(panel).setVisible(true);
                            SwingUtilities.getWindowAncestor(panel).toFront();
                            DefaultTableModel presupuestoTableModel = mapearModeloPresupuesto(presupuestoRepo.obtenerTodos());
                            tablaPresupuesto.setModel(presupuestoTableModel);
                            ajustarAnchoColumnas(tablaPresupuesto);
                            cargarGraficos();
                        }
                    });
                    break;
            }
        }
    }

    public void botonAgregarObraClick(){
        SwingUtilities.getWindowAncestor(panel).setEnabled(false);
        JFrame newJframe = new JFrame("Agregar Obra");
        newJframe.setContentPane(new RegistrarObra(obraRepo).panel);
        newJframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newJframe.pack();
        newJframe.setVisible(true);

        newJframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                // Reactivar el JFrame principal
                SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                SwingUtilities.getWindowAncestor(panel).toFront();
                DefaultTableModel obraTableModel = mapearModeloObra(obraRepo.obtenerObras());
                tablaObras.setModel(obraTableModel);
                ajustarAnchoColumnas(tablaObras);
            }
        });
    }

    public void tablaObrasClick(MouseEvent e){
        if (e.getClickCount() == 2 && tablaObras.getSelectedRow() != -1) {
            int selectedRow = tablaObras.getSelectedRow();
            Long id = Long.parseLong(tablaObras.getValueAt(selectedRow, 0).toString());
            String tipoObraString = tablaObras.getValueAt(selectedRow, 1).toString();
            TipoObra tipoObra = obraRepo.obtenerTipoObraPorNombre(tipoObraString);
            Double area = Double.parseDouble(tablaObras.getValueAt(selectedRow, 2).toString());
            String nombre = tablaObras.getValueAt(selectedRow, 3).toString();
            String descripcion = tablaObras.getValueAt(selectedRow, 4).toString();
            String estado = tablaObras.getValueAt(selectedRow, 5).toString();
            String presupuestoString = tablaObras.getValueAt(selectedRow, 6).toString().split(" -- ")[0];
            Presupuesto presupuesto = presupuestoRepo.obtenerPorDescripcion(presupuestoString);

            // preguntar al usuario si desea modificar o eliminar el tipo de maquinaria
            String[] options = {"Modificar", "Eliminar"};
            int option = JOptionPane.showOptionDialog(panel, "¿Qué desea hacer con la obra?", "Obra", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            Obra obra;
            switch (option) {
                case 1:
                    obraRepo.eliminarObra(id);
                    DefaultTableModel obraTableModel = mapearModeloObra(obraRepo.obtenerObras());
                    tablaObras.setModel(obraTableModel);
                    ajustarAnchoColumnas(tablaObras);
                    cargarGraficos();
                    break;
                case 0:
                    obra = new Obra(id, tipoObra, area, nombre, descripcion, estado, presupuesto);
                    JFrame modificarObraFrame = new JFrame("Modificar Obra");
                    modificarObraFrame.setContentPane(new ModificarRegistroObra(obraRepo, obra).panel);
                    modificarObraFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    modificarObraFrame.pack();
                    modificarObraFrame.setVisible(true);
                    modificarObraFrame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent windowEvent) {
                            SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                            SwingUtilities.getWindowAncestor(panel).setVisible(true);
                            SwingUtilities.getWindowAncestor(panel).toFront();
                            DefaultTableModel obraTableModel = mapearModeloObra(obraRepo.obtenerObras());
                            tablaObras.setModel(obraTableModel);
                            ajustarAnchoColumnas(tablaObras);
                            cargarGraficos();
                        }
                    });
                    break;
            }
        }
    }

    public void setTableInasistenciaModel() throws IllegalAccessException {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel personalTableModel = mapearModeloInasistencia(personalRepo.obtenerInasistenciasModelo());

        // Set the TableModel to the JTable
        tablaInasistencia.setModel(personalTableModel);

        ajustarAnchoColumnas(tablaInasistencia);
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

    public DefaultTableModel mapearModeloInasistencia(List<ModeloInasistencia> modeloInasistenciaList) {
        Field[] fields = ModeloInasistencia.class.getDeclaredFields();

        // Create a Vector to hold the column names
        Vector<String> columnNames = new Vector<>();
        for (Field field : fields) {
            columnNames.add(field.getName());
        }

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (ModeloInasistencia personal : modeloInasistenciaList) {
            Vector<Object> row = new Vector<>();
            row.add(personal.getId());
            row.add(personal.getFecha());
            row.add(personal.getMotivo());
            row.add(personal.getPersonal());
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

    public void setTablePresupuestoModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel presupuestoTableModel = mapearModeloPresupuesto(presupuestoRepo.obtenerTodos());

        // Set the TableModel to the JTable
        tablaPresupuesto.setModel(presupuestoTableModel);

        ajustarAnchoColumnas(tablaPresupuesto);
    }

    public DefaultTableModel mapearModeloPresupuesto(List<Presupuesto> presupuestoList) {
        Field[] fields = Presupuesto.class.getDeclaredFields();

        // Create a Vector to hold the column names
        Vector<String> columnNames = new Vector<>();
        for (Field field : fields) {
            if (!field.getName().equals("creado") && !field.getName().equals("modificado") && !field.getName().equals("eliminado")) {
                columnNames.add(field.getName());
            }
        }

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (Presupuesto presupuesto : presupuestoList) {
            Vector<Object> row = new Vector<>();
            String tiempoEstimado = presupuesto.getTiempoEstimado().toHours() + " horas " + presupuesto.getTiempoEstimado().toMinutesPart() + " minutos";
            row.add(presupuesto.getId());
            row.add(presupuesto.getDescripcion());
            row.add(presupuesto.getDireccion());
            row.add(tiempoEstimado);
            row.add(presupuesto.getCosto());
            row.add(presupuesto.getCliente().getNombre());
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public DefaultTableModel mapearModeloObra(List<Obra> obraList) {
        Field[] fields = Obra.class.getDeclaredFields();

        // Create a Vector to hold the column names
        Vector<String> columnNames = new Vector<>();
        for (Field field : fields) {
            if (!field.getName().equals("creado") && !field.getName().equals("modificado") && !field.getName().equals("eliminado")) {
                columnNames.add(field.getName());
            }
        }

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (Obra obra : obraList) {
            Vector<Object> row = new Vector<>();
            row.add(obra.getId());
            row.add(obra.getTipoObra().getNombre());
            row.add(obra.getArea());
            row.add(obra.getNombre());
            row.add(obra.getDescripcion());
            row.add(obra.getEstado());
            String presupuesto = obra.getPresupuesto().getDescripcion() + " -- " + obra.getPresupuesto().getCosto();
            row.add(presupuesto);
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void setTableObraModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel obraTableModel = mapearModeloObra(obraRepo.obtenerObras());

        // Set the TableModel to the JTable
        tablaObras.setModel(obraTableModel);

        ajustarAnchoColumnas(tablaObras);
    }

    public void cargarGraficos() {
        inicio.removeAll();
        List<ModeloCliente> clientes = clienteRepo.obtenerTodos();
        List<Obra> obras = obraRepo.obtenerObras();
        String[] clientesObras = obras.stream().map(obra -> obra.getPresupuesto().getCliente().getNombre()).toArray(String[]::new);

        Map<String, Long> clienteObrasMap = clientes.stream()
                .collect(Collectors.toMap(ModeloCliente::getNombre, cliente -> 0L));

// Luego, contamos las obras asignadas a cada cliente
        for (String clienteObra : clientesObras) {
            clienteObrasMap.put(clienteObra, clienteObrasMap.getOrDefault(clienteObra, 0L) + 1);
        }

// Convertimos el mapa a los arreglos necesarios
        String[] nombresClientes = clienteObrasMap.keySet().toArray(new String[0]);
        double[] cantidadObras = clienteObrasMap.values().stream().mapToDouble(Long::doubleValue).toArray();
        ChartPanel chartPanel = GeneradorGraficos.generarGraficoBarras("Obras por cliente", "Clientes", "Cantidad de obras", nombresClientes, cantidadObras, 385, 300);
        inicio.add(chartPanel);

        List<Maquinaria> maquinarias = tipoMaquinariaRepo.obtenerMaquinarias();
        String[] tiposMaquinaria = maquinarias.stream().map(maquinaria -> maquinaria.getTipoMaquinaria().getNombre()).toArray(String[]::new);
        double[] cantidadTiposMaquinaria = Arrays.stream(tiposMaquinaria)
                .mapToDouble(tipoMaquinaria -> maquinarias.stream().filter(maquinaria -> maquinaria.getTipoMaquinaria().getNombre().equals(tipoMaquinaria)).count())
                .toArray();
        ChartPanel chartPanelMaquinaria = GeneradorGraficos.generarGraficoPastel("Maquinaria por tipo", tiposMaquinaria, cantidadTiposMaquinaria, 385, 300);
        inicio.add(chartPanelMaquinaria);

        double[] costosClientes = obras.stream()
                .mapToDouble(obra -> obra.getPresupuesto().getCosto())
                .toArray();

// Definir las desviaciones como un porcentaje del costo
        double[] desvios = new double[costosClientes.length];
        for (int i = 0; i < costosClientes.length; i++) {
            desvios[i] = costosClientes[i] * 0.1; // Ejemplo: 10% del costo como desviación
        }

// Generar el gráfico de líneas con desviación
        ChartPanel chartPanelCostos = GeneradorGraficos.generarGraficoDesviacion(
                "Costos de obras por cliente", "Clientes", "Costo", "Costo", costosClientes, desvios, 790, 300);
        inicio.add(chartPanelCostos);
    }
}
