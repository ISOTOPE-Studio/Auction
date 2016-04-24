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
				sender.sendMessage(S.toPrefixRed("����Ҫ��Ҳ���ִ��"));
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
								"���Ѿ��ϼ���" + Data.getItemSize(DataLocationType.MARKET, player) + "����Ʒ�������ٶ���"));
						return true;
					}
					if (item.getType().equals(Material.AIR)) {
						player.sendMessage(S.toPrefixRed("����û����Ʒ"));
						return true;
					}
					try {
						for (String lore : item.getItemMeta().getLore()) {
							if (lore.contains("��")) {
								player.sendMessage(S.toPrefixRed("�޷��ϼܰ󶨵ĵ���"));
								return true;
							}
						}
					} catch (Exception ex) {
					}
					if (price <= 0 || price > 500000) {
						player.sendMessage(S.toPrefixRed("�ⲻ����Ч������������ (0~500000)"));
						return true;
					}
					player.setItemInHand(null);
					Data.storeItemIntoMarket(player.getName(), item, price);
					player.sendMessage(
							S.toPrefixGreen("�ɹ��ϼ�, " + "�����Ѿ��ϼ���" + Data.getItemSize(DataLocationType.MARKET, player))
									+ "����Ʒ");
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
		player.sendMessage(S.toPrefixGreen("�����˵�"));
		player.sendMessage(S.toBoldGreen("/" + label + " <�۸�> �ϼ���Ʒ"));
		player.sendMessage(S.toBoldGreen("/" + label + " market �鿴������"));
		player.sendMessage(S.toGold("�����ܹ���(˫��)��������������Ʒ"));
		player.sendMessage(S.toBoldGreen("/" + label + " mail �鿴�������"));
		player.sendMessage(S.toGold("����鿴����ʼ�"));
		player.sendMessage(S.toBoldGreen("/" + label + " shelf �鿴����ϼܵ���Ʒ"));
		player.sendMessage(S.toGold("�������ϼ�/�¼�����������Ʒ"));
	}

}
