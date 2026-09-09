package com.qeiwt.qeiwtpet;

import com.qeiwt.qeiwtpet.listeners.PetListener;
import com.qeiwt.qeiwtpet.manager.PetManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class QeiwtPet extends JavaPlugin {

    private static QeiwtPet instance;
    private PetManager petManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        
        this.petManager = new PetManager(this);
        getServer().getPluginManager().registerEvents(new PetListener(this, petManager), this);

        if (getCommand("qeiwtpet") != null) {
            getCommand("qeiwtpet").setExecutor(this);
            getCommand("qeiwtpet").setTabCompleter(this);
        }

        getLogger().info("QeiwtPet (EcoPets Rebranded) enabled successfully!");
    }

    @Override
    public void onDisable() {
        if (petManager != null) {
            petManager.removeAllPets();
        }
        getLogger().info("QeiwtPet disabled!");
    }

    public static QeiwtPet getInstance() {
        return instance;
    }

    public PetManager getPetManager() {
        return petManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only in-game players can execute QeiwtPet commands!");
            return true;
        }

        if (args.length == 0) {
            petManager.openMainGUI(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("qeiwtpet.admin")) {
                player.sendMessage("§cYou lack permission to execute this command!");
                return true;
            }
            reloadConfig();
            petManager.reloadConfigValues();
            player.sendMessage("§a[QeiwtPet] All configurations and EcoPets logic reloaded!");
            return true;
        }

        if (args[0].equalsIgnoreCase("menu") || args[0].equalsIgnoreCase("gui")) {
            petManager.openMainGUI(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("deactivate")) {
            petManager.removeAllPetsForPlayer(player);
            player.sendMessage("§c[QeiwtPet] Deactivated all your active pets!");
            return true;
        }

        player.sendMessage("§e[QeiwtPet] Usage: §a/qpet §f| §a/qpet gui §f| §a/qpet clear §f| §a/qpet reload");
        return true;
    }
}
