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

    @Column(unique = true) // Για να μην μπαίνει το ίδιο Discord account δύο φορές
    private Long discordId;

    private String preferredLanguage = "en"; // Default στα Αγγλικά
    private String timezone = "UTC";          // Default στο παγκόσμιο UTC

    // 1. Άδειος Constructor (Απαραίτητος για το Hibernate/JPA)
    public User() {}

    // 2. Constructor για το Registration
    public User(String username, String email, Long discordId) {
        this.username = username;
        this.email = email;
        this.discordId = discordId;
        this.preferredLanguage = "en";
        this.timezone = "UTC";
    }

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getDiscordId() { return discordId; }
    public void setDiscordId(Long discordId) { this.discordId = discordId; }

    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}