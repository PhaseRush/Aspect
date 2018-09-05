package main.utility.fortnite.shop;

public class CatalogEntry {
    String offerID;
    String devName;
    String offerType;
    Prices[] prices;

    String[] categories;

    int dailyLimit;
    int weeklyLimit;
    int monthlyLimit;
    String[] appStoreId;

    Requirements[] requirements;

    MetaInfo[] metaInfo;

    String catalogGroup;
    int catalogGroupPriority;
    int sortPriority;
    String title;
    String shortDescription;
    String description;
    String displayAssetPath;

    ItemGrants[] itemGrants;

    public String getOfferID() {
        return offerID;
    }

    public String getDevName() {
        return devName;
    }

    public String getOfferType() {
        return offerType;
    }

    public Prices[] getPrices() {
        return prices;
    }

    public String[] getCategories() {
        return categories;
    }

    public int getDailyLimit() {
        return dailyLimit;
    }

    public int getWeeklyLimit() {
        return weeklyLimit;
    }

    public int getMonthlyLimit() {
        return monthlyLimit;
    }

    public String[] getAppStoreId() {
        return appStoreId;
    }

    public Requirements[] getRequirements() {
        return requirements;
    }

    public MetaInfo[] getMetaInfo() {
        return metaInfo;
    }

    public String getCatalogGroup() {
        return catalogGroup;
    }

    public int getCatalogGroupPriority() {
        return catalogGroupPriority;
    }

    public int getSortPriority() {
        return sortPriority;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayAssetPath() {
        return displayAssetPath;
    }

    public ItemGrants[] getItemGrants() {
        return itemGrants;
    }
}
