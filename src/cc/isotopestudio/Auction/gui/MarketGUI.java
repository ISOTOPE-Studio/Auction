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
import cc.isotopestudio.Auction.data.Data;
import cc.isotopestudio.Auction.utli.DataLocationType;
import cc.isotopestudio.Auction.utli.S;

public class MarketGUI extends GUI implements Listener {

	public MarketGUI(int page, Plugin plugin) {
		super(S.toBoldDarkAqua("全球市场  第 " + (page + 1) + " 页"), 9 * 6, plugin);
		this.page = page;
		slotIDMap = new HashMap<Integer, Integer>();
		setOption(0, new ItemStack(Material.ARROW), S.toBoldGold("上一页"), S.toRed("第 " + (page + 1) + " 页"));
		setOption(8, new ItemStack(Material.ARROW), S.toBoldGold("下一页"), S.toRed("第 " + (page + 1) + " 页"));
		setOption(45, new ItemStack(Material.ARROW), S.toBoldGold("上一页"), S.toRed("第 " + (page + 1) + " 页"));
		setOption(53, new ItemStack(Material.ARROW), S.toBoldGold("下一页"), S.toRed("第 " + (page + 1) + " 页"));
		int size = Data.getItemSize(DataLocationType.MARKET);
		System.out.println(size);
		int index = Data.getMarketRowID(size - page * 6 * 7) + 1;
		int pos = 1;
		while (index > 0 && pos < 53) {
			index--;
			ItemStack item = Data.getItem(index, DataLocationType.MARKET);
			if (item == null) {
				continue;
			} else {
				ItemMeta meta = item.getItemMeta();
				List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();
				lore.add(S.toGray("-------- (" + index + ") --------"));
				lore.add(S.toAqua("售卖:   ") + S.toGreen(Data.getOwner(index, DataLocationType.MARKET)));
				lore.add(S.toAqua("价格:   ") + S.toGreen(Data.getMarketPrice(index) + ""));
				lore.add(S.toAqua("剩余:   ") + S.toGreen(Data.getMarketRemainDate(index)));
				lore.add(S.toYellow("双击购买！"));
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

	@SuppressWarnings("deprecation")
	void onBuyItem(OptionClickEvent e, int slot) {
		e.setWillClose(true);
		Player player = e.getPlayer();
		int index = slotIDMap.get(slot);
		if (Data.getOwner(index, DataLocationType.MARKET).equalsIgnoreCase(player.getName())) {
			player.sendMessage(S.toPrefixRed("不能购买自己的物品，请在展示柜中下架商品"));
			return;
		}
		double price = Data.getMarketPrice(index);
		if (price > Auction.econ.getBalance(player.getName())) {
			player.sendMessage(S.toPrefixRed("余额不足"));
			return;
		}
		Auction.econ.withdrawPlayer(player.getName(), price);
		String ownerName = Data.getOwner(index, DataLocationType.MARKET);
		Data.storeMoneyIntoMail(ownerName, price);
		if (Bukkit.getPlayer(ownerName) != null) {
			Bukkit.getPlayer(ownerName).sendMessage(S.toYellow("你的物品被购买，去邮箱中查看！"));
		} else {
			Data.storeMsg(ownerName, S.toYellow("你的物品被购买，去邮箱中查看！"));
		}
		Data.storeItemIntoMail(player.getName(), Data.getItem(index, DataLocationType.MARKET));
		Data.removeItem(index, DataLocationType.MARKET);
		player.sendMessage(S.toGreen("成功购买"));
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
						onBuyItem(e, slot);
					}
				}

				if (e != null && e.willClose()) {
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
