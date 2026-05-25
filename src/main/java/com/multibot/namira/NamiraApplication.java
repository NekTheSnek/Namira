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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NamiraApplication {

    @Value("${discord.bot.token}")
    private String botToken;

    public static void main(String[] args) {
        SpringApplication.run(NamiraApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDiscordBot(ApplicationContext ctx) {
        return args -> {
            try {
                // Έλεγχος αν το token τραβιέται σωστά από το Railway
                if (botToken == null || botToken.isEmpty() || botToken.equals("${BOT_TOKEN}")) {
                    System.out.println("❌ DISCORD TOKEN IS MISSING! Check Railway Variables.");
                    return;
                }

                // Παίρνουμε τον Listener μέσω του ApplicationContext για να αποφύγουμε Circular Dependencies
                DiscordListener listener = ctx.getBean(DiscordListener.class);

                System.out.println("🚀 Starting JDA Connection...");
                JDA jda = JDABuilder.createDefault(botToken)
                        .addEventListeners(listener)
                        .build();

                // Καταχώρηση των εντολών στο Discord
                jda.updateCommands().addCommands(
                        Commands.slash("setup_welcome", "Spawn the welcome language selection embed"),
                        Commands.slash("create_event", "Δημιουργία νέου event")
                                .addOption(OptionType.STRING, "title", "Ο τίτλος του event", true)
                                .addOption(OptionType.STRING, "description", "Η περιγραφή του event", true)
                                .addOption(OptionType.STRING, "date_time", "Format: HH:mm dd-MM-yyyy", true)
                ).queue();

                System.out.println("✅ Namira Bot is now ONLINE!");

            } catch (Exception e) {
                System.out.println("❌ JDA Init Error: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}