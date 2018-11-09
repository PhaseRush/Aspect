package main.commands.dontopendeadinside;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.SmmryObject;
import main.utility.Visuals;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.util.List;

public class Summarize implements Command {
    private OkHttpClient client = new OkHttpClient();
    private String req = "http://api.smmry.com/&SM_API_KEY=" + BotUtils.SMMRY_API_KEY;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        long startTime = System.currentTimeMillis();
        String httpResponse;
        try {
            httpResponse = runHttpGet(req + "&SM_URL=" + args.get(0));
        } catch (IOException e) {
            BotUtils.sendMessage(event.getChannel(), "Malformed URL");
            return; //Don't proceed
        }
        SmmryObject smmryObject = new Gson().fromJson(httpResponse, SmmryObject.class);

        //handle return errors
        if (smmryObject.hasError())
            switch (smmryObject.getSm_api_error()) {
                case "3":
                    BotUtils.sendMessage(event.getChannel(), smmryObject.getSm_api_message().toLowerCase());
                    return;
            }


        if (Integer.valueOf(smmryObject.getSm_api_character_count()) > 2048) {
            BotUtils.sendMessage(event.getChannel(), "Text body too long; cannot shorten to 2048 characters");
            return;
        }

        EmbedBuilder eb = new EmbedBuilder()
                .withDesc(smmryObject.getSm_api_content() + "\n\n[Original](" + args.get(0) + ")")
                .withColor(Visuals.getVibrantColor())
                .withFooterText("Reduced content by " + smmryObject.getSm_api_content_reduced() + " to " + smmryObject.getSm_api_character_count() + " characters in " + (System.currentTimeMillis() - startTime) + " ms");

        BotUtils.sendMessage(event.getChannel(), eb);
    }

    private String runHttpGet(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @Override
    public boolean canRun(MessageReceivedEvent event) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Uses SMMRY to summarize any text article.";
    }
}
