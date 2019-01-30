package main.utility.grapher;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LineChart {

    public static ByteArrayOutputStream generateTwoPlot(String name1, String name2, double[] dataSet1x, double[] dataSet2x, double[] dataSet1y, double[] dataSet2y) {
        //create the series - add some dummy data
        XYSeries series1 = new XYSeries(name1);
        XYSeries series2 = new XYSeries(name2);

        for (int i = 0; i < dataSet1y.length; i++) {
            series1.add(dataSet1x[i], dataSet1y[i]);
        }
        for (int i = 0; i < dataSet2y.length; i++) {
            series2.add(dataSet2x[i], dataSet2y[i]);
        }

        // FAKE DATA
//        double[] timeScale = {
//                -15, -14.5, -14, -13.5, -13, -12.5, -12, -11.5, -11, -10.5,
//                -10, -9.5, -9, -8.5, -8, -7.5, -7, -6.5, -6, -5.5, -5,
//                -4.5, -4, -3.5, -3, -2.5, -2, -1.5, -1, -.5, 0};
//
//        for (int i = 0; i < 30; i++) {
//            series1.add(timeScale[i], 100-i*2);
//            series2.add(timeScale[i], 50+i);
//        }
        // END FAKE DATA

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
        JFreeChart chart = new JFreeChart("System Cpu and Memory vs time", null, plot, true);
        chart.setBackgroundPaint(Color.WHITE);


        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.BLACK);

        BufferedImage objBufferedImage = chart.createBufferedImage(800,500);
        ByteArrayOutputStream bas = new ByteArrayOutputStream();

        try {
            ImageIO.write(objBufferedImage, "png", bas);
            return bas;
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            String dir = System.getProperty("user.dir") + "/stats_time_" + UUID.randomUUID().toString() + ".png";
//            File file = new File(dir);
//            ChartUtilities.saveChartAsPNG(file, chart, 800, 500);
//            return dir;
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

        return null;
    }
}