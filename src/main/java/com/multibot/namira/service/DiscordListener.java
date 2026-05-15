package com.multibot.namira.service;

import com.multibot.namira.model.User;
import com.multibot.namira.repository.UserRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscordListener extends ListenerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("register")) {
            DiscordLocale userLocale = event.getUserLocale();
            String discordId = event.getUser().getId();
            String name = event.getUser().getName();

            // Έλεγχος αν ο χρήστης υπάρχει ήδη στη βάση
            boolean exists = userRepository.findAll().stream()
                    .anyMatch(u -> discordId.equals(u.getDiscordId()));

            String response;
            if (!exists) {
                // Αποθήκευση νέου χρήστη
                userRepository.save(new User(name, name + "@discord.com", discordId));

                response = getTranslation(userLocale,
                        "Εγγραφή επιτυχής! Γεια σου " + name + "!",
                        "Registration successful! Hello " + name + "!",
                        "Inscription réussie ! Bonjour " + name + " !");
            } else {
                response = getTranslation(userLocale,
                        "Είσαι ήδη εγγεγραμμένος στη Namira!",
                        "You are already registered in Namira!",
                        "Vous êtes déjà inscrit sur Namira !");
            }

            // Απάντηση μόνο στον χρήστη (Ephemeral)
            event.reply(response).setEphemeral(true).queue();
        }
    }

    // Βοηθητική μέθοδος για τις 3 γλώσσες
    private String getTranslation(DiscordLocale locale, String greek, String english, String french) {
        if (locale == DiscordLocale.GREEK) return greek;
        if (locale == DiscordLocale.FRENCH) return french;
        return english; // Default στα Αγγλικά
    }
}