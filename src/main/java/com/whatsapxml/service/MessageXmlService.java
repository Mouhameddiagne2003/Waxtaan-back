package com.whatsapxml.service;

import com.whatsapxml.model.Message;
import com.whatsapxml.xml.MessagesWrapper;
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
public class MessageXmlService {
    private final String xmlFilePath = "data/messages.xml";

    public List<Message> getAllMessages() {
        try {
            File file = new File(xmlFilePath);
            if (!file.exists()) {
                return new ArrayList<>();
            }
            JAXBContext context = JAXBContext.newInstance(MessagesWrapper.class);
            Unmarshaller um = context.createUnmarshaller();
            // Validation XSD
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("schemas/platform.xsd"));
            um.setSchema(schema);
            MessagesWrapper wrapper = (MessagesWrapper) um.unmarshal(file);
            return wrapper.getMessages() != null ? wrapper.getMessages() : new ArrayList<>();
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveAllMessages(List<Message> messages) {
        try {
            JAXBContext context = JAXBContext.newInstance(MessagesWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Validation XSD
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("schemas/platform.xsd"));
            m.setSchema(schema);
            MessagesWrapper wrapper = new MessagesWrapper();
            wrapper.setMessages(messages);
            File dir = new File("data");
            if (!dir.exists()) dir.mkdir();
            m.marshal(wrapper, new File(xmlFilePath));
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }
    }

    public Message getMessageById(String id) {
        return getAllMessages().stream().filter(msg -> msg.getId().equals(id)).findFirst().orElse(null);
    }

    public Message addMessage(Message message) {
        List<Message> messages = getAllMessages();
        int maxId = messages.stream()
        .mapToInt(m -> {
            try { return Integer.parseInt(m.getId()); }
            catch (Exception e) { return 0; }
        })
        .max().orElse(0);
        String newId = String.valueOf(maxId + 1);
        message.setId(newId);
        messages.add(message);
        saveAllMessages(messages);
        return message;
    }

    public Message updateMessage(String id, Message message) {
        List<Message> messages = getAllMessages();
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getId().equals(id)) {
                message.setId(id);
                messages.set(i, message);
                saveAllMessages(messages);
                return message;
            }
        }
        return null; // Or throw an exception
    }

    public void deleteMessage(String id) {
        List<Message> messages = getAllMessages();
        messages.removeIf(msg -> msg.getId().equals(id));
        saveAllMessages(messages);
    }

    public void deleteMessagesBetweenUsers(String userId1, String userId2) {
        List<Message> messages = getAllMessages();
        messages.removeIf(msg ->
            (msg.getSender().equals(userId1) && msg.getRecipient().equals(userId2)) ||
            (msg.getSender().equals(userId2) && msg.getRecipient().equals(userId1))
        );
        saveAllMessages(messages);
    }
}
