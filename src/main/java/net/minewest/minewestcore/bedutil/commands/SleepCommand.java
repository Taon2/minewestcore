package net.minewest.minewestcore.bedutil.commands;

import net.minewest.minewestcore.MinewestCorePlugin;
import net.minewest.minewestcore.bedutil.BedSleepManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SleepCommand implements CommandExecutor {

    private BedSleepManager manager;
    private static List<UUID> players = new ArrayList<UUID>();

    public SleepCommand(BedSleepManager manager) {
        this.manager = manager;
    }

    public boolean onCommand(final CommandSender commandSender, Command command, String s, String[] args) {
        final Player player = (Player) commandSender;

        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.WHITE + "Usage: " + ChatColor.RED + "/sleep [accept/deny]");
        }

        boolean hasStorm = player.getWorld().hasStorm();
        boolean isThundering = player.getWorld().isThundering();
        boolean thunderstorm = hasStorm && isThundering;

        if (MinewestCorePlugin.getInstance().getBedSleepManager().day(player.getWorld()))  {
            if (!thunderstorm) {
                commandSender.sendMessage(ChatColor.RED + "You can only do this at night or during a thunderstorm!");
                return true;
            }
        }

        if (players.contains(((Player) commandSender).getUniqueId())) {
            commandSender.sendMessage(ChatColor.RED + "You have already selected an option!");
            return true;
        }

        if (args[0].equals("accept")) {
            manager.increaseSleepRequest();
            Bukkit.broadcastMessage(ChatColor.WHITE + Integer.toString(manager.getRequests()) + "/" +
                    manager.getNeededRequests() + " " + ChatColor.GREEN + commandSender.getName() + " has accepted.");
        } else if (args[0].equals("deny")) {
            manager.decreaseSleepRequest();
            Bukkit.broadcastMessage(ChatColor.WHITE + Integer.toString(manager.getRequests()) + "/" +
                    manager.getNeededRequests() + " " + ChatColor.RED + commandSender.getName() + " has denied.");
        }

        players.add(player.getUniqueId());
        manager.checkRequired();

        return true;
    }

    public static List<UUID> getPlayers() {
        return players;
    }

    public static void clearPlayers() {
        players.clear();
    }
}
