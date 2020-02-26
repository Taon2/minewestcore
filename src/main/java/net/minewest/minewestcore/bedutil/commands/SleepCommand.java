package net.minewest.minewestcore.bedutil.commands;

import net.minewest.minewestcore.MinewestCorePlugin;
import net.minewest.minewestcore.bedutil.BedSleepManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SleepCommand implements CommandExecutor {

    private BedSleepManager manager;
    private static Set<UUID> players = new HashSet<UUID>();

    public SleepCommand(BedSleepManager manager) {
        this.manager = manager;
    }

    public boolean onCommand(final CommandSender commandSender, Command command, String s, String[] args) {
        final Player player = (Player) commandSender;

        if (args.length != 1) {
            sendUsage(commandSender);
            return false;
        }

        boolean accept = args[0].equals("accept");
        boolean deny = args[0].equals("deny");

        if (!accept && !deny) {
            sendUsage(commandSender);
            return false;
        }

        if (!manager.isValidPlayer(player)) {
            commandSender.sendMessage(ChatColor.RED + "You can are not in an appropriate world!");
            return true;
        }

        if (manager.isDay(player.getWorld())) {
            if (!manager.isThundering(player.getWorld())) {
                commandSender.sendMessage(ChatColor.RED + "You can only do this at night or during a thunderstorm!");
                return true;
            }
        }

        if (players.contains(player.getUniqueId())) {
            commandSender.sendMessage(ChatColor.RED + "You have already selected an option!");
            return true;
        }

        if (accept) {
            manager.increaseSleepRequest();
            Bukkit.broadcastMessage(ChatColor.WHITE + Integer.toString(manager.getRequests()) + "/" +
                    manager.getNeededRequests() + " " + ChatColor.GREEN + commandSender.getName() + " has accepted.");
        } else {
            manager.decreaseSleepRequest();
            Bukkit.broadcastMessage(ChatColor.WHITE + Integer.toString(manager.getRequests()) + "/" +
                    manager.getNeededRequests() + " " + ChatColor.RED + commandSender.getName() + " has denied.");
        }

        players.add(player.getUniqueId());
        manager.checkRequired();

        return true;
    }

    private static void sendUsage(final CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.WHITE + "Usage: " + ChatColor.RED + "/sleep [accept/deny]");
    }

    public static Collection<UUID> getPlayers() {
        return players;
    }

    public static void clearPlayers() {
        players.clear();
    }
}
