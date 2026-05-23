package com.multibot.namira.repository;

import com.multibot.namira.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Το γυρνάμε σε Long για να κουμπώσει με το User.java και τον DiscordListener!
    Optional<User> findByDiscordId(Long discordId);
}