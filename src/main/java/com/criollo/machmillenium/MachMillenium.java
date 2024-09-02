/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.criollo.machmillenium;

import com.criollo.machmillenium.vistas.Inicio;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
/**
 *
 * @author henrrius
 */
public class MachMillenium {

    public static void main(String[] args) {
        // Esto garantiza que toda la interfaz gráfica se ejecute en el hilo de despacho de eventos
        SwingUtilities.invokeLater(() -> {
            // Crear el JFrame
            JFrame frame = new JFrame("MachMillenium");

            // Establecer el comportamiento para el botón de cerrar
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Crear la instancia del JPanel Inicio
            Inicio inicio = new Inicio();

            // Agregar el JPanel al JFrame
            frame.getContentPane().add(inicio);

            // Ajustar el tamaño del JFrame según el contenido
            frame.pack();

            // Hacer que la ventana sea visible
            frame.setVisible(true);
        });
    }
}
