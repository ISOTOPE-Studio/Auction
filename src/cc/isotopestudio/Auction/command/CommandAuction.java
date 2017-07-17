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
                sender.sendMessage(S.toPrefixRed("����Ҫ��Ҳ���ִ��"));
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
                    player.sendMessage(S.toPrefixGreen("�˷������Ķ��Ʋ�� - Auction | ����"));
                    player.sendMessage(S.toPrefixYellow("��ISOTOPE Studio(Mars Tan)����"));
                    player.sendMessage(S.toPrefixGray("http://www.isotopestudio.cc/minecraft.html"));
                    return true;
                }
                try {
                    ItemStack item = player.getInventory().getItemInMainHand().clone();
                    if (Data.getItemSize(DataLocationType.MARKET, player) >= 20) {
                        player.sendMessage(S.toPrefixRed(
                                "���Ѿ��ϼ���" + Data.getItemSize(DataLocationType.MARKET, player) + "����Ʒ�������ٶ���"));
                        return true;
                    }
                    if (item.getType().equals(Material.AIR)) {
                        player.sendMessage(S.toPrefixRed("����û����Ʒ"));
                        return true;
                    }
                    if (Settings.isBlocked(item)) {
                        player.sendMessage(S.toPrefixRed("����Ʒ�޷��ϼ�"));
                        return true;
                    }
                    double price;
                    if (args[0].startsWith("p")) {
                        price = Integer.parseInt(args[0].substring(1));
                        Data.storeItemIntoMarket(player.getName(), item, -price);
                    } else {
                        price = Double.parseDouble(args[0]);
                        if (price <= 0 || price > 10000000) {
                            player.sendMessage(S.toPrefixRed("�ⲻ����Ч������������ (0~10000000)"));
                            return true;
                        }
                        Data.storeItemIntoMarket(player.getName(), item, price);
                    }
                    player.getInventory().setItemInMainHand(null);
                    player.sendMessage(
                            S.toPrefixGreen("�ɹ��ϼ�, " + "�����Ѿ��ϼ���" + Data.getItemSize(DataLocationType.MARKET, player))
                                    + "����Ʒ");
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
        player.sendMessage(S.toPrefixGreen("�����˵�"));
        player.sendMessage(S.toBoldGreen("/" + label + " <�۸�> �ϼ���Ʒ(����: ��Ϸ��)"));
        player.sendMessage(S.toBoldGreen("/" + label + " p<�۸�> �ϼ���Ʒ(����: ���)"));
        player.sendMessage(S.toBoldGreen("/" + label + " market �鿴������"));
        player.sendMessage(S.toGold("�����ܹ���(˫��)��������������Ʒ"));
        player.sendMessage(S.toBoldGreen("/" + label + " mail �鿴�������"));
        player.sendMessage(S.toGold("����鿴����ʼ�"));
        player.sendMessage(S.toBoldGreen("/" + label + " shelf �鿴����ϼܵ���Ʒ"));
        player.sendMessage(S.toGold("�������ϼ�/�¼�����������Ʒ"));
        player.sendMessage(S.toBoldGreen("/" + label + " info �鿴�����Ϣ"));
    }

}
