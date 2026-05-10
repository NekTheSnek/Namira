package com.multibot.namira.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return "<h1>Namira Universal Bot Hub</h1>" +
                    "Παρακαλώ <a href='/oauth2/authorization/google'>συνδεθείτε με Google</a> για να διαχειριστείτε τα events σας.";
        }

        String name = principal.getAttribute("name");
        String email = principal.getAttribute("email");

        return "<h1>Καλώς ήρθες στο Namira, " + name + "!</h1>" +
                "<p>Email σύνδεσης: " + email + "</p>" +
                "<hr>" +
                "<h3>Universal Control Panel Status:</h3>" +
                "<ul>" +
                "<li><b>Database (Railway):</b> Online</li>" +
                "<li><b>Security:</b> Active (Google OAuth2)</li>" +
                "<li><b>Organizer:</b> Ready to sync events</li>" +
                "</ul>";
    }
}