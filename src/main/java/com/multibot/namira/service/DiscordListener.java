package com.multibot.namira.service;

import com.multibot.namira.model.User;
import com.multibot.namira.repository.UserRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscordListener extends ListenerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("set_language")) {
            StringSelectMenu menu = StringSelectMenu.create("choose-lang")
                    .addOption("Ελληνικά 🇬🇷", "el")
                    .addOption("English 🇺🇸", "en")
                    .addOption("Français 🇫🇷", "fr")
                    .setPlaceholder("Επιλέξτε γλώσσα / Choose language")
                    .build();

            event.reply("Choose your preferred language for Namira Bot:")
                    .addActionRow(menu)
                    .setEphemeral(true)
                    .queue();

        } else if (command.equals("register")) {
            String discordId = event.getUser().getId();
            // Ψάχνουμε αν υπάρχει ο χρήστης
            User user = userRepository.findByDiscordId(discordId).orElse(null);

            if (user == null) {
                user = new User(event.getUser().getName(), event.getUser().getName() + "@discord.com", discordId);
                userRepository.save(user);
            }

            // Απάντηση βάσει της γλώσσας που έχει στη βάση
            String lang = user.getPreferredLanguage();
            String response = switch (lang) {
                case "el" -> "Εγγραφή επιτυχής! Γεια σου " + user.getUsername() + "!";
                case "fr" -> "Inscription réussie ! Bonjour " + user.getUsername() + " !";
                default -> "Registration successful! Hello " + user.getUsername() + "!";
            };
            event.reply(response).setEphemeral(true).queue();
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("choose-lang")) {
            String selectedLang = event.getValues().get(0);
            String discordId = event.getUser().getId();

            // Ενημερώνουμε τη γλώσσα στη βάση
            userRepository.findByDiscordId(discordId).ifPresentOrElse(user -> {
                user.setPreferredLanguage(selectedLang);
                userRepository.save(user);
            }, () -> {
                // Αν δεν έχει κάνει register ακόμα, τον φτιάχνουμε τώρα
                User newUser = new User(event.getUser().getName(), event.getUser().getName() + "@discord.com", discordId);
                newUser.setPreferredLanguage(selectedLang);
                userRepository.save(newUser);
            });

            String msg = switch (selectedLang) {
                case "el" -> "Η γλώσσα άλλαξε σε Ελληνικά!";
                case "fr" -> "La langue a été changée en Français !";
                default -> "Language changed to English!";
            };
            event.reply(msg).setEphemeral(true).queue();
        }
    }
}