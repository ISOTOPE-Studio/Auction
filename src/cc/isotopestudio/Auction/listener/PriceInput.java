package cc.isotopestudio.Auction.listener;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import cc.isotopestudio.Auction.data.Data;

public class PriceInput implements Listener {

	static HashMap<Player, ItemStack> map = new HashMap<Player, ItemStack>();

	public static boolean add(Player player, ItemStack item) {
		if (map.get(player) == null) {
			map.put(player, item);
			return true;
		} else
			return false;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		ItemStack item = map.get(player);
		double price = 0;
		if (item == null)
			return;
		event.setCancelled(true);
		try {
			price = Double.parseDouble(event.getMessage());
		} catch (Exception e) {
			player.sendMessage("这不是有效的数字请再试");
			return;
		}
		Data.storeItemIntoMarket(player, item, price);
		map.remove(player);
		player.sendMessage("成功上架");
	}
}
