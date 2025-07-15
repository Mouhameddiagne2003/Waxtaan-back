package com.whatsapxml.controller;

import com.whatsapxml.model.Message;
import com.whatsapxml.service.FileStorageService;
import com.whatsapxml.service.MessageXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageXmlService messageXmlService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public List<Message> getAllMessages() {
        return messageXmlService.getAllMessages();
    }

    @GetMapping("/{id}")
    public Message getMessage(@PathVariable String id) {
        return messageXmlService.getMessageById(id);
    }

    @PostMapping
    public Message addMessage(@RequestPart("message") Message message, @RequestPart(value = "file", required = false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.save(file);
            String fileDownloadUri = "/api/files/" + fileName;
            message.setFile(fileDownloadUri);
        }
        return messageXmlService.addMessage(message);
    }

    @PutMapping("/{id}")
    public Message updateMessage(@PathVariable String id, @RequestBody Message message) {
        return messageXmlService.updateMessage(id, message);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable String id) {
        messageXmlService.deleteMessage(id);
    }
}
