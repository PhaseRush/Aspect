import main.utility.BotUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static junit.framework.Assert.*;

class BotUtilsTest {
    private static String DIR_PATH;
    private static Calendar CALENDAR;

    @BeforeAll
    static void setup() {
        DIR_PATH = System.getProperty("user.dir") + "/";
        CALENDAR = Calendar.getInstance();
    }

    @Test
    void dictionary_Init() {
        assertEquals(BotUtils.dictionary.size(), 466544);
    }

    @Test
    void populateInsults_Init() {
        List<String> insults = BotUtils.insults;
        assertEquals(insults.get(12), "I was pro-life before I met you");
    }

    @Test //wip
    void writeToFIle_Const() {
        String text = "Text";
        String fileName = "BotUtilsTest_writeToFile_Const.txt";
        //BotUtils.writeToFile(DIR_PATH+fileName, text, false);


    }

    @Test
    void getRandomFromListString_Const() {
        List<String> list = Arrays.asList("Hello", "World", "How are you doing?");

        String random = BotUtils.getRandomFromListString(list);
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


    // TODO: 2019-01-13
    //test getStringFromUrl
    //test buildOptions
    //test getCharFromInt

    //test sortMap

    //test millisToHMS
    //test millistoMS
    //test limitStrLen
    //test stringSimilarity
    //test generateWeirdFlex

    //test SHA256

    //test getTodayYYYYMMDD

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
