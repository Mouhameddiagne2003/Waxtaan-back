package com.whatsapxml.service;

import com.whatsapxml.model.User;
import com.whatsapxml.xml.UsersWrapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.whatsapxml.exception.ResourceNotFoundException;
import com.whatsapxml.exception.DuplicateIdException;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

@Service
public class UserXmlService {
    private final String xmlFilePath = "data/users.xml";

    public List<User> getAllUsers() {
        try {
            File file = new File(xmlFilePath);
            if (!file.exists() || file.length() == 0) { // Ajout de la vérification de la taille
                return new ArrayList<>();
            }
            JAXBContext context = JAXBContext.newInstance(UsersWrapper.class);
            Unmarshaller um = context.createUnmarshaller();
            // Validation XSD
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("schemas/platform.xsd"));
            um.setSchema(schema);
            UsersWrapper wrapper = (UsersWrapper) um.unmarshal(file);
            return wrapper.getUsers() != null ? wrapper.getUsers() : new ArrayList<>();
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveAllUsers(List<User> users) {
        System.out.println("saveAllUsers: nb users = " + users.size());
        for (User u : users) {
            System.out.println("user: " + u.getId() + " - " + u.getName());
        }
        try {
            JAXBContext context = JAXBContext.newInstance(UsersWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Validation XSD
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("schemas/platform.xsd"));
            m.setSchema(schema);
            UsersWrapper wrapper = new UsersWrapper();
            wrapper.setUsers(users);
            File dir = new File("data");
            if (!dir.exists()) dir.mkdir();
            m.marshal(wrapper, new File(xmlFilePath));
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }
    }

    public User getUserById(String id) {
        System.out.println("size"+getAllUsers().size());
        System.out.println("id"+id);
        return getAllUsers().stream()
            .filter(u -> u.getId() != null && u.getId().trim().equals(id.trim()))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur avec l'id " + id + " non trouvé."));
    }

    public void addUser(User user) {
        List<User> users = getAllUsers();
        // Génération auto de l'id si non fourni
        if (user.getId() == null || user.getId().isEmpty()) {
            int maxId = users.stream()
                .filter(u -> u.getId() != null && u.getId().matches("\\d+"))
                .mapToInt(u -> Integer.parseInt(u.getId()))
                .max().orElse(0);
            user.setId(String.valueOf(maxId + 1));
        }
        boolean exists = users.stream().anyMatch(u -> u.getId() != null && u.getId().equals(user.getId()));
        if (exists) {
            throw new DuplicateIdException("Un utilisateur avec l'id " + user.getId() + " existe déjà.");
        }
        users.add(user);
        saveAllUsers(users);
    }

    public void updateUser(String id, User user) {
        List<User> users = getAllUsers();
        System.out.println("updateUser: avant modif nb users = " + users.size());
        boolean found = false;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.set(i, user);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new ResourceNotFoundException("Utilisateur avec l'id " + id + " non trouvé.");
        }
        System.out.println("updateUser: après modif nb users = " + users.size());
        saveAllUsers(users);
    }

    public void deleteUser(String id) {
        List<User> users = getAllUsers();
        boolean removed = users.removeIf(u -> u.getId().equals(id));
        if (!removed) {
            throw new ResourceNotFoundException("Utilisateur avec l'id " + id + " non trouvé.");
        }
        saveAllUsers(users);
    }
}
