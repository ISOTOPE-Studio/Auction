package cc.isotopestudio.Auction.gui;

import cc.isotopestudio.Auction.Auction;
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

public class MarketGUI extends GUI implements Listener {

    public MarketGUI(Player player, int page, Plugin plugin) {
        super(getName(S.toBoldDarkAqua("ȫ���г�  �� " + (page + 1) + " ҳ")), 9 * 6, player, plugin);
        this.page = page;
        slotIDMap = new HashMap<>();
        setOption(0, new ItemStack(Material.ARROW), S.toBoldGold("��һҳ"), S.toRed("�� " + (page + 1) + " ҳ"));
        setOption(8, new ItemStack(Material.ARROW), S.toBoldGold("��һҳ"), S.toRed("�� " + (page + 1) + " ҳ"));
        setOption(45, new ItemStack(Material.ARROW), S.toBoldGold("��һҳ"), S.toRed("�� " + (page + 1) + " ҳ"));
        setOption(53, new ItemStack(Material.ARROW), S.toBoldGold("��һҳ"), S.toRed("�� " + (page + 1) + " ҳ"));
        int size = Data.getItemSize(DataLocationType.MARKET);
        int index = Data.getMarketRowID(size - page * 6 * 7) + 1;
        int pos = 1;
        while (index > 0 && pos < 53) {
            index--;
            ItemStack item = Data.getItem(index, DataLocationType.MARKET);
            if (item == null) {
                continue;
            } else {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                lore.add(S.toGray("-------- (" + index + ") --------"));
                lore.add(S.toAqua("����:   ") + S.toGreen(Data.getMarketOwner(index)));
                double price = Data.getMarketPrice(index);
                if (price > 0)
                    lore.add(S.toAqua("�۸�:   ") + S.toGreen(price + ""));
                else {
                    lore.add(S.toAqua("�۸�:   ") + S.toRed(-price + " ���"));
                }
                lore.add(S.toAqua("ʣ��:   ") + S.toGreen(Data.getMarketRemainDate(index)));
                if (player.isOp())
                    lore.add(S.toRed("Shift+�Ҽ� �¼ܣ�"));
                else
                    lore.add(S.toYellow("Shift+�Ҽ� ����"));
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
        int size = Data.getItemSize(DataLocationType.MARKET);
        int page = size / (7 * 6);
        if (size % (7 * 6) != 0)
            page++;
        return page;
    }

    private void onNextPage(OptionClickEvent e) {
        e.setWillClose(true);
        final Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(CommandAuction.plugin, new Runnable() {
            public void run() {
                (new MarketGUI(player, page + 1, CommandAuction.plugin)).open(player);
            }
        }, 2);
    }

    private void onPreviousPage(OptionClickEvent e) {
        e.setWillClose(true);
        final Player player = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(CommandAuction.plugin, new Runnable() {
            public void run() {
                (new MarketGUI(player, page - 1, CommandAuction.plugin)).open(e.getPlayer());
            }
        }, 2);
    }

    @SuppressWarnings("deprecation")
    private void onBuyItem(OptionClickEvent e, int slot) {
        e.setWillClose(true);
        Player player = e.getPlayer();
        int index = slotIDMap.get(slot);
        if (Data.getItem(index, DataLocationType.MARKET) == null) {
            player.sendMessage(S.toPrefixRed("�����Ʒ�ѱ�����"));
            return;
        }
        if (player.isOp()) {
            String ownerName = Data.getMarketOwner(index);
            if (Bukkit.getPlayer(ownerName) != null) {
                Bukkit.getPlayer(ownerName).sendMessage(S.toPrefixYellow("�����Ʒ������Ա�¼ܣ�ȥ�����в鿴��"));
            } else {
                Data.storeMsg(ownerName, S.toPrefixYellow("�����Ʒ������Ա�¼ܣ�ȥ�����в鿴��"));
            }
            Data.storeItemIntoMail(ownerName, Data.getItem(index, DataLocationType.MARKET));
            Data.removeItem(index, DataLocationType.MARKET);
            player.sendMessage(S.toPrefixGreen("�ɹ��¼�"));
        } else {
            if (Data.getMarketOwner(index).equalsIgnoreCase(player.getName())) {
                player.sendMessage(S.toPrefixRed("���ܹ����Լ�����Ʒ������չʾ�����¼���Ʒ"));
                return;
            }
            double price = Data.getMarketPrice(index);
            if (price > Auction.econ.getBalance(player.getName())) {
                player.sendMessage(S.toPrefixRed("����"));
                return;
            }
            Auction.econ.withdrawPlayer(player.getName(), price);
            String ownerName = Data.getMarketOwner(index);
            Data.storeMoneyIntoMail(ownerName, player.getName(), price);
            if (Bukkit.getPlayer(ownerName) != null) {
                Bukkit.getPlayer(ownerName).sendMessage(S.toPrefixYellow("�����Ʒ������ȥ�����в鿴��"));
            } else {
                Data.storeMsg(ownerName, S.toPrefixYellow("�����Ʒ������ȥ�����в鿴��"));
            }
            Data.storeItemIntoMail(player.getName(), Data.getItem(index, DataLocationType.MARKET));
            Data.removeItem(index, DataLocationType.MARKET);
            player.sendMessage(S.toPrefixGreen("�ɹ�����"));
        }
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
                    if (event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                        onBuyItem(e, slot);
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
