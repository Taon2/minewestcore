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

        manager = new BedSleepManager(this);

        this.getCommand("sleep").setExecutor(new SleepCommand(manager));

        Bukkit.getPluginManager().registerEvents(new PlayerListener(manager), this);
    }

    @Override
    public void onDisable() {
    }
}
