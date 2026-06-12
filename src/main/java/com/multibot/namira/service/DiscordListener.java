package com.multibot.namira.service;

import com.multibot.namira.repository.UserRepository;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Service;

@Service
public class DiscordListener extends ListenerAdapter {
    private final UserRepository userRepository;
    private final TranslationService translationService;

    public DiscordListener(UserRepository userRepository, TranslationService translationService) {
        this.userRepository = userRepository;
        this.translationService = translationService;
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        userRepository.findByDiscordId(event.getUser().getIdLong())
                .ifPresentOrElse(user -> {
                    event.reply(translationService.get(user.getPreferredLanguage(), "lang_changed")).setEphemeral(true).queue();
                }, () -> {
                    event.reply("❌ Access Denied: Not registered.").setEphemeral(true).queue();
                });
    }
}