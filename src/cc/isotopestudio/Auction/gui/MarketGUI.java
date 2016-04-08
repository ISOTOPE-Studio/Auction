package cc.isotopestudio.Auction.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import cc.isotopestudio.Auction.data.Data;
import cc.isotopestudio.Auction.handler.DataLocationType;

public class MarketGUI extends GUI implements Listener {

	public MarketGUI(int page, Plugin plugin) {
		super("全球市场  第 " + (page + 1) + " 页", 9 * 6, plugin);
		this.page = page;
		setOption(0, new ItemStack(Material.ARROW), "上一页", "第 " + (page + 1) + " 页");
		setOption(8, new ItemStack(Material.ARROW), "下一页", "第 " + (page + 1) + " 页");
		setOption(45, new ItemStack(Material.ARROW), "上一页", "第 " + (page + 1) + " 页");
		setOption(53, new ItemStack(Material.ARROW), "下一页", "第 " + (page + 1) + " 页");
		int size = Data.getItemSizeID(DataLocationType.MARKET);
		System.out.println(size);
		int index = size - Data.getMarketRowID(page * 6 * 7 + 1) + 1;
		int pos = 1;
		while (index > 0 && pos < 53) {
			ItemStack item = Data.getItem(index, DataLocationType.MARKET);
			index--;
			if (item == null) {
				continue;
			} else {
				setOption(pos, item);
				pos++;
			}
			while (pos % 9 == 0 || pos % 9 == 8)
				pos++;
			System.out.println(" " + (index + 1) + " " + (pos - 1));
		}

	}

	int getTotalPage() {
		int size = Data.getItemSize(DataLocationType.MARKET);
		int page = size / (7 * 6);
		if (size % (7 * 6) != 0)
			page++;
		return page;
	}

	void onNextPage(OptionClickEvent e) {
		e.setWillClose(true);
		final Player player = e.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				(new MarketGUI(page + 1, plugin)).open(player);
			}
		}, 2);
	}

	void onPreviousPage(OptionClickEvent e) {
		e.setWillClose(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				(new MarketGUI(page - 1, plugin)).open(e.getPlayer());
			}
		}, 2);
	}

	void onClickItem(OptionClickEvent e, int slot) {
		e.setWillClose(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(final InventoryClickEvent event) {
		if (event.getInventory().getTitle().equals(name)) {
			event.setCancelled(true);
			int slot = event.getRawSlot();
			if (slot < 0 || slot >= size) {
				return;
			}
			if (/* handler[slot] != null && */optionIcons[slot] != null) {
				System.out.println(event.getInventory().getTitle());
				OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionNames[slot]);
				if (slot == 0 || slot == 45) {
					if (page > 0)
						onPreviousPage(e);
					else
						return;
				} else if (slot == 8 || slot == 53) {
					if (page < getTotalPage() - 1)
						onNextPage(e);
					else
						return;
				} else if (slot % 9 > 0 && slot % 9 < 8) {
					if (event.getClick().equals(ClickType.DOUBLE_CLICK)) {
						onClickItem(e, slot);
					}
				}
				// handler[slot].onOptionClick(e);
				if (e.willClose()) {
					final Player p = (Player) event.getWhoClicked();
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						public void run() {
							p.closeInventory();
						}
					}, 1);
				}
			}

		}
	}

}
