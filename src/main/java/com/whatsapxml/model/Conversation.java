package com.whatsapxml.model;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "conversation")
@XmlAccessorType(XmlAccessType.FIELD)
public class Conversation {
    @XmlElement
    private String id;
    @XmlElement
    private String type; // "private" ou "group"
    @XmlElementWrapper(name = "participants")
    @XmlElement(name = "user")
    private List<String> participants; // user ids
    @XmlElementWrapper(name = "messages")
    @XmlElement(name = "message")
    private List<Message> messages;

    public Conversation() {}

    public Conversation(String id, String type, List<String> participants, List<Message> messages) {
        this.id = id;
        this.type = type;
        this.participants = participants;
        this.messages = messages;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public List<String> getParticipants() { return participants; }
    public void setParticipants(List<String> participants) { this.participants = participants; }
    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
}
