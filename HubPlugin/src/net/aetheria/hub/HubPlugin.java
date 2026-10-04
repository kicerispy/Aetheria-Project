package net.aetheria.hub;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

/**
 * Aetheria hub helper:
 *  - teleports players to the hub spawn on join
 *  - rescues players who fall into the void
 *  - gives a locked "server selector" compass that runs /servers
 */
public class HubPlugin extends JavaPlugin implements Listener {

    // ---- Change these if needed ----
    private static final String HUB_WORLD = "aetheria_hub";
    private static final double VOID_Y = 60.0;   // below this Y, players are rescued
    private static final int COMPASS_SLOT = 4;   // 0-8, slot 4 = 5th hotbar slot
    private static final String MENU_COMMAND = "servers";
    // --------------------------------

    private NamespacedKey compassKey;

    @Override
    public void onEnable() {
        compassKey = new NamespacedKey(this, "server_selector");
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("AetheriaHub enabled.");
    }

    private Location spawn() {
        World world = Bukkit.getWorld(HUB_WORLD);
        if (world == null) {
            return null;
        }
        return world.getSpawnLocation().add(0.5, 0.0, 0.5);
    }

    @SuppressWarnings("deprecation")
    private ItemStack makeCompass() {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("\u00a7b\u00a7lServer Selector \u00a77(Right Click)");
        meta.setLore(List.of("\u00a77Choose where to play."));
        meta.getPersistentDataContainer().set(compassKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }

    private boolean isCompass(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        return item.getItemMeta().getPersistentDataContainer().has(compassKey, PersistentDataType.BYTE);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Location spawn = spawn();
        if (spawn != null) {
            player.teleport(spawn);
        }
        player.getInventory().setItem(COMPASS_SLOT, makeCompass());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location to = event.getTo();
        if (to == null || to.getWorld() == null) {
            return;
        }
        if (!to.getWorld().getName().equals(HUB_WORLD) || to.getY() >= VOID_Y) {
            return;
        }
        Location spawn = spawn();
        if (spawn != null) {
            Player player = event.getPlayer();
            player.setFallDistance(0f);
            player.setVelocity(new Vector(0, 0, 0));
            player.teleport(spawn);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (!isCompass(event.getItem())) {
            return;
        }
        event.setCancelled(true);
        event.getPlayer().performCommand(MENU_COMMAND);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (isCompass(event.getItemDrop().getItemStack())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (isCompass(event.getCurrentItem()) || isCompass(event.getCursor())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        if (isCompass(event.getMainHandItem()) || isCompass(event.getOffHandItem())) {
            event.setCancelled(true);
        }
    }
}
