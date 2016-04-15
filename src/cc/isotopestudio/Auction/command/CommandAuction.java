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
import cc.isotopestudio.Auction.utli.DataLocationType;
import cc.isotopestudio.Auction.utli.S;

public class CommandAuction implements CommandExecutor {
	private final Auction plugin;

	public CommandAuction(Auction plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("auction")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(S.toPrefixRed("必须要玩家才能执行"));
				sendHelp(sender, label);
				return true;
			}
			Player player = (Player) sender;
			if (args.length > 0 && !args[0].equalsIgnoreCase("help")) {
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
				sendHelp(player, label);
				return true;
			} else {
				sendHelp(player, label);
				return true;
			}
		}
		return false;
	}

	void sendHelp(CommandSender player, String label) {
		player.sendMessage(S.toPrefixGreen("帮助菜单"));
		player.sendMessage(S.toBoldGreen("/" + label + " market 查看拍卖行"));
		player.sendMessage(S.toYellow("这里能购买(双击)所有人拍卖的物品"));
		player.sendMessage(S.toBoldGreen("/" + label + " mail 查看玩家邮箱"));
		player.sendMessage(S.toYellow("这里查看你的邮件"));
		player.sendMessage(S.toBoldGreen("/" + label + " shelf 查看玩家上架的商品"));
		player.sendMessage(S.toYellow("这里能上架/下架你拍卖的物品"));
		player.sendMessage(S.toYellow("上架: 把物品放进GUI中，下架: 双击物品"));
	}

}
