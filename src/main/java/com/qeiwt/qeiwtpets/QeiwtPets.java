package com.qeiwt.qeiwtpets;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QeiwtPets extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        // Save default config.yml
        saveDefaultConfig();
        
        // Ensure lang.yml exists
        File langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            saveResource("lang.yml", false);
        }

        // Ensure pets folder and gojo.yml exist
        File petsFolder = new File(getDataFolder(), "pets");
        if (!petsFolder.exists()) {
            petsFolder.mkdirs();
        }
        File gojoFile = new File(petsFolder, "gojo.yml");
        if (!gojoFile.exists()) {
            saveResource("pets/gojo.yml", false);
        }

        // Register main command
        if (getCommand("qeiwtpets") != null) {
            getCommand("qeiwtpets").setExecutor(this);
        }

        getLogger().info("QeiwtPets has been enabled successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("QeiwtPets has been disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        player.sendMessage(colorize("&#7F00FF&lQeiwtPets &8» &aPlugin is working perfectly and files are loaded!"));
        return true;
    }

    private String colorize(String text) {
        if (text == null) return "";
        Pattern pattern = Pattern.compile("<#([A-Fa-f0-9]{6})>|&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String color = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
            matcher.appendReplacement(buffer, ChatColor.COLOR_CHAR + "x"
                    + ChatColor.COLOR_CHAR + color.charAt(0) + ChatColor.COLOR_CHAR + color.charAt(1)
                    + ChatColor.COLOR_CHAR + color.charAt(2) + ChatColor.COLOR_CHAR + color.charAt(3)
                    + ChatColor.COLOR_CHAR + color.charAt(4) + ChatColor.COLOR_CHAR + color.charAt(5));
        }
        matcher.appendTail(buffer);
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
}
