package cc.isotopestudio.Auction.gui;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class MarketGUI extends GUI implements Listener{

	public MarketGUI(int page, Plugin plugin) {
		super("ȫ���г�  �� " + (page + 1) + " ҳ", 9 * 6, plugin);
		setOption(0, new ItemStack(Material.ARROW), "��һҳ", "�� " + (page + 1) + " ҳ");
		setOption(8, new ItemStack(Material.ARROW), "��һҳ", "�� " + (page + 1) + " ҳ");
	}
	
	

}
