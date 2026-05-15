package com.multibot.namira.service;

import com.multibot.namira.model.User;
import com.multibot.namira.repository.UserRepository;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiscordListener extends ListenerAdapter {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getMessage().getContentRaw().equalsIgnoreCase("!hi")) {
            User user = new User(event.getAuthor().getName(), "test@test.com", event.getAuthor().getId());
            userRepository.save(user);
            event.getChannel().sendMessage("Σε αποθήκευσα στη βάση!").queue();
        }
    }
}