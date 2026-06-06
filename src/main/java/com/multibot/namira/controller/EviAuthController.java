package com.multibot.namira.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EviAuthController {

    private String Styles() {
        return "<style>" +
                "body { font-family: sans-serif; background: #f4f7f6; display: flex; " +
                "       justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                ".container { background: white; padding: 2rem; border-radius: 8px; " +
                "            box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; width: 300px; }" +
                "button { width: 100%; padding: 10px; margin-top: 10px; border-radius: 4px; " +
                "          border: 1px solid #ddd; cursor: pointer; font-size: 14px; }" +
                ".btn-blue { background: #007bff; color: white; border: none; }" +
                ".btn-outline { background: white; color: #555; }" +
                "a { text-decoration: none; }" +
                "</style>";
    }

    // Το αλλάξαμε σε /evi για να μην συγκρούεται με τον HomeController σου
    @GetMapping("/evi")
    public String home() {
        return Styles() +
                "<div class='container'>" +
                "<h2>Authenticator</h2>" +
                "<p style='color: #666;'>Welcome! Please sign in.</p>" +
                "<a href='/secureaccess'><button class='btn-blue'>Home</button></a>" +
                "</div>";
    }

    @GetMapping("/secureaccess")
    public String secureaccess(@AuthenticationPrincipal OAuth2User principal) {
        String name = (principal != null) ? principal.getAttribute("name") : "User";

        return Styles() +
                "<div class='container'>" +
                "<h2>Success</h2>" +
                "<p>Hello, <b>" + name + "</b></p>" +
                "<a href='/evi'><button class='btn-outline'>Home</button></a>" +
                "<a href='/logout'><button class='btn-outline' style='color:red;'>Logout</button></a>" +
                "</div>";
    }
}