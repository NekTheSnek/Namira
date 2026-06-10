package com.multibot.namira.controller;

import com.multibot.namira.model.User;
import com.multibot.namira.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private String getStyles() {
        return "<style>" +
                "body { font-family: sans-serif; background: #f4f7f6; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }" +
                ".container { background: white; padding: 2rem; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center; width: 300px; }" +
                "input { width: 90%; padding: 10px; margin: 5px 0; border: 1px solid #ddd; border-radius: 4px; }" +
                "button { width: 100%; padding: 10px; margin-top: 10px; border-radius: 4px; border: none; cursor: pointer; }" +
                ".btn-blue { background: #007bff; color: white; }" +
                ".btn-google { background: #db4437; color: white; margin-top: 20px; }" +
                "</style>";
    }

    @GetMapping("/")
    public String home() {
        return getStyles() +
                "<div class='container'>" +
                "<h2>Namira Auth</h2>" +
                "<form action='/login' method='post'>" +
                "  <input type='text' name='username' autocomplete='username' placeholder='Username'><br>" +
                "  <input type='password' name='password' autocomplete='current-password' placeholder='Password'><br>" +
                "  <button type='submit' class='btn-blue'>Sign In</button>" +
                "</form>" +
                "<hr>" +
                "<p>Don't have an account? <a href='/signup'>Sign Up</a></p>" +
                "</div>";
    }

    @GetMapping("/signup")
    public String signup() {
        return getStyles() +
                "<div class='container'>" +
                "<h2>Create Account</h2>" +
                "<form action='/register' method='post'>" +
                "  <input type='text' name='username' autocomplete='username' placeholder='Choose Username' required><br>" +
                "  <input type='email' name='email' autocomplete='email' placeholder='Enter Email' required><br>" +
                "  <input type='password' name='password' autocomplete='new-password' placeholder='Choose Password' required><br>" +
                "  <button type='submit' class='btn-blue'>Register</button>" +
                "</form>" +
                "</div>";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        // Εδώ μπορείς να ορίσεις αν είναι admin (π.χ. με βάση το email)
        if(email.equals("your-email@example.com")) {
            newUser.setRole("ADMIN");
        } else {
            newUser.setRole("USER");
        }
        userRepository.save(newUser);
        return "Registration successful! <a href='/'>Go to Login</a>";
    }
}