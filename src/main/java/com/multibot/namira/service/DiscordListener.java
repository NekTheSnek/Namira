package com.multibot.namira.service;

import com.multibot.namira.repository.UserRepository;
import com.multibot.namira.model.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.Permission;
import org.springframework.stereotype.Service;

@Service
public class DiscordListener extends ListenerAdapter {

    private final UserRepository userRepository;
    private final TranslationService translationService;

    public DiscordListener(UserRepository userRepository, TranslationService translationService) {
        this.userRepository = userRepository;
        this.translationService = translationService;
    }

    // 1. Διαχείριση Slash Commands (Μόνο για Admins)
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("setup_welcome")) {
            // Έλεγχος δικαιωμάτων διαχειριστή
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.reply("❌ Δεν έχεις δικαιώματα διαχειριστή για αυτή την εντολή.").setEphemeral(true).queue();
                return;
            }

            // Αποστολή μηνύματος με κουμπιά
            event.reply("Καλώς ήρθες! Παρακαλώ επίλεξε τη γλώσσα σου:")
                    .addActionRow(
                            Button.primary("lang_el", "Ελληνικά"),
                            Button.primary("lang_en", "English")
                    ).queue();
        }
    }

    // 2. Διαχείριση Κουμπιών (Για όλους τους χρήστες)
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();

        if (componentId != null && componentId.startsWith("lang_")) {
            String lang = componentId.split("_")[1]; // παίρνει το 'el' ή 'en'
            long discordId = event.getUser().getIdLong();

            // Αναζήτηση και αποθήκευση στη βάση (Hibernate/PostgreSQL)
            userRepository.findByDiscordId(discordId).ifPresentOrElse(
                    user -> {
                        user.setPreferredLanguage(lang);
                        userRepository.save(user);
                        event.reply("✅ Η γλώσσα σου ενημερώθηκε σε: " + lang.toUpperCase()).setEphemeral(true).queue();
                    },
                    () -> {
                        User newUser = new User();
                        newUser.setDiscordId(discordId);
                        newUser.setPreferredLanguage(lang);
                        userRepository.save(newUser);
                        event.reply("✅ Καλώς ήρθες! Η γλώσσα σου ορίστηκε σε: " + lang.toUpperCase()).setEphemeral(true).queue();
                    }
            );
        }
    }
}