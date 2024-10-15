package com.criollo.machmillenium.vistas.emergentes.clientes;

import com.criollo.machmillenium.entidades.Cliente;
import com.criollo.machmillenium.modelos.ModeloCliente;
import com.criollo.machmillenium.repos.ClienteRepo;
import com.criollo.machmillenium.utilidades.Utilidades;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;
import java.util.regex.Matcher;
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

    public ModificarCliente(ModeloCliente modeloCliente) {
        if (modeloCliente == null) {
            JOptionPane.showMessageDialog(panel, "No se ha seleccionado un cliente para modificar", "Error", JOptionPane.ERROR_MESSAGE);
            SwingUtilities.getWindowAncestor(panel).dispose();
            this.idCliente = null;
            return;
        }

        this.idCliente = modeloCliente.getId();
        
        // Cargar los datos del cliente en los campos
        campoNombre.setText(modeloCliente.getNombre());
        campoSexo.setText(modeloCliente.getSexo());
        campoCedula.setText(modeloCliente.getCedula().substring(2));
        campoTelefono.setText(modeloCliente.getTelefono().substring(6).trim());
        textAreaDireccion.setText(modeloCliente.getDireccion());
        campoEdad.setText(String.valueOf(modeloCliente.getEdad()));
        campoCorreo.setText(modeloCliente.getCorreo());

        System.out.println(modeloCliente.getCedula().charAt(0));
        System.out.println(modeloCliente.getCedula().substring(2));
        System.out.println(modeloCliente.getTelefono().substring(0, 6));
        System.out.println(modeloCliente.getTelefono().substring(0, 6).trim());

        comboboxCedula.setSelectedItem(modeloCliente.getCedula().charAt(0));
        comboboxPrefijo.setSelectedItem(modeloCliente.getTelefono().substring(0, 6));
        
        cerrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.getWindowAncestor(panel).dispose();
            }
        });
        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClienteRepo clienteRepo = new ClienteRepo();

                // Crear un nuevo cliente con los datos ingresados
                String nombre = campoNombre.getText();
                String cedula = ((String) Objects.requireNonNull(comboboxCedula.getSelectedItem())).concat("-") + campoCedula.getText();
                Cliente cliente = getCliente(nombre, cedula);

                System.out.println(idCliente);
                clienteRepo.actualizar(cliente);
                JOptionPane.showMessageDialog(panel, "Cliente modificado exitosamente", "Modificado", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.getWindowAncestor(panel).dispose();
            }

            private Cliente getCliente(String nombre, String cedula) {
                String telefono = ((String) Objects.requireNonNull(comboboxPrefijo.getSelectedItem())).concat(" ") + campoTelefono.getText();
                String direccion = textAreaDireccion.getText();
                Integer edad = Integer.parseInt(campoEdad.getText());
                String correo = campoCorreo.getText();
                String sexo = campoSexo.getText();

                // Crear un nuevo cliente con los datos ingresados
                Cliente cliente = new Cliente();
                cliente.setId(idCliente);
                cliente.setNombre(nombre);
                cliente.setCedula(cedula);
                cliente.setTelefono(telefono);
                cliente.setDireccion(direccion);
                cliente.setEdad(edad);
                cliente.setCorreo(correo);
                cliente.setSexo(sexo);
                return cliente;
            }
        });
    }

    private void createUIComponents() {
        comboboxCedula = new JComboBox<>();
        comboboxCedula.setModel(new DefaultComboBoxModel<>(new String[]{"V", "E", "J", "G"}));
        comboboxPrefijo = new JComboBox<>();
        comboboxPrefijo.setModel(new DefaultComboBoxModel<>(Utilidades.prefijosTelefonicosVenezuela));

        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(true);
        NumberFormatter numberFormatter = new NumberFormatter(format);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0L);
        numberFormatter.setMaximum(1_999_999_999L);
        campoCedula = new JFormattedTextField(numberFormatter);

        // Configurar el campo de teléfono con el formato XXX-XX-XX
        try {
            MaskFormatter telefonoFormatter = new MaskFormatter("###-##-##");
            telefonoFormatter.setPlaceholderCharacter('#');
            campoTelefono = new JFormattedTextField(telefonoFormatter);
        } catch (ParseException e) {
            campoTelefono = new JFormattedTextField();
            e.printStackTrace();
        }

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

        NumberFormatter edadFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
        edadFormatter.setValueClass(Integer.class);
        edadFormatter.setMinimum(0);
        edadFormatter.setMaximum(110);
        campoEdad = new JFormattedTextField(edadFormatter);
    }
}
