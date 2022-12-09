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

    public static void putNbtBase(ItemStack item, String key, NbtBase<?> nbtBase) {
        NbtCompound nbtCompound = NbtFactory.asCompound(NbtFactory.fromItemTag(item));
        nbtCompound.put(key, nbtBase);
        NbtFactory.setItemTag(item, nbtCompound);
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

    public static boolean warfareHasFiremode(ItemStack item) {
        NbtBase nbtBase = Utils.getItemCompoundByKey(item, "firemode");
        if(nbtBase == null) {
            return false;
        }
        return true;
    }

    public static void initFiremode(Player player, ItemStack item) {
        FileConfiguration config = CWModularWarfareFixer.instance.getConfig();
        String fullData = config.getString("FiremodeNBT.FULL");
        String semiData = config.getString("FiremodeNBT.SEMI");
        NbtBase<?> fullNbtBase = Utils.string2Nbt(fullData);
        NbtBase<?> semiNbtBase = Utils.string2Nbt(semiData);

        if(Utils.itemIsEmpty(item)) {
            return;
        }
        if(Utils.warfareHasFiremode(item)) {
            return;
        }
        if(Utils.getFiremode(player, item.toString()).equals("FULL")) {
            Utils.putNbtBase(player.getItemInHand(), "firemode", fullNbtBase);
            //player.sendMessage("已将 " + item.getType().toString() + " 的射击模式设为FULL");
            return;
        }
        if(Utils.getFiremode(player, item.toString()).equals("SEMI")) {
            Utils.putNbtBase(player.getItemInHand(), "firemode", semiNbtBase);
            //player.sendMessage("已将 " + item.getType().toString() + " 的射击模式设为SEMI");
        }
    }
}
