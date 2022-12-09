package com.propersama.cwmodularwarfarefixer;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static Map<String, String> loreNbt = new HashMap();

    public static void load() {
        loreNbt.clear();
        CWModularWarfareFixer.instance.saveDefaultConfig();
        FileConfiguration config = CWModularWarfareFixer.instance.getConfig();

        int status = 0;
        String currentLore = null;

        for (String data : config.getStringList("lore-nbt"))
            if (status == 0) {
                currentLore = data;
                status = 1;
            } else {
                String currentNbt = data;
                status = 0;
                loreNbt.put(currentLore.replace("&", "§"), currentNbt);
            }
    }

}
