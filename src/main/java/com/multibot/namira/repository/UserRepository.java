package com.multibot.namira.repository;

import com.multibot.namira.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Ψάχνουμε με Long για να κουμπώνει 100% με το User.java σου!
    Optional<User> findByDiscordId(Long discordId);
}