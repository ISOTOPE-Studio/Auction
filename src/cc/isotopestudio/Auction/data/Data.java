package cc.isotopestudio.Auction.data;

import java.util.Calendar;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
			Auction.statement.executeUpdate(string.toString());
		} catch (SQLException e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			return "超时!";
		} else {
			diff = time1 - time2;
		}
		day = diff / (24 * 60 * 60 * 1000);
		hour = (diff / (60 * 60 * 1000) - day * 24);
		min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
		return day + "天" + hour + "小时" + min + "分";
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
			e.printStackTrace();
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
			res.last();
			return res.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
		return 0;
	}

}
