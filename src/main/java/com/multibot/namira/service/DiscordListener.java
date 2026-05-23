package com.multibot.namira.service;

import com.multibot.namira.model.User;
import com.multibot.namira.repository.UserRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Service;

import java.awt.Color;

@Service
public class DiscordListener extends ListenerAdapter {

    private final UserRepository userRepository;
    private final TranslationService ts;
    private final TimeService timeService;

    // Constructor Injection για τα Services
    public DiscordListener(UserRepository userRepository, TranslationService ts, TimeService timeService) {
        this.userRepository = userRepository;
        this.ts = ts;
        this.timeService = timeService;
    }

    // 1. Ο ADMIN ΣΤΗΝΕΙ ΤΟ WELCOME EMBED
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("setup_welcome")) {
            EmbedBuilder welcome = new EmbedBuilder()
                    .setTitle("Welcome to Namira! / Καλώς ήρθατε!")
                    .setDescription("Please select your language to unlock the rest of the server.\n" +
                            "Παρακαλώ επιλέξτε γλώσσα για να ξεκλειδώσετε τον server.")
                    .setColor(Color.MAGENTA);

            // Προσοχή στα IDs: "lang-el", "lang-en", "lang-fr"
            event.getChannel().sendMessageEmbeds(welcome.build())
                    .addActionRow(
                            Button.primary("lang-el", "Ελληνικά 🇬🇷"),
                            Button.secondary("lang-en", "English 🇺🇸"),
                            Button.danger("lang-fr", "Français 🇫🇷")
                    )
                    .queue();

            event.reply("Welcome message spawned!").setEphemeral(true).queue();
        }
    }

    // 2. ΟΤΑΝ ΟΙ ΧΡΗΣΤΕΣ ΠΑΤΑΝΕ ΤΑ ΚΟΥΜΠΙΑ
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();

        // Ελέγχουμε αν το κουμπί ξεκινάει από "lang-"
        if (buttonId.startsWith("lang-")) {
            String selectedLang = buttonId.substring(5); // Παίρνει το "el", "en" ή "fr"
            Long userId = event.getUser().getIdLong();
            String name = event.getUser().getName();

            // Αναζήτηση ή δημιουργία χρήστη στη βάση με Long Discord ID
            User user = userRepository.findByDiscordId(userId)
                    .orElse(new User(name, name + "@discord.com", userId));

            user.setPreferredLanguage(selectedLang);

            // Αντιστοίχιση Timezone βάσει της γλώσσας
            switch (selectedLang) {
                case "el" -> user.setTimezone("Europe/Athens");
                case "fr" -> user.setTimezone("Europe/Paris");
                default -> user.setTimezone("Europe/London");
            }

            // Αποθήκευση στην PostgreSQL
            userRepository.save(user);

            // Παίρνουμε την ώρα από το TimeService
            String localTime = timeService.formatUserTime(user);

            // Δημιουργία του κρυφού embed επιβεβαίωσης
            EmbedBuilder replyEmbed = new EmbedBuilder().setColor(Color.CYAN);

            String successMessage = ts.get(selectedLang, "lang_changed");
            String timeMessage = ts.get(selectedLang, "current_time") + " **" + localTime + "**";

            replyEmbed.setDescription(successMessage + "\n" + timeMessage);

            // Απαντάμε στον χρήστη κρυφά (Ephemeral) για να ολοκληρωθεί το interaction
            event.replyEmbeds(replyEmbed.build()).setEphemeral(true).queue();
        }
    }
}