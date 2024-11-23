package com.criollo.machmillenium.vistas.emergentes.obra;

import com.criollo.machmillenium.entidades.*;
import com.criollo.machmillenium.repos.ObraRepo;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class ModificarRegistroObra {
    public JPanel panel;
    private JTextField campoNombre;
    private JTextArea campoDescripcion;
    private JComboBox<String> comboboxEstado;
    private JFormattedTextField campoArea;
    private JList<String> listaMateriales;
    private JList<String> listaPersonal;
    private JList<String> listaMaquinaria;
    private JButton cerrarButton;
    private JButton editarButton;
    private JComboBox<String> comboboxPresupuesto;
    private JComboBox<String> comboboxTipoObra;
    private final ObraRepo obraRepo;
    private final Obra obra;

    public ModificarRegistroObra(ObraRepo obraRepo, Obra obra) {
        if (obra == null) {
            this.obraRepo = null;
            this.obra = null;
            JOptionPane.showMessageDialog(null, "No se ha seleccionado una obra", "Error", JOptionPane.ERROR_MESSAGE);
            SwingUtilities.getWindowAncestor(cerrarButton).dispose();
            return;
        }

        this.obraRepo = obraRepo;
        this.obra = obra;
        llenarCampos();

        cerrarButton.addActionListener(e -> SwingUtilities.getWindowAncestor(cerrarButton).dispose());
        editarButton.addActionListener(e -> {
            Obra obraEditada = new Obra();
            obraEditada.setId(this.obra.getId());
            obraEditada.setNombre(campoNombre.getText());
            obraEditada.setDescripcion(campoDescripcion.getText());
            obraEditada.setEstado((String) comboboxEstado.getSelectedItem());
            obraEditada.setArea(Double.parseDouble(campoArea.getText()));
            String descripcionPresupuesto = ((String) Objects.requireNonNull(comboboxPresupuesto.getSelectedItem())).split(" -- ")[0];

            obraEditada.setPresupuesto(obraRepo.obtenerPresupuestoPorDescripcion(descripcionPresupuesto));
            obraEditada.setTipoObra(obraRepo.obtenerTipoObraPorNombre((String) comboboxTipoObra.getSelectedItem()));
            obraEditada = obraRepo.actualizarObra(obraEditada);

            Obra finalObra = obraEditada;
            // Lista de materiales actuales desde la base de datos
            List<ObraMaterial> materialesExistentes = obraRepo.obtenerMaterialesPorObra(finalObra.getId());

// Lista de materiales seleccionados por el usuario
            List<ObraMaterial> materialesSeleccionados = listaMateriales.getSelectedValuesList().stream().map(material -> {
                String[] materialParts = material.split(" -- ");
                Long cantidad = Long.parseLong(JOptionPane.showInputDialog(null, "Cantidad de " + materialParts[0]));
                return new ObraMaterial(finalObra, obraRepo.obtenerMaterialPorNombre(materialParts[0]), cantidad);
            }).toList();

// Eliminar materiales que ya no están seleccionados
            materialesExistentes.stream()
                    .filter(materialExistente -> !materialesSeleccionados.contains(materialExistente))
                    .forEach(obraRepo::eliminarObraMaterial);

// Insertar materiales nuevos seleccionados
            materialesSeleccionados.stream()
                    .filter(materialSeleccionado -> !materialesExistentes.contains(materialSeleccionado))
                    .forEach(obraRepo::insertarObraMaterial);

// Mantener el mismo enfoque para personal y maquinaria
// Para personal:
            List<ObraPersonal> personalExistentes = obraRepo.obtenerPersonalPorObra(finalObra.getId());
            List<ObraPersonal> personalSeleccionados = listaPersonal.getSelectedValuesList().stream().map(personalString -> {
                String[] personalParts = personalString.split(" -- ");
                return new ObraPersonal(finalObra, obraRepo.obtenerPersonalPorNombre(personalParts[0]));
            }).toList();

// Eliminar personal que ya no está seleccionado
            personalExistentes.stream()
                    .filter(personalExistente -> !personalSeleccionados.contains(personalExistente))
                    .forEach(obraRepo::eliminarObraPersonal);

// Insertar personal nuevo seleccionado
            personalSeleccionados.stream()
                    .filter(personalSeleccionado -> !personalExistentes.contains(personalSeleccionado))
                    .forEach(obraRepo::insertarObraPersonal);

// Para maquinaria:
            List<ObraMaquinaria> maquinariaExistentes = obraRepo.obtenerMaquinariaPorObra(finalObra.getId());
            List<ObraMaquinaria> maquinariaSeleccionadas = listaMaquinaria.getSelectedValuesList().stream().map(maquinariaString -> {
                String[] maquinariaParts = maquinariaString.split(" -- ");
                return new ObraMaquinaria(finalObra, obraRepo.obtenerMaquinariaPorNombre(maquinariaParts[0]));
            }).toList();

// Eliminar maquinaria que ya no está seleccionada
            maquinariaExistentes.stream()
                    .filter(maquinariaExistente -> !maquinariaSeleccionadas.contains(maquinariaExistente))
                    .forEach(obraRepo::eliminarObraMaquinaria);

            // Insertar maquinaria nueva seleccionada
            maquinariaSeleccionadas.stream()
                    .filter(maquinariaSeleccionada -> !maquinariaExistentes.contains(maquinariaSeleccionada))
                    .forEach(obraRepo::insertarObraMaquinaria);

            JOptionPane.showMessageDialog(null, "Obra modificada correctamente", "Registro de obra", JOptionPane.INFORMATION_MESSAGE);
            SwingUtilities.getWindowAncestor(cerrarButton).dispose();
        });
    }

    private void llenarCampos(){
        campoNombre.setText(obra.getNombre());
        campoDescripcion.setText(obra.getDescripcion());
        comboboxEstado.setSelectedItem(obra.getEstado());
        campoArea.setText(String.valueOf(obra.getArea()));
        comboboxPresupuesto.setSelectedItem(obra.getPresupuesto().getDescripcion() + " -- Bs. " + obra.getPresupuesto().getCosto());
        comboboxPresupuesto.setEnabled(false);
        comboboxTipoObra.setSelectedItem(obra.getTipoObra().getNombre());

        // Set the selected values of the lists
        List<ObraMaterial> materialesPorObra = obraRepo.obtenerMaterialesPorObra(obra.getId());
        List<ObraPersonal> personalPorObra = obraRepo.obtenerPersonalPorObra(obra.getId());
        List<ObraMaquinaria> maquinariaPorObra = obraRepo.obtenerMaquinariaPorObra(obra.getId());

        listaMateriales.setSelectedIndices(materialesPorObra.stream().mapToInt(material -> {
            int index = 0;
            for (int i = 0; i < listaMateriales.getModel().getSize(); i++) {
                if (listaMateriales.getModel().getElementAt(i).startsWith(material.getMaterial().getNombre())) {
                    index = i;
                    break;
                }
            }
            return index;
        }).toArray());
        listaPersonal.setSelectedIndices(personalPorObra.stream().mapToInt(personal1 -> {
            int index = 0;
            for (int i = 0; i < listaPersonal.getModel().getSize(); i++) {
                if (listaPersonal.getModel().getElementAt(i).startsWith(personal1.getPersonal().getNombre())) {
                    index = i;
                    break;
                }
            }
            return index;
        }).toArray());
        listaMaquinaria.setSelectedIndices(maquinariaPorObra.stream().mapToInt(maquinaria1 -> {
            int index = 0;
            for (int i = 0; i < listaMaquinaria.getModel().getSize(); i++) {
                if (listaMaquinaria.getModel().getElementAt(i).startsWith(maquinaria1.getMaquinaria().getNombre())) {
                    index = i;
                    break;
                }
            }
            return index;
        }).toArray());
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
