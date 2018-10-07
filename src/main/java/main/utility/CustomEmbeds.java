package main.utility;

import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomEmbeds {

    public static Map<String, EmbedBuilder> personalEmbedMap = new LinkedHashMap<>();
    public static EmbedBuilder dev;
    public static EmbedBuilder celery;


    static {
        dev = new EmbedBuilder()
                .withAuthorName("KVTVRINV")
                .withAuthorIcon("https://i.imgur.com/echE5EF.jpg")
                .withAuthorUrl("https://google.com")

                .withTitle("Hi, I'm Kat!")
                .withUrl("https://discordapp.com")
                .withDesc("18 earth year old ambitious lump of organic matter that goes around\n trying to not be useless :)")

                .withColor(new Color(13458387))
                .withTimestamp(System.currentTimeMillis())
                .withFooterText("Look Onwards")
                .withFooterIcon("https://pbs.twimg.com/profile_images/664158849536921600/KZAnmaIr_400x400.jpg")

                .withThumbnail("https://vignette.wikia.nocookie.net/leagueoflegends/images/1/12/PROJECT_Katarina_profileicon.png/revision/latest?cb=20170505005706")
                .withImage("https://i.imgur.com/8kGgNGX.jpg")

                .appendField(":cat:", "placeholder 1", false)
                .appendField("comments, suggestions, critique", "contact@email.com", false)
                .appendField("ph", "ph", false)
                .appendField("Schrodinger's", "dead", true)
                .appendField("Cat", "alive", true);

        celery = new EmbedBuilder()
                .withAuthorIcon("https://goo.gl/images/dzYYLK")
                .withAuthorName("soup")
                .withAuthorName("bing.com")

                .withDesc("no alcohol content. not edible...")

                .withColor(new Color(0x4277f4))

                .withThumbnail("https://goo.gl/images/7w3sLn")
                .withImage("https://i.imgur.com/52d2Ejm.jpg")

                .appendField("for official business, contant", "quitedrunk#8151", false)
                .appendField("consult fee", "soul", false);


        personalEmbedMap.put("264213620026638336", dev);
        personalEmbedMap.put("197061517055623168", celery);
    }

    public static Map<String, EmbedBuilder> getPersonalEmbedMap() {
        return personalEmbedMap;
    }
}
