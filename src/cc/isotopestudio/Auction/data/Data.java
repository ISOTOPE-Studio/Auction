package cc.isotopestudio.Auction.data;

import cc.isotopestudio.Auction.Auction;
import cc.isotopestudio.Auction.sql.SerializeItemStack;
import cc.isotopestudio.Auction.utli.DataLocationType;
import cc.isotopestudio.Auction.utli.S;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Data {

    public final static String moneyName = "!-!money!-!";

    public static void storeItemIntoMail(String playerName, ItemStack item) {
        StringBuilder string = new StringBuilder("insert into ");
        string.append("mail ");
        string.append("values(null,").append(storeItemPre(playerName, item)).append(", null);");
        try {
            Auction.statement.executeUpdate(string.toString());
        } catch (SQLException ignored) {

        }
    }

    public static void storeMoneyIntoMail(String playerName, String buyer, double money) {
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName(moneyName);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(S.toGold("你出售的物品"));
        lore.add(S.toGold("已被 " + buyer + " 购买!"));
        if (money > 0)
            lore.add(S.toAqua("金币:   ") + S.toGreen("" + money));
        else
            lore.add(S.toAqua("点卷:   ") + S.toGreen("" + -money));
        meta.setLore(lore);
        paper.setItemMeta(meta);
        StringBuilder string = new StringBuilder("insert into ");
        string.append("mail ");
        string.append("values(null,").append(storeItemPre(playerName, paper)).append(",").append(money).append(");");
        try {
            Auction.statement.executeUpdate(string.toString());
        } catch (SQLException ignored) {

        }
    }

    public static void storeItemIntoMarket(String playerName, ItemStack item, double money) {
        StringBuilder string = new StringBuilder("insert into ");
        string.append("market ");
        string.append("values(null, null,").append(money).append(",").append(storeItemPre(playerName, item)).append(");");
        try {
            Auction.statement.executeUpdate(string.toString());
        } catch (SQLException ignored) {

        }
    }

    public static void storeMsg(String playerName, String msg) {
        StringBuilder string = new StringBuilder("insert into ");
        string.append("msg ");
        string.append("values(null,\"").append(playerName).append("\", \"").append(msg).append("\");");
        try {
            Auction.statement.executeUpdate(string.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getMsg(String PlayerName) {
        ResultSet res;
        ArrayList<String> result;
        try {
            res = Auction.statement.executeQuery("select * from msg where player=\"" + PlayerName + "\";");
            result = new ArrayList<>();
            while (res.next()) {
                result.add(res.getString("msg"));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeMsg(String string) {
        try {
            Auction.statement.executeUpdate("delete from msg where player=\"" + string + "\";");
        } catch (SQLException ignored) {

        }
    }

    public static void removeItem(int id, DataLocationType type) {
        try {
            if (type.equals(DataLocationType.MAIL)) {
                Auction.statement.executeUpdate("delete from mail where id=" + id + ";");
            } else if (type.equals(DataLocationType.MARKET)) {
                Auction.statement.executeUpdate("delete from market where id=" + id + ";");
            }
        } catch (SQLException ignored) {

        }
    }

    private static String storeItemPre(String playerName, ItemStack item) {
        String itemString = SerializeItemStack.itemToStringBlob(item).replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + playerName + "\", \"" + itemString + "\"";
    }

    public static ItemStack getItem(int id, DataLocationType type) {
        ResultSet res = null;
        try {
            if (type.equals(DataLocationType.MAIL)) {
                res = Auction.statement.executeQuery("select * from mail where id=" + id + ";");
            } else if (type.equals(DataLocationType.MARKET)) {
                res = Auction.statement.executeQuery("select * from market where id=" + id + ";");
            }
            if (!res.next())
                return null;
            String itemString = res.getString("item");
            return SerializeItemStack.stringBlobToItem(itemString);
        } catch (SQLException ignored) {

        }
        return null;
    }

    public static String getMarketOwner(int id) {
        ResultSet res;
        try {
            res = Auction.statement.executeQuery("select * from market where id=" + id + ";");

            if (!res.next())
                return "";
            return res.getString("owner");
        } catch (SQLException ignored) {

        }
        return "";
    }

    public static double getMarketPrice(int id) {
        ResultSet res;
        try {
            res = Auction.statement.executeQuery("select * from market where id=" + id + ";");
            if (!res.next())
                return -1;
            return res.getDouble("money");
        } catch (SQLException ignored) {

        }
        return -1;
    }

    public static double getMailMoney(int id) {
        ResultSet res;
        try {
            res = Auction.statement.executeQuery("select * from mail where id=" + id + ";");
            if (!res.next())
                return -1;
            return res.getDouble("money");
        } catch (SQLException ignored) {

        }
        return -1;
    }

    private static Date getMarketDate(int id) {
        ResultSet res;
        try {
            res = Auction.statement.executeQuery("select * from market where id=" + id + ";");
            if (!res.next())
                return null;
            return res.getTimestamp("time");
        } catch (SQLException ignored) {

        }
        return null;
    }

    public static String getMarketRemainDate(int id) {
        Date time = getMarketDate(id);
        Date now = new Date();
        time.setTime(time.getTime() + 7 * 24 * 60 * 60 * 1000);
        long day;
        long hour;
        long min;
        long time1 = time.getTime();
        long time2 = now.getTime();
        long diff;
        if (time1 < time2) {
            return "timeout";
        } else {
            diff = time1 - time2;
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        return day + "天" + hour + "小时" + min + "分";
    }

    public static int getItemSize(DataLocationType type) {
        ResultSet res = null;
        try {
            if (type.equals(DataLocationType.MAIL)) {
                res = Auction.statement.executeQuery("select * from mail;");
            } else if (type.equals(DataLocationType.MARKET)) {
                res = Auction.statement.executeQuery("select * from market;");
            }
            res.last();
            return res.getRow();
        } catch (SQLException ignored) {

        }
        return 0;
    }

    public static int getItemSize(DataLocationType type, Player player) {
        ResultSet res = null;
        try {
            if (type.equals(DataLocationType.MAIL)) {
                res = Auction.statement.executeQuery("select * from mail where owner=\"" + player.getName() + "\";");
            } else if (type.equals(DataLocationType.MARKET)) {
                res = Auction.statement.executeQuery("select * from market where owner=\"" + player.getName() + "\";");
            }
            res.last();
            return res.getRow();
        } catch (SQLException ignored) {

        }
        return 0;
    }

    public static int getMarketRowID(int count) {
        ResultSet res;
        try {
            res = Auction.statement.executeQuery("select * from market;");
            for (int i = 0; i < count; i++)
                res.next();
            return res.getInt("id");
        } catch (SQLException ignored) {

        }
        return 0;
    }

    public static int getMarketMinID() {
        ResultSet res;
        try {
            res = Auction.statement.executeQuery("select * from market;");
            res.next();
            return res.getInt("id");
        } catch (SQLException ignored) {
        }
        return 0;
    }

    public static ArrayList<Integer> getResult(DataLocationType type, Player player, int prerun, int limit) {
        ArrayList<Integer> result = new ArrayList<>();
        ResultSet res = null;
        try {
            if (type.equals(DataLocationType.MAIL)) {
                res = Auction.statement.executeQuery("select * from mail where owner=\"" + player.getName() + "\";");
            } else if (type.equals(DataLocationType.MARKET)) {
                res = Auction.statement.executeQuery("select * from market where owner=\"" + player.getName() + "\";");
            }
            for (int i = 0; i < prerun; i++)
                res.next();
            int count = 0;
            while (res.next() && count < limit) {
                count++;
                result.add(res.getInt("id"));
            }
        } catch (SQLException ignored) {
        }
        return result;
    }

}
