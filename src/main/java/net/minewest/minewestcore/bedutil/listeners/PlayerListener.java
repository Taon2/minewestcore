package net.minewest.minewestcore.bedutil.listeners;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minewest.minewestcore.bedutil.BedSleepManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private BedSleepManager manager;

    public PlayerListener(BedSleepManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerSleepEvent(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) {
            return;
        }

        Bukkit.broadcastMessage(ChatColor.GOLD + event.getPlayer().getName() + " wants to sleep.");

        BaseComponent[] c = new ComponentBuilder("- ")
                .color(net.md_5.bungee.api.ChatColor.DARK_GRAY)
                .append("Accept")
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sleep accept"))
                .append(" - ")
                .color(net.md_5.bungee.api.ChatColor.DARK_GRAY)
                .append("Deny")
                .color(net.md_5.bungee.api.ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sleep deny"))
                .append(" -")
                .color(net.md_5.bungee.api.ChatColor.DARK_GRAY)
                .create();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(c);
        }


        manager.setEnabled(event.getPlayer().getUniqueId());
        event.getPlayer().performCommand("sleep accept");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        manager.updatePlayers();
    }
}
