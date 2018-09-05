package main.utility.warframe.dynamic;

public class DailyDeal {
    String StoreItem;
    EventDate Activation;
    EventDate Expiry;
    int Discount;
    int OriginalPrice;
    int SalePrice;
    int AmountTotal;
    int AmountSold;

    public String getStoreItem() {
        return StoreItem;
    }

    public EventDate getActivation() {
        return Activation;
    }

    public EventDate getExpiry() {
        return Expiry;
    }

    public int getDiscount() {
        return Discount;
    }

    public int getOriginalPrice() {
        return OriginalPrice;
    }

    public int getSalePrice() {
        return SalePrice;
    }

    public int getAmountTotal() {
        return AmountTotal;
    }

    public int getAmountSold() {
        return AmountSold;
    }
}
