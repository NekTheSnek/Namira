package com.multibot.namira.service;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Service;

@Service
public class DiscordListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        // Εδώ δεν κάνουμε καμία κλήση στη βάση, απλά απαντάμε ΑΜΕΣΑ
        String lang = event.getComponentId();

        event.deferReply(true).queue(); // Σταματάμε το "Interaction failed"

        String response = "Language set to: " + lang;
        event.getHook().sendMessage(response).queue();
    }
}