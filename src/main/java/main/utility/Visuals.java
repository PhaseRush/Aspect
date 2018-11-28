package main.utility;

import sx.blah.discord.handle.impl.obj.Embed.EmbedField;
import sx.blah.discord.util.EmbedBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Visuals {
    private Random r = new Random();
    private static Random rs = ThreadLocalRandom.current();

    public static Color getVibrantColor() {
        // this always generates the same color on startup.
        return Color.getHSBColor(rs.nextFloat(), .9f, 1.0f);
    }

    /*
    generate random vibrant color with seed
     */
    public static Color getVibrantColor(String seed) {
        return null;
    }

    public static BufferedImage urlToBufferedImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] urlToImageByteArray(String url) {
        try( InputStream in = new BufferedInputStream(new URL(url).openStream());
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=in.read(buf)))
                out.write(buf, 0, n);
            return out.toByteArray();
        } catch (MalformedURLException e) {
            System.out.println("bad url - visuals.urlToByteArray");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("io exception - visuals.urlToByteArray");
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage urlToBufferedImageWithAgentHeader(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0"); // also try .addRequestProperty()
            InputStream inputStream = connection.getInputStream();
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            System.out.println("url to buff img w/agent header - visuals");
            e.printStackTrace();
        }

        return null;
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

        BufferedImage img = bi.getSubimage(bi.getWidth()/ratio, bi.getHeight()/ratio, bi.getWidth()*(ratio-1)/ratio, bi.getHeight()*(ratio-1)/ratio); // fill in the corners of the desired crop location here
        BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(img, 0, 0, null);

        return analyizeImageColor(copyOfImage);
    }

    // https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
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

    // https://stackoverflow.com/questions/3224561/crop-image-to-smallest-size-by-removing-transparent-pixels-in-java
    public static BufferedImage cropTransparent(BufferedImage image) {
        WritableRaster raster = image.getAlphaRaster();
        int width = raster.getWidth();
        int height = raster.getHeight();
        int left = 0;
        int top = 0;
        int right = width - 1;
        int bottom = height - 1;
        int minRight = width - 1;
        int minBottom = height - 1;

        top:
        for (;top < bottom; top++){
            for (int x = 0; x < width; x++){
                if (raster.getSample(x, top, 0) != 0){
                    minRight = x;
                    minBottom = top;
                    break top;
                }
            }
        }

        left:
        for (;left < minRight; left++){
            for (int y = height - 1; y > top; y--){
                if (raster.getSample(left, y, 0) != 0){
                    minBottom = y;
                    break left;
                }
            }
        }

        bottom:
        for (;bottom > minBottom; bottom--){
            for (int x = width - 1; x >= left; x--){
                if (raster.getSample(x, bottom, 0) != 0){
                    minRight = x;
                    break bottom;
                }
            }
        }

        right:
        for (;right > minRight; right--){
            for (int y = bottom; y >= top; y--){
                if (raster.getSample(right, y, 0) != 0){
                    break right;
                }
            }
        }

        return image.getSubimage(left, top, right - left + 1, bottom - top + 1);
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
