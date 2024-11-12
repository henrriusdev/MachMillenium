package com.criollo.machmillenium.vistas.usuario;

import com.criollo.machmillenium.entidades.Obra;
import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.repos.AuditoriaRepo;
import com.criollo.machmillenium.repos.ObraRepo;
import com.criollo.machmillenium.utilidades.GeneradorGraficos;
import com.criollo.machmillenium.utilidades.Utilidades;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public class UsuarioOperativo {
    public JPanel panel;
    private JTable tablaObras;
    private JPanel inicio;
    private JButton verGráficosButton;
    private JButton imprimirFacturaButton;
    private JButton registrarPagoButton;
    private JTable table1;
    private final ObraRepo obraRepo;
    private final AuditoriaRepo auditoriaRepo;

    public UsuarioOperativo(Personal personal) {
        this.obraRepo = new ObraRepo();
        this.auditoriaRepo = new AuditoriaRepo(personal.getNombre());
        auditoriaRepo.registrar("Ingreso", "Ingreso al módulo de usuario operativo");

        Utilidades.cambiarClaveOriginal(personal.getClave(), personal.getId(), true);
        setTableObraModel();

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
}
