package cc.isotopestudio.Auction.task;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import cc.isotopestudio.Auction.data.Data;
import cc.isotopestudio.Auction.utli.S;

public class MailMsg extends BukkitRunnable {

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		for (Player player : (List<Player>) Bukkit.getOnlinePlayers()) {
			runForPlayer(player);
		}

	}

	public static void runForPlayer(Player player) {
		ArrayList<String> result = Data.getMsg(player.getName());
		if (result == null)
			return;
		if (result.size() > 5) {
			player.sendMessage(S.toPrefixYellow("你有多条物品在邮箱内待处理"));
		} else {
			for (String msg : result) {
				player.sendMessage(msg);
			}
		}
		Data.removeMsg(player.getName());
	}

}
