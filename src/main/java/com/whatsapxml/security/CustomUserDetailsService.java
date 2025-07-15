package com.whatsapxml.security;

import com.whatsapxml.model.User;
import com.whatsapxml.service.UserXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserXmlService userXmlService;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userXmlService.getAllUsers().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        return new org.springframework.security.core.userdetails.User(
                user.getId(), // Use ID as the username for UserDetails to match token subject
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );
    }
}
