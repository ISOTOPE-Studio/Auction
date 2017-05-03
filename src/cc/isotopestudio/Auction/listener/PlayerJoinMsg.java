package cc.isotopestudio.Auction.listener;

import cc.isotopestudio.Auction.task.MailMsg;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static cc.isotopestudio.Auction.Auction.plugin;

public class PlayerJoinMsg implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                () -> MailMsg.runForPlayer(e.getPlayer()), 40);
    }

}
