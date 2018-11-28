package main.utility;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

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
            System.out.println("GoogleUtil. Static initializer. GeneralSecurityException thrown");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("GoogleUtil. Static initializer. IOException thrown");
            e.printStackTrace();
        }

        youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, request -> {
        }).build();
    }

    public static YouTube getYoutube() {
        return youtube;
    }

    public static void prettyPrintYoutube(Collection<SearchResult> searchResults, String query) {

        System.out.println("\n=============================================================");
        System.out.println("   First " + "x number of" + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (searchResults.size() == 0) {
            System.out.println(" There aren't any results for your query.");
        }

        //while (iteratorSearchResults.hasNext()) {
        for (SearchResult singleVideo : searchResults) {
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) { //probably don't need if set type to video
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                System.out.println(" Video Id" + rId.getVideoId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" Thumbnail: " + thumbnail.getUrl());
                System.out.println("\n-------------------------------------------------------------\n");
            }
        }
    }
}
