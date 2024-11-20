package com.criollo.machmillenium.vistas.gestor;

import com.criollo.machmillenium.entidades.*;
import com.criollo.machmillenium.modelos.*;
import com.criollo.machmillenium.repos.*;
import com.criollo.machmillenium.utilidades.GeneradorGraficos;
import com.criollo.machmillenium.utilidades.GeneradorReportes;
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
import com.criollo.machmillenium.vistas.emergentes.presupuesto.Calculadora;
import com.criollo.machmillenium.vistas.emergentes.presupuesto.CrearPresupuesto;
import com.criollo.machmillenium.vistas.emergentes.presupuesto.EditarPresupuesto;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GestorProyectos {
    public JPanel panel;
    private JTable tablaClientes;
    private JButton btnAgregarCliente;
    private JTextField txtFiltroNombre;
    private JTextField txtFiltroCedula;
    private JTextField txtFiltroCorreo;
    private JComboBox<String> comboFiltroSexo;
    private JButton btnFiltrarClientes;
    private JButton btnLimpiarFiltros;
    private JTable tablaObras;
    private JButton botonAgregarObra;
    private JTable tablaMaquinarias;
    private JButton botonAgregarMaquinaria;
    private JTextField txtFiltroNombreMaquinaria;
    private JTextField txtFiltroCostoMin;
    private JTextField txtFiltroCostoMax;
    private JComboBox<String> comboFiltroTipoMaquinaria;
    private JButton btnFiltrarMaquinarias;
    private JButton btnLimpiarFiltrosMaquinarias;
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
    private JButton imprimirTablaButton;
    private JButton verGraficasMateriales;
    private JButton verGraficasObras;
    private JButton verGraficasMaquinarias;
    private JButton imprimirMaquinarias;
    private JButton calcularButton;
    private final PersonalRepo personalRepo;
    private final ClienteRepo clienteRepo;
    private final TipoMaquinariaRepo tipoMaquinariaRepo;
    private final TipoInsumoRepo tipoInsumoRepo;
    private final PresupuestoRepo presupuestoRepo;
    private final ObraRepo obraRepo;
    private final AuditoriaRepo auditoriaRepo;
    private JTextField txtFiltroNombreMaterial;
    private JTextField txtFiltroCostoMinMaterial;
    private JTextField txtFiltroCostoMaxMaterial;
    private JComboBox<String> comboFiltroTipoInsumo;
    private JButton btnFiltrarMateriales;
    private JButton btnLimpiarFiltrosMateriales;
    private JTextField txtFiltroNombreObra;
    private JComboBox<String> comboFiltroEstadoObra;
    private JTextField txtFiltroClienteObra;
    private JComboBox<String> comboFiltroTipoObra;
    private JTextField txtFiltroPresupuestoMinObra;
    private JTextField txtFiltroPresupuestoMaxObra;
    private JButton btnFiltrarObras;
    private JButton btnLimpiarFiltrosObras;
    private JButton btnReporteObrasFiltradas;
    private JTable tablaInasistencia;
    private JButton registrarInasistencia;
    private JButton buttonGenerar;

    public GestorProyectos(Personal personal) {
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
        botonAgregar.addActionListener(e -> botonAgregarActionPerformed());
        tablaTipoMaquinaria.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaTipoMaquinariaClick(e);
            }
        });
        botonAgregarMaquinaria.addActionListener(e -> {
            auditoriaRepo.registrar("Agregar", "Maquinaria");
            
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
                }
            });
        });
        buttonGenerar.addActionListener(e -> {
            auditoriaRepo.registrar("Generar solicitud de compra", "Ingreso al formulario de generación de solicitud de compra");
            Utilidades.generarSolicitudCompra(personal, panel);
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
                            
                            tipoMaquinariaRepo.eliminarMaquinaria(id);
                            break;
                        case 0:
                            auditoriaRepo.registrar("Modificar", "Maquinaria con nombre " + nombre);
                            
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
        btnFiltrarObras.addActionListener(e -> filtrarObras());
        btnLimpiarFiltrosObras.addActionListener(e -> limpiarFiltrosObras());
        recuperarClaveButton.addActionListener(e -> Utilidades.cambiarClaveOriginal(personal.getClave(), personal.getId(), false));
        verGraficosClientesButton.addActionListener(e -> {
            verGraficosClientes();
        });
        verGraficasMaquinarias.addActionListener(e -> {
            auditoriaRepo.registrar("Ver", "Gráficos de maquinarias");
            
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
        imprimirMaquinarias.addActionListener(e -> {
            String nombre = txtFiltroNombreMaquinaria.getText().trim();
            Double costoMin = txtFiltroCostoMin.getText().isEmpty() ? null : Double.parseDouble(txtFiltroCostoMin.getText());
            Double costoMax = txtFiltroCostoMax.getText().isEmpty() ? null : Double.parseDouble(txtFiltroCostoMax.getText());
            String tipoMaquinaria = (String) comboFiltroTipoMaquinaria.getSelectedItem();

            List<ModeloMaquinaria> maquinariasFiltradas = tipoMaquinariaRepo.obtenerMaquinariasFiltradas(
                    nombre, costoMin, costoMax, tipoMaquinaria
            );

            GeneradorReportes.generarReporteMaquinarias(maquinariasFiltradas);
        });
        calcularButton.addActionListener(e -> {
            Calculadora calculadora = new Calculadora();
            calculadora.pack();
            calculadora.setVisible(true);
        });

        btnFiltrarClientes.addActionListener(e -> {
            String nombre = txtFiltroNombre.getText().trim();
            String cedula = txtFiltroCedula.getText().trim();
            String correo = txtFiltroCorreo.getText().trim();
            String sexo = (String) comboFiltroSexo.getSelectedItem();

            List<ModeloCliente> clientesFiltrados = clienteRepo.obtenerClientesFiltrados(nombre, cedula, correo, sexo);
            DefaultTableModel clienteTableModel = mapearModeloCliente(clientesFiltrados);
            tablaClientes.setModel(clienteTableModel);
        });

        btnLimpiarFiltros.addActionListener(e -> {
            txtFiltroNombre.setText("");
            txtFiltroCedula.setText("");
            txtFiltroCorreo.setText("");
            comboFiltroSexo.setSelectedIndex(0);

            DefaultTableModel clienteTableModel = mapearModeloCliente(clienteRepo.obtenerTodos());
            tablaClientes.setModel(clienteTableModel);
        });

        imprimirClientesButton.addActionListener(e -> {
            String sexo = (String) comboFiltroSexo.getSelectedItem();

            List<ModeloCliente> clientesFiltrados = clienteRepo.obtenerClientesFiltrados(
                    txtFiltroNombre.getText().trim(),
                    txtFiltroCedula.getText().trim(),
                    txtFiltroCorreo.getText().trim(),
                    sexo
            );

            GeneradorReportes.generarReporteClientes(clientesFiltrados);
        });

        btnFiltrarMaquinarias.addActionListener(e -> {
            String nombre = txtFiltroNombreMaquinaria.getText().trim();
            Double costoMin = txtFiltroCostoMin.getText().isEmpty() ? null : Double.parseDouble(txtFiltroCostoMin.getText());
            Double costoMax = txtFiltroCostoMax.getText().isEmpty() ? null : Double.parseDouble(txtFiltroCostoMax.getText());
            String tipoMaquinaria = (String) comboFiltroTipoMaquinaria.getSelectedItem();

            List<ModeloMaquinaria> maquinariasFiltradas = tipoMaquinariaRepo.obtenerMaquinariasFiltradas(nombre, costoMin, costoMax, tipoMaquinaria);
            DefaultTableModel maquinariaTableModel = mapearModeloMaquinaria(maquinariasFiltradas);
            tablaMaquinarias.setModel(maquinariaTableModel);
        });

        btnLimpiarFiltrosMaquinarias.addActionListener(e -> {
            txtFiltroNombreMaquinaria.setText("");
            txtFiltroCostoMin.setText("");
            txtFiltroCostoMax.setText("");
            comboFiltroTipoMaquinaria.setSelectedIndex(0);

            DefaultTableModel maquinariaTableModel = mapearModeloMaquinaria(tipoMaquinariaRepo.obtenerTodosMaquinaria());
            tablaMaquinarias.setModel(maquinariaTableModel);
        });

        btnFiltrarMateriales.addActionListener(e -> {
            String nombre = txtFiltroNombreMaterial.getText().trim();
            Double costoMin = txtFiltroCostoMinMaterial.getText().isEmpty() ? null : Double.parseDouble(txtFiltroCostoMinMaterial.getText());
            Double costoMax = txtFiltroCostoMaxMaterial.getText().isEmpty() ? null : Double.parseDouble(txtFiltroCostoMaxMaterial.getText());
            String tipoInsumo = (String) comboFiltroTipoInsumo.getSelectedItem();

            List<Material> materialesFiltrados = tipoInsumoRepo.obtenerMaterialesFiltrados(
                    nombre, costoMin, costoMax, tipoInsumo
            );

            DefaultTableModel materialTableModel = mapearModeloMaterial(materialesFiltrados);
            tablaMateriales.setModel(materialTableModel);
        });

        btnLimpiarFiltrosMateriales.addActionListener(e -> {
            txtFiltroNombreMaterial.setText("");
            txtFiltroCostoMinMaterial.setText("");
            txtFiltroCostoMaxMaterial.setText("");
            comboFiltroTipoInsumo.setSelectedItem("Todos");

            DefaultTableModel materialTableModel = mapearModeloMaterial(tipoInsumoRepo.obtenerMateriales());
            tablaMateriales.setModel(materialTableModel);
        });
        btnReporteObrasFiltradas.addActionListener(e -> {
            DefaultTableModel modelo = (DefaultTableModel) tablaObras.getModel();
            List<ModeloObra> obras = new ArrayList<>();

            for (int i = 0; i < modelo.getRowCount(); i++) {
                ModeloObra obra = new ModeloObra();
                obra.setId(Long.parseLong(modelo.getValueAt(i, 0).toString()));
                obra.setNombre(modelo.getValueAt(i, 1).toString());
                obra.setDescripcion(modelo.getValueAt(i, 2).toString());
                obra.setTipoObraNombre(modelo.getValueAt(i, 3).toString());
                obra.setEstado(modelo.getValueAt(i, 4).toString());
                obra.setClienteNombre(modelo.getValueAt(i, 5).toString());
                obra.setPresupuestoTotal(Double.parseDouble(modelo.getValueAt(i, 6).toString()));
                obras.add(obra);
            }

            GeneradorReportes.generarReporteObras(obras);
        });
        imprimirTablaButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) tablaMateriales.getModel();
            List<ModeloMaterial> materiales = new ArrayList<>();

            for (int i = 0; i < model.getRowCount(); i++) {
                ModeloMaterial material = new ModeloMaterial();
                material.setId(Long.parseLong(model.getValueAt(i, 0).toString()));
                material.setNombre(model.getValueAt(i, 1).toString());
                material.setCantidad(Long.parseLong(model.getValueAt(i, 2).toString()));
                material.setCosto(Double.parseDouble(model.getValueAt(i, 3).toString()));
                material.setTipoInsumoNombre(model.getValueAt(i, 4).toString());
                materiales.add(material);
            }

            GeneradorReportes.generarReporteMateriales(materiales);
        });
        tablaInasistencia.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaInasistenciaClick(e);
            }
        });
        registrarInasistencia.addActionListener(e -> {
            auditoriaRepo.registrar("Registrar inasistencia", "El usuario " + personal.getNombre() + " ha registrado una inasistencia");
            JDialog dialog = new JDialog((JFrame) null, "Registrar Inasistencia", true);
            dialog.setContentPane(new RegistrarInasistencia(personalRepo).contentPane);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            DefaultTableModel inasistenciaTableModel = mapearModeloInasistencia(personalRepo.obtenerInasistenciasModelo());
            tablaInasistencia.setModel(inasistenciaTableModel);
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

    public void setTableInasistenciaModel() throws IllegalAccessException {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel personalTableModel = mapearModeloInasistencia(personalRepo.obtenerInasistenciasModelo());

        // Set the TableModel to the JTable
        tablaInasistencia.setModel(personalTableModel);
    }

    public DefaultTableModel mapearModeloInasistencia(List<ModeloInasistencia> modeloInasistenciaList) {
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Fecha", "Motivo", "Personal"));

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

    public void tablaInasistenciaClick(MouseEvent e){
        if (e.getClickCount() == 2 && tablaInasistencia.getSelectedRow() != -1) {
            int selectedRow = tablaInasistencia.getSelectedRow();
            auditoriaRepo.registrar("Modificar inasistencia", "El usuario ha modificado la inasistencia de un empleado con id " + tablaInasistencia.getValueAt(selectedRow, 0).toString());

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
            cargarGraficos();
        }
    }

    public void btnAgregarClienteClick() {
        auditoriaRepo.registrar("Agregar", "Cliente");
        
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
            String correo = tablaClientes.getValueAt(selectedRow, 4).toString();
            String direccion = tablaClientes.getValueAt(selectedRow, 5).toString();
            Integer edad = Integer.parseInt(tablaClientes.getValueAt(selectedRow, 6).toString());
            String sexo = tablaClientes.getValueAt(selectedRow, 7).toString();

            // Crear el ModeloCliente correspondiente
            ModeloCliente clienteSeleccionado = new ModeloCliente(id, nombre, cedula, telefono, direccion, edad, correo, sexo);
            auditoriaRepo.registrar("Modificar", "Cliente con nombre " + nombre);
            

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

                    cargarGraficos();
                }
            });
        }
    }

    public void botonAgregarActionPerformed(){
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del tipo de maquinaria");
        if (nombre == null || nombre.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "El nombre del tipo de maquinaria no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        auditoriaRepo.registrar("Agregar", "Tipo de maquinaria");
        

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
                    
                    tipoMaquinaria = new TipoMaquinaria(nombre);
                    tipoMaquinaria.setId(id);
                    tipoMaquinaria.setEliminado(LocalDateTime.now());
                    tipoMaquinariaRepo.actualizar(tipoMaquinaria);
                    break;
                case 0:
                    String nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del tipo de maquinaria", nombre);
                    auditoriaRepo.registrar("Modificar", "Tipo de maquinaria con nombre " + nombre  + " a " + nuevoNombre);
                    
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
        

        TipoInsumo tipoInsumo = new TipoInsumo(nombre);
        tipoInsumoRepo.insertar(tipoInsumo);
        DefaultTableModel tipoInsumoTableModel = mapearModeloTipoInsumo(tipoInsumoRepo.obtenerTodos());
        tablaTipoInsumos.setModel(tipoInsumoTableModel);
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
                    
                    tipoInsumoRepo.eliminar(id);
                    break;
                case 0:
                    String nuevoNombre = JOptionPane.showInputDialog("Ingrese el nuevo nombre del tipo de insumo", nombre);
                    if (nuevoNombre == null || nuevoNombre.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "El nombre del tipo de insumo no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    auditoriaRepo.registrar("Modificar", "Tipo de insumo con nombre " + nombre  + " a " + nuevoNombre);
                    
                    tipoInsumo = new TipoInsumo(nuevoNombre);
                    tipoInsumo.setId(id);
                    tipoInsumoRepo.actualizar(tipoInsumo);
                    break;
            }

            DefaultTableModel tipoInsumoTableModel = mapearModeloTipoInsumo(tipoInsumoRepo.obtenerTodos());
            tablaTipoInsumos.setModel(tipoInsumoTableModel);
        }
    }

    public void botonAgregarMaterialClick(){
        auditoriaRepo.registrar("Agregar", "Material");
        
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

            }
        });
    }

    public void tablaMaterialClick(MouseEvent e){
        if (e.getClickCount() == 2 && tablaMateriales.getSelectedRow() != -1) {
            int selectedRow = tablaMateriales.getSelectedRow();
            Long id = Long.parseLong(tablaMateriales.getValueAt(selectedRow, 0).toString());
            String nombre = tablaMateriales.getValueAt(selectedRow, 1).toString();
            Long cantidad = Long.parseLong(tablaMateriales.getValueAt(selectedRow, 2).toString());
            Double costo = Double.parseDouble(tablaMateriales.getValueAt(selectedRow, 3).toString());
            String tipoInsumo = tablaMateriales.getValueAt(selectedRow, 4).toString();

            // preguntar al usuario si desea modificar o eliminar el tipo de maquinaria
            String[] options = {"Modificar", "Eliminar"};
            int option = JOptionPane.showOptionDialog(panel, "¿Qué desea hacer con el material?", "Material", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            Material material;
            switch (option) {
                case 1:
                    auditoriaRepo.registrar("Eliminar", "Material con nombre " + nombre);
                    
                    tipoInsumoRepo.eliminarMaterial(id);
                    DefaultTableModel materialTableModel = mapearModeloMaterial(tipoInsumoRepo.obtenerMateriales());
                    tablaMateriales.setModel(materialTableModel);

                    break;
                case 0:
                    auditoriaRepo.registrar("Modificar", "Material con nombre " + nombre);
                    
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
                    
                    presupuestoRepo.eliminar(id);
                    DefaultTableModel presupuestoTableModel = mapearModeloPresupuesto(presupuestoRepo.obtenerTodos());
                    tablaPresupuesto.setModel(presupuestoTableModel);

                    cargarGraficos();
                    break;
                case 0:
                    auditoriaRepo.registrar("Modificar", "Presupuesto con descripción " + descripcion);
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

                            cargarGraficos();
                        }
                    });
                    break;
            }
        }
    }

    public void botonAgregarObraClick(){
        auditoriaRepo.registrar("Agregar", "Obra");
        
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

            }
        });
    }

    public void tablaObrasClick(MouseEvent e){
        if (e.getClickCount() == 2 && tablaObras.getSelectedRow() != -1) {
            int selectedRow = tablaObras.getSelectedRow();
            Long id = Long.parseLong(tablaObras.getValueAt(selectedRow, 0).toString());
            String nombre = tablaObras.getValueAt(selectedRow, 1).toString();
            String descripcion = tablaObras.getValueAt(selectedRow, 2).toString();
            String tipoObraString = tablaObras.getValueAt(selectedRow, 3).toString();
            TipoObra tipoObra = obraRepo.obtenerTipoObraPorNombre(tipoObraString);
            String estado = tablaObras.getValueAt(selectedRow, 4).toString();
            String nombreCliente = tablaObras.getValueAt(selectedRow, 5).toString();
            Double presupuestoString = Double.parseDouble(tablaObras.getValueAt(selectedRow, 6).toString());
            Double area = Double.parseDouble(tablaObras.getValueAt(selectedRow, 7).toString());
            Presupuesto presupuesto = presupuestoRepo.obtenerTodos().stream().filter(p -> p.getCliente().getNombre().equals(nombreCliente) && p.getCosto().equals(presupuestoString)).findFirst().orElse(null);

            // preguntar al usuario si desea modificar o eliminar el tipo de maquinaria
            String[] options = {"Modificar", "Eliminar"};
            int option = JOptionPane.showOptionDialog(panel, "¿Qué desea hacer con la obra?", "Obra", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            Obra obra;
            switch (option) {
                case 1:
                    auditoriaRepo.registrar("Eliminar", "Obra con nombre " + nombre);
                    
                    obraRepo.eliminarObra(id);
                    DefaultTableModel obraTableModel = mapearModeloObra(obraRepo.obtenerObras());
                    tablaObras.setModel(obraTableModel);

                    cargarGraficos();
                    break;
                case 0:
                    auditoriaRepo.registrar("Modificar", "Obra con nombre " + nombre);
                    
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

                            cargarGraficos();
                        }
                    });
                    break;
            }
        }
    }

    public void setTableClienteModel() throws IllegalAccessException {
        ClienteRepo clienteRepo = new ClienteRepo();

        // Create a DefaultTableModel with the column names and data
        DefaultTableModel clienteTableModel = mapearModeloCliente(clienteRepo.obtenerTodos());

        // Set the TableModel to the JTable
        tablaClientes.setModel(clienteTableModel);


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

    public void setTableTipoMaquinariaModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel tipoMaquinariaTableModel = mapearModeloTipoMaquinaria(tipoMaquinariaRepo.obtenerTodos());

        // Set the TableModel to the JTable
        tablaTipoMaquinaria.setModel(tipoMaquinariaTableModel);


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

    }

    public void setTableMaterialModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel materialTableModel = mapearModeloMaterial(tipoInsumoRepo.obtenerMateriales());

        // Set the TableModel to the JTable
        tablaMateriales.setModel(materialTableModel);

    }

    public DefaultTableModel mapearModeloMaterial(List<Material> materialList) {
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Nombre", "Cantidad", "Costo", "Tipo de material"));

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (Material material : materialList) {
            Vector<Object> row = new Vector<>();
            row.add(material.getId());
            row.add(material.getNombre());
            row.add(material.getCantidad());
            row.add(material.getCosto());
            row.add(material.getTipoInsumo().getNombre());
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
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Nombre", "Descripción", "Tipo de Obra", "Estado", "Cliente", "Presupuesto", "Área"));

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (Obra obra : obraList) {
            Vector<Object> row = new Vector<>();
            row.add(obra.getId());
            row.add(obra.getNombre());
            row.add(obra.getDescripcion());
            row.add(obra.getTipoObra().getNombre());
            row.add(obra.getEstado());
            row.add(obra.getPresupuesto().getCliente().getNombre());
            row.add(obra.getPresupuesto().getCosto());
            row.add(obra.getArea());
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
        TipoMaquinariaRepo tipoMaquinariaRepo = new TipoMaquinariaRepo();
        TipoInsumoRepo tipoInsumoRepo = new TipoInsumoRepo();
        ObraRepo obraRepo = new ObraRepo();

        comboFiltroTipoMaquinaria = new JComboBox<>();
        comboFiltroTipoMaquinaria.setPreferredSize(new Dimension(150, 25));
        comboFiltroTipoMaquinaria.addItem("Todos");
        List<String> tiposMaquinaria = tipoMaquinariaRepo.obtenerTodos().stream()
                .map(TipoMaquinaria::getNombre)
                .toList();
        tiposMaquinaria.forEach(comboFiltroTipoMaquinaria::addItem);

        comboFiltroTipoInsumo = new JComboBox<>(new String[]{"Todos"});
        comboFiltroTipoInsumo.setModel(new DefaultComboBoxModel<>(
                Stream.concat(
                        Stream.of("Todos"),
                        tipoInsumoRepo.obtenerTodos().stream().map(TipoInsumo::getNombre)
                ).toArray(String[]::new)
        ));

        List<String> estados = List.of("Todos","Activa", "Inactiva", "Finalizada", "Cancelada", "Espera", "En aprobación");
        comboFiltroEstadoObra = new JComboBox<>(estados.toArray(new String[0]));
        comboFiltroEstadoObra.setSelectedItem("Todos");

        comboFiltroTipoObra = new JComboBox<>(new String[]{"Todos"});
        comboFiltroTipoObra.setModel(new DefaultComboBoxModel<>(Stream.concat(Stream.of("Todos"), obraRepo.obtenerTiposObra().stream().map(TipoObra::getNombre)).toArray(String[]::new)));
    }

    private void verGraficosClientes(){
        auditoriaRepo.registrar("Ver", "Gráficos de clientes");
        
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
    }

    private void filtrarObras() {
        try {
            String nombre = txtFiltroNombreObra.getText().trim();
            String estado = (String) comboFiltroEstadoObra.getSelectedItem();
            String clienteNombre = txtFiltroClienteObra.getText().trim();
            String tipoObra = (String) comboFiltroTipoObra.getSelectedItem();

            Double presupuestoMin = null;
            if (!txtFiltroPresupuestoMinObra.getText().isEmpty()) {
                presupuestoMin = Double.parseDouble(txtFiltroPresupuestoMinObra.getText());
            }

            Double presupuestoMax = null;
            if (!txtFiltroPresupuestoMaxObra.getText().isEmpty()) {
                presupuestoMax = Double.parseDouble(txtFiltroPresupuestoMaxObra.getText());
            }

            List<Obra> obrasFiltradas = obraRepo.obtenerObrasFiltradas(
                    nombre, estado, clienteNombre, tipoObra,
                    presupuestoMin, presupuestoMax
            );

            DefaultTableModel obraTableModel = mapearModeloObra(obrasFiltradas);
            tablaObras.setModel(obraTableModel);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Por favor, ingrese valores numéricos válidos para el presupuesto",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al filtrar obras: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFiltrosObras() {
        txtFiltroNombreObra.setText("");
        comboFiltroEstadoObra.setSelectedItem("Todos");
        txtFiltroClienteObra.setText("");
        comboFiltroTipoObra.setSelectedItem("Todos");
        txtFiltroPresupuestoMinObra.setText("");
        txtFiltroPresupuestoMaxObra.setText("");

        DefaultTableModel obraTableModel = mapearModeloObra(obraRepo.obtenerObras());
        tablaObras.setModel(obraTableModel);
    }
}
