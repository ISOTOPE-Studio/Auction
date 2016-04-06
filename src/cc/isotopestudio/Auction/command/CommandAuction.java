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
						new StringBuilder(Auction.prefix).append(ChatColor.RED).append("����Ҫ��Ҳ���ִ��").toString());
				return true;
			}
			Player player = (Player) sender;
			if (args.length > 0 && !args[0].equalsIgnoreCase("help")) {
				if (args[0].equals("store")) {
					for (int i = 0; i <= 36; i++) {
						ItemStack item = player.getInventory().getItem(i);
						if (item != null) {
							Data.storeItemIntoMarket(player, item, (int) (Math.random() * 100));
						}
					}
				}
				if (args[0].equalsIgnoreCase("market")) {
					// Data.storeItemIntoMarket(player, player.getItemInHand(),
					// 100);
					(new MarketGUI(0, plugin)).open(player);
					return true;
				}
				if (args[0].equalsIgnoreCase("mail")) {
					// Data.storeItemIntoMail(player, player.getItemInHand());
					(new MailGUI(player, 0, plugin)).open(player);
					return true;
				}
				if (args[0].equalsIgnoreCase("shelf")) {
					(new ShelfGUI(player, 0, plugin)).open(player);
					// player.setItemInHand(Data.getItem(Integer.parseInt(args[1]),
					// DataLocationType.MARKET));
					return true;
				}
				return true;
			} else {
				player.sendMessage(new StringBuilder(Auction.prefix).append(ChatColor.GREEN).append("�����˵�").toString());
				sender.sendMessage(
						new StringBuilder().append(ChatColor.GREEN).append("/auction market �鿴������").toString());
				sender.sendMessage(
						new StringBuilder().append(ChatColor.GREEN).append("/auction mail �鿴�������").toString());
				sender.sendMessage(
						new StringBuilder().append(ChatColor.GREEN).append("/auction shelf �鿴����ϼܵ���Ʒ").toString());
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("market")) {

		}
		return false;
	}

}
