package cc.isotopestudio.Auction.gui;

import cc.isotopestudio.Auction.command.CommandAuction;
import cc.isotopestudio.Auction.data.Data;
import cc.isotopestudio.Auction.utli.DataLocationType;
import cc.isotopestudio.Auction.utli.S;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShelfGUI extends GUI implements Listener {

    public ShelfGUI(Player player, int page, Plugin plugin) {
        super(getName(S.toBoldPurple("你的上架商品 ") + S.toGray(" 第 " + (page + 1) + " 页")), 9, player, plugin);
        this.page = page;
        slotIDMap = new HashMap<>();
        setOption(0, new ItemStack(Material.ARROW), S.toBoldGold("上一页"), S.toRed("第 " + (page + 1) + " 页"));
        setOption(8, new ItemStack(Material.ARROW), S.toBoldGold("下一页"), S.toRed("第 " + (page + 1) + " 页"));
        int index = -1;
        int pos = 1;
        final int prerun = page * 7;
        ArrayList<Integer> indexList = Data.getResult(DataLocationType.MARKET, player, prerun, 7);
        int count = 0;
        while (count < indexList.size() && pos < 8) {
            index = indexList.get(count);
            count++;
            ItemStack item = Data.getItem(index, DataLocationType.MARKET);
            if (item == null) {
                continue;
            } else {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                lore.add(S.toGray("-------- (" + index + ") --------"));
                double price = Data.getMarketPrice(index);
                if (price > 0)
                    lore.add(S.toAqua("价格:   ") + S.toGreen(price + ""));
                else {
                    lore.add(S.toAqua("价格:   ") + S.toRed(-price + " 点卷"));
                }
                lore.add(S.toAqua("剩余:   ") + S.toGreen(Data.getMarketRemainDate(index)));
                lore.add(S.toYellow("Shift+右键 下架！"));
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

    private int getTotalPage() {
        int size = Data.getItemSize(DataLocationType.MARKET, player);
        int page = size / (7 * 1);
        if (size % (7 * 1) != 0)
            page++;
        return page;
    }

    private void onNextPage(OptionClickEvent e) {
        e.setWillClose(true);
        final Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(CommandAuction.plugin, new Runnable() {
            public void run() {
                (new ShelfGUI(player, page + 1, CommandAuction.plugin)).open(player);
            }
        }, 2);
    }

    private void onPreviousPage(OptionClickEvent e) {
        e.setWillClose(true);
        final Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(CommandAuction.plugin, new Runnable() {
            public void run() {
                (new ShelfGUI(player, page - 1, CommandAuction.plugin)).open(player);
            }
        }, 2);
    }

    private void onUnshelfItem(OptionClickEvent e, int slot) {
        e.setWillClose(true);
        int index = slotIDMap.get(slot);
        Data.storeItemIntoMail(player.getName(), Data.getItem(index, DataLocationType.MARKET));
        Data.removeItem(index, DataLocationType.MARKET);
        player.sendMessage(S.toPrefixGreen("成功下架"));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name) && playerName.equals(event.getWhoClicked().getName())) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            OptionClickEvent e = null;
            if (slot < 0) {
                return;
            } else if (slot > 8) {
                return;
            } else if (optionIcons[slot] != null) {
                e = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionNames[slot]);
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
                    if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                        onUnshelfItem(e, slot);
                    }
                }
            } else {
                return;
            }

            // handler[slot].onOptionClick(e);
            if (e.willClose()) {
                final Player p = (Player) event.getWhoClicked();
                p.closeInventory();
            }

        }

    }

}
