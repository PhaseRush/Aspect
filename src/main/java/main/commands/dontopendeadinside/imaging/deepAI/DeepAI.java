package main.commands.dontopendeadinside.imaging.deepAI;

import main.Command;
import main.utility.metautil.BotUtils;
import okhttp3.FormBody;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public abstract class DeepAI implements Command {
    protected static String getTargetUrl(MessageReceivedEvent event, List<String> args) {
        String targetUrl;
        try {
            targetUrl = event.getMessage().getAttachments().get(0).getUrl();
        } catch (Exception e) { // if caught then they posted url or tagged someone instead of attaching photo
            if (event.getMessage().getMentions().isEmpty()) {
                targetUrl = args.get(0); // url
            } else {
                targetUrl = event.getMessage().getMentions().get(0).getAvatarURL(); // tagged person's pfp
            }
        }
        return targetUrl;
    }

    protected static String fetchJson(String baseUrl, String targetUrl) {
        FormBody form = new FormBody.Builder()
                .add("image", targetUrl)
                .build();

        return BotUtils.getStringFromUrl(baseUrl, "api-key", BotUtils.DEEP_AI_API_KEY, form);
    }

    public static String fetchWaifu2x(MessageReceivedEvent event, List<String> args) {
        String targetUrl = getTargetUrl(event, args);
        String json = fetchJson("https://api.deepai.org/api/waifu2x", targetUrl);

        return BotUtils.gson.fromJson(json, Waifu2x.Container.class).output_url;
    }

    /**
     * Just guarantee there is at least one image somewhere
     */
    @Override
    public boolean canRun(MessageReceivedEvent event, List<String> args) {
        return !event.getMessage().getAttachments().isEmpty() ||
                BotUtils.numPictures(event.getMessage()) > 0 ||
                !args.isEmpty(); // for manual image linking
    }

    // container object for returned json object
    protected class Container {
    }
}
