package main.commands.dontopendeadinside;

import main.Command;
import main.utility.BotUtils;
import main.utility.SmmryObject;
import main.utility.Visuals;
import main.utility.gist.GistUtils;
import main.utility.gist.gist_json.GistContainer;
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
        SmmryObject smmryObject = BotUtils.gson.fromJson(httpResponse, SmmryObject.class);

        //handle return errors
        if (smmryObject.hasError())
            switch (smmryObject.getSm_api_error()) {
                case "3":
                    BotUtils.sendMessage(event.getChannel(), smmryObject.getSm_api_message().toLowerCase());
                    return;
            }

        //if too big, put into a Gist and link it in the channel instead of sending it directly
        if (Integer.valueOf(smmryObject.getSm_api_character_count()) > 1000) {
            //try to favorably format string. (single line to multi-line) every sentence gets a new line.
            String formattedSummary = smmryObject.getSm_api_content().replaceAll("\\. ", ".\n");

            //NOTE:: DO NOT CHANGE THE FILENAME OR THE JSON BREAKS. SMMRY has a terrible json response that has field names which are dependant on the input :(
            String jsonUrl = GistUtils.makeGistGetUrl("Summary", smmryObject.getSm_api_title(), formattedSummary);
            String json = BotUtils.getStringFromUrl(jsonUrl);
            GistContainer gist = BotUtils.gson.fromJson(json, GistContainer.class);

            EmbedBuilder eb = new EmbedBuilder()
                    .withTitle(smmryObject.getSm_api_title())
                    .withUrl(args.get(0))
                    .withColor(Visuals.getVibrantColor())
                    .withDesc("Since the summary was too long, please refer to this link\n" + gist.getHtml_url() + //hitmlURL is NULL -- FIXED
                            "\nVisit this page if you prefer raw\n" + gist.getFiles().getSummary().getRaw_url());

            BotUtils.sendMessage(event.getChannel(), eb);
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
