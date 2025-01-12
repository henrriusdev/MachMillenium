package com.criollo.machmillenium.vistas;

import com.criollo.machmillenium.entidades.*;
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
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Mach {
    private JPanel mainPanel;
    private JMenuBar menuBar;
    private Map<String, JPanel> paneles;
    private Personal usuarioActual;
    private final PrivilegioRepo privilegioRepo;
    private final PersonalRepo personalRepo;
    private final ClienteRepo clienteRepo;
    private final TipoMaquinariaRepo tipoMaquinariaRepo;
    private final TipoInsumoRepo tipoInsumoRepo;
    private final PresupuestoRepo presupuestoRepo;
    private final MaquinariaRepo maquinariaRepo;
    private final MaterialRepo materialRepo;
    private final ObraRepo obraRepo;
    private final InasistenciaRepo inasistenciaRepo;
    private final PagoRepo pagoRepo;
    private final AuditoriaRepo auditoriaRepo;
    private final EspecialidadRepo especialidadRepo;
    private final RolRepo rolRepo;
    
    public Mach(Personal usuario) {
        this.usuarioActual = usuario;
        this.privilegioRepo = new PrivilegioRepo();
        this.personalRepo = new PersonalRepo();
        this.clienteRepo = new ClienteRepo();
        this.tipoMaquinariaRepo = new TipoMaquinariaRepo();
        this.tipoInsumoRepo = new TipoInsumoRepo();
        this.presupuestoRepo = new PresupuestoRepo();
        this.maquinariaRepo = new MaquinariaRepo();
        this.materialRepo = new MaterialRepo();
        this.obraRepo = new ObraRepo();
        this.inasistenciaRepo = new InasistenciaRepo();
        this.pagoRepo = new PagoRepo();
        this.auditoriaRepo = new AuditoriaRepo();
        this.especialidadRepo = new EspecialidadRepo();
        this.rolRepo = new RolRepo();
        
        initComponents();
        configurarMenuBar();
        mostrarComponentesSegunPrivilegios();
        initInasistenciasListeners();
        initPagosListeners();
    }
    
    public JPanel getMainPanel() {
        return mainPanel;
    }
    
    private void initComponents() {
        mainPanel = new JPanel(new CardLayout());
        paneles = new HashMap<>();
        
        // Inicializar todos los paneles
        paneles.put("CLIENTES", crearPanelClientes());
        paneles.put("PERSONAL", crearPanelPersonal());
        paneles.put("OBRAS", crearPanelObras());
        paneles.put("MAQUINARIAS", crearPanelMaquinarias());
        paneles.put("MATERIALES", crearPanelMateriales());
        paneles.put("PRESUPUESTOS", crearPanelPresupuestos());
        paneles.put("INASISTENCIAS", crearPanelInasistencias());
        paneles.put("PAGOS", crearPanelPagos());
        paneles.put("AUDITORIA", crearPanelAuditoria());
        
        // Agregar paneles al mainPanel
        for (Map.Entry<String, JPanel> entry : paneles.entrySet()) {
            mainPanel.add(entry.getValue(), entry.getKey());
        }
    }
    
    private void configurarMenuBar() {
        menuBar = new JMenuBar();
        
        // Menú Gestión
        JMenu menuGestion = new JMenu("Gestión");
        agregarItemMenu(menuGestion, "Clientes", "CLIENTES", "GESTIONAR_CLIENTES");
        agregarItemMenu(menuGestion, "Personal", "PERSONAL", "GESTIONAR_PERSONAL");
        agregarItemMenu(menuGestion, "Obras", "OBRAS", "GESTIONAR_OBRAS");
        agregarItemMenu(menuGestion, "Maquinarias", "MAQUINARIAS", "GESTIONAR_MAQUINARIAS");
        agregarItemMenu(menuGestion, "Materiales", "MATERIALES", "GESTIONAR_MATERIALES");
        
        // Menú Finanzas
        JMenu menuFinanzas = new JMenu("Finanzas");
        agregarItemMenu(menuFinanzas, "Presupuestos", "PRESUPUESTOS", "GESTIONAR_PRESUPUESTOS");
        agregarItemMenu(menuFinanzas, "Pagos", "PAGOS", "GESTIONAR_PAGOS");
        
        // Menú Reportes
        JMenu menuReportes = new JMenu("Reportes");
        agregarItemMenu(menuReportes, "Inasistencias", "INASISTENCIAS", "VER_INASISTENCIAS");
        agregarItemMenu(menuReportes, "Auditoría", "AUDITORIA", "VER_AUDITORIA");
        
        menuBar.add(menuGestion);
        menuBar.add(menuFinanzas);
        menuBar.add(menuReportes);
    }
    
    private void agregarItemMenu(JMenu menu, String titulo, String panelId, String codigoPrivilegio) {
        JMenuItem menuItem = new JMenuItem(titulo);
        menuItem.setActionCommand(codigoPrivilegio);
        menuItem.addActionListener(e -> mostrarPanel(panelId));
        menu.add(menuItem);
    }
    
    private void mostrarPanel(String panelId) {
        CardLayout cl = (CardLayout) mainPanel.getLayout();
        cl.show(mainPanel, panelId);
    }
    
    private void mostrarComponentesSegunPrivilegios() {
        List<Privilegio> privilegios = privilegioRepo.obtenerPrivilegiosPorRol(usuarioActual.getRol().getNombre());
        Set<String> codigosPrivilegios = privilegios.stream()
            .map(Privilegio::getCodigo)
            .collect(Collectors.toSet());
            
        // Ocultar menús según privilegios
        for (Component menuComponent : menuBar.getComponents()) {
            if (menuComponent instanceof JMenu) {
                JMenu menu = (JMenu) menuComponent;
                boolean tieneAcceso = false;
                
                for (int i = 0; i < menu.getItemCount(); i++) {
                    JMenuItem item = menu.getItem(i);
                    if (item != null) {
                        String privilegioCodigo = item.getActionCommand();
                        if (!codigosPrivilegios.contains(privilegioCodigo)) {
                            item.setVisible(false);
                        } else {
                            tieneAcceso = true;
                        }
                    }
                }
                
                menu.setVisible(tieneAcceso);
            }
        }
    }
    
    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Componentes del panel de clientes
        JTable tablaClientes = new JTable();
        JButton btnAgregarCliente = new JButton("Agregar Cliente");
        JTextField txtFiltroNombre = new JTextField(20);
        JTextField txtFiltroCedula = new JTextField(15);
        JTextField txtFiltroCorreo = new JTextField(20);
        JComboBox<String> comboFiltroSexo = new JComboBox<>(new String[]{"Todos", "M", "F"});
        JButton btnFiltrarClientes = new JButton("Filtrar");
        JButton btnLimpiarFiltros = new JButton("Limpiar Filtros");
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());
        panelFiltros.add(new JLabel("Nombre:"));
        panelFiltros.add(txtFiltroNombre);
        panelFiltros.add(new JLabel("Cédula:"));
        panelFiltros.add(txtFiltroCedula);
        panelFiltros.add(new JLabel("Correo:"));
        panelFiltros.add(txtFiltroCorreo);
        panelFiltros.add(new JLabel("Sexo:"));
        panelFiltros.add(comboFiltroSexo);
        panelFiltros.add(btnFiltrarClientes);
        panelFiltros.add(btnLimpiarFiltros);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnAgregarCliente);
        
        // Configuración de la tabla
        DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Cédula", "Correo", "Sexo", "Teléfono", "Dirección"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaClientes.setModel(modeloTabla);
        
        // Agregar componentes al panel principal
        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        // Eventos
        btnAgregarCliente.addActionListener(e -> {
            AgregarCliente dialog = new AgregarCliente();
            dialog.setVisible(true);
            if (dialog.isExito()) {
                actualizarTablaClientes(tablaClientes);
            }
        });
        
        btnFiltrarClientes.addActionListener(e -> {
            String nombre = txtFiltroNombre.getText();
            String cedula = txtFiltroCedula.getText();
            String correo = txtFiltroCorreo.getText();
            String sexo = comboFiltroSexo.getSelectedItem().toString();
            
            List<Cliente> clientes = clienteRepo.obtenerTodos();
            clientes = clientes.stream()
                .filter(c -> nombre.isEmpty() || c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(c -> cedula.isEmpty() || c.getCedula().contains(cedula))
                .filter(c -> correo.isEmpty() || c.getCorreo().toLowerCase().contains(correo.toLowerCase()))
                .filter(c -> sexo.equals("Todos") || c.getSexo().equals(sexo))
                .collect(Collectors.toList());
                
            actualizarTablaClientes(tablaClientes, clientes);
        });
        
        btnLimpiarFiltros.addActionListener(e -> {
            txtFiltroNombre.setText("");
            txtFiltroCedula.setText("");
            txtFiltroCorreo.setText("");
            comboFiltroSexo.setSelectedIndex(0);
            actualizarTablaClientes(tablaClientes);
        });
        
        // Doble clic en la tabla para editar
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tablaClientes.getSelectedRow();
                    if (row != -1) {
                        Long id = (Long) tablaClientes.getValueAt(row, 0);
                        Cliente cliente = clienteRepo.obtenerPorId(id);
                        if (cliente != null) {
                            ModificarCliente dialog = new ModificarCliente(cliente);
                            dialog.setVisible(true);
                            if (dialog.isExito()) {
                                actualizarTablaClientes(tablaClientes);
                            }
                        }
                    }
                }
            }
        });
        
        // Cargar datos iniciales
        actualizarTablaClientes(tablaClientes);
        
        return panel;
    }
    
    private void actualizarTablaClientes(JTable tabla) {
        actualizarTablaClientes(tabla, clienteRepo.obtenerTodos());
    }
    
    private void actualizarTablaClientes(JTable tabla, List<Cliente> clientes) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        
        for (Cliente cliente : clientes) {
            modelo.addRow(new Object[]{
                cliente.getId(),
                cliente.getNombre(),
                cliente.getCedula(),
                cliente.getCorreo(),
                cliente.getSexo(),
                cliente.getTelefono(),
                cliente.getDireccion()
            });
        }
    }
    
    private JPanel crearPanelPersonal() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Componentes del panel de personal
        JTable tablaPersonal = new JTable();
        JButton btnAgregarPersonal = new JButton("Agregar Personal");
        JButton btnGestionarEspecialidades = new JButton("Gestionar Especialidades");
        JButton btnVerGraficos = new JButton("Ver Gráficos");
        JButton btnImprimir = new JButton("Imprimir");
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JTextField txtFiltroNombre = new JTextField(20);
        JTextField txtFiltroCedula = new JTextField(15);
        JTextField txtFiltroCorreo = new JTextField(20);
        
        // Radio buttons para personal fijo
        JPanel panelFijo = new JPanel();
        panelFijo.setBorder(BorderFactory.createTitledBorder("Personal Fijo"));
        ButtonGroup grupoFijo = new ButtonGroup();
        JRadioButton rbFijoTodos = new JRadioButton("Todos", true);
        JRadioButton rbFijoSi = new JRadioButton("Sí");
        JRadioButton rbFijoNo = new JRadioButton("No");
        grupoFijo.add(rbFijoTodos);
        grupoFijo.add(rbFijoSi);
        grupoFijo.add(rbFijoNo);
        panelFijo.add(rbFijoTodos);
        panelFijo.add(rbFijoSi);
        panelFijo.add(rbFijoNo);
        
        // Radio buttons para personal activo
        JPanel panelActivo = new JPanel();
        panelActivo.setBorder(BorderFactory.createTitledBorder("Personal Activo"));
        ButtonGroup grupoActivo = new ButtonGroup();
        JRadioButton rbActivoTodos = new JRadioButton("Todos", true);
        JRadioButton rbActivoSi = new JRadioButton("Sí");
        JRadioButton rbActivoNo = new JRadioButton("No");
        grupoActivo.add(rbActivoTodos);
        grupoActivo.add(rbActivoSi);
        grupoActivo.add(rbActivoNo);
        panelActivo.add(rbActivoTodos);
        panelActivo.add(rbActivoSi);
        panelActivo.add(rbActivoNo);
        
        // Combos para especialidad y rol
        JComboBox<String> comboEspecialidad = new JComboBox<>();
        JComboBox<String> comboRol = new JComboBox<>();
        
        // Cargar especialidades y roles
        comboEspecialidad.addItem("Todas");
        List<Especialidad> especialidades = especialidadRepo.obtenerTodas();
        for (Especialidad esp : especialidades) {
            comboEspecialidad.addItem(esp.getNombre());
        }
        
        comboRol.addItem("Todos");
        List<Rol> roles = rolRepo.obtenerTodos();
        for (Rol rol : roles) {
            comboRol.addItem(rol.getNombre());
        }
        
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnLimpiarFiltros = new JButton("Limpiar");
        
        panelFiltros.add(new JLabel("Nombre:"));
        panelFiltros.add(txtFiltroNombre);
        panelFiltros.add(new JLabel("Cédula:"));
        panelFiltros.add(txtFiltroCedula);
        panelFiltros.add(new JLabel("Correo:"));
        panelFiltros.add(txtFiltroCorreo);
        panelFiltros.add(panelFijo);
        panelFiltros.add(panelActivo);
        panelFiltros.add(new JLabel("Especialidad:"));
        panelFiltros.add(comboEspecialidad);
        panelFiltros.add(new JLabel("Rol:"));
        panelFiltros.add(comboRol);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnLimpiarFiltros);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnAgregarPersonal);
        panelBotones.add(btnGestionarEspecialidades);
        panelBotones.add(btnVerGraficos);
        panelBotones.add(btnImprimir);
        
        // Configuración de la tabla
        DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Cédula", "Correo", "Teléfono", "Fijo", "Activo", "Especialidad", "Rol"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 || columnIndex == 6 ? Boolean.class : Object.class;
            }
        };
        tablaPersonal.setModel(modeloTabla);
        
        // Agregar componentes al panel principal
        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaPersonal), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        // Eventos
        btnAgregarPersonal.addActionListener(e -> {
            AgregarPersonal dialog = new AgregarPersonal();
            dialog.setVisible(true);
            if (dialog.isExito()) {
                actualizarTablaPersonal(tablaPersonal);
            }
        });
        
        btnGestionarEspecialidades.addActionListener(e -> {
            GestionarEspecialidades dialog = new GestionarEspecialidades();
            dialog.setVisible(true);
            if (dialog.isExito()) {
                // Actualizar combo de especialidades
                comboEspecialidad.removeAllItems();
                comboEspecialidad.addItem("Todas");
                List<Especialidad> nuevasEspecialidades = especialidadRepo.obtenerTodas();
                for (Especialidad esp : nuevasEspecialidades) {
                    comboEspecialidad.addItem(esp.getNombre());
                }
            }
        });
        
        btnVerGraficos.addActionListener(e -> {
            ChartPanel chartPanel = GeneradorGraficos.generarGraficoPersonalPorEspecialidad();
            Graficos dialog = new Graficos(chartPanel, "Personal por Especialidad");
            dialog.setVisible(true);
        });
        
        btnImprimir.addActionListener(e -> {
            try {
                GeneradorReportes.generarReportePersonal();
                JOptionPane.showMessageDialog(panel, 
                    "Reporte generado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, 
                    "Error al generar el reporte: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnFiltrar.addActionListener(e -> {
            String nombre = txtFiltroNombre.getText();
            String cedula = txtFiltroCedula.getText();
            String correo = txtFiltroCorreo.getText();
            
            Boolean fijo = null;
            if (rbFijoSi.isSelected()) fijo = true;
            else if (rbFijoNo.isSelected()) fijo = false;
            
            Boolean activo = null;
            if (rbActivoSi.isSelected()) activo = true;
            else if (rbActivoNo.isSelected()) activo = false;
            
            String especialidad = comboEspecialidad.getSelectedItem().toString();
            if (especialidad.equals("Todas")) especialidad = null;
            
            String rol = comboRol.getSelectedItem().toString();
            if (rol.equals("Todos")) rol = null;
            
            List<Personal> personal = personalRepo.obtenerTodos();
            personal = personal.stream()
                .filter(p -> nombre.isEmpty() || 
                    (p.getNombre() != null && p.getNombre().toLowerCase().contains(nombre.toLowerCase())))
                .filter(p -> cedula.isEmpty() || 
                    (p.getCedula() != null && p.getCedula().toLowerCase().contains(cedula.toLowerCase())))
                .filter(p -> correo.isEmpty() || 
                    (p.getCorreo() != null && p.getCorreo().toLowerCase().contains(correo.toLowerCase())))
                .filter(p -> fijo == null || p.isFijo() == fijo)
                .filter(p -> activo == null || p.isActivo() == activo)
                .filter(p -> especialidad == null || 
                    (p.getEspecialidad() != null && p.getEspecialidad().getNombre().equals(especialidad)))
                .filter(p -> rol == null || 
                    (p.getRol() != null && p.getRol().getNombre().equals(rol)))
                .collect(Collectors.toList());
                
            actualizarTablaPersonal(tablaPersonal, personal);
        });
        
        btnLimpiarFiltros.addActionListener(e -> {
            txtFiltroNombre.setText("");
            txtFiltroCedula.setText("");
            txtFiltroCorreo.setText("");
            rbFijoTodos.setSelected(true);
            rbActivoTodos.setSelected(true);
            comboEspecialidad.setSelectedIndex(0);
            comboRol.setSelectedIndex(0);
            actualizarTablaPersonal(tablaPersonal);
        });
        
        // Doble clic en la tabla para editar
        tablaPersonal.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tablaPersonal.getSelectedRow();
                    if (row != -1) {
                        Long id = (Long) tablaPersonal.getValueAt(row, 0);
                        Personal personal = personalRepo.obtenerPorId(id);
                        if (personal != null) {
                            ModificarPersonal dialog = new ModificarPersonal(personal);
                            dialog.setVisible(true);
                            if (dialog.isExito()) {
                                actualizarTablaPersonal(tablaPersonal);
                            }
                        }
                    }
                }
            }
        });
        
        // Cargar datos iniciales
        actualizarTablaPersonal(tablaPersonal);
        
        return panel;
    }
    
    private void actualizarTablaPersonal(JTable tabla) {
        actualizarTablaPersonal(tabla, personalRepo.obtenerTodos());
    }
    
    private void actualizarTablaPersonal(JTable tabla, List<Personal> personal) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        
        for (Personal p : personal) {
            modelo.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getCedula(),
                p.getCorreo(),
                p.getTelefono(),
                p.isFijo(),
                p.isActivo(),
                p.getEspecialidad() != null ? p.getEspecialidad().getNombre() : "",
                p.getRol() != null ? p.getRol().getNombre() : ""
            });
        }
    }
    
    private JPanel crearPanelObras() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Componentes del panel de obras
        JTable tablaObras = new JTable();
        JButton btnAgregarObra = new JButton("Registrar Obra");
        JButton btnVerGraficos = new JButton("Ver Gráficos");
        JButton btnImprimir = new JButton("Imprimir");
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JTextField txtFiltroNombre = new JTextField(20);
        JTextField txtFiltroCliente = new JTextField(20);
        JComboBox<String> comboFiltroEstado = new JComboBox<>(new String[]{"Todos", "En Proceso", "Finalizada", "Cancelada"});
        JComboBox<String> comboFiltroTipo = new JComboBox<>();
        JTextField txtFiltroPresupuestoMin = new JTextField(10);
        JTextField txtFiltroPresupuestoMax = new JTextField(10);
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnLimpiarFiltros = new JButton("Limpiar");
        
        // Cargar tipos de obra
        comboFiltroTipo.addItem("Todos");
        List<TipoObra> tiposObra = tipoObraRepo.obtenerTodos();
        for (TipoObra tipo : tiposObra) {
            comboFiltroTipo.addItem(tipo.getNombre());
        }
        
        panelFiltros.add(new JLabel("Nombre:"));
        panelFiltros.add(txtFiltroNombre);
        panelFiltros.add(new JLabel("Cliente:"));
        panelFiltros.add(txtFiltroCliente);
        panelFiltros.add(new JLabel("Estado:"));
        panelFiltros.add(comboFiltroEstado);
        panelFiltros.add(new JLabel("Tipo:"));
        panelFiltros.add(comboFiltroTipo);
        panelFiltros.add(new JLabel("Presupuesto Min:"));
        panelFiltros.add(txtFiltroPresupuestoMin);
        panelFiltros.add(new JLabel("Presupuesto Max:"));
        panelFiltros.add(txtFiltroPresupuestoMax);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnLimpiarFiltros);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnAgregarObra);
        panelBotones.add(btnVerGraficos);
        panelBotones.add(btnImprimir);
        
        // Configuración de la tabla
        DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Cliente", "Tipo", "Estado", "Fecha Inicio", "Fecha Fin", "Presupuesto"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaObras.setModel(modeloTabla);
        
        // Agregar componentes al panel principal
        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaObras), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        // Eventos
        btnAgregarObra.addActionListener(e -> {
            RegistrarObra dialog = new RegistrarObra();
            dialog.setVisible(true);
            if (dialog.isExito()) {
                actualizarTablaObras(tablaObras);
            }
        });
        
        btnVerGraficos.addActionListener(e -> {
            // Generar gráfico de barras para cantidad de obras por cliente
            Map<String, Long> clienteObrasMap = new HashMap<>();
            List<Obra> obras = obraRepo.obtenerTodas();
            
            for (Obra obra : obras) {
                if (obra.getCliente() != null) {
                    String nombreCliente = obra.getCliente().getNombre();
                    clienteObrasMap.merge(nombreCliente, 1L, Long::sum);
                }
            }
            
            String[] nombresClientes = clienteObrasMap.keySet().toArray(new String[0]);
            double[] cantidadObras = clienteObrasMap.values().stream()
                .mapToDouble(Long::doubleValue)
                .toArray();
                
            ChartPanel chartPanel = GeneradorGraficos.generarGraficoBarras(
                "Obras por cliente", 
                "Clientes", 
                "Cantidad de obras",
                nombresClientes,
                cantidadObras,
                385,
                300
            );
            
            double[] costosClientes = obras.stream()
                .mapToDouble(obra -> obra.getPresupuesto() != null ? obra.getPresupuesto().getTotal() : 0.0)
                .toArray();
            
            // Definir las desviaciones como un porcentaje del costo
            double[] desvios = new double[costosClientes.length];
            for (int i = 0; i < costosClientes.length; i++) {
                desvios[i] = costosClientes[i] * 0.1; // 10% del costo como desviación
            }
            
            ChartPanel chartPanelCostos = GeneradorGraficos.generarGraficoDesviacion(
                "Costos de obras por cliente",
                "Clientes",
                "Costo",
                "Costo",
                costosClientes,
                desvios,
                790,
                300
            );
            
            List<ChartPanel> graficos = Arrays.asList(chartPanel, chartPanelCostos);
            Graficos dialog = new Graficos("Gráficos de Obras", graficos);
            dialog.setVisible(true);
        });
        
        btnImprimir.addActionListener(e -> {
            try {
                GeneradorReportes.generarReporteObras();
                JOptionPane.showMessageDialog(panel, 
                    "Reporte generado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, 
                    "Error al generar el reporte: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnFiltrar.addActionListener(e -> {
            try {
                String nombre = txtFiltroNombre.getText();
                String cliente = txtFiltroCliente.getText();
                String estado = comboFiltroEstado.getSelectedItem().toString();
                String tipo = comboFiltroTipo.getSelectedItem().toString();
                
                Double presupuestoMin = null;
                if (!txtFiltroPresupuestoMin.getText().isEmpty()) {
                    presupuestoMin = Double.parseDouble(txtFiltroPresupuestoMin.getText());
                }
                
                Double presupuestoMax = null;
                if (!txtFiltroPresupuestoMax.getText().isEmpty()) {
                    presupuestoMax = Double.parseDouble(txtFiltroPresupuestoMax.getText());
                }
                
                List<Obra> obras = obraRepo.obtenerTodas();
                obras = obras.stream()
                    .filter(o -> nombre.isEmpty() || 
                        (o.getNombre() != null && o.getNombre().toLowerCase().contains(nombre.toLowerCase())))
                    .filter(o -> cliente.isEmpty() || 
                        (o.getCliente() != null && o.getCliente().getNombre().toLowerCase().contains(cliente.toLowerCase())))
                    .filter(o -> estado.equals("Todos") || 
                        (o.getEstado() != null && o.getEstado().equals(estado)))
                    .filter(o -> tipo.equals("Todos") || 
                        (o.getTipoObra() != null && o.getTipoObra().getNombre().equals(tipo)))
                    .filter(o -> presupuestoMin == null || 
                        (o.getPresupuesto() != null && o.getPresupuesto().getTotal() >= presupuestoMin))
                    .filter(o -> presupuestoMax == null || 
                        (o.getPresupuesto() != null && o.getPresupuesto().getTotal() <= presupuestoMax))
                    .collect(Collectors.toList());
                    
                actualizarTablaObras(tablaObras, obras);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Por favor, ingrese valores numéricos válidos para el presupuesto",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnLimpiarFiltros.addActionListener(e -> {
            txtFiltroNombre.setText("");
            txtFiltroCliente.setText("");
            comboFiltroEstado.setSelectedIndex(0);
            comboFiltroTipo.setSelectedIndex(0);
            txtFiltroPresupuestoMin.setText("");
            txtFiltroPresupuestoMax.setText("");
            actualizarTablaObras(tablaObras);
        });
        
        // Doble clic en la tabla para editar
        tablaObras.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tablaObras.getSelectedRow();
                    if (row != -1) {
                        Long id = (Long) tablaObras.getValueAt(row, 0);
                        Obra obra = obraRepo.obtenerPorId(id);
                        if (obra != null) {
                            ModificarRegistroObra dialog = new ModificarRegistroObra(obra);
                            dialog.setVisible(true);
                            if (dialog.isExito()) {
                                actualizarTablaObras(tablaObras);
                            }
                        }
                    }
                }
            }
        });
        
        // Menú contextual
        JPopupMenu menuContextual = new JPopupMenu();
        JMenuItem itemIncidencias = new JMenuItem("Gestionar Incidencias");
        JMenuItem itemObjetivos = new JMenuItem("Gestionar Objetivos");
        
        itemIncidencias.addActionListener(e -> {
            int row = tablaObras.getSelectedRow();
            if (row != -1) {
                Long id = (Long) tablaObras.getValueAt(row, 0);
                Obra obra = obraRepo.obtenerPorId(id);
                if (obra != null) {
                    IncidenciasObra dialog = new IncidenciasObra(obra);
                    dialog.setVisible(true);
                }
            }
        });
        
        itemObjetivos.addActionListener(e -> {
            int row = tablaObras.getSelectedRow();
            if (row != -1) {
                Long id = (Long) tablaObras.getValueAt(row, 0);
                Obra obra = obraRepo.obtenerPorId(id);
                if (obra != null) {
                    ObjetivosObra dialog = new ObjetivosObra(obra);
                    dialog.setVisible(true);
                }
            }
        });
        
        menuContextual.add(itemIncidencias);
        menuContextual.add(itemObjetivos);
        
        tablaObras.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = tablaObras.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        tablaObras.setRowSelectionInterval(row, row);
                        menuContextual.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
        
        // Cargar datos iniciales
        actualizarTablaObras(tablaObras);
        
        return panel;
    }
    
    private void actualizarTablaObras(JTable tabla) {
        actualizarTablaObras(tabla, obraRepo.obtenerTodas());
    }
    
    private void actualizarTablaObras(JTable tabla, List<Obra> obras) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Obra obra : obras) {
            modelo.addRow(new Object[]{
                obra.getId(),
                obra.getNombre(),
                obra.getCliente() != null ? obra.getCliente().getNombre() : "",
                obra.getTipoObra() != null ? obra.getTipoObra().getNombre() : "",
                obra.getEstado(),
                obra.getFechaInicio() != null ? obra.getFechaInicio().format(formatter) : "",
                obra.getFechaFin() != null ? obra.getFechaFin().format(formatter) : "",
                obra.getPresupuesto() != null ? String.format("$%.2f", obra.getPresupuesto().getTotal()) : ""
            });
        }
    }
    
    private JPanel crearPanelMaquinarias() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Componentes del panel de maquinarias
        JTable tablaMaquinarias = new JTable();
        JButton btnAgregarMaquinaria = new JButton("Agregar Maquinaria");
        JButton btnVerGraficos = new JButton("Ver Gráficos");
        JButton btnImprimir = new JButton("Imprimir");
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JTextField txtFiltroNombre = new JTextField(20);
        JTextField txtFiltroCostoMin = new JTextField(10);
        JTextField txtFiltroCostoMax = new JTextField(10);
        JComboBox<String> comboFiltroTipo = new JComboBox<>();
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnLimpiarFiltros = new JButton("Limpiar");
        
        // Cargar tipos de maquinaria en el combo
        List<TipoMaquinaria> tiposMaquinaria = tipoMaquinariaRepo.obtenerTodos();
        comboFiltroTipo.addItem("Todos");
        for (TipoMaquinaria tipo : tiposMaquinaria) {
            comboFiltroTipo.addItem(tipo.getNombre());
        }
        
        panelFiltros.add(new JLabel("Nombre:"));
        panelFiltros.add(txtFiltroNombre);
        panelFiltros.add(new JLabel("Costo Min:"));
        panelFiltros.add(txtFiltroCostoMin);
        panelFiltros.add(new JLabel("Costo Max:"));
        panelFiltros.add(txtFiltroCostoMax);
        panelFiltros.add(new JLabel("Tipo:"));
        panelFiltros.add(comboFiltroTipo);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnLimpiarFiltros);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnAgregarMaquinaria);
        panelBotones.add(btnVerGraficos);
        panelBotones.add(btnImprimir);
        
        // Configuración de la tabla
        DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Tipo", "Costo por Hora", "Estado", "Disponibilidad"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaMaquinarias.setModel(modeloTabla);
        
        // Agregar componentes al panel principal
        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaMaquinarias), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        // Eventos
        btnAgregarMaquinaria.addActionListener(e -> {
            AgregarMaquinaria dialog = new AgregarMaquinaria();
            dialog.setVisible(true);
            if (dialog.isExito()) {
                actualizarTablaMaquinarias(tablaMaquinarias);
            }
        });
        
        btnVerGraficos.addActionListener(e -> {
            ChartPanel chartPanel = GeneradorGraficos.generarGraficoMaquinariasPorTipo();
            Graficos dialog = new Graficos(chartPanel, "Maquinarias por Tipo");
            dialog.setVisible(true);
        });
        
        btnImprimir.addActionListener(e -> {
            try {
                GeneradorReportes.generarReporteMaquinarias();
                JOptionPane.showMessageDialog(panel, 
                    "Reporte generado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, 
                    "Error al generar el reporte: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnFiltrar.addActionListener(e -> {
            String nombre = txtFiltroNombre.getText();
            String tipo = comboFiltroTipo.getSelectedItem().toString();
            
            Double costoMin = null;
            Double costoMax = null;
            
            try {
                if (!txtFiltroCostoMin.getText().isEmpty()) {
                    costoMin = Double.parseDouble(txtFiltroCostoMin.getText());
                }
                if (!txtFiltroCostoMax.getText().isEmpty()) {
                    costoMax = Double.parseDouble(txtFiltroCostoMax.getText());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Por favor, ingrese valores numéricos válidos para los costos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<Maquinaria> maquinarias = maquinariaRepo.obtenerTodas();
            maquinarias = maquinarias.stream()
                .filter(m -> nombre.isEmpty() || m.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(m -> tipo.equals("Todos") || 
                    (m.getTipoMaquinaria() != null && m.getTipoMaquinaria().getNombre().equals(tipo)))
                .filter(m -> costoMin == null || m.getCostoPorHora() >= costoMin)
                .filter(m -> costoMax == null || m.getCostoPorHora() <= costoMax)
                .collect(Collectors.toList());
                
            actualizarTablaMaquinarias(tablaMaquinarias, maquinarias);
        });
        
        btnLimpiarFiltros.addActionListener(e -> {
            txtFiltroNombre.setText("");
            txtFiltroCostoMin.setText("");
            txtFiltroCostoMax.setText("");
            comboFiltroTipo.setSelectedIndex(0);
            actualizarTablaMaquinarias(tablaMaquinarias);
        });
        
        // Doble clic en la tabla para editar
        tablaMaquinarias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tablaMaquinarias.getSelectedRow();
                    if (row != -1) {
                        Long id = (Long) tablaMaquinarias.getValueAt(row, 0);
                        Maquinaria maquinaria = maquinariaRepo.obtenerPorId(id);
                        if (maquinaria != null) {
                            ModificarMaquinaria dialog = new ModificarMaquinaria(maquinaria);
                            dialog.setVisible(true);
                            if (dialog.isExito()) {
                                actualizarTablaMaquinarias(tablaMaquinarias);
                            }
                        }
                    }
                }
            }
        });
        
        // Cargar datos iniciales
        actualizarTablaMaquinarias(tablaMaquinarias);
        
        return panel;
    }
    
    private void actualizarTablaMaquinarias(JTable tabla) {
        actualizarTablaMaquinarias(tabla, maquinariaRepo.obtenerTodas());
    }
    
    private void actualizarTablaMaquinarias(JTable tabla, List<Maquinaria> maquinarias) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        
        for (Maquinaria maquinaria : maquinarias) {
            modelo.addRow(new Object[]{
                maquinaria.getId(),
                maquinaria.getNombre(),
                maquinaria.getTipoMaquinaria() != null ? maquinaria.getTipoMaquinaria().getNombre() : "",
                String.format("$%.2f", maquinaria.getCostoPorHora()),
                maquinaria.getEstado(),
                maquinaria.isDisponible() ? "Disponible" : "No Disponible"
            });
        }
    }
    
    private JPanel crearPanelMateriales() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Componentes del panel de materiales
        JTable tablaMateriales = new JTable();
        JButton btnAgregarMaterial = new JButton("Agregar Material");
        JButton btnVerGraficos = new JButton("Ver Gráficos");
        JButton btnImprimir = new JButton("Imprimir");
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JTextField txtFiltroNombre = new JTextField(20);
        JTextField txtFiltroPrecioMin = new JTextField(10);
        JTextField txtFiltroPrecioMax = new JTextField(10);
        JComboBox<String> comboFiltroTipo = new JComboBox<>();
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnLimpiarFiltros = new JButton("Limpiar");
        
        // Cargar tipos de material en el combo
        List<TipoInsumo> tiposInsumo = tipoInsumoRepo.obtenerTodos();
        comboFiltroTipo.addItem("Todos");
        for (TipoInsumo tipo : tiposInsumo) {
            comboFiltroTipo.addItem(tipo.getNombre());
        }
        
        panelFiltros.add(new JLabel("Nombre:"));
        panelFiltros.add(txtFiltroNombre);
        panelFiltros.add(new JLabel("Precio Min:"));
        panelFiltros.add(txtFiltroPrecioMin);
        panelFiltros.add(new JLabel("Precio Max:"));
        panelFiltros.add(txtFiltroPrecioMax);
        panelFiltros.add(new JLabel("Tipo:"));
        panelFiltros.add(comboFiltroTipo);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnLimpiarFiltros);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnAgregarMaterial);
        panelBotones.add(btnVerGraficos);
        panelBotones.add(btnImprimir);
        
        // Configuración de la tabla
        DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Tipo", "Precio", "Stock", "Unidad de Medida"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaMateriales.setModel(modeloTabla);
        
        // Agregar componentes al panel principal
        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaMateriales), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        // Eventos
        btnAgregarMaterial.addActionListener(e -> {
            AgregarMaterial dialog = new AgregarMaterial();
            dialog.setVisible(true);
            if (dialog.isExito()) {
                actualizarTablaMateriales(tablaMateriales);
            }
        });
        
        btnVerGraficos.addActionListener(e -> {
            ChartPanel chartPanel = GeneradorGraficos.generarGraficoMaterialesPorTipo();
            Graficos dialog = new Graficos(chartPanel, "Materiales por Tipo");
            dialog.setVisible(true);
        });
        
        btnImprimir.addActionListener(e -> {
            try {
                GeneradorReportes.generarReporteMateriales();
                JOptionPane.showMessageDialog(panel, 
                    "Reporte generado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, 
                    "Error al generar el reporte: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnFiltrar.addActionListener(e -> {
            String nombre = txtFiltroNombre.getText();
            String tipo = comboFiltroTipo.getSelectedItem().toString();
            
            Double precioMin = null;
            Double precioMax = null;
            
            try {
                if (!txtFiltroPrecioMin.getText().isEmpty()) {
                    precioMin = Double.parseDouble(txtFiltroPrecioMin.getText());
                }
                if (!txtFiltroPrecioMax.getText().isEmpty()) {
                    precioMax = Double.parseDouble(txtFiltroPrecioMax.getText());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel,
                    "Por favor, ingrese valores numéricos válidos para los precios",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<Material> materiales = materialRepo.obtenerTodos();
            materiales = materiales.stream()
                .filter(m -> nombre.isEmpty() || m.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .filter(m -> tipo.equals("Todos") || 
                    (m.getTipoInsumo() != null && m.getTipoInsumo().getNombre().equals(tipo)))
                .filter(m -> precioMin == null || m.getPrecio() >= precioMin)
                .filter(m -> precioMax == null || m.getPrecio() <= precioMax)
                .collect(Collectors.toList());
                
            actualizarTablaMateriales(tablaMateriales, materiales);
        });
        
        btnLimpiarFiltros.addActionListener(e -> {
            txtFiltroNombre.setText("");
            txtFiltroPrecioMin.setText("");
            txtFiltroPrecioMax.setText("");
            comboFiltroTipo.setSelectedIndex(0);
            actualizarTablaMateriales(tablaMateriales);
        });
        
        // Doble clic en la tabla para editar
        tablaMateriales.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tablaMateriales.getSelectedRow();
                    if (row != -1) {
                        Long id = (Long) tablaMateriales.getValueAt(row, 0);
                        Material material = materialRepo.obtenerPorId(id);
                        if (material != null) {
                            EditarMaterial dialog = new EditarMaterial(material);
                            dialog.setVisible(true);
                            if (dialog.isExito()) {
                                actualizarTablaMateriales(tablaMateriales);
                            }
                        }
                    }
                }
            }
        });
        
        // Cargar datos iniciales
        actualizarTablaMateriales(tablaMateriales);
        
        return panel;
    }
    
    private void actualizarTablaMateriales(JTable tabla) {
        actualizarTablaMateriales(tabla, materialRepo.obtenerTodos());
    }
    
    private void actualizarTablaMateriales(JTable tabla, List<Material> materiales) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        
        for (Material material : materiales) {
            modelo.addRow(new Object[]{
                material.getId(),
                material.getNombre(),
                material.getTipoInsumo() != null ? material.getTipoInsumo().getNombre() : "",
                String.format("$%.2f", material.getPrecio()),
                material.getStock(),
                material.getUnidadMedida()
            });
        }
    }
    
    private JPanel crearPanelPresupuestos() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Componentes del panel de presupuestos
        JTable tablaPresupuestos = new JTable();
        JButton btnAgregarPresupuesto = new JButton("Crear Presupuesto");
        JButton btnCalculadora = new JButton("Calculadora");
        JButton btnImprimir = new JButton("Imprimir");
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JTextField txtFiltroObra = new JTextField(20);
        JTextField txtFiltroCliente = new JTextField(20);
        JComboBox<String> comboFiltroEstado = new JComboBox<>(new String[]{"Todos", "Pendiente", "Aprobado", "Rechazado"});
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnLimpiarFiltros = new JButton("Limpiar");
        
        panelFiltros.add(new JLabel("Obra:"));
        panelFiltros.add(txtFiltroObra);
        panelFiltros.add(new JLabel("Cliente:"));
        panelFiltros.add(txtFiltroCliente);
        panelFiltros.add(new JLabel("Estado:"));
        panelFiltros.add(comboFiltroEstado);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnLimpiarFiltros);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnAgregarPresupuesto);
        panelBotones.add(btnCalculadora);
        panelBotones.add(btnImprimir);
        
        // Configuración de la tabla
        DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Obra", "Cliente", "Fecha", "Total", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPresupuestos.setModel(modeloTabla);
        
        // Agregar componentes al panel principal
        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaPresupuestos), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        // Eventos
        btnAgregarPresupuesto.addActionListener(e -> {
            CrearPresupuesto dialog = new CrearPresupuesto();
            dialog.setVisible(true);
            if (dialog.isExito()) {
                actualizarTablaPresupuestos(tablaPresupuestos);
            }
        });
        
        btnCalculadora.addActionListener(e -> {
            Calculadora dialog = new Calculadora();
            dialog.setVisible(true);
        });
        
        btnImprimir.addActionListener(e -> {
            try {
                GeneradorReportes.generarReportePresupuestos();
                JOptionPane.showMessageDialog(panel, 
                    "Reporte generado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, 
                    "Error al generar el reporte: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnFiltrar.addActionListener(e -> {
            String obra = txtFiltroObra.getText();
            String cliente = txtFiltroCliente.getText();
            String estado = comboFiltroEstado.getSelectedItem().toString();
            
            List<Presupuesto> presupuestos = presupuestoRepo.obtenerTodos();
            presupuestos = presupuestos.stream()
                .filter(p -> obra.isEmpty() || 
                    (p.getObra() != null && p.getObra().getNombre().toLowerCase().contains(obra.toLowerCase())))
                .filter(p -> cliente.isEmpty() || 
                    (p.getObra() != null && p.getObra().getCliente() != null && 
                    p.getObra().getCliente().getNombre().toLowerCase().contains(cliente.toLowerCase())))
                .filter(p -> estado.equals("Todos") || p.getEstado().equals(estado))
                .collect(Collectors.toList());
                
            actualizarTablaPresupuestos(tablaPresupuestos, presupuestos);
        });
        
        btnLimpiarFiltros.addActionListener(e -> {
            txtFiltroObra.setText("");
            txtFiltroCliente.setText("");
            comboFiltroEstado.setSelectedIndex(0);
            actualizarTablaPresupuestos(tablaPresupuestos);
        });
        
        // Doble clic en la tabla para editar
        tablaPresupuestos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tablaPresupuestos.getSelectedRow();
                    if (row != -1) {
                        Long id = (Long) tablaPresupuestos.getValueAt(row, 0);
                        Presupuesto presupuesto = presupuestoRepo.obtenerPorId(id);
                        if (presupuesto != null) {
                            EditarPresupuesto dialog = new EditarPresupuesto(presupuesto);
                            dialog.setVisible(true);
                            if (dialog.isExito()) {
                                actualizarTablaPresupuestos(tablaPresupuestos);
                            }
                        }
                    }
                }
            }
        });
        
        // Menú contextual
        JPopupMenu menuContextual = new JPopupMenu();
        JMenuItem itemDetalles = new JMenuItem("Ver Detalles");
        JMenuItem itemAprobar = new JMenuItem("Aprobar Presupuesto");
        JMenuItem itemRechazar = new JMenuItem("Rechazar Presupuesto");
        
        itemDetalles.addActionListener(e -> {
            int row = tablaPresupuestos.getSelectedRow();
            if (row != -1) {
                Long id = (Long) tablaPresupuestos.getValueAt(row, 0);
                Presupuesto presupuesto = presupuestoRepo.obtenerPorId(id);
                if (presupuesto != null) {
                    // TODO: Implementar vista de detalles
                    JOptionPane.showMessageDialog(panel,
                        "Detalles del presupuesto:\n\n" +
                        "Obra: " + presupuesto.getObra().getNombre() + "\n" +
                        "Cliente: " + presupuesto.getObra().getCliente().getNombre() + "\n" +
                        "Total: $" + String.format("%.2f", presupuesto.getTotal()) + "\n" +
                        "Estado: " + presupuesto.getEstado(),
                        "Detalles del Presupuesto",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        itemAprobar.addActionListener(e -> {
            int row = tablaPresupuestos.getSelectedRow();
            if (row != -1) {
                Long id = (Long) tablaPresupuestos.getValueAt(row, 0);
                Presupuesto presupuesto = presupuestoRepo.obtenerPorId(id);
                if (presupuesto != null && !presupuesto.getEstado().equals("Aprobado")) {
                    presupuesto.setEstado("Aprobado");
                    presupuestoRepo.actualizar(presupuesto);
                    actualizarTablaPresupuestos(tablaPresupuestos);
                }
            }
        });
        
        itemRechazar.addActionListener(e -> {
            int row = tablaPresupuestos.getSelectedRow();
            if (row != -1) {
                Long id = (Long) tablaPresupuestos.getValueAt(row, 0);
                Presupuesto presupuesto = presupuestoRepo.obtenerPorId(id);
                if (presupuesto != null && !presupuesto.getEstado().equals("Rechazado")) {
                    presupuesto.setEstado("Rechazado");
                    presupuestoRepo.actualizar(presupuesto);
                    actualizarTablaPresupuestos(tablaPresupuestos);
                }
            }
        });
        
        menuContextual.add(itemDetalles);
        menuContextual.add(itemAprobar);
        menuContextual.add(itemRechazar);
        
        tablaPresupuestos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = tablaPresupuestos.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        tablaPresupuestos.setRowSelectionInterval(row, row);
                        menuContextual.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
        
        // Cargar datos iniciales
        actualizarTablaPresupuestos(tablaPresupuestos);
        
        return panel;
    }
    
    private void actualizarTablaPresupuestos(JTable tabla) {
        actualizarTablaPresupuestos(tabla, presupuestoRepo.obtenerTodos());
    }
    
    private void actualizarTablaPresupuestos(JTable tabla, List<Presupuesto> presupuestos) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Presupuesto presupuesto : presupuestos) {
            modelo.addRow(new Object[]{
                presupuesto.getId(),
                presupuesto.getObra() != null ? presupuesto.getObra().getNombre() : "",
                presupuesto.getObra() != null && presupuesto.getObra().getCliente() != null ? 
                    presupuesto.getObra().getCliente().getNombre() : "",
                presupuesto.getFecha() != null ? presupuesto.getFecha().format(formatter) : "",
                String.format("$%.2f", presupuesto.getTotal()),
                presupuesto.getEstado()
            });
        }
    }
    
    private JPanel crearPanelInasistencias() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Componentes del panel de inasistencias
        JTable tablaInasistencias = new JTable();
        JButton btnRegistrarInasistencia = new JButton("Registrar Inasistencia");
        JButton btnVerGraficos = new JButton("Ver Gráficos");
        JButton btnImprimir = new JButton("Imprimir");
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JTextField txtFiltroPersonal = new JTextField(20);
        JComboBox<String> comboFiltroMes = new JComboBox<>();
        JTextField txtFiltroAnio = new JTextField(4);
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnLimpiarFiltros = new JButton("Limpiar");
        
        // Cargar meses en el combo
        String[] meses = {"Todos", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        for (String mes : meses) {
            comboFiltroMes.addItem(mes);
        }
        
        panelFiltros.add(new JLabel("Personal:"));
        panelFiltros.add(txtFiltroPersonal);
        panelFiltros.add(new JLabel("Mes:"));
        panelFiltros.add(comboFiltroMes);
        panelFiltros.add(new JLabel("Año:"));
        panelFiltros.add(txtFiltroAnio);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnLimpiarFiltros);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnRegistrarInasistencia);
        panelBotones.add(btnVerGraficos);
        panelBotones.add(btnImprimir);
        
        // Configuración de la tabla
        DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Personal", "Fecha", "Motivo", "Justificada"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 4 ? Boolean.class : Object.class;
            }
        };
        tablaInasistencias.setModel(modeloTabla);
        
        // Agregar componentes al panel principal
        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaInasistencias), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        // Eventos
        btnRegistrarInasistencia.addActionListener(e -> {
            RegistrarInasistencia dialog = new RegistrarInasistencia();
            dialog.setVisible(true);
            if (dialog.isExito()) {
                actualizarTablaInasistencias(tablaInasistencias);
            }
        });
        
        btnVerGraficos.addActionListener(e -> {
            ChartPanel chartPanel = GeneradorGraficos.generarGraficoInasistenciasPorMes();
            Graficos dialog = new Graficos(chartPanel, "Inasistencias por Mes");
            dialog.setVisible(true);
        });
        
        btnImprimir.addActionListener(e -> {
            try {
                GeneradorReportes.generarReporteInasistencias();
                JOptionPane.showMessageDialog(panel, 
                    "Reporte generado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, 
                    "Error al generar el reporte: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnFiltrar.addActionListener(e -> {
            String personal = txtFiltroPersonal.getText();
            String mes = comboFiltroMes.getSelectedItem().toString();
            String anio = txtFiltroAnio.getText();
            
            List<Inasistencia> inasistencias = inasistenciaRepo.obtenerTodas();
            inasistencias = inasistencias.stream()
                .filter(i -> personal.isEmpty() || 
                    (i.getPersonal() != null && i.getPersonal().getNombre().toLowerCase().contains(personal.toLowerCase())))
                .filter(i -> {
                    if (mes.equals("Todos")) return true;
                    if (i.getFecha() == null) return false;
                    return i.getFecha().getMonth().getDisplayName(TextStyle.FULL, new Locale("es")).equals(mes);
                })
                .filter(i -> {
                    if (anio.isEmpty()) return true;
                    if (i.getFecha() == null) return false;
                    try {
                        int yearToFilter = Integer.parseInt(anio);
                        return i.getFecha().getYear() == yearToFilter;
                    } catch (NumberFormatException ex) {
                        return true;
                    }
                })
                .collect(Collectors.toList());
                
            actualizarTablaInasistencias(tablaInasistencias, inasistencias);
        });
        
        btnLimpiarFiltros.addActionListener(e -> {
            txtFiltroPersonal.setText("");
            comboFiltroMes.setSelectedIndex(0);
            txtFiltroAnio.setText("");
            actualizarTablaInasistencias(tablaInasistencias);
        });
        
        // Doble clic en la tabla para editar
        tablaInasistencias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tablaInasistencias.getSelectedRow();
                    if (row != -1) {
                        Long id = (Long) tablaInasistencias.getValueAt(row, 0);
                        Inasistencia inasistencia = inasistenciaRepo.obtenerPorId(id);
                        if (inasistencia != null) {
                            EditarInasistencia dialog = new EditarInasistencia(inasistencia);
                            dialog.setVisible(true);
                            if (dialog.isExito()) {
                                actualizarTablaInasistencias(tablaInasistencias);
                            }
                        }
                    }
                }
            }
        });
        
        // Cargar datos iniciales
        actualizarTablaInasistencias(tablaInasistencias);
        
        return panel;
    }
    
    private void actualizarTablaInasistencias(JTable tabla) {
        actualizarTablaInasistencias(tabla, inasistenciaRepo.obtenerTodas());
    }
    
    private void actualizarTablaInasistencias(JTable tabla, List<Inasistencia> inasistencias) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Inasistencia inasistencia : inasistencias) {
            modelo.addRow(new Object[]{
                inasistencia.getId(),
                inasistencia.getPersonal() != null ? inasistencia.getPersonal().getNombre() : "",
                inasistencia.getFecha() != null ? inasistencia.getFecha().format(formatter) : "",
                inasistencia.getMotivo(),
                inasistencia.isJustificada()
            });
        }
    }
    
    private JPanel crearPanelPagos() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Componentes del panel de pagos
        JTable tablaPagos = new JTable();
        JButton btnRegistrarPago = new JButton("Registrar Pago");
        JButton btnVerGraficos = new JButton("Ver Gráficos");
        JButton btnImprimir = new JButton("Imprimir");
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JTextField txtFiltroPersonal = new JTextField(20);
        JComboBox<String> comboFiltroMes = new JComboBox<>();
        JTextField txtFiltroAnio = new JTextField(4);
        JComboBox<String> comboFiltroTipo = new JComboBox<>(new String[]{"Todos", "Sueldo", "Bono", "Adelanto"});
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnLimpiarFiltros = new JButton("Limpiar");
        
        // Cargar meses en el combo
        String[] meses = {"Todos", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", 
                         "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        for (String mes : meses) {
            comboFiltroMes.addItem(mes);
        }
        
        panelFiltros.add(new JLabel("Personal:"));
        panelFiltros.add(txtFiltroPersonal);
        panelFiltros.add(new JLabel("Mes:"));
        panelFiltros.add(comboFiltroMes);
        panelFiltros.add(new JLabel("Año:"));
        panelFiltros.add(txtFiltroAnio);
        panelFiltros.add(new JLabel("Tipo:"));
        panelFiltros.add(comboFiltroTipo);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnLimpiarFiltros);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnRegistrarPago);
        panelBotones.add(btnVerGraficos);
        panelBotones.add(btnImprimir);
        
        // Configuración de la tabla
        DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Personal", "Fecha", "Tipo", "Monto", "Descripción"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPagos.setModel(modeloTabla);
        
        // Agregar componentes al panel principal
        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaPagos), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        // Eventos
        btnRegistrarPago.addActionListener(e -> {
            RegistrarPago dialog = new RegistrarPago();
            dialog.setVisible(true);
            if (dialog.isExito()) {
                actualizarTablaPagos(tablaPagos);
            }
        });
        
        btnVerGraficos.addActionListener(e -> {
            ChartPanel chartPanel = GeneradorGraficos.generarGraficoPagosPorMes();
            Graficos dialog = new Graficos(chartPanel, "Pagos por Mes");
            dialog.setVisible(true);
        });
        
        btnImprimir.addActionListener(e -> {
            try {
                GeneradorReportes.generarReportePagos();
                JOptionPane.showMessageDialog(panel, 
                    "Reporte generado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, 
                    "Error al generar el reporte: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnFiltrar.addActionListener(e -> {
            String personal = txtFiltroPersonal.getText();
            String mes = comboFiltroMes.getSelectedItem().toString();
            String anio = txtFiltroAnio.getText();
            String tipo = comboFiltroTipo.getSelectedItem().toString();
            
            List<Pago> pagos = pagoRepo.obtenerTodos();
            pagos = pagos.stream()
                .filter(p -> personal.isEmpty() || 
                    (p.getPersonal() != null && p.getPersonal().getNombre().toLowerCase().contains(personal.toLowerCase())))
                .filter(p -> {
                    if (mes.equals("Todos")) return true;
                    if (p.getFecha() == null) return false;
                    return p.getFecha().getMonth().getDisplayName(TextStyle.FULL, new Locale("es")).equals(mes);
                })
                .filter(p -> {
                    if (anio.isEmpty()) return true;
                    if (p.getFecha() == null) return false;
                    try {
                        int yearToFilter = Integer.parseInt(anio);
                        return p.getFecha().getYear() == yearToFilter;
                    } catch (NumberFormatException ex) {
                        return true;
                    }
                })
                .filter(p -> tipo.equals("Todos") || p.getTipo().equals(tipo))
                .collect(Collectors.toList());
                
            actualizarTablaPagos(tablaPagos, pagos);
        });
        
        btnLimpiarFiltros.addActionListener(e -> {
            txtFiltroPersonal.setText("");
            comboFiltroMes.setSelectedIndex(0);
            txtFiltroAnio.setText("");
            comboFiltroTipo.setSelectedIndex(0);
            actualizarTablaPagos(tablaPagos);
        });
        
        // Doble clic en la tabla para editar
        tablaPagos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tablaPagos.getSelectedRow();
                    if (row != -1) {
                        Long id = (Long) tablaPagos.getValueAt(row, 0);
                        Pago pago = pagoRepo.obtenerPorId(id);
                        if (pago != null) {
                            EditarPago dialog = new EditarPago(pago);
                            dialog.setVisible(true);
                            if (dialog.isExito()) {
                                actualizarTablaPagos(tablaPagos);
                            }
                        }
                    }
                }
            }
        });
        
        // Cargar datos iniciales
        actualizarTablaPagos(tablaPagos);
        
        return panel;
    }
    
    private void actualizarTablaPagos(JTable tabla) {
        actualizarTablaPagos(tabla, pagoRepo.obtenerTodos());
    }
    
    private void actualizarTablaPagos(JTable tabla, List<Pago> pagos) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        for (Pago pago : pagos) {
            modelo.addRow(new Object[]{
                pago.getId(),
                pago.getPersonal() != null ? pago.getPersonal().getNombre() : "",
                pago.getFecha() != null ? pago.getFecha().format(formatter) : "",
                pago.getTipo(),
                String.format("$%.2f", pago.getMonto()),
                pago.getDescripcion()
            });
        }
    }
    
    private JPanel crearPanelAuditoria() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Componentes del panel de auditoría
        JTable tablaAuditoria = new JTable();
        
        // Panel de filtros
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JComboBox<String> comboPersonal = new JComboBox<>();
        JFormattedTextField campoRealizado = new JFormattedTextField();
        JTextField campoAccion = new JTextField(20);
        JTextField campoDetalle = new JTextField(30);
        JButton btnBuscar = new JButton("Buscar");
        JButton btnLimpiar = new JButton("Limpiar");
        
        try {
            MaskFormatter formatter = new MaskFormatter("##/##/####");
            formatter.setPlaceholderCharacter('_');
            campoRealizado = new JFormattedTextField(formatter);
            campoRealizado.setColumns(10);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        // Cargar personal en el combo
        comboPersonal.addItem("Todos");
        List<Personal> personal = personalRepo.obtenerTodos();
        for (Personal p : personal) {
            comboPersonal.addItem(p.getNombre());
        }
        
        panelFiltros.add(new JLabel("Personal:"));
        panelFiltros.add(comboPersonal);
        panelFiltros.add(new JLabel("Fecha:"));
        panelFiltros.add(campoRealizado);
        panelFiltros.add(new JLabel("Acción:"));
        panelFiltros.add(campoAccion);
        panelFiltros.add(new JLabel("Detalle:"));
        panelFiltros.add(campoDetalle);
        panelFiltros.add(btnBuscar);
        panelFiltros.add(btnLimpiar);
        
        // Configuración de la tabla
        DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Personal", "Fecha", "Acción", "Detalle"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaAuditoria.setModel(modeloTabla);
        
        // Agregar componentes al panel principal
        panel.add(panelFiltros, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaAuditoria), BorderLayout.CENTER);
        
        // Eventos
        btnBuscar.addActionListener(e -> {
            String personalSeleccionado = comboPersonal.getSelectedItem().toString();
            String fecha = campoRealizado.getText();
            String accion = campoAccion.getText();
            String detalle = campoDetalle.getText();
            
            List<Auditoria> auditorias = auditoriaRepo.obtenerTodas();
            auditorias = auditorias.stream()
                .filter(a -> personalSeleccionado.equals("Todos") || 
                    (a.getPersonal() != null && a.getPersonal().getNombre().equals(personalSeleccionado)))
                .filter(a -> fecha.contains("_") || 
                    (a.getFecha() != null && a.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).equals(fecha)))
                .filter(a -> accion.isEmpty() || 
                    (a.getAccion() != null && a.getAccion().toLowerCase().contains(accion.toLowerCase())))
                .filter(a -> detalle.isEmpty() || 
                    (a.getDetalle() != null && a.getDetalle().toLowerCase().contains(detalle.toLowerCase())))
                .collect(Collectors.toList());
                
            actualizarTablaAuditoria(tablaAuditoria, auditorias);
        });
        
        btnLimpiar.addActionListener(e -> {
            comboPersonal.setSelectedIndex(0);
            campoRealizado.setText("");
            campoAccion.setText("");
            campoDetalle.setText("");
            actualizarTablaAuditoria(tablaAuditoria);
        });
        
        // Cargar datos iniciales
        actualizarTablaAuditoria(tablaAuditoria);
        
        return panel;
    }
    
    private void actualizarTablaAuditoria(JTable tabla) {
        actualizarTablaAuditoria(tabla, auditoriaRepo.obtenerTodas());
    }
    
    private void actualizarTablaAuditoria(JTable tabla, List<Auditoria> auditorias) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        for (Auditoria auditoria : auditorias) {
            modelo.addRow(new Object[]{
                auditoria.getId(),
                auditoria.getPersonal() != null ? auditoria.getPersonal().getNombre() : "",
                auditoria.getFecha() != null ? auditoria.getFecha().format(formatter) : "",
                auditoria.getAccion(),
                auditoria.getDetalle()
            });
        }
    }

    private void initInasistenciasListeners() {
        tablaAusencias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablaInasistenciaClick(e);
            }
        });

        btnAgregarAusencia.addActionListener(e -> {
            auditoriaRepo.registrar("Registrar inasistencia", "El usuario " + personal.getNombre() + " ha registrado una inasistencia");
            JDialog dialog = new JDialog((JFrame) null, "Registrar Inasistencia", true);
            dialog.setContentPane(new RegistrarInasistencia(personalRepo).contentPane);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            DefaultTableModel inasistenciaTableModel = mapearModeloInasistencia(personalRepo.obtenerInasistenciasModelo());
            tablaAusencias.setModel(inasistenciaTableModel);
        });

        btnFiltrarAusencias.addActionListener(e -> {
            String personal = Objects.requireNonNull(comboFiltroPersonalAusencia.getSelectedItem()).toString();
            String fecha = txtFiltroFechaAusencia.getText();

            List<ModeloInasistencia> inasistenciasFiltradas = personalRepo.obtenerInasistenciasFiltradas(
                    personal.equals("Todos") ? null : personal,
                    fecha.isEmpty() ? null : fecha
            );

            DefaultTableModel inasistenciaTableModel = mapearModeloInasistencia(inasistenciasFiltradas);
            tablaAusencias.setModel(inasistenciaTableModel);
        });

        btnLimpiarFiltrosAusencias.addActionListener(e -> {
            comboFiltroPersonalAusencia.setSelectedIndex(0);
            txtFiltroFechaAusencia.setText("");

            DefaultTableModel inasistenciaTableModel = mapearModeloInasistencia(personalRepo.obtenerInasistenciasModelo());
            tablaAusencias.setModel(inasistenciaTableModel);
        });
    }

    private void tablaInasistenciaClick(MouseEvent e) {
        if (e.getClickCount() == 2 && tablaAusencias.getSelectedRow() != -1) {
            int selectedRow = tablaAusencias.getSelectedRow();
            auditoriaRepo.registrar("Modificar inasistencia", "El usuario ha modificado la inasistencia de un empleado con id " + tablaAusencias.getValueAt(selectedRow, 0).toString());

            Long id = Long.parseLong(tablaAusencias.getValueAt(selectedRow, 0).toString());
            String nuevoMotivo = JOptionPane.showInputDialog("Ingrese el nuevo motivo de la inasistencia");
            while (nuevoMotivo == null || nuevoMotivo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El motivo de la inasistencia no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
                nuevoMotivo = JOptionPane.showInputDialog("Ingrese el nuevo motivo de la inasistencia");
            }
            personalRepo.actualizarMotivoInasistencia(id, nuevoMotivo);

            DefaultTableModel personalTableModel = mapearModeloInasistencia(personalRepo.obtenerInasistenciasModelo());
            tablaAusencias.setModel(personalTableModel);
            cargarGraficos();
        }
    }

    private DefaultTableModel mapearModeloInasistencia(List<ModeloInasistencia> modeloInasistenciaList) {
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Fecha", "Motivo", "Personal"));

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

    private void setTableInasistenciaModel() {
        DefaultTableModel personalTableModel = mapearModeloInasistencia(personalRepo.obtenerInasistenciasModelo());
        tablaAusencias.setModel(personalTableModel);
    }

    private void initPagosListeners() {
        btnAgregarPago.addActionListener(e -> {
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

        tablaPagos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tablaPagos.getSelectedRow() != -1) {
                    auditoriaRepo.registrar("Imprimir factura", "Impresión de factura de pago directo");
                    Long id = (Long) tablaPagos.getValueAt(tablaPagos.getSelectedRow(), 0);
                    Pago pago = pagoRepo.obtenerPagoPorId(id);
                    ModeloRecibo modeloRecibo = new ModeloRecibo(pago);
                    GeneradorReportes.generarRecibo(modeloRecibo);
                }
            }
        });

        btnFiltrarPagos.addActionListener(e -> {
            String personal = Objects.requireNonNull(comboFiltroPersonalPago.getSelectedItem()).toString();
            String fecha = txtFiltroFechaPago.getText();
            Double montoMin = txtFiltroMontoMinPago.getText().isEmpty() ? null : Double.parseDouble(txtFiltroMontoMinPago.getText());
            Double montoMax = txtFiltroMontoMaxPago.getText().isEmpty() ? null : Double.parseDouble(txtFiltroMontoMaxPago.getText());

            List<Pago> pagosFiltrados = pagoRepo.obtenerPagosFiltrados(
                    personal.equals("Todos") ? null : personal,
                    fecha.isEmpty() ? null : fecha,
                    montoMin,
                    montoMax
            );

            DefaultTableModel pagosTableModel = mapearModeloPago(pagosFiltrados);
            tablaPagos.setModel(pagosTableModel);
        });

        btnLimpiarFiltrosPagos.addActionListener(e -> {
            comboFiltroPersonalPago.setSelectedIndex(0);
            txtFiltroFechaPago.setText("");
            txtFiltroMontoMinPago.setText("");
            txtFiltroMontoMaxPago.setText("");

            DefaultTableModel pagosTableModel = mapearModeloPago(pagoRepo.obtenerTodos());
            tablaPagos.setModel(pagosTableModel);
        });
    }

    private DefaultTableModel mapearModeloPago(List<Pago> pagoList) {
        Vector<String> columnNames = new Vector<>(Arrays.asList("ID", "Fecha", "Monto", "Descripción", "Obra", "Cantidad", "Pagadas"));

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

    private void setTablePagoModel() {
        DefaultTableModel pagosTableModel = mapearModeloPago(pagoRepo.obtenerTodos());
        tablaPagos.setModel(pagosTableModel);
    }
}
