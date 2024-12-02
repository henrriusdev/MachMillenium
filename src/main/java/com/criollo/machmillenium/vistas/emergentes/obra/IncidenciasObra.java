package com.criollo.machmillenium.vistas.emergentes.obra;

import com.criollo.machmillenium.entidades.Incidencia;
import com.criollo.machmillenium.entidades.Obra;
import com.criollo.machmillenium.repos.ObraRepo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;
import javax.swing.text.MaskFormatter;

public class IncidenciasObra extends JDialog {
    public JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tablaIncidencias;
    private JComboBox<Incidencia.TipoIncidencia> comboTipo;
    private JTextField campoDescripcion;
    private JFormattedTextField campoRetraso;
    private final ObraRepo obraRepo;
    private final Long idObra;
    private DefaultTableModel tableModel;

    public IncidenciasObra(Long idObra) {
        this.idObra = idObra;
        this.obraRepo = new ObraRepo();

        initComponents();
        cargarIncidencias();

        setContentPane(contentPane);
        setModal(true);
        setTitle("Incidencias de la Obra");
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // Configurar el combo de tipos de incidencia
        comboTipo.setModel(new DefaultComboBoxModel<>(Incidencia.TipoIncidencia.values()));

        // Configurar la tabla
        String[] columnNames = {"Tipo", "Descripción", "Retraso", "Fecha"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaIncidencias.setModel(tableModel);

        // Configurar listeners
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // Agregar listener para Enter en el campo descripción
        campoDescripcion.addActionListener(e -> agregarIncidencia());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // Establecer valor inicial del campo retraso
        campoRetraso.setValue("000:00");
    }

    private void cargarIncidencias() {
        List<Incidencia> incidencias = obraRepo.obtenerIncidenciasPorObra(idObra);
        tableModel.setRowCount(0);

        for (Incidencia incidencia : incidencias) {
            Vector<Object> row = new Vector<>();
            row.add(incidencia.getTipo());
            row.add(incidencia.getDescripcion());
            Duration retraso = incidencia.getRetraso();
            row.add(String.format("%03d:%02d", retraso.toHours(), retraso.toMinutesPart()));
            row.add(incidencia.getFechaIncidencia());
            tableModel.addRow(row);
        }
    }

    private void agregarIncidencia() {
        if (campoDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese una descripción",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String retrasoTexto = campoRetraso.getText().replace("#", "0").trim();
        String[] tiempoPartes = retrasoTexto.split(":");
        try {
            int horas = Integer.parseInt(tiempoPartes[0]);
            int minutos = Integer.parseInt(tiempoPartes[1]);

            if (minutos < 0 || minutos >= 60) {
                throw new NumberFormatException();
            }

            Duration retraso = Duration.ofHours(horas).plusMinutes(minutos);
            Obra obra = obraRepo.obtenerObraPorId(idObra);
            
            // Inicializar tiempoTotal si es null con el tiempo estimado del presupuesto
            if (obra.getTiempoTotal() == null) {
                obra.setTiempoTotal(obra.getPresupuesto().getTiempoEstimado());
            }
            
            // Actualizar el tiempo total sumando el retraso
            obra.setTiempoTotal(obra.getTiempoTotal().plus(retraso));
            obra = obraRepo.actualizarObra(obra);

            Incidencia incidencia = new Incidencia(
                    (Incidencia.TipoIncidencia) comboTipo.getSelectedItem(),
                    campoDescripcion.getText().trim(),
                    retraso,
                    obra
            );

            obraRepo.insertarIncidencia(incidencia);
            cargarIncidencias();

            // Limpiar campos
            campoDescripcion.setText("");
            campoRetraso.setValue("000:00");
            campoDescripcion.requestFocus();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un tiempo válido en formato HHH:mm",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void cerrarDialogo() {
        this.setVisible(false);
        this.dispose();
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof IncidenciasObra) {
                window.dispose();
            }
        }
    }

    private void onOK() {
        agregarIncidencia();
    }

    private void onCancel() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro que desea salir?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            cerrarDialogo();
        }
    }

    private void createUIComponents() {
        try {
            MaskFormatter horaMinutoFormatter = new MaskFormatter("###:##");
            horaMinutoFormatter.setPlaceholderCharacter('#');
            campoRetraso = new JFormattedTextField(horaMinutoFormatter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
