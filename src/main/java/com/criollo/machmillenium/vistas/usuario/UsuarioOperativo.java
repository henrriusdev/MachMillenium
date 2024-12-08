package com.criollo.machmillenium.vistas.usuario;

import com.criollo.machmillenium.entidades.Obra;
import com.criollo.machmillenium.entidades.Pago;
import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.TipoInsumo;
import com.criollo.machmillenium.modelos.ModeloRecibo;
import com.criollo.machmillenium.modelos.ModeloSolicitudCompra;
import com.criollo.machmillenium.repos.AuditoriaRepo;
import com.criollo.machmillenium.repos.ObraRepo;
import com.criollo.machmillenium.repos.PagoRepo;
import com.criollo.machmillenium.repos.TipoInsumoRepo;
import com.criollo.machmillenium.utilidades.GeneradorGraficos;
import com.criollo.machmillenium.utilidades.GeneradorReportes;
import com.criollo.machmillenium.utilidades.Utilidades;
import com.criollo.machmillenium.vistas.emergentes.clientes.Cuotas;
import com.criollo.machmillenium.vistas.emergentes.clientes.RegistrarPago;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UsuarioOperativo {
    public JPanel panel;
    private JTable tablaObras;
    private JPanel inicio;
    private JButton verGráficosButton;
    private JButton registrarPagoButton;
    private JTable tablaDirectos;
    private JButton imprimir;
    private JTable tablaCuotas;
    private JButton buttonCambiar;
    private JButton btnGenerar;
    private final ObraRepo obraRepo;
    private final AuditoriaRepo auditoriaRepo;
    private final PagoRepo pagoRepo;
    private Personal personal;

    public UsuarioOperativo(Personal personal) {
        this.obraRepo = new ObraRepo();
        this.auditoriaRepo = new AuditoriaRepo(personal.getNombre());
        this.pagoRepo = new PagoRepo();
        this.personal = personal;
        auditoriaRepo.registrar("Ingreso", "Ingreso al módulo de usuario operativo");

        Utilidades.cambiarClaveOriginal(personal.getClave(), personal.getId(), true);
        setTableObraModel();
        setTablePagoDirectoModel();
        setTablePagoCuotasModel();

        List<Obra> obras = obraRepo.obtenerObras();
        String[] estados = obras.stream().map(Obra::getEstado).toArray(String[]::new);
        double[] cantidadObras = obras.stream().collect(Collectors.groupingBy(Obra::getEstado, Collectors.counting()))
                .values().stream().mapToDouble(Long::doubleValue).toArray();
        estados = Arrays.stream(estados).distinct().toArray(String[]::new);
        ChartPanel chartPanel = GeneradorGraficos.generarGraficoPastel("Obras por estado", estados, cantidadObras, 360, 250);

        String[] tiposObra = obras.stream().map(obra -> obra.getTipoObra().getNombre()).toArray(String[]::new);
        double[] cantidadTiposObra = obras.stream().collect(Collectors.groupingBy(obra -> obra.getTipoObra().getNombre(), Collectors.counting()))
                .values().stream().mapToDouble(Long::doubleValue).toArray();
        tiposObra = Arrays.stream(tiposObra).distinct().toArray(String[]::new);
        ChartPanel chartPanelTiposObra = GeneradorGraficos.generarGraficoPastel("Obras por tipo", tiposObra, cantidadTiposObra, 360, 250);
        
        String[] nombresObras = obras.stream().map(Obra::getNombre).toArray(String[]::new);
        double[] costosObras = obras.stream().mapToDouble(obra -> obra.getPresupuesto().getCosto()).toArray();
        ChartPanel chartPanelCostos = GeneradorGraficos.generarGraficoBarras("Costos de obras", "Obras", "Costo", nombresObras, costosObras, 360, 250);

        String[] nombresClientes = obras.stream().map(obra -> obra.getPresupuesto().getCliente().getNombre()).toArray(String[]::new);
        double[] cantidadObrasClientes = obras.stream().collect(Collectors.groupingBy(obra -> obra.getPresupuesto().getCliente().getNombre(), Collectors.counting()))
                .values().stream().mapToDouble(Long::doubleValue).toArray();
        nombresClientes = Arrays.stream(nombresClientes).distinct().toArray(String[]::new);
        ChartPanel chartPanelClientes = GeneradorGraficos.generarGraficoPastel("Obras por cliente", nombresClientes, cantidadObrasClientes, 360, 250);

        inicio.add(chartPanel);
        inicio.add(chartPanelTiposObra);
        inicio.add(chartPanelCostos);
        inicio.add(chartPanelClientes);
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
        buttonCambiar.addActionListener(e -> {
            auditoriaRepo.registrar("Cambiar clave", "Ingreso al formulario de cambio de clave");
            Utilidades.cambiarClaveOriginal(personal.getClave(), personal.getId(), false);
        });
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
}
