package net.minewest.minewestcore.afkutil.commands;

import net.minewest.minewestcore.afkutil.InactiveManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class GetInactiveCommand implements CommandExecutor {

    private InactiveManager manager;

    public GetInactiveCommand(InactiveManager manager) {
        this.manager = manager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        for (UUID player : manager.getInactivePlayers()) {
            commandSender.sendMessage(ChatColor.GREEN + Bukkit.getOfflinePlayer(player).getName() + " is AFK.");
        }
        return true;
    }
}
