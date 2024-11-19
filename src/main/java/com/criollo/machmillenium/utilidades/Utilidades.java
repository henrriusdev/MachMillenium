package com.criollo.machmillenium.utilidades;

import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.TipoInsumo;
import com.criollo.machmillenium.modelos.ModeloSolicitudCompra;
import com.criollo.machmillenium.repos.TipoInsumoRepo;
import com.criollo.machmillenium.vistas.emergentes.RecuperarClave;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class Utilidades {
    public static final String[] prefijosTelefonicosVenezuela = {
        // Prefijos de líneas móviles
        "(0412)", // Movistar
        "(0414)", // Movilnet
        "(0416)", // Movilnet
        "(0416)", // Digitel
        "(0424)", // Movistar
        "(0426)", // Digitel

        // Prefijos de líneas fijas por estado
        "(0212)", // Caracas (Distrito Capital)
        "(0240)", // Aragua
        "(0241)", // Carabobo
        "(0242)", // Aragua
        "(0243)", // Aragua
        "(0244)", // Guárico
        "(0245)", // Carabobo
        "(0246)", // Guárico
        "(0251)", // Lara
        "(0252)", // Lara
        "(0253)", // Portuguesa
        "(0254)", // Portuguesa
        "(0255)", // Yaracuy
        "(0256)", // Cojedes
        "(0257)", // Lara
        "(0261)", // Zulia
        "(0262)", // Zulia
        "(0263)", // Zulia
        "(0264)", // Falcón
        "(0265)", // Zulia
        "(0266)", // Zulia
        "(0271)", // Mérida
        "(0272)", // Mérida
        "(0273)", // Trujillo
        "(0274)", // Mérida
        "(0275)", // Trujillo
        "(0276)", // Táchira
        "(0277)", // Táchira
        "(0281)", // Anzoátegui
        "(0282)", // Anzoátegui
        "(0283)", // Anzoátegui
        "(0284)", // Bolívar
        "(0285)", // Bolívar
        "(0286)", // Bolívar
        "(0287)", // Sucre
        "(0288)", // Sucre
        "(0289)", // Nueva Esparta
        "(0291)", // Sucre
        "(0292)", // Nueva Esparta
        "(0293)", // Sucre
        "(0294)", // Nueva Esparta
        "(0295)", // Nueva Esparta
        "(0296)", // Monagas
        "(0297)", // Delta Amacuro
        "(0298)", // Monagas
        "(0299)"  // Delta Amacuro
    };

    public static NumberFormatter getNumberFormatter() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(true);
        NumberFormatter numberFormatter = new NumberFormatter(format);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(false);
        numberFormatter.setMinimum(0L);
        numberFormatter.setMaximum(1_999_999_999L);
        return numberFormatter;
    }

    public static NumberFormatter getNumberFormatterSinGrupo() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        NumberFormatter numberFormatter = new NumberFormatter(format);
        numberFormatter.setValueClass(Long.class);
        numberFormatter.setAllowsInvalid(true);
        numberFormatter.setMinimum(0L);
        numberFormatter.setMaximum(1_999_999_999L);
        return numberFormatter;
    }

    public static void cambiarClaveOriginal(String clavePersistida, Long idPersonal, boolean inicial) {
        if (inicial && BCrypt.checkpw("12345678", clavePersistida)) {
            mostrarRecuperarClave(clavePersistida, idPersonal);
        } else if (!inicial) {
            mostrarRecuperarClave(clavePersistida, idPersonal);
        }
    }

    private static void mostrarRecuperarClave(String clavePersistida, Long idPersonal) {
        // Crear el panel principal
        RecuperarClave recuperarClave = new RecuperarClave(clavePersistida, idPersonal);

        // Crear el JDialog modal
        JDialog dialog = new JDialog((JFrame) null, "Recuperar Clave", true); // 'true' para que sea modal
        dialog.setContentPane(recuperarClave.mainPanel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null); // Centrar el diálogo
        dialog.setVisible(true); // Esto bloquea la ejecución hasta que se cierre el diálogo
    }

    public static boolean esNumero(String cadena) {
        try {
            Long.parseLong(cadena);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void generarSolicitudCompra(Personal personal, JPanel panel) {
        TipoInsumoRepo tipoInsumoRepo = new TipoInsumoRepo();

        // Create and configure the custom dialog
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(panel), "Generar Solicitud de Compra", true);
        JPanel dialogPanel = new JPanel(new GridBagLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create form components
        JTextField nombreMaterialField = new JTextField(20);
        JComboBox<String> tipoMaterialCombo = new JComboBox<>(tipoInsumoRepo.obtenerTodos().stream()
                .map(TipoInsumo::getNombre)
                .toArray(String[]::new));
        JTextField cantidadField = new JTextField(10);
        cantidadField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }
        });

        JTextField presentacionField = new JTextField(20);
        JTextArea justificacionArea = new JTextArea(4, 40);
        justificacionArea.setLineWrap(true);
        justificacionArea.setWrapStyleWord(true);
        JScrollPane justificacionScroll = new JScrollPane(justificacionArea);

        // Date components - with current year + 5 years
        int currentYear = LocalDateTime.now().getYear();
        JComboBox<Integer> diaCombo = new JComboBox<>(IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new));
        JComboBox<Integer> mesCombo = new JComboBox<>(IntStream.rangeClosed(1, 12).boxed().toArray(Integer[]::new));
        JComboBox<Integer> anioCombo = new JComboBox<>(
                IntStream.rangeClosed(currentYear, currentYear + 5)
                        .boxed()
                        .toArray(Integer[]::new)
        );

        // Add components using GridBagLayout

        // Nombre del Material
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        dialogPanel.add(new JLabel("Nombre del Material:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 2;
        dialogPanel.add(nombreMaterialField, gbc);

        // Tipo de Material
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        dialogPanel.add(new JLabel("Tipo de Material:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 2;
        dialogPanel.add(tipoMaterialCombo, gbc);

        // Cantidad
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        dialogPanel.add(new JLabel("Cantidad:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialogPanel.add(cantidadField, gbc);

        // Presentación
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        dialogPanel.add(new JLabel("Presentación:"), gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialogPanel.add(presentacionField, gbc);

        // Justificación
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        dialogPanel.add(new JLabel("Justificación:"), gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        dialogPanel.add(justificacionScroll, gbc);

        // Fecha Límite
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0.0;
        dialogPanel.add(new JLabel("Fecha Límite:"), gbc);

        // Date panel
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("Día:"));
        datePanel.add(diaCombo);
        datePanel.add(new JLabel("Mes:"));
        datePanel.add(mesCombo);
        datePanel.add(new JLabel("Año:"));
        datePanel.add(anioCombo);

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 3;
        dialogPanel.add(datePanel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("Generar");
        JButton cancelButton = new JButton("Cancelar");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 3;
        dialogPanel.add(buttonPanel, gbc);

        // Add action listeners
        AtomicBoolean confirmed = new AtomicBoolean(false);
        okButton.addActionListener(evt -> {
            // Validate inputs
            if (nombreMaterialField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor ingrese el nombre del material", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Utilidades.esNumero(cantidadField.getText())) {
                JOptionPane.showMessageDialog(dialog, "La cantidad debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (presentacionField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor ingrese la presentación", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (justificacionArea.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Por favor ingrese la justificación", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            confirmed.set(true);
            dialog.dispose();
        });

        cancelButton.addActionListener(evt -> dialog.dispose());

        // Show dialog
        dialog.add(dialogPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        // If user confirmed, generate the report
        if (confirmed.get()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String fecha = LocalDateTime.now().format(formatter);
            String solicitante = personal.getNombre();
            String cargo = personal.getRol().getNombre();

            String fechaLimite = String.format("%d/%d/%d",
                    diaCombo.getSelectedItem(),
                    mesCombo.getSelectedItem(),
                    anioCombo.getSelectedItem());

            ModeloSolicitudCompra modeloSolicitudCompra = new ModeloSolicitudCompra(
                    fecha,
                    solicitante,
                    cargo,
                    nombreMaterialField.getText().trim(),
                    (String) tipoMaterialCombo.getSelectedItem(),
                    cantidadField.getText(),
                    presentacionField.getText().trim(),
                    justificacionArea.getText().trim(),
                    fechaLimite
            );
            GeneradorReportes.generarSolicitud(modeloSolicitudCompra);
        }
    }
}
