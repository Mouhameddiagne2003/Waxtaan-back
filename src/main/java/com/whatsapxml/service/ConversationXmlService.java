package com.whatsapxml.service;

import com.whatsapxml.model.Conversation;
import com.whatsapxml.xml.ConversationsWrapper;
import jakarta.xml.bind.*;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

@Service
public class ConversationXmlService {
    private static final String XML_FILE = "data/conversations.xml";

    public List<Conversation> getAllConversations() {
        try {
            File file = new File(XML_FILE);
            if (!file.exists()) return new ArrayList<>();
            JAXBContext context = JAXBContext.newInstance(ConversationsWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            // Validation XSD
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("schemas/platform.xsd"));
            unmarshaller.setSchema(schema);
            ConversationsWrapper wrapper = (ConversationsWrapper) unmarshaller.unmarshal(file);
            return wrapper.getConversations();
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveAllConversations(List<Conversation> conversations) {
        try {
            JAXBContext context = JAXBContext.newInstance(ConversationsWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            // Validation XSD
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File("schemas/platform.xsd"));
            marshaller.setSchema(schema);
            ConversationsWrapper wrapper = new ConversationsWrapper(conversations);
            marshaller.marshal(wrapper, new File(XML_FILE));
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }
    }

    public List<Conversation> getUserConversations(String userId) {
        List<Conversation> all = getAllConversations();
        List<Conversation> result = new ArrayList<>();
        for (Conversation conv : all) {
            if (conv.getParticipants() != null && conv.getParticipants().contains(userId)) {
                result.add(conv);
            }
        }
        return result;
    }
}
