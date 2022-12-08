package com.propersama.cwmodularwarfarefixer;

import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.io.NbtTextSerializer;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String getItemNbt(ItemStack itemStack, String key) {
        NbtCompound nbtCompound = NbtFactory.asCompound(NbtFactory.fromItemTag(itemStack.clone()));
        return nbtCompound.getString(key);
    }
    public static NbtBase<?> getItemCompoundByKey(ItemStack itemStack, String key) {
        NbtCompound nbtCompound = NbtFactory.asCompound(NbtFactory.fromItemTag(itemStack.clone()));

        if (!nbtCompound.containsKey(key)) {
            return null;
        }
        return nbtCompound.getValue(key);
    }

    public static String nbt2String(NbtBase<?> nbtBase) {
        return new NbtTextSerializer().serialize(nbtBase);
    }

    public static NbtBase<?> string2Nbt(String data) {
        try {
            return new NbtTextSerializer().deserialize(data);
        } catch (IOException e) {
            e.printStackTrace();
        }return null;
    }

    public static boolean itemIsEmpty(ItemStack item) {
        return (item == null) || (item.getType() == Material.AIR) || (item.getAmount() == 0);
    }

    public static List<String> getLore(ItemStack item) {
        return item.getItemMeta().hasLore() ? item.getItemMeta().getLore() : new ArrayList();
    }

    public static void putNbtBase(ItemStack item, String key, NbtBase<?> nbtBase) {
        NbtCompound nbtCompound = NbtFactory.asCompound(NbtFactory.fromItemTag(item));
        nbtCompound.put(key, nbtBase);
        NbtFactory.setItemTag(item, nbtCompound);
    }

    public static void updateItem(ItemStack item, String data) {
        NbtBase nbtBase = Utils.string2Nbt(data);
        Utils.putNbtBase(item, "firemode", nbtBase);
    }

    public static void setFiremodeNBT(Player player, NbtBase nbtBase) {
        ItemStack item = player.getItemInHand();
        Utils.putNbtBase(item, "firemode", nbtBase);
    }

    public static String getFiremode(Player player, String itemType) {
        FileConfiguration config = CWModularWarfareFixer.instance.getConfig();
        List fullList = config.getList("WarfareList.FULL");
        List semiList = config.getList("WarfareList.SEMI");
        ItemStack itemInHand = player.getItemInHand();

        for (int i = 0; i < fullList.size(); i++) {
            if(itemInHand.getType().toString().equals(fullList.get(i).toString())) {
                return "FULL";
            }
        }
        for (int i = 0; i < semiList.size(); i++) {
            if(itemInHand.getType().toString().equals(semiList.get(i).toString())) {
                return "SEMI";
            }
        }
        return "UNKNOWN";
    }
}
