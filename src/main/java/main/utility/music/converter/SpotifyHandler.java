package main.utility.music.converter;

import com.google.api.services.youtube.YouTube;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import main.passive.ScheduledActions;
import main.utility.GoogleUtil;
import main.utility.metautil.BotUtils;
import main.utility.structures.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SpotifyHandler {
    private static SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(BotUtils.SPOTIFY_CLIENT_ID)
            .setClientSecret(BotUtils.SPOTIFY_CLIENT_SECRET)
            .build();

    private static ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();

    static { // run token refresh immediately
        refreshSpotifyToken();
    }

    private static void refreshSpotifyToken() {
        ClientCredentials creds;
        try {
            creds = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(creds.getAccessToken());

            // self refresh
            ScheduledActions.scheduler.schedule(SpotifyHandler::refreshSpotifyToken, creds.getExpiresIn(), TimeUnit.SECONDS);
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }
    }

    private static YouTube.Search ytSearch = GoogleUtil.getYoutube().search();


    /**
     * @param spotifyPlaylistUrl
     * @return pair<youtube ids, failed video titles>
     * @throws IOException
     * @throws SpotifyWebApiException
     */
    public static Pair<List<String>, List<String>> spotifyToYT(String spotifyPlaylistUrl) throws IOException, SpotifyWebApiException {
        Playlist playlist = spotifyApi.getPlaylist(parseUrl(spotifyPlaylistUrl)).build().execute();
        PlaylistTrack[] tracks = playlist.getTracks().getItems();

        List<String> youtubeIDs = new ArrayList<>();
        List<String> ommited = new ArrayList<>();

        Arrays.stream(tracks).map(PlaylistTrack::getTrack).forEach(t -> {
                    String query = "song " + t.getName() + " by " +
                            Arrays.stream(t.getArtists()).map(ArtistSimplified::getName).collect(Collectors.joining(" "));

                    System.out.println(query); // delete this later
                    try {
                        youtubeIDs.add(ytSearch
                                .list("id,snippet")
                                .setKey(BotUtils.YOUTUBE_API_KEY)
                                .setQ(query)
                                .setType("video")
                                .setMaxResults(1L)
                                .execute()
                                .getItems()
                                .get(0)
                                .getId()
                                .getVideoId());
                    } catch (IOException | IndexOutOfBoundsException e) { // some youtube error, just add to ommited
                        ommited.add(query);
                    }
                }
        );

        return new Pair<>(youtubeIDs, ommited);
    }

    private static String parseUrl(String url) {
        if (url.contains("?si=")) {
            url = url.substring(0, url.indexOf("?si="));
        }
        return url.substring(url.lastIndexOf('/') + 1);
    }
}
