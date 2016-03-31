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
						new StringBuilder(Auction.prefix).append(ChatColor.RED).append("必须要玩家才能执行").toString());
				return true;
			}
			Player player = (Player) sender;
			if (args.length > 0 && !args[0].equalsIgnoreCase("help")) {
				
				return true;
			} else {
				player.sendMessage(
						new StringBuilder(Auction.prefix).append(ChatColor.GREEN).append("帮助菜单").toString());
				sender.sendMessage(
						new StringBuilder().append(ChatColor.GREEN).append("/auction market 查看拍卖行").toString());
				sender.sendMessage(
						new StringBuilder().append(ChatColor.GREEN).append("/auction mail 查看玩家邮箱").toString());
				sender.sendMessage(
						new StringBuilder().append(ChatColor.GREEN).append("/auction shelf 查看玩家上架的商品").toString());
				return true;
			}
		}
		return false;
	}

}
