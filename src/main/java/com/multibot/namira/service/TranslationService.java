package com.multibot.namira.service;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class TranslationService {

    private final Map<String, Map<String, String>> dictionary = Map.of(
            "el", Map.of(
                    "welcome_title", "Welcome to Namira! / Καλώς ήρθατε!",
                    "welcome_desc", "Please select your language to unlock the rest of the server.\nΠαρακαλώ επιλέξτε γλώσσα για να ξεκλειδώσετε τον server.",
                    "lang_changed", "✨ Η γλώσσα άλλαξε επιτυχώς σε **Ελληνικά**!",
                    "current_time", "Η τοπική σας ώρα είναι: "
            ),
            "en", Map.of(
                    "welcome_title", "Welcome to Namira! / Καλώς ήρθατε!",
                    "welcome_desc", "Please select your language to unlock the rest of the server.\nΠαρακαλώ επιλέξτε γλώσσα για να ξεκλειδώσετε τον server.",
                    "lang_changed", "✨ Language successfully changed to **English**!",
                    "current_time", "Your local time is: "
            ),
            "fr", Map.of(
                    "welcome_title", "Welcome to Namira! / Καλώς ήρθατε!",
                    "welcome_desc", "Please select your language to unlock the rest of the server.\nΠαρακαλώ επιλέξτε γλώσσα για να ξεκλειδώσετε τον server.",
                    "lang_changed", "✨ La langue a été changée en **Français** !",
                    "current_time", "Votre heure locale est: "
            )
    );

    public String get(String lang, String key) {
        String safeLang = (lang != null) ? lang.toLowerCase() : "en";
        return dictionary.getOrDefault(safeLang, dictionary.get("en"))
                .getOrDefault(key, "Missing text: " + key);
    }
}