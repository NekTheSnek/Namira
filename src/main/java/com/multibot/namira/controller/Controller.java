package com.multibot.namira.controller;

import com.multibot.namira.model.User;
import com.multibot.namira.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Το στυλ της φίλης σου (πιο καθαρό)
    private String Styles() {
        return "<style>body { font-family: sans-serif; background: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; } " +
                ".container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; width: 300px; } " +
                "button { width: 100%; padding: 10px; margin-top: 10px; border-radius: 4px; border: none; cursor: pointer; font-size: 14px; } " +
                ".btn-blue { background: #007bff; color: white; } " +
                ".btn-outline { background: white; border: 1px solid #ddd; } </style>";
    }

    @GetMapping("/")
    public String home() {
        return Styles() + "<div class='container'><h2>Namira Auth</h2>" +
                "<a href='/oauth2/authorization/google'><button class='btn-blue'>Sign in with Google</button></a>" +
                "<a href='/secureaccess'><button class='btn-outline'>Local Access</button></a></div>";
    }

    @GetMapping("/secureaccess")
    public String secureaccess(@AuthenticationPrincipal OAuth2User principal) {
        String name = (principal != null) ? principal.getAttribute("name") : "User";
        return Styles() + "<div class='container'><h2>Success</h2><p>Hello, <b>" + name + "</b></p>" +
                "<a href='/logout'><button class='btn-outline' style='color:red;'>Logout</button></a></div>";
    }
}