package com.criollo.machmillenium.vistas;

import com.criollo.machmillenium.entidades.*;
import com.criollo.machmillenium.enums.Privilegios;
import com.criollo.machmillenium.modelos.*;
import com.criollo.machmillenium.repos.*;
import com.criollo.machmillenium.utilidades.*;
import com.criollo.machmillenium.vistas.emergentes.*;
import com.criollo.machmillenium.vistas.emergentes.clientes.*;
import com.criollo.machmillenium.vistas.emergentes.maquinaria.*;
import com.criollo.machmillenium.vistas.emergentes.material.*;
import com.criollo.machmillenium.vistas.emergentes.obra.*;
import com.criollo.machmillenium.vistas.emergentes.personal.*;
import com.criollo.machmillenium.vistas.emergentes.presupuesto.*;
import com.criollo.machmillenium.vistas.gestor.RegistrarInasistencia;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Mach {
    public JPanel panel;
    private JTable tablaClientes;
    private JButton btnAgregarCliente;
    private JTextField txtFiltroNombre;
    private JTextField txtFiltroCedula;
    private JTextField txtFiltroCorreo;
    private JComboBox<String> comboFiltroSexo;
    private JButton btnFiltrarClientes;
    private JButton btnLimpiarFiltros;
    private JTable tablaPersonal;
    private JButton agregarButton;
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
    private JButton MaquinariaButton;
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
    private final EspecialidadRepo especialidadRepo;
    private final PagoRepo pagoRepo;
    private final PrivilegioRepo privilegioRepo;
    private JButton botonGestionarEspecialidades;
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
    private JTextField txtFiltroNombrePersonal;
    private JTextField txtFiltroCedulaPersonal;
    private JTextField txtFiltroCorreoPersonal;
    private JComboBox<String> comboEspecialidad;
    private JComboBox<String> comboRol;
    private JButton btnLimpiarfiltrosPersonal;
    private JButton btnFiltrarPersonal;
    private JRadioButton fijoRadioButton;
    private JRadioButton noFijoRadioButton;
    private JRadioButton activoRadioButton;
    private JRadioButton noActivoRadioButton;
    private JTable tablaInasistencia;
    private JButton registrarInasistencia;
    private JButton buttonGenerar;
    private JButton verGráficosButton;
    private JButton registrarPagoButton;
    private JTable tablaDirectos;
    private JButton imprimir;
    private JTable tablaCuotas;
    private JButton btnGenerar;
    private JButton verTipoMaquinariaButton;
    private JButton verTipoInsumoButton;
    private JButton cambiarPreguntasSeguridadButton;
    private JTabbedPane panelTabs;
    private JPanel panelInicio;
    private JPanel panelClientes;
    private JPanel panelPersonal;
    private JPanel panelMaquinaria;
    private JPanel panelMateriales;
    private JPanel panelPresupuesto;
    private JPanel panelObras;
    private JPanel panelAuditoria;
    private JPanel panelInasistencia;
    private JPanel panelRecibos;
    private Personal personal;
    private Set<String> privilegios;

    public Mach(Personal personal) {
        this.personalRepo = new PersonalRepo();
        this.clienteRepo = new ClienteRepo();
        this.tipoMaquinariaRepo = new TipoMaquinariaRepo();
        this.tipoInsumoRepo = new TipoInsumoRepo();
        this.presupuestoRepo = new PresupuestoRepo();
        this.obraRepo = new ObraRepo();
        this.auditoriaRepo = new AuditoriaRepo(personal.getNombre());
        this.especialidadRepo = new EspecialidadRepo();
        this.pagoRepo = new PagoRepo();
        this.privilegioRepo = new PrivilegioRepo();
        this.auditoriaRepo.registrar("Ingreso", "Ingreso al módulo de administrador");
        Utilidades.cambiarClaveOriginal(personal.getClave(), personal.getId(), true);

        this.personal = personal;
        this.privilegios = privilegioRepo.obtenerPrivilegiosDePersonal(personal).stream().map(Privilegio::getNombre).collect(Collectors.toSet());

        comprobarPrivilegios();
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
        imprimirPersonalButton.addActionListener(e -> {
            String nombre = txtFiltroNombrePersonal.getText().trim();
            String cedula = txtFiltroCedulaPersonal.getText().trim();
            String correo = txtFiltroCorreoPersonal.getText().trim();
            // si nnguno de los campos fijo o activo está seleccionado, se considera que no se filtra por ese campo
            Boolean fijo = null;
            if (fijoRadioButton.isSelected()) {
                fijo = true;
            } else if (noFijoRadioButton.isSelected()) {
                fijo = false;
            }

            Boolean activo = null;
            if (activoRadioButton.isSelected()) {
                activo = true;
            } else if (noActivoRadioButton.isSelected()) {
                activo = false;
            }

            String especialidad = (String) comboEspecialidad.getSelectedItem();
            String rol = (String) comboRol.getSelectedItem();


            List<ModeloPersonal> personalFiltrado = personalRepo.obtenerTodosPorFiltros(
                    nombre, cedula, correo, fijo, activo, especialidad, rol
            );

            GeneradorReportes.generarReportePersonal(personalFiltrado);
        });
        verGraficosClientesButton.addActionListener(e -> {
            verGraficosClientes();
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
        limpiarButton.addActionListener(e -> {
            auditoriaRepo.registrar("Limpiar", "Auditoría");
            setTableAuditoriaModel();
            personalCombo.setSelectedIndex(0);
            campoRealizado.setText("");
            campoAccion.setText("");
            campoDetalle.setText("");
            DefaultTableModel auditoriaTableModel = mapearModeloAuditoria(auditoriaRepo.obtenerAuditorias());
            auditoria.setModel(auditoriaTableModel);
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
        });
        calcularButton.addActionListener(e -> {
            Calculadora calculadora = new Calculadora();
            calculadora.pack();
            calculadora.setVisible(true);
        });
        botonGestionarEspecialidades.addActionListener(e -> abrirGestionEspecialidades());

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
        btnFiltrarPersonal.addActionListener(e -> filtrarPersonal());
        btnLimpiarfiltrosPersonal.addActionListener(e -> limpiarFiltrosPersonal());
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
        registrarPagoButton.addActionListener(e -> {
            auditoriaRepo.registrar("Registrar pago", "Ingreso al formulario de registro de pago");
            RegistrarPago registrarPago = new RegistrarPago();
            JFrame jframe = new JFrame("Registrar pago");
            jframe.setContentPane(registrarPago.panel);
            jframe.pack();
            jframe.setVisible(true);
            jframe.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                    auditoriaRepo.registrar("Registrar pago", "Cierre del formulario de registro de pago");
                    setTablePagoCuotasModel();
                    setTablePagoDirectoModel();
                }
            });
        });
        tablaDirectos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!personal.getRol().getNombre().equals("Usuario Operativo") && !privilegios.contains(Privilegios.MODIFICAR_RECIBOS.name()) ) {
                    return;
                }

                if (e.getClickCount() == 2 && tablaDirectos.getSelectedRow() != -1) {
                    auditoriaRepo.registrar("Imprimir factura", "Impresión de factura de pago directo");
                    Long id = (Long) tablaDirectos.getValueAt(tablaDirectos.getSelectedRow(), 0);
                    Pago pago = pagoRepo.obtenerPagoPorId(id);
                    ModeloRecibo modeloRecibo = new ModeloRecibo(pago);
                    GeneradorReportes.generarRecibo(modeloRecibo);
                }
            }
        });
        tablaCuotas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!personal.getRol().getNombre().equals("Usuario Operativo") && !privilegios.contains(Privilegios.MODIFICAR_RECIBOS.name()) ) {
                    return;
                }

                if (e.getClickCount() == 2 && tablaCuotas.getSelectedRow() != -1) {
                    auditoriaRepo.registrar("Ver cuotas", "Visualización de cuotas de pago");
                    Long id = (Long) tablaCuotas.getValueAt(tablaCuotas.getSelectedRow(), 0);
                    Cuotas cuotas = new Cuotas(pagoRepo, id);
                    cuotas.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    cuotas.pack();
                    cuotas.setLocationRelativeTo(SwingUtilities.getWindowAncestor(panel));
                    cuotas.setVisible(true);
                }
            }
        });
        btnGenerar.addActionListener(e -> {
            auditoriaRepo.registrar("Generar solicitud de compra", "Ingreso al formulario de generación de solicitud de compra");
            Utilidades.generarSolicitudCompra(personal, panel);
        });
        verTipoMaquinariaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auditoriaRepo.registrar("Ver", "Tipos de maquinaria");
                setTableAuditoriaModel();
                SwingUtilities.getWindowAncestor(panel).setEnabled(false);
                VerTipoMaquinaria dialog = new VerTipoMaquinaria();
                dialog.setVisible(true);
                setTableMaquinariaModel();
            }
        });
        verTipoInsumoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auditoriaRepo.registrar("Ver", "Tipos de insumo");
                setTableAuditoriaModel();
                SwingUtilities.getWindowAncestor(panel).setEnabled(false);
                VerTipoInsumo dialog = new VerTipoInsumo();
                dialog.setVisible(true);
                setTableMaterialModel();
            }
        });
        cambiarPreguntasSeguridadButton.addActionListener(e -> {
            ConfigurarPreguntasSeguridad dialog = new ConfigurarPreguntasSeguridad(personal);
            dialog.setVisible(true);
        });
    }

    public void setTables(){
        try {
            setTablePersonalModel();
            setTableClienteModel();
            setTableMaquinariaModel();
            setTableMaterialModel();
            setTablePresupuestoModel();
            setTableObraModel();
            setTableAuditoriaModel();
            setTableInasistenciaModel();
            setTablePagoCuotasModel();
            setTablePagoDirectoModel();
        } catch (Exception e) {
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
                cargarGraficos();
            }
        });
    }

    public void tablaClientesClick(MouseEvent e) {
        if (personal.getRol().getNombre().equals("Usuario Operativo") && !privilegios.contains(Privilegios.MODIFICAR_CLIENTES.name()) ) {
            return;
        }
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

                    cargarGraficos();
                }
            });
        }
    }

    public void tablaPersonalClick(MouseEvent e){
        if (!personal.getRol().getNombre().equals("Administrador") && !privilegios.contains(Privilegios.MODIFICAR_PERSONAL.name()) ) {
            return;
        }

        if (e.getClickCount() == 2 && tablaPersonal.getSelectedRow() != -1) {
            int selectedRow = tablaPersonal.getSelectedRow();

            // Obtener los valores de la fila seleccionada para crear el ModeloCliente
            Long id = Long.parseLong(tablaPersonal.getValueAt(selectedRow, 0).toString());
            String nombre = tablaPersonal.getValueAt(selectedRow, 1).toString();
            String cedula = tablaPersonal.getValueAt(selectedRow, 2).toString();
            String correo = tablaPersonal.getValueAt(selectedRow, 3).toString();
            Boolean fijo = Boolean.parseBoolean(tablaPersonal.getValueAt(selectedRow, 7).toString());
            String especialidad = tablaPersonal.getValueAt(selectedRow, 6).toString();
            String rol = tablaPersonal.getValueAt(selectedRow, 4).toString();
            Boolean activo = Boolean.parseBoolean(tablaPersonal.getValueAt(selectedRow, 8).toString());
            String fechaTerminoContrato = tablaPersonal.getValueAt(selectedRow, 5).toString();

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

                cargarGraficos();
            }
        });
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

            }
        });
    }

    public void tablaMaterialClick(MouseEvent e){
        if (personal.getRol().getNombre().equals("Usuario Operativo") && !privilegios.contains(Privilegios.MODIFICAR_MATERIALES.name()) ) {
            return;
        }
        if (e.getClickCount() == 2 && tablaMateriales.getSelectedRow() != -1) {
            int selectedRow = tablaMateriales.getSelectedRow();
            Long id = Long.parseLong(tablaMateriales.getValueAt(selectedRow, 0).toString());
            String nombre = tablaMateriales.getValueAt(selectedRow, 1).toString();
            Long cantidad = Long.parseLong(tablaMateriales.getValueAt(selectedRow, 2).toString());
            Long stockMinimo = Long.parseLong(tablaMateriales.getValueAt(selectedRow, 3).toString());
            Long stockMaximo = Long.parseLong(tablaMateriales.getValueAt(selectedRow, 4).toString());
            Double costo = Double.parseDouble(tablaMateriales.getValueAt(selectedRow, 5).toString());
            String tipoInsumo = tablaMateriales.getValueAt(selectedRow, 6).toString();

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

                    break;
                case 0:
                    auditoriaRepo.registrar("Modificar", "Material con nombre " + nombre);
                    setTableAuditoriaModel();
                    material = new Material(id, tipoInsumo, nombre, cantidad, costo, stockMinimo, stockMaximo);
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
        if (personal.getRol().getNombre().equals("Usuario Operativo") && !privilegios.contains(Privilegios.MODIFICAR_PRESUPUESTO.name()) ) {
            return;
        }
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

            }
        });
    }

    public void tablaObrasClick(MouseEvent e){
        if (personal.getRol().getNombre().equals("Usuario Operativo") && !privilegios.contains(Privilegios.MODIFICAR_OBRAS.name()) ) {
            return;
        }

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
            String[] options = {"Modificar", "Eliminar", "Ver objetivos", "Ver incidencias", "Cancelar"};
            int option = JOptionPane.showOptionDialog(panel, "¿Qué desea hacer con la obra?", "Obra", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            Obra obra;
            switch (option) {
                case 1:
                    auditoriaRepo.registrar("Eliminar", "Obra con nombre " + nombre);
                    setTableAuditoriaModel();
                    obraRepo.eliminarObra(id);
                    DefaultTableModel obraTableModel = mapearModeloObra(obraRepo.obtenerObras());
                    tablaObras.setModel(obraTableModel);

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

                            cargarGraficos();
                        }
                    });
                    break;
                case 2:
                    auditoriaRepo.registrar("Ver objetivos", "Obra con nombre " + nombre);
                    setTableAuditoriaModel();
                    ObjetivosObra objetivosObra = new ObjetivosObra(id);
                    objetivosObra.setTitle("Objetivos de la obra " + nombre);
                    objetivosObra.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    objetivosObra.pack();
                    objetivosObra.setLocationRelativeTo(SwingUtilities.getWindowAncestor(panel));
                    objetivosObra.setVisible(true);

                    objetivosObra.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                            SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                            SwingUtilities.getWindowAncestor(panel).setVisible(true);
                            SwingUtilities.getWindowAncestor(panel).toFront();
                        }
                    });
                    break;
                case 3:
                    auditoriaRepo.registrar("Ver incidencias", "Obra con nombre " + nombre);
                    setTableAuditoriaModel();
                    IncidenciasObra incidenciasObra = new IncidenciasObra(id);
                    incidenciasObra.setTitle("Incidencias de la obra " + nombre);
                    incidenciasObra.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    incidenciasObra.pack();
                    incidenciasObra.setLocationRelativeTo(SwingUtilities.getWindowAncestor(panel));
                    incidenciasObra.setVisible(true);

                    incidenciasObra.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                            SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                            SwingUtilities.getWindowAncestor(panel).setVisible(true);
                            SwingUtilities.getWindowAncestor(panel).toFront();
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

    public void setTableMaterialModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel materialTableModel = mapearModeloMaterial(tipoInsumoRepo.obtenerMateriales());

        // Set the TableModel to the JTable
        tablaMateriales.setModel(materialTableModel);

    }

    public DefaultTableModel mapearModeloMaterial(List<Material> materialList) {
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Nombre", "Cantidad", "Stock Mínimo", "Stock Máximo", "Costo", "Tipo de material"));

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (Material material : materialList) {
            Vector<Object> row = new Vector<>();
            row.add(material.getId());
            row.add(material.getNombre());
            row.add(material.getCantidad());
            row.add(material.getStockMinimo());
            row.add(material.getStockMaximo());
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
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Nombre", "Descripción", "Tipo de Obra", "Estado", "Cliente", "Presupuesto", "Area", "Tiempo total"));

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

            String tiempoTotal = obra.getTiempoTotal() != null ? obra.getTiempoTotal().toHours() + " horas " + obra.getTiempoTotal().toMinutesPart() + " minutos" : "No definido";
            row.add(tiempoTotal);
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

    }

    public void abrirGestionEspecialidades() {
        SwingUtilities.getWindowAncestor(panel).setEnabled(false);
        GestionarEspecialidades dialog = new GestionarEspecialidades(especialidadRepo);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(panel));
        dialog.setVisible(true);

        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                SwingUtilities.getWindowAncestor(panel).setEnabled(true);
                SwingUtilities.getWindowAncestor(panel).toFront();
                DefaultTableModel personalTableModel = mapearModeloPersonal(personalRepo.obtenerTodos());
                tablaPersonal.setModel(personalTableModel);
            }
        });
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
        double[] cantidadMaquinarias = maquinarias.stream().collect(Collectors.groupingBy(maquinaria -> maquinaria.getTipoMaquinaria().getNombre(), Collectors.counting()))
                .values().stream().mapToDouble(Long::doubleValue).toArray();
        tiposMaquinaria = Arrays.stream(tiposMaquinaria).distinct().toArray(String[]::new);
        ChartPanel chartPanelMaquinaria = GeneradorGraficos.generarGraficoPastel("Maquinaria por tipo", tiposMaquinaria, cantidadMaquinarias, 385, 300);
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
        EspecialidadRepo especialidadRepo = new EspecialidadRepo();
        RolRepo rolRepo = new RolRepo();
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            campoRealizado = new JFormattedTextField(dateMask);
        } catch (ParseException e) {
            campoRealizado = new JFormattedTextField();
            e.printStackTrace();
        }
        campoRealizado.setColumns(10);

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

        comboRol = new JComboBox<>(new String[]{"Todos"});
        comboRol.setModel(new DefaultComboBoxModel<>(Stream.concat(Stream.of("Todos"), rolRepo.obtenerTodos().stream().map(Rol::getNombre)).toArray(String[]::new)));

        comboEspecialidad = new JComboBox<>(new String[]{"Todos"});
        comboEspecialidad.setModel(new DefaultComboBoxModel<>(Stream.concat(Stream.of("Todos"), especialidadRepo.obtenerTodos().stream().map(Especialidad::getNombre)).toArray(String[]::new)));
    }

    private void verGraficosClientes(){
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

    private void filtrarPersonal() {
        try {
            String nombre = txtFiltroNombrePersonal.getText().trim();
            String cedula = txtFiltroCedulaPersonal.getText().trim();
            String correo = txtFiltroCorreoPersonal.getText().trim();
            // si nnguno de los campos fijo o activo está seleccionado, se considera que no se filtra por ese campo
            Boolean fijo = null;
            if (fijoRadioButton.isSelected()) {
                fijo = true;
            } else if (noFijoRadioButton.isSelected()) {
                fijo = false;
            }

            Boolean activo = null;
            if (activoRadioButton.isSelected()) {
                activo = true;
            } else if (noActivoRadioButton.isSelected()) {
                activo = false;
            }

            String especialidad = (String) comboEspecialidad.getSelectedItem();
            String rol = (String) comboRol.getSelectedItem();


            List<ModeloPersonal> personalFiltrado = personalRepo.obtenerTodosPorFiltros(
                    nombre, cedula, correo, fijo, activo, especialidad, rol
            );

            DefaultTableModel personalTableModel = mapearModeloPersonal(personalFiltrado);
            tablaPersonal.setModel(personalTableModel);

            // Store the filtered results for the report
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al filtrar personal: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFiltrosPersonal() {
        txtFiltroNombrePersonal.setText("");
        txtFiltroCedulaPersonal.setText("");
        txtFiltroCorreoPersonal.setText("");

        DefaultTableModel personalTableModel = mapearModeloPersonal(personalRepo.obtenerTodos());
        tablaPersonal.setModel(personalTableModel);
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
        if (personal.getRol().getNombre().equals("Usuario Operativo") && privilegios.contains(Privilegios.MODIFICAR_INASISTENCIAS)) {
            return;
        }
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

    public DefaultTableModel mapearModeloPagoDirecto(List<Pago> pagoList) {
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Fecha", "Monto", "Descripción", "Obra"));

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (Pago pago : pagoList) {
            Vector<Object> row = new Vector<>();
            row.add(pago.getId());
            row.add(pago.getCreado());
            row.add(pago.getObra().getPresupuesto().getCosto());
            row.add(pago.getObra().getDescripcion());
            row.add(pago.getObra().getNombre());
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public DefaultTableModel mapearModeloPagoCuotas(List<Pago> pagoList) {
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Fecha", "Monto", "Descripción", "Obra", "Cantidad", "Pagadas"));

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (Pago pago : pagoList) {
            Vector<Object> row = new Vector<>();
            row.add(pago.getId());
            row.add(pago.getCreado());
            row.add(pago.getObra().getPresupuesto().getCosto());
            row.add(pago.getObra().getDescripcion());
            row.add(pago.getObra().getNombre());
            row.add(pago.getCantidadCuotas());

            Long cuotas = pagoRepo.obtenerCantidadCuotasPorPagoId(pago.getId());
            row.add(cuotas);
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void setTablePagoDirectoModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel pagoDirectoTableModel = mapearModeloPagoDirecto(pagoRepo.obtenerPagosPorCuotas(false));

        // Set the TableModel to the JTable
        tablaDirectos.setModel(pagoDirectoTableModel);
    }

    public void setTablePagoCuotasModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel pagoCuotasTableModel = mapearModeloPagoCuotas(pagoRepo.obtenerPagosPorCuotas(true));

        // Set the TableModel to the JTable
        tablaCuotas.setModel(pagoCuotasTableModel);
    }

    /*
    *  Inicio = 0
    * Clientes = 1
    * Personal = 2
    * Maquinaria = 3
    * Materiales = 4
    * Presupuesto = 5
    * Obras = 6
    * Auditoria = 7
    * Inasistencias = 8
    * Pagos = 9
     */
    private void comprobarPrivilegios() {
        String rolNombre = personal.getRol().getNombre();

        System.out.println("Privilegios: " + privilegios);

        boolean tienePrivilegiosRecibos = privilegios.contains(Privilegios.VER_RECIBOS.name()) ||
                                        privilegios.contains(Privilegios.CREAR_RECIBOS.name()) ||
                                        privilegios.contains(Privilegios.MODIFICAR_RECIBOS.name());
        
        boolean tienePrivilegiosPersonal = privilegios.contains(Privilegios.VER_PERSONAL.name()) ||
                                         privilegios.contains(Privilegios.CREAR_PERSONAL.name()) ||
                                         privilegios.contains(Privilegios.MODIFICAR_PERSONAL.name());

        switch (rolNombre) {
            case "Administrador":
                // Por defecto puede ver todo
                // Solo remueve recibos si no tiene privilegios específicos
                if (!tienePrivilegiosRecibos) {
                    panelTabs.removeTabAt(9);
                    btnGenerar.setVisible(false);
                }
                // Configurar botones según privilegios
                btnAgregarCliente.setVisible(true);
                agregarButton.setVisible(true);
                verTipoMaquinariaButton.setVisible(true);
                verTipoInsumoButton.setVisible(true);
                botonAgregarMaquinaria.setVisible(true);
                botonAgregarMaterial.setVisible(true);
                botonAgregarPresupuesto.setVisible(true);
                botonAgregarObra.setVisible(true);
                registrarInasistencia.setVisible(true);
                registrarPagoButton.setVisible(privilegios.contains(Privilegios.CREAR_RECIBOS.name()));
                break;

            case "Gerente de Proyecto":
                // Por defecto no ve recibos ni personal, pero los privilegios pueden habilitarlos
                List<Integer> tabsToRemove = new ArrayList<>();
                if (!tienePrivilegiosRecibos) {
                    tabsToRemove.add(9);
                    btnGenerar.setVisible(false);
                }
                if (!tienePrivilegiosPersonal) {
                    tabsToRemove.add(1);
                }
                // Remover tabs de mayor a menor índice para evitar problemas con los índices
                tabsToRemove.sort(Collections.reverseOrder());
                for (int index : tabsToRemove) {
                    panelTabs.removeTabAt(index);
                }
                // Configurar botones según privilegios
                btnAgregarCliente.setVisible(true);
                verTipoMaquinariaButton.setVisible(true);
                verTipoInsumoButton.setVisible(true);
                botonAgregarMaquinaria.setVisible(true);
                botonAgregarMaterial.setVisible(true);
                botonAgregarPresupuesto.setVisible(true);
                botonAgregarObra.setVisible(true);
                registrarInasistencia.setVisible(true);
                agregarButton.setVisible(privilegios.contains(Privilegios.CREAR_PERSONAL.name()));
                registrarPagoButton.setVisible(privilegios.contains(Privilegios.CREAR_RECIBOS.name()));
                break;

            case "Usuario Operativo":
                // Por defecto solo ve obras (sin crear/editar) y tiene acceso total a pagos
                Set<Integer> tabsToKeep = new HashSet<>();

                tabsToKeep.add(6);  // Obras
                tabsToKeep.add(9); // Recibos
                
                // Agregar tabs adicionales según privilegios
                if (privilegios.contains(Privilegios.VER_CLIENTES.name())) tabsToKeep.add(1);
                if (privilegios.contains(Privilegios.VER_PERSONAL.name())) tabsToKeep.add(2);
                if (privilegios.contains(Privilegios.VER_TIPO_MAQUINARIA.name())) tabsToKeep.add(3);
                if (privilegios.contains(Privilegios.VER_MAQUINARIA.name())) tabsToKeep.add(4);
                if (privilegios.contains(Privilegios.VER_TIPO_INSUMO.name())) tabsToKeep.add(5);
                if (privilegios.contains(Privilegios.VER_MATERIALES.name())) tabsToKeep.add(6);
                if (privilegios.contains(Privilegios.VER_PRESUPUESTO.name())) tabsToKeep.add(7);
                if (privilegios.contains(Privilegios.VER_AUDITORIA.name())) tabsToKeep.add(9);
                if (privilegios.contains(Privilegios.VER_INASISTENCIAS.name())) tabsToKeep.add(10);

                System.out.println("Tabs to keep: " + tabsToKeep);

                // Remover todos los tabs excepto los que debe mantener
                for (int i = panelTabs.getTabCount() - 1; i > 0; i--) {
                    if (!tabsToKeep.contains(i)) {
                        panelTabs.removeTabAt(i);
                    }
                }

                // Configurar botones según privilegios
                btnAgregarCliente.setVisible(privilegios.contains(Privilegios.CREAR_CLIENTES.name()));
                agregarButton.setVisible(privilegios.contains(Privilegios.CREAR_PERSONAL.name()));
                verTipoMaquinariaButton.setVisible(privilegios.contains(Privilegios.VER_TIPO_MAQUINARIA.name()));
                verTipoInsumoButton.setVisible(privilegios.contains(Privilegios.VER_TIPO_INSUMO.name()));
                botonAgregarMaquinaria.setVisible(privilegios.contains(Privilegios.CREAR_MAQUINARIA.name()));
                botonAgregarMaterial.setVisible(privilegios.contains(Privilegios.CREAR_MATERIALES.name()));
                botonAgregarPresupuesto.setVisible(privilegios.contains(Privilegios.CREAR_PRESUPUESTO.name()));
                botonAgregarObra.setVisible(privilegios.contains(Privilegios.CREAR_OBRAS.name()));
                registrarInasistencia.setVisible(privilegios.contains(Privilegios.CREAR_INASISTENCIAS.name()));
                registrarPagoButton.setVisible(true); // Siempre tiene acceso total a recibos
                break;
        }
    }
}
