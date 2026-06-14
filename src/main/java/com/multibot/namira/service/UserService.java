package com.multibot.namira.service;

import com.multibot.namira.model.User;
import com.multibot.namira.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Αυτή η μέθοδος κάνει "Setup" τον χρήστη με όλα τα απαραίτητα δεδομένα
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