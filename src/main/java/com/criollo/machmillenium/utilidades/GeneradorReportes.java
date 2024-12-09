package com.criollo.machmillenium.utilidades;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Obra;
import com.criollo.machmillenium.modelos.ModeloCliente;
import com.criollo.machmillenium.modelos.ModeloMaquinaria;
import com.criollo.machmillenium.modelos.ModeloMaterial;
import com.criollo.machmillenium.modelos.ModeloObra;
import com.criollo.machmillenium.modelos.ModeloPersonal;
import com.criollo.machmillenium.modelos.ModeloRecibo;
import com.criollo.machmillenium.modelos.ModeloSolicitudCompra;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.hibernate.Session;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GeneradorReportes {
    public static void generarReporteClientes(List<ModeloCliente> clientes) {
        try {
            InputStream reportStream = GeneradorReportes.class.getClassLoader().getResourceAsStream("reportes/clientes.jasper");
            if (reportStream == null) {
                throw new IllegalStateException("No se pudo encontrar el archivo de reporte: reportes/clientes.jasper");
            }

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

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(clientes);

                // Parameters for the report
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("Titulo", "Reporte de Clientes");
                
                // Load logo as stream
                InputStream logoStream = GeneradorReportes.class.getClassLoader().getResourceAsStream("main.jpg");
                if (logoStream != null) {
                    parameters.put("LogoPath", logoStream);
                }

                // Fill the report
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);

                // Export to PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void generarRecibo(ModeloRecibo recibo) {
        try {
            InputStream reportStream = GeneradorReportes.class.getClassLoader().getResourceAsStream("reportes/recibo.jasper");
            if (reportStream == null) {
                throw new IllegalStateException("No se pudo encontrar el archivo de reporte: reportes/recibo.jasper");
            }

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
                    // Fill report using the input stream
                    JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, new JREmptyDataSource());

                    // Export to PDF
                    JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
                } catch (JRException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void generarSolicitud(ModeloSolicitudCompra solicitud) {
        try {
            InputStream reportStream = GeneradorReportes.class.getClassLoader().getResourceAsStream("reportes/solicitud_compra.jasper");
            if (reportStream == null) {
                throw new IllegalStateException("No se pudo encontrar el archivo de reporte: reportes/solicitud_compra.jasper");
            }

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
                    // Fill report using the input stream
                    JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, new JREmptyDataSource());

                    // Export to PDF
                    JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
                } catch (JRException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void generarReporteMaquinarias(List<ModeloMaquinaria> maquinarias) {
        try {
            InputStream reportStream = GeneradorReportes.class.getClassLoader().getResourceAsStream("reportes/maquinarias.jasper");
            if (reportStream == null) {
                throw new IllegalStateException("No se pudo encontrar el archivo de reporte: reportes/maquinarias.jasper");
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar reporte de maquinarias");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
            fileChooser.setSelectedFile(new File("reporte_maquinarias.pdf"));
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".pdf")) {
                    file = new File(file.getParent(), file.getName() + ".pdf");
                }

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(maquinarias);
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("Titulo", "Reporte de Maquinarias");

                // Fill report using the input stream
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);

                // Export to PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void generarReporteObras(List<ModeloObra> obras) {
        try {
            InputStream reportStream = GeneradorReportes.class.getClassLoader().getResourceAsStream("reportes/obras.jasper");
            if (reportStream == null) {
                throw new IllegalStateException("No se pudo encontrar el archivo de reporte: reportes/obras.jasper");
            }

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

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(obras);
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("Titulo", "Reporte de Obras");

                // Fill report using the input stream
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);

                // Export to PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void generarReporteMateriales(List<ModeloMaterial> materiales) {
        try {
            InputStream reportStream = GeneradorReportes.class.getClassLoader().getResourceAsStream("reportes/materiales.jasper");
            if (reportStream == null) {
                throw new IllegalStateException("No se pudo encontrar el archivo de reporte: reportes/materiales.jasper");
            }

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

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(materiales);
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("Titulo", "Reporte de Materiales");

                // Fill report using the input stream
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);

                // Export to PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void generarReportePersonal(List<ModeloPersonal> personal) {
        try {
            InputStream reportStream = GeneradorReportes.class.getClassLoader().getResourceAsStream("reportes/personal_list.jasper");
            if (reportStream == null) {
                throw new IllegalStateException("No se pudo encontrar el archivo de reporte: reportes/personal_list.jasper");
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar reporte de personal");
            fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
            fileChooser.setSelectedFile(new File("reporte_personal.pdf"));
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".pdf")) {
                    file = new File(file.getParent(), file.getName() + ".pdf");
                }

                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(personal);
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("Titulo", "Reporte de Personal");

                // Fill report using the input stream
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);

                // Export to PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
