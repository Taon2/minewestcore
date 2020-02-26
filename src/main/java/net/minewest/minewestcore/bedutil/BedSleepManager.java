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

        return (int) (REQUIRED_PLAYER_RATIO * getValidPlayers());
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
            world.setTime(1000);
            requests = 0;
        }
        Bukkit.broadcastMessage(ChatColor.GOLD + "Requests met!");

        SleepCommand.clearPlayers();
        resetSleepRequests();
    }

    public boolean isDay(World world) {
        if (world == null) return false;

        long time = world.getTime();

        return time < 12541 || time > 23458;
    }
}
