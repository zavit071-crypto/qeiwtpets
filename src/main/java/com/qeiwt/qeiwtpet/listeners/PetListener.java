package com.qeiwt.qeiwtpet.listeners;

import com.qeiwt.qeiwtpet.manager.PetManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PetListener implements Listener {

    private final PetManager petManager;

    public PetListener(PetManager petManager) {
        this.petManager = petManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        petManager.removeAllPetsForPlayer(event.getPlayer());
    }
}
