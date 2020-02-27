package net.minewest.minewestcore.bedutil;

import net.minewest.minewestcore.MinewestCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BedSleepManager {

    private static final float REQUIRED_PLAYER_RATIO = 0.5f;

    private Map<UUID, Boolean> requests = new HashMap<UUID, Boolean>();

    public void castVote(UUID player, boolean accept) {
        requests.put(player, accept);
        checkRequired();
    }

    public boolean hasVoted(UUID player) {
        return requests.containsKey(player);
    }

    public void removePlayer(UUID player) {
        requests.remove(player);
        checkRequired();
    }

    public int getRequests() {
        int acceptances = 0;
        for (UUID player : requests.keySet()) {
            if (requests.get(player)) {
                acceptances++;
            }
        }
        return acceptances;
    }

    public void resetRequests() {
        requests.clear();
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

    private void checkRequired() {
        if (getRequests() < getNeededRequests()) {
            return;
        }

        for (World world : Bukkit.getWorlds()) {
            if (!isDay(world)) {
                world.setTime(MinewestCorePlugin.MORNING_START);
            }

            if (isThundering(world)) {
                world.setThundering(false);
                world.setStorm(false);
            }
        }
        Bukkit.broadcastMessage(ChatColor.GOLD + "Requests met!");
        resetRequests();
    }

    public boolean isDay(World world) {
        if (world == null) {
            return false;
        }

        long time = world.getTime();

        return time < MinewestCorePlugin.BED_START || time >= MinewestCorePlugin.BED_END;
    }

    public boolean isThundering(World world) {
        if (world == null) {
            return false;
        }

        boolean hasStorm = world.hasStorm();
        boolean isThundering = world.isThundering();
        return (hasStorm && isThundering);
    }
}
