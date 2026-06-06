package com.multibot.namira.controller; // Αυτό "κουμπώνει" στο project σου

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    // Το UI της φίλης σου
    private String Styles() {
        return "<style>" +
                "body { font-family: sans-serif; background: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                ".container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; width: 300px; }" +
                "button { width: 100%; padding: 10px; margin-top: 10px; border-radius: 4px; border: 1px solid #ddd; cursor: pointer; font-size: 14px; }" +
                ".btn-blue { background: #007bff; color: white; border: none; }" +
                ".btn-google { background: #db4437; color: white; border: none; margin-top: 15px; }" +
                "a { text-decoration: none; }" +
                "</style>";
    }

    @GetMapping("/")
    public String home() {
        return Styles() +
                "<div class='container'>" +
                "<h2>Namira Auth</h2>" + // Το δικό της UI
                "<p style='color: #666;'>Welcome! Please sign in.</p>" +

                // Το κουμπί της
                "<a href='/secureaccess'><button class='btn-blue'>Enter Secure Area</button></a>" +

                // Το δικό σου extra Google Login
                "<a href='/oauth2/authorization/google'><button class='btn-google'>Sign In with Google</button></a>" +
                "</div>";
    }

    @GetMapping("/secureaccess")
    public String secureaccess(@AuthenticationPrincipal OAuth2User principal) {
        String name = (principal != null) ? principal.getAttribute("name") : "Guest";

        return Styles() +
                "<div class='container'>" +
                "<h2>Secure Area</h2>" +
                "<p>Hello, <b>" + name + "</b></p>" +
                "<a href='/'><button>Home</button></a>" +
                "<a href='/logout'><button style='color:red;'>Logout</button></a>" +
                "</div>";
    }
}