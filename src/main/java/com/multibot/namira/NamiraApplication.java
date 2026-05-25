package com.multibot.namira;

import com.multibot.namira.service.DiscordListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NamiraApplication {

    @Value("${discord.bot.token}")
    private String botToken;

    public static void main(String[]Empty) {
        SpringApplication.run(NamiraApplication.class, Empty);
    }

    @Bean
    public CommandLineRunner initDiscordBot(DiscordListener discordListener) {
        return args -> {
            try {
                if (botToken == null || botToken.isEmpty() || botToken.equals("${BOT_TOKEN}")) {
                    System.out.println("❌ ERROR: Discord Bot Token is missing or invalid!");
                    return;
                }

                // Αρχικοποίηση JDA χωρίς awaitReady για να μην κρασάρει το Railway
                JDA jda = JDABuilder.createDefault(botToken)
                        .addEventListeners(discordListener)
                        .build();

                // Δήλωση των Slash Commands (Global για να παίζουν παντού με ασφάλεια)
                jda.updateCommands().addCommands(
                        Commands.slash("setup_welcome", "Spawn the welcome language selection embed"),
                        Commands.slash("create_event", "Δημιουργία νέου event με αυτόματη τοπική ώρα")
                                .addOption(OptionType.STRING, "title", "Ο τίτλος του event", true)
                                .addOption(OptionType.STRING, "description", "Η περιγραφή του event", true)
                                .addOption(OptionType.STRING, "date_time", "Format: HH:mm dd-MM-yyyy (π.χ. 21:00 25-05-2026)", true)
                ).queue();

                System.out.println("✅ Namira Bot successfully connected to Discord Gateway!");

            } catch (Exception e) {
                System.out.println("❌ Failed to start Discord Bot: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}