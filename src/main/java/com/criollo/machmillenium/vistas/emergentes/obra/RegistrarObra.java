package com.criollo.machmillenium.vistas.emergentes.obra;

import com.criollo.machmillenium.entidades.*;
import com.criollo.machmillenium.repos.ObraRepo;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegistrarObra {
    private JTextField campoNombre;
    private JTextArea campoDescripcion;
    private JComboBox<String> comboboxEstado;
    private JFormattedTextField campoArea;
    private JComboBox<String> comboboxPresupuesto;
    private JComboBox<String> comboboxTipoObra;
    private JButton cerrarButton;
    private JButton agregarButton;
    private JList<String> listaMateriales;
    private JList<String> listaMaquinaria;
    private JList<String> listaPersonal;
    public JPanel panel;

    private static final int MAX_NOMBRE_LENGTH = 100;
    private static final int MAX_DESCRIPCION_LENGTH = 500;
    private static final double MIN_AREA = 1.0;
    private static final double MAX_AREA = 1000000.0;
    private static final Pattern NOMBRE_PATTERN = Pattern.compile("^[A-Za-zÁáÉéÍíÓóÚúÑñ0-9\\s-]{2,}$");

    public RegistrarObra(ObraRepo obraRepo) {
        setupUI();
        setupListeners(obraRepo);
    }

    private void setupUI() {
        // Configurar límites de caracteres
        campoNombre.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) 
                    throws javax.swing.text.BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= MAX_NOMBRE_LENGTH) {
                    super.insertString(offs, str, a);
                }
            }
        });

        campoDescripcion.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) 
                    throws javax.swing.text.BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= MAX_DESCRIPCION_LENGTH) {
                    super.insertString(offs, str, a);
                }
            }
        });

        // Configurar tooltips
        campoNombre.setToolTipText("Ingrese el nombre de la obra (letras, números, espacios y guiones)");
        campoDescripcion.setToolTipText("Ingrese la descripción de la obra (máximo " + MAX_DESCRIPCION_LENGTH + " caracteres)");
        campoArea.setToolTipText("Ingrese el área en metros cuadrados (entre " + MIN_AREA + " y " + MAX_AREA + ")");
        comboboxEstado.setToolTipText("Seleccione el estado de la obra");
        comboboxPresupuesto.setToolTipText("Seleccione el presupuesto asignado");
        comboboxTipoObra.setToolTipText("Seleccione el tipo de obra");
        listaMateriales.setToolTipText("Seleccione los materiales necesarios");
        listaMaquinaria.setToolTipText("Seleccione la maquinaria necesaria");
        listaPersonal.setToolTipText("Seleccione el personal asignado");

        // Configurar selección múltiple para las listas
        listaMateriales.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaMaquinaria.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaPersonal.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    private void setupListeners(ObraRepo obraRepo) {
        // Configurar eventos de teclado para el campo nombre
        campoNombre.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c) && c != '-' && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });

        cerrarButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(panel);
            if (window != null) {
                window.dispose();
            }
        });

        agregarButton.addActionListener(e -> {
            try {
                if (!validarCampos()) {
                    return;
                }

                // Obtener y validar datos básicos
                String nombre = campoNombre.getText().trim();
                String descripcion = campoDescripcion.getText().trim();
                String estado = (String) comboboxEstado.getSelectedItem();
                double area = Double.parseDouble(campoArea.getText().replace(",", ""));

                // Validar selección de presupuesto
                if (comboboxPresupuesto.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(panel,
                        "Debe seleccionar un presupuesto",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                    comboboxPresupuesto.requestFocus();
                    return;
                }

                // Validar selección de tipo de obra
                if (comboboxTipoObra.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(panel,
                        "Debe seleccionar un tipo de obra",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                    comboboxTipoObra.requestFocus();
                    return;
                }

                // Validar selección de materiales
                if (listaMateriales.getSelectedValuesList().isEmpty()) {
                    JOptionPane.showMessageDialog(panel,
                        "Debe seleccionar al menos un material",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                    listaMateriales.requestFocus();
                    return;
                }

                // Validar selección de personal
                if (listaPersonal.getSelectedValuesList().isEmpty()) {
                    JOptionPane.showMessageDialog(panel,
                        "Debe seleccionar al menos un personal",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                    listaPersonal.requestFocus();
                    return;
                }

                // Crear y guardar la obra
                Obra obra = new Obra();
                obra.setNombre(nombre);
                obra.setDescripcion(descripcion);
                obra.setEstado(estado);
                obra.setArea(area);

                String descripcionPresupuesto = ((String) Objects.requireNonNull(comboboxPresupuesto.getSelectedItem())).split(" -- ")[0];

                obra.setPresupuesto(obraRepo.obtenerPresupuestoPorDescripcion(descripcionPresupuesto));
                obra.setTipoObra(obraRepo.obtenerTipoObraPorNombre((String) comboboxTipoObra.getSelectedItem()));

                try {
                    obra = obraRepo.insertarObra(obra);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                        "Error al guardar la obra: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    return;
                }

                // Procesar materiales seleccionados
                Obra finalObra = obra;
                try {
                    List<ObraMaterial> materiales = listaMateriales.getSelectedValuesList().stream().map(material -> {
                        String[] materialParts = material.split(" -- ");
                        Long cantidad = null;
                        while (cantidad == null) {
                            String input = JOptionPane.showInputDialog(panel,
                                "Cantidad de " + materialParts[0] + ":",
                                "Ingrese cantidad",
                                JOptionPane.QUESTION_MESSAGE);
                            
                            if (input == null) {
                                return null; // Usuario canceló
                            }
                            
                            try {
                                cantidad = Long.parseLong(input);
                                if (cantidad <= 0) {
                                    JOptionPane.showMessageDialog(panel,
                                        "La cantidad debe ser mayor a 0",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                    cantidad = null;
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(panel,
                                    "Por favor ingrese un número válido",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        return new ObraMaterial(finalObra, obraRepo.obtenerMaterialPorNombre(materialParts[0]), cantidad);
                    }).filter(Objects::nonNull).toList();

                    materiales.forEach(obraRepo::insertarObraMaterial);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                        "Error al registrar los materiales: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    return;
                }

                // Procesar personal seleccionado
                try {
                    List<ObraPersonal> personal = listaPersonal.getSelectedValuesList().stream()
                        .map(personalString -> {
                            String[] personalParts = personalString.split(" -- ");
                            return new ObraPersonal(finalObra, obraRepo.obtenerPersonalPorNombre(personalParts[0]));
                        })
                        .toList();
                    personal.forEach(obraRepo::insertarObraPersonal);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                        "Error al registrar el personal: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    return;
                }

                // Procesar maquinaria seleccionada
                try {
                    List<ObraMaquinaria> maquinaria = listaMaquinaria.getSelectedValuesList().stream()
                        .map(maquinariaString -> {
                            String[] maquinariaParts = maquinariaString.split(" -- ");
                            return new ObraMaquinaria(finalObra, obraRepo.obtenerMaquinariaPorNombre(maquinariaParts[0]));
                        })
                        .toList();
                    maquinaria.forEach(obraRepo::insertarObraMaquinaria);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                        "Error al registrar la maquinaria: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                    return;
                }

                JOptionPane.showMessageDialog(panel,
                    "Obra registrada correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                
                Window window = SwingUtilities.getWindowAncestor(panel);
                if (window != null) {
                    window.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                    "Error inesperado: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }

    private boolean validarCampos() {
        // Validar nombre
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty() || !NOMBRE_PATTERN.matcher(nombre).matches()) {
            JOptionPane.showMessageDialog(panel,
                "El nombre debe contener letras, números, espacios o guiones, y tener al menos 2 caracteres",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            campoNombre.requestFocus();
            return false;
        }

        // Validar descripción
        String descripcion = campoDescripcion.getText().trim();
        if (descripcion.isEmpty() || descripcion.length() < 10) {
            JOptionPane.showMessageDialog(panel,
                "La descripción debe tener al menos 10 caracteres",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            campoDescripcion.requestFocus();
            return false;
        }

        // Validar área
        try {
            double area = Double.parseDouble(campoArea.getText().replace(",", ""));
            if (area < MIN_AREA || area > MAX_AREA) {
                JOptionPane.showMessageDialog(panel,
                    String.format("El área debe estar entre %.2f y %.2f metros cuadrados",
                        MIN_AREA, MAX_AREA),
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE);
                campoArea.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel,
                "El área debe ser un número válido",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            campoArea.requestFocus();
            return false;
        }

        return true;
    }

    private void createUIComponents() {
        ObraRepo obraRepo = new ObraRepo();

        // Configurar comboboxes
        List<TipoObra> tipoObras = obraRepo.obtenerTiposObra();
        String[] tipoObraStrings = tipoObras.stream().map(TipoObra::getNombre).toArray(String[]::new);
        comboboxTipoObra = new JComboBox<>(tipoObraStrings);

        List<String> estados = List.of("Activa", "Inactiva", "Finalizada", "Cancelada", "Espera", "En aprobación");
        comboboxEstado = new JComboBox<>(estados.toArray(new String[0]));

        List<String> presupuestos = obraRepo.obtenerPresupuestos().stream()
            .map(presupuesto -> presupuesto.getDescripcion() + " -- Bs. " + presupuesto.getCosto())
            .toList();
        comboboxPresupuesto = new JComboBox<>(presupuestos.toArray(new String[0]));

        // Configurar listas
        listaMateriales = new JList<>(obraRepo.obtenerMateriales().stream()
            .map(material -> material.getNombre() + " -- Bs. " + material.getCosto())
            .toArray(String[]::new));

        listaMaquinaria = new JList<>(obraRepo.obtenerMaquinaria().stream()
            .map(maquinaria -> maquinaria.getNombre() + " -- Bs. " + maquinaria.getCostoTotal())
            .toArray(String[]::new));

        listaPersonal = new JList<>(obraRepo.obtenerPersonal().stream()
            .map(personal -> personal.getNombre() + " -- " + personal.getEspecialidad().getNombre())
            .toArray(String[]::new));

        // Configurar campo de área
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Double.class);
        formatter.setMinimum(MIN_AREA);
        formatter.setMaximum(MAX_AREA);
        formatter.setAllowsInvalid(false);
        campoArea = new JFormattedTextField(new DefaultFormatterFactory(formatter));
        campoArea.setValue(MIN_AREA);
    }
}
