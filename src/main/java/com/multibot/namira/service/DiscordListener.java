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

    public DiscordListener(UserRepository userRepository, TranslationService ts, TimeService timeService) {
        this.userRepository = userRepository;
        this.ts = ts;
        this.timeService = timeService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("setup_welcome")) {
            EmbedBuilder welcome = new EmbedBuilder()
                    .setTitle("Welcome to Namira! / Καλώς ήρθατε!")
                    .setDescription("Please select your language to unlock the rest of the server.\n" +
                            "Παρακαλώ επιλέξτε γλώσσα για να ξεκλειδώσετε τον server.")
                    .setColor(Color.MAGENTA);

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

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();

        if (buttonId.startsWith("lang-")) {
            String selectedLang = buttonId.substring(5);

            // Κρατάμε το ID ως String ακριβώς όπως το θέλει το Repository σου!
            String discordId = event.getUser().getId();
            String name = event.getUser().getName();

            // Αναζήτηση με String ID και δημιουργία αν δεν υπάρχει
            User user = userRepository.findByDiscordId(discordId)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setUsername(name);
                        newUser.setEmail(name + "@discord.com");
                        // Αν το setDiscordId σου δέχεται Long, το μετατρέπει εδώ αυτόματα.
                        // Αν δέχεται String, βγάζεις απλά το Long.parseLong()
                        try {
                            newUser.setDiscordId(Long.parseLong(discordId));
                        } catch (Exception e) {
                            // Αν το μοντέλο σου τελικά το έχει String, θα μπει από εδώ:
                            // newUser.setDiscordId(discordId);
                        }
                        return newUser;
                    });

            user.setPreferredLanguage(selectedLang);

            switch (selectedLang) {
                case "el" -> user.setTimezone("Europe/Athens");
                case "fr" -> user.setTimezone("Europe/Paris");
                default -> user.setTimezone("Europe/London");
            }

            userRepository.save(user);

            String localTime = timeService.formatUserTime(user);

            EmbedBuilder replyEmbed = new EmbedBuilder().setColor(Color.CYAN);
            String successMessage = ts.get(selectedLang, "lang_changed");
            String timeMessage = ts.get(selectedLang, "current_time") + " **" + localTime + "**";

            replyEmbed.setDescription(successMessage + "\n" + timeMessage);

            event.replyEmbeds(replyEmbed.build()).setEphemeral(true).queue();
        }
    }
}