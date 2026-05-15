package com.multibot.namira.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String discordId; // Το νέο πεδίο

    // Default Constructor (Απαραίτητος για το Hibernate)
    public User() {}

    // Constructor για γρήγορη δημιουργία
    public User(String username, String email, String discordId) {
        this.username = username;
        this.email = email;
        this.discordId = discordId;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiscordId() { return discordId; }
    public void setDiscordId(String discordId) { this.discordId = discordId; }
}