package com.whatsapxml.controller;

import com.whatsapxml.security.JwtUtil;
import com.whatsapxml.service.UserXmlService;
import com.whatsapxml.controller.AuthController.AuthRequest;
import com.whatsapxml.model.User;
import com.whatsapxml.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserXmlService userXmlService;
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = userXmlService.getAllUsers().stream()
                .filter(u -> u.getName().equals(request.getUsername()))
                .findFirst()
                .orElse(null);
       
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Identifiants invalides");
        }

        var claims = new HashMap<String, Object>();
        claims.put("userId", user.getId());
    
        String token = jwtUtil.generateToken(user.getId(), claims);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    static class AuthRequest {
        private String username;
        private String password;
        private String name;
        private String avatar;
        private String status;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<?> register(
            @RequestPart("username") String username,
            @RequestPart("password") String password,
            @RequestPart(value = "name", required = false) String name,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar,
            @RequestPart(value = "status", required = false) String status
    ) {
        User user = new User();
        user.setName(name != null ? name : username);
        user.setPassword(password);
        user.setStatus(status != null ? status : "active");

        if (avatar != null && !avatar.isEmpty()) {
            try {
                String fileName = fileStorageService.save(avatar);
                String fileDownloadUri = "/uploads/" + fileName;
                user.setAvatar(fileDownloadUri);
            } catch (Exception e) {
                user.setAvatar("https://api.dicebear.com/7.x/bottts/svg?seed=whatsappxml");
            }
        } else {
            user.setAvatar("https://api.dicebear.com/7.x/bottts/svg?seed=whatsappxml");
        }

        userXmlService.addUser(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = jwtUtil.generateToken(user.getId(), claims);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    static class AuthResponse {
        private String token;
        public AuthResponse(String token) { this.token = token; }
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
}
