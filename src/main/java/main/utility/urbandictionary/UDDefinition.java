package main.utility.urbandictionary;

import java.util.List;

public class UDDefinition {
    private String definition;
    private String permalink;
    private int thumbs_up;
    private List<String> sound_urls;
    private String author;
    private String word;
    private int defid;
    private String current_vote;
    private String written_on; //ISO 8601 "2007-02-22T00:00:00.000Z"
    private String example;
    private int thumbs_down;

    public String getDefinition() {
        return definition;
    }

    public String getPermalink() {
        return permalink;
    }

    public int getThumbs_up() {
        return thumbs_up;
    }

    public List<String> getSound_urls() {
        return sound_urls;
    }

    public String getAuthor() {
        return author;
    }

    public String getWord() {
        return word;
    }

    public int getDefid() {
        return defid;
    }

    public String getCurrent_vote() {
        return current_vote;
    }

    public String getWritten_on() {
        return written_on;
    }

    public String getExample() {
        return example;
    }

    public int getThumbs_down() {
        return thumbs_down;
    }
}
