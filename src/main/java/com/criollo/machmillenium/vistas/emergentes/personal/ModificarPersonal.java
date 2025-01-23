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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
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

        // Add tooltips
        campoNombre.setToolTipText("Ingrese el nombre completo del personal");
        campoCedula.setToolTipText("Ingrese el número de cédula sin puntos ni guiones");
        campoCorreo.setToolTipText("Ingrese un correo electrónico válido");
        campoEspecialidad.setToolTipText("Ingrese la especialidad del personal");
        campoFinContrato.setToolTipText("Ingrese la fecha de fin de contrato (dd/mm/yyyy)");
        restablecerClaveButton.setToolTipText("Restablece la contraseña a '12345678'");

        // Set initial values
        try {
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
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel, 
                "Error al cargar los datos del personal: " + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        // Add field validation listeners
        campoNombre.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                try {
                    validarNombre(campoNombre.getText());
                    campoNombre.setBackground(Color.WHITE);
                } catch (IllegalArgumentException e) {
                    campoNombre.setBackground(new Color(255, 200, 200));
                    JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        campoCorreo.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent evt) {
                try {
                    if (!campoCorreo.getText().isEmpty()) {
                        validarCorreo();
                        campoCorreo.setBackground(Color.WHITE);
                    }
                } catch (IllegalArgumentException e) {
                    campoCorreo.setBackground(new Color(255, 200, 200));
                    JOptionPane.showMessageDialog(panel, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cerrarButton.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());
        
        editarButton.addActionListener(e -> {
            try {
                // Validate required fields
                if (!validarCamposRequeridos()) {
                    return;
                }

                // Validate and get input data
                String nombre = validarNombre(campoNombre.getText());
                String cedula = validarCedula();
                String correo = validarCorreo();
                Boolean fijo = siRadioButton.isSelected();
                LocalDateTime fechaTerminoContrato = validarFechaContrato();
                String rolStr = validarRol();
                String especialidadStr = validarEspecialidad();
                Boolean activo = activoRadioButton.isSelected();

                // Check if email is already in use by another personal
                Personal existingPersonal = personalRepo.obtenerPorCorreo(correo);
                if (existingPersonal != null && !existingPersonal.getId().equals(idPersonal)) {
                    throw new IllegalArgumentException("El correo ya está en uso por otro personal");
                }

                // Get role and specialty
                Rol rol = personalRepo.obtenerRolPorNombre(rolStr);
                if (rol == null) {
                    throw new IllegalArgumentException("El rol seleccionado no existe");
                }

                Especialidad especialidad = personalRepo.obtenerOCrearEspecialidad(especialidadStr);

                // Create and update personal
                Personal personal = new Personal(nombre, cedula, correo, fijo, fechaTerminoContrato, rol, especialidad);
                personal.setActivo(activo);
                personal.setId(idPersonal);

                // Get selected privileges
                Set<Privilegio> privilegiosSeleccionados = obtenerPrivilegiosSeleccionados();
                if (privilegiosSeleccionados.isEmpty()) {
                    int confirm = JOptionPane.showConfirmDialog(
                        panel,
                        "No ha seleccionado ningún privilegio. ¿Desea continuar?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm != JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                personal.setPrivilegios(privilegiosSeleccionados);

                // Update personal
                personalRepo.actualizar(personal);
                JOptionPane.showMessageDialog(panel, 
                    "Personal modificado exitosamente\nNombre: " + nombre + "\nCédula: " + cedula,
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor(panel).dispose();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, 
                    "Error al modificar el personal: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        restablecerClaveButton.addActionListener(e -> {
            try {
                int confirm = JOptionPane.showConfirmDialog(
                    panel,
                    "¿Está seguro que desea restablecer la contraseña?\nLa nueva contraseña será: 12345678",
                    "Confirmar Restablecimiento",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    String nuevaClave = BCrypt.hashpw("12345678", BCrypt.gensalt());
                    personalRepo.restablecerClave(idPersonal, nuevaClave);
                    JOptionPane.showMessageDialog(panel, 
                        "Contraseña restablecida exitosamente\nNueva contraseña: 12345678",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                    "Error al restablecer la contraseña: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
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
        NumberFormatter numberFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
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

    private boolean validarCamposRequeridos() {
        StringBuilder mensajeError = new StringBuilder("Por favor complete los siguientes campos:\n");
        boolean camposValidos = true;

        if (campoNombre.getText().trim().isEmpty()) {
            mensajeError.append("- Nombre\n");
            camposValidos = false;
            campoNombre.setBackground(new Color(255, 200, 200));
        }

        if (campoCedula.getText().trim().isEmpty()) {
            mensajeError.append("- Cédula\n");
            camposValidos = false;
            campoCedula.setBackground(new Color(255, 200, 200));
        }

        if (campoCorreo.getText().trim().isEmpty()) {
            mensajeError.append("- Correo\n");
            camposValidos = false;
            campoCorreo.setBackground(new Color(255, 200, 200));
        }

        if (campoEspecialidad.getText().trim().isEmpty()) {
            mensajeError.append("- Especialidad\n");
            camposValidos = false;
            campoEspecialidad.setBackground(new Color(255, 200, 200));
        }

        if (campoFinContrato.getText().equals("__/__/____")) {
            mensajeError.append("- Fecha de fin de contrato\n");
            camposValidos = false;
            campoFinContrato.setBackground(new Color(255, 200, 200));
        }

        if (!camposValidos) {
            JOptionPane.showMessageDialog(panel, mensajeError.toString(), "Campos Requeridos", JOptionPane.WARNING_MESSAGE);
        }

        return camposValidos;
    }

    private String validarNombre(String nombre) {
        nombre = nombre.trim();
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres");
        }
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios");
        }
        return nombre;
    }

    private String validarCedula() {
        String cedula = campoCedula.getText().trim();
        if (cedula.isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía");
        }
        if (!cedula.matches("\\d+")) {
            throw new IllegalArgumentException("La cédula solo puede contener números");
        }
        if (cedula.length() < 6 || cedula.length() > 8) {
            throw new IllegalArgumentException("La cédula debe tener entre 6 y 8 dígitos");
        }
        return ((String) Objects.requireNonNull(comboboxCedula.getSelectedItem())).concat("-") + cedula;
    }

    private String validarCorreo() {
        String correo = campoCorreo.getText().trim();
        if (correo.isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }
        if (!esEmailValido(correo)) {
            throw new IllegalArgumentException("El formato del correo es inválido");
        }
        return correo;
    }

    private LocalDateTime validarFechaContrato() {
        try {
            String fecha = campoFinContrato.getText();
            if (fecha.equals("__/__/____")) {
                throw new IllegalArgumentException("La fecha de fin de contrato es requerida");
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaContrato = LocalDate.parse(fecha, formatter);
            
            if (fechaContrato.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha de fin de contrato no puede ser anterior a hoy");
            }
            
            return fechaContrato.atStartOfDay();
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use dd/mm/yyyy");
        }
    }

    private String validarRol() {
        String rol = (String) comboboxRol.getSelectedItem();
        if (rol == null || rol.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un rol");
        }
        return rol;
    }

    private String validarEspecialidad() {
        String especialidad = campoEspecialidad.getText().trim();
        if (especialidad.isEmpty()) {
            throw new IllegalArgumentException("La especialidad no puede estar vacía");
        }
        if (especialidad.length() < 3) {
            throw new IllegalArgumentException("La especialidad debe tener al menos 3 caracteres");
        }
        return especialidad;
    }

    private Set<Privilegio> obtenerPrivilegiosSeleccionados() {
        Set<Privilegio> privilegiosSeleccionados = new HashSet<>();
        for (Map.Entry<Privilegios, JCheckBox> entry : privilegiosCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                Privilegio privilegio = privilegioRepo.obtenerPorNombre(entry.getKey().name());
                if (privilegio != null) {
                    privilegiosSeleccionados.add(privilegio);
                }
            }
        }
        return privilegiosSeleccionados;
    }

    private boolean esEmailValido(String email) {
        // Expresión regular para validar correos electrónicos
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
