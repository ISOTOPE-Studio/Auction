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

public class ShelfGUI extends GUI implements Listener {

	private final Player player;

	public ShelfGUI(Player player, int page, Plugin plugin) {
		super(player.getName() + "的上架商品", 9, plugin);
		this.player = player;
		this.page = page;
		setOption(0, new ItemStack(Material.ARROW), "上一页", "第 " + (page + 1) + " 页");
		setOption(8, new ItemStack(Material.ARROW), "下一页", "第 " + (page + 1) + " 页");
		int size = Data.getItemSizeID(DataLocationType.MARKET);
		System.out.println(size);
		int index = Data.getRowID(DataLocationType.MARKET, player, page * 1 * 7 + 1);
		int pos = 1;
		while (index <= size && pos < 8) {
			if (!Data.getOwner(index, DataLocationType.MARKET).equals(player.getName())) {
				index++;
				continue;
			}
			System.out.println(" " + index + " " + pos);
			ItemStack item = Data.getItem(index, DataLocationType.MARKET);
			index++;
			if (item == null) {
				continue;
			} else {
				setOption(pos, item);
				pos++;
			}
			while (pos % 9 == 0 || pos % 9 == 8)
				pos++;
		}
	}

	int getTotalPage() {
		int size = Data.getItemSize(DataLocationType.MARKET, player);
		int page = size / (7 * 1);
		if (size % (7 * 1) != 0)
			page++;
		return page;
	}

	void onNextPage(OptionClickEvent e) {
		e.setWillClose(true);
		final Player player = e.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				(new ShelfGUI(player, page + 1, plugin)).open(player);
			}
		}, 2);
	}

	void onPreviousPage(OptionClickEvent e) {
		e.setWillClose(true);
		final Player player = e.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				(new ShelfGUI(player, page - 1, plugin)).open(player);
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
			if (/* handler[slot] != null && */optionNames[slot] != null) {
				System.out.println(event.getInventory().getTitle());
				OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionNames[slot]);
				if (slot == 0) {
					if (page > 0)
						onPreviousPage(e);
					else
						return;
				} else if (slot == 8) {
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
