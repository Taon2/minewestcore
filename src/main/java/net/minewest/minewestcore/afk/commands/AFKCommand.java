package net.minewest.minewestcore.afk.commands;

import net.minewest.minewestcore.afk.AFKManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AFKCommand implements CommandExecutor {
    private AFKManager manager;

    public AFKCommand(AFKManager manager) {
        this.manager = manager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        final Player player = (Player) commandSender;

        boolean isAFK = manager.toggleAFK(player.getUniqueId());

        if (isAFK) {
            Bukkit.broadcastMessage(ChatColor.GREEN + commandSender.getName() + " is now AFK.");
        } else {
            Bukkit.broadcastMessage(ChatColor.GREEN + commandSender.getName() + " is no longer AFK.");
        }

        return true;
    }
}
