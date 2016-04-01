package cc.isotopestudio.Auction;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import cc.isotopestudio.Auction.sql.SqlManager;
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

	public void createFile(String name) {
		File file;
		file = new File(getDataFolder(), name + ".yml");
		if (!file.exists()) {
			saveDefaultConfig();
		}
	}

	@Override
	public void onEnable() {
		getLogger().info("���������ļ���");

		createFile("config");
		if (SqlManager.connectMySQL(this) == false) {
			getLogger().info(pluginName + "�޷�����!");
			getLogger().info(pluginName + "���ݿ��޷����ӣ�");
			this.getPluginLoader().disablePlugin(this);
		}
		if (SqlManager.createTables(this) == false) {
			getLogger().info(pluginName + "�޷�����!");
			getLogger().info(pluginName + "���ݿⴴ��ʧ�ܣ�");
			this.getPluginLoader().disablePlugin(this);
		}
		PluginManager pm = this.getServer().getPluginManager();
		
		// pm.registerEvents(new WorldListener(this), this);

		this.getCommand("auction").setExecutor(new CommandAuction());
		this.getCommand("market").setExecutor(new CommandAuction());

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
}
