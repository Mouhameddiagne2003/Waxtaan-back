package com.whatsapxml.model;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class Message {
    @XmlElement
    private String id;
    @XmlElement
    private String sender;
    @XmlElement
    private String recipient;
    @XmlElement
    private String content;
    @XmlElement
    private String timestamp; // Utilise String pour la compatibilité JAXB
    @XmlElement
    private String type;
    @XmlElement
    private String file;

    public Message() {}

    public Message(String id, String sender, String recipient, String content, String timestamp, String type, String file) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = timestamp;
        this.type = type;
        this.file = file;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getFile() { return file; }
    public void setFile(String file) { this.file = file; }
}

