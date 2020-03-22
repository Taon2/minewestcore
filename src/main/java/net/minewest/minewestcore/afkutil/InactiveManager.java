package net.minewest.minewestcore.afkutil;

import net.minewest.minewestcore.afkutil.commands.GetInactiveCommand;
import net.minewest.minewestcore.afkutil.commands.SetInactiveCommand;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class InactiveManager {

    private HashSet<UUID> inactivePlayers;

    private SetInactiveCommand setInactiveCommand;
    private GetInactiveCommand getInactiveCommand;

    public InactiveManager() {
        inactivePlayers = new HashSet<UUID>();
        setInactiveCommand = new SetInactiveCommand(this);
        getInactiveCommand = new GetInactiveCommand(this);
    }

    /**
     * @param player to toggle AFK status
     * @return player's current AFK status
     */
    public boolean toggleInactive(UUID player) {
        if (inactivePlayers.contains(player)) {
            inactivePlayers.remove(player);
            return false;
        } else {
            inactivePlayers.add(player);
            return true;
        }
    }

    /**
     * @param player to set AFK status
     * @return player was AFK
     */
    public boolean setInactive(UUID player, boolean status) {
        if (inactivePlayers.contains(player)) {
            if (!status) {
                inactivePlayers.remove(player);
            }
            return true;
        } else if (status) {
            inactivePlayers.add(player);
        }
        return false;
    }

    public boolean isInactive(UUID player) {
        return inactivePlayers.contains(player);
    }

    public Collection<UUID> getInactivePlayers() {
        return inactivePlayers;
    }

    public SetInactiveCommand getSetInactiveCommand() {
        return setInactiveCommand;
    }

    public GetInactiveCommand getGetInactiveCommand() {
        return getInactiveCommand;
    }
}
