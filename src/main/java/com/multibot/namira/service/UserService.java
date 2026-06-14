package com.multibot.namira.service;

import com.multibot.namira.model.User;
import com.multibot.namira.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Εδώ θα μπαίνει όλη η λογική που τώρα έχεις μέσα στον Listener
    public void updateUserLanguage(Long discordId, String newLanguage) {
        Optional<User> userOpt = userRepository.findByDiscordId(discordId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPreferredLanguage(newLanguage);
            userRepository.save(user);
        }
    }

    // Εδώ θα προσθέτουμε μελλοντικά και άλλες μεθόδους,
    // π.χ. για Telegram: updateTelegramLanguage(Long telegramId, String newLanguage)
}