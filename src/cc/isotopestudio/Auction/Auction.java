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
			.append("����").append("]").append(ChatColor.GREEN).toString();
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
		getLogger().info("����Vault API");
		if (!setupEconomy()) {
			getLogger().severe(pluginName + "�޷�����!");
			getLogger().severe("Vaultδ��װ��");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		getLogger().info("���������ļ���");

		createFile("config");
		if (SqlManager.connectMySQL(this) == false) {
			getLogger().severe(pluginName + "�޷�����!");
			getLogger().severe("���ݿ��޷����ӣ�");
			this.getPluginLoader().disablePlugin(this);
		}
		if (SqlManager.createTables(this) == false) {
			getLogger().severe(pluginName + "�޷�����!");
			getLogger().severe("���ݿⴴ��ʧ�ܣ�");
			this.getPluginLoader().disablePlugin(this);
		}
		PluginManager pm = this.getServer().getPluginManager();

		// pm.registerEvents(new WorldListener(this), this);

		this.getCommand("auction").setExecutor(new CommandAuction(this));
		this.getCommand("market").setExecutor(new CommandAuction(this));

		getLogger().info(pluginName + "�ɹ�����!");
		getLogger().info(pluginName + "��ISOTOPE Studio����!");
		getLogger().info("http://isotopestudio.cc");
	}

	@Override
	public void onDisable() {
		getLogger().info(pluginName + "�ɹ�ж��!");
	}

	public void onReload() {
		this.reloadConfig();
		getLogger().info(pluginName + "�ɹ�����!");
		getLogger().info(pluginName + "��ISOTOPE Studio����!");
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
