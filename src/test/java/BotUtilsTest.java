import main.utility.BotUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

class BotUtilsTest {

    @Test
    void dictionary_Init() {
        assertEquals(BotUtils.dictionary.size(), 466544);
    }

    @Test
    void populateInsults_Init() {
        List<String> insults = BotUtils.insults;
        assertEquals(insults.get(12), "I was pro-life before I met you");
    }


}
