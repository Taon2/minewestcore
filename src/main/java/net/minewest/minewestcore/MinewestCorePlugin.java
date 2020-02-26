package net.minewest.minewestcore;

import net.minewest.minewestcore.bedutil.BedSleepManager;
import net.minewest.minewestcore.bedutil.commands.SleepCommand;
import net.minewest.minewestcore.bedutil.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MinewestCorePlugin extends JavaPlugin {

    private static MinewestCorePlugin instance;
    private BedSleepManager manager;

    @Override
    public void onEnable() {
        instance = this;

        manager = new BedSleepManager();

        this.getCommand("sleep").setExecutor(new SleepCommand(manager));

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        // Every 30 seconds, if it is day, reset the bed information
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            public void run() {
                if (manager.isDay(getServer().getWorld("world"))) {
                    SleepCommand.clearPlayers();
                    manager.resetSleepRequests();
                }
            }
        }, 0, 600);
    }

    public static MinewestCorePlugin getInstance() {
        return instance;
    }

    public BedSleepManager getBedSleepManager() {
        return manager;
    }

    @Override
    public void onDisable() {
    }
}
