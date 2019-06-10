package main.utility;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.VideoStatistics;
import main.Aspect;
import main.utility.metautil.BotUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;

/**
 * boilerplate code for google stuffs
 * A hard fucking ree. took a good few hours to refactor code that didnt work (thanks google)
 * <p>
 * basically copy pasta from: https://developers.google.com/youtube/v3/docs/search/list
 * adjusted for clarity
 */
public class GoogleUtil {

    private static HttpTransport HTTP_TRANSPORT = null;
    private static JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static YouTube youtube;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            Aspect.LOG.info("GoogleUtil. Static initializer. GeneralSecurityException thrown");
            e.printStackTrace();
        } catch (IOException e) {
            Aspect.LOG.info("GoogleUtil. Static initializer. IOException thrown");
            e.printStackTrace();
        }

        youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, request -> {
        })
                .setApplicationName("AspectV2")
                .build();
    }


//    /**
//     * Authorizes the installed application to access user's protected data.
//     */
//    private static Credential authorize() throws Exception {
//        // load client secrets
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
//                new InputStreamReader(GoogleUtil.class.getResourceAsStream("/client_secrets.json"))); //file to input stream
//        // set up authorization code flow
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                httpTransport, JSON_FACTORY, clientSecrets,
//                Collections.singleton(CalendarScopes.CALENDAR)).setDataStoreFactory(dataStoreFactory)
//                .build();
//        // authorize
//        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
//    }

    public static YouTube getYoutube() {
        return youtube;
    }

    public static VideoStatistics getYTRatings(String videoId) {
        System.out.println("trying with id: " + videoId);
        try {
            return youtube.videos()
                    .list("statistics")
                    .setKey(BotUtils.YOUTUBE_API_KEY)
                    .setId(videoId)
                    .execute()
                    .getItems()
                    .get(0)
                    .getStatistics();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void prettyPrintYoutube(Collection<SearchResult> searchResults, String query) {

        Aspect.LOG.info("\n=============================================================");
        Aspect.LOG.info("   First " + "x number of" + " videos for search on \"" + query + "\".");
        Aspect.LOG.info("=============================================================\n");

        if (searchResults.size() == 0) {
            Aspect.LOG.info(" There aren't any results for your query.");
        }

        //while (iteratorSearchResults.hasNext()) {
        for (SearchResult singleVideo : searchResults) {
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) { //probably don't need if set type to video
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                Aspect.LOG.info(" Video Id" + rId.getVideoId());
                Aspect.LOG.info(" Title: " + singleVideo.getSnippet().getTitle());
                Aspect.LOG.info(" Thumbnail: " + thumbnail.getUrl());
                Aspect.LOG.info("\n-------------------------------------------------------------\n");
            }
        }
    }
}
