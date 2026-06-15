package com.multibot.namira.service;

import com.multibot.namira.model.User;
import com.multibot.namira.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor Injection (Καλύτερη πρακτική από το @Autowired)
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ΜΕΘΟΔΟΣ ΓΙΑ ΤΟ WEB: Εγγραφή χρήστη από το Form
    @Transactional
    public void registerUser(String username, String email, String password) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setPreferredLanguage("en"); // Default τιμές
        newUser.setTimezone("UTC");
        userRepository.save(newUser);
    }

    // ΜΕΘΟΔΟΣ ΓΙΑ ΤΑ BOTS: Discord/Telegram ρυθμίσεις
    @Transactional
    public void setLanguageAndTimezone(long discordId, String lang) {
        userRepository.findByDiscordId(discordId).ifPresentOrElse(
                user -> {
                    user.setPreferredLanguage(lang);
                    user.setTimezone("UTC");
                    userRepository.save(user);
                },
                () -> {
                    User newUser = new User();
                    newUser.setDiscordId(discordId);
                    newUser.setPreferredLanguage(lang);
                    newUser.setTimezone("UTC");
                    userRepository.save(newUser);
                }
        );
    }
}