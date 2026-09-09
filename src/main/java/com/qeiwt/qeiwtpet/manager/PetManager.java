package com.qeiwt.qeiwtpet.manager;

import com.qeiwt.qeiwtpet.QeiwtPet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class PetManager {

    private final QeiwtPet plugin;
    private final Map<UUID, List<ArmorStand>> activePetsMap = new HashMap<>();

    private int maxActivePets;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private boolean showHologram;

    public PetManager(QeiwtPet plugin) {
        this.plugin = plugin;
        reloadConfigValues();
        startPetFollowTask();
    }

    public void reloadConfigValues() {
        this.maxActivePets = plugin.getConfig().getInt("max-active-pets", 2);
        this.offsetX = plugin.getConfig().getDouble("pet-entity.location-x-offset", 0.75);
        this.offsetY = plugin.getConfig().getDouble("pet-entity.location-y-offset", -0.1);
        this.offsetZ = plugin.getConfig().getDouble("pet-entity.location_z_offset", 1.5);
        this.showHologram = plugin.getConfig().getBoolean("pet-entity.show-hologram", true);
    }

    public void openMainGUI(Player player) {
        int rows = plugin.getConfig().getInt("gui.rows", 6);
        Inventory gui = Bukkit.createInventory(null, rows * 9, "§8QeiwtPet Manager");

        ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = filler.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(" ");
            filler.setItemMeta(meta);
        }

        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, filler);
        }

        player.openInventory(gui);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
    }

    public void spawnPet(Player player) {
        UUID uuid = player.getUniqueId();
        List<ArmorStand> currentPets = activePetsMap.computeIfAbsent(uuid, k -> new ArrayList<>());

        if (currentPets.size() >= maxActivePets) {
            player.sendMessage("§c[QeiwtPet] Maximum limit reached (" + maxActivePets + " active pets)!");
            return;
        }

        Location loc = player.getLocation();
        ArmorStand pet = (ArmorStand) player.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);

        pet.setVisible(false);
        pet.setGravity(false);
        pet.setSmall(true);
        pet.setCustomName("§d§l" + player.getName() + "'s Pet #" + (currentPets.size() + 1));
        pet.setCustomNameVisible(showHologram);

        if (pet.getEquipment() != null) {
            pet.getEquipment().setHelmet(new ItemStack(Material.DRAGON_HEAD));
        }

        currentPets.add(pet);
        player.sendMessage("§a[QeiwtPet] Pet #" + currentPets.size() + " spawned and bound to you!");
    }

    public void removeAllPetsForPlayer(Player player) {
        List<ArmorStand> pets = activePetsMap.remove(player.getUniqueId());
        if (pets != null) {
            for (ArmorStand pet : pets) {
                if (pet != null && pet.isValid()) {
                    pet.remove();
                }
            }
        }
    }

    public void removeAllPets() {
        for (List<ArmorStand> pets : activePetsMap.values()) {
            for (ArmorStand pet : pets) {
                if (pet != null && pet.isValid()) {
                    pet.remove();
                }
            }
        }
        activePetsMap.clear();
    }

    private void startPetFollowTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, List<ArmorStand>> entry : activePetsMap.entrySet()) {
                    Player player = Bukkit.getPlayer(entry.getKey());
                    List<ArmorStand> pets = entry.getValue();

                    if (player == null || !player.isOnline()) {
                        continue;
                    }

                    Location pLoc = player.getLocation();
                    Vector dir = pLoc.getDirection().setY(0).normalize();
                    Vector side = new Vector(-dir.getZ(), 0, dir.getX());

                    for (int i = 0; i < pets.size(); i++) {
                        ArmorStand pet = pets.get(i);
                        if (pet == null || !pet.isValid()) continue;

                        double sideMultiplier = (i % 2 == 0) ? offsetX : -offsetX;

                        Location targetLoc = pLoc.clone()
                                .add(side.clone().multiply(sideMultiplier))
                                .add(0, offsetY + 1.2, 0)
                                .subtract(dir.clone().multiply(offsetZ * 0.5));

                        targetLoc.setYaw(pLoc.getYaw());
                        pet.teleport(targetLoc);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }
}
