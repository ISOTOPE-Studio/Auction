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
				sender.sendMessage(S.toPrefixRed("����Ҫ��Ҳ���ִ��"));
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
				return true;
			} else {
				player.sendMessage(S.toPrefixGreen("�����˵�"));
				sender.sendMessage(S.toBoldGreen("/" + label + " market �鿴������"));
				sender.sendMessage(S.toBoldGreen("/" + label + " mail �鿴�������"));
				sender.sendMessage(S.toBoldGreen("/" + label + " shelf �鿴����ϼܵ���Ʒ"));
				return true;
			}
		}
		return false;
	}

}
