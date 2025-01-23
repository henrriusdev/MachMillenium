/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.criollo.machmillenium;

import com.criollo.machmillenium.enums.Privilegios;
import com.criollo.machmillenium.entidades.*;
import com.criollo.machmillenium.repos.EspecialidadRepo;
import com.criollo.machmillenium.repos.ObraRepo;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.repos.PrivilegioRepo;
import com.criollo.machmillenium.repos.PreguntasSeguridadRepo;
import com.criollo.machmillenium.repos.RolRepo;
import com.criollo.machmillenium.vistas.Inicio;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MachMillenium {

    public static void main(String[] args) {
        // Esto garantiza que toda la interfaz gráfica se ejecute en el hilo de despacho de eventos
        SwingUtilities.invokeLater(() -> {
            // Crear el JFrame
            JFrame frame = new JFrame("MachMillenium");

            // Establecer el comportamiento para el botón de cerrar
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Crear la instancia del JPanel Inicio
            CustomFlatLaf.setup();
            Inicio inicio = new Inicio(frame);
            // Agregar el JPanel al JFrame
            frame.getContentPane().add(inicio.panelPrincipal);
            // Ajustar el tamaño del JFrame según el contenido
            frame.pack();

            insertarRoles();
            insertarTipoObra();
            inicializarPrivilegios();
            verificarPersonal();
            // Hacer que la ventana sea visible
            frame.setVisible(true);

            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            JFrame.setDefaultLookAndFeelDecorated(true);
            frame.getRootPane().putClientProperty("JRootPane.titleBarBackground", new Color(214,129,67));
            frame.getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.white);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
                personal.setCorreo(row.getCell(4).getStringCellValue().toLowerCase());
                String claveProtegida = BCrypt.hashpw("12345678", BCrypt.gensalt());
                personal.setClave(claveProtegida);

                String nombreRol = row.getCell(5).getStringCellValue();
                Rol rol = rolRepository.obtenerPorNombre(nombreRol);
                personal.setRol(rol);

                // Set fecha fin contrato from column G
                Cell fechaCell = row.getCell(6);
                if (fechaCell != null) {
                    try {
                        LocalDateTime fechaFin;
                        if (fechaCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(fechaCell)) {
                            // If it's a date in Excel format
                            fechaFin = fechaCell.getLocalDateTimeCellValue();
                        } else if (fechaCell.getCellType() == CellType.STRING) {
                            // If it's a string in DD/MM/YYYY format
                            String fechaStr = fechaCell.getStringCellValue();
                            try {
                                LocalDate fecha = LocalDate.parse(fechaStr, formatter);
                                fechaFin = fecha.atStartOfDay();
                            } catch (DateTimeParseException e) {
                                throw new IllegalArgumentException("La fecha debe estar en formato DD/MM/AAAA. Ejemplo: 31/12/2024");
                            }
                        } else {
                            throw new IllegalArgumentException("Formato de fecha no válido en la fila " + (row.getRowNum() + 1));
                        }
                        personal.setFechaTerminoContrato(fechaFin);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, 
                            "Error en la fecha de fin de contrato en la fila " + (row.getRowNum() + 1) + ": " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                        continue; // Skip this row and continue with the next
                    }
                }

                // Insertar el personal
                personal = personalRepository.insertar(personal);

                String pregunta1 = row.getCell(7).getStringCellValue();
                String respuesta1 = row.getCell(8).getStringCellValue();
                String pregunta2 = row.getCell(9).getStringCellValue();
                String respuesta2 = row.getCell(10).getStringCellValue();
                String pregunta3 = row.getCell(11).getStringCellValue();
                String respuesta3 = row.getCell(12).getStringCellValue();

                // Crear preguntas de seguridad por defecto
                PreguntasSeguridad preguntas = new PreguntasSeguridad();
                preguntas.setPersonal(personal);
                preguntas.setPregunta1(pregunta1);
                preguntas.setRespuesta1(respuesta1);
                preguntas.setPregunta2(pregunta2);
                preguntas.setRespuesta2(respuesta2);
                preguntas.setPregunta3(pregunta3);
                preguntas.setRespuesta3(respuesta3);
                
                PreguntasSeguridadRepo preguntasSeguridadRepo = new PreguntasSeguridadRepo();
                preguntasSeguridadRepo.insertar(preguntas);
            }
        }
    }

    private static void insertarRoles() {
        RolRepo rolRepository = new RolRepo();
        if (rolRepository.contar() == 0) {
            rolRepository.insertar(new Rol("Administrador"));
            rolRepository.insertar(new Rol("Gerente de Proyecto"));
            rolRepository.insertar(new Rol("Usuario Operativo"));
            rolRepository.insertar(new Rol("Trabajador"));
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

    private static void inicializarPrivilegios() {
        PrivilegioRepo privilegioRepo = new PrivilegioRepo();
        
        for (Privilegios privilegio : Privilegios.values()) {
            crearPrivilegioSiNoExiste(privilegioRepo, privilegio.name(), privilegio.getDescripcion());
        }
        
        privilegioRepo.cerrarSesion();
    }
    
    private static void crearPrivilegioSiNoExiste(PrivilegioRepo repo, String nombre, String descripcion) {
        if (repo.obtenerPorNombre(nombre) == null) {
            Privilegio privilegio = new Privilegio();
            privilegio.setNombre(nombre);
            privilegio.setDescripcion(descripcion);
            repo.guardar(privilegio);
        }
    }
}
