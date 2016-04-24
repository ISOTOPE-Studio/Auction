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

import cc.isotopestudio.Auction.Auction;
import cc.isotopestudio.Auction.command.CommandAuction;
import cc.isotopestudio.Auction.data.Data;
import cc.isotopestudio.Auction.utli.DataLocationType;
import cc.isotopestudio.Auction.utli.S;

public class MailGUI extends GUI implements Listener {

	private final String moneyDisplayName = S.toYellow("��Ǯ");

	public MailGUI(Player player, int page, Plugin plugin) {
		super(S.toBoldDarkGreen(player.getName() + "������ ") + S.toGray(" �� " + (page + 1) + " ҳ"), 9 * 3, player,
				plugin);
		this.page = page;
		slotIDMap = new HashMap<Integer, Integer>();
		setOption(9, new ItemStack(Material.ARROW), S.toBoldGold("��һҳ"), S.toRed("�� " + (page + 1) + " ҳ"));
		setOption(17, new ItemStack(Material.ARROW), S.toBoldGold("��һҳ"), S.toRed("�� " + (page + 1) + " ҳ"));
		int size = Data.getItemSizeID(DataLocationType.MAIL);
		int index = Data.getRowID(DataLocationType.MAIL, player, page * 3 * 7 + 1) - 1;
		int pos = 1;
		while (index <= size && pos < 26) {
			index++;
			if (!Data.getOwner(index, DataLocationType.MAIL).equals(player.getName())) {
				continue;
			}
			ItemStack item = Data.getItem(index, DataLocationType.MAIL);
			if (item == null) {
				continue;
			} else {
				ItemMeta meta = item.getItemMeta();
				try {
					if (meta.getDisplayName().equals(Data.moneyName)) {
						meta.setDisplayName(moneyDisplayName);
					}
				} catch (Exception e) {
				}
				List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
				lore.add(S.toGray("-------- (" + index + ") --------"));
				lore.add(S.toYellow("Shift+�Ҽ� ȡ�أ�"));
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
		int size = Data.getItemSize(DataLocationType.MAIL, player);
		int page = size / (7 * 3);
		if (size % (7 * 3) != 0)
			page++;
		return page;
	}

	void onNextPage(OptionClickEvent e) {
		e.setWillClose(true);
		final Player player = e.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(CommandAuction.plugin, new Runnable() {
			public void run() {
				(new MailGUI(player, page + 1, CommandAuction.plugin)).open(player);
			}
		}, 2);
	}

	void onPreviousPage(OptionClickEvent e) {
		e.setWillClose(true);
		final Player player = e.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(CommandAuction.plugin, new Runnable() {
			public void run() {
				(new MailGUI(player, page - 1, CommandAuction.plugin)).open(player);
			}
		}, 2);
	}

	@SuppressWarnings("deprecation")
	void onClickItem(OptionClickEvent e, int slot) {
		e.setWillClose(true);
		Player player = e.getPlayer();
		int index = slotIDMap.get(slot);
		ItemStack item = Data.getItem(index, DataLocationType.MAIL);
		boolean isMoney = false;
		try {
			isMoney = item.getItemMeta().getDisplayName().equals(Data.moneyName);
		} catch (Exception ex) {
		}
		if (isMoney) {
			double money = Data.getMailMoney(index);
			Auction.econ.depositPlayer(player.getName(), money);
			player.sendMessage(S.toPrefixGreen("�ɹ���ȡ���" + money));
		} else {
			if (player.getInventory().firstEmpty() == -1) {
				player.sendMessage(S.toPrefixRed("��������?"));
				return;
			}
			player.getInventory().addItem(item);
			player.sendMessage(S.toPrefixGreen("��Ʒ�Ѵ��������"));
		}
		Data.removeItem(index, DataLocationType.MAIL);

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(final InventoryClickEvent event) {
		if (event.getInventory().getTitle().equals(name)) {
			event.setCancelled(true);
			int slot = event.getRawSlot();
			if (slot < 0 || slot >= size) {
				return;
			}

			if (optionIcons[slot] != null) {
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
					if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
						onClickItem(e, slot);
					}
				}

				if (e.willClose()) {
					final Player p = (Player) event.getWhoClicked();
					p.closeInventory();
				}
			}

		}
	}

}
