package com.criollo.machmillenium.vistas.admin;

import com.criollo.machmillenium.entidades.*;
import com.criollo.machmillenium.modelos.ModeloCliente;
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
import com.criollo.machmillenium.vistas.emergentes.presupuesto.Calculadora;
import com.criollo.machmillenium.vistas.emergentes.presupuesto.CrearPresupuesto;
import com.criollo.machmillenium.vistas.emergentes.presupuesto.EditarPresupuesto;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Administrador {
    public JPanel panel;
    private JTable tablaClientes;
    private JButton btnAgregarCliente;
    private JTable tablaPersonal;
    private JButton agregarButton;
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
    private JButton verGraficoButton;
    private JButton imprimirPersonalButton;
    private JButton imprimirTablaButton;
    private JButton verGraficasMateriales;
    private JButton verGraficasObras;
    private JButton imprimirTablaButton2;
    private JButton verGraficasMaquinarias;
    private JButton imprimirMaquinarias;
    private JTable auditoria;
    private JComboBox<String> personalCombo;
    private JFormattedTextField campoRealizado;
    private JTextField campoAccion;
    private JTextField campoDetalle;
    private JButton limpiarButton;
    private JButton buscarButton;
    private JButton calcularButton;
    private final PersonalRepo personalRepo;
    private final ClienteRepo clienteRepo;
    private final TipoMaquinariaRepo tipoMaquinariaRepo;
    private final TipoInsumoRepo tipoInsumoRepo;
    private final PresupuestoRepo presupuestoRepo;
    private final ObraRepo obraRepo;
    private final AuditoriaRepo auditoriaRepo;

    public Administrador(Personal personal) {
        this.personalRepo = new PersonalRepo();
        this.clienteRepo = new ClienteRepo();
        this.tipoMaquinariaRepo = new TipoMaquinariaRepo();
        this.tipoInsumoRepo = new TipoInsumoRepo();
        this.presupuestoRepo = new PresupuestoRepo();
        this.obraRepo = new ObraRepo();
        this.auditoriaRepo = new AuditoriaRepo(personal.getNombre());
        this.auditoriaRepo.registrar("Ingreso", "Ingreso al módulo de administrador");
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
            auditoriaRepo.registrar("Agregar", "Maquinaria");
            setTableAuditoriaModel();
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
                    String tipoMaquinaria = tablaMaquinarias.getValueAt(selectedRow, 1).toString();
                    String nombre = tablaMaquinarias.getValueAt(selectedRow, 2).toString();
                    long horas = Long.parseLong(tablaMaquinarias.getValueAt(selectedRow, 3).toString().split(" ")[0]);
                    long minutos = Long.parseLong(tablaMaquinarias.getValueAt(selectedRow, 3).toString().split(" ")[3]);
                    Duration tiempoEstimadoDeUso = Duration.ofHours(horas).plusMinutes(minutos);
                    Double costoPorTiempoDeUso = Double.parseDouble(tablaMaquinarias.getValueAt(selectedRow, 4).toString());
                    Double costoTotal = Double.parseDouble(tablaMaquinarias.getValueAt(selectedRow, 5).toString());

                    // preguntar al usuario si desea modificar o eliminar el tipo de maquinaria
                    String[] options = {"Modificar", "Eliminar"};
                    int option = JOptionPane.showOptionDialog(panel, "¿Qué desea hacer con la maquinaria?", "Maquinaria", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    ModeloMaquinaria maquinaria;
                    switch (option) {
                        case 1:
                            auditoriaRepo.registrar("Eliminar", "Maquinaria con nombre " + nombre);
                            setTableAuditoriaModel();
                            tipoMaquinariaRepo.eliminarMaquinaria(id);
                            break;
                        case 0:
                            auditoriaRepo.registrar("Modificar", "Maquinaria con nombre " + nombre);
                            setTableAuditoriaModel();
                            TipoMaquinaria entidadTipoMaquinaria = tipoMaquinariaRepo.obtenerPorNombre(tipoMaquinaria);
                            maquinaria = new ModeloMaquinaria(id, entidadTipoMaquinaria.getId(), nombre, tiempoEstimadoDeUso, costoPorTiempoDeUso, costoTotal, tipoMaquinaria);
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
        botonAgregarMaterial.addActionListener(e -> botonAgregarMaterialClick());
        tablaMateriales.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaMaterialClick(e);
            }
        });
        botonAgregarPresupuesto.addActionListener(e -> {
            auditoriaRepo.registrar("Agregar", "Presupuesto");
            setTableAuditoriaModel();
            SwingUtilities.getWindowAncestor(panel).setEnabled(false);
            JFrame newJframe = new JFrame("Agregar Presupuesto");
            newJframe.setContentPane(new CrearPresupuesto(presupuestoRepo).panel);
            newJframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newJframe.pack();
            newJframe.setVisible(true);

            newJframe.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
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
        imprimirPersonalButton.addActionListener(e -> GeneradorReportes.generarReportePersonal());
        verGraficosClientesButton.addActionListener(e -> {
            auditoriaRepo.registrar("Ver", "Gráficos de clientes");
            setTableAuditoriaModel();
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
        verGraficoButton.addActionListener(e -> {
            auditoriaRepo.registrar("Ver", "Gráficos del personal");
            setTableAuditoriaModel();
            List<ModeloPersonal> personal2 = personalRepo.obtenerTodos();
            String[] especialidades = personal2.stream().map(ModeloPersonal::getEspecialidad).toArray(String[]::new);
            double[] cantidadEspecialidades = personal2.stream().collect(Collectors.groupingBy(ModeloPersonal::getEspecialidad, Collectors.counting()))
                    .values().stream().mapToDouble(Long::doubleValue).toArray();
            // eliminar los duplicados
            especialidades = Arrays.stream(especialidades).distinct().toArray(String[]::new);
            ChartPanel chartPanel = GeneradorGraficos.generarGraficoPastel("Personal por especialidad", especialidades, cantidadEspecialidades, 380, 250);

            String[] roles = personal2.stream().map(ModeloPersonal::getRol).toArray(String[]::new);
            double[] cantidadRoles = personal2.stream().collect(Collectors.groupingBy(ModeloPersonal::getRol, Collectors.counting()))
                    .values().stream().mapToDouble(Long::doubleValue).toArray();
            roles = Arrays.stream(roles).distinct().toArray(String[]::new);
            ChartPanel chartPanelRoles = GeneradorGraficos.generarGraficoPastel("Personal por rol", roles, cantidadRoles, 380, 250);

            String[] fijos = personal2.stream().map(ModeloPersonal::getFijo).toArray(String[]::new);
            double[] cantidadFijos = personal2.stream().collect(Collectors.groupingBy(ModeloPersonal::getFijo, Collectors.counting()))
                    .values().stream().mapToDouble(Long::doubleValue).toArray();
            fijos = Arrays.stream(fijos).distinct().toArray(String[]::new);
            ChartPanel chartPanelFijos = GeneradorGraficos.generarGraficoPastel("Personal fijo", fijos, cantidadFijos, 380, 250);

            Graficos graficosDialog = new Graficos("Gráficos", List.of(chartPanel, chartPanelRoles, chartPanelFijos));
            graficosDialog.pack();
            graficosDialog.setVisible(true);
        });
        verGraficasMaquinarias.addActionListener(e -> {
            auditoriaRepo.registrar("Ver", "Gráficos de maquinarias");
            setTableAuditoriaModel();
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
        verGraficasMateriales.addActionListener(e -> {
            auditoriaRepo.registrar("Ver", "Gráficos de materiales");
            setTableAuditoriaModel();
            List<Material> materiales = tipoInsumoRepo.obtenerMateriales();
            String[] tiposInsumo = materiales.stream().map(material -> material.getTipoInsumo().getNombre()).toArray(String[]::new);
            double[] cantidadMateriales = materiales.stream().collect(Collectors.groupingBy(material -> material.getTipoInsumo().getNombre(), Collectors.counting()))
                    .values().stream().mapToDouble(Long::doubleValue).toArray();
            tiposInsumo = Arrays.stream(tiposInsumo).distinct().toArray(String[]::new);
            ChartPanel chartPanel = GeneradorGraficos.generarGraficoPastel("Materiales por tipo", tiposInsumo, cantidadMateriales, 380, 250);

            Graficos graficosDialog = new Graficos("Gráficos", List.of(chartPanel));
            graficosDialog.pack();
            graficosDialog.setVisible(true);
        });
        verGraficasObras.addActionListener(e -> {
            auditoriaRepo.registrar("Ver", "Gráficos de obras");
            setTableAuditoriaModel();
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

            String[] nombresObras = obras.stream().map(Obra::getNombre).toArray(String[]::new);
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
        });
        imprimirMaquinarias.addActionListener(e -> GeneradorReportes.generarReporteMaquinaria());
        imprimirTablaButton.addActionListener(e -> GeneradorReportes.generarReporteMateriales());
        imprimirTablaButton2.addActionListener(e -> GeneradorReportes.generarReporteObras());
        limpiarButton.addActionListener(e -> {
            auditoriaRepo.registrar("Limpiar", "Auditoría");
            setTableAuditoriaModel();
            personalCombo.setSelectedIndex(0);
            campoRealizado.setText("");
            campoAccion.setText("");
            campoDetalle.setText("");
            DefaultTableModel auditoriaTableModel = mapearModeloAuditoria(auditoriaRepo.obtenerAuditorias());
            auditoria.setModel(auditoriaTableModel);
            ajustarAnchoColumnas(auditoria);
        });
        buscarButton.addActionListener(e -> {
            auditoriaRepo.registrar("Buscar", "Auditoría");
            String personalSeleccionado = Objects.requireNonNull(personalCombo.getSelectedItem()).toString();
            LocalDate realizado = null;
            if (!campoRealizado.getText().equals("__/__/____")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                realizado = LocalDate.parse(campoRealizado.getText(), formatter);
            }
            String accion = campoAccion.getText();
            String detalle = campoDetalle.getText();
            List<Auditoria> auditorias = auditoriaRepo.obtenerAuditoriasPorFiltros(personalSeleccionado, realizado, accion, detalle);
            DefaultTableModel auditoriaTableModel = mapearModeloAuditoria(auditorias);
            auditoria.setModel(auditoriaTableModel);
            ajustarAnchoColumnas(auditoria);
        });
        calcularButton.addActionListener(e -> {
            Calculadora calculadora = new Calculadora();
            calculadora.pack();
            calculadora.setVisible(true);
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
            setTablePresupuestoModel();
            setTableObraModel();
            setTableAuditoriaModel();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void btnAgregarClienteClick() {
        auditoriaRepo.registrar("Agregar", "Cliente");
        setTableAuditoriaModel();
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
            auditoriaRepo.registrar("Modificar", "Cliente con nombre " + nombre);
            setTableAuditoriaModel();

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
                    cargarGraficos();
                }
            });
        }
    }

    public void tablaPersonalClick(MouseEvent e){
        if (e.getClickCount() == 2 && tablaPersonal.getSelectedRow() != -1) {
            int selectedRow = tablaPersonal.getSelectedRow();

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

            auditoriaRepo.registrar("Modificar", "Personal con nombre " + nombre);
            setTableAuditoriaModel();
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
                    cargarGraficos();
                }
            });
        }
    }

    public void agregarButtonActionPerformed() {
        auditoriaRepo.registrar("Agregar", "Personal");
        setTableAuditoriaModel();
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
                cargarGraficos();
            }
        });
    }

    public void botonAgregarActionPerformed(){
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del tipo de maquinaria");
        if (nombre == null || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "El nombre del tipo de maquinaria no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        auditoriaRepo.registrar("Agregar", "Tipo de maquinaria");
        setTableAuditoriaModel();

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
                    auditoriaRepo.registrar("Eliminar", "Tipo de maquinaria con nombre " + nombre);
                    setTableAuditoriaModel();
                    tipoMaquinaria = new TipoMaquinaria(nombre);
                    tipoMaquinaria.setId(id);
                    tipoMaquinaria.setEliminado(LocalDateTime.now());
                    tipoMaquinariaRepo.actualizar(tipoMaquinaria);
                    break;
                case 0:
                    String nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del tipo de maquinaria", nombre);
                    auditoriaRepo.registrar("Modificar", "Tipo de maquinaria con nombre " + nombre  + " a " + nuevoNombre);
                    setTableAuditoriaModel();
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
        auditoriaRepo.registrar("Agregar", "Tipo de insumo");
        setTableAuditoriaModel();

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
                    auditoriaRepo.registrar("Eliminar", "Tipo de insumo con nombre " + nombre);
                    setTableAuditoriaModel();
                    tipoInsumoRepo.eliminar(id);
                    break;
                case 0:
                    String nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del tipo de insumo", nombre);
                    if (nuevoNombre == null || nuevoNombre.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "El nombre del tipo de insumo no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    auditoriaRepo.registrar("Modificar", "Tipo de insumo con nombre " + nombre  + " a " + nuevoNombre);
                    setTableAuditoriaModel();
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
        auditoriaRepo.registrar("Agregar", "Material");
        setTableAuditoriaModel();
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
                    auditoriaRepo.registrar("Eliminar", "Material con nombre " + nombre);
                    setTableAuditoriaModel();
                    tipoInsumoRepo.eliminarMaterial(id);
                    DefaultTableModel materialTableModel = mapearModeloMaterial(tipoInsumoRepo.obtenerMateriales());
                    tablaMateriales.setModel(materialTableModel);
                    ajustarAnchoColumnas(tablaMateriales);
                    break;
                case 0:
                    auditoriaRepo.registrar("Modificar", "Material con nombre " + nombre);
                    setTableAuditoriaModel();
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
                    auditoriaRepo.registrar("Eliminar", "Presupuesto con descripción " + descripcion);
                    setTableAuditoriaModel();
                    presupuestoRepo.eliminar(id);
                    DefaultTableModel presupuestoTableModel = mapearModeloPresupuesto(presupuestoRepo.obtenerTodos());
                    tablaPresupuesto.setModel(presupuestoTableModel);
                    ajustarAnchoColumnas(tablaPresupuesto);
                    cargarGraficos();
                    break;
                case 0:
                    auditoriaRepo.registrar("Modificar", "Presupuesto con descripción " + descripcion);
                    setTableAuditoriaModel();
                    ClienteRepo clienteRepo = new ClienteRepo();
                    Cliente cliente = clienteRepo.obtenerPorNombre(nombreCliente);
                    presupuesto = new Presupuesto(id, descripcion, direccion, tiempoEstimado, costo, cliente);
                    JFrame modificarPresupuestoFrame = new JFrame("Modificar Presupuesto");
                    modificarPresupuestoFrame.setContentPane(new EditarPresupuesto(presupuestoRepo, presupuesto).panel);
                    modificarPresupuestoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    modificarPresupuestoFrame.pack();
                    modificarPresupuestoFrame.setVisible(true);
                    modificarPresupuestoFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
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
        auditoriaRepo.registrar("Agregar", "Obra");
        setTableAuditoriaModel();
        SwingUtilities.getWindowAncestor(panel).setEnabled(false);
        JFrame newJframe = new JFrame("Agregar Obra");
        newJframe.setContentPane(new RegistrarObra(obraRepo).panel);
        newJframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newJframe.pack();
        newJframe.setVisible(true);

        newJframe.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
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
                    auditoriaRepo.registrar("Eliminar", "Obra con nombre " + nombre);
                    setTableAuditoriaModel();
                    obraRepo.eliminarObra(id);
                    DefaultTableModel obraTableModel = mapearModeloObra(obraRepo.obtenerObras());
                    tablaObras.setModel(obraTableModel);
                    ajustarAnchoColumnas(tablaObras);
                    cargarGraficos();
                    break;
                case 0:
                    auditoriaRepo.registrar("Modificar", "Obra con nombre " + nombre);
                    setTableAuditoriaModel();
                    obra = new Obra(id, tipoObra, area, nombre, descripcion, estado, presupuesto);
                    JFrame modificarObraFrame = new JFrame("Modificar Obra");
                    modificarObraFrame.setContentPane(new ModificarRegistroObra(obraRepo, obra).panel);
                    modificarObraFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    modificarObraFrame.pack();
                    modificarObraFrame.setVisible(true);
                    modificarObraFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
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
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Nombre", "C.I", "Teléfono", "Correo electrónico", "Dirección", "Edad", "Sexo"));

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (ModeloCliente cliente : modeloClienteList) {
            Vector<Object> row = new Vector<>();
            row.add(cliente.getId());
            row.add(cliente.getNombre());
            row.add(cliente.getCedula());
            row.add(cliente.getTelefono());
            row.add(cliente.getCorreo());
            row.add(cliente.getDireccion());
            row.add(cliente.getEdad());
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
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Nombre", "C.I", "Correo electrónico", "Rol", "Fecha Fin Contrato", "Especialidad", "¿Es Fijo?", "¿Está Activo?"));

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (ModeloPersonal personal : modeloPersonalList) {
            Vector<Object> row = new Vector<>();
            row.add(personal.getId());
            row.add(personal.getNombre());
            row.add(personal.getCedula());
            row.add(personal.getCorreo());
            row.add(personal.getRol());
            row.add(personal.getFechaFinContrato());
            row.add(personal.getEspecialidad());
            row.add(personal.getFijo());
            row.add(personal.getActivo());
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
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Nombre", "Creado", "Modificado"));

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (TipoMaquinaria tipoMaquinaria : tipoMaquinariaList) {
            Vector<Object> row = new Vector<>();
            row.add(tipoMaquinaria.getId());
            row.add(tipoMaquinaria.getNombre());
            row.add(tipoMaquinaria.getCreado());
            row.add(tipoMaquinaria.getModificado());
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
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Tipo Maquinaria", "Nombre", "Tiempo Estimado de Uso", "Costo por Tiempo de Uso", "Costo Total"));

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (ModeloMaquinaria maquinaria : modeloMaquinariaList) {
            Vector<Object> row = new Vector<>();
            row.add(maquinaria.getId());
            row.add(maquinaria.getTipoMaquinaria());
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
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Nombre", "Creado", "Modificado"));

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (TipoInsumo tipoInsumo : tipoInsumoList) {
            Vector<Object> row = new Vector<>();
            row.add(tipoInsumo.getId());
            row.add(tipoInsumo.getNombre());
            row.add(tipoInsumo.getCreado());
            row.add(tipoInsumo.getModificado());
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
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Tipo Insumo", "Nombre", "Cantidad", "Costo"));

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
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Descripción", "Dirección", "Tiempo Estimado", "Costo", "Cliente"));

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
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Tipo Obra", "Área", "Nombre", "Descripción", "Estado", "Presupuesto"));

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

    public DefaultTableModel mapearModeloAuditoria(List<Auditoria> auditoriaList) {
        Vector<String> columnNames = new Vector<>(Arrays.asList("Personal", "Realizado el", "Acción", "Detalle"));

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (Auditoria auditoria : auditoriaList) {
            Vector<Object> row = new Vector<>();
            row.add(auditoria.getPersonal());
            row.add(auditoria.getRealizado().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            row.add(auditoria.getAccion());
            row.add(auditoria.getDetalle());
            data.add(row);
        }

        personalCombo.setModel(new DefaultComboBoxModel<>(personalRepo.obtenerTodos().stream().map(ModeloPersonal::getNombre).toArray(String[]::new)));

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void setTableAuditoriaModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel auditoriaTableModel = mapearModeloAuditoria(auditoriaRepo.obtenerAuditorias());

        // Set the TableModel to the JTable
        auditoria.setModel(auditoriaTableModel);

        ajustarAnchoColumnas(auditoria);
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

    private void createUIComponents() {
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            campoRealizado = new JFormattedTextField(dateMask);
        } catch (ParseException e) {
            campoRealizado = new JFormattedTextField();
            e.printStackTrace();
        }
        campoRealizado.setColumns(10);
    }
}
