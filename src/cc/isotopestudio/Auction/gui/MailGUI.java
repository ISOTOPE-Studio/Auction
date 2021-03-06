package cc.isotopestudio.Auction.gui;

import cc.isotopestudio.Auction.Auction;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static cc.isotopestudio.Auction.Auction.playerPoints;
import static cc.isotopestudio.Auction.Auction.plugin;

public class MailGUI extends GUI implements Listener {

    private final String moneyDisplayName = S.toYellow("金钱");

    public MailGUI(Player player, int page) {
        super(getName(S.toBoldDarkGreen("你的邮箱 ") + S.toGray(" 第 " + (page + 1) + " 页")),
                9 * 3, player);
        this.page = page;
        slotIDMap = new HashMap<>();
        setOption(9, new ItemStack(Material.ARROW), S.toBoldGold("上一页"), S.toRed("第 " + (page + 1) + " 页"));
        setOption(17, new ItemStack(Material.ARROW), S.toBoldGold("下一页"), S.toRed("第 " + (page + 1) + " 页"));
        int index;
        int pos = 1;
        final int prerun = page * 3 * 7;
        ArrayList<Integer> indexList = Data.getResult(DataLocationType.MAIL, player, prerun, 7 * 3);
        int count = 0;
        while (count < indexList.size() && pos < 26) {
            index = indexList.get(count);
            count++;
            ItemStack item = Data.getItem(index, DataLocationType.MAIL);
            if (item == null) {
                continue;
            } else {
                ItemMeta meta = item.getItemMeta();
                try {
                    if (meta.getDisplayName().equals(Data.moneyName)) {
                        meta.setDisplayName(moneyDisplayName);
                    }
                } catch (Exception ignored) {
                }
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                lore.add(S.toGray("-------- (" + index + ") --------"));
                lore.add(S.toYellow("Shift+右键 取回！"));
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
        int size = Data.getItemSize(DataLocationType.MAIL, player);
        int page = size / (7 * 3);
        if (size % (7 * 3) != 0)
            page++;
        return page;
    }

    private void onNextPage(OptionClickEvent e) {
        e.setWillClose(true);
        final Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                () -> new MailGUI(player, page + 1).open(player), 2);
    }

    private void onPreviousPage(OptionClickEvent e) {
        e.setWillClose(true);
        final Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                () -> new MailGUI(player, page - 1).open(player), 2);
    }

    @SuppressWarnings("deprecation")
    private void onClickItem(OptionClickEvent e, int slot) {
        e.setWillClose(true);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                () -> new MailGUI(player, page).open(player), 3);
        Player player = e.getPlayer();
        int index = slotIDMap.get(slot);
        ItemStack item = Data.getItem(index, DataLocationType.MAIL);
        if (item == null) {
            player.sendMessage(S.toPrefixRed("物品消失了额"));
            return;
        }
        boolean isMoney = false;
        try {
            isMoney = item.getItemMeta().getDisplayName().equals(Data.moneyName);
        } catch (Exception ignored) {
        }
        if (isMoney) {
            double money = Data.getMailMoney(index);
            if (money < 0) {
                playerPoints.getAPI().give(player.getName(), (int) -money);
                player.sendMessage(S.toPrefixGreen("成功领取点卷" + -money));
            } else {
                Auction.econ.depositPlayer(player.getName(), money);
                player.sendMessage(S.toPrefixGreen("成功领取金币" + money));
            }
        } else {
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(S.toPrefixRed("背包满了?"));
                return;
            }
            player.getInventory().addItem(item);
            player.sendMessage(S.toPrefixGreen("物品已存放至背包"));
        }
        Data.removeItem(index, DataLocationType.MAIL);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(final InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name) && playerName.equals(event.getWhoClicked().getName())) {
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
