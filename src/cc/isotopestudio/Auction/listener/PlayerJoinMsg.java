package cc.isotopestudio.Auction.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import cc.isotopestudio.Auction.Auction;
import cc.isotopestudio.Auction.task.MailMsg;

public class PlayerJoinMsg implements Listener {

	private final Auction plugin;

	public PlayerJoinMsg(Auction plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				MailMsg.runForPlayer(e.getPlayer());
			}
		}, 40);
	}

}
