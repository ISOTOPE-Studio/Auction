package cc.isotopestudio.Auction.gui;

import org.bukkit.Bukkit;
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

import java.util.Arrays;
import java.util.HashMap;

import static cc.isotopestudio.Auction.Auction.plugin;

public abstract class GUI implements Listener {

    // From: https://bukkit.org/threads/icon-menu.108342

    final String name;
    final int size;
    private OptionClickEventHandler[] handler;
    String[] optionNames;
    ItemStack[] optionIcons;
    int page;
    HashMap<Integer, Integer> slotIDMap;
    final Player player;
    final String playerName;
    private boolean isDestoryed = false;

    GUI(String name, int size, Player player) {
        this.name = name;
        this.size = size;
        this.player = player;
        playerName = player.getName();
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    void setOption(int position, ItemStack icon, String name, String... info) {
        optionNames[position] = name;
        optionIcons[position] = setItemNameAndLore(icon, name, info);
    }

    void setOption(int position, ItemStack item) {
        optionNames[position] = item.getItemMeta() == null ? item.getType().toString()
                : item.getItemMeta().getDisplayName();
        optionIcons[position] = item;
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

    private void Destory() {
        isDestoryed = true;
        HandlerList.unregisterAll(this);
        handler = null;
        plugin = null;
        optionNames = null;
        optionIcons = null;
    }

    public void setHandlerList(OptionClickEventHandler[] handler) {
        this.handler = handler;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(final InventoryClickEvent event) {
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getTitle().equals(name) && playerName.equals(event.getPlayer().getName())) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> Destory(), 0);
        }
    }

    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }

    static String getName(String a) {
        StringBuilder aBuilder = new StringBuilder(a);
        for (int i = 0; i <= 5; i++) {
            switch ((int) (Math.random() * 5)) {
                case (0): {
                    aBuilder.append("¡ìf");
                    break;
                }
                case (1): {
                    aBuilder.append("¡ì1");
                    break;
                }
                case (2): {
                    aBuilder.append("¡ì2");
                    break;
                }
                case (3): {
                    aBuilder.append("¡ì3");
                    break;
                }
                case (4): {
                    aBuilder.append("¡ì4");
                    break;
                }
            }
        }
        a = aBuilder.toString();
        return a;
    }

    private interface OptionClickEventHandler {
        void onOptionClick(OptionClickEvent event);
    }

    public class OptionClickEvent {
        private final Player player;
        private final int position;
        private final String name;
        private boolean close;
        private boolean destroy;

        public OptionClickEvent(Player player, int position, String name) {
            this.player = player;
            this.position = position;
            this.name = name;
            this.close = false;
            this.destroy = false;
        }

        public Player getPlayer() {
            return player;
        }

        public int getPosition() {
            return position;
        }

        public String getName() {
            return name;
        }

        public boolean willClose() {
            return close;
        }

        public boolean willDestroy() {
            return destroy;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }
}