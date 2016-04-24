package cc.isotopestudio.Auction;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import cc.isotopestudio.Auction.sql.SqlManager;
import cc.isotopestudio.Auction.task.ClearOutdatedItem;
import cc.isotopestudio.Auction.task.MailMsg;
import cc.isotopestudio.Auction.command.CommandAuction;
import cc.isotopestudio.Auction.listener.PlayerJoinMsg;
import cc.isotopestudio.Auction.listener.SafeListener;
import cc.isotopestudio.Auction.sql.MySQL;

public class Auction extends JavaPlugin {
	public static final String prefix = (new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("[")
			.append("����").append("]").append(ChatColor.GREEN).toString();
	public static final String pluginName = "Auction 1.0.2";

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

		pm.registerEvents(new PlayerJoinMsg(this), this);
		pm.registerEvents(new SafeListener(), this);
		this.getCommand("auction").setExecutor(new CommandAuction(this));

		(new ClearOutdatedItem()).runTaskTimer(this, 60, 20 * 60);
		(new MailMsg()).runTaskTimer(this, 60, 5 * 20 * 60);
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
