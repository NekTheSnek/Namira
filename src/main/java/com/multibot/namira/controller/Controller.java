package com.multibot.namira.controller;

import com.multibot.namira.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final UserRepository userRepository;

    // Το Spring θα κάνει αυτόματα "inject" το userRepository εδώ
    public Controller(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private String getStyles() {
        return "<style>" +
                "body { font-family: 'Segoe UI', sans-serif; background: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                ".container { background: white; padding: 2.5rem; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.15); text-align: center; width: 320px; }" +
                "h2 { color: #333; margin-bottom: 1.5rem; }" +
                "button { width: 100%; padding: 12px; margin-top: 10px; border-radius: 6px; border: none; cursor: pointer; font-weight: bold; transition: 0.3s; }" +
                ".btn-blue { background: #4285f4; color: white; }" +
                ".btn-blue:hover { background: #357ae8; }" +
                ".btn-outline { background: #eee; color: #555; }" +
                "a { text-decoration: none; }" +
                "</style>";
    }

    @GetMapping("/")
    public String home() {
        return getStyles() +
                "<div class='container'>" +
                "<h2>Namira Auth</h2>" +
                "<p style='color: #666;'>Συνδεθείτε για πρόσβαση στη βάση.</p>" +
                "<a href='/oauth2/authorization/google'><button class='btn-blue'>Sign In with Google</button></a>" +
                "</div>";
    }

    @GetMapping("/secureaccess")
    public String secureaccess(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) return "Παρακαλώ συνδεθείτε.";

        String email = principal.getAttribute("email");

        // Εδώ γίνεται η μαγεία: Ελέγχουμε τη βάση δεδομένων στο Railway
        return userRepository.findByEmail(email)
                .map(user -> getStyles() +
                        "<div class='container'><h2>Access Granted</h2>" +
                        "<p>Καλώς ήρθες, <b>" + user.getUsername() + "</b>!</p>" +
                        "<a href='/logout'><button class='btn-outline' style='color:red;'>Logout</button></a></div>")
                .orElse(getStyles() +
                        "<div class='container'><h2>Access Denied</h2>" +
                        "<p>Το email <b>" + email + "</b> δεν βρέθηκε στη βάση.</p>" +
                        "<a href='/'><button class='btn-outline'>Back</button></a></div>");
    }
}