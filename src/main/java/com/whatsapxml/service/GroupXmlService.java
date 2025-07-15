package com.whatsapxml.service;

import com.whatsapxml.model.Group;
import com.whatsapxml.model.User;
import com.whatsapxml.xml.GroupsWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

@Service
public class GroupXmlService {
    private final String xmlFilePath = "data/groups.xml";

    public List<Group> getAllGroups() {
        try {
            File file = new File(xmlFilePath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            JAXBContext context = JAXBContext.newInstance(GroupsWrapper.class);
            Unmarshaller um = context.createUnmarshaller();
            // Validation XSD
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("schemas/platform.xsd"));
            um.setSchema(schema);
            GroupsWrapper wrapper = (GroupsWrapper) um.unmarshal(file);
            return wrapper.getGroups() != null ? wrapper.getGroups() : new ArrayList<>();
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveAllGroups(List<Group> groups) {
        try {
            JAXBContext context = JAXBContext.newInstance(GroupsWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Validation XSD
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("schemas/platform.xsd"));
            m.setSchema(schema);
            GroupsWrapper wrapper = new GroupsWrapper();
            wrapper.setGroups(groups);
            File dir = new File("data");
            if (!dir.exists()) dir.mkdir();
            m.marshal(wrapper, new File(xmlFilePath));
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }
    }

    public Group getGroupById(String id) {
        return getAllGroups().stream().filter(g -> g.getId().equals(id)).findFirst().orElse(null);
    }

    // Suppose que UserXmlService est injecté
    private final UserXmlService userXmlService;
    public GroupXmlService(UserXmlService userXmlService) {
        this.userXmlService = userXmlService;
    }

    public void addGroup(Group group) {
        List<Group> groups = getAllGroups();
        // Générer un nouvel id auto-incrémenté
        int maxId = groups.stream()
        .mapToInt(g -> {
            try { return Integer.parseInt(g.getId()); }
            catch (Exception e) { return 0; }
        })
        .max().orElse(0);
        String newId = String.valueOf(maxId + 1000);
        group.setId(newId);
        // Ajout du créateur dans les membres si absent (on suppose que le front l'ajoute déjà, sinon à renforcer ici)
        List<String> members = group.getMembers();
        // Ajout du groupe dans la liste des groupes de chaque membre
        for (String memberId : members) {
            User user = userXmlService.getUserById(memberId);
            List<String> userGroups = user.getGroups();
            if (userGroups == null) {
                userGroups = new java.util.ArrayList<>();
                user.setGroups(userGroups);
            }
            if (!userGroups.contains(newId)) {
                userGroups.add(newId);
                userXmlService.updateUser(user.getId(), user);
            }
        }
        groups.add(group);
        saveAllGroups(groups);
    }

    public void updateGroup(String id, Group group) {
        List<Group> groups = getAllGroups();
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).getId().equals(id)) {
                groups.set(i, group);
                break;
            }
        }
        saveAllGroups(groups);
    }

    public void deleteGroup(String id) {
        List<Group> groups = getAllGroups();
        groups.removeIf(g -> g.getId().equals(id));
        saveAllGroups(groups);
    }
}
