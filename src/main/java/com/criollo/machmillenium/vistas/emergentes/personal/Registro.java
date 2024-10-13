package com.criollo.machmillenium.vistas.emergentes.personal;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Registro {
    private JTextField textField1;
    private JRadioButton siRadioButton;
    private JRadioButton noRadioButton;
    private JFormattedTextField formattedTextField1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;

    private void createUIComponents() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        formattedTextField1 = new JFormattedTextField(dateFormat);

    }
}
