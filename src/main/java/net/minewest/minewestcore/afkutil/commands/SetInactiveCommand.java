package net.minewest.minewestcore.afkutil.commands;

import net.minewest.minewestcore.afkutil.InactiveManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetInactiveCommand implements CommandExecutor {
    private InactiveManager manager;

    public SetInactiveCommand(InactiveManager manager) {
        this.manager = manager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        final Player player = (Player) commandSender;

        boolean isInactive = manager.toggleInactive(player.getUniqueId());

        if (isInactive) {
            Bukkit.broadcastMessage(ChatColor.GREEN + commandSender.getName() + " is now AFK.");
        } else {
            Bukkit.broadcastMessage(ChatColor.GREEN + commandSender.getName() + " is no longer AFK.");
        }

        return true;
    }
}
