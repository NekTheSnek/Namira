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
                    .setTitle(ts.get("en", "welcome_title"))
                    .setDescription(ts.get("en", "welcome_desc"))
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

            Long discordId = event.getUser().getIdLong();
            String name = event.getUser().getName();

            User user = userRepository.findByDiscordId(discordId)
                    .orElse(new User(name, name + "@discord.com", discordId));

            user.setPreferredLanguage(selectedLang);

            // Αυτόματη ανάθεση Timezone ανάλογα με τη γλώσσα που πάτησε
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