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

    @Bean
    public CommandLineRunner initDiscordBot(DiscordListener listener) {
        return args -> {
            JDABuilder.createDefault(botToken)
                    .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(listener)
                    .build()
                    .updateCommands()
                    .addCommands(
                            Commands.slash("setup_welcome", "Spawn welcome message")
                    )
                    .queue();
        };
    }
}