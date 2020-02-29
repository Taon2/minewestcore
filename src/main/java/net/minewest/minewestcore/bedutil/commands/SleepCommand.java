package net.minewest.minewestcore.bedutil.commands;

import net.minewest.minewestcore.bedutil.BedSleepManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SleepCommand implements CommandExecutor {

    private BedSleepManager manager;

    public SleepCommand(BedSleepManager manager) {
        this.manager = manager;
        manager.setOnSuccess(new Runnable() {
            public void run() {
                Bukkit.broadcastMessage(ChatColor.GOLD + "Requests met!");
            }
        });
    }

    public boolean onCommand(final CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        final Player player = (Player) commandSender;

        if (args.length != 1) {
            return false;
        }

        boolean accept = args[0].equals("accept");
        boolean deny = args[0].equals("deny");

        if (!accept && !deny) {
            return false;
        }

        if (!BedSleepManager.isValidPlayer(player)) {
            commandSender.sendMessage(ChatColor.RED + "You are not in an appropriate world!");
            return true;
        }

        if (BedSleepManager.isDay(player.getWorld())) {
            if (!BedSleepManager.isThundering(player.getWorld())) {
                commandSender.sendMessage(ChatColor.RED + "You can only do this at night or during a thunderstorm!");
                return true;
            }
        }

        if (!manager.getEnabled()) {
            commandSender.sendMessage(ChatColor.RED + "Nobody has requested to sleep yet!");
            return true;
        }

        if (manager.hasVoted(player.getUniqueId())) {
            commandSender.sendMessage(ChatColor.RED + "You have already selected an option!");
            return true;
        }

        manager.castVote(player.getUniqueId(), accept);
        if (accept) {
            Bukkit.broadcastMessage(ChatColor.WHITE + Integer.toString(manager.getRequests()) + "/" +
                    BedSleepManager.getNeededRequests() + " " + ChatColor.GREEN + commandSender.getName() + " has accepted.");
        } else {
            Bukkit.broadcastMessage(ChatColor.WHITE + Integer.toString(manager.getRequests()) + "/" +
                    BedSleepManager.getNeededRequests() + " " + ChatColor.RED + commandSender.getName() + " has denied.");
        }

        return true;
    }
}
