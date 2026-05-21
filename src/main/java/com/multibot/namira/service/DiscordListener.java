package com.multibot.namira.service;

import com.multibot.namira.model.User;
import com.multibot.namira.model.Event; // <--- Σιγουρέψου ότι έχεις αυτό το μοντέλο
import com.multibot.namira.repository.UserRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.util.Objects;

@Service
public class DiscordListener extends ListenerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository; // <--- ΕΔΩ: Inject το repository για να σώζουμε τα events

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();

        if (command.equals("setup_welcome")) {
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

        } else if (command.equals("create-event")) { // 🌟 ΕΔΩ: Η ΝΕΑ ΕΝΤΟΛΗ ΜΑΣ!
            String discordId = event.getUser().getId();

            // Παίρνουμε τα options που έγραψε ο χρήστης στο Discord
            String title = Objects.requireNonNull(event.getOption("title")).getAsString();
            String description = event.getOption("description") != null ?
                    Objects.requireNonNull(event.getOption("description")).getAsString() : "No description provided.";

            // Ψάχνουμε τη γλώσσα του χρήστη για να του απαντήσουμε σωστά
            User user = userRepository.findByDiscordId(discordId).orElse(null);
            String lang = (user != null) ? user.getPreferredLanguage() : "en";

            // Αποθήκευση του Event στη βάση δεδομένων
            Event newEvent = new Event();
            newEvent.setTitle(title);
            newEvent.setDescription(description);
            newEvent.setCreatorDiscordId(discordId);
            eventRepository.save(newEvent); // <--- Σώζεται live στη MySQL/PostgreSQL

            // Φτιάχνουμε το Embed απάντησης ανάλογα με τη γλώσσα του
            EmbedBuilder eventEmbed = new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setThumbnail(event.getUser().getEffectiveAvatarUrl());

            switch (lang) {
                case "el" -> eventEmbed.setTitle("📅 Το Event Δημιουργήθηκε Επιτυχώς!")
                        .setDescription("**Τίτλος:** " + title + "\n**Περιγραφή:** " + description + "\n\n*Αποθηκεύτηκε στη βάση δεδομένων της Namira!*");
                case "fr" -> eventEmbed.setTitle("📅 Événement Créé avec Succès!")
                        .setDescription("**Titre:** " + title + "\n**Description:** " + description + "\n\n*Enregistré dans la base de données !*");
                default -> eventEmbed.setTitle("📅 Event Created Successfully!")
                        .setDescription("**Title:** " + title + "\n**Description:** " + description + "\n\n*Saved in Namira database!*");
            }

            event.replyEmbeds(eventEmbed.build()).setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();

        if (buttonId.startsWith("lang-")) {
            String selectedLang = buttonId.substring(5);
            String discordId = event.getUser().getId();
            String name = event.getUser().getName();

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

            event.editMessageEmbeds(replyEmbed.build()).setComponents().queue();
        }
    }
}