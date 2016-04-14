package cc.isotopestudio.Auction.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cc.isotopestudio.Auction.Auction;
import cc.isotopestudio.Auction.data.Data;
import cc.isotopestudio.Auction.gui.MailGUI;
import cc.isotopestudio.Auction.gui.MarketGUI;
import cc.isotopestudio.Auction.gui.ShelfGUI;
import cc.isotopestudio.Auction.handler.DataLocationType;

public class CommandAuction implements CommandExecutor {
	private final Auction plugin;

	public CommandAuction(Auction plugin) {
		this.plugin = plugin;
	}

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
				if (args[0].equals("test")) {
					if (args[1].equals("1"))
						player.sendMessage(
								Integer.parseInt(args[2]) + ": " + Data.getMarketRowID(Integer.parseInt(args[2])));
					if (args[1].equals("item"))
						player.sendMessage(Data.getItem(Integer.parseInt(args[2]), DataLocationType.MARKET).toString());
					if (args[1].equals("money"))
						player.sendMessage("" + Data.getMarketPrice(Integer.parseInt(args[2])));
					if (args[1].equals("owner"))
						player.sendMessage(Data.getOwner(Integer.parseInt(args[2]), DataLocationType.MARKET));
					if (args[1].equals("remains"))
						player.sendMessage(Data.getMarketRemainDate(Integer.parseInt(args[2])));
					return true;
				}
				if (args[0].equalsIgnoreCase("market")) {
					(new MarketGUI(0, plugin)).open(player);
					return true;
				}
				if (args[0].equalsIgnoreCase("mail")) {
					(new MailGUI(player, 0, plugin)).open(player);
					return true;
				}
				if (args[0].equalsIgnoreCase("shelf")) {
					(new ShelfGUI(player, 0, plugin)).open(player);
					return true;
				}
				return true;
			} else {
				player.sendMessage(new StringBuilder(Auction.prefix).append(ChatColor.GREEN).append("帮助菜单").toString());
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
