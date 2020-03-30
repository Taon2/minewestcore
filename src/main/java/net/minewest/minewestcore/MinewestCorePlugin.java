package net.minewest.minewestcore;

import net.minewest.minewestcore.afkutil.InactiveManager;
import net.minewest.minewestcore.bedutil.BedSleepManager;
import net.minewest.minewestcore.bedutil.commands.SleepCommand;
import net.minewest.minewestcore.bedutil.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MinewestCorePlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        InactiveManager inactiveManager = new InactiveManager();

        this.getCommand("afk").setExecutor(inactiveManager.getSetInactiveCommand());
        this.getCommand("getafk").setExecutor(inactiveManager.getGetInactiveCommand());

        BedSleepManager sleepManager = new BedSleepManager(this, inactiveManager);

        this.getCommand("sleep").setExecutor(new SleepCommand(sleepManager));
        Bukkit.getPluginManager().registerEvents(new PlayerListener(sleepManager), this);
    }

    @Override
    public void onDisable() {
    }
}
