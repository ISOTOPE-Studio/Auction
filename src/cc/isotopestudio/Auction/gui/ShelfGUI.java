package cc.isotopestudio.Auction.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ShelfGUI extends GUI implements Listener {

	public ShelfGUI(Player player, int page, Plugin plugin) {
		super(player.getName() + "���ϼ���Ʒ", 9, plugin);
		setOption(0, new ItemStack(Material.ARROW), "��һҳ", "�� " + (page + 1) + " ҳ");
		setOption(8, new ItemStack(Material.ARROW), "��һҳ", "�� " + (page + 1) + " ҳ");
	}
}
