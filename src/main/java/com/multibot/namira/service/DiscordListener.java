package com.multibot.namira.service;

import com.multibot.namira.model.User;
import com.multibot.namira.repository.UserRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;

@Service
public class DiscordListener extends ListenerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        // 🔒 ADMIN COMMAND: Στήνει το αρχικό, μόνιμο μήνυμα
        if (command.equals("setup_welcome")) {
            EmbedBuilder welcome = new EmbedBuilder()
                    .setTitle("Welcome to Namira! / Καλώς ήρθατε!")
                    .setDescription("Please select your language to unlock the rest of the server.\n" +
                            "Παρακαλώ επιλέξτε γλώσσα για να ξεκλειδώσετε τον server.")
                    .setColor(Color.MAGENTA);

            // sendMessageEmbeds -> Το στέλνει κανονικά στο κανάλι και ΜΕΝΕΙ ΕΚΕΙ ΓΙΑ ΠΑΝΤΑ
            event.getChannel().sendMessageEmbeds(welcome.build())
                    .addActionRow(
                            Button.primary("lang-el", "Ελληνικά 🇬🇷"),
                            Button.secondary("lang-en", "English 🇺🇸"),
                            Button.danger("lang-fr", "Français 🇫🇷")
                    )
                    .queue();

            // Απαντάμε κρυφά στον Admin ότι η εντολή εκτελέστηκε
            event.reply("Welcome message spawned!").setEphemeral(true).queue();
        }

        // SLASH COMMAND: Χειροκίνητη αλλαγή γλώσσας (κρυφό embed)
        if (command.equals("set_language")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("🌐 Language Selection / Επιλογή Γλώσσας")
                    .setDescription("Please select your preferred language for Namira Bot below.\n" +
                            "Επιλέξτε την γλώσσα προτίμησής σας για το Namira Bot παρακάτω.")
                    .setColor(new Color(114, 137, 218))
                    .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl());

            event.replyEmbeds(embed.build())
                    .addActionRow(
                            Button.primary("lang-el", "Ελληνικά 🇬🇷"),
                            Button.secondary("lang-en", "English 🇺🇸"),
                            Button.danger("lang-fr", "Français 🇫🇷")
                    )
                    .setEphemeral(true)
                    .queue();

        } else if (command.equals("register")) {
            String discordId = event.getUser().getId();
            String name = event.getUser().getName();

            User user = userRepository.findByDiscordId(discordId).orElse(null);
            String lang = "en";

            if (user == null) {
                user = new User(name, name + "@discord.com", discordId);
                userRepository.save(user);
            } else {
                lang = user.getPreferredLanguage();
            }

            EmbedBuilder successEmbed = new EmbedBuilder()
                    .setThumbnail(event.getUser().getEffectiveAvatarUrl())
                    .setColor(Color.GREEN);

            switch (lang) {
                case "el" -> successEmbed.setTitle("✅ Η εγγραφή ολοκληρώθηκε!")
                        .setDescription("Γεια σου **" + name + "**!\nΚαλώς όρισες στο σύστημα της Namira. Τα στοιχεία σου αποθηκεύτηκαν με ασφάλεια.");
                case "fr" -> successEmbed.setTitle("✅ Inscription Réussie!")
                        .setDescription("Bonjour **" + name + "**!\nBienvenue sur Namira. Vos données ont été enregistrées avec succès.");
                default -> successEmbed.setTitle("✅ Registration Successful!")
                        .setDescription("Hello **" + name + "**!\nWelcome to Namira system. Your details have been securely saved.");
            }

            event.replyEmbeds(successEmbed.build()).setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();

        if (buttonId.startsWith("lang-")) {
            String selectedLang = buttonId.substring(5);
            String discordId = event.getUser().getId();
            String name = event.getUser().getName();

            // Αποθήκευση ή ενημέρωση στη βάση
            userRepository.findByDiscordId(discordId).ifPresentOrElse(user -> {
                user.setPreferredLanguage(selectedLang);
                userRepository.save(user);
            }, () -> {
                User newUser = new User(name, name + "@discord.com", discordId);
                newUser.setPreferredLanguage(selectedLang);
                userRepository.save(newUser);
            });

            EmbedBuilder replyEmbed = new EmbedBuilder().setColor(Color.CYAN);

            switch (selectedLang) {
                case "el" -> replyEmbed.setDescription("✨ Η γλώσσα άλλαξε επιτυχώς σε **Ελληνικά**!");
                case "fr" -> replyEmbed.setDescription("✨ La langue a été changée en **Français** !");
                default -> replyEmbed.setDescription("✨ Language successfully changed to **English**!");
            }

            // 🔥 Η ΜΕΓΑΛΗ ΑΛΛΑΓΗ: Χρησιμοποιούμε event.replyEmbeds().setEphemeral(true)
            // Αντί να αλλάξουμε το κεντρικό μήνυμα, απαντάμε στον χρήστη με ένα ΚΡΥΦΟ μήνυμα που βλέπει ΜΟΝΟ αυτός.
            // Έτσι, το welcome μήνυμα στο κανάλι μένει άθικτο και πεντακάθαρο για τον επόμενο!
            event.replyEmbeds(replyEmbed.build()).setEphemeral(true).queue();
        }
    }
}