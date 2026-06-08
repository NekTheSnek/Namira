package com.multibot.namira.repository;

import com.multibot.namira.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByDiscordId(Long discordId);
    Optional<User> findByEmail(String email);

    // Προσθήκη για να μπορεί το σύστημα να βρίσκει τον χρήστη κατά το Login
    Optional<User> findByUsername(String username);
}