package com.multibot.namira.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String role = "USER";
    @Column(name = "discord_id", unique = true, columnDefinition = "BIGINT")
    private Long discordId;

    private String preferredLanguage = "en";
    private String timezone = "UTC";

    public User() {}

    public User(String username, String password, String email, Long discordId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.discordId = discordId;
        this.preferredLanguage = "en";
        this.timezone = "UTC";
        this.role = "USER";
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getDiscordId() { return discordId; }
    public void setDiscordId(Long discordId) { this.discordId = discordId; }

    public String getPreferredLanguage() { return preferredLanguage; }
    public void setPreferredLanguage(String preferredLanguage) { this.preferredLanguage = preferredLanguage; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}