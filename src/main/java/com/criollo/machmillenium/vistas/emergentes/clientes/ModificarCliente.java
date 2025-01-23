package com.criollo.machmillenium.vistas.emergentes.clientes;

import com.criollo.machmillenium.entidades.Cliente;
import com.criollo.machmillenium.modelos.ModeloCliente;
import com.criollo.machmillenium.repos.ClienteRepo;
import com.criollo.machmillenium.utilidades.Utilidades;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;
import java.util.regex.Pattern;

public class ModificarCliente {
    public JPanel panel;
    private JTextField campoNombre;
    private JTextField campoSexo;
    private JComboBox<String> comboboxCedula;
    private JFormattedTextField campoCedula;
    private JComboBox<String> comboboxPrefijo;
    private JFormattedTextField campoTelefono;
    private JFormattedTextField campoCorreo;
    private JFormattedTextField campoEdad;
    private JTextArea textAreaDireccion;
    private JButton cerrarButton;
    private JButton editarButton;
    private final Long idCliente;

    private static final int MAX_NOMBRE_LENGTH = 100;
    private static final int MAX_DIRECCION_LENGTH = 255;
    private static final int MIN_EDAD = 18;
    private static final int MAX_EDAD = 110;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern NOMBRE_PATTERN = Pattern.compile("^[A-Za-zÁáÉéÍíÓóÚúÑñ\\s]{2,}$");
    private static final Pattern SEXO_PATTERN = Pattern.compile("^[MF]$");

