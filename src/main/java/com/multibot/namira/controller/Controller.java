package com.multibot.namira.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private String getStyles() {
        return "<style>" +
                "body { font-family: 'Segoe UI', sans-serif; background: #f0f2f5; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                ".container { background: white; padding: 3rem; border-radius: 15px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); text-align: center; width: 350px; }" +
                "h2 { color: #333; margin-bottom: 20px; }" +
                ".lang-btn { display: inline-block; padding: 10px 20px; margin: 5px; border: 2px solid #007bff; border-radius: 25px; color: #007bff; text-decoration: none; font-weight: bold; transition: 0.3s; }" +
                ".lang-btn:hover { background: #007bff; color: white; }" +
                "</style>";
    }

    @GetMapping("/")
    public String home() {
        return getStyles() +
                "<div class='container'>" +
                "<h2>Namira Welcome</h2>" +
                "<p>Please select your language:</p>" +
                "<a href='/set-lang?lang=en' class='lang-btn'>English</a>" +
                "<a href='/set-lang?lang=el' class='lang-btn'>Ελληνικά</a>" +
                "</div>";
    }

    @GetMapping("/set-lang")
    public String setLang(String lang) {
        return getStyles() +
                "<div class='container'>" +
                "<h2>Language set to: " + lang.toUpperCase() + "</h2>" +
                "<a href='/' class='lang-btn'>Back Home</a>" +
                "</div>";
    }
}