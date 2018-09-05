package main.utility.fortnite.shop;

public class FortniteShopJson {
    int refreshIntervalHrs;
    int dailyPurchaseHrs;

    String expiration;
    Storefront[] storefronts;

    BR br;

    String lastUpdate;

    public int getRefreshIntervalHrs() {
        return refreshIntervalHrs;
    }

    public int getDailyPurchaseHrs() {
        return dailyPurchaseHrs;
    }

    public String getExpiration() {
        return expiration;
    }

    public Storefront[] getStorefronts() {
        return storefronts;
    }

    public BR getBr() {
        return br;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

}
