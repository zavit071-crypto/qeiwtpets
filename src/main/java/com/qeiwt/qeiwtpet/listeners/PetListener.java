package com.qeiwt.qeiwtpet.listeners;

import com.qeiwt.qeiwtpet.QeiwtPet;
import com.qeiwt.qeiwtpet.manager.PetManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PetListener implements Listener {

    private final QeiwtPet plugin;
    private final PetManager petManager;

    public PetListener(QeiwtPet plugin, PetManager petManager) {
        this.plugin = plugin;
        this.petManager = petManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        petManager.removeAllPetsForPlayer(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().contains("QeiwtPet")) {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player player) {
                if (event.getRawSlot() < event.getInventory().getSize()) {
                    petManager.spawnPet(player);
                    player.closeInventory();
                }
            }
        }
    }
}
