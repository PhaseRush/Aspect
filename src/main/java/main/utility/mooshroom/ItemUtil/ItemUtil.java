package main.utility.mooshroom.ItemUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.utility.ReadWrite;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    ReadWrite readerWriter = new ReadWrite();

    private final String readPath = "C:\\Users\\Positron\\IdeaProjects\\Kitty_Kat\\txtfiles\\Mooshroom\\rotmgJsonFormatted.txt";
    private final String json = readerWriter.readFromFile(readPath);

    List<RotmgItem> itemList = new ArrayList<>();

    Gson gson = new Gson();

    public ItemUtil() {
        itemList = gson.fromJson(json, new TypeToken<List<RotmgItem>>(){}.getType());
    }

    private RotmgItem getItem(String itemName) {
        RotmgItem item = itemList.get(0);

        for (RotmgItem i :
                itemList) {
            if(i.getId().equals(itemName))
                item = i;
        }
        return item;
    }

    public boolean isUt(String itemName) {
        RotmgItem item = getItem(itemName);
        return item.getUtst() == 1;
    }

    public boolean isSt(String itemName) {
        RotmgItem item = getItem(itemName);
        return item.getUtst() == 2;
    }

    public int getItemTier(String itemName) {
        RotmgItem item = getItem(itemName);
        return item.getTier();
    }
    public int getItemSlotType(String itemName) {
        RotmgItem item = getItem(itemName);
        return item.getSlotType();
    }
    public int getItemXCord(String itemName) {
        RotmgItem item = getItem(itemName);
        return item.getX();
    }
    public int getItemYCord(String itemName) {
        RotmgItem item = getItem(itemName);
        return item.getY();
    }
    public int getItemFameBonus(String itemName) {
        RotmgItem item = getItem(itemName);
        return item.getFameBonus();
    }
    public int getItemFeedPower(String itemName) {
        RotmgItem item = getItem(itemName);
        return item.getFeedPower();
    }
    public boolean getItemSoulbound(String itemName) {
        RotmgItem item = getItem(itemName);
        return item.isSoulbound();
    }
    public int getItemUtSt(String itemName) {
        RotmgItem item = getItem(itemName);
        return item.getUtst();
    }
}
