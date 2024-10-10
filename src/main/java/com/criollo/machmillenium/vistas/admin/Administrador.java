package com.criollo.machmillenium.vistas.admin;

import javax.swing.*;

public class Administrador {
    public JPanel panel;
    private JTabbedPane panelMenu;
    private JPanel panelClientes;
    private JPanel panelPersonal;
    private JPanel panelObras;
    private JPanel panelMaquinarias;
    private JPanel panelMonitoreo;
    private JTable tablaClientes;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JTable tablaPersonal;
    private JButton agregarButton;
    private JButton editarButton;
    private JButton inactivarButton;
    private JTable tablaObras;
    private JButton agregarButton1;
    private JButton editarButton1;
    private JButton eliminarButton;
    private JTable tablaMaquinarias;
    private JButton agregarButton2;
    private JButton editarButton2;
    private JButton eliminarButton1;
    private JButton tiposMaquinariaButton;
    private JTable table1;
    private JPanel panelPresupuesto;
    private JTable table2;
    private JButton agregarButton3;
    private JButton editarButton3;
    private JButton eliminarButton2;
    private JFrame jframe;

    public Administrador() {}

    public Administrador(JFrame jframe) {
        this.jframe = jframe;
    }
}
