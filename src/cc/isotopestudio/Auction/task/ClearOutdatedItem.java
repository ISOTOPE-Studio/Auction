package cc.isotopestudio.Auction.task;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import cc.isotopestudio.Auction.data.Data;
import cc.isotopestudio.Auction.utli.DataLocationType;
import cc.isotopestudio.Auction.utli.S;

public class ClearOutdatedItem extends BukkitRunnable {

	@Override
	public void run() {
		int index = Data.getMarketRowID(1);
		while (true) {
			index = Data.getMarketRowID(1);
			if (index > 0 && Data.getMarketRemainDate(index).equals("timeout")) {
				String playerName = Data.getOwner(index, DataLocationType.MARKET);
				if (Bukkit.getPlayer(playerName) != null) {
					Bukkit.getPlayer(playerName).sendMessage(S.toPrefixYellow("你的物品已超时，请在邮箱中查看"));
				} else {
					Data.storeMsg(playerName, S.toPrefixYellow("你的物品已超时，请在邮箱中查看"));
				}
				Data.storeItemIntoMail(playerName, Data.getItem(index, DataLocationType.MARKET));
				Data.removeItem(index, DataLocationType.MARKET);
			} else {
				break;
			}
		}
	}

}
