package main.utility;

import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.language.CanadianEnglish;

import java.util.Arrays;
import java.util.List;

public class LangUtil {

    public static JLanguageTool langToolAmerican = new JLanguageTool(new AmericanEnglish());
    public static JLanguageTool langToolEnglish = new JLanguageTool(new CanadianEnglish());


    static {
        removeLangRules();
    }

    // dumped rules here: https://pastebin.com/hZgERU1Z
    private static void removeLangRules() {
        List<String> uselessRules = Arrays.asList(
                "COMMA_PARENTHESIS_WHITESPACE",
                "DOUBLE_PUNCTUATION",
                "UPPERCASE_SENTENCE_START",
                "WHITESPACE_RULE",
                "SENTENCE_WHITESPACE",
                "PUNCTUATION_PARAGRAPH_END",
                "EN_UNPAIRED_BRACKETS",
                "ENGLISH_WORD_REPEAT_RULE",
                "EN_A_VS_AN",
                "ENGLISH_WORD_REPEAT_BEGINNING_RULE",
                "EN_COMPOUNDS",
                "EN_CONTRACTION_SPELLING", // !important
                "ENGLISH_WRONG_WORD_IN_CONTEXT",
                "EN_DASH_RULE",
                "METRIC_UNITS_EN_US",
                "DEGREE_CHANGE",
                "HALO_HALLO",
                "APOS_RE", //we'Re'
                "PERS_PRON_CONTRACTION",
                "WAN_T",
                //... many rules
                "WIFI",
                "I_A",
                "IT_IS",
                "WRONG_GENITIVE_APOSTROPHE",
                "MISSING_GENITIVE",
                "POSSESSIVE_APOSTROPHE",
                "APOSTROPHE_PLURAL",
                "HASNT_IRREGULAR_VERB",
                "COULDVE_IRREGULAR_VERB"
                // add more later
        );
        langToolAmerican.disableRules(uselessRules);
        langToolEnglish.disableRules(uselessRules);
    }

}
