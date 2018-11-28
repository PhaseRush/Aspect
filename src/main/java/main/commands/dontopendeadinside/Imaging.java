package main.commands.dontopendeadinside;

import com.google.gson.Gson;
import main.Command;
import main.utility.BotUtils;
import main.utility.Visuals;
import main.utility.imaging.ImagingGetBody;
import main.utility.imaging.ImagingPostBody;
import okhttp3.*;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

public class Imaging implements Command {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> returnHandle = null;

    @Override
    public void runCommand(MessageReceivedEvent event, List<String> args) {
        if(args.size() !=1) {
            BotUtils.send(event.getChannel(), "Please provide exactly 1 url. Use \"" + BotUtils.DEFAULT_BOT_PREFIX + "help img\" for more info");
        } else {
            IMessage loadingMsg = event.getChannel().sendMessage("```\nProcessing Image```");
            getCloudSightJson(args.get(0), event, loadingMsg);
        }
    }


    private void getCloudSightJson(String imageURL, MessageReceivedEvent event, IMessage loadingMsg) {
        long startTime = System.currentTimeMillis();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        String jsonStr = "{\"remote_image_url\":\""+ imageURL + "\",\"locale\": \"en_US\"}";
        String imageTokenJson = "";
        try {
            //returns the response body
            imageTokenJson = postWithJson("https://api.cloudsight.ai/v1/images", jsonStr);
        } catch (IOException e) {
            BotUtils.send(event.getChannel(), e.toString());
        }

        ImagingPostBody postBody = new Gson().fromJson(imageTokenJson, ImagingPostBody.class);

        //injected in to here
        handleGetRequestWithLoop(postBody, event, startTime);

        //delete loading message
        loadingMsg.delete();
    }

    //if this works decc the hecc will achieve god status
    private void handleGetRequestWithLoop(ImagingPostBody postBody, MessageReceivedEvent event, long startTime) {

        // handle handleGetRequestWithLoop things
        // send request to the api

        try {
            String intermediateGetBody = getRequest("https://api.cloudsight.ai/v1/images/" + postBody.getToken());
            //do initial check before delegating to scheduler
            ImagingGetBody initialGet = new Gson().fromJson(intermediateGetBody, ImagingGetBody.class);
            if (initialGet.getStatus().equals("completed")) {
                handleCompletedStatus(event, initialGet, startTime, 0);
            } else if (initialGet.getStatus().equals("skipped")) {
                handleSkippedStatus(event, initialGet);
            }
        } catch (IOException e) {
            //kms
        }

        // start timer
        returnHandle = scheduler.scheduleAtFixedRate(new Runnable() {
            int numFailedGets = 0;
            public void run() {
                // make a request to the api again

                String repeatedGetBody = "";
                try {
                    repeatedGetBody = getRequest("https://api.cloudsight.ai/v1/images/" + postBody.getToken());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ImagingGetBody intermediateGetBody = new Gson().fromJson(repeatedGetBody, ImagingGetBody.class);
                // if the response is complete then
                if (intermediateGetBody.getStatus().equals("completed")) {
                    handleCompletedStatus(event, intermediateGetBody, startTime, numFailedGets);

                    // and kill this
                    returnHandle.cancel(true);
                } else if (intermediateGetBody.getStatus().equals("skipped")) {
                    handleSkippedStatus(event, intermediateGetBody);

                    // and kill this
                    returnHandle.cancel(true);
                }
                //reach this point means status was not an endcase status (not one of "completed" or "skipped")
                numFailedGets++;
                if (numFailedGets % 5 == 0) {
                    System.out.println("Img looped: " + numFailedGets);
                }
                returnHandle.cancel(true);

                // if not then just let it keep running
            }
        }, 1, 1, SECONDS);
    }

    private static void handleSkippedStatus(MessageReceivedEvent event, ImagingGetBody getBody) {
        String response;

        switch (getBody.getReason()) {
            case "offensive":
                response = "No dick pics";
                break;
            case "blurry":
                response = "Your potato camera needs a better lens.";
                break;
            case "dark":
                response = "Why use a phone camera in a darkroom?";
                break;
            case "bright":
                response = "Stop taking pictures of the sun. Leave that to NASA.";
                break;
            case "unsure":
                response = "Ok this image matches nothing that the human race knows of";
                break;
            case "close":
                response = "Call it a wrap the api fucked up";
                break;
            default:
                response = "This really shouldn't have occurred. This spaghetti code needs more salt";
        }
        BotUtils.send(event.getChannel(), response);
    }

    private static void handleCompletedStatus(MessageReceivedEvent event, ImagingGetBody getBody, long startTIme, int numFailedGets) {
        List<ReactionEmoji> emojis = BotUtils.initializeNumberEmojis();


        EmbedBuilder eb = new EmbedBuilder()
                .withTitle("Aspect Image Recognition")
                .withDesc("```" + getBody.getName() + "```")
                .withColor(Visuals.getVibrantColor())
                .withFooterText("This operation took me " + (System.currentTimeMillis() - startTIme + "ms after " + (numFailedGets == 0 ? "no failed attempts" : numFailedGets + " failed attempts")));

        IMessage embedMessage = event.getChannel().sendMessage(eb.build());
        //BotUtils.reactAllEmojis(embedMessage, emojis); //makes stuff freak out
        System.out.println("img analysis failed : " + numFailedGets);
    }


    public static String postWithJson(String url, String json) throws IOException { //could also throw NullPE
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("authorization", "CloudSight " + BotUtils.CLOUDSIGHT_API_KEY)
                .header("cache-control", "no-cache")
                .header("content-type", "application/json")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }


    public static String getRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("authorization", "CloudSight " + BotUtils.CLOUDSIGHT_API_KEY)
                .header("cache-control", "no-cache")
                .header("content-type", "application/json")
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
        return "Cloudsight Image Recognition";
    }
}