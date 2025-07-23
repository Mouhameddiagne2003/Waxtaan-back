package com.whatsapxml.controller;

import com.whatsapxml.model.User;
import com.whatsapxml.security.JwtUtil;
import com.whatsapxml.service.FileStorageService;
import com.whatsapxml.service.UserXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.whatsapxml.service.MessageXmlService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @PostMapping("/{myId}/contacts")
    public ResponseEntity<?> addContact(@PathVariable String myId, @RequestBody String contactId) {
        User me = userXmlService.getUserById(myId);
        System.out.println("contactid"+contactId);
        // Enlève les guillemets éventuels autour de l'id
        String contactIdClean = contactId.replaceAll("^\"|\"$", "").trim();
        User contact = userXmlService.getUserById(contactIdClean);
        System.out.println("me"+me);
        System.out.println("contact"+contact);
        if (me == null || contact == null) {
            return ResponseEntity.status(404).body("Utilisateur ou contact non trouvé");
        }
        if (me.getContacts() != null && me.getContacts().contains(contactId)) {
            return ResponseEntity.badRequest().body("Ce contact est déjà dans votre liste");
        }
        if (me.getContacts() == null) {
            me.setContacts(new java.util.ArrayList<>());
        }
        me.getContacts().add(contactId);
        userXmlService.updateUser(myId, me);
        return ResponseEntity.ok("Contact ajouté");
    }
    @GetMapping("/by-username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userXmlService.getAllUsers().stream()
            .filter(u -> u.getName().trim().equalsIgnoreCase(username.trim()))
            .findFirst()
            .orElseThrow(() -> new com.whatsapxml.exception.ResourceNotFoundException("Aucun utilisateur avec ce nom"));
    }
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserXmlService userXmlService;

    @Autowired
    private FileStorageService fileStorageService;

    // AJOUTE CECI :
    @Autowired
    private MessageXmlService messageXmlService;

    @GetMapping("/me")
    public User getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(token);
        return userXmlService.getUserById(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userXmlService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        return userXmlService.getUserById(id);
    }

    @PostMapping
    public void addUser(@RequestBody User user) {
        userXmlService.addUser(user);
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateUserProfile(@RequestHeader("Authorization") String authHeader,
                                                  @RequestParam("name") String name,
                                                  @RequestParam("status") String status,
                                                  @RequestParam(value = "password", required = false) String password,
                                                  @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        String token = authHeader.replace("Bearer ", "");
        String userId = jwtUtil.extractUserId(token);
        User user = userXmlService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setName(name);
        user.setStatus(status);

        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }

        if (avatar != null && !avatar.isEmpty()) {
            try {
                String fileName = fileStorageService.save(avatar);
                String fileDownloadUri = "/api/files/" + fileName;
                user.setAvatar(fileDownloadUri);
            } catch (Exception e) {
                // Gérer l'erreur de sauvegarde de fichier
                return ResponseEntity.status(500).build();
            }
        }

        userXmlService.updateUser(userId, user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable String id, @RequestBody User user) {
        userXmlService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userXmlService.deleteUser(id);
    }

    @DeleteMapping("/{myId}/contacts/{contactId}")
    public ResponseEntity<?> removeContactAndMessages(
        @PathVariable String myId,
        @PathVariable String contactId,
        @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String userIdFromToken = jwtUtil.extractUserId(token);
        System.out.println("userIdFromToken=" + userIdFromToken + ", myId=" + myId);
        if (!userIdFromToken.equals(myId)) {
            return ResponseEntity.status(403).body("Non autorisé");
        }
        User me = userXmlService.getUserById(myId);
        if (me.getContacts() != null) {
            me.getContacts().removeIf(id -> id.equals(contactId) || id.equals("\"" + contactId + "\""));
            userXmlService.updateUser(myId, me);
        }
        // Suppression des messages entre les deux utilisateurs
        messageXmlService.deleteMessagesBetweenUsers(myId, contactId); // This line was commented out in the original file
        return ResponseEntity.ok("Contact et messages supprimés");
    }
}
