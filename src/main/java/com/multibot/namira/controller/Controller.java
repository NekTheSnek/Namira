package com.multibot.namira.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {

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

    // Η κεντρική σελίδα που τα έχει όλα
    @GetMapping("/")
    public String home() {
        return getStyles() +
                "<div class='container'>" +
                "<h2>Namira Auth</h2>" +
                // LOGIN ΦΟΡΜΑ
                "<form action='/login' method='post'>" +
                "  <input type='text' name='username' placeholder='Username'><br>" +
                "  <input type='password' name='password' placeholder='Password'><br>" +
                "  <button class='btn-blue'>Sign In</button>" +
                "</form>" +
                "<hr>" +
                // SIGN UP ΛΙΝΚ
                "<p>Don't have an account? <a href='/signup'>Sign Up</a></p>" +
                // GOOGLE EXTRA
                "<a href='/oauth2/authorization/google'><button class='btn-google'>Sign In with Google</button></a>" +
                "</div>";
    }

    // Η σελίδα εγγραφής
    @GetMapping("/signup")
    public String signup() {
        return getStyles() +
                "<div class='container'>" +
                "<h2>Create Account</h2>" +
                "<form action='/register' method='post'>" +
                "  <input type='text' name='username' placeholder='Choose Username'><br>" +
                "  <input type='password' name='password' placeholder='Choose Password'><br>" +
                "  <button class='btn-blue'>Register</button>" +
                "</form>" +
                "<a href='/'>Back to Login</a>" +
                "</div>";
    }
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        // 1. Εδώ δημιουργείς το αντικείμενο User
        User newUser = new User();
        newUser.setUsername(username);
        // ΠΡΟΣΟΧΗ: Χρησιμοποίησε έναν BCryptPasswordEncoder για τον κωδικό!
        newUser.setPassword(passwordEncoder.encode(password));

        // 2. Το αποθηκεύεις στη βάση
        userRepository.save(newUser);

        return "redirect:/"; // Επιστροφή στο Login
    }

}
