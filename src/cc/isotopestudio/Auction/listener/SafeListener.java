package cc.isotopestudio.Auction.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SafeListener implements Listener {

	@EventHandler
	public void onClickItem(PlayerInteractEvent event) {
		ItemStack item = event.getPlayer().getItemInHand();
		try {
			for (String lore : item.getItemMeta().getLore()) {
				if (lore.contains("Shift+ÓÒ¼ü") || lore.contains(" Ò³")) {
					event.getPlayer().setItemInHand(null);
				}
			}
		} catch (Exception e) {
		}
	}

	@EventHandler
	public void onClickInventory(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		if (inv.getTitle().contains(event.getWhoClicked().getName() + "µÄ") || inv.getTitle().contains("È«ÇòÊÐ³¡"))
			return;
		for (int i = 0; i < inv.getContents().length; i++) {
			ItemStack item = inv.getContents()[i];
			try {
				for (String lore : item.getItemMeta().getLore()) {
					if (lore.contains("Shift+ÓÒ¼ü") || lore.contains(" Ò³")) {
						inv.setItem(i, null);
					}
				}
			} catch (Exception e) {
			}

		}
	}
}
