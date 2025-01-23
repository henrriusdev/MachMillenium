package com.criollo.machmillenium.vistas.emergentes.obra;

import com.criollo.machmillenium.entidades.Incidencia;
import com.criollo.machmillenium.entidades.Obra;
import com.criollo.machmillenium.repos.ObraRepo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;
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

    private static final int MAX_DESCRIPCION_LENGTH = 500;
    private static final int MIN_DESCRIPCION_LENGTH = 10;
    private static final Pattern DESCRIPCION_PATTERN = Pattern.compile("^[A-Za-zÁáÉéÍíÓóÚúÑñ0-9\\s.,;:-]{" + MIN_DESCRIPCION_LENGTH + ",}$");
    private static final Duration MAX_RETRASO = Duration.ofHours(999).plusMinutes(59);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public IncidenciasObra(Long idObra) {
        if (idObra == null) {
            throw new IllegalArgumentException("El ID de la obra no puede ser nulo");
        }

        this.idObra = idObra;
        this.obraRepo = new ObraRepo();

        try {
            Obra obra = obraRepo.obtenerObraPorId(idObra);
            if (obra == null) {
                throw new IllegalArgumentException("No se encontró la obra con ID: " + idObra);
            }

            initComponents();
            cargarIncidencias();

            setContentPane(contentPane);
            setModal(true);
            setTitle("Incidencias de la Obra: " + obra.getNombre());
            setSize(800, 500);
            setLocationRelativeTo(null);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                "Error al inicializar la ventana de incidencias: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void initComponents() {
        // Configurar el panel principal
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Configurar el combo de tipos de incidencia
        comboTipo.setModel(new DefaultComboBoxModel<>(Incidencia.TipoIncidencia.values()));
        comboTipo.setToolTipText("Seleccione el tipo de incidencia");

        // Configurar campo de descripción
        campoDescripcion.setToolTipText("Ingrese la descripción de la incidencia (mínimo " + MIN_DESCRIPCION_LENGTH + " caracteres)");
        campoDescripcion.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) 
                    throws javax.swing.text.BadLocationException {
                if (str == null) return;
                if ((getLength() + str.length()) <= MAX_DESCRIPCION_LENGTH) {
                    super.insertString(offs, str, a);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });

        // Validación en tiempo real del campo de descripción
        campoDescripcion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String texto = campoDescripcion.getText().trim();
                boolean valido = texto.length() >= MIN_DESCRIPCION_LENGTH && 
                               texto.length() <= MAX_DESCRIPCION_LENGTH &&
                               DESCRIPCION_PATTERN.matcher(texto).matches();
                
                if (!valido && texto.length() > 0) {
                    campoDescripcion.setBackground(new Color(255, 235, 235));
                    buttonOK.setEnabled(false);
                } else {
                    campoDescripcion.setBackground(Color.WHITE);
                    buttonOK.setEnabled(true);
                }
            }
        });

        // Configurar la tabla
        String[] columnNames = {"Tipo", "Descripción", "Retraso", "Fecha"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Incidencia.TipoIncidencia.class : String.class;
            }
        };
        tablaIncidencias.setModel(tableModel);
        
        // Configurar el renderizado de la tabla
        tablaIncidencias.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (row % 2 == 0 && !isSelected) {
                    c.setBackground(new Color(240, 240, 240));
                } else if (!isSelected) {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        // Configurar tamaños de columnas
        tablaIncidencias.getColumnModel().getColumn(0).setPreferredWidth(100); // Tipo
        tablaIncidencias.getColumnModel().getColumn(1).setPreferredWidth(300); // Descripción
        tablaIncidencias.getColumnModel().getColumn(2).setPreferredWidth(100); // Retraso
        tablaIncidencias.getColumnModel().getColumn(3).setPreferredWidth(150); // Fecha

        // Configurar botones
        buttonOK = new JButton("Agregar");
        buttonOK.setToolTipText("Agregar nueva incidencia");
        buttonCancel = new JButton("Cerrar");
        buttonCancel.setToolTipText("Cerrar ventana");

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
        campoRetraso.setToolTipText("Ingrese el tiempo de retraso en formato HHH:mm (máximo 999:59)");
    }

    private void cargarIncidencias() {
        try {
            List<Incidencia> incidencias = obraRepo.obtenerIncidenciasPorObra(idObra);
            tableModel.setRowCount(0);

            if (incidencias.isEmpty()) {
                // Agregar una fila indicando que no hay incidencias
                Vector<Object> emptyRow = new Vector<>();
                emptyRow.add(null);
                emptyRow.add("No hay incidencias registradas");
                emptyRow.add("");
                emptyRow.add("");
                tableModel.addRow(emptyRow);
                tablaIncidencias.setEnabled(false);
            } else {
                tablaIncidencias.setEnabled(true);
                for (Incidencia incidencia : incidencias) {
                    Vector<Object> row = new Vector<>();
                    row.add(incidencia.getTipo());
                    row.add(incidencia.getDescripcion());
                    Duration retraso = incidencia.getRetraso();
                    row.add(String.format("%03d:%02d", retraso.toHours(), retraso.toMinutesPart()));
                    row.add(incidencia.getFechaIncidencia().format(DATE_FORMATTER));
                    tableModel.addRow(row);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar las incidencias: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarIncidencia() {
        try {
            // Validar descripción
            String descripcion = campoDescripcion.getText().trim();
            if (descripcion.isEmpty()) {
                throw new IllegalArgumentException("La descripción no puede estar vacía");
            }

            if (descripcion.length() < MIN_DESCRIPCION_LENGTH) {
                throw new IllegalArgumentException("La descripción debe tener al menos " + 
                    MIN_DESCRIPCION_LENGTH + " caracteres");
            }

            if (!DESCRIPCION_PATTERN.matcher(descripcion).matches()) {
                throw new IllegalArgumentException("La descripción contiene caracteres no válidos");
            }

            // Validar y parsear el tiempo de retraso
            String retrasoTexto = campoRetraso.getText().replace("#", "0").trim();
            String[] tiempoPartes = retrasoTexto.split(":");
            
            if (tiempoPartes.length != 2) {
                throw new IllegalArgumentException("Formato de tiempo inválido. Use HHH:mm");
            }

            int horas = Integer.parseInt(tiempoPartes[0]);
            int minutos = Integer.parseInt(tiempoPartes[1]);

            if (minutos < 0 || minutos >= 60) {
                throw new IllegalArgumentException("Los minutos deben estar entre 0 y 59");
            }

            if (horas < 0 || horas > 999) {
                throw new IllegalArgumentException("Las horas deben estar entre 0 y 999");
            }

            Duration retraso = Duration.ofHours(horas).plusMinutes(minutos);
            if (retraso.compareTo(MAX_RETRASO) > 0) {
                throw new IllegalArgumentException("El retraso máximo permitido es 999:59");
            }

            // Obtener y validar la obra
            Obra obra = obraRepo.obtenerObraPorId(idObra);
            if (obra == null) {
                throw new IllegalArgumentException("No se encontró la obra especificada");
            }
            
            // Inicializar tiempoTotal si es null con el tiempo estimado del presupuesto
            if (obra.getTiempoTotal() == null) {
                if (obra.getPresupuesto() == null || obra.getPresupuesto().getTiempoEstimado() == null) {
                    throw new IllegalStateException("La obra no tiene un tiempo estimado definido");
                }
                obra.setTiempoTotal(obra.getPresupuesto().getTiempoEstimado());
            }
            
            // Actualizar el tiempo total sumando el retraso
            obra.setTiempoTotal(obra.getTiempoTotal().plus(retraso));
            obra = obraRepo.actualizarObra(obra);

            // Crear y guardar la incidencia
            Incidencia incidencia = new Incidencia(
                    (Incidencia.TipoIncidencia) comboTipo.getSelectedItem(),
                    descripcion,
                    retraso,
                    obra
            );

            obraRepo.insertarIncidencia(incidencia);
            
            // Actualizar la tabla
            cargarIncidencias();

            // Limpiar campos
            campoDescripcion.setText("");
            campoRetraso.setValue("000:00");
            campoDescripcion.requestFocus();

            JOptionPane.showMessageDialog(this,
                "Incidencia registrada correctamente",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error al registrar la incidencia: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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
        // Verificar si hay datos sin guardar
        boolean hayDatosSinGuardar = !campoDescripcion.getText().trim().isEmpty() || 
                                   !"000:00".equals(campoRetraso.getText());
                                   
        if (hayDatosSinGuardar) {
            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "Hay datos sin guardar. ¿Está seguro que desea salir?",
                    "Confirmar Salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) {
                cerrarDialogo();
            }
        } else {
            cerrarDialogo();
        }
    }

    private void createUIComponents() {
        try {
            MaskFormatter horaMinutoFormatter = new MaskFormatter("###:##");
            horaMinutoFormatter.setPlaceholderCharacter('#');
            horaMinutoFormatter.setValidCharacters("0123456789");
            campoRetraso = new JFormattedTextField(horaMinutoFormatter);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null,
                "Error al crear el campo de retraso: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
