package main.utility.humorUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CuteUtil {
    public static Map<String, String> cuteUrls = new HashMap<>();

    static {
        cuteUrls.put("baka", "http://pm1.narvii.com/6511/5f5c48c50768d5ac75a9e7ff6239e41fbcabcff2_00.jpg");
        cuteUrls.put("headpat", "https://cdnb.artstation.com/p/assets/images/images/010/608/879/large/aoi-ogata-pat-me2.jpg?1525303993");
        cuteUrls.put("butt", "no");
    }

    public static Set<String> getCuties() {
        return cuteUrls.keySet();
    }
}
