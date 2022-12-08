package com.propersama.cwmodularwarfarefixer;

import com.comphenix.protocol.wrappers.nbt.NbtBase;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public static void add(String lore, String nbtData)
    {
        lore = lore.replace("&", "§");
        FileConfiguration config = CWModularWarfareFixer.instance.getConfig();
        loreNbt.put(lore, nbtData);
        List list = new ArrayList(loreNbt.size() * 2);
        for (Map.Entry entry : loreNbt.entrySet()) {
            list.add(entry.getKey());
            list.add(entry.getValue());
        }

        config.set("lore-nbt", list);
        CWModularWarfareFixer.instance.saveConfig();
    }

    public static void remove(String lore) {
        lore = lore.replace("&", "§");
        loreNbt.remove(lore);
        FileConfiguration config = CWModularWarfareFixer.instance.getConfig();
        List list = new ArrayList(loreNbt.size() * 2);
        for (Map.Entry entry : loreNbt.entrySet()) {
            list.add(entry.getKey());
            list.add(entry.getValue());
        }

        config.set("lore-nbt", list);
        CWModularWarfareFixer.instance.saveConfig();
    }

    public static List loadWarfareList() {
        FileConfiguration config = CWModularWarfareFixer.instance.getConfig();
        List warfareList = config.getStringList("WarfareList");
        return warfareList;
    }

    public static NbtBase loadFiremodeNBT() {
        FileConfiguration config = CWModularWarfareFixer.instance.getConfig();
        NbtBase nbtBase = Utils.string2Nbt(config.getString("FiremodeNBT"));
        return nbtBase;
    }

}
