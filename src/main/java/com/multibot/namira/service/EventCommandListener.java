package com.multibot.namira.service;

import com.multibot.namira.model.Event;
import com.multibot.namira.repository.EventRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventCommandListener extends ListenerAdapter {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("create-event")) {

            String title = event.getOption("title").getAsString();
            String description = event.getOption("description") != null ? event.getOption("description").getAsString() : "No description provided.";
            String discordId = event.getUser().getId();

            // Δημιουργία του event αντικειμένου
            Event newEvent = new Event(title, description, LocalDateTime.now(), discordId);

            // Αποθήκευση στη βάση μέσω Hibernate
            eventRepository.save(newEvent);

            // Απάντηση στο Discord
            event.reply("🎉 Το event **" + title + "** δημιουργήθηκε με επιτυχία!").setEphemeral(true).queue();
        }
    }
}