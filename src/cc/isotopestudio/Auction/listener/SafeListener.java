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
				if (lore.contains("Shift+�Ҽ�") || lore.contains(" ҳ")) {
					event.getPlayer().setItemInHand(null);
				}
			}
		} catch (Exception ignored) {
		}
	}

	@EventHandler
	public void onClickInventory(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		if (inv.getTitle().contains("������� ") || inv.getTitle().contains("����ϼ���Ʒ ") || inv.getTitle().contains("ȫ���г�"))
			return;
		for (int i = 0; i < inv.getContents().length; i++) {
			ItemStack item = inv.getContents()[i];
			try {
				for (String lore : item.getItemMeta().getLore()) {
					if (lore.contains("Shift+�Ҽ�") || lore.contains(" ҳ")) {
						inv.setItem(i, null);
					}
				}
			} catch (Exception ignored) {
			}

		}
	}
}
