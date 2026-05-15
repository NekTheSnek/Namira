package com.multibot.namira;

import com.multibot.namira.service.DiscordListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class NamiraApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NamiraApplication.class, args);

        // Παίρνουμε το Token από τα Variables του Railway
        String token = context.getEnvironment().getProperty("BOT_TOKEN");
        DiscordListener listener = context.getBean(DiscordListener.class);

        // Ξεκινάμε το Discord Bot
        JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(listener)
                .build();
    }
}