package com.multibot.namira;

import com.multibot.namira.service.DiscordListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NamiraApplication {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(NamiraApplication.class, args);

        String token = context.getEnvironment().getProperty("BOT_TOKEN");

        // 🛠️ Παίρνουμε ΜΟΝΟ τον δικό σου κλασικό DiscordListener
        DiscordListener listener = context.getBean(DiscordListener.class);

        if (token != null && !token.isEmpty()) {
            JDA jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                    .addEventListeners(listener) // Μόνο αυτόν δηλώνουμε!
                    .build();

            jda.awaitReady();

            // Ο Server σου
            Guild guild = jda.getGuildById("1123699556764562642");

            if (guild != null) {
                guild.updateCommands()
                        .addCommands(
                                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("register", "Εγγραφή στη βάση / Register"),
                                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("set_language", "Αλλαγή γλώσσας / Change language"),
                                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("setup_welcome", "Admin only: Spawns the welcome message"),

                                // 🔒 Προσθήκη /create-event με δικαιώματα μόνο για Admins/Moderators
                                net.dv8tion.jda.api.interactions.commands.build.Commands.slash("create-event", "Δημιουργήστε ένα νέο Event!")
                                        .addOption(OptionType.STRING, "title", "Ο τίτλος του event", true)
                                        .addOption(OptionType.STRING, "description", "Η περιγραφή του event", false)
                                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_SERVER))
                        ).queue();

                System.out.println("Namira Bot: Guild Commands Updated Instantly!");
            } else {
                System.out.println("Could not find Guild! Make sure the ID is correct.");
            }
        }
    }
}