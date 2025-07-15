package com.whatsapxml.xml;

import com.whatsapxml.model.Message;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "messages")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessagesWrapper {
    @XmlElement(name = "message")
    private List<Message> messages;

    public List<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) { this.messages = messages; }
}
