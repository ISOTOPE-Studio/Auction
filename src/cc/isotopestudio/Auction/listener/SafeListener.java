package cc.isotopestudio.Auction.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SafeListener implements Listener {

	@EventHandler
	public void onClickItem(PlayerInteractEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		try {
			for (String lore : item.getItemMeta().getLore()) {
				if (lore.contains("Shift+右键") || lore.contains(" 页")) {
					event.getPlayer().setItemInHand(null);
				}
			}
		} catch (Exception ignored) {
		}
	}

	@EventHandler
	public void onClickInventory(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		if (inv.getTitle().contains("你的邮箱 ") || inv.getTitle().contains("你的上架商品 ") || inv.getTitle().contains("全球市场"))
			return;
		for (int i = 0; i < inv.getContents().length; i++) {
			ItemStack item = inv.getContents()[i];
			try {
				for (String lore : item.getItemMeta().getLore()) {
					if (lore.contains("Shift+右键") || lore.contains(" 页")) {
						inv.setItem(i, null);
					}
				}
			} catch (Exception ignored) {
			}

		}
	}
}
