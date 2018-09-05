package main.utility.warframe.market.marketlistings;

public class WarframeTradeListing {
    private String last_update;
    private WarframeListingUser user;
    private int quatity;
    private boolean visible;
    private int platinum;
    private String order_type; //"sell"
    private String creation_date;
    private String id;
    private String platform; //"pc"
    private String region;// "en"

    public String getLast_update() {
        return last_update;
    }

    public WarframeListingUser getUser() {
        return user;
    }

    public int getQuatity() {
        return quatity;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getPlatinum() {
        return platinum;
    }

    public String getOrder_type() {
        return order_type;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public String getId() {
        return id;
    }

    public String getPlatform() {
        return platform;
    }

    public String getRegion() {
        return region;
    }
}
