package main.utility;

public class SmmryObject {
    private String sm_api_message;
    private String sm_api_character_count;
    private String sm_api_content_reduced;
    private String sm_api_title;
    private String sm_api_content;
    private String sm_api_limitation;
    private String sm_api_keyword_array;
    private String sm_api_error;

    public String getSm_api_message() {
        return sm_api_message;
    }

    public String getSm_api_character_count() {
        return sm_api_character_count;
    }

    public String getSm_api_content_reduced() {
        return sm_api_content_reduced;
    }

    public String getSm_api_title() {
        return sm_api_title;
    }

    public String getSm_api_content() {
        return sm_api_content;
    }

    public String getSm_api_limitation() {
        return sm_api_limitation;
    }

    public String getSm_api_keyword_array() {
        return sm_api_keyword_array;
    }

    public String getSm_api_error() {
        return sm_api_error;
    }

    //util
    public boolean hasError() {
        return sm_api_error != null;
    }
}
