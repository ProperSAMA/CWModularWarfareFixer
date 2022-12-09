package com.propersama.cwmodularwarfarefixer;

import com.comphenix.protocol.wrappers.nbt.NbtBase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.List;

public final class CWModularWarfareFixer extends JavaPlugin {
    public static CWModularWarfareFixer instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Config.load();

        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerInteract(PlayerInteractEvent event) {
                ItemStack item = event.getItem();
                Player player = event.getPlayer();

                Utils.initFiremode(player, item);
            }
        }, this);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        FileConfiguration config = CWModularWarfareFixer.instance.getConfig();

        if (((sender instanceof Player)) && args.length <= 0) {
            return false;
        }

        if (((sender instanceof Player)) && (args.length > 1) && ("save".equalsIgnoreCase(args[0]))) {
            Player player = (Player)sender;
            ItemStack itemInHand = player.getItemInHand();
            NbtBase<?> nbtBase = Utils.getItemCompoundByKey(itemInHand, "firemode");
            String nbt2String = Utils.nbt2String(nbtBase);

            if("FULL".equalsIgnoreCase(args[1])) {
                config.set("FiremodeNBT.FULL", nbt2String);
                saveConfig();
                sender.sendMessage("保存完成(FULL)");
                return true;
            }
            if("SEMI".equalsIgnoreCase(args[1])) {
                config.set("FiremodeNBT.SEMI", nbt2String);
                saveConfig();
                sender.sendMessage("保存完成(SEMI)");
                return true;
            }
            else {
                sender.sendMessage("输入错误");
            }
        }

        if (((sender instanceof Player)) && ("update".equalsIgnoreCase(args[0]))) {
            Player player = (Player)sender;
            String fullData = config.getString("FiremodeNBT.FULL");
            String semiData = config.getString("FiremodeNBT.SEMI");
            NbtBase<?> fullNbtBase = Utils.string2Nbt(fullData);
            NbtBase<?> semiNbtBase = Utils.string2Nbt(semiData);
            ItemStack itemInHand = player.getItemInHand();

            if(Utils.getFiremode(player, itemInHand.toString()).equals("FULL")) {
                Utils.putNbtBase(player.getItemInHand(), "firemode", fullNbtBase);
                player.sendMessage("已将 " + itemInHand.getType().toString() + " 的射击模式设为FULL");
                return true;
            }
            if(Utils.getFiremode(player, itemInHand.toString()).equals("SEMI")) {
                Utils.putNbtBase(player.getItemInHand(), "firemode", semiNbtBase);
                player.sendMessage("已将 " + itemInHand.getType().toString() + " 的射击模式设为SEMI");
                return true;
            }
            else {
                player.sendMessage(itemInHand.getType().toString() + "未被列为ModularWarfare");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
