package com.multibot.namira;

import com.multibot.namira.service.DiscordListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NamiraApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(NamiraApplication.class, args);

        String token = context.getEnvironment().getProperty("BOT_TOKEN");
        DiscordListener listener = context.getBean(DiscordListener.class);

        if (token != null && !token.isEmpty()) {
            JDA jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(listener)
                    .build();

            // Περιμένουμε 2 δευτερόλεπτα να συνδεθεί το Bot στο Discord
            jda.awaitReady();

            // Αντί για Global, περνάμε τις εντολές απευθείας στον Server σου για να φαίνονται ΑΜΕΣΩΣ!
            // Αντικατάστησε το "ID_TOU_SERVER_SOU" με το πραγματικό ID του server σου (δεξί κλικ στον server -> Copy Server ID)
            Guild guild = jda.getGuildById("1239855543144349787");

            if (guild != null) {
                guild.updateCommands()
                        .addCommands(
                                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("register", "Εγγραφή στη βάση / Register"),
                                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("set_language", "Αλλαγή γλώσσας / Change language"),
                                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("setup_welcome", "Admin only: Spawns the welcome message")
                        ).queue();

                System.out.println("Namira Bot: Guild Commands Updated Instantly!");
            } else {
                System.out.println("Could not find Guild! Make sure the ID is correct.");
            }
        }
    }
}