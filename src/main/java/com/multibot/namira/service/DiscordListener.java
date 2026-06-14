package com.multibot.namira.service;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.Permission;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;

@Service
public class DiscordListener extends ListenerAdapter {

    private final UserService userService;
    private final TranslationService translationService;

    // Ο Listener πλέον εξαρτάται ΜΟΝΟ από το Service, όχι από το Repository
    public DiscordListener(UserService userService, TranslationService translationService) {
        this.userService = userService;
        this.translationService = translationService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("setup_welcome")) {
            if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                event.reply("❌ Μόνο για Admins.").setEphemeral(true).queue();
                return;
            }

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
        event.deferReply(true).queue();

        CompletableFuture.runAsync(() -> {
            try {
                String componentId = event.getComponentId();
                if (componentId != null && componentId.startsWith("lang_")) {
                    String lang = componentId.split("_")[1];
                    long discordId = event.getUser().getIdLong();

                    // Καθαρή κλήση του Service - ο Listener δεν "βλέπει" τη βάση πλέον
                    userService.updateUserLanguage(discordId, lang);

                    event.getHook().sendMessage("✅ Η γλώσσα σου ενημερώθηκε σε: " + lang.toUpperCase()).queue();
                }
            } catch (Exception e) {
                e.printStackTrace();
                event.getHook().sendMessage("❌ Σφάλμα κατά την ενημέρωση.").queue();
            }
        });
    }
}