/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.criollo.machmillenium;

import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.vistas.Inicio;
import com.formdev.flatlaf.FlatLightLaf;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.*;

/**
 *
 * @author henrrius
 */
public class MachMillenium {

    public static void main(String[] args) {
        // Esto garantiza que toda la interfaz gráfica se ejecute en el hilo de despacho de eventos
        SwingUtilities.invokeLater(() -> {
            // Crear el JFrame
            JFrame frame = new JFrame("MachMillenium");

            // Establecer el comportamiento para el botón de cerrar
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Crear la instancia del JPanel Inicio
            Inicio inicio;
            try {
                JOptionPane.showConfirmDialog(frame, "Download the excel for the first time, is needed to register the personal", "Download Excel", JOptionPane.OK_CANCEL_OPTION);
                UIManager.setLookAndFeel(new FlatLightLaf());
                inicio = new Inicio();
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }
            // Agregar el JPanel al JFrame
            frame.getContentPane().add(inicio.panel);

            // Ajustar el tamaño del JFrame según el contenido
            frame.pack();

            verificarPersonal();
            // Hacer que la ventana sea visible
            frame.setVisible(true);
        });
    }

    private static void verificarPersonal() {
        PersonalRepo personalRepository = new PersonalRepo();
        Long count = personalRepository.contar();
        if (count == 0) {
            int option = JOptionPane.showConfirmDialog(null, "No hay personal registrado. ¿Desea descargar un archivo de Excel para agregar personal?", "No hay personal", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                descargarPlantilla();
                subirArchivo();
            }
        }
    }

    private static Workbook crearPlantilla() {
        Workbook workbook = new XSSFWorkbook();
        Sheet personalSheet = workbook.createSheet("Personal");
        personalSheet.createRow(0).createCell(0).setCellValue("Nombre");
        personalSheet.createRow(0).createCell(1).setCellValue("Cedula");
        personalSheet.createRow(0).createCell(2).setCellValue("Especialidad");
        personalSheet.createRow(0).createCell(3).setCellValue("Fijo");
        Sheet especialidadSheet = workbook.createSheet("Especialidad");
        especialidadSheet.createRow(0).createCell(0).setCellValue("Nombre");
        return workbook;
    }

    private static void guardarPlantilla(Workbook workbook) {
        try {
            FileOutputStream fileOut = new FileOutputStream("plantilla.xlsx");
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar la plantilla: " + e.getMessage());
        }
    }

    private static File seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Excel", "xlsx"));
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    private static Workbook leerArchivo(File file) {
        try {
            return new XSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }

    private static boolean verificarEspecialidad(Workbook workbook) {
        Sheet personalSheet = workbook.getSheet("Personal");
        Sheet especialidadSheet = workbook.getSheet("Especialidad");
        for (Row row : personalSheet) {
            String especialidad = row.getCell(2).getStringCellValue();
            boolean existe = false;
            for (Row especialidadRow : especialidadSheet) {
                if (especialidadRow.getCell(0).getStringCellValue().equals(especialidad)) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                JOptionPane.showMessageDialog(null, "La especialidad no existe en la hoja de especialidades.");
                return false;
            }
        }
        return true;
    }

    private static void descargarPlantilla() {
        Workbook workbook = crearPlantilla();
        guardarPlantilla(workbook);
    }

    private static void subirArchivo() {
        File file = seleccionarArchivo();
        if (file != null) {
            Workbook workbook = leerArchivo(file);
            if (workbook != null) {
                if (verificarEspecialidad(workbook)) {
                    // Insertar los datos en el sistema
                }
            }
        }
    }
}
