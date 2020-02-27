package net.minewest.minewestcore.bedutil;

import net.minewest.minewestcore.bedutil.commands.SleepCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BedSleepManager {

    private static final float REQUIRED_PLAYER_RATIO = 0.5f;

    private int requests = 0;

    public void increaseSleepRequest() {
        requests++;
    }

    public void decreaseSleepRequest() {
        requests--;
    }

    public void resetSleepRequests() {
        requests = 0;
    }

    public int getRequests() {
        return requests;
    }

    public int getNeededRequests() {

        if (Bukkit.getOnlinePlayers().size() == 1) {
            return 1;
        }

        int needed = (int) (REQUIRED_PLAYER_RATIO * getValidPlayers());

        if (needed == 0) {
            return 1;
        }

        return needed;
    }

    private int getValidPlayers() {
        int validPlayers = 0;

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (isValidPlayer(p)) {
                validPlayers++;
            }
        }
        return validPlayers;
    }

    public boolean isValidPlayer(Player p) {
        World overworld = Bukkit.getWorld("world");
        return p.getWorld().equals(overworld);
    }

    public void checkRequired() {
        if (requests < getNeededRequests()) return;

        for (World world : Bukkit.getWorlds()) {
            if (!isDay(world)) {
                world.setTime(MinewestCorePlugin.MORNING_START);
            }

            if (isThundering(world)) {
                world.setThundering(false);
                world.setStorm(false);
            }
        }
        requests = 0;
        Bukkit.broadcastMessage(ChatColor.GOLD + "Requests met!");

        SleepCommand.clearPlayers();
        resetSleepRequests();
    }

    public boolean isDay(World world) {
        if (world == null) return false;

        long time = world.getTime();

        return time < MinewestCorePlugin.BED_START || time >= MinewestCorePlugin.BED_END;
    }

    public boolean isThundering(World world) {
        if (world == null) return false;

        boolean hasStorm = world.hasStorm();
        boolean isThundering = world.isThundering();
        return (hasStorm && isThundering);
    }
}
