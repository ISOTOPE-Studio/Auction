package cc.isotopestudio.Auction.utli;

import org.bukkit.ChatColor;

import cc.isotopestudio.Auction.Auction;

public class S {

	public static String toRed(String s) {
		return String.valueOf(ChatColor.RED) + s;
	}

	public static String toGreen(String s) {
		return String.valueOf(ChatColor.GREEN) + s;
	}

	public static String toYellow(String s) {
		return String.valueOf(ChatColor.YELLOW) + s;
	}

	public static String toAqua(String s) {
		return String.valueOf(ChatColor.AQUA) + s;
	}

	public static String toGray(String s) {
		return String.valueOf(ChatColor.GRAY) + s;
	}

	public static String toGold(String s) {
		return String.valueOf(ChatColor.GOLD) + s;
	}

	public static String toBoldGreen(String s) {
		return String.valueOf(ChatColor.GREEN) + ChatColor.BOLD + s;
	}

	public static String toBoldDarkGreen(String s) {
		return String.valueOf(ChatColor.DARK_GREEN) + ChatColor.BOLD + s;
	}

	public static String toBoldDarkAqua(String s) {
		return String.valueOf(ChatColor.DARK_AQUA) + ChatColor.BOLD + s;
	}

	public static String toBoldPurple(String s) {
		return String.valueOf(ChatColor.LIGHT_PURPLE) + ChatColor.BOLD + s;
	}

	public static String toBoldGold(String s) {
		return String.valueOf(ChatColor.GOLD) + ChatColor.BOLD + s;
	}

	public static String toPrefixRed(String s) {
		return Auction.prefix + ChatColor.RED + s;
	}

	public static String toPrefixGreen(String s) {
		return Auction.prefix + ChatColor.GREEN + s;
	}

	public static String toPrefixYellow(String s) {
		return Auction.prefix + ChatColor.YELLOW + s;
	}

	public static String toPrefixGray(String s) {
		return Auction.prefix + ChatColor.GRAY + s;
	}
}
