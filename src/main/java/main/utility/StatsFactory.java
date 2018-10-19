package main.utility;

public class StatsFactory {


    public static StringBuilder generateRow(String category, Object compVal, Object quickVal, int width) {
        int totalWidth = width;
        int catLen = category.length();
        int firstE = 25;
        int secondE = 39;

        StringBuilder sb = new StringBuilder(category);

        //fill with blanks
        for (int i = catLen; i < totalWidth; i++) sb.append(" ");

        String compString = String.valueOf(compVal);
        String quickString = String.valueOf(quickVal);
        //replace each
        sb.replace(firstE-compString.length(),firstE, compString);
        sb.replace(secondE-quickString.length(),secondE, quickString);

        return sb;
    }

    public static StringBuilder generateRow(String category, String soloVal, String duoVal, String squadVal, int width) {
        int totalWidth = width;
        int catLen = category.length();
        int firstE = 19;
        int secondE = 31;
        int thirdE = 45;

        StringBuilder sb = new StringBuilder(category);

        //fill with blanks
        for (int i = catLen; i < totalWidth; i++) sb.append(" ");

        //replace each
        sb.replace(firstE-soloVal.length(),firstE, soloVal);
        sb.replace(secondE-duoVal.length(),secondE,duoVal);
        sb.replace(thirdE-squadVal.length(),thirdE, squadVal);

        return sb;
    }

}
