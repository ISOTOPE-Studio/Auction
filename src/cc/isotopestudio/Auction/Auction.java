package cc.isotopestudio.Auction;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import cc.isotopestudio.Auction.sql.SqlManager;
import net.milkbowl.vault.economy.Economy;
import cc.isotopestudio.Auction.command.CommandAuction;
import cc.isotopestudio.Auction.sql.MySQL;

public class Auction extends JavaPlugin {
	public static final String prefix = (new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("[")
			.append("拍卖").append("]").append(ChatColor.GREEN).toString();
	public static final String pluginName = "Auction";

	// mySQL
	public static MySQL MySQL;
	public static Connection c;
	public static Statement statement;

	// Vault
	public static Economy econ = null;

	public void createFile(String name) {
		File file;
		file = new File(getDataFolder(), name + ".yml");
		if (!file.exists()) {
			saveDefaultConfig();
		}
	}

	@Override
	public void onEnable() {
		getLogger().info("加载Vault API");
		if (!setupEconomy()) {
			getLogger().severe(pluginName + "无法加载!");
			getLogger().severe("Vault未安装！");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		getLogger().info("加载配置文件中");

		createFile("config");
		if (SqlManager.connectMySQL(this) == false) {
			getLogger().severe(pluginName + "无法加载!");
			getLogger().severe("数据库无法连接！");
			this.getPluginLoader().disablePlugin(this);
		}
		if (SqlManager.createTables(this) == false) {
			getLogger().severe(pluginName + "无法加载!");
			getLogger().severe("数据库创建失败！");
			this.getPluginLoader().disablePlugin(this);
		}
		PluginManager pm = this.getServer().getPluginManager();

		// pm.registerEvents(new WorldListener(this), this);

		this.getCommand("auction").setExecutor(new CommandAuction(this));
		this.getCommand("market").setExecutor(new CommandAuction(this));

		getLogger().info(pluginName + "成功加载!");
		getLogger().info(pluginName + "由ISOTOPE Studio制作!");
		getLogger().info("http://isotopestudio.cc");
	}

	@Override
	public void onDisable() {
		getLogger().info(pluginName + "成功卸载!");
	}

	public void onReload() {
		this.reloadConfig();
		getLogger().info(pluginName + "成功重载!");
		getLogger().info(pluginName + "由ISOTOPE Studio制作!");
	}

	// Vault API
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

}
