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
        // 1. Απαντάμε αμέσως στο Discord ότι επεξεργαζόμαστε το αίτημα
        event.deferReply(true).queue();

        // 2. Εκτελούμε τη λογική με τη βάση δεδομένων
        userRepository.findByDiscordId(event.getUser().getIdLong())
                .ifPresentOrElse(user -> {
                    // 3. Στέλνουμε το τελικό μήνυμα χρησιμοποιώντας το hook
                    event.getHook().sendMessage(translationService.get(user.getPreferredLanguage(), "lang_changed"))
                            .queue();
                }, () -> {
                    // 4. Στέλνουμε μήνυμα λάθους αν δεν βρεθεί
                    event.getHook().sendMessage("❌ Access Denied: Not registered.")
                            .queue();
                });
    }
}