package cc.isotopestudio.Auction.data;

import java.util.ArrayList;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cc.isotopestudio.Auction.Auction;
import cc.isotopestudio.Auction.sql.SerializeItemStack;
import cc.isotopestudio.Auction.utli.DataLocationType;
import cc.isotopestudio.Auction.utli.S;

public class Data {

	public final static String moneyName = "!-!money!-!";

	public static void storeItemIntoMail(String playerName, ItemStack item) {
		StringBuilder string = new StringBuilder("insert into ");
		string.append("mail ");
		string.append("values(null," + storeItemPre(playerName, item) + ", null);");
		try {
			Auction.statement.executeUpdate(string.toString());
		} catch (SQLException e) {

		}
	}

	public static void storeMoneyIntoMail(String playerName, double money) {
		ItemStack paper = new ItemStack(Material.PAPER);
		ItemMeta meta = paper.getItemMeta();
		meta.setDisplayName(moneyName);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(S.toGold("����۵���Ʒ�ѱ�����!"));
		lore.add(S.toAqua("���:   ") + S.toGreen("" + money));
		meta.setLore(lore);
		paper.setItemMeta(meta);
		StringBuilder string = new StringBuilder("insert into ");
		string.append("mail ");
		string.append("values(null," + storeItemPre(playerName, paper) + "," + money + ");");
		try {
			Auction.statement.executeUpdate(string.toString());
		} catch (SQLException e) {

		}
	}

	public static void storeItemIntoMarket(String playerName, ItemStack item, double money) {
		StringBuilder string = new StringBuilder("insert into ");
		string.append("market ");
		string.append("values(null, null," + money + "," + storeItemPre(playerName, item) + ");");
		try {
			Auction.statement.executeUpdate(string.toString());
		} catch (SQLException e) {

		}
	}

