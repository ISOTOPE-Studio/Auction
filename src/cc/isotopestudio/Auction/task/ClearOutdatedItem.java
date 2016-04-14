package cc.isotopestudio.Auction.task;

import org.bukkit.scheduler.BukkitRunnable;

import cc.isotopestudio.Auction.data.Data;
import cc.isotopestudio.Auction.handler.DataLocationType;

public class ClearOutdatedItem extends BukkitRunnable {

	@Override
	public void run() {
		int index = Data.getMarketRowID(1);
		while (true) {
			if (Data.getMarketRemainDate(index).equals("timeout")) {
				String playerName = Data.getOwner(index, DataLocationType.MARKET);
				Data.storeItemIntoMail(playerName, Data.getItem(index, DataLocationType.MARKET));
				Data.removeItem(index, DataLocationType.MARKET);
			} else {
				break;
			}
		}
	}

}
