package com.zenika.snmptrans.gui.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Map;

public class OutputWriter {

    @NotEmpty
    private String atClass;

    private Map<String, Object> settings;

    @JsonProperty("@class")
    public String getAtClass() {
        return atClass;
    }

    @JsonProperty("@class")
    public void setAtClass(String atClass) {
        this.atClass = atClass;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }
}
