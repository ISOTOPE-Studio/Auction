package cc.isotopestudio.Auction.sql;

import java.sql.*;

import cc.isotopestudio.Auction.Auction;

public class SqlManager {

	public static boolean connectMySQL(Auction plugin) {
		String host = plugin.getConfig().getString("mySQL.host");
		String port = plugin.getConfig().getString("mySQL.port");
		String user = plugin.getConfig().getString("mySQL.user");
		String pw = plugin.getConfig().getString("mySQL.password");
		String db = plugin.getConfig().getString("mySQL.database");
		Auction.MySQL = new MySQL(host, port, db, user, pw);
		Auction.c = null;
		try {
			Auction.c = Auction.MySQL.openConnection();
		} catch (ClassNotFoundException e) {
			plugin.getLogger().info(Auction.pluginName + "数据库出错 Error1");
			return false;
		} catch (SQLException e) {
			plugin.getLogger().info(Auction.pluginName + "数据库出错 Error2");
			return false;
		}		
		Statement statement = null;
		try {
			statement = Auction.c.createStatement();
		} catch (SQLException e1) {
			plugin.getLogger().info(Auction.pluginName + "数据库出错 Error3");
			return false;
		}
		// PlayerData.statement = statement;
		
		return true;
	}

}
