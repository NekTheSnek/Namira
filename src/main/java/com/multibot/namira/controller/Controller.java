package com.multibot.namira.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private String getStyles() {
        return "<style>" +
                "body { font-family: sans-serif; background: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                ".container { background: white; padding: 2.5rem; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); text-align: center; width: 320px; }" +
                "h2 { color: #333; margin-bottom: 1.5rem; }" +
                "input { width: 90%; padding: 10px; margin: 5px 0; border: 1px solid #ccc; border-radius: 4px; }" +
                "button { width: 100%; padding: 10px; margin-top: 10px; border-radius: 4px; border: none; cursor: pointer; font-weight: bold; }" +
                ".btn-blue { background: #007bff; color: white; }" +
                ".btn-google { background: #db4437; color: white; margin-top: 20px; }" +
                "a { text-decoration: none; }" +
                "</style>";
    }

    @GetMapping("/")
    public String home() {
        return getStyles() +
                "<div class='container'>" +
                "<h2>Namira Auth</h2>" +
                // Το δικό σας Login
                "<form action='/login' method='post'>" +
                "  <input type='text' name='username' placeholder='Username'><br>" +
                "  <input type='password' name='password' placeholder='Password'><br>" +
                "  <button type='submit' class='btn-blue'>Sign In</button>" +
                "</form>" +
                "<hr style='margin: 20px 0;'>" +
                // Το Extra Google Login
                "<a href='/oauth2/authorization/google'><button class='btn-google'>Sign In with Google</button></a>" +
                "</div>";
    }

    @GetMapping("/secureaccess")
    public String secureaccess(@AuthenticationPrincipal OAuth2User principal) {
        String name = (principal != null) ? principal.getAttribute("name") : "User";
        return getStyles() +
                "<div class='container'><h2>Secure Area</h2>" +
                "<p>Welcome, <b>" + name + "</b></p>" +
                "<a href='/'><button class='btn-blue'>Home</button></a>" +
                "</div>";
    }
}