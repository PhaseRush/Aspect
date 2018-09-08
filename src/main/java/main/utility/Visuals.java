package main.utility;

import sx.blah.discord.handle.impl.obj.Embed.EmbedField;
import sx.blah.discord.util.EmbedBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Visuals {
    private Random r = new Random();
    private static Random rs = ThreadLocalRandom.current();

    public static Color getVibrantColor() {
        // this always generates the same color on startup.
        return Color.getHSBColor(rs.nextFloat(), .9f, 1.0f);
    }

    public static BufferedImage urlToBufferedImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }

    public static Color analyizeImageColor(BufferedImage bi) {
        long sumR = 0, sumG = 0, sumB = 0;

        for (int i = 0; i < bi.getWidth(); i++) { // gets fucked at some urls
            for (int j = 0; j < bi.getHeight(); j++) {
                Color pixelColor = new Color(bi.getRGB(i,j));
                sumR += pixelColor.getRed();
                sumG += pixelColor.getGreen();
                sumB += pixelColor.getBlue();
            }
        }
        long avgR = sumR/255;
        long avgG = sumG/255;
        long avgB = sumB/255;

        float[] hsbArray = Color.RGBtoHSB((int) avgR, (int)avgG, (int)avgB, null);
        return Color.getHSBColor(hsbArray[0], .9f, 1.0f);
    }

    public static Color analyzeImageColor(Image img) {
        return analyizeImageColor(convertImageToBufferedImage(img));
    }

    public static Color analyizeWeightedImageColor(Image i, int ratio) {
        BufferedImage bi = convertImageToBufferedImage(i);

        BufferedImage img = bi.getSubimage(bi.getWidth()/ratio, bi.getHeight()/ratio, bi.getWidth()*(ratio-1)/ratio, bi.getHeight()*(ratio-1)/ratio); //fill in the corners of the desired crop location here
        BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(img, 0, 0, null);

        return analyizeImageColor(copyOfImage);
    }

    //https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
    private static BufferedImage convertImageToBufferedImage(Image img) {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public Color getRandomHSBColor() {
        return Color.getHSBColor(r.nextFloat(), r.nextFloat(), r.nextFloat());
    }


    /**
     * get rid of this thing ASAP
     *
     * @param title
     * @param url
     * @param description
     * @param color
     * @param epochTime
     * @param thumbnailURL
     * @param authorName
     * @param authorIcon
     * @param authorURL
     * @return
     */
    public static EmbedBuilder getEmbedBuilderNoField(String title, String url, String description, Color color, long epochTime, String thumbnailURL,
                                                      String authorName, String authorIcon, String authorURL) {
        return new EmbedBuilder()
                .withTitle(title)
                .withUrl(url)
                .withDesc(description)
                .withColor(color)
                .withTimestamp(epochTime)
                .withThumbnail(thumbnailURL)
                .withAuthorName(authorName)
                .withAuthorIcon(authorIcon)
                .withAuthorUrl(authorURL);
    }

    /**
     * get rid of this thing as well someday
     * @param title
     * @param value
     * @param inLine
     * @return
     */
    public EmbedField getEmbedField(String title, String value, boolean inLine) {
        return new EmbedField(title, value, inLine);
    }

}
