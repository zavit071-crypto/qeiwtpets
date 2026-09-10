package com.qeiwt.qeiwtpets;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class QeiwtPets extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        // រក្សាទុក config.yml ដើម
        saveDefaultConfig();
        
        // ចុះឈ្មោះ Command ឈ្មោះ qeiwtpets
        if (getCommand("qeiwtpets") != null) {
            getCommand("qeiwtpets").setExecutor(this);
        }
        
        getLogger().info("QeiwtPets has been enabled successfully! Made by qeiwt.");
    }

    @Override
    public void onDisable() {
        getLogger().info("QeiwtPets has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("qeiwtpets")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', " &#d977c2[QeiwtPets] &fRunning version 1.0.0 made by qeiwt."));
            return true;
        }
        return false;
    }
}
