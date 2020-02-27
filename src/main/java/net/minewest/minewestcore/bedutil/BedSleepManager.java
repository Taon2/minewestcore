package net.minewest.minewestcore.bedutil;

import net.minewest.minewestcore.MinewestCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;

public class BedSleepManager {

    private static final float REQUIRED_PLAYER_RATIO = 0.5f;
    private static final int MINIMUM_ABSOLUTE_REQUESTS = 1;

    // See https://minecraft.gamepedia.com/Day-night_cycle
    public static final int DAY_LENGTH = 24000;

    // Time when using the `/time set day` command.
    public static final int MORNING_START = 1000;

    // 12542: In clear weather, beds can be used at this point.
    public static final int BED_START = 12542;

    // 23460: In clear weather, beds can no longer be used.
    public static final int BED_END = 23460;

    private Map<UUID, Boolean> requests = new HashMap<UUID, Boolean>();
    private Set<UUID> sleepingPlayers = new HashSet<UUID>();

    private BukkitTask morningDisableTask;

    public void setEnabled(UUID enablingPlayer) {
        sleepingPlayers.add(enablingPlayer);
        if (getEnabled()) {
            autoDisable();
        }
    }

    private void autoDisable() {
        long currentTime = Bukkit.getWorld("world").getTime();
        long timeUntilMorning = (DAY_LENGTH + MORNING_START - currentTime) % DAY_LENGTH;

        if (morningDisableTask != null) {
            morningDisableTask.cancel();
        }

        morningDisableTask = Bukkit.getScheduler().runTaskTimerAsynchronously(MinewestCorePlugin.getInstance(), new Runnable() {
            public void run() {
                resetRequests();
            }
        }, timeUntilMorning, DAY_LENGTH);
    }

    private boolean getEnabled() {
        return !sleepingPlayers.isEmpty();
    }

    public void castVote(UUID player, boolean accept) {
        if (getEnabled()) {
            requests.put(player, accept);
            checkRequired();
        }
    }

    public boolean hasVoted(UUID player) {
        return requests.containsKey(player);
    }

    public void updatePlayers() {
        for (UUID player : requests.keySet()) {
            if (!Bukkit.getOnlinePlayers().contains(player)) {
                requests.remove(player);
                sleepingPlayers.remove(player);
            }
        }
        checkRequired();
    }

    private void resetRequests() {
        sleepingPlayers.clear();
        requests.clear();
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

    public int getNeededRequests() {
        int needed = (int) (REQUIRED_PLAYER_RATIO * getValidPlayers());
        return Math.max(needed, MINIMUM_ABSOLUTE_REQUESTS);
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
        if (!getEnabled() || getRequests() < getNeededRequests()) {
            return;
        }


        for (World world : Bukkit.getWorlds()) {
            if (!isDay(world)) {
                world.setTime(MORNING_START);
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

        return time < BED_START || time >= BED_END;
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
