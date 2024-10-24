package com.criollo.machmillenium.utilidades;

import com.criollo.machmillenium.HibernateUtil;
import net.sf.jasperreports.engine.*;
import org.hibernate.Session;


import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.sql.Connection;

public class GeneradorReportes {
    public static void generarReporteClientes() {
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

    public static void generarReportePersonal(){
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
}
