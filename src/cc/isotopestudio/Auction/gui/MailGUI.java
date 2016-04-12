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

public class MailGUI extends GUI implements Listener {

	private final Player player;

	public MailGUI(Player player, int page, Plugin plugin) {
		super(player.getName() + "的邮箱  第 " + (page + 1) + " 页", 9 * 3, plugin);
		this.player = player;
		this.page = page;
		setOption(9, new ItemStack(Material.ARROW), "上一页", "第 " + (page + 1) + " 页");
		setOption(17, new ItemStack(Material.ARROW), "下一页", "第 " + (page + 1) + " 页");
		int size = Data.getItemSizeID(DataLocationType.MAIL);
		System.out.println(size);
		int index = Data.getRowID(DataLocationType.MAIL, player, page * 3 * 7 + 1);
		int pos = 1;
		while (index <= size && pos < 26) {
			if (!Data.getOwner(index, DataLocationType.MAIL).equals(player.getName())) {
				index++;
				continue;
			}
			System.out.println(" " + index + " " + pos);
			ItemStack item = Data.getItem(index, DataLocationType.MAIL);
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
		int size = Data.getItemSize(DataLocationType.MAIL, player);
		int page = size / (7 * 3);
		if (size % (7 * 3) != 0)
			page++;
		return page;
	}

	void onNextPage(OptionClickEvent e) {
		e.setWillClose(true);
		final Player player = e.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				(new MailGUI(player, page + 1, plugin)).open(player);
			}
		}, 2);
	}

	void onPreviousPage(OptionClickEvent e) {
		e.setWillClose(true);
		final Player player = e.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				(new MailGUI(player, page - 1, plugin)).open(player);
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
				if (slot == 9) {
					if (page > 0)
						onPreviousPage(e);
					else
						return;
				} else if (slot == 17) {
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
