package net.minewest.minewestcore.trackingutil.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.Arrays;
import java.util.List;

public class TrackingListener implements Listener {

    private String BROKE_BLOCK_MESSAGE = "%player% broke %material% block at %world% %x% %y% %z%";
    private String BLOCK_INTERACT_MESSAGE = "%player% opened %material% at %world% %x% %y% %z%";
    private String PLAYER_PORTAL_MESSAGE = "%player% portaled to %world% %x% %y% %z%";

    private static List<Material> brokenBlocksToTrack;
    private static List<Material> interactedBlocksToTrack;

    static {
        brokenBlocksToTrack = Arrays.asList(
                Material.CHEST,
                Material.TRAPPED_CHEST,
                Material.BARREL,
                Material.SPAWNER,
                Material.SHULKER_BOX,
                Material.BLACK_SHULKER_BOX,
                Material.BLUE_SHULKER_BOX,
                Material.BROWN_SHULKER_BOX,
                Material.CYAN_SHULKER_BOX,
                Material.GRAY_SHULKER_BOX,
                Material.GREEN_SHULKER_BOX,
                Material.LIGHT_BLUE_SHULKER_BOX,
                Material.LIGHT_GRAY_SHULKER_BOX,
                Material.LIME_SHULKER_BOX,
                Material.MAGENTA_SHULKER_BOX,
                Material.ORANGE_SHULKER_BOX,
                Material.PINK_SHULKER_BOX,
                Material.PURPLE_SHULKER_BOX,
                Material.RED_SHULKER_BOX,
                Material.WHITE_SHULKER_BOX,
                Material.YELLOW_SHULKER_BOX
        );

        interactedBlocksToTrack = Arrays.asList(
                Material.CHEST,
                Material.TRAPPED_CHEST,
                Material.BARREL,
                Material.SHULKER_BOX,
                Material.SHULKER_BOX,
                Material.BLACK_SHULKER_BOX,
                Material.BLUE_SHULKER_BOX,
                Material.BROWN_SHULKER_BOX,
                Material.CYAN_SHULKER_BOX,
                Material.GRAY_SHULKER_BOX,
                Material.GREEN_SHULKER_BOX,
                Material.LIGHT_BLUE_SHULKER_BOX,
                Material.LIGHT_GRAY_SHULKER_BOX,
                Material.LIME_SHULKER_BOX,
                Material.MAGENTA_SHULKER_BOX,
                Material.ORANGE_SHULKER_BOX,
                Material.PINK_SHULKER_BOX,
                Material.PURPLE_SHULKER_BOX,
                Material.RED_SHULKER_BOX,
                Material.WHITE_SHULKER_BOX,
                Material.YELLOW_SHULKER_BOX
        );
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getLocation().getWorld() == null) return;

        if (brokenBlocksToTrack.contains(event.getBlock().getType())) {
            logToConsole(BROKE_BLOCK_MESSAGE
                    .replace("%player%", event.getPlayer().getDisplayName())
                    .replace("%material%", event.getBlock().getType().name())
                    .replace("%world%", event.getBlock().getLocation().getWorld().getName())
                    .replace("%x%", Double.toString(event.getBlock().getLocation().getX()))
                    .replace("%y%", Double.toString(event.getBlock().getLocation().getY()))
                    .replace("%z%", Double.toString(event.getBlock().getLocation().getZ())));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null
                || event.getClickedBlock().getLocation().getWorld() == null
                || !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        if (interactedBlocksToTrack.contains(event.getClickedBlock().getType())) {
            logToConsole(BLOCK_INTERACT_MESSAGE
                    .replace("%player%", event.getPlayer().getDisplayName())
                    .replace("%material%", event.getClickedBlock().getType().name())
                    .replace("%world%", event.getClickedBlock().getLocation().getWorld().getName())
                    .replace("%x%", Double.toString(event.getClickedBlock().getLocation().getX()))
                    .replace("%y%", Double.toString(event.getClickedBlock().getLocation().getY()))
                    .replace("%z%", Double.toString(event.getClickedBlock().getLocation().getZ())));
        }
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        if (event.getTo() == null || event.getTo().getWorld() == null) return;

        logToConsole(PLAYER_PORTAL_MESSAGE
                .replace("%player%", event.getPlayer().getDisplayName())
                .replace("%world%", event.getTo().getWorld().getName())
                .replace("%x%", Double.toString(event.getTo().getX()))
                .replace("%y%", Double.toString(event.getTo().getY()))
                .replace("%z%", Double.toString(event.getTo().getZ())));
    }

    private void logToConsole(String message) {
        Bukkit.getLogger().info(message);
    }
}
