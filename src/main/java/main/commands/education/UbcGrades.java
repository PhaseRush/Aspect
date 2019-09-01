package main.commands.education;

import com.google.gson.annotations.SerializedName;
import main.Command;
import main.utility.Visuals;
import main.utility.metautil.BotUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

public class UbcGrades implements Command {
    private static final String BASE_URl = "https://ubcgrades.com/api";

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        String time = "2018W";
        final String subject;
        final String courseNumber;
        String courseSection = "OVERALL";

        if (args.get(0).matches("\\d{4}[A-Za-z]")) { // did not omit first argument
            time = args.get(0);
            subject = args.get(1);
            courseNumber = args.get(2);
            if (args.size() == 4) {
                courseSection = args.get(3);
            }
        } else {
            subject = args.get(0);
            courseNumber = args.get(1);
            if (args.size() == 3) {
                courseSection = args.get(2);
            }
        }

        final String requestUrl = BASE_URl + String.join("/", Arrays.asList(time, subject, courseNumber, courseSection));
        Response response = BotUtils.gson.fromJson(BotUtils.getStringFromUrl(requestUrl), Response.class);

        CategoryDataset dataset = generateDataSet(response.grades);
        JFreeChart barChart = ChartFactory.createBarChart(
                response.id,
                "",
                "Percent",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        BotUtils.send(event.getChannel(),
                new EmbedBuilder().withImage("attachment://grade_dist.png"),
                genChart(barChart),
                "grade_dist.png");
    }

    private ByteArrayInputStream genChart(JFreeChart barChart) {
        barChart.setBackgroundPaint(Color.WHITE);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.BLACK);

        BufferedImage bufferedImage = barChart.createBufferedImage(800, 500);
        return new ByteArrayInputStream(Visuals.buffImgToOutputStream(bufferedImage, "png").toByteArray());
    }

    private CategoryDataset generateDataSet(Grades grades) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(grades.zero, "Percent", "0-9%");
        dataset.addValue(grades.ten, "Percent", "10-19%");
        dataset.addValue(grades.twenty, "Percent", "20-29%");
        dataset.addValue(grades.thirty, "Percent", "30-39%");
        dataset.addValue(grades.forty, "Percent", "40-49%");
        dataset.addValue(grades.fifty, "Percent", "50-54%");
        dataset.addValue(grades.fiftyfive, "Percent", "55-59%");
        dataset.addValue(grades.sixty, "Percent", "60-63%");
        dataset.addValue(grades.sixtyfour, "Percent", "64-67%");
        dataset.addValue(grades.sixtyeight, "Percent", "68-71%");
        dataset.addValue(grades.seventytwo, "Percent", "72-75%");
        dataset.addValue(grades.seventysix, "Percent", "76-79%");
        dataset.addValue(grades.eighty, "Percent", "80-84%");
        dataset.addValue(grades.eightyfive, "Percent", "85-89%");
        dataset.addValue(grades.ninety, "Percent", "90-100%");
        return dataset;
    }

    @Override
    public String getDesc() {
        return "Shows a bar graph representing grade distribution for the specified course, across all sections";
    }

    @Override
    public String getSyntax() {
        return "`$grades [YearSession], [Subject], [Course #], [Section]`\nex:\t`$grades 2018W, CPSC, 110, 101`\n" +
                "Notes: If yearSession is omitted, defaults to 2018W. if Section omitted, defaults to OVERALL";
    }


    private class Response {
        Grades grades;
        Stats stats;
        String id, yearsession, session, subject, course, section, title, instructor;
        int year, enrolled;
    }

    private class Grades {
        @SerializedName("0-9%")
        int zero;

        @SerializedName("10-19%")
        int ten;

        @SerializedName("20-29%")
        int twenty;

        @SerializedName("30-39%")
        int thirty;

        @SerializedName("40-49%")
        int forty;

        @SerializedName("<50%")
        int failed;

        @SerializedName("50-54%")
        int fifty;

        @SerializedName("55-59%")
        int fiftyfive;

        @SerializedName("60-63%")
        int sixty;

        @SerializedName("64-67%")
        int sixtyfour;

        @SerializedName("68-71%")
        int sixtyeight;

        @SerializedName("72-75%")
        int seventytwo;

        @SerializedName("76-79%")
        int seventysix;

        @SerializedName("80-84%")
        int eighty;

        @SerializedName("85-89%")
        int eightyfive;

        @SerializedName("90-100%")
        int ninety;

    }

    private class Stats {
        float average, stdev, high, low, pass, fail, withdrew, audit, other;
    }
}
