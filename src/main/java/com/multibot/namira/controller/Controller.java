package com.multibot.namira.controller;

import com.multibot.namira.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final UserRepository userRepository;

    public Controller(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/secureaccess")
    public String secureaccess(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) return "Παρακαλώ συνδεθείτε.";

        String email = principal.getAttribute("email");
        return userRepository.findByEmail(email)
                .map(user -> "<h1>Access Granted</h1><p>Welcome, " + user.getUsername() + "!</p>")
                .orElse("<h1>Access Denied</h1><p>Δεν είστε εγγεγραμμένος χρήστης στη Namira.</p>");
    }
}