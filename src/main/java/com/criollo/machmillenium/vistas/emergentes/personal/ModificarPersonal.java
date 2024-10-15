package com.criollo.machmillenium.vistas.emergentes.personal;

import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.Rol;
import com.criollo.machmillenium.modelos.ModeloPersonal;
import com.criollo.machmillenium.repos.PersonalRepo;
import com.criollo.machmillenium.utilidades.Utilidades;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModificarPersonal {
    public JPanel panel;
    private JTextField campoNombre;
    private JRadioButton siRadioButton;
    private JRadioButton noRadioButton;
    private JComboBox<Rol> comboboxRol;
    private JComboBox<String> comboboxCedula;
    private JFormattedTextField campoCedula;
    private JFormattedTextField campoCorreo;
    private JFormattedTextField campoFinContrato;
    private JTextField campoEspecialidad;
    private JButton cerrarButton;
    private JButton editarButton;
    private JRadioButton activoRadioButton;
    private JRadioButton inactivoRadioButton;
    private final Long idPersonal;

    public ModificarPersonal(ModeloPersonal modeloPersonal) {
        if (modeloPersonal == null) {
            JOptionPane.showMessageDialog(panel, "No se ha seleccionado un personal para modificar", "Personal no seleccionado", JOptionPane.ERROR_MESSAGE);
            SwingUtilities.getWindowAncestor(panel).dispose();
            this.idPersonal = null;
            return;
        }

        this.idPersonal = modeloPersonal.getId();
        cerrarButton.addActionListener(e -> SwingUtilities.getWindowAncestor(panel).dispose());
        editarButton.addActionListener(e -> {
            PersonalRepo personalRepo = new PersonalRepo();
            // Crear un nuevo personal con los datos ingresados
            String nombre = campoNombre.getText();
            String cedula = ((String) comboboxCedula.getSelectedItem()).concat("-") + campoCedula.getText();
            String correo = campoCorreo.getText();
            Boolean fijo = siRadioButton.isSelected();
            Rol rol = (Rol) comboboxRol.getSelectedItem();
            String especialidad = campoEspecialidad.getText();
            Boolean activo = activoRadioButton.isSelected();
             LocalDateTime fechaTerminoContrato = LocalDateTime.parse(campoFinContrato.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

             Personal personal = new Personal(nombre, cedula, correo, fijo, fechaTerminoContrato, rol, null);
             personal.setActivo(activo);
             personal.setId(idPersonal);
             personalRepo.actualizar(personal);

            JOptionPane.showMessageDialog(panel, "Personal modificado exitosamente", "Personal modificado", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.getWindowAncestor(panel).dispose();
        });
    }

    private void createUIComponents() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        campoFinContrato = new JFormattedTextField(dateFormat);

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
    }
}
