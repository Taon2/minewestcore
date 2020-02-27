package net.minewest.minewestcore;

import net.minewest.minewestcore.bedutil.BedSleepManager;
import net.minewest.minewestcore.bedutil.commands.SleepCommand;
import net.minewest.minewestcore.bedutil.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MinewestCorePlugin extends JavaPlugin {

    private static MinewestCorePlugin instance;
    private BedSleepManager manager;


    // See https://minecraft.gamepedia.com/Day-night_cycle
    public static final int DAY_LENGTH = 24000;

    // Time when using the `/time set day` command.
    public static final int MORNING_START = 1000;

    // 12542: In clear weather, beds can be used at this point.
    public static final int BED_START = 12542;

    // 23460: In clear weather, beds can no longer be used.
    public static final int BED_END = 23460;

    @Override
    public void onEnable() {
        instance = this;

        manager = new BedSleepManager();

        this.getCommand("sleep").setExecutor(new SleepCommand(manager));

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        long currentTime = Bukkit.getWorld("world").getTime();
        long timeUntilMorning = (DAY_LENGTH + MORNING_START - currentTime) % DAY_LENGTH;

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
            public void run() {
                manager.resetRequests();
            }
        }, timeUntilMorning, DAY_LENGTH);
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
