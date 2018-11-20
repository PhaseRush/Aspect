
package main.utility.gist.gist_json;

import java.util.HashMap;
import java.util.Map;

public class Files {

    private Summary Summary;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Summary getSummary() {
        return Summary;
    }

    public void setSummary(Summary summary) {
        this.Summary = summary;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
