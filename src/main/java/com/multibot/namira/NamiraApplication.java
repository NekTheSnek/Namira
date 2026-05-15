package com.multibot.namira;

import com.multibot.namira.service.DiscordListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
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

            // Εγγραφή των δύο εντολών
            jda.upsertCommand("register", "Εγγραφή στη βάση / Register to database").queue();
            jda.upsertCommand("set_language", "Αλλαγή γλώσσας / Change language").queue();

            System.out.println("Namira Bot is Online!");
        }
    }
}