package cc.isotopestudio.Auction.gui;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public abstract class GUI implements Listener {

	// From: https://bukkit.org/threads/icon-menu.108342

	private String name;
	private int size;
	private int lorePos;
	private Plugin plugin;
	private String[] optionNames;
	private ItemStack[] optionIcons;
	private boolean ifFinished;

	public GUI(String name, int size, int lorePos, Plugin plugin) {
		this.name = name;
		this.size = size;
		this.lorePos = lorePos;
		this.plugin = plugin;
		this.optionNames = new String[size];
		this.optionIcons = new ItemStack[size];
		ifFinished = false;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public GUI setOption(int position, ItemStack icon, String name, String... info) {
		optionNames[position] = name;
		optionIcons[position] = setItemNameAndLore(icon, name, info);
		return this;
	}

	public void open(Player player) {
		Inventory inventory = Bukkit.createInventory(player, size, name);
		for (int i = 0; i < optionIcons.length; i++) {
			if (optionIcons[i] != null) {
				inventory.setItem(i, optionIcons[i]);
			}
		}
		player.openInventory(inventory);
	}

	public void Destory() {
		HandlerList.unregisterAll(this);
		plugin = null;
		optionNames = null;
		optionIcons = null;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	void onInventoryClick(final InventoryClickEvent event) {
		if (event.getInventory().getTitle().equals(name)) {
			int size = event.getRawSlot();
			int pos = event.getSlot();
			try {
				if (!(event.getCurrentItem().getType().equals(Material.EMERALD)
						&& event.getCursor().getType().equals(Material.AIR)
						|| event.getCursor().getType().equals(Material.EMERALD)
								&& event.getCurrentItem().getType().equals(Material.AIR))) {
					event.setCancelled(true);
					return;
				}
			} catch (Exception e) {
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	void onInventoryClose(InventoryCloseEvent event) {
		if (event.getInventory().getTitle().equals(name)) {
			Destory();
		}
	}

	private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		im.setLore(Arrays.asList(lore));
		item.setItemMeta(im);
		return item;
	}
}