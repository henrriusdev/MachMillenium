package com.criollo.machmillenium.vistas.gestor;

import com.criollo.machmillenium.entidades.Inasistencia;
import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.repos.PersonalRepo;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RegistrarInasistencia extends JDialog {
    public JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<String> list1;
    private final PersonalRepo personalRepo;

    public RegistrarInasistencia(PersonalRepo personalRepo) {
        this.personalRepo = personalRepo;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // obtener el personal seleccionado
        List<String> selectedValuesList = list1.getSelectedValuesList();
        if (selectedValuesList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un personal", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // registrar la inasistencia
        List<Inasistencia> inasistencias = new ArrayList<>();
        for (String selectedValue : selectedValuesList) {
            Personal personal = personalRepo.obtenerPorNombre(selectedValue);
            Inasistencia inasistencia = new Inasistencia();
            inasistencia.setPersonal(personal);
            // preguntar el motivo de la inasistencia
            inasistencia.setMotivo(JOptionPane.showInputDialog(this, "Motivo de la inasistencia de " + personal.getNombre()));
            String fecha = JOptionPane.showInputDialog(this, "Fecha de la inasistencia de " + personal.getNombre() + " (dd/MM/yyyy)");
            inasistencia.setFecha(LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            inasistencias.add(inasistencia);
        }

        personalRepo.registrarInasistencias(inasistencias);
        JOptionPane.showMessageDialog(this, "Inasistencias registradas con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        SwingUtilities.getWindowAncestor(contentPane).dispose();
    }

    private void onCancel() {
        int option = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea cancelar?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            SwingUtilities.getWindowAncestor(contentPane).dispose();
        }
    }

    private void createUIComponents() {
        PersonalRepo personalRepo = new PersonalRepo();
        List<Personal> personalList = personalRepo.obtenerPorRol("Trabajador");
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Personal personal : personalList) {
            listModel.addElement(personal.getNombre());
        }

        list1 = new JList<>(listModel);
    }
}
