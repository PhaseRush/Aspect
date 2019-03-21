package main.utility.grapher;

import main.utility.Visuals;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class LineChart {


    public static BufferedImage generateOnePlot(String chartName, String dataName, float[] dataSet1x, float[] dataSet1y) {
        XYSeries series = new XYSeries(dataName);

        for (int i = 0; i < dataSet1x.length; i++) {
            series.add(dataSet1x[i], dataSet1y[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        XYPlot plot = new XYPlot();
        plot.setDataset(dataset);

        plot.setRenderer(new XYSplineRenderer());
        plot.setDomainAxis(new NumberAxis("Band"));
        plot.setRangeAxis(new NumberAxis("Weight"));

        //generate the chart
        JFreeChart chart = new JFreeChart(chartName, Visuals.Fonts.MONTSERRAT.getFont(), plot, false);
        chart.setBackgroundPaint(Color.WHITE);


        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.BLACK);

        return chart.createBufferedImage(800,500);
    }

    public static ByteArrayOutputStream generateTwoPlot(String name1, String name2, double[] dataSet1x, double[] dataSet2x, double[] dataSet1y, double[] dataSet2y) {
        XYSeries series1 = new XYSeries(name1);
        XYSeries series2 = new XYSeries(name2);

        for (int i = 0; i < dataSet1y.length; i++) {
            series1.add(dataSet1x[i], dataSet1y[i]);
        }
        for (int i = 0; i < dataSet2y.length; i++) {
            series2.add(dataSet2x[i], dataSet2y[i]);
        }

        //create the datasets
        XYSeriesCollection dataset1 = new XYSeriesCollection();
        XYSeriesCollection dataset2 = new XYSeriesCollection();
        dataset1.addSeries(series1);
        dataset2.addSeries(series2);

        //construct the plot
        XYPlot plot = new XYPlot();
        plot.setDataset(0, dataset1);
        plot.setDataset(1, dataset2);

        //customize the plot with renderers and axis
        plot.setRenderer(0, new XYSplineRenderer());//use default fill paint for first series
        XYSplineRenderer splinerenderer = new XYSplineRenderer();
        splinerenderer.setSeriesFillPaint(0, Color.BLUE);
        plot.setRenderer(1, splinerenderer);
        plot.setRangeAxis(0, new NumberAxis(name1));
        plot.setRangeAxis(1, new NumberAxis(name2));
        plot.setDomainAxis(new NumberAxis("Time (minutes ago)"));

        //Map the data to the appropriate axis
        plot.mapDatasetToRangeAxis(0, 0);
        plot.mapDatasetToRangeAxis(1, 1);

        //generate the chart
        JFreeChart chart = new JFreeChart("System Cpu and Memory vs time", new Font("Arial", Font.PLAIN, 20), plot, true);
        chart.setBackgroundPaint(Color.WHITE);


        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.BLACK);

        BufferedImage objBufferedImage = chart.createBufferedImage(800,500);
        return Visuals.buffImgToOutputStream(objBufferedImage, "png");
    }

}
