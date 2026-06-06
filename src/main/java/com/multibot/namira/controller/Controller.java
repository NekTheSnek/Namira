package com.multibot.namira.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private String getStyles() {
        return "<style>" +
                "body { font-family: 'Segoe UI', sans-serif; background: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                ".container { background: white; padding: 2rem; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); text-align: center; width: 320px; }" +
                "h2 { color: #333; }" +
                "button { width: 100%; padding: 12px; margin-top: 10px; border-radius: 6px; border: none; cursor: pointer; font-weight: bold; transition: 0.3s; }" +
                ".btn-blue { background: #007bff; color: white; }" +
                ".btn-blue:hover { background: #0056b3; }" +
                ".btn-outline { background: #eee; color: #555; }" +
                "a { text-decoration: none; }" +
                "</style>";
    }

    @GetMapping("/")
    public String home() {
        return getStyles() +
                "<div class='container'>" +
                "<h2>Namira Auth</h2>" +
                "<p style='color: #666;'>Καλώς ήρθατε στο σύστημα ασφαλείας.</p>" +
                "<a href='/secureaccess'><button class='btn-blue'>Enter Secure Area</button></a>" +
                "</div>";
    }

    @GetMapping("/secureaccess")
    public String secureaccess(@AuthenticationPrincipal OAuth2User principal) {
        String name = (principal != null) ? principal.getAttribute("name") : "User";

        return getStyles() +
                "<div class='container'>" +
                "<h2>Success!</h2>" +
                "<p>Hello, <b>" + name + "</b></p>" +
                "<p style='font-size: 12px; color: #888;'>Πρόσβαση εγγυημένη.</p>" +
                "<a href='/'><button class='btn-outline'>Back Home</button></a>" +
                "<a href='/logout'><button class='btn-outline' style='color:red;'>Logout</button></a>" +
                "</div>";
    }
}