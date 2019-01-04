package main.utility;

import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.language.CanadianEnglish;

public class LangUtil {

    public static JLanguageTool langToolAmerican = new JLanguageTool(new AmericanEnglish());
    public static JLanguageTool langToolEnglish = new JLanguageTool(new CanadianEnglish());

}
