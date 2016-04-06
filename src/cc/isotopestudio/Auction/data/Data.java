package cc.isotopestudio.Auction.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import cc.isotopestudio.Auction.Auction;
import cc.isotopestudio.Auction.handler.DataLocationType;
import cc.isotopestudio.Auction.sql.SerializeItemStack;

public class Data {

	public static void storeItemIntoMail(Player player, ItemStack item) {
		StringBuilder string = new StringBuilder("insert into ");
		string.append("mail ");
		string.append("values(null," + storeItemPre(player, item) + ");");
		try {
			// System.out.println(string);
			Auction.statement.executeUpdate(string.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void storeItemIntoMarket(Player player, ItemStack item, double money) {
		StringBuilder string = new StringBuilder("insert into ");
		string.append("market ");
		string.append("values(null, null," + money + "," + storeItemPre(player, item) + ");");
		try {
			// System.out.println(string);
			Auction.statement.executeUpdate(string.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static String storeItemPre(Player player, ItemStack item) {
		String playerName = player.getName();
		String itemString = SerializeItemStack.itemToStringBlob(item).replace("\\", "\\\\").replace("\"", "\\\"");
		String string = "\"" + playerName + "\", \"" + itemString + "\"";
		return string;
	}

	public static ItemStack getItem(int id, DataLocationType type) {
		ResultSet res = null;
		try {
			if (type.equals(DataLocationType.MAIL)) {
				res = Auction.statement.executeQuery("select * from mail where id=" + id + ";");
			} else if (type.equals(DataLocationType.MARKET)) {
				res = Auction.statement.executeQuery("select * from market where id=" + id + ";");
			}
			if (!res.next())
				return null;
			String itemString = res.getString("item");
			return SerializeItemStack.stringBlobToItem(itemString);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getItemSize(DataLocationType type) {
		ResultSet res = null;
		try {
			if (type.equals(DataLocationType.MAIL)) {
				res = Auction.statement.executeQuery("select * from mail;");
			} else if (type.equals(DataLocationType.MARKET)) {
				res = Auction.statement.executeQuery("select * from market;");
			}
			res.last();
			return res.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	

}
