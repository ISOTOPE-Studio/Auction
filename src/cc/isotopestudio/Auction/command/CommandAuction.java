package cc.isotopestudio.Auction.command;

import org.bukkit.Material;
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
	public static Auction plugin;

	public CommandAuction(Auction plugin) {
		CommandAuction.plugin = plugin;
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
					(new MarketGUI(player, 0, plugin)).open(player);
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
				try {
					double price = Double.parseDouble(args[0]);
					ItemStack item = player.getItemInHand().clone();
					if (Data.getItemSize(DataLocationType.MARKET, player) >= 20) {
						player.sendMessage(S.toPrefixRed(
								"你已经上架了" + Data.getItemSize(DataLocationType.MARKET, player) + "个物品，不能再多了"));
						return true;
					}
					if (item.getType().equals(Material.AIR)) {
						player.sendMessage(S.toPrefixRed("手中没有物品"));
						return true;
					}
					try {
						for (String lore : item.getItemMeta().getLore()) {
							if (lore.contains("绑定")) {
								player.sendMessage(S.toPrefixRed("无法上架绑定的道具"));
								return true;
							}
						}
					} catch (Exception ex) {
					}
					if (price <= 0 || price > 500000) {
						player.sendMessage(S.toPrefixRed("这不是有效的数字请再试 (0~500000)"));
						return true;
					}
					player.setItemInHand(null);
					Data.storeItemIntoMarket(player.getName(), item, price);
					player.sendMessage(
							S.toPrefixGreen("成功上架, " + "现在已经上架了" + Data.getItemSize(DataLocationType.MARKET, player))
									+ "个物品");
					return true;
				} catch (Exception e) {
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
		player.sendMessage(S.toBoldGreen("/" + label + " <价格> 上架商品"));
		player.sendMessage(S.toBoldGreen("/" + label + " market 查看拍卖行"));
		player.sendMessage(S.toGold("这里能购买(双击)所有人拍卖的物品"));
		player.sendMessage(S.toBoldGreen("/" + label + " mail 查看玩家邮箱"));
		player.sendMessage(S.toGold("这里查看你的邮件"));
		player.sendMessage(S.toBoldGreen("/" + label + " shelf 查看玩家上架的商品"));
		player.sendMessage(S.toGold("这里能上架/下架你拍卖的物品"));
	}

}
