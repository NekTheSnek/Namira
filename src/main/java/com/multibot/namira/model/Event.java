package com.multibot.namira.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime eventDate;
    private String creatorDiscordId; // Το Discord ID αυτού που το έφτιαξε

    // Default Constructor (Απαραίτητος για το Hibernate)
    public Event() {}

    // Constructor για γρήγορη δημιουργία
    public Event(String title, String description, LocalDateTime eventDate, String creatorDiscordId) {
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.creatorDiscordId = creatorDiscordId;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public String getCreatorDiscordId() { return creatorDiscordId; }
    public void setCreatorDiscordId(String creatorDiscordId) { this.creatorDiscordId = creatorDiscordId; }
}