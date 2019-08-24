import main.utility.metautil.BotUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.*;

class BotUtilsTest {
    private static String DIR_PATH;
    private static Date DATE;

    @BeforeAll
    static void setup() {
        DIR_PATH = System.getProperty("user.dir") + "/";
        DATE = new Date();
    }

    @Test
    void dictionary_Init() {
        assertEquals(BotUtils.dictionarySet.size(), 466544);
    }

    @Test
    void populateInsults_Init() {
        List<String> insults = BotUtils.insults;
        assertEquals("I was pro-life before I met you", insults.get(12));
    }

    @Test
        //wip
    void writeToFIle_Const() {
        String text = "Text";
        String fileName = "BotUtilsTest_writeToFile_Const.txt";
        //BotUtils.writeToFile(DIR_PATH+fileName, text, false);


    }

    @Test
    void getRandomFromListString_Const() {
        List<String> list = Arrays.asList("Hello", "World", "How are you doing?");

        String random = BotUtils.getRandFromCollection(list);
        assertNotNull(random);
        assertTrue(list.contains(random));
    }

    @Test
    void capitalizeFirstLowerRest_Const() {
        String s1 = "test";
        String s2 = "Test";
        String s3 = "TEST";
        String s4 = ";test";

        assertEquals(BotUtils.capitalizeFirstLowerRest(s1), "Test");
        assertEquals(BotUtils.capitalizeFirstLowerRest(s2), "Test");
        assertEquals(BotUtils.capitalizeFirstLowerRest(s3), "Test");
        assertEquals(BotUtils.capitalizeFirstLowerRest(s4), ";test");
    }

    @Test
    void capitalizeFirst_Const() {
        String s1 = "test";
        String s2 = "Test";
        String s3 = "TEST";
        String s4 = ";test";

        assertEquals(BotUtils.capitalizeFirst(s1), "Test");
        assertEquals(BotUtils.capitalizeFirst(s2), "Test");
        assertEquals(BotUtils.capitalizeFirst(s3), "TEST");
        assertEquals(BotUtils.capitalizeFirst(s4), ";test");
    }

    @Test
    void getStringFromUrl_Const() {
        String url = "https://raw.githubusercontent.com/PhaseRush/Aspect/master/LICENSE";
        String text = BotUtils.getStringFromUrl(url);

        assertTrue(text.startsWith("MIT License"));
        assertTrue(text.endsWith("SOFTWARE.\n"));
    }

    //test buildOptions

    @Test
    void getCharFromInt_Const() {
        String[] chars = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

        for (int i = 0; i < 26; i++) {
            assertEquals(BotUtils.ithChar(i), chars[i]);
        }
    }
    //test sortMap

    //test millisToHMS
    //test millistoMS

    //test limitStrLen
    //test stringSimilarity

    @Test
    void stringSimilarity_Const() {
        String s0 = "test";
        String s1 = "test";
        String s2 = "tesq";
        String s3 = "test1";

        assertEquals(BotUtils.stringSimilarity(s0, s1), 0.0);
        assertEquals(BotUtils.stringSimilarity(s1, s0), 0.0);
        assertEquals(BotUtils.stringSimilarity(s1, s2), 1.0);
        assertEquals(BotUtils.stringSimilarity(s1, s3), 1.0);
        assertEquals(BotUtils.stringSimilarity(s3, s2), 2.0);
    }

    @Test
    void SHA256_const() {
        assertEquals(BotUtils.dictionarySet.size(), 466544);

        BotUtils.dictionarySet
                .forEach((s) ->
                        assertEquals(
                                org.apache.commons.codec.digest.DigestUtils.sha256Hex(s),
                                BotUtils.SHA256(s)));
    }

    @Test
    void getTodayYYYYMMDD_const() {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");

        assertEquals(BotUtils.getTodayYYYYMMDD(), format.format(DATE));
    }

    @Test
    void isWindows_Const() {
        assertTrue(BotUtils.isWindows());
    }

    @Test
    void isLinux_Const() {
        assertFalse(BotUtils.isLinux());
    }

    @Test
    void toString_JOKE() {
        assertEquals(new BotUtils().toString(), "Baka don't touch me!");
    }
}
