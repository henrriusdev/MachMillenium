package com.criollo.machmillenium.utilidades;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.modelos.ModeloCliente;
import com.criollo.machmillenium.modelos.ModeloRecibo;
import com.criollo.machmillenium.modelos.ModeloSolicitudCompra;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.hibernate.Session;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GeneradorReportes {
    public static void generarReporteClientes(List<ModeloCliente> clientes) {
        try (Session session = HibernateUtil.getSession()) {
            String reporte = GeneradorReportes.class.getClassLoader().getResource("reportes/clientes.jasper").getPath();
            // show swing dialog for show where to save the report
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar reporte de clientes");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
            fileChooser.setSelectedFile(new File("reporte_clientes.pdf"));
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".pdf")) {
                    file = new File(file.getParent(), file.getName() + ".pdf");
                }
                // generate the report

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(clientes);

            // Parameters for the report
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("Titulo", "Reporte de Clientes");
            parameters.put("LogoPath", GeneradorReportes.class.getClassLoader().getResource("main.jpg").getPath());

            // Fill the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, parameters, dataSource);

            // Exportar el reporte a un archivo PDF
            JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReportePersonal() {
        try (Session session = HibernateUtil.getSession()) {
            String reporte = GeneradorReportes.class.getClassLoader().getResource("reportes/personal.jasper").getPath();
            // show swing dialog for show where to save the report
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar reporte de clientes");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
            fileChooser.setSelectedFile(new File("reporte_personal.pdf"));
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".pdf")) {
                    file = new File(file.getParent(), file.getName() + ".pdf");
                }
                // generate the report

                Connection connection = session.doReturningWork(conn -> conn);

                // Llenar el reporte utilizando la conexión obtenida
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, connection);

                // Exportar el reporte a un archivo PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteMaquinaria() {
        try (Session session = HibernateUtil.getSession()) {
            String reporte = GeneradorReportes.class.getClassLoader().getResource("reportes/maquinarias.jasper").getPath();
            // show swing dialog for show where to save the report
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar reporte de maquinaria");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
            fileChooser.setSelectedFile(new File("reporte_maquinaria.pdf"));
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".pdf")) {
                    file = new File(file.getParent(), file.getName() + ".pdf");
                }
                // generate the report

                Connection connection = session.doReturningWork(conn -> conn);

                // Llenar el reporte utilizando la conexión obtenida
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, connection);

                // Exportar el reporte a un archivo PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteMateriales() {
        try (Session session = HibernateUtil.getSession()) {
            String reporte = GeneradorReportes.class.getClassLoader().getResource("reportes/materiales.jasper").getPath();
            // show swing dialog for show where to save the report
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar reporte de materiales");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
            fileChooser.setSelectedFile(new File("reporte_materiales.pdf"));
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".pdf")) {
                    file = new File(file.getParent(), file.getName() + ".pdf");
                }
                // generate the report

                Connection connection = session.doReturningWork(conn -> conn);

                // Llenar el reporte utilizando la conexión obtenida
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, connection);

                // Exportar el reporte a un archivo PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarReporteObras() {
        try (Session session = HibernateUtil.getSession()) {
            String reporte = GeneradorReportes.class.getClassLoader().getResource("reportes/obras.jasper").getPath();
            // show swing dialog for show where to save the report
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar reporte de obras");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
            fileChooser.setSelectedFile(new File("reporte_obras.pdf"));
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".pdf")) {
                    file = new File(file.getParent(), file.getName() + ".pdf");
                }
                // generate the report

                Connection connection = session.doReturningWork(conn -> conn);

                // Llenar el reporte utilizando la conexión obtenida
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, connection);

                // Exportar el reporte a un archivo PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void generarRecibo(ModeloRecibo recibo) {
        String reporte = Objects.requireNonNull(GeneradorReportes.class.getClassLoader().getResource("reportes/recibo.jasper")).getPath();
        // show swing dialog for show where to save the report
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar recibo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
        fileChooser.setSelectedFile(new File("recibo.pdf"));
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".pdf")) {
                file = new File(file.getParent(), file.getName() + ".pdf");
            }
            // generate the report

            Map<String, Object> params = new HashMap<>();
            params.put("IB_NombreCliente", recibo.getNombreCliente());
            params.put("IB_Correo", recibo.getCorreoCliente());
            params.put("IB_Telefono", recibo.getTelefonoCliente());
            params.put("IB_Cedula", recibo.getCedulaCliente());
            params.put("IB_TipoObra", recibo.getTipoObra());
            params.put("IB_NombreObra", recibo.getNombreObra());
            params.put("IB_Descripcion", recibo.getDescripcionObra());
            params.put("IB_Estado", recibo.getEstadoObra());
            params.put("IB_Costo", recibo.getCostoObra());
            params.put("IB_TiempoEstimado", recibo.getTiempoEstimadoObra());
            params.put("IB_Direccion", recibo.getDireccionObra());

            params.put("IDS_Metodo", recibo.getMetodoPago());
            params.put("IDS_Monto", recibo.getMontoPago());
            params.put("IDS_PorCuotas", recibo.getPorCuotas());
            params.put("IDS_NumeroCuota", recibo.getNumeroCuota());
            params.put("IDS_CuotasPorPagar", recibo.getCuotasPorPagar());
            params.put("IDS_MontoPorPagar", recibo.getMontoPorPagar());

            params.put("H_fecha", recibo.getFecha());

            try {
                // Llenar el reporte utilizando la conexión obtenida
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, params, new JREmptyDataSource());

                // Exportar el reporte a un archivo PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void generarSolicitud(ModeloSolicitudCompra solicitud) {
        String reporte = Objects.requireNonNull(GeneradorReportes.class.getClassLoader().getResource("reportes/solicitud_compra.jasper")).getPath();
        // show swing dialog for show where to save the report
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar solicitud de compra del material " + solicitud.getNombreMaterial());
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
        fileChooser.setSelectedFile(new File("solicitud.pdf"));
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".pdf")) {
                file = new File(file.getParent(), file.getName() + ".pdf");
            }
            // generate the report

            Map<String, Object> params = new HashMap<>();
            params.put("fecha", solicitud.getFecha());
            params.put("solicitante", solicitud.getSolicitante());
            params.put("cargo", solicitud.getCargo());
            params.put("nombreMaterial", solicitud.getNombreMaterial());
            params.put("tipo", solicitud.getTipoMaterial());
            params.put("presentacion", solicitud.getPresentacion());
            params.put("cantidadRequerida", solicitud.getCantidad());
            params.put("justificacion", solicitud.getJustificacion());
            params.put("fechaLimite", solicitud.getFechaLimite());

            try {
                // Llenar el reporte utilizando la conexión obtenida
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, params, new JREmptyDataSource());

                // Exportar el reporte a un archivo PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
