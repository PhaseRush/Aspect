package main.utility.wikipedia;

public class WikiContainer {
    private String type;
    private String title;
    private String displaytitle;
    //namespace
    //titles
    private int pageid;
    private WikiImageInfo thumbnail;
    private WikiImageInfo originalimage;
    private String lang;
    //dir
    //revision
    //tid
    private String timestamp; // ISO 8601
    private String description;
    //content_urls
    private WikiApiUrls api_urls;
    private String extract;
    private String extract_html;

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDisplaytitle() {
        return displaytitle;
    }

    public int getPageid() {
        return pageid;
    }

    public WikiImageInfo getThumbnail() {
        return thumbnail;
    }

    public WikiImageInfo getOriginalimage() {
        return originalimage;
    }

    public String getLang() {
        return lang;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public WikiApiUrls getApi_urls() {
        return api_urls;
    }

    public String getExtract() {
        return extract;
    }

    public String getExtract_html() {
        return extract_html;
    }
}
