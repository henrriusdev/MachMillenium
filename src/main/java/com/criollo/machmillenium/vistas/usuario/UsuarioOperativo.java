package com.criollo.machmillenium.vistas.usuario;

import com.criollo.machmillenium.entidades.Obra;
import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.repos.ObraRepo;
import com.criollo.machmillenium.utilidades.TableColumnAdjuster;
import com.criollo.machmillenium.utilidades.Utilidades;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Vector;

public class UsuarioOperativo {
    public JPanel panel;
    private JTable tablaObras;
    private final ObraRepo obraRepo;

    public UsuarioOperativo(Personal personal) {
        this.obraRepo = new ObraRepo();

        Utilidades.cambiarClaveOriginal(personal.getClave(), personal.getId(), true);
        setTableObraModel();
    }

    public void ajustarAnchoColumnas(JTable tabla) {
        // Desactivar el ajuste automático de tamaño para que el JTable no redimensione automáticamente
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumnModel columnModel = tabla.getColumnModel();

        for (int column = 0; column < tabla.getColumnCount(); column++) {
            TableColumn tableColumn = columnModel.getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < tabla.getRowCount(); row++) {
                TableCellRenderer cellRenderer = tabla.getCellRenderer(row, column);
                Component c = tabla.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + tabla.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                // Si superamos el ancho máximo, no necesitamos revisar más filas
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth);  // Ajustar ancho preferido
        }

        // Aplicar el ajuste de columnas usando el TableColumnAdjuster
        TableColumnAdjuster tca = new TableColumnAdjuster(tabla);
        tca.adjustColumns();  // Ajustar las columnas automáticamente
    }

    public DefaultTableModel mapearModeloObra(List<Obra> obraList) {
        Field[] fields = Obra.class.getDeclaredFields();

        // Create a Vector to hold the column names
        Vector<String> columnNames = new Vector<>();
        for (Field field : fields) {
            if (!field.getName().equals("creado") && !field.getName().equals("modificado") && !field.getName().equals("eliminado")) {
                columnNames.add(field.getName());
            }
        }

        // Create a Vector to hold the data
        Vector<Vector<Object>> data = new Vector<>();
        for (Obra obra : obraList) {
            Vector<Object> row = new Vector<>();
            row.add(obra.getId());
            row.add(obra.getTipoObra().getNombre());
            row.add(obra.getArea());
            row.add(obra.getNombre());
            row.add(obra.getDescripcion());
            row.add(obra.getEstado());
            String presupuesto = obra.getPresupuesto().getDescripcion() + " -- " + obra.getPresupuesto().getCosto();
            row.add(presupuesto);
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public void setTableObraModel() {
        // Create a DefaultTableModel with the column names and data
        DefaultTableModel obraTableModel = mapearModeloObra(obraRepo.obtenerObras());

        // Set the TableModel to the JTable
        tablaObras.setModel(obraTableModel);

        ajustarAnchoColumnas(tablaObras);
    }
}
