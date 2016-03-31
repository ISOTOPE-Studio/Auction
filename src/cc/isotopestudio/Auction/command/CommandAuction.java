package cc.isotopestudio.Auction.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cc.isotopestudio.Auction.Auction;

public class CommandAuction implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("auction")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(
						new StringBuilder(Auction.prefix).append(ChatColor.RED).append("����Ҫ��Ҳ���ִ��").toString());
				return true;
			}
			Player player = (Player) sender;
			if (args.length > 0 && !args[0].equalsIgnoreCase("help")) {
				
				return true;
			} else {
				player.sendMessage(
						new StringBuilder(Auction.prefix).append(ChatColor.GREEN).append("�����˵�").toString());
				sender.sendMessage(
						new StringBuilder().append(ChatColor.GREEN).append("/auction market �鿴������").toString());
				sender.sendMessage(
						new StringBuilder().append(ChatColor.GREEN).append("/auction mail �鿴�������").toString());
				sender.sendMessage(
						new StringBuilder().append(ChatColor.GREEN).append("/auction shelf �鿴����ϼܵ���Ʒ").toString());
				return true;
			}
		}
		return false;
	}

}