    public ModificarCliente(ModeloCliente modeloCliente) {
        if (modeloCliente == null) {
            JOptionPane.showMessageDialog(null,
                "No se ha seleccionado un cliente para modificar",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            this.idCliente = null;
            Window window = SwingUtilities.getWindowAncestor(panel);
            if (window != null) {
                window.dispose();
            }
            return;
        }

        this.idCliente = modeloCliente.getId();
        setupUI();
        cargarDatosCliente(modeloCliente);
        setupListeners();
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

        textAreaDireccion.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) 
                    throws javax.swing.text.BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= MAX_DIRECCION_LENGTH) {
                    super.insertString(offs, str, a);
                }
            }
        });

        // Configurar campo de edad
        NumberFormat format = NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(format) {
            @Override
            public Object stringToValue(String text) throws ParseException {
                if (text.isEmpty()) {
                    return null;
                }
                return super.stringToValue(text);
            }
        };
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(MIN_EDAD);
        formatter.setMaximum(MAX_EDAD);
        formatter.setCommitsOnValidEdit(true);
        campoEdad.setFormatterFactory(new DefaultFormatterFactory(formatter));

        // Configurar tooltips
        campoNombre.setToolTipText("Ingrese el nombre completo (solo letras y espacios)");
        campoCedula.setToolTipText("Ingrese el número de cédula (solo números)");
        campoTelefono.setToolTipText("Ingrese el número telefónico en formato ###-##-##");
        campoCorreo.setToolTipText("Ingrese un correo electrónico válido");
        campoEdad.setToolTipText("Ingrese la edad (entre " + MIN_EDAD + " y " + MAX_EDAD + " años)");
        campoSexo.setToolTipText("Ingrese M para Masculino o F para Femenino");

        // Configurar el tamaño preferido del campo de edad
        campoEdad.setColumns(3);
    }

    private void cargarDatosCliente(ModeloCliente modeloCliente) {
        try {
            campoNombre.setText(modeloCliente.getNombre());
            campoSexo.setText(modeloCliente.getSexo());
            
            String cedula = modeloCliente.getCedula();
            if (cedula != null && cedula.length() > 2) {
                campoCedula.setText(cedula.substring(2));
                comboboxCedula.setSelectedItem(String.valueOf(cedula.charAt(0)));
            }

            String telefono = modeloCliente.getTelefono();
            if (telefono != null && telefono.length() > 6) {
                campoTelefono.setText(telefono.substring(6).trim());
                comboboxPrefijo.setSelectedItem(telefono.substring(0, 6).trim());
            }

            textAreaDireccion.setText(modeloCliente.getDireccion());
            campoEdad.setText(String.valueOf(modeloCliente.getEdad()));
            campoCorreo.setText(modeloCliente.getCorreo());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(panel,
                "Error al cargar los datos del cliente: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void setupListeners() {
        // Configurar eventos de teclado para el campo nombre
        campoNombre.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isSpaceChar(c) && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });

        // Configurar eventos de teclado para el campo sexo
        campoSexo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = Character.toUpperCase(e.getKeyChar());
                if (c != 'M' && c != 'F' && c != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
                if (c == 'M' || c == 'F') {
                    campoSexo.setText(String.valueOf(c));
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

        editarButton.addActionListener(e -> {
            try {
                if (!validarCampos()) {
                    return;
                }

                // Obtener y validar los datos
                String nombre = campoNombre.getText().trim();
                String cedula = ((String) Objects.requireNonNull(comboboxCedula.getSelectedItem())).concat("-") + campoCedula.getText().trim();
                String telefono = ((String) Objects.requireNonNull(comboboxPrefijo.getSelectedItem())).concat(" ") + campoTelefono.getText().trim();
                String direccion = textAreaDireccion.getText().trim();
                String correo = campoCorreo.getText().trim();
                String sexo = campoSexo.getText().trim().toUpperCase();

                // Validar y convertir edad
                Integer edad;
                try {
                    edad = Integer.parseInt(campoEdad.getText().trim());
                    if (edad < MIN_EDAD || edad > MAX_EDAD) {
                        throw new IllegalArgumentException("La edad debe estar entre " + MIN_EDAD + " y " + MAX_EDAD + " años");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel,
                        "La edad debe ser un número válido",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                    campoEdad.requestFocus();
                    return;
                }

                // Verificar si el cliente ya existe (excepto si es el mismo)
                ClienteRepo clienteRepo = new ClienteRepo();
                Cliente clienteExistente = clienteRepo.obtenerPorCedula(cedula);
                if (clienteExistente != null && !clienteExistente.getId().equals(idCliente)) {
                    JOptionPane.showMessageDialog(panel,
                        "Ya existe otro cliente registrado con esta cédula",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    campoCedula.requestFocus();
                    return;
                }

                // Crear y actualizar el cliente
                Cliente cliente = new Cliente();
                cliente.setId(idCliente);
                cliente.setNombre(nombre);
                cliente.setCedula(cedula);
                cliente.setTelefono(telefono);
                cliente.setDireccion(direccion);
                cliente.setEdad(edad);
                cliente.setCorreo(correo);
                cliente.setSexo(sexo);

                try {
                    clienteRepo.actualizar(cliente);
                    JOptionPane.showMessageDialog(panel,
                        "Cliente modificado exitosamente",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    Window window = SwingUtilities.getWindowAncestor(panel);
                    if (window != null) {
                        window.dispose();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                        "Error al actualizar el cliente: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                    "Error inesperado: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Agregar validación adicional para el campo de edad
        campoEdad.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                try {
                    String texto = campoEdad.getText().trim();
                    if (!texto.isEmpty()) {
                        int edad = Integer.parseInt(texto);
                        if (edad < MIN_EDAD || edad > MAX_EDAD) {
                            JOptionPane.showMessageDialog(panel,
                                "La edad debe estar entre " + MIN_EDAD + " y " + MAX_EDAD + " años",
                                "Error de validación",
                                JOptionPane.ERROR_MESSAGE);
                            campoEdad.requestFocus();
                        }
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(panel,
                        "Por favor ingrese una edad válida",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                    campoEdad.requestFocus();
                }
            }
        });

        // Agregar KeyListener para permitir solo números
        campoEdad.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
                // Limitar a 3 dígitos
                if (campoEdad.getText().length() >= 3 && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });
    }

    private boolean validarCampos() {
        // Validar nombre
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty() || !NOMBRE_PATTERN.matcher(nombre).matches()) {
            JOptionPane.showMessageDialog(panel,
                "El nombre debe contener solo letras y espacios, y tener al menos 2 caracteres",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            campoNombre.requestFocus();
            return false;
        }

        // Validar cédula
        if (campoCedula.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(panel,
                "La cédula es obligatoria",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            campoCedula.requestFocus();
            return false;
        }

        // Validar teléfono
        String telefono = campoTelefono.getText().trim();
        if (!telefono.matches("\\d{3}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(panel,
                "El teléfono debe tener el formato ###-##-##",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            campoTelefono.requestFocus();
            return false;
        }

        // Validar dirección
        String direccion = textAreaDireccion.getText().trim();
        if (direccion.isEmpty() || direccion.length() < 5) {
            JOptionPane.showMessageDialog(panel,
                "La dirección debe tener al menos 5 caracteres",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            textAreaDireccion.requestFocus();
            return false;
        }

        // Validar correo
        String correo = campoCorreo.getText().trim();
        if (!correo.isEmpty() && !EMAIL_PATTERN.matcher(correo).matches()) {
            JOptionPane.showMessageDialog(panel,
                "El formato del correo electrónico no es válido",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            campoCorreo.requestFocus();
            return false;
        }

        // Validar sexo
        String sexo = campoSexo.getText().trim().toUpperCase();
        if (!SEXO_PATTERN.matcher(sexo).matches()) {
            JOptionPane.showMessageDialog(panel,
                "El sexo debe ser M (Masculino) o F (Femenino)",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            campoSexo.requestFocus();
            return false;
        }

        return true;
    }

    private void createUIComponents() {
        comboboxCedula = new JComboBox<>();
        comboboxCedula.setModel(new DefaultComboBoxModel<>(new String[]{"V", "E", "J", "G"}));
        comboboxPrefijo = new JComboBox<>();
        comboboxPrefijo.setModel(new DefaultComboBoxModel<>(Utilidades.prefijosTelefonicosVenezuela));

        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        NumberFormatter numberFormatter = new NumberFormatter(format);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0L);
        numberFormatter.setMaximum(999999999L);
        campoCedula = new JFormattedTextField(numberFormatter);

        try {
            MaskFormatter telefonoFormatter = new MaskFormatter("###-##-##");
            telefonoFormatter.setPlaceholderCharacter('_');
            telefonoFormatter.setValidCharacters("0123456789");
            campoTelefono = new JFormattedTextField(telefonoFormatter);
        } catch (ParseException e) {
            campoTelefono = new JFormattedTextField();
            e.printStackTrace();
        }

        campoCorreo = new JFormattedTextField();
        campoCorreo.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String email = ((JFormattedTextField) input).getText().trim();
                return email.isEmpty() || EMAIL_PATTERN.matcher(email).matches();
            }
        });

        NumberFormatter edadFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        edadFormatter.setValueClass(Integer.class);
        edadFormatter.setMinimum(MIN_EDAD);
        edadFormatter.setMaximum(MAX_EDAD);
        campoEdad = new JFormattedTextField(edadFormatter);
    }
}
