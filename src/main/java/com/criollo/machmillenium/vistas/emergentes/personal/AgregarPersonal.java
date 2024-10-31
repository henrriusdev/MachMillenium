package com.criollo.machmillenium.vistas.emergentes.personal;

import com.criollo.machmillenium.entidades.Especialidad;
import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.Rol;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.repos.RolRepo;
import com.criollo.machmillenium.utilidades.Utilidades;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public AgregarPersonal() {
        cerrarButton.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());
        agregarButton.addActionListener(e -> {
            PersonalRepo personalRepo = new PersonalRepo();
            // Crear un nuevo personal con los datos ingresados
            String nombre = campoNombre.getText();
            String cedula = ((String) Objects.requireNonNull(comboboxCedula.getSelectedItem())).concat("-") + campoCedula.getText();
            String correo = campoCorreo.getText();
            Boolean fijo = siRadioButton.isSelected();
            LocalDateTime fechaTerminoContrato = LocalDate.parse(campoFinContrato.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
            String rol = (String) comboboxRol.getSelectedItem();
            String especialidad = campoEspecialidad.getText();

            Rol rolObj = personalRepo.obtenerRolPorNombre(rol);
            Especialidad especialidadObj = personalRepo.obtenerOCrearEspecialidad(especialidad);

            Personal personal = new Personal(nombre, cedula, correo, fijo, fechaTerminoContrato, rolObj, especialidadObj);
            if (rolObj.getNombre().equals("Trabajador")) {
                personal.setActivo(false);
            }
            personalRepo.insertar(personal);

            JOptionPane.showMessageDialog(panel, "Personal agregado exitosamente", "Cliente agregado", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.getWindowAncestor(panel).dispose();
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
}
