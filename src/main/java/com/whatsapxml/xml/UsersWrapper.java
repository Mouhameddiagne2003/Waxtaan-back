package com.whatsapxml.xml;

import com.whatsapxml.model.User;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "users")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsersWrapper {
    @XmlElement(name = "user")
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }
    public void setUsers(List<User> users) {
        this.users = users;
    }
}
