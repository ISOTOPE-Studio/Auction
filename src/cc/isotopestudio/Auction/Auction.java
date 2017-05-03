package cc.isotopestudio.Auction;

import cc.isotopestudio.Auction.command.CommandAuction;
import cc.isotopestudio.Auction.listener.PlayerJoinMsg;
import cc.isotopestudio.Auction.listener.SafeListener;
import cc.isotopestudio.Auction.settings.Settings;
import cc.isotopestudio.Auction.sql.MySQL;
import cc.isotopestudio.Auction.sql.SqlManager;
import cc.isotopestudio.Auction.task.ClearOutdatedItem;
import cc.isotopestudio.Auction.task.MailMsg;
import cc.isotopestudio.Auction.utli.PluginFile;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.Statement;

public class Auction extends JavaPlugin {
    public static final String prefix = (new StringBuilder()).append(ChatColor.GOLD).append(ChatColor.BOLD).append("[")
            .append("����").append("]").append(ChatColor.GREEN).toString();
    public static final String pluginName = "Auction 1.2.6";

    // mySQL
    public static MySQL MySQL;
    public static Connection c;
    public static Statement statement;

    // Vault API
    public static Economy econ = null;

    // PlayerPoints API
    public static PlayerPoints playerPoints;

    // plugin
    public static Auction plugin;
    public static PluginFile config;

    private boolean hookPlayerPoints() {
        final Plugin plugin = this.getServer().getPluginManager().getPlugin("PlayerPoints");
        playerPoints = PlayerPoints.class.cast(plugin);
        return playerPoints != null;
    }

    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("����Vault API");
        if (!setupEconomy()) {
            getLogger().severe(pluginName + "�޷�����!");
            getLogger().severe("Vaultδ��װ��");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!hookPlayerPoints()) {
            getLogger().info("PlayerPoints �������");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("���������ļ���");
        config = new PluginFile(this, "config.yml", "config.yml");
        Settings.init();

        if (!SqlManager.connectMySQL()) {
            getLogger().severe(pluginName + "�޷�����!");
            getLogger().severe("���ݿ��޷����ӣ�");
            this.getPluginLoader().disablePlugin(this);
        }
        if (!SqlManager.createTables()) {
            getLogger().severe(pluginName + "�޷�����!");
            getLogger().severe("���ݿⴴ��ʧ�ܣ�");
            this.getPluginLoader().disablePlugin(this);
        }
        PluginManager pm = this.getServer().getPluginManager();

        pm.registerEvents(new PlayerJoinMsg(), this);
        if (getConfig().getBoolean("check", false))
            pm.registerEvents(new SafeListener(), this);
        this.getCommand("auction").setExecutor(new CommandAuction());

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
        config.reload();
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
