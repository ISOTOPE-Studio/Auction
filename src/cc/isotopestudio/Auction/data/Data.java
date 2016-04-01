package cc.isotopestudio.Auction.data;

import java.sql.SQLException;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cc.isotopestudio.Auction.Auction;
import cc.isotopestudio.Auction.handler.DataActionType;
import cc.isotopestudio.Auction.handler.DataLocationType;

public class Data {

	public static void storeItem(Player player, ItemStack item, double money, DataActionType type) {
		String playerName = player.getName().toLowerCase();
		String id = item.getType().toString();
		int amount = item.getAmount();
		ItemMeta meta = item.getItemMeta();
		String itemName;
		if (meta.hasDisplayName())
			itemName = meta.getDisplayName();
		else
			itemName = null;
		String lore = meta.getLore().toString();
		lore = lore.substring(1, lore.length() - 1);
		String enchant = meta.getEnchants().toString();
		System.out.print(itemName);
		System.out.print(lore);
		System.out.print(enchant);
		StringBuilder string = new StringBuilder("insert into ");
		if (type.equals(DataActionType.INTOMAIL)) {
			string.append("mail ");

		} else if (type.equals(DataActionType.INTOMARKET)) {
			string.append("market ");
			string.append("values(null, null," + money + ",\"" + playerName + "\", \"" + id + "\"," + amount
					+ ", null, null, null);");
		}
		try {
			System.out.println(string);
			Auction.statement.executeUpdate(string.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ItemStack getItem(int id, DataLocationType type) {
		return null;
	}

}
