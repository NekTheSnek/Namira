package com.multibot.namira.repository;

import com.multibot.namira.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Αλλάζουμε το String σε Long για να ταιριάζει με τη βάση και το User.java σου!
    Optional<User> findByDiscordId(Long discordId);
}