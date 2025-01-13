package com.criollo.machmillenium.vistas.emergentes.personal;

import com.criollo.machmillenium.entidades.Especialidad;
import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.Privilegio;
import com.criollo.machmillenium.entidades.Rol;
import com.criollo.machmillenium.enums.Privilegios;
import com.criollo.machmillenium.modelos.ModeloPersonal;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.repos.PrivilegioRepo;
import com.criollo.machmillenium.repos.RolRepo;
import com.criollo.machmillenium.utilidades.Utilidades;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ModificarPersonal {
    public JPanel panel;
    private JTextField campoNombre;
    private JRadioButton siRadioButton;
    private JRadioButton noRadioButton;
    private JComboBox<String> comboboxRol;
    private JComboBox<String> comboboxCedula;
    private JFormattedTextField campoCedula;
    private JFormattedTextField campoCorreo;
    private JFormattedTextField campoFinContrato;
    private JTextField campoEspecialidad;
    private JButton cerrarButton;
    private JButton editarButton;
    private JRadioButton activoRadioButton;
    private JRadioButton inactivoRadioButton;
    private JButton restablecerClaveButton;
    private JPanel panelPrivilegios;
    private final Long idPersonal;
    private final Map<Privilegios, JCheckBox> privilegiosCheckboxes = new HashMap<>();
    private final PersonalRepo personalRepo = new PersonalRepo();
    private final PrivilegioRepo privilegioRepo = new PrivilegioRepo();

    public ModificarPersonal(ModeloPersonal modeloPersonal) {
        if (modeloPersonal == null) {
            JOptionPane.showMessageDialog(panel, "No se ha seleccionado un personal para modificar", "Personal no seleccionado", JOptionPane.ERROR_MESSAGE);
            SwingUtilities.getWindowAncestor(panel).dispose();
            this.idPersonal = null;
            return;
        }

        this.idPersonal = modeloPersonal.getId();
        initializePrivilegiosPanel();
        loadCurrentPrivileges();

        campoNombre.setText(modeloPersonal.getNombre());

        String documentoCedula = modeloPersonal.getCedula().charAt(0) + "";
        comboboxCedula.setSelectedItem(documentoCedula);

        campoCedula.setText(modeloPersonal.getCedula().substring(2));
        campoCorreo.setText(modeloPersonal.getCorreo());

        siRadioButton.setSelected(modeloPersonal.getFijo().equals("Si"));
        noRadioButton.setSelected(modeloPersonal.getFijo().equals("No"));

        campoFinContrato.setText(modeloPersonal.getFechaFinContrato());
        campoEspecialidad.setText(modeloPersonal.getEspecialidad());
        comboboxRol.setSelectedItem(modeloPersonal.getRol());

        activoRadioButton.setSelected(modeloPersonal.getActivo().equals("Si"));
        inactivoRadioButton.setSelected(modeloPersonal.getActivo().equals("No"));

        cerrarButton.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());
        editarButton.addActionListener(e -> {
            try {
                PersonalRepo personalRepo = new PersonalRepo();
                // Crear un nuevo personal con los datos ingresados
                String nombre = campoNombre.getText();
                String cedula = ((String) comboboxCedula.getSelectedItem()).concat("-") + campoCedula.getText();
                String correo = campoCorreo.getText();
                Boolean fijo = siRadioButton.isSelected();
                String rolStr = (String) comboboxRol.getSelectedItem();
                String especialidadStr = campoEspecialidad.getText();
                Boolean activo = activoRadioButton.isSelected();
                LocalDateTime fechaTerminoContrato = LocalDate.parse(campoFinContrato.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();

                Rol rol = personalRepo.obtenerRolPorNombre(rolStr);
                Especialidad especialidad = personalRepo.obtenerOCrearEspecialidad(especialidadStr);

                Personal personal = new Personal(nombre, cedula, correo, fijo, fechaTerminoContrato, rol, especialidad);
                personal.setActivo(activo);
                personal.setId(idPersonal);

                // Actualizar privilegios
                Set<Privilegio> privilegiosSeleccionados = new HashSet<>();
                for (Map.Entry<Privilegios, JCheckBox> entry : privilegiosCheckboxes.entrySet()) {
                    if (entry.getValue().isSelected()) {
                        Privilegio privilegio = privilegioRepo.obtenerPorNombre(entry.getKey().name());
                        if (privilegio != null) {
                            privilegiosSeleccionados.add(privilegio);
                        }
                    }
                }
                personal.setPrivilegios(privilegiosSeleccionados);

                // Actualizar el personal
                personalRepo.actualizar(personal);
                JOptionPane.showMessageDialog(panel, "Personal modificado exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor(panel).dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error al modificar el personal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        restablecerClaveButton.addActionListener(e -> {
            PersonalRepo personalRepo = new PersonalRepo();
            String nuevaClave = BCrypt.hashpw("12345678", BCrypt.gensalt());
            personalRepo.restablecerClave(idPersonal, nuevaClave);
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

    private void loadCurrentPrivileges() {
        try {
            Personal personal = personalRepo.obtenerPorId(idPersonal);
            Set<String> privilegiosActuales;
            
            if (personal != null && personal.getPrivilegios() != null) {
                privilegiosActuales = personal.getPrivilegios().stream()
                    .map(Privilegio::getNombre)
                    .collect(Collectors.toSet());
            } else {
                privilegiosActuales = new HashSet<>();
            }

            // Marcar los checkboxes de los privilegios actuales y habilitar/deshabilitar según el rol
            String rolActual = personal.getRol().getNombre();
            if (rolActual != null) {
                privilegiosCheckboxes.forEach((privilegio, checkBox) -> {
                    // Primero marcar si está seleccionado
                    checkBox.setSelected(privilegiosActuales.contains(privilegio.name()));
                    
                    // Luego aplicar las reglas de habilitación según el rol
                    checkBox.setSelected(false);
                    if (rolActual.equals("Administrador")) {
                        switch (privilegio) {
                            case VER_RECIBOS:
                            case CREAR_RECIBOS:
                            case MODIFICAR_RECIBOS:
                                checkBox.setEnabled(true);
                                break;
                            default:
                                checkBox.setEnabled(false);
                        }
                    } else if (rolActual.equals("Gerente de Proyecto")) {
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
                        }
                    } else if (rolActual.equals("Usuario Operativo")) {
                        switch (privilegio) {
                            case VER_RECIBOS:
                            case CREAR_RECIBOS:
                            case MODIFICAR_RECIBOS:
                                checkBox.setEnabled(false);
                                break;
                            default:
                                checkBox.setEnabled(true);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel, "Error al cargar los privilegios: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
