/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.criollo.machmillenium;

import com.criollo.machmillenium.entidades.Especialidad;
import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.Rol;
import com.criollo.machmillenium.entidades.TipoObra;
import com.criollo.machmillenium.repos.EspecialidadRepo;
import com.criollo.machmillenium.repos.ObraRepo;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.repos.RolRepo;
import com.criollo.machmillenium.vistas.Inicio;
import com.formdev.flatlaf.FlatLightLaf;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

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
                UIManager.setLookAndFeel(new FlatLightLaf());
                inicio = new Inicio(frame);
            } catch (UnsupportedLookAndFeelException e) {
                throw new RuntimeException(e);
            }
            // Agregar el JPanel al JFrame
            frame.getContentPane().add(inicio.panelPrincipal);

            // Ajustar el tamaño del JFrame según el contenido
            frame.pack();

            verificarPersonal();
            // Hacer que la ventana sea visible
            frame.setVisible(true);

            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    if (frame.getTitle().equals("MachMillenium")) {
                        System.exit(0);
                    }

                    int option = JOptionPane.showConfirmDialog(frame, "¿Está seguro que desea cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        frame.setContentPane(new Inicio(frame).panelPrincipal);
                        frame.setTitle("MachMillenium");
                        frame.pack();
                        frame.setVisible(true);
                        frame.repaint();
                    }
                }
            });
        });
    }

    private static void verificarPersonal() {
        PersonalRepo personalRepository = new PersonalRepo();
        insertarRoles();
        insertarTipoObra();
        if (personalRepository.contar() == 0) {
            int option = JOptionPane.showConfirmDialog(null, "No hay personal registrado. ¿Desea descargar un archivo de Excel para agregar personal?", "No hay personal", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                descargarPlantilla();
                subirArchivo();
            } else {
                subirArchivo();
            }
        }
    }

    private static Workbook crearPlantilla() {
        try (InputStream inputStream = MachMillenium.class.getClassLoader().getResourceAsStream("plantilla_carga_masiva.xlsx")) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            return workbook;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar la plantilla: " + e.getMessage());
            return null;
        }
    }

    private static void guardarPlantilla(Workbook workbook) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar plantilla");
        fileChooser.setSelectedFile(new File("plantilla.xlsx"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Excel", "xlsx"));
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith(".xlsx")) {
                file = new File(file.getParent(), file.getName() + ".xlsx");
            }
            if (file.exists()) {
                int option = JOptionPane.showConfirmDialog(null, "El archivo ya existe. ¿Desea sobrescribirlo?", "Archivo existente", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    try {
                        FileOutputStream fileOut = new FileOutputStream(file);
                        workbook.write(fileOut);
                        fileOut.close();
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Error al guardar la plantilla: " + e.getMessage());
                    }
                }
            } else {
                try {
                    FileOutputStream fileOut = new FileOutputStream(file);
                    workbook.write(fileOut);
                    fileOut.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error al guardar la plantilla: " + e.getMessage());
                }
            }
        }
    }

    private static File seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar plantilla de cargue masivo");
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

    private static void descargarPlantilla() {
        Workbook workbook = crearPlantilla();
        guardarPlantilla(workbook);
    }

    private static void subirArchivo() {
        File file = seleccionarArchivo();
        if (file != null) {
            Workbook workbook = leerArchivo(file);
            if (workbook != null) {
                    insertarPersonal(workbook);

            }
        }
    }

    private static void insertarPersonal(Workbook workbook) {
        PersonalRepo personalRepository = new PersonalRepo();
        EspecialidadRepo especialidadRepository = new EspecialidadRepo();
        RolRepo rolRepository = new RolRepo();
        Sheet personalSheet = workbook.getSheet("Personal");
        for (Row row : personalSheet) {
            if (row.getRowNum() > 0) {
                Personal personal = new Personal();
                personal.setNombre(row.getCell(0).getStringCellValue());
                personal.setCedula(row.getCell(1).getStringCellValue());

                Especialidad especialidad = new Especialidad();
                especialidad.setNombre(row.getCell(2).getStringCellValue());
                especialidad = especialidadRepository.encuentraOInserta(especialidad.getNombre());
                personal.setEspecialidad(especialidad);

                personal.setFijo(row.getCell(3).getBooleanCellValue());
                personal.setCorreo(row.getCell(4).getStringCellValue());
                String claveProtegida = BCrypt.hashpw("12345678", BCrypt.gensalt());
                personal.setClave(claveProtegida);

                String nombreRol = row.getCell(5).getStringCellValue();
                Rol rol = rolRepository.obtenerPorNombre(nombreRol);
                personal.setRol(rol);

                personalRepository.insertar(personal);
            }
        }
    }

    private static void insertarRoles() {
        RolRepo rolRepository = new RolRepo();
        if (rolRepository.contar() == 0) {
            rolRepository.insertar(new Rol("Administrador"));
            rolRepository.insertar(new Rol("Gerente de Proyecto"));
            rolRepository.insertar(new Rol("Usuario Operativo"));
        }

    }

    private static void insertarTipoObra() {
        ObraRepo tipoObraRepo = new ObraRepo();
        if (tipoObraRepo.contarTipoObra() == 0) {
            tipoObraRepo.insertarTipoObra(new TipoObra("Construcción"));
            tipoObraRepo.insertarTipoObra(new TipoObra("Remodelación"));
            tipoObraRepo.insertarTipoObra(new TipoObra("Mantenimiento"));
            tipoObraRepo.insertarTipoObra(new TipoObra("Demolición"));
            tipoObraRepo.insertarTipoObra(new TipoObra("Rehabilitación"));
            tipoObraRepo.insertarTipoObra(new TipoObra("Ampliación"));
            tipoObraRepo.insertarTipoObra(new TipoObra("Reparación"));
        }
    }
}
