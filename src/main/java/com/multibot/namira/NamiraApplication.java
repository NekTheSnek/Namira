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

        // Παίρνουμε το Token από τα Variables του Railway
        String token = context.getEnvironment().getProperty("BOT_TOKEN");
        DiscordListener listener = context.getBean(DiscordListener.class);

        if (token != null && !token.isEmpty()) {
            // Χτίζουμε το JDA και το αποθηκεύουμε σε μια μεταβλητή jda
            JDA jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(listener)
                    .build();

            // Τώρα στέλνουμε το Slash Command στο Discord
            jda.upsertCommand("register", "Κάνε εγγραφή στη βάση δεδομένων της Namira").queue();

            System.out.println("Namira Bot is Online with Slash Commands!");
        } else {
            System.err.println("ERROR: BOT_TOKEN is missing from Railway Variables!");
        }
    }
}