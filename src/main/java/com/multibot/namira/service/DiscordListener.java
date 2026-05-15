package com.multibot.namira.service;

import com.multibot.namira.model.User;
import com.multibot.namira.repository.UserRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscordListener extends ListenerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("register")) {
            String discordId = event.getUser().getId();
            String name = event.getUser().getName();

            // Έλεγχος αν υπάρχει ήδη
            boolean exists = userRepository.findAll().stream()
                    .anyMatch(u -> discordId.equals(u.getDiscordId()));

            if (!exists) {
                User newUser = new User(name, name + "@discord.com", discordId);
                userRepository.save(newUser);
                event.reply("Εγγραφή επιτυχής! Γεια σου " + name + "!").setEphemeral(true).queue();
            } else {
                event.reply("Είσαι ήδη εγγεγραμμένος στη Namira!").setEphemeral(true).queue();
            }
        }
    }
}