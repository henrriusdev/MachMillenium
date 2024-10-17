package com.criollo.machmillenium.vistas.emergentes.obra;

import com.criollo.machmillenium.entidades.*;
import com.criollo.machmillenium.repos.ObraRepo;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class RegistrarObra {
    private JTextField campoNombre;
    private JTextArea campoDescripcion;
    private JComboBox<String> comboboxEstado;
    private JFormattedTextField campoArea;
    private JComboBox<String> comboboxPresupuesto;
    private JComboBox<String> comboboxTipoObra;
    private JButton cerrarButton;
    private JButton agregarButton;
    private JList<String> listaMateriales;
    private JList<String> listaMaquinaria;
    private JList<String> listaPersonal;
    public JPanel panel;

    public RegistrarObra(ObraRepo obraRepo) {
        cerrarButton.addActionListener(e -> SwingUtilities.getWindowAncestor(cerrarButton).dispose());
        agregarButton.addActionListener(e -> {
            Obra obra = new Obra();
            obra.setNombre(campoNombre.getText());
            obra.setDescripcion(campoDescripcion.getText());
            obra.setEstado((String) comboboxEstado.getSelectedItem());
            obra.setArea(Double.parseDouble(campoArea.getText()));
            String descripcionPresupuesto = ((String) Objects.requireNonNull(comboboxPresupuesto.getSelectedItem())).split(" -- ")[0];

            obra.setPresupuesto(obraRepo.obtenerPresupuestoPorDescripcion(descripcionPresupuesto));
            obra.setTipoObra(obraRepo.obtenerTipoObraPorNombre((String) comboboxTipoObra.getSelectedItem()));
            obra = obraRepo.insertarObra(obra);

            Obra finalObra = obra;
            List<ObraMaterial> materiales = listaMateriales.getSelectedValuesList().stream().map(material -> {
                String[] materialParts = material.split(" -- ");
                return new ObraMaterial(finalObra, obraRepo.obtenerMaterialPorNombre(materialParts[0]));
            }).toList();
            materiales.forEach(obraRepo::insertarObraMaterial);

            List<ObraPersonal> personal = listaPersonal.getSelectedValuesList().stream().map(personalString -> {
                String[] personalParts = personalString.split(" -- ");
                return new ObraPersonal(finalObra, obraRepo.obtenerPersonalPorNombre(personalParts[0]));
            }).toList();
            personal.forEach(obraRepo::insertarObraPersonal);

            List<ObraMaquinaria> maquinaria = listaMaquinaria.getSelectedValuesList().stream().map(maquinariaString -> {
                String[] maquinariaParts = maquinariaString.split(" -- ");
                return new ObraMaquinaria(finalObra, obraRepo.obtenerMaquinariaPorNombre(maquinariaParts[0]));
            }).toList();
            maquinaria.forEach(obraRepo::insertarObraMaquinaria);

            JOptionPane.showMessageDialog(null, "Obra registrada correctamente", "Registro de obra", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.getWindowAncestor(cerrarButton).dispose();
        });
    }

    private void createUIComponents() {
        ObraRepo obraRepo = new ObraRepo();
        List<TipoObra> tipoObras = obraRepo.obtenerTiposObra();
        String[] tipoObraStrings = tipoObras.stream().map(TipoObra::getNombre).toArray(String[]::new);
        comboboxTipoObra = new JComboBox<>(tipoObraStrings);

        List<String> estados = List.of("Activa", "Inactiva", "Finalizada", "Cancelada", "Espera", "En aprobación");
        comboboxEstado = new JComboBox<>(estados.toArray(new String[0]));

        List<String> presupuestos = obraRepo.obtenerPresupuestos().stream().map(presupuesto -> presupuesto.getDescripcion() + " -- Bs. " + presupuesto.getCosto()).toList();
        comboboxPresupuesto = new JComboBox<>(presupuestos.toArray(new String[0]));

        listaMateriales = new JList<>(obraRepo.obtenerMateriales().stream().map(material -> material.getNombre() + " -- Bs. " + material.getCosto()).toArray(String[]::new));
        listaMaquinaria = new JList<>(obraRepo.obtenerMaquinaria().stream().map(maquinaria -> maquinaria.getNombre() + " -- Bs. " + maquinaria.getCostoTotal()).toArray(String[]::new));
        listaPersonal = new JList<>(obraRepo.obtenerPersonal().stream().map(personal -> personal.getNombre() + " -- " + personal.getEspecialidad().getNombre()).toArray(String[]::new));

        DecimalFormat decimalFormat = new DecimalFormat("#,###.00");
        campoArea = new JFormattedTextField(decimalFormat);
    }
}
