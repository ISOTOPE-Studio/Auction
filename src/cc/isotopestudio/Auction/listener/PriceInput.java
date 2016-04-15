package cc.isotopestudio.Auction.listener;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import cc.isotopestudio.Auction.data.Data;
import cc.isotopestudio.Auction.utli.S;

public class PriceInput implements Listener {

	static HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();

	public static void add(Player player, ItemStack item) {
		map.put(player.getName(), item);
	}

	public static boolean ifAvailable(Player player) {
		if (map.get(player.getName()) == null) {
			return true;
		} else
			return false;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		ItemStack item = map.get(player.getName());
		double price = 0;
		if (item == null)
			return;
		event.setCancelled(true);
		try {
			price = Double.parseDouble(event.getMessage());
		} catch (Exception e) {
			player.sendMessage(S.toPrefixRed("这不是有效的数字请再试"));
			return;
		}
		if (price <= 0) {
			player.sendMessage(S.toPrefixRed("这不是有效的数字请再试"));
			return;
		}
		Data.storeItemIntoMarket(player.getName(), item, price);
		map.remove(player.getName());
		player.sendMessage(S.toPrefixGreen("成功上架"));
	}
}
