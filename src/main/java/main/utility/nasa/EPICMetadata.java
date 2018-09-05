package main.utility.nasa;

public class EPICMetadata { //todo, can make more robust with more nested classes
    private String identifier;
    private String caption;
    private String image;
    private String version;

    private String date;

    public String getIdentifier() {
        return identifier;
    }

    public String getCaption() {
        return caption;
    }

    public String getImage() {
        return image;
    }

    public String getVersion() {
        return version;
    }

    public String getDate() {
        return date;
    }
}
