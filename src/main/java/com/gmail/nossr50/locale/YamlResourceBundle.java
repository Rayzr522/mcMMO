package com.gmail.nossr50.locale;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

/**
 * @author Rayzr522
 */
public class YamlResourceBundle extends ResourceBundle {
    private final File configFile;
    private Map<String, String> messages = new HashMap<String, String>();

    YamlResourceBundle(File configFile) {
        this.configFile = configFile;
        reload();
    }

    private static String baseKeyFor(String key) {
        return key.indexOf('.') == -1 ? "" : key.substring(0, key.lastIndexOf('.'));
    }

    public void reload() {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        messages.clear();

        for (String key : config.getKeys(true)) {
            messages.put(key, String.valueOf(config.get(key)));
        }
    }

    private String getOrDefault(String key, String defaultValue) {
        String value = messages.get(key);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    private String getPrefixFor(String key) {
        String next = key;
        String prefix = null;

        while (!(next = baseKeyFor(next)).isEmpty()) {
            prefix = getOrDefault(next + ".prefix", null);
            if (prefix != null) {
                break;
            }
        }

        if (prefix == null) {
            prefix = getOrDefault("prefix", "");
        }

        String prefixAddon = getOrDefault(baseKeyFor(key) + ".prefix-addon", "");
        return prefix + prefixAddon;
    }

    private String getTranslatedMessage(String key) {
        String message = messages.get(key);
        if (message == null) {
            return null;
        }

        return getPrefixFor(key) + message;
    }

    @Override
    protected Object handleGetObject(String key) {
        return getTranslatedMessage(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(messages.keySet());
    }
}
