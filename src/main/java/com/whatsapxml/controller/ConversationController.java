package com.whatsapxml.controller;

import com.whatsapxml.model.Conversation;
import com.whatsapxml.service.ConversationXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    @Autowired
    private ConversationXmlService conversationXmlService;

    @GetMapping("/user/{userId}")
    public List<Conversation> getUserConversations(@PathVariable String userId) {
        return conversationXmlService.getUserConversations(userId);
    }

    @GetMapping("")
    public List<Conversation> getAllConversations() {
        return conversationXmlService.getAllConversations();
    }
}
