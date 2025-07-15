package com.whatsapxml.model;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "group")
@XmlAccessorType(XmlAccessType.FIELD)
public class Group {
    @XmlElement
    private String id;
    @XmlElement
    private String name;
    @XmlElementWrapper(name = "members")
    @XmlElement(name = "member")
    private List<String> members;

    public Group() {}

    public Group(String id, String name, List<String> members) {
        this.id = id;
        this.name = name;
        this.members = members;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<String> getMembers() { return members; }
    public void setMembers(List<String> members) { this.members = members; }
}

