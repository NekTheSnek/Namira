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
        if (command.equals("setup_welcome")) {
            // Έλεγχος αν είσαι εσύ ο Admin (προαιρετικό αλλά καλό)
            EmbedBuilder welcome = new EmbedBuilder()
                    .setTitle("Welcome to Namira! / Καλώς ήρθατε!")
                    .setDescription("Please select your language to unlock the rest of the server.\n" +
                            "Παρακαλώ επιλέξτε γλώσσα για να ξεκλειδώσετε τον server.")
                    .setColor(Color.MAGENTA);

            // Το Bot στέλνει το μήνυμα ΜΟΝΙΜΑ στο κανάλι
            event.getChannel().sendMessageEmbeds(welcome.build())
                    .addActionRow(
                            Button.primary("lang-el", "Ελληνικά 🇬🇷"),
                            Button.secondary("lang-en", "English 🇺🇸"),
                            Button.danger("lang-fr", "Français 🇫🇷")
                    )
                    .queue();

            event.reply("Welcome message spawned!").setEphemeral(true).queue();
        }

        if (command.equals("set_language")) {
            // Όμορφο Embed για την επιλογή γλώσσας
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle("🌐 Language Selection / Επιλογή Γλώσσας")
                    .setDescription("Please select your preferred language for Namira Bot below.\n" +
                            "Επιλέξτε την γλώσσα προτίμησής σας για το Namira Bot παρακάτω.")
                    .setColor(new Color(114, 137, 218)) // Discord Blue
                    .setThumbnail(event.getJDA().getSelfUser().getEffectiveAvatarUrl());

            // 3 Όμορφα Κουμπιά με Σημαίες
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

            // Φτιάχνουμε ένα πανέμορφο Embed για την επιτυχία
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
            String selectedLang = buttonId.substring(5); // Παίρνει το "el", "en" ή "fr"
            String discordId = event.getUser().getId();
            String name = event.getUser().getName();

            // Αποθήκευση στη βάση
            userRepository.findByDiscordId(discordId).ifPresentOrElse(user -> {
                user.setPreferredLanguage(selectedLang);
                userRepository.save(user);
            }, () -> {
                User newUser = new User(name, name + "@discord.com", discordId);
                newUser.setPreferredLanguage(selectedLang);
                userRepository.save(newUser);
            });

            // Μήνυμα επιβεβαίωσης σε Embed
            EmbedBuilder replyEmbed = new EmbedBuilder().setColor(Color.CYAN);

            switch (selectedLang) {
                case "el" -> replyEmbed.setDescription("✨ Η γλώσσα άλλαξε επιτυχώς σε **Ελληνικά**!");
                case "fr" -> replyEmbed.setDescription("✨ La langue α été changée en **Français** !");
                default -> replyEmbed.setDescription("✨ Language successfully changed to **English**!");
            }

            // Κάνουμε edit το αρχικό μήνυμα για να εξαφανιστούν τα κουμπιά αφού επιλέξει
            event.editMessageEmbeds(replyEmbed.build()).setComponents().queue();
        }
    }
}