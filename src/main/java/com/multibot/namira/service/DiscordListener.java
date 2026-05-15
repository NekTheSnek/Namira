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
            String name = event.getUser().getName();

            // Ψάχνουμε αν υπάρχει ο χρήστης
            User user = userRepository.findByDiscordId(discordId).orElse(null);
            String lang = "en"; // Default αν δεν υπάρχει

            if (user == null) {
                // Αν δεν υπάρχει, τον φτιάχνουμε με default 'en'
                user = new User(name, name + "@discord.com", discordId);
                userRepository.save(user);
            } else {
                // Αν υπάρχει, παίρνουμε τη γλώσσα του
                lang = user.getPreferredLanguage();
            }

            // Απάντηση βάσει της γλώσσας
            String response = switch (lang) {
                case "el" -> "Εγγραφή επιτυχής! Γεια σου " + name + "!";
                case "fr" -> "Inscription réussie ! Bonjour " + name + " !";
                default -> "Registration successful! Hello " + name + "!";
            };

            event.reply(response).setEphemeral(true).queue();
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("choose-lang")) {
            String selectedLang = event.getValues().get(0);
            String discordId = event.getUser().getId();
            String name = event.getUser().getName();

            // Ενημερώνουμε τη γλώσσα στη βάση με ασφάλεια
            userRepository.findByDiscordId(discordId).ifPresentOrElse(user -> {
                user.setPreferredLanguage(selectedLang);
                userRepository.save(user);
            }, () -> {
                User newUser = new User(name, name + "@discord.com", discordId);
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