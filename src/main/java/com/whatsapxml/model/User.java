package com.whatsapxml.model;

import jakarta.xml.bind.annotation.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    @XmlElement
    @NotBlank(message = "L'identifiant est obligatoire")
    private String id;
    @XmlElement
    @NotBlank(message = "Le nom est obligatoire")
    private String name;
    @XmlElement
    private String avatar;
    @XmlElement
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @XmlElement
    private String status;
    @XmlElementWrapper(name = "contacts")
    @XmlElement(name = "contact")
    private List<String> contacts;
    @XmlElementWrapper(name = "groups")
    @XmlElement(name = "group")
    private List<String> groups;
    @XmlElementWrapper(name = "settings")
    @XmlElement(name = "setting")
    private List<Setting> settings;

    public User() {}

    public User(String id, String name, String avatar, String status, List<String> contacts, List<String> groups, List<Setting> settings) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.status = status;
        this.contacts = contacts;
        this.groups = groups;
        this.settings = settings;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getContacts() { return contacts; }
    public void setContacts(List<String> contacts) { this.contacts = contacts; }

    public List<String> getGroups() { return groups; }
    public void setGroups(List<String> groups) { this.groups = groups; }

    public List<Setting> getSettings() { return settings; }
    public void setSettings(List<Setting> settings) { this.settings = settings; }
}

