package com.whatsapxml.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Setting {
    @XmlElement
    private String key;
    @XmlElement
    private String value;

    public Setting() {}

    public Setting(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}

