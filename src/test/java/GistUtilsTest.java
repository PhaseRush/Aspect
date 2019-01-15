import main.utility.BotUtils;
import main.utility.gist.GistUtils;
import main.utility.gist.gist_json.GistContainer;
import org.junit.jupiter.api.Test;

public class GistUtilsTest {
    private static final String FILENAME = "FILENAME_TEST";
    private static final String FILEDESC = "FILEDESC_TEST";
    private static final String FILECONTENT = "FILECONTENT_TEST";


    @Test
    void makeGistGetUrl_create() {
        String gistUrl = GistUtils.makeGistGetUrl(FILENAME, FILEDESC, FILECONTENT);
        String json = BotUtils.getStringFromUrl(gistUrl);

        GistContainer container = BotUtils.gson.fromJson(json, GistContainer.class);

        String htmlUrl = container.getHtml_url();

        // TODO: 2019-01-15 a
    }

    @Test
    void makeGistGetObj_create() {
        GistContainer container = GistUtils.makeGistGetObj(FILENAME, FILEDESC, FILECONTENT);

        // TODO: 2019-01-15 a
    }

    @Test
    void makeGistGetHtmlUrl_create() {
        String htmlUrl = GistUtils.makeGistGetHtmlUrl(FILENAME, FILEDESC, FILECONTENT);

        // TODO: 2019-01-15 a
    }
}