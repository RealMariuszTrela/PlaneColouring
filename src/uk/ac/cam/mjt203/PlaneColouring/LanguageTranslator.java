package uk.ac.cam.mjt203.PlaneColouring;

import java.util.HashMap;
import java.util.Map;

public class LanguageTranslator {
    private static Map<String, Map<String, String>> translations = new HashMap<>();
    static {
        translations.put("pl", new HashMap<>());
        translations.get("pl").put("Plane", "Płaszczyzna");
        translations.get("pl").put("Plane colouring", "Kolorowanie płaszczyzn");
        translations.get("pl").put("Patterns", "Kolorowania do wyboru");
        translations.get("pl").put("Restore original", "Przywróć pierwotną wersję");
        translations.get("pl").put("Clear all added lines", "Usuń wszystkie proste");
        translations.get("pl").put("Number of colours: ", "Liczba kolorów: ");
        translations.get("pl").put("Selected colour", "Wybrany kolor");
        translations.get("pl").put("Slide", "Pochyl");
        translations.get("pl").put("Translate", "Przesuń");
    }
    public static String getTranslation(String lang, String s) {
        if(!translations.containsKey(lang)) return s;
        if(!translations.get(lang).containsKey(s)) return s;
        return translations.get(lang).get(s);
    }
}
