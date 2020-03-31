package net.minewest.minewestcore.afkutil.listeners;

import net.minewest.minewestcore.MinewestCorePlugin;
import net.minewest.minewestcore.afkutil.InactiveManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class InactiveDetectListener implements Listener {

    private static final long TICKS_TO_INACTIVE = 20 * 60 * 5;

    private MinewestCorePlugin plugin;
    private InactiveManager inactiveManager;

    private HashMap<UUID, BukkitTask> setInactiveTasks;


    public InactiveDetectListener(MinewestCorePlugin plugin, InactiveManager inactiveManager) {
        this.plugin = plugin;
        this.setInactiveTasks = new HashMap<>();
        this.inactiveManager = inactiveManager;
    }

    private void register(final Player player) {
        final UUID uuid = player.getUniqueId();

        BukkitTask setInactive = this.setInactiveTasks.get(uuid);
        if (setInactive != null) {
            setInactive.cancel();
        }
        setInactive = Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
            @Override
            public void run() {
                System.out.println("task run");
                if (!InactiveDetectListener.this.inactiveManager.isInactive(uuid)) {
                    player.performCommand("afk");
                }
            }
        }, TICKS_TO_INACTIVE);
        System.out.println("task set");
        this.setInactiveTasks.put(uuid, setInactive);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        register(event.getPlayer());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        setActive(event.getPlayer());
        register(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        setActive(player);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        Bukkit.getScheduler().runTask(this.plugin, new Runnable() {
            @Override
            public void run() {
                setActive(player);
                register(player);
            }
        });
    }

    private void setActive(Player player) {
        if (this.inactiveManager.isInactive(player.getUniqueId())) {
            player.performCommand("afk");
        }

        BukkitTask setInactive = this.setInactiveTasks.remove(player.getUniqueId());
        if (setInactive != null) {
            setInactive.cancel();
        }
    }
}
