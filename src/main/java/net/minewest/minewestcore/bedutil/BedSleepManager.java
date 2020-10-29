package net.minewest.minewestcore.bedutil;

import net.minewest.minewestcore.MinewestCorePlugin;
import net.minewest.minewestcore.afkutil.InactiveManager;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BedSleepManager {

    // See https://minecraft.gamepedia.com/Day-night_cycle
    public static final int DAY_LENGTH = 24000;
    // Time when using the `/time set day` command.
    public static final int MORNING_START = 1000;
    // 12542: In clear weather, beds can be used at this point.
    public static final int BED_START = 12542;
    // 23460: In clear weather, beds can no longer be used.
    public static final int BED_END = 23460;

    private static final float REQUIRED_PLAYER_RATIO = 0.5f;
    private static final int MINIMUM_ABSOLUTE_REQUESTS = 1;

    private Map<UUID, Map<String, Boolean>> requests = new HashMap<UUID, Map<String, Boolean>>();
    private Set<UUID> sleepingPlayers = new HashSet<UUID>();

    private BukkitTask morningDisableTask;

    private MinewestCorePlugin plugin;
    private InactiveManager inactiveManager;

    public BedSleepManager(MinewestCorePlugin plugin, InactiveManager inactiveManager) {
        this.plugin = plugin;
        this.inactiveManager = inactiveManager;
    }

    public static boolean isDay(World world) {
        if (world == null) {
            return false;
        }

        long time = world.getTime();

        return time < BED_START || time >= BED_END;
    }

    public static boolean isThundering(World world) {
        if (world == null) {
            return false;
        }

        boolean hasStorm = world.hasStorm();
        boolean isThundering = world.isThundering();
        return (hasStorm && isThundering);
    }

    private static void resetInsomnia(Player player) {
        player.setStatistic(Statistic.TIME_SINCE_REST, 0);
    }

    public static boolean isInValidWorld(Player player) {
        World overworld = Bukkit.getWorld("world");
        return player.getWorld().equals(overworld);
    }

    public int getNeededRequests() {
        int needed = (int) (REQUIRED_PLAYER_RATIO * getValidPlayers());
        return Math.max(needed, MINIMUM_ABSOLUTE_REQUESTS);
    }

    private int getValidPlayers() {
        int validPlayers = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isValidPlayer(player)) {
                validPlayers++;
            }
        }
        return validPlayers;
    }

    private boolean isValidPlayer(Player player) {
        boolean valid = isInValidWorld(player);
        valid &= !isInactive(player);
        return valid;
    }

    public boolean isInactive(Player player) {
        return inactiveManager.isInactive(player.getUniqueId());
    }

    private void autoDisable() {
        long currentTime = Bukkit.getWorld("world").getTime();
        long timeUntilMorning = (DAY_LENGTH + MORNING_START - currentTime) % DAY_LENGTH;

        cancelDisable();

        morningDisableTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
            public void run() {
                resetRequests();
            }
        }, timeUntilMorning, DAY_LENGTH);
    }

    private void cancelDisable() {
        if (morningDisableTask != null) {
            morningDisableTask.cancel();
        }
    }

    public boolean getEnabled() {
        return !sleepingPlayers.isEmpty();
    }

    public void setEnabled(UUID enablingPlayer) {
        if (enablingPlayer != null) {
            sleepingPlayers.add(enablingPlayer);
        }
        if (getEnabled()) {
            autoDisable();
        }
    }

    public boolean castVote(UUID player, World world, boolean accept, Runnable messageRunnable) {
        if (getEnabled()) {
            Map<String, Boolean> map = new HashMap<>();
            map.put(world.getName(), accept);
            requests.put(player, map);
        }
        return checkRequired(messageRunnable, world);
    }

    public boolean hasVoted(UUID player) {
        return requests.containsKey(player);
    }

    private void removePlayers(Collection<UUID> toRemove) {
        for (UUID player : toRemove) {
            requests.remove(player);
            sleepingPlayers.remove(player);
        }
    }

    public void updatePlayers() {
        Set<UUID> toRemove = new HashSet<UUID>();
        for (UUID player : requests.keySet()) {
            if (!Bukkit.getOfflinePlayer(player).isOnline()) {
                toRemove.add(player);
            }
        }
        removePlayers(toRemove);

        for (World world : Bukkit.getWorlds()) {
            checkRequired(null, world);
        }
    }

    private void resetRequests() {
        sleepingPlayers.clear();
        requests.clear();
    }

    public int getRequests(World world) {
        int acceptances = 0;
        for (UUID player : requests.keySet()) {
            if (requests.get(player).get(world.getName())) {
                acceptances++;
            } else {
                acceptances--;
            }
        }
        return acceptances;
    }

    private boolean checkRequired(Runnable messageRunnable, World world) {
        if (messageRunnable != null) {
            messageRunnable.run();
        }

        if (!getEnabled() || getRequests(world) < getNeededRequests()) {
            return false;
        }

        if (world == null) return false;

        if (!isDay(world)) {
            world.setTime(MORNING_START);
        }

        if (isThundering(world)) {
            world.setThundering(false);
            world.setStorm(false);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isValidPlayer(player)) {
                resetInsomnia(player);
            }
        }

        resetRequests();
        cancelDisable();
        return true;
    }
}
