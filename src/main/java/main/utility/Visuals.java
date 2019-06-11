package main.utility;

import main.Aspect;
import main.utility.metautil.BotUtils;
import main.utility.miscJsonObj.CatMediaContainer;
import org.jetbrains.annotations.Nullable;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import sx.blah.discord.handle.impl.obj.Embed.EmbedField;
import sx.blah.discord.util.EmbedBuilder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Visuals {
    private Random r = new Random();
    private static Random rs = ThreadLocalRandom.current();

    static {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            Font mont = Font.createFont(Font.PLAIN, new File(System.getProperty("user.dir") + "/data/Montserrat.ttf"))
                    .deriveFont(20f);
            ge.registerFont(mont);

            Aspect.LOG.info("Mont size: " + mont.getSize());
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public enum Fonts {
        MONTSERRAT();

        private Font font;


        public Font getFont() {
            return font;
        }

        Fonts() {
            this.font =
                    Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts())
                            .filter(f -> f.getName().toUpperCase().contains(toString()))
                            .findFirst()
                            .get();
        }
    }

    public static Color getRandVibrantColour() {
        // this always generates the same color on startup b/c using thread local random
        return Color.getHSBColor(rs.nextFloat(), .9f, 1.0f);
    }

    /*
    generate random vibrant color with seed
     */
    public static Color getRandVibrantColour(String seed) {
        return Color.getHSBColor(new Random(Long.valueOf(seed)).nextFloat(), .9f, 1.0f);
    }

    public static BufferedImage urlToBufferedImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }

    public static byte[] urlToImageByteArray(String url) {
        try (InputStream in = new BufferedInputStream(new URL(url).openStream());
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf)))
                out.write(buf, 0, n);
            return out.toByteArray();
        } catch (MalformedURLException e) {
            Aspect.LOG.info("bad url - visuals.urlToByteArray");
            e.printStackTrace();
        } catch (IOException e) {
            Aspect.LOG.info("io exception - visuals.urlToByteArray");
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
            Aspect.LOG.info("url to buff img w/agent header - visuals");
            e.printStackTrace();
        }

        return null;
    }

    public static Color analyzeImageColor(BufferedImage bi) {
        long sumR = 0, sumG = 0, sumB = 0;

        for (int i = 0; i < bi.getWidth(); i++) { // gets fucked at some urls
            for (int j = 0; j < bi.getHeight(); j++) {
                Color pixelColor = new Color(bi.getRGB(i, j));
                sumR += pixelColor.getRed();
                sumG += pixelColor.getGreen();
                sumB += pixelColor.getBlue();
            }
        }
        long avgR = sumR / 255;
        long avgG = sumG / 255;
        long avgB = sumB / 255;

        float[] hsbArray = Color.RGBtoHSB((int) avgR, (int) avgG, (int) avgB, null);
        return Color.getHSBColor(hsbArray[0], .9f, 1.0f);
    }

    public static Color analyzeImageColor(Image img) {
        return analyzeImageColor(convertImageToBufferedImage(img));
    }

    public static Color analyzeWeightedImageColor(Image i, int ratio) {
        BufferedImage bi = convertImageToBufferedImage(i);

        BufferedImage img = bi.getSubimage(bi.getWidth() / ratio, bi.getHeight() / ratio, bi.getWidth() * (ratio - 1) / ratio, bi.getHeight() * (ratio - 1) / ratio); // fill in the corners of the desired crop location here
        BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(img, 0, 0, null);

        return analyzeImageColor(copyOfImage);
    }

    // https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
    private static BufferedImage convertImageToBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
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
        for (; top < bottom; top++) {
            for (int x = 0; x < width; x++) {
                if (raster.getSample(x, top, 0) != 0) {
                    minRight = x;
                    minBottom = top;
                    break top;
                }
            }
        }

        left:
        for (; left < minRight; left++) {
            for (int y = height - 1; y > top; y--) {
                if (raster.getSample(left, y, 0) != 0) {
                    minBottom = y;
                    break left;
                }
            }
        }

        bottom:
        for (; bottom > minBottom; bottom--) {
            for (int x = width - 1; x >= left; x--) {
                if (raster.getSample(x, bottom, 0) != 0) {
                    minRight = x;
                    break bottom;
                }
            }
        }

        right:
        for (; right > minRight; right--) {
            for (int y = bottom; y >= top; y--) {
                if (raster.getSample(right, y, 0) != 0) {
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
     *
     * @param title
     * @param value
     * @param inLine
     * @return
     */
    public EmbedField getEmbedField(String title, String value, boolean inLine) {
        return new EmbedField(title, value, inLine);
    }


    // some real fancy shit gonna go down

    //https://stackoverflow.com/questions/18800717/convert-text-content-to-image
    public static ByteArrayOutputStream genTextImage(String text) {
        /*
           Because font metrics is based on a graphics context, we need to create
           a small, temporary image so we can ascertain the width and height
           of the final image
         */
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = Fonts.MONTSERRAT.getFont().deriveFont(48f);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        optimizeGraphics2dForText(g2d);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();

        return buffImgToOutputStream(img, "png");
    }

    public static void optimizeGraphics2dForText(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    @Nullable
    public static ByteArrayOutputStream buffImgToOutputStream(BufferedImage img, String format) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, format, baos);
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static BufferedImage genTextImage(String text, int width, int height) {
//        BufferedImage before = genTextImage(text);
//        double scaleX = ((double)width)/before.getWidth();
//        double scaleY = ((double)height)/before.getHeight();
//
//
//        int w = before.getWidth();
//        int h = before.getHeight();
//        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//        AffineTransform at = new AffineTransform();
//        at.scale(scaleX, scaleY);
//        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
//        after = scaleOp.filter(before, after);
//
//        return after;
//    }

    public static void saveImg(BufferedImage img, String path) {
        try {
            ImageIO.write(img, "png", new File(path + ".png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getCatMedia() {
        String json = BotUtils.getStringFromUrl("https://api.thecatapi.com/v1/images/search?size=full");
        json = json.substring(1, json.length() - 1);
        CatMediaContainer cat = BotUtils.gson.fromJson(json, CatMediaContainer.class);
        return cat.getUrl();
    }


    public static ByteArrayOutputStream renderTex(String texInput) {
        TeXFormula formula = new TeXFormula(texInput);

        TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);

        icon.setInsets(new Insets(5, 5, 5, 5));

        BufferedImage texBuffImg =
                new BufferedImage(
                        icon.getIconWidth(),
                        icon.getIconHeight(),
                        BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = texBuffImg.createGraphics();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));
        icon.paintIcon(jl, g2d, 0, 0);

        return buffImgToOutputStream(texBuffImg, "png");
    }

}
