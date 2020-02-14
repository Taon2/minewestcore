package net.minewest.minewestcore.bedutil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;

public class BedSleepManager {

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

    public int getRequests(){
        return requests;
    }

    public int getNeededRequests(){

        if (Bukkit.getOnlinePlayers().size() == 1) {
            return 1;
        }

        return Bukkit.getOnlinePlayers().size()/2;
    }

    public void checkRequired() {
        if (requests <  Bukkit.getOnlinePlayers().size()/2) return;

        for(World world : Bukkit.getServer().getWorlds()){
            world.setTime(1000);
            requests = 0;
        }
        Bukkit.broadcastMessage(ChatColor.GOLD + "Requests met!");
    }
}
