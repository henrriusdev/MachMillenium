package com.criollo.machmillenium.utilidades;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.*;

import java.awt.*;

public class GeneradorGraficos {
    public static ChartPanel generarGraficoBarras(String titulo, String ejeX, String ejeY, String[] etiquetas, double[] valores, int ancho, int alto) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < etiquetas.length; i++) {
            dataset.addValue(valores[i], etiquetas[i], etiquetas[i]);
        }

        JFreeChart chart = ChartFactory.createBarChart(titulo, ejeX, ejeY, dataset, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(ancho, alto));

        CategoryPlot plot = chartPanel.getChart().getCategoryPlot();
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setRange(0.0, rangeAxis.getUpperBound());

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter()); // Quitar degradado
        renderer.setSeriesPaint(0, Color.BLUE); // Cambiar color de las barras a sólido (puedes elegir el color)
        return chartPanel;
    }

    public static ChartPanel generarGraficoPastel(String titulo, String[] etiquetas, double[] valores, int ancho, int alto) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        for (int i = 0; i < etiquetas.length; i++) {
            dataset.setValue(etiquetas[i], valores[i]);
        }

        JFreeChart chart = ChartFactory.createPieChart(titulo, dataset, true, true, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(ancho, alto));

        return chartPanel;
    }

    public static ChartPanel generarGraficoDesviacion(String titulo, String ejeX, String ejeY, String tituloSeries, double[] valores, double[] desvios, int ancho, int alto) {
        YIntervalSeriesCollection dataset = new YIntervalSeriesCollection();

        // Crear la serie donde X e Y tienen los mismos valores y sus bandas de desviación
        YIntervalSeries series = new YIntervalSeries(tituloSeries);
        for (int i = 0; i < valores.length; i++) {
            double valor = valores[i];
            double desvio = desvios[i];
            series.add(valor, valor, valor - desvio, valor + desvio); // X = Y, Y-Min, Y-Max
        }
        dataset.addSeries(series);

        // Crear el gráfico con bandas de desviación
        JFreeChart chart = ChartFactory.createXYLineChart(titulo, ejeX, ejeY, dataset);

        // Usar DeviationRenderer para mostrar las bandas de desviación
        XYPlot plot = chart.getXYPlot();
        DeviationRenderer renderer = new DeviationRenderer(true, false);
        renderer.setSeriesFillPaint(0, new java.awt.Color(150, 200, 250, 128)); // Color de la banda
        plot.setRenderer(renderer);

        // Crear el panel de gráfico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(ancho, alto));

        return chartPanel;
    }
}
