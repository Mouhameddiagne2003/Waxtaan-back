package com.whatsapxml.xml;

import com.whatsapxml.model.Conversation;
import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "conversations")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConversationsWrapper {
    @XmlElement(name = "conversation")
    private List<Conversation> conversations;

    public ConversationsWrapper() {}
    public ConversationsWrapper(List<Conversation> conversations) {
        this.conversations = conversations;
    }
    public List<Conversation> getConversations() { return conversations; }
    public void setConversations(List<Conversation> conversations) { this.conversations = conversations; }
}
