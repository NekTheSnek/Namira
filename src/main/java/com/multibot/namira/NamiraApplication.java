package com.multibot.namira;

import com.multibot.namira.service.DiscordListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NamiraApplication {

    @Value("${BOT_TOKEN}") // Τώρα θα βρει ακριβώς αυτό που έχεις στο image_e10147.png
    private String botToken;

    public static void main(String[] args) {
        SpringApplication.run(NamiraApplication.class, args);
    }

    // Αντικατάστησε το υπάρχον initDiscordBot με αυτό:
    @Bean
    public CommandLineRunner initDiscordBot(DiscordListener listener) {
        return args -> {
            net.dv8tion.jda.api.JDA jda = net.dv8tion.jda.api.JDABuilder.createDefault(botToken)
                    .enableIntents(
                            net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES,
                            net.dv8tion.jda.api.requests.GatewayIntent.MESSAGE_CONTENT,
                            net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS
                    )
                    .addEventListeners(listener)
                    .build();

            jda.awaitReady(); // ΠΕΡΙΜΕΝΕΙ να συνδεθεί ΠΡΙΝ κάνει οτιδήποτε άλλο
            jda.updateCommands().addCommands(
                    net.dv8tion.jda.api.interactions.commands.build.Commands.slash("setup_welcome", "Spawn welcome message")
            ).queue();
        };
    }
}