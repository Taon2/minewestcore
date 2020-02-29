package net.minewest.minewestcore;

import net.minewest.minewestcore.bedutil.BedSleepManager;
import net.minewest.minewestcore.bedutil.commands.SleepCommand;
import net.minewest.minewestcore.bedutil.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MinewestCorePlugin extends JavaPlugin {

    private BedSleepManager manager;

    @Override
    public void onEnable() {
        manager = new BedSleepManager(this);

        this.getCommand("sleep").setExecutor(new SleepCommand(manager));

        Bukkit.getPluginManager().registerEvents(new PlayerListener(manager), this);
    }

    @Override
    public void onDisable() {
    }
}
