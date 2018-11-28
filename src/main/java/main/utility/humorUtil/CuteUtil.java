package main.utility.humorUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CuteUtil {
    public static Map<String, String> cuteUrls = new LinkedHashMap<>();
    public static List<String> banned = new ArrayList<>(); // load from fat json

    static {
        cuteUrls.put("baka", "~http://pm1.narvii.com/6511/5f5c48c50768d5ac75a9e7ff6239e41fbcabcff2_00.jpg");
        cuteUrls.put("headpat", "~https://cdnb.artstation.com/p/assets/images/images/010/608/879/large/aoi-ogata-pat-me2.jpg?1525303993");
        cuteUrls.put("butt", "no");
        cuteUrls.put("chinrest", "~https://pbs.twimg.com/media/Do5KSXmUwAEymPV.jpg:large");
        cuteUrls.put("eevee", "https://video.twimg.com/tweet_video/DoJtPVuU8AEuABj.mp4"); //@todo updated url (still doesnt work)
    }
}