	public static void storeMsg(String playerName, String msg) {
		StringBuilder string = new StringBuilder("insert into ");
		string.append("msg ");
		string.append("values(null,\"" + playerName + "\", \"" + msg + "\");");
		try {
			Auction.statement.executeUpdate(string.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getMsg(String PlayerName) {
		ResultSet res = null;
		ArrayList<String> result;
		try {
			res = Auction.statement.executeQuery("select * from msg where player=\"" + PlayerName + "\";");
			result = new ArrayList<String>();
			while (res.next()) {
				result.add(res.getString("msg"));
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void removeMsg(String string) {
		try {
			Auction.statement.executeUpdate("delete from msg where player=\"" + string + "\";");
		} catch (SQLException e) {

		}
	}

	public static void removeItem(int id, DataLocationType type) {
		try {
			if (type.equals(DataLocationType.MAIL)) {
				Auction.statement.executeUpdate("delete from mail where id=" + id + ";");
			} else if (type.equals(DataLocationType.MARKET)) {
				Auction.statement.executeUpdate("delete from market where id=" + id + ";");
			}
		} catch (SQLException e) {

		}
	}

	private static String storeItemPre(String playerName, ItemStack item) {
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

		}
		return null;
	}

	public static String getOwner(int id, DataLocationType type) {
		ResultSet res = null;
		try {
			if (type.equals(DataLocationType.MAIL)) {
				res = Auction.statement.executeQuery("select * from mail where id=" + id + ";");
			} else if (type.equals(DataLocationType.MARKET)) {
				res = Auction.statement.executeQuery("select * from market where id=" + id + ";");
			}
			if (!res.next())
				return "";
			return res.getString("owner");
		} catch (SQLException e) {

		}
		return "";
	}

	public static double getMarketPrice(int id) {
		ResultSet res = null;
		try {
			res = Auction.statement.executeQuery("select * from market where id=" + id + ";");
			if (!res.next())
				return -1;
			return res.getDouble("money");
		} catch (SQLException e) {

		}
		return -1;
	}

	public static double getMailMoney(int id) {
		ResultSet res = null;
		try {
			res = Auction.statement.executeQuery("select * from mail where id=" + id + ";");
			if (!res.next())
				return -1;
			return res.getDouble("money");
		} catch (SQLException e) {

		}
		return -1;
	}

	public static Date getMarketDate(int id) {
		ResultSet res = null;
		try {
			res = Auction.statement.executeQuery("select * from market where id=" + id + ";");
			if (!res.next())
				return null;
			return (Date) res.getTimestamp("time");
		} catch (SQLException e) {

		}
		return null;
	}

	public static String getMarketRemainDate(int id) {
		Date time = getMarketDate(id);
		Date now = new Date();
		time.setTime(time.getTime() + 7 * 24 * 60 * 60 * 1000);
		long day = 0;
		long hour = 0;
		long min = 0;
		long time1 = time.getTime();
		long time2 = now.getTime();
		long diff;
		if (time1 < time2) {
			return "timeout";
		} else {
			diff = time1 - time2;
		}
		day = diff / (24 * 60 * 60 * 1000);
		hour = (diff / (60 * 60 * 1000) - day * 24);
		min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		return day + "��" + hour + "Сʱ" + min + "��";
	}

	public static ResultSet getPlayerItemList(Player player, DataLocationType type) {
		ResultSet res = null;
		try {
			if (type.equals(DataLocationType.MAIL)) {
				res = Auction.statement.executeQuery("select * from mail where owner=\"" + player.getName() + "\";");
			} else if (type.equals(DataLocationType.MARKET)) {
				res = Auction.statement.executeQuery("select * from market where owner=\"" + player.getName() + "\";");
			}
			return res;
		} catch (SQLException e) {

		}
		return null;
	}

	public static int getItemSizeID(DataLocationType type) {
		ResultSet res = null;
		try {
			if (type.equals(DataLocationType.MAIL)) {
				res = Auction.statement.executeQuery("select * from mail;");
			} else if (type.equals(DataLocationType.MARKET)) {
				res = Auction.statement.executeQuery("select * from market;");
			}
			if (res.wasNull()) {
				return -1;
			}
			res.last();
			return res.getInt("id");
		} catch (SQLException e) {

		}
		return 0;
	}

	public static int getItemSizeID(DataLocationType type, Player player) {
		ResultSet res = null;
		try {
			if (type.equals(DataLocationType.MAIL)) {
				res = Auction.statement.executeQuery("select * from mail where owner=\"" + player.getName() + "\";");
			} else if (type.equals(DataLocationType.MARKET)) {
				res = Auction.statement.executeQuery("select * from market where owner=\"" + player.getName() + "\";");
			}
			res.last();
			return res.getInt("id");
		} catch (SQLException e) {

		}
		return 0;
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

		}
		return 0;
	}

	public static int getItemSize(DataLocationType type, Player player) {
		ResultSet res = null;
		try {
			if (type.equals(DataLocationType.MAIL)) {
				res = Auction.statement.executeQuery("select * from mail where owner=\"" + player.getName() + "\";");
			} else if (type.equals(DataLocationType.MARKET)) {
				res = Auction.statement.executeQuery("select * from market where owner=\"" + player.getName() + "\";");
			}
			res.last();
			return res.getRow();
		} catch (SQLException e) {

		}
		return 0;
	}

	public static int getMarketRowID(int count) {
		ResultSet res = null;
		try {
			res = Auction.statement.executeQuery("select * from market;");
			for (int i = 0; i < count; i++)
				res.next();
			return res.getInt("id");
		} catch (SQLException e) {

		}
		return 0;
	}

	public static int getRowID(DataLocationType type, Player player, int count) {
		ResultSet res = null;
		try {
			if (type.equals(DataLocationType.MAIL)) {
				res = Auction.statement.executeQuery("select * from mail where owner=\"" + player.getName() + "\";");
			} else if (type.equals(DataLocationType.MARKET)) {
				res = Auction.statement.executeQuery("select * from market where owner=\"" + player.getName() + "\";");
			}
			for (int i = 0; i < count; i++)
				res.next();
			return res.getInt("id");
		} catch (SQLException e) {

		}
		return 0;
	}

}
