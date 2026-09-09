package com.qeiwt.qeiwtpet;

import com.qeiwt.qeiwtpet.listeners.PetListener;
import com.qeiwt.qeiwtpet.manager.PetManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class QeiwtPet extends JavaPlugin {

    private PetManager petManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.petManager = new PetManager(this);

        getServer().getPluginManager().registerEvents(new PetListener(petManager), this);
        if (getCommand("qeiwtpet") != null) {
            getCommand("qeiwtpet").setExecutor(this);
        }

        getLogger().info("QeiwtPet Enabled with Max Active Pets: " + getConfig().getInt("max-active-pets", 2));
    }

    @Override
    public void onDisable() {
        if (petManager != null) {
            petManager.removeAllPets();
        }
        getLogger().info("QeiwtPet Disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players in-game!");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("qeiwtpet.admin")) {
                player.sendMessage("§cYou do not have permission to use this command!");
                return true;
            }
            reloadConfig();
            petManager.reloadConfigValues();
            player.sendMessage("§a[QeiwtPet] Configuration reloaded successfully!");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("spawn")) {
            petManager.spawnPet(player);
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("clear")) {
            petManager.removeAllPetsForPlayer(player);
            player.sendMessage("§c[QeiwtPet] Despawned all active pets for you!");
            return true;
        }

        player.sendMessage("§e[QeiwtPet] §fUsage: §a/qpet spawn §f| §a/qpet clear §f| §a/qpet reload");
        return true;
    }
}
