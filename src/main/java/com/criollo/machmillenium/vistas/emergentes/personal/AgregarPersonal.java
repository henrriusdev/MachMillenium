package com.criollo.machmillenium.vistas.emergentes.personal;

import com.criollo.machmillenium.entidades.Especialidad;
import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.Privilegio;
import com.criollo.machmillenium.enums.Privilegios;
import com.criollo.machmillenium.entidades.Rol;
import com.criollo.machmillenium.repos.EspecialidadRepo;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.repos.PrivilegioRepo;
import com.criollo.machmillenium.repos.RolRepo;
import com.criollo.machmillenium.utilidades.Utilidades;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AgregarPersonal {
    public JPanel panel;
    private JTextField campoNombre;
    private JRadioButton siRadioButton;
    private JRadioButton noRadioButton;
    private JFormattedTextField campoFinContrato;
    private JComboBox<String> comboboxRol;
    private JComboBox<String> comboboxCedula;
    private JFormattedTextField campoCedula;
    private JFormattedTextField campoCorreo;
    private JTextField campoEspecialidad;
    private JButton cerrarButton;
    private JButton agregarButton;
    private JPanel panelPrivilegios;
    private final Map<Privilegios, JCheckBox> privilegiosCheckboxes = new HashMap<>();


    public AgregarPersonal() {
        cerrarButton.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());
        initializePrivilegiosPanel();
        agregarButton.addActionListener(e -> {
            PersonalRepo personalRepo = new PersonalRepo();
            PrivilegioRepo privilegioRepo = new PrivilegioRepo();
            RolRepo rolRepo = new RolRepo();
            EspecialidadRepo especialidadRepo = new EspecialidadRepo();
            // Crear un nuevo personal con los datos ingresados
            String nombre = campoNombre.getText();
            String cedula = ((String) Objects.requireNonNull(comboboxCedula.getSelectedItem())).concat("-") + campoCedula.getText();
            String correo = campoCorreo.getText();
            Boolean fijo = siRadioButton.isSelected();
            LocalDateTime fechaTerminoContrato = LocalDate.parse(campoFinContrato.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
            String rol = (String) comboboxRol.getSelectedItem();
            String especialidad = campoEspecialidad.getText();

            // Obtener los privilegios seleccionados
            Set<Privilegio> privilegiosSeleccionados = new HashSet<>();
            for (Map.Entry<Privilegios, JCheckBox> entry : privilegiosCheckboxes.entrySet()) {
                if (entry.getValue().isSelected()) {
                    Privilegio privilegio = privilegioRepo.obtenerPorNombre(entry.getKey().name());
                    if (privilegio != null) {
                        privilegiosSeleccionados.add(privilegio);
                    }
                }
            }

            try {
                // Validar los datos
                if (nombre.isEmpty() || cedula.isEmpty() || correo.isEmpty() || especialidad.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Por favor, complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear el personal
                Personal personal = new Personal();
                personal.setNombre(nombre);
                personal.setCedula(cedula);
                personal.setCorreo(correo);
                personal.setFijo(fijo);
                personal.setFechaTerminoContrato(fechaTerminoContrato);
                personal.setRol(rolRepo.obtenerPorNombre(rol));
                personal.setEspecialidad(especialidadRepo.obtenerPorNombre(especialidad));

                Personal nuevoPersonal = personalRepo.insertar(personal);

                // Asignar privilegios al personal
                nuevoPersonal.setPrivilegios(privilegiosSeleccionados);
                personalRepo.actualizar(nuevoPersonal);

                JOptionPane.showMessageDialog(panel, "Personal agregado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor(panel).dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        comboboxRol.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String rol = (String) comboboxRol.getSelectedItem();
                if (rol != null) {
                    // los privilegios deshabilitados son los que ya tiene el rol
                    if (rol.equals("Administrador")) {
                        // deshabilitar checkbox de privilegios (excepto los de pagos)
                        privilegiosCheckboxes.forEach((privilegio, checkBox) -> {
                            switch (privilegio) {
                                case VER_RECIBOS:
                                case CREAR_RECIBOS:
                                case MODIFICAR_RECIBOS:
                                    checkBox.setEnabled(true);
                                    break;
                                default:
                                    checkBox.setEnabled(false);
                                    checkBox.setSelected(false);
                            }
                        });
                    }

                    if (rol.equals("Gerente de Proyecto")){
                        // solo dejar habilitados los checkbox de personal y recibos
                        privilegiosCheckboxes.forEach((privilegio, checkBox) -> {
                            switch (privilegio) {
                                case VER_PERSONAL:
                                case CREAR_PERSONAL:
                                case MODIFICAR_PERSONAL:
                                case VER_RECIBOS:
                                case CREAR_RECIBOS:
                                case MODIFICAR_RECIBOS:
                                    checkBox.setEnabled(true);
                                    break;
                                default:
                                    checkBox.setEnabled(false);
                                    checkBox.setSelected(false);
                            }
                        });
                    }

                    if (rol.equals("Usuario Operativo")) {
                        // solo dejar habilitados los checkbox de recibos
                        privilegiosCheckboxes.forEach((privilegio, checkBox) -> {
                            switch (privilegio) {
                                case VER_RECIBOS:
                                case CREAR_RECIBOS:
                                case MODIFICAR_RECIBOS:
                                    checkBox.setEnabled(false);
                                    break;
                                default:
                                    checkBox.setEnabled(true);
                                    checkBox.setSelected(false);
                            }
                        });
                    }
                }
            }
        });
    }

    private void createUIComponents() {
        RolRepo rolRepo = new RolRepo();
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            campoFinContrato = new JFormattedTextField(dateMask);
        } catch (ParseException e) {
            campoFinContrato = new JFormattedTextField();
            e.printStackTrace();
        }
        campoFinContrato.setColumns(10);

        comboboxCedula = new JComboBox<>();
        comboboxCedula.setModel(new DefaultComboBoxModel<>(new String[]{"V", "E", "J", "G"}));
        NumberFormatter numberFormatter = Utilidades.getNumberFormatter();
        campoCedula = new JFormattedTextField(numberFormatter);

        DefaultFormatterFactory factory = new DefaultFormatterFactory();
        campoCorreo = new JFormattedTextField(factory);

        // Añadir un InputVerifier para la validación del correo
        campoCorreo.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String email = ((JFormattedTextField) input).getText();
                return esEmailValido(email);
            }

            private boolean esEmailValido(String email) {
                // Expresión regular para validar correos electrónicos
                String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
                Pattern pattern = Pattern.compile(emailRegex);
                Matcher matcher = pattern.matcher(email);
                return matcher.matches();
            }
        });

        List<Rol> roles = rolRepo.obtenerTodos();

        // Crear un arreglo solo con los nombres de los roles
        String[] roleNames = roles.stream().map(Rol::getNombre).toArray(String[]::new);

        // Rellenar el JComboBox con los nombres de los roles
        comboboxRol = new JComboBox<>(roleNames);
    }

    private void initializePrivilegiosPanel() {
        // Configurar el panel de privilegios con un GridLayout
        panelPrivilegios.setLayout(new GridLayout(0, 3, 10, 5)); // 3 columnas, filas automáticas
        
        // Agrupar privilegios por categoría
        Map<String, List<Privilegios>> privilegiosPorCategoria = Arrays.stream(Privilegios.values())
                .collect(Collectors.groupingBy(priv -> {
                    String name = priv.name();
                    int firstUnderscore = name.indexOf('_');
                    if (firstUnderscore == -1) return "OTROS";
                    
                    // Extraer la parte después del primer underscore
                    String afterFirstUnderscore = name.substring(firstUnderscore + 1);
                    int secondUnderscore = afterFirstUnderscore.indexOf('_');
                    
                    // Si no hay segundo underscore, usar toda la parte después del primer underscore
                    if (secondUnderscore == -1) return afterFirstUnderscore;
                    
                    // Si hay segundo underscore, extraer la parte entre los underscores
                    return afterFirstUnderscore.substring(0, secondUnderscore);
                }));

        // Agregar un panel por cada categoría
        privilegiosPorCategoria.forEach((categoria, privilegios) -> {
            JPanel categoriaPanel = new JPanel();
            categoriaPanel.setLayout(new BoxLayout(categoriaPanel, BoxLayout.Y_AXIS));
            categoriaPanel.setBorder(BorderFactory.createTitledBorder(categoria));

            for (Privilegios privilegio : privilegios) {
                JCheckBox checkBox = new JCheckBox(privilegio.getDescripcion());
                privilegiosCheckboxes.put(privilegio, checkBox);
                categoriaPanel.add(checkBox);
            }

            panelPrivilegios.add(categoriaPanel);
        });
    }
}
