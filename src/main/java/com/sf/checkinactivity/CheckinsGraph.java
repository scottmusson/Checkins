package com.sf.checkinactivity;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;
import org.xml.sax.SAXException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class CheckinsGraph
    extends JFrame {
    private Checkins checkins;

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public CheckinsGraph(String title, Checkins checkins) {

        super(title);
        this.checkins = checkins;

        CategoryDataset dataset = createDetailedDataset();
        JFreeChart chart = createChart(title, dataset);
        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);

    }

    private CategoryDataset createDetailedDataset() {

        // column keys...
        String monday = Checkins.DAYS_OF_WEEK.MONDAY.toString();
        String tuesday = Checkins.DAYS_OF_WEEK.TUESDAY.toString();
        String wednesday = Checkins.DAYS_OF_WEEK.WEDNESDAY.toString();
        String thursday = Checkins.DAYS_OF_WEEK.THURSDAY.toString();
        String friday = Checkins.DAYS_OF_WEEK.FRIDAY.toString();
        String saturday = Checkins.DAYS_OF_WEEK.SATURDAY.toString();
        String sunday = Checkins.DAYS_OF_WEEK.SUNDAY.toString();

        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (String dev : checkins.getDevelopers()) {
            dataset.addValue(checkins.totalForDayForDev(Checkins.DAYS_OF_WEEK.MONDAY, dev), dev, monday);
            dataset.addValue(checkins.totalForDayForDev(Checkins.DAYS_OF_WEEK.TUESDAY, dev), dev, tuesday);
            dataset.addValue(checkins.totalForDayForDev(Checkins.DAYS_OF_WEEK.WEDNESDAY, dev), dev, wednesday);
            dataset.addValue(checkins.totalForDayForDev(Checkins.DAYS_OF_WEEK.THURSDAY, dev), dev, thursday);
            dataset.addValue(checkins.totalForDayForDev(Checkins.DAYS_OF_WEEK.FRIDAY, dev), dev, friday);
            dataset.addValue(checkins.totalForDayForDev(Checkins.DAYS_OF_WEEK.SATURDAY, dev), dev, saturday);
            dataset.addValue(checkins.totalForDayForDev(Checkins.DAYS_OF_WEEK.SUNDAY, dev), dev, sunday);
        }

        // add the total in
        dataset.addValue(checkins.totalForDay(Checkins.DAYS_OF_WEEK.MONDAY), "Total", monday);
        dataset.addValue(checkins.totalForDay(Checkins.DAYS_OF_WEEK.TUESDAY), "Total", tuesday);
        dataset.addValue(checkins.totalForDay(Checkins.DAYS_OF_WEEK.WEDNESDAY), "Total", wednesday);
        dataset.addValue(checkins.totalForDay(Checkins.DAYS_OF_WEEK.THURSDAY), "Total", thursday);
        dataset.addValue(checkins.totalForDay(Checkins.DAYS_OF_WEEK.FRIDAY), "Total", friday);
        dataset.addValue(checkins.totalForDay(Checkins.DAYS_OF_WEEK.SATURDAY), "Total", saturday);
        dataset.addValue(checkins.totalForDay(Checkins.DAYS_OF_WEEK.SUNDAY), "Total", sunday);

        return dataset;
    }



    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private JFreeChart createChart(String title, CategoryDataset dataset) {

        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
                title,         // chart title
                "Day of Week",               // domain axis label
                "Total",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(
                0.0f, 0.0f, Color.blue,
                0.0f, 0.0f, Color.lightGray
        );
        GradientPaint gp1 = new GradientPaint(
                0.0f, 0.0f, Color.green,
                0.0f, 0.0f, Color.lightGray
        );
        GradientPaint gp2 = new GradientPaint(
                0.0f, 0.0f, Color.red,
                0.0f, 0.0f, Color.lightGray
        );

        GradientPaint gp3 = new GradientPaint(
                0.0f, 0.0f, Color.cyan,
                0.0f, 0.0f, Color.lightGray
        );

        GradientPaint gp4 = new GradientPaint(
                0.0f, 0.0f, Color.magenta,
                0.0f, 0.0f, Color.lightGray
        );
        GradientPaint gp5 = new GradientPaint(
                0.0f, 0.0f, Color.orange,
                0.0f, 0.0f, Color.lightGray
        );
        GradientPaint gp6 = new GradientPaint(
                0.0f, 0.0f, Color.pink,
                0.0f, 0.0f, Color.lightGray
        );
        GradientPaint gp7 = new GradientPaint(
                0.0f, 0.0f, Color.yellow,
                0.0f, 0.0f, Color.lightGray
        );

        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
        renderer.setSeriesPaint(3, gp3);
        renderer.setSeriesPaint(4, gp4);
        renderer.setSeriesPaint(5, gp5);
        renderer.setSeriesPaint(6, gp6);
        renderer.setSeriesPaint(7, gp7);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }

    // ****************************************************************************
    // * JFREECHART DEVELOPER GUIDE                                               *
    // * The JFreeChart Developer Guide, written by David Gilbert, is available   *
    // * to purchase from Object Refinery Limited:                                *
    // *                                                                          *
    // * http://www.object-refinery.com/jfreechart/guide.html                     *
    // *                                                                          *
    // * Sales are used to provide funding for the JFreeChart project - please    *
    // * support us so that we can continue developing free software.             *
    // ****************************************************************************

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        if (args.length > 3) {
            Checkins checkins = new Checkins();
            try {
                String serverName = args[0];
                String userName = args[1];
                String password = args[2];
                String clientName = args[3];
                checkins.p4(serverName, userName, password, clientName, Arrays.asList(new String[]{"nlipke"}), args.length > 5 ? args[4] : null, args.length > 6 ? args[5] : null);

                CheckinsGraph demo = new CheckinsGraph("Total Checkins per Day", checkins);
                demo.pack();
                RefineryUtilities.centerFrameOnScreen(demo);
                demo.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Usage: Checkins <depot xml file> or Checkins <depot xml file> <startDate e.g. 12/1/2011> or Checkins <depot xml file> <startDate e.g. 12/1/2012> <endDate>");
        }
    }
}
