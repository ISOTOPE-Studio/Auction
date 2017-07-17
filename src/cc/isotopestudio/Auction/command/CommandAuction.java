package cc.isotopestudio.Auction.command;

import cc.isotopestudio.Auction.data.Data;
import cc.isotopestudio.Auction.gui.MailGUI;
import cc.isotopestudio.Auction.gui.MarketGUI;
import cc.isotopestudio.Auction.gui.ShelfGUI;
import cc.isotopestudio.Auction.settings.Settings;
import cc.isotopestudio.Auction.utli.DataLocationType;
import cc.isotopestudio.Auction.utli.S;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandAuction implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("auction")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(S.toPrefixRed("必须要玩家才能执行"));
                sendHelp(sender, label);
                return true;
            }
            Player player = (Player) sender;

            if (args.length > 0 && !args[0].equalsIgnoreCase("help")) {
                if (args[0].equalsIgnoreCase("market")) {
                    (new MarketGUI(player, 0)).open(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("mail")) {
                    (new MailGUI(player, 0)).open(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("shelf")) {
                    (new ShelfGUI(player, 0)).open(player);
                    return true;
                }
                if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("about")) {
                    player.sendMessage(S.toPrefixGreen("此服务器的定制插件 - Auction | 拍卖"));
                    player.sendMessage(S.toPrefixYellow("由ISOTOPE Studio(Mars Tan)制作"));
                    player.sendMessage(S.toPrefixGray("http://www.isotopestudio.cc/minecraft.html"));
                    return true;
                }
                try {
                    ItemStack item = player.getInventory().getItemInMainHand().clone();
                    if (Data.getItemSize(DataLocationType.MARKET, player) >= 20) {
                        player.sendMessage(S.toPrefixRed(
                                "你已经上架了" + Data.getItemSize(DataLocationType.MARKET, player) + "个物品，不能再多了"));
                        return true;
                    }
                    if (item.getType().equals(Material.AIR)) {
                        player.sendMessage(S.toPrefixRed("手中没有物品"));
                        return true;
                    }
                    if (Settings.isBlocked(item)) {
                        player.sendMessage(S.toPrefixRed("此物品无法上架"));
                        return true;
                    }
                    double price;
                    if (args[0].startsWith("p")) {
                        price = Integer.parseInt(args[0].substring(1));
                        Data.storeItemIntoMarket(player.getName(), item, -price);
                    } else {
                        price = Double.parseDouble(args[0]);
                        if (price <= 0 || price > 10000000) {
                            player.sendMessage(S.toPrefixRed("这不是有效的数字请再试 (0~10000000)"));
                            return true;
                        }
                        Data.storeItemIntoMarket(player.getName(), item, price);
                    }
                    player.getInventory().setItemInMainHand(null);
                    player.sendMessage(
                            S.toPrefixGreen("成功上架, " + "现在已经上架了" + Data.getItemSize(DataLocationType.MARKET, player))
                                    + "个物品");
                    return true;
                } catch (Exception ignored) {
                }
                sendHelp(player, label);
                return true;
            } else {
                sendHelp(player, label);
                return true;
            }
        }
        return false;
    }

    private void sendHelp(CommandSender player, String label) {
        player.sendMessage(S.toPrefixGreen("帮助菜单"));
        player.sendMessage(S.toBoldGreen("/" + label + " <价格> 上架商品(货币: 游戏币)"));
        player.sendMessage(S.toBoldGreen("/" + label + " p<价格> 上架商品(货币: 点卷)"));
        player.sendMessage(S.toBoldGreen("/" + label + " market 查看拍卖行"));
        player.sendMessage(S.toGold("这里能购买(双击)所有人拍卖的物品"));
        player.sendMessage(S.toBoldGreen("/" + label + " mail 查看玩家邮箱"));
        player.sendMessage(S.toGold("这里查看你的邮件"));
        player.sendMessage(S.toBoldGreen("/" + label + " shelf 查看玩家上架的商品"));
        player.sendMessage(S.toGold("这里能上架/下架你拍卖的物品"));
        player.sendMessage(S.toBoldGreen("/" + label + " info 查看插件信息"));
    }

}
