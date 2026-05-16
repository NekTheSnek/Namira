package com.multibot.namira;

import com.multibot.namira.service.DiscordListener;
import com.multibot.namira.service.EventCommandListener; // <--- ΕΔΩ: Import τον νέο listener
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
        EventCommandListener eventListener = context.getBean(EventCommandListener.class); // <--- ΕΔΩ: Παίρνουμε τον νέο listener από τη Spring

        if (token != null && !token.isEmpty()) {
            JDA jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(listener, eventListener) // <--- ΕΔΩ: Δηλώνουμε και τον νέο listener στο JDA
                    .build();

            // Περιμένουμε να συνδεθεί το Bot στο Discord
            jda.awaitReady();

            // Ο Server σου
            Guild guild = jda.getGuildById("1239855543144349787");

            if (guild != null) {
                guild.updateCommands()
                        .addCommands(
                                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("register", "Εγγραφή στη βάση / Register"),
                                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("set_language", "Αλλαγή γλώσσας / Change language"),
                                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("setup_welcome", "Admin only: Spawns the welcome message"),

                                // 🌟 ΕΔΩ: Προσθέτουμε την εντολή /create-event με τις επιλογές της!
                                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("create-event", "Δημιουργήστε ένα νέο Event!")
                                        .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "title", "Ο τίτλος του event", true)
                                        .addOption(net.dv8tion.jda.api.interactions.commands.OptionType.STRING, "description", "Η περιγραφή του event", false)
                        ).queue();

                System.out.println("Namira Bot: Guild Commands Updated Instantly with Create-Event!");
            } else {
                System.out.println("Could not find Guild! Make sure the ID is correct.");
            }
        }
    }
}