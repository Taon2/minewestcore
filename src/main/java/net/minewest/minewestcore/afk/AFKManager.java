package net.minewest.minewestcore.afk;

import net.minewest.minewestcore.afk.commands.AFKCommand;
import net.minewest.minewestcore.afk.commands.GetAFKCommand;

import java.util.HashSet;
import java.util.UUID;

public class AFKManager {

    private HashSet<UUID> AFKPlayers;

    private AFKCommand AFKCommand;
    private GetAFKCommand getAFKCommand;

    public AFKManager() {
        AFKPlayers = new HashSet<UUID>();
        AFKCommand = new AFKCommand(this);
        getAFKCommand = new GetAFKCommand(this);
    }

    /**
     * @param player to toggle AFK status
     * @return player's current AFK status
     */
    public boolean toggleAFK(UUID player) {
        if (AFKPlayers.contains(player)) {
            AFKPlayers.remove(player);
            return false;
        } else {
            AFKPlayers.add(player);
            return true;
        }
    }

    /**
     * @param player to set AFK status
     * @return player was AFK
     */
    public boolean setAFK(UUID player, boolean status) {
        if (AFKPlayers.contains(player)) {
            if (!status) {
                AFKPlayers.remove(player);
            }
            return true;
        } else if (status) {
            AFKPlayers.add(player);
        }
        return false;
    }

    public boolean isAFK(UUID player) {
        return AFKPlayers.contains(player);
    }

    public HashSet<UUID> getAFKPlayers() {
        return AFKPlayers;
    }

    public AFKCommand getAFKCommand() {
        return AFKCommand;
    }

    public GetAFKCommand getGetAFKCommand() {
        return getAFKCommand;
    }
}
