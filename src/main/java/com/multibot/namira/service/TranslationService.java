package com.multibot.namira.service;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class TranslationService {

    // Κεντρικό Maps μεταφράσεων: Γλώσσα -> (Κλειδί -> Κείμενο)
    private final Map<String, Map<String, String>> dictionary = Map.of(
            "el", Map.of(
                    "welcome_title", "Welcome to Namira! / Καλώς ήρθατε!",
                    "welcome_desc", "Please select your language to unlock the rest of the server.\nΠαρακαλώ επιλέξτε γλώσσα για να ξεκλειδώσετε τον server.",
                    "lang_changed", "✨ Η γλώσσα άλλαξε επιτυχώς σε **Ελληνικά**!",
                    "current_time", "Η τοπική σας ώρα είναι: ",
                    "no_user", "Δεν βρέθηκε το προφίλ σας στη βάση. Πατήστε πρώτα ένα κουμπί γλώσσας!"
            ),
            "en", Map.of(
                    "welcome_title", "Welcome to Namira! / Καλώς ήρθατε!",
                    "welcome_desc", "Please select your language to unlock the rest of the server.\nΠαρακαλώ επιλέξτε γλώσσα για να ξεκλειδώσετε τον server.",
                    "lang_changed", "✨ Language successfully changed to **English**!",
                    "current_time", "Your local time is: ",
                    "no_user", "Your profile was not found. Please select a language flag first!"
            ),
            "fr", Map.of(
                    "welcome_title", "Welcome to Namira! / Καλώς ήρθατε!",
                    "welcome_desc", "Please select your language to unlock the rest of the server.\nΠαρακαλώ επιλέξτε γλώσσα για να ξεκλειδώσετε τον server.",
                    "lang_changed", "✨ La langue a été changée en **Français** !",
                    "current_time", "Votre heure locale est: ",
                    "no_user", "Votre profil n'a pas été trouvé. Veuillez d'abord cliquer sur un bouton de langue!"
            )
    );

    public String get(String lang, String key) {
        String safeLang = (lang != null) ? lang.toLowerCase() : "en";
        return dictionary.getOrDefault(safeLang, dictionary.get("en"))
                .getOrDefault(key, "Missing text: " + key);
    }
}