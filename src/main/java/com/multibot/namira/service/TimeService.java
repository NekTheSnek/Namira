package com.multibot.namira.service;

import com.multibot.namira.model.User;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class TimeService {

    public String formatUserTime(User user) {
        String timezoneId = (user.getTimezone() != null) ? user.getTimezone() : "UTC";
        ZoneId zoneId = ZoneId.of(timezoneId);
        ZonedDateTime userDateTime = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm (dd-MM-yyyy)");
        return userDateTime.format(formatter);
    }
}