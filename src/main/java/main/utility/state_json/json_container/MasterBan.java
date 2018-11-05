package main.utility.state_json.json_container;

import java.util.List;

public class MasterBan {
    List<String> masterBanList;
    List<String> cuteBanList;
    List<String> specificBanList;
    String note;

    public List<String> getMasterBanList() {
        return masterBanList;
    }

    public List<String> getCuteBanList() {
        return cuteBanList;
    }

    public List<String> getSpecificBanList() {
        return specificBanList;
    }

    public String getNote() {
        return note;
    }
}
