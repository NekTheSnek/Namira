package com.multibot.namira;

import com.multibot.namira.service.DiscordListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NamiraApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NamiraApplication.class, args);

        String token = context.getEnvironment().getProperty("BOT_TOKEN");
        DiscordListener listener = context.getBean(DiscordListener.class);

        if (token != null && !token.isEmpty()) {
            JDA jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(listener)
                    .build();

            // Δήλωση της εντολής με μεταφράσεις στο μενού του Discord
            jda.upsertCommand("register", "Register to our database")
                    .setNameLocalization(DiscordLocale.GREEK, "εγγραφή")
                    .setDescriptionLocalization(DiscordLocale.GREEK, "Κάνε εγγραφή στη βάση δεδομένων της Namira")
                    .setNameLocalization(DiscordLocale.FRENCH, "s-inscrire")
                    .setDescriptionLocalization(DiscordLocale.FRENCH, "Inscrivez-vous à la base de données Namira")
                    .queue();

            System.out.println("Namira Bot is Online with Multilingual Slash Commands!");
        }
    }
}