package net.minewest.minewestcore.afk.commands;

import net.minewest.minewestcore.afk.AFKManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class GetAFKCommand implements CommandExecutor {

    private AFKManager manager;

    public GetAFKCommand(AFKManager manager) {
        this.manager = manager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        for (UUID player : manager.getAFKPlayers()) {
            commandSender.sendMessage(ChatColor.GREEN + Bukkit.getOfflinePlayer(player).getName() + " is AFK.");
        }
        return true;
    }
}
