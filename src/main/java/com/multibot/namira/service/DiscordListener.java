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

    // Constructor Injection για απόλυτη ασφάλεια και Clean Architecture
    public DiscordListener(UserRepository userRepository, TranslationService ts, TimeService timeService) {
        this.userRepository = userRepository;
        this.ts = ts;
        this.timeService = timeService;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getName();
        Long userId = event.getUser().getIdLong(); // Το παίρνουμε ως Long για τη βάση

        // Τραβάμε τον χρήστη για να ξέρουμε τη γλώσσα του. Αν δεν υπάρχει, default "en"
        User currentUser = userRepository.findByDiscordId(userId).orElse(null);
        String lang = (currentUser != null) ? currentUser.getPreferredLanguage() : "en";

        // 1. ADMIN COMMAND: /setup_welcome
        if (command.equals("setup_welcome")) {
            EmbedBuilder welcome = new EmbedBuilder()
                    .setTitle(ts.get(lang, "welcome_title"))
                    .setDescription(ts.get(lang, "welcome_desc"))
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

        // 2. SLASH COMMAND: /set_language
        else if (command.equals("set_language")) {
            EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(lang.equals("el") ? "🌐 Επιλογή Γλώσσας" : lang.equals("fr") ? "🌐 Choix de la Langue" : "🌐 Language Selection")
                    .setDescription(lang.equals("el") ? "Επιλέξτε την γλώσσα προτίμησής σας παρακάτω." : lang.equals("fr") ? "Veuillez sélectionner votre langue ci-dessous." : "Please select your preferred language below.")
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
        }

        // 3. SLASH COMMAND: /register
        else if (command.equals("register")) {
            String name = event.getUser().getName();

            if (currentUser == null) {
                // Χρήση του Long userId στον Constructor
                currentUser = new User(name, name + "@discord.com", userId);
                userRepository.save(currentUser);
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

        // 4. AUTOMATIC TOOLS COMMAND: /time (Τοπική ώρα βάσει Χρήστη)
        else if (command.equals("time")) {
            if (currentUser == null) {
                event.reply(ts.get(lang, "no_user")).setEphemeral(true).queue();
                return;
            }

            String localTime = timeService.formatUserTime(currentUser);
            String response = ts.get(lang, "current_time") + " **" + localTime + "**";

            event.reply(response).setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();

        if (buttonId.startsWith("lang-")) {
            String selectedLang = buttonId.substring(5);
            Long userId = event.getUser().getIdLong();
            String name = event.getUser().getName();

            // Αναζήτηση και δυναμική ενημέρωση Γλώσσας ΚΑΙ Timezone στη βάση
            User user = userRepository.findByDiscordId(userId).orElse(new User(name, name + "@discord.com", userId));
            user.setPreferredLanguage(selectedLang);

            // Αυτόματη αντιστοίχιση Timezone βάσει της γλώσσας που πάτησε
            switch (selectedLang) {
                case "el" -> user.setTimezone("Europe/Athens");
                case "fr" -> user.setTimezone("Europe/Paris");
                default -> user.setTimezone("Europe/London"); // Ή America/New_York
            }

            userRepository.save(user);

            EmbedBuilder replyEmbed = new EmbedBuilder().setColor(Color.CYAN);
            replyEmbed.setDescription(ts.get(selectedLang, "lang_changed"));

            event.replyEmbeds(replyEmbed.build()).setEphemeral(true).queue();
        }
    }
}