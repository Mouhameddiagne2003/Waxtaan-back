package com.whatsapxml.xml;

import com.whatsapxml.model.Group;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "groups")
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupsWrapper {
    @XmlElement(name = "group")
    private List<Group> groups;

    public List<Group> getGroups() { return groups; }
    public void setGroups(List<Group> groups) { this.groups = groups; }
}
