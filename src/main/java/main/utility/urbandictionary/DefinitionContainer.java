package main.utility.urbandictionary;

import java.util.List;

public class DefinitionContainer {
    private List<String> tags;
    private String result_type;
    private List<UDDefinition> list;
    private List<String> sounds;

    public List<String> getTags() {
        return tags;
    }

    public String getResult_type() {
        return result_type;
    }

    public List<UDDefinition> getList() {
        return list;
    }

    public List<String> getSounds() {
        return sounds;
    }
}
