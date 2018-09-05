package main.utility.imaging;

public class ImagingGetBody {
    private String token;
    private String url;
    private int ttl;

    private String status;
    private String reason;

    private String name;
    private String[] flags;


    public String getToken() {
        return token;
    }

    public String getUrl() {
        return url;
    }

    public int getTtl() {
        return ttl;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getName() {
        return name;
    }

    public String[] getFlags() {
        return flags;
    }
}
