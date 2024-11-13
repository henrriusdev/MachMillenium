package com.criollo.machmillenium.utilidades;

import com.criollo.machmillenium.vistas.emergentes.RecuperarClave;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

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

}
