package main.utility.gist;


import main.utility.BotUtils;
import main.utility.gist.gist_json.GistContainer;
import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;

import java.io.IOException;
import java.util.Collections;

/**
 * uses a library from 2011... but it works :)
 */
public class GistUtils {
    public static GitHubClient client = new GitHubClient().setCredentials(BotUtils.DEV_GITHUB_NAME, BotUtils.DEV_GITHUB_PASSWORD);


    public static String makeGistGetUrl(String fileName, String fileDescription, String fileContent) {
        Gist gist = new Gist().setDescription(fileDescription);
        GistFile file = new GistFile().setContent(fileContent);
        gist.setFiles(Collections.singletonMap(fileName, file)).setPublic(true);

        try {
            gist = new GistService(client).createGist(gist);
            System.out.println("Gist url : " + gist.getUrl());
            return gist.getUrl();
        } catch (IOException e) {
            System.out.println("make gist error");
            e.printStackTrace();
        }
        return null;
    }

    public static GistContainer makeGistGetObj(String fileName, String fileDesc, String fileContent) {
        String url = makeGistGetUrl(fileName, fileDesc, fileContent);
        String json = BotUtils.getStringFromUrl(url);
        return BotUtils.gson.fromJson(json, GistContainer.class);
    }

    public static String makeGistGetHtmlUrl(String fileName, String fileDesc, String fileContent) {
        return makeGistGetObj(fileName, fileDesc, fileContent).getHtml_url();
    }

}
