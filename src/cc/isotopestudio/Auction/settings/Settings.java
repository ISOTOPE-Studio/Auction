package cc.isotopestudio.Auction.settings;
/*
 * Created by david on 2017/5/3.
 * Copyright ISOTOPE Studio
 */

import cc.isotopestudio.Auction.utli.S;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static cc.isotopestudio.Auction.Auction.config;

public abstract class Settings {

    private static Set<Material> materialblack;
    private static Set<String> loreblack;

    public static void init() {
        materialblack = new HashSet<>(config.getStringList("blacklist.item").stream()
                .map(Material::matchMaterial).filter(Objects::nonNull)
                .collect(Collectors.toList()));
        loreblack = new HashSet<>(config.getStringList("blacklist.lore").stream()
                .map(S::translate)
                .collect(Collectors.toList()));
        System.out.println(materialblack);
        System.out.println(loreblack);
    }

    public static boolean isBlocked(ItemStack item) {
        if (materialblack.contains(item.getType())) return true;
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            for (String s : item.getItemMeta().getLore()) {
                if (loreblack.contains(s)) return true;
            }
        }
        return false;
    }

}
