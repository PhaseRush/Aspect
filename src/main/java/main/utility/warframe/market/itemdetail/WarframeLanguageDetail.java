package main.utility.warframe.market.itemdetail;

public class WarframeLanguageDetail {
    String codex; //"Used together, these Orokin pistols...."
    String item_name;
    WarframeDetailedDrop[] drop;
    String dexcription;//"<p>The Akbronco Prime lowers  Impact damage to increase  Slash damage, while gaining increased firing rate, critical damage, status chance and magazine size. The Akbronco Prime was added into the game in Update 12.4.</p>"
    String wiki_link;

    public String getCodex() {
        return codex;
    }

    public String getItem_name() {
        return item_name;
    }

    public WarframeDetailedDrop[] getDrop() {
        return drop;
    }

    public String getDexcription() {
        return dexcription;
    }

    public String getWiki_link() {
        return wiki_link;
    }
}
