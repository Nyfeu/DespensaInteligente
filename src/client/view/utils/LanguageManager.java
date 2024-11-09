package client.view.utils;

import javax.swing.*;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    private static LanguageManager instance;
    private ResourceBundle resourceBundle;
    private Locale currentLocale;
    private JLabel idiomaLabel;

    private LanguageManager() {

        // Definindo o idioma padrão como Português
        currentLocale = new Locale("pt", "BR");
        loadResourceBundle();

    }

    public static LanguageManager getInstance() {

        if (instance == null) instance = new LanguageManager();
        return instance;

    }

    private void loadResourceBundle() {

        resourceBundle = ResourceBundle.getBundle("client.resources.properties.DespensaInteligente", currentLocale);

    }

    public ResourceBundle getResourceBundle() {

        return resourceBundle;

    }

    public Locale getCurrentLocale() {

        return currentLocale;

    }

    public void changeLanguage(Locale locale, JLabel idiomaLabel) {

        this.currentLocale = locale;
        this.idiomaLabel = idiomaLabel;
        loadResourceBundle();
        updateLanguageIcon();
        idiomaLabel.setText(getLanguageDisplayName(locale));

    }

    public String getLanguageDisplayName(Locale locale) {

        return switch (locale.getLanguage()) {
            case "pt" -> "Português";
            case "en" -> "English";
            case "it" -> "Italiano";
            case "es" -> "Español";
            case "fr" -> "Français";
            default -> "Idioma";
        };

    }

    private void updateLanguageIcon() {

        String iconPath = "client/resources/images/" + currentLocale.getLanguage() + "_" + currentLocale.getCountry() + ".png";
        URL iconUrl = getClass().getClassLoader().getResource(iconPath);

        if (iconUrl != null) idiomaLabel.setIcon(new ImageIcon(iconUrl));
        else idiomaLabel.setIcon(null);

    }

}
