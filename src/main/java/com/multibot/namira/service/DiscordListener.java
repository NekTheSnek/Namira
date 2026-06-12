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

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("setup_welcome")) {
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.reply("❌ Μόνο για Admins.").setEphemeral(true).queue();
                return;
            }
            // Εδώ το μήνυμα στέλνεται κανονικά στο κανάλι (όχι ephemeral)
            // για να μένει "καρφωμένο" σαν το Aleeva.
            event.getChannel().sendMessage("Καλώς ήρθες! Παρακαλώ επίλεξε τη γλώσσα σου:")
                    .addActionRow(
                            Button.primary("lang_el", "Ελληνικά"),
                            Button.primary("lang_en", "English")
                    ).queue();

            event.reply("Το μήνυμα υποδοχής στάλθηκε!").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        // Εδώ το deferReply σταματάει το "Interaction Failed"
        event.deferReply(true).queue();

        String componentId = event.getComponentId();
        if (componentId != null && componentId.startsWith("lang_")) {
            String lang = componentId.split("_")[1];
            long discordId = event.getUser().getIdLong();

            // Χρήση του getHook() για να απαντήσουμε μετά το deferReply
            userRepository.findByDiscordId(discordId).ifPresentOrElse(
                    user -> {
                        user.setPreferredLanguage(lang);
                        userRepository.save(user);
                        event.getHook().sendMessage("✅ Η γλώσσα σου ενημερώθηκε σε: " + lang.toUpperCase()).queue();
                    },
                    () -> {
                        User newUser = new User();
                        newUser.setDiscordId(discordId);
                        newUser.setPreferredLanguage(lang);
                        userRepository.save(newUser);
                        event.getHook().sendMessage("✅ Καλώς ήρθες! Η γλώσσα σου ορίστηκε σε: " + lang.toUpperCase()).queue();
                    }
            );
        }
    }
}