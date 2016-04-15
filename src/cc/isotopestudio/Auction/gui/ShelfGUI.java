package cc.isotopestudio.Auction.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import cc.isotopestudio.Auction.data.Data;
import cc.isotopestudio.Auction.listener.PriceInput;
import cc.isotopestudio.Auction.utli.DataLocationType;
import cc.isotopestudio.Auction.utli.S;

public class ShelfGUI extends GUI implements Listener {

	private final Player player;

	public ShelfGUI(Player player, int page, Plugin plugin) {
		super(S.toBoldPurple(player.getName() + "���ϼ���Ʒ"), 9, plugin);
		this.player = player;
		this.page = page;
		slotIDMap = new HashMap<Integer, Integer>();
		setOption(0, new ItemStack(Material.ARROW), S.toBoldGold("��һҳ"), S.toRed("�� " + (page + 1) + " ҳ"));
		setOption(8, new ItemStack(Material.ARROW), S.toBoldGold("��һҳ"), S.toRed("�� " + (page + 1) + " ҳ"));
		int size = Data.getItemSizeID(DataLocationType.MARKET);
		System.out.println(size);
		int index = Data.getRowID(DataLocationType.MARKET, player, page * 1 * 7 + 1) - 1;
		int pos = 1;
		while (index <= size && pos < 8) {
			index++;
			if (!Data.getOwner(index, DataLocationType.MARKET).equals(player.getName())) {
				continue;
			}
			ItemStack item = Data.getItem(index, DataLocationType.MARKET);
			if (item == null) {
				continue;
			} else {
				ItemMeta meta = item.getItemMeta();
				List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
				lore.add(S.toGray("-------- (" + index + ") --------"));
				lore.add(S.toAqua("�۸�:   ") + S.toGreen(Data.getMarketPrice(index) + ""));
				lore.add(S.toAqua("ʣ��:   ") + S.toGreen(Data.getMarketRemainDate(index)));
				lore.add(S.toYellow("˫���¼ܣ�"));
				meta.setLore(lore);
				item.setItemMeta(meta);
				setOption(pos, item);
				slotIDMap.put(pos, index);
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

	void onUnshelfItem(OptionClickEvent e, int slot) {
		e.setWillClose(true);
		int index = slotIDMap.get(slot);
		Data.storeItemIntoMail(player.getName(), Data.getItem(index, DataLocationType.MARKET));
		Data.removeItem(index, DataLocationType.MARKET);
		player.sendMessage(S.toPrefixGreen("�ɹ��¼�"));
	}

	void onUpshelfItem(OptionClickEvent e, ItemStack item) {
		e.setWillClose(true);
		Player player = e.getPlayer();
		player.sendMessage(S.toPrefixYellow("���������������۸�"));
		PriceInput.add(player, item);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(final InventoryClickEvent event) {
		if (event.getInventory().getTitle().equals(name)) {
			int slot = event.getRawSlot();
			System.out.println(event.getInventory().getTitle());
			System.out.println("slot: " + slot);
			OptionClickEvent e = null;
			if (slot < 0) {
				event.setCancelled(true);
				return;
			} else if (slot > 8) {
				if (!event.getCurrentItem().getType().equals(Material.AIR)
						&& event.getCursor().getType().equals(Material.AIR)) {
					// Upshelf step 1
					event.setCancelled(false);
					return;
				} else {
					return;
				}

			} else if (!event.getCursor().getType().equals(Material.AIR)) {
				// Upshelf step 2
				if (!PriceInput.ifAvailable(player)) {
					player.sendMessage(S.toPrefixRed("����δ��ɵ��ϼ�"));
					event.setCancelled(true);
					return;
				}
				event.setCancelled(false);
				ItemStack item = event.getCursor().clone();
				event.setCurrentItem(null);
				e = new OptionClickEvent((Player) event.getWhoClicked(), slot,
						item.getItemMeta() == null ? item.getType().toString() : item.getItemMeta().getDisplayName());
				onUpshelfItem(e, item);

			} else if (optionIcons[slot] != null) {
				e = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionNames[slot]);
				event.setCancelled(true);
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
						onUnshelfItem(e, slot);
					}
				}
			} else {
				return;
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
