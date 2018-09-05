package main.utility.warframe.market.itemdetail;

public class WarframeDetailedItem {
    WarframeLanguageDetail en;
    String sub_icon;
    int trading_tax;
    String icon_format;
    int ducats;
    String id;
    boolean set_root;
    String thumb;
    int mastery_level;
    String[] tags; //aka keywords
    String icon;
    String url_name;

    public WarframeLanguageDetail getEn() {
        return en;
    }

    public String getSub_icon() {
        return sub_icon;
    }

    public int getTrading_tax() {
        return trading_tax;
    }

    public String getIcon_format() {
        return icon_format;
    }

    public int getDucats() {
        return ducats;
    }

    public String getId() {
        return id;
    }

    public boolean isSet_root() {
        return set_root;
    }

    public String getThumb() {
        return thumb;
    }

    public int getMastery_level() {
        return mastery_level;
    }

    public String[] getTags() {
        return tags;
    }

    public String getIcon() {
        return icon;
    }

    public String getUrl_name() {
        return url_name;
    }
}
