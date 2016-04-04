package cc.isotopestudio.Auction.gui;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class MarketGUI extends GUI implements Listener{

	public MarketGUI(int page, Plugin plugin) {
		super("全球市场  第 " + (page + 1) + " 页", 9 * 6, plugin);
		setOption(0, new ItemStack(Material.ARROW), "上一页", "第 " + (page + 1) + " 页");
		setOption(8, new ItemStack(Material.ARROW), "下一页", "第 " + (page + 1) + " 页");
	}
	
	

}
