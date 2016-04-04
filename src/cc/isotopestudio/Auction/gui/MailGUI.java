package cc.isotopestudio.Auction.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class MailGUI extends GUI implements Listener{

	public MailGUI(Player player, int page, Plugin plugin) {
		super(player.getName() + "的邮箱  第 " + (page + 1) + " 页", 9 * 3, plugin);
		setOption(0, new ItemStack(Material.ARROW), "上一页", "第 " + (page + 1) + " 页");
		setOption(8, new ItemStack(Material.ARROW), "下一页", "第 " + (page + 1) + " 页");
	}

}